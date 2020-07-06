package net.superdark.minecraft.plugins.SuperDarkCore.listeners;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.api.PlayerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    /**
     *
     * @param instance_ Plugin instance.
     * @param playerAPI_ Cache of PlayerAPI used to call its functions.
     */
    public PlayerEvents(SuperDarkCorePlugin instance_, PlayerAPI playerAPI_)
    {
        instance_.getServer().getPluginManager().registerEvents(this, instance_);

        this.playerAPI_ = playerAPI_;

        //player can be connected before a plugin register, so check for players and add them.
        for (Player p : instance_.getServer().getOnlinePlayers())
        {
            playerAPI_.registerPlayer(p.getName(), p.getUniqueId());
        }
    }

    @EventHandler
    private void OnPlayerJoinEvent(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        playerAPI_.registerPlayer(player.getName(), player.getUniqueId());

    }

    @EventHandler
    private void OnPlayerQuitEvent(PlayerQuitEvent e)
    {
        playerAPI_.unregisterPlayer(e.getPlayer().getName());
    }

    private PlayerAPI playerAPI_;
}
