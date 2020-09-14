package net.superdark.minecraft.plugins.SuperDarkCore.listeners;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.api.PlayerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.api.WebhookAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class PlayerEvents implements Listener {

    /**
     *
     * @param instance_ Plugin instance.
     * @param playerAPI_ Cache of PlayerAPI used to call its functions.
     */
    public PlayerEvents(SuperDarkCorePlugin instance_, PlayerAPI playerAPI_)
    {
        instance_.getServer().getPluginManager().registerEvents(this, instance_);

        this.plugin_ = instance_;
        this.playerAPI_ = playerAPI_;
        this.webhookAPI_ = this.plugin_.getWebhookAPI_();

        //player can be connected before a plugin register, so check for players and add them.
        for (Player p : instance_.getServer().getOnlinePlayers())
        {
            playerAPI_.registerPlayer(p.getName(), p.getUniqueId());
        }

    }

    @EventHandler
    private void OnPlayerJoinEvent(PlayerJoinEvent e) throws MalformedURLException
    {
        Player player = e.getPlayer();
        playerAPI_.registerPlayer(player.getName(), player.getUniqueId());

        //For default webhook

        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has joined the server.**");
    }

    @EventHandler
    private void OnPlayerQuitEvent(PlayerQuitEvent e) throws MalformedURLException
    {
        playerAPI_.unregisterPlayer(e.getPlayer().getName());

        //For Default Webhook
        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has left the server.**");
    }

    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) throws MalformedURLException
    {
        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**<" + e.getPlayer().getDisplayName() + ">**: " + e.getMessage());
    }

    private URL getDiscordWebookURL() throws MalformedURLException
    {

        return new URL(Objects.requireNonNull(this.plugin_.getSuperDarkCoreConfig().getString("DiscordWebhook")));
    }

    private SuperDarkCorePlugin plugin_;

    private PlayerAPI playerAPI_;

    private WebhookAPI webhookAPI_;
}
