package net.superdark.minecraft.plugins.SuperDarkCore.listeners;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.api.PlayerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.api.WebhookAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

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
        this.serverTag = this.plugin_.getConfig().getString("serverTag");

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

        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has joined the server.**", serverTag);
    }

    @EventHandler
    private void OnPlayerQuitEvent(PlayerQuitEvent e) throws MalformedURLException
    {
        playerAPI_.unregisterPlayer(e.getPlayer().getName());

        //For Default Webhook
        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has left the server.**", serverTag);
    }

    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) throws MalformedURLException
    {
        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**<" + e.getPlayer().getDisplayName() + ">**: " + e.getMessage(), serverTag);
    }

    @EventHandler
    private void onPlayerDeathEvent(PlayerDeathEvent e) throws MalformedURLException {
        webhookAPI_.postDiscordMessage(getDiscordWebookURL(), "**[Server] Player " + e.getEntity().getPlayer().getName() + " has died.**", serverTag);
    }

    private @Nullable URL getDiscordWebookURL() {
        URL url;
        try {
            url = new URL(Objects.requireNonNull(this.plugin_.getSuperDarkCoreConfig().getString("DiscordWebhook")));
        } catch (MalformedURLException e) {
            this.plugin_.getLogger().severe("You have provided a malformed Webhook URL. Please change it in the config.");
            return null;
        }


        return url;
    }

    private SuperDarkCorePlugin plugin_;

    private PlayerAPI playerAPI_;

    private WebhookAPI webhookAPI_;

    private String serverTag;
}
