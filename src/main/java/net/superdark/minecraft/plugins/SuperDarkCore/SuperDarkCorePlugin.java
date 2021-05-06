package net.superdark.minecraft.plugins.SuperDarkCore;

import net.superdark.minecraft.plugins.SuperDarkCore.registration.BaseSuperDarkPlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.services.*;
import net.superdark.minecraft.plugins.SuperDarkCore.listeners.PlayerEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SuperDarkCorePlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Keep our instance so we can use it later
        instance_ = this;

        //load the config
        loadDefaultConfig();

        // Create our APIs
        loadServices();

        //Register our events
        createEvents();

    }

    @Override
    public void onDisable()
    {
        // Don't leave memory leaking
        instance_ = null;

        //flush what needs to be flushed to disk
        flush();

        destroyAPIs();
    }

    public static SuperDarkCorePlugin getInstance()
    {
        return instance_;
    }

    public PlayerService getPlayerService()
    {
        return playerService_;
    }

    public TeleportService getTeleportService()
    {
        return teleportService_;
    }

    public LoggerService getLoggerService() {
        return loggerService_;
	}

    public DataTrackerService getDataTrackerService() {
        return dataTrackerAPI_;
    }

    public WebhookService getWebhookService() {
        return webhookService_;
    }

    //getConfig is a FileConfiguration method
    public FileConfiguration getSuperDarkCoreConfig() {
        return config;
    }

    private void loadServices()
    {
        playerService_ = new PlayerService(this);
        teleportService_ = new TeleportService(this);
        loggerService_ = new LoggerService(this);
        dataTrackerAPI_ = new DataTrackerService(this);
        webhookService_ = new WebhookService(this);
    }

    private void destroyAPIs()
    {
        playerService_ = null;
        teleportService_ = null;
        loggerService_ = null;
    }

    private void createEvents()
    {
        new PlayerEvents(instance_, playerService_);
    }


    private void loadDefaultConfig()
    {
        this.config = getConfig();
        config.addDefault("LOG_TICK_INTERVAL", 6000);
        config.addDefault("DisableDiscordWebhook", false);
        config.addDefault("DiscordWebhook", "WEBHOOK_HERE");
        config.addDefault("serverTag", "Change your server tag in the config.");
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void flush()
    {
        //Flush PlayerDataObjects to disk
        dataTrackerAPI_.flush();

        //Flush the logs
        loggerService_.flush();
    }

    public void registerPlugin(BaseSuperDarkPlugin plugin)
    {
        this.registeredPlugins.add(plugin);
    }

    private List<BaseSuperDarkPlugin> getRegisteredPlugins()
    {
        return this.registeredPlugins;
    }

    private static SuperDarkCorePlugin instance_ = null;

    private List<BaseSuperDarkPlugin> registeredPlugins;

    private PlayerService playerService_;

    private TeleportService teleportService_;

    private LoggerService loggerService_;

    private FileConfiguration config;

    private DataTrackerService dataTrackerAPI_;

    private WebhookService webhookService_;
}
