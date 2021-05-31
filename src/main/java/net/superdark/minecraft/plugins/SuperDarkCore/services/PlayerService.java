package net.superdark.minecraft.plugins.SuperDarkCore.services;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerService
{
    public PlayerService(SuperDarkCorePlugin plugin)
    {
        superDarkCorePlugin_ = plugin;
    }

    /**
     * Adds a player and their Minecraft UUID to a map. If the player is an admin, their player object will also be cached.
     * @param player A player's Username associated with their Minecraft account.
     * @param uuid Player's Minecraft UUID.
     */
    public void registerPlayer (String player, UUID uuid)
    {
        onlineUserNameMap.put(player.toLowerCase(Locale.ROOT), uuid);
        if (isAdmin(player))
        {
            registerAdmin(this.superDarkCorePlugin_.getServer().getPlayer(player));
        }
    }

    /**
     * Attempts to unregister a player from the list of online users and the list of admins.
     * @param p A player's string Username associated with their Minecraft account.
     */
    public void unregisterPlayer (String p)
    {
        if (onlineUserNameMap.get(p) != null) {
            onlineUserNameMap.remove(p);
        }

        Player player = superDarkCorePlugin_.getServer().getPlayer(onlineUserNameMap.get(p));
        if (adminPlayerObjectList.contains(player))
        {
            unregisterAdmin(player);
        }
    }

    /**
     * Registers a player to the list of admins on the server for quick access to their UUID.
     * @param player A player's Bukkit Player object.
     */
    public void registerAdmin(Player player)
    {
        this.adminPlayerObjectList.add(player);
    }

    /**
     * Unregisters an admin's player object from our cached list of admins.
     * @param player Bukkit player object to remove from list.
     */
    public void unregisterAdmin(Player player)
    {
        this.adminPlayerObjectList.removeIf(l -> l.equals(player));
    }

    /**
     *
     * @param player A Bukkit Player object.
     * @return TRUE if the player is a Bukkit Server Operator, or if they have the "superdark.admin" permission, FALSE otherwise.
     */
    public boolean isAdmin(Player player)
    {
        return player.isOp() || player.hasPermission("superdark.admin");
    }

    /**
     * This method assumes the parameter's casing matches the user's actual username.
     * @param s A Minecraft account's String username.
     * @return TRUE if player is an admin, FALSE if the player does not exist or is not an admin.
     */
    public boolean isAdmin(String s)
    {
        Player player = superDarkCorePlugin_.getServer().getPlayer(onlineUserNameMap.get(s));
        if(player == null) return false; // Player does not exist on the server currently, return false.
        return isAdmin(player);
    }

    public boolean isPlayerConnected(String name)
    {
        return this.onlineUserNameMap.containsKey(name);
    }

    /**
     * Gets a player object from online players.
     * @param playerName Player's in-game username.
     * @return Player Object if the user is online, NULL otherwise.
     */
    public Player getPlayer(String playerName)
    {
        if(isPlayerConnected(playerName))
        {
            return Bukkit.getPlayer(this.onlineUserNameMap.get(playerName));
        }
        return null;
    }

    /**
     * Gets a player's UUID only if they are online.
     * @param playerName Player's in-game name.
     * @return Player's UUID if it exists, NULL if it does not.
     */
    public UUID getPlayerUUID(String playerName)
    {
        return this.onlineUserNameMap.get(playerName);
    }

    @Deprecated
    public Map<String, UUID> getOnlineUserNameMap()
    {
        return this.onlineUserNameMap;
    }

    private Map<String, UUID> onlineUserNameMap = new HashMap<>();
    private ArrayList<Player> adminPlayerObjectList = new ArrayList<>();
    private SuperDarkCorePlugin superDarkCorePlugin_;

}
