package net.superdark.minecraft.plugins.SuperDarkCore.listeners;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.services.PlayerService;
import net.superdark.minecraft.plugins.SuperDarkCore.services.WebhookService;
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
     * @param playerService_ Cache of PlayerAPI used to call its functions.
     */
    public PlayerEvents(SuperDarkCorePlugin instance_, PlayerService playerService_)
    {
        instance_.getServer().getPluginManager().registerEvents(this, instance_);

        this.plugin_ = instance_;
        this.playerService_ = playerService_;
        this.webhookService_ = this.plugin_.getWebhookAPI();
        this.serverTag = this.plugin_.getConfig().getString("serverTag");

        //player can be connected before a plugin register, so check for players and add them.
        for (Player p : instance_.getServer().getOnlinePlayers())
        {
            playerService_.registerPlayer(p.getName(), p.getUniqueId());
        }

    }

    @EventHandler
    private void OnPlayerJoinEvent(PlayerJoinEvent e) throws MalformedURLException
    {
        Player player = e.getPlayer();
        playerService_.registerPlayer(player.getName(), player.getUniqueId());

        //For default webhook

        webhookService_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has joined the server.**", serverTag);
    }

    @EventHandler
    private void OnPlayerQuitEvent(PlayerQuitEvent e) throws MalformedURLException
    {
        playerService_.unregisterPlayer(e.getPlayer().getName());

        //For Default Webhook
        webhookService_.postDiscordMessage(getDiscordWebookURL(), "**[Server] " + e.getPlayer().getDisplayName() + " has left the server.**", serverTag);
    }

    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) throws MalformedURLException
    {
        webhookService_.postDiscordMessage(getDiscordWebookURL(), "**<" + e.getPlayer().getDisplayName() + ">**: " + e.getMessage(), serverTag);
    }

    @EventHandler
    private void onPlayerDeathEvent(PlayerDeathEvent e) throws MalformedURLException {
        webhookService_.postDiscordMessage(getDiscordWebookURL(), "**[Server] Player " + e.getEntity().getPlayer().getName() + " has died.**", serverTag);
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

    private PlayerService playerService_;

    private WebhookService webhookService_;

    private String serverTag;
}
