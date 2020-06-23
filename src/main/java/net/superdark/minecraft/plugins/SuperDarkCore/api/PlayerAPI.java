package net.superdark.minecraft.plugins.SuperDarkCore.api;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAPI
{
    public PlayerAPI(SuperDarkCorePlugin plugin)
    {
        superDarkCorePlugin_ = plugin;
    }

    /**
     * Adds a player and their Minecraft UUID to a map.
     * @param player A player's Username associated with their Minecraft account.
     * @param uuid Player's Minecraft UUID.
     */
    public void registerPlayer (String player, UUID uuid)
    {
        onlineUserNameMap.put(player, uuid);
    }

    /**
     *
     * @param player A player's Username associated with their Minecraft account.
     */
    public void unregisterPlayer (String player)
    {
        if (onlineUserNameMap.get(player) != null) {
            onlineUserNameMap.remove(player);
        }
    }

    public Map<String, UUID> getOnlineUserNameMap()
    {
        return this.onlineUserNameMap;
    }

    private Map<String, UUID> onlineUserNameMap = new HashMap<>();

    private SuperDarkCorePlugin superDarkCorePlugin_;

}
