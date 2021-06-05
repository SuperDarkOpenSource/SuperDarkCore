package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GenericCommandExecutor implements CommandExecutor
{
    /**
     * Listens to all the given delegates
     * @param delegates The delegates to listen to
     */
    public GenericCommandExecutor(List<CommandDelegate> delegates)
    {
        _corePlugin = SuperDarkCorePlugin.getInstance();
        _playerService = _corePlugin.getPlayerService();

        for(CommandDelegate delegate : delegates)
        {
            addDelegate(delegate);
        }
    }

    /**
     * Called by bukkit when a sender invokes our command
     * @param sender The sender of the command
     * @param command The command that was issued
     * @param alias The alias the sender used
     * @param args The array of arguments that the sender invoked with
     * @return TRUE if we handled the command, FALSE otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        // Figure out what permission level the sender has
        CommandPermissionLevel sendersPermission = CommandPermissionLevel.ALL;
        if(sender instanceof ConsoleCommandSender)
        {
            sendersPermission = CommandPermissionLevel.CONSOLE_ONLY;
        }
        else if(sender instanceof Player)
        {
            sendersPermission = CommandPermissionLevel.PLAYERS_ONLY;

            Player player = (Player)sender;

            // Check if the player is a admin (as defined by our core plugin)
            if(_playerService.isAdmin(player))
            {
                sendersPermission = CommandPermissionLevel.ADMINS_ONLY;
            }
        }

        // The first parameter for our CommandHanders is always a CommandSender
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(sender);

        // Figure out what CommandNode the current args are referring too
        Node node = findNodeFromArgs(args, _root, 0, parameters);

        // Did the senders arguments match a command in our tree?
        if(node != null && node.delegate != null)
        {
            CommandPermissionLevel wantedPermissionLevel = node.permission;

            // Only invoke the command if the current sender has the correct permissions
            if(comparePermissions(wantedPermissionLevel, sendersPermission))
            {
                node.delegate.invoke(parameters.toArray());
            }
            else
            {
                // Send a message that they don't have enough permissions.
                sender.sendMessage(ChatColor.RED + "You currently have insufficient permissions, or not the right type " +
                        "of sender to execute this command");
            }

            // Tell bukkit that we handled the command
            return true;
        }

        // Tell bukkit that we didn't handle the command
        return false;
    }

    /**
     * Adds a delegate to our command tree
     * @param delegate
     */
    private void addDelegate(CommandDelegate delegate)
    {
        CommandHandler annotation = delegate.getAnnotation();

        if(delegate.getAnnotation().Path().isBlank())
        {
            _root.delegate = delegate;
            _root.permission = delegate.getAnnotation().PermissionLevel();
            return;
        }

        String[] path = annotation.Path().split("\\.");

        buildPath(path, 0, _root, delegate);
    }

    /**
     * Recursivly build the tree for a given CommandDelegate
     * @param path The path to build
     * @param n How deep are we (recursively)
     * @param node The parent node for this iteration
     * @param delegate The delegate that handles the end
     */
    private void buildPath(String[] path, int n, Node node, CommandDelegate delegate)
    {
        // Are we out of bounds?
        if(n > path.length - 1 || node == null || delegate == null)
        {
            return;
        }

        for(Node child : node.children)
        {
            if(child.path.equals(path[n]))
            {
                buildPath(path, n + 1, child, delegate);
                return;
            }
        }

        Node newChild = new Node();
        newChild.path = path[n];

        if(n == path.length - 1)
        {
            newChild.delegate = delegate;
            newChild.permission = delegate.getAnnotation().PermissionLevel();
        }

        node.children.add(newChild);

        buildPath(path, n + 1, newChild, delegate);
    }

    /**
     * Find the node for our executors args, also build the delegates argument list
     * @param args The sender's args
     * @param node The iteration's parent node
     * @param n What iteration are we on?
     * @param arguments List of objects to build (for the delegate).
     * @return a Node if a matching node was found, null otherwise
     */
    private Node findNodeFromArgs(String[] args, Node node, int n, ArrayList<Object> arguments)
    {
        // Base case is no more arguments to process
        if(n >= args.length)
        {
            return node;
        }

        String arg = args[n];

        // If there exists a child node with the exact name
        for(Node child : node.children)
        {
            if(child.path.equals(args[n]))
            {
                return findNodeFromArgs(args, child, n + 1, arguments);
            }
        }

        // Next check if we can convert arg into on of the special paths
        for(Node child : node.children)
        {
            if(!child.path.startsWith("{") || !child.path.endsWith("}"))
            {
                continue;
            }

            Object obj = convertArgToPathItem(child.path, arg);

            if(obj != null)
            {
                arguments.add(obj);

                return findNodeFromArgs(args, child, n + 1, arguments);
            }
        }

        // If there is no compatible child node, then the args have no possible handler
        return null;
    }

    /**
     * Given a path argument (special), see if you can convert it to a valid arg
     * @param path The path that describes the type
     * @param arg The arg to convert into the path type
     * @return The converted arg, null if it couldn't be converted
     */
    private Object convertArgToPathItem(String path, String arg)
    {
        Object result = null;

        path = path.replace("{", "").replace("}", "");
        switch(path)
        {
            case "String":
                result = arg;
                break;

            case "Player":

                if(_playerService.getOnlineUserNameMap().containsKey(arg))
                {
                    UUID playerID = _playerService.getOnlineUserNameMap().get(arg);

                    result = _corePlugin.getServer().getPlayer(playerID);
                }

                break;

            case "ItemStack":
                Material material = Material.getMaterial(arg);

                if(material != null)
                {
                    result = material;
                }

                break;

            case "Int":
                try
                {
                    result = Integer.parseInt(arg);
                }
                catch (Exception ignored) { }
                break;

            case "Float":
                try
                {
                    result = Float.parseFloat(arg);
                }
                catch (Exception ignored) { }
                break;
        }

        return result;
    }

    /**
     * Does the sender have the ability to call this node?
     * @param wanted The permission level that the node wants
     * @param current The permission level the sender currently has
     * @return TRUE if the sender can invoke this node, FALSE otherwise
     */
    private boolean comparePermissions(CommandPermissionLevel wanted, CommandPermissionLevel current)
    {
        // Console can only invoke console commands
        if(wanted == CommandPermissionLevel.CONSOLE_ONLY)
        {
            return current == CommandPermissionLevel.CONSOLE_ONLY;
        }

        return wanted.compareTo(current) <= 0;
    }

    private Node _root = new Node();
    private SuperDarkCorePlugin _corePlugin;
    private PlayerService _playerService;

    /**
     * Class to store our command nodes
     */
    private class Node
    {
        public String path = "";
        public CommandDelegate delegate = null;
        public CommandPermissionLevel permission = CommandPermissionLevel.ALL;
        public ArrayList<Node> children = new ArrayList<>();
    }
}
