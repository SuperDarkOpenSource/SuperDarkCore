package net.superdark.minecraft.plugins.SuperDarkCore;

import net.superdark.minecraft.plugins.SuperDarkCore.api.*;
import net.superdark.minecraft.plugins.SuperDarkCore.listeners.PlayerEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
        createAPIs();

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

    public PlayerAPI getPlayerAPI()
    {
        return playerAPI_;
    }

    public TeleportAPI getTeleportAPI_()
    {
        return teleportAPI_;
    }

    public LoggerAPI getLoggerAPI() {
        return loggerAPI_;
	}

    public DataTrackerAPI getDataTrackerAPI() {
        return dataTrackerAPI_;
    }

    public WebhookAPI getWebhookAPI() {
        return webhookAPI_;
    }

    //getConfig is a FileConfiguration method
    public FileConfiguration getSuperDarkCoreConfig() {
        return config;
    }

    private void createAPIs()
    {
        playerAPI_ = new PlayerAPI(this);
        teleportAPI_ = new TeleportAPI(this);
        loggerAPI_ = new LoggerAPI(this);
        dataTrackerAPI_ = new DataTrackerAPI(this);
        webhookAPI_ = new WebhookAPI(this);
    }

    private void destroyAPIs()
    {
        playerAPI_ = null;
        teleportAPI_ = null;
        loggerAPI_ = null;
    }

    private void createEvents()
    {
        new PlayerEvents(instance_, playerAPI_);
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
        loggerAPI_.flush();
    }

    private static SuperDarkCorePlugin instance_ = null;

    private PlayerAPI playerAPI_;

    private TeleportAPI teleportAPI_;

    private LoggerAPI loggerAPI_;

    private FileConfiguration config;

    private DataTrackerAPI dataTrackerAPI_;

    private WebhookAPI webhookAPI_;
}
