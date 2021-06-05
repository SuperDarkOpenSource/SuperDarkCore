package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Level;

/**
 * Utils for CommandReflection class. Callers outside of this plugin should be wary of using these methods.
 */
public class CommandReflection
{
    /**
     * For the given plugin, find each class annotated with Command and bind each static method marked with
     * CommandHandler. Will automatically register the commands with the given plugin.
     * @param plugin The plugin (either core or child) to register the commands with.
     * @param packageLocation The java package path to search
     */
    public static void RegisterCommands(JavaPlugin plugin, String packageLocation)
    {
        Reflections reflections = new Reflections(packageLocation);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Command.class);

        for(Class<?> c : classes)
        {
            Method[] methods = c.getMethods();
            ArrayList<CommandDelegate> delegates = new ArrayList<>();

            for(Method m : methods)
            {
                try
                {
                    delegates.add(new CommandDelegate(m.getAnnotation(CommandHandler.class), m));
                }
                catch (IllegalArgumentException e)
                {
                    plugin.getLogger().log(Level.SEVERE, "Method " + m.getName() +
                            " was not able to be build as a @Command handler. Reason: \n" + e.getMessage());
                }
            }

            // There were no valid commands in the interface
            if(delegates.size() == 0)
            {
                continue;
            }

            try
            {
                Command command = c.getAnnotation(Command.class);

                GenericCommandExecutor executor = new GenericCommandExecutor(delegates);

                plugin.getCommand(command.Name()).setExecutor(executor);

                if(!command.Aliases().isBlank())
                {
                    String[] commandAliases = command.Aliases().split(",");

                    for(String alias : commandAliases)
                    {
                        plugin.getCommand(alias).setExecutor(executor);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param path A CommandHandler path
     * @return The list of typed parameters that the path needs to execute
     */
    public static List<Class<?>> GetWantedTypesFromPath(String path)
    {
        ArrayList<Class<?>> result = new ArrayList<>();

        // Ignore a blank path
        if(path.isBlank())
        {
            return result;
        }

        String[] pathArray = path.split("\\.");

        for(String p : pathArray)
        {
            // We only care about parameter arguments (special)
            if(p.startsWith("{") && p.endsWith("}"))
            {
                p = p.replace("{", "").replace("}", "");

                Class<?> item = GetTypeFromStr(p);

                if(item != null)
                {
                    result.add(item);
                }
            }
        }

        return result;
    }

    /**
     * Check is a method's parameters meet the requirements set out in the CommandHandler path
     * @param path The path to check against
     * @param m The method to checl
     * @return TRUE if the method m meets the path's requirements, FALSE otherwise
     */
    public static boolean DoesMethodMeetPathReq(String path, Method m)
    {
        Parameter[] parameters = m.getParameters();
        List<Class<?>> wantedParams = GetWantedTypesFromPath(path);

        // We require the method to have at least the CommandSender arg AND
        // we also require having the same amount of parameters as the given path
        if(parameters.length == 0 || wantedParams.size() != parameters.length - 1)
        {
            return false;
        }

        // Check that the first arg is a CommandSender
        if(!parameters[0].getType().equals(CommandSender.class))
        {
            return false;
        }

        ArrayList<Class<?>> params = new ArrayList<>();

        // Get the given parameters into a list, ignoring the first element
        int paramCount = parameters.length;
        for(int i = 1; i < paramCount; i++)
        {
            params.add(parameters[i].getType());
        }

        // Check that the wanted param types and the given ones are the same
        paramCount = params.size();
        for(int i = 0; i < paramCount; i++)
        {
            if(!params.get(i).equals(wantedParams.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the type for the argument string.
     * @param t The string to check
     * @return the Class of the type if it matches, null otherwise
     */
    private static Class<?> GetTypeFromStr(String t)
    {
        Class<?> result = null;

        switch(t)
        {
            case "Player":
                result = Player.class;
                break;
            case "String":
                result = String.class;
                break;
            case "Int":
                result = int.class;
                break;
            case "Float":
                result = float.class;
                break;
            case "ItemStack":
                result = ItemStack.class;
                break;
        }

        return result;
    }
}
