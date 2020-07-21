package net.superdark.minecraft.plugins.SuperDarkCore;

import net.superdark.minecraft.plugins.SuperDarkCore.api.DataTrackerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.api.LoggerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.api.PlayerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.api.TeleportAPI;
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
        destroyAPIs();

        //Flush the logs
        loggerAPI_.flush();
    }

    static SuperDarkCorePlugin getInstance()
    {
        return instance_;
    }

    public PlayerAPI getPlayerAPI()
    {
        return playerAPI_;
    }

    public TeleportAPI getTeleportAPI()
    {
        return teleportAPI;
    }

    public LoggerAPI getLoggerAPI_() {
        return loggerAPI_;
	}

    public DataTrackerAPI getDataTrackerAPI_() {
        return dataTrackerAPI_;
    }

    //getConfig is a FileConfiguration method
    public FileConfiguration getSuperDarkCoreConfig() {
        return config;
    }

    private void createAPIs()
    {
        playerAPI_ = new PlayerAPI(this);
        teleportAPI = new TeleportAPI(this);
        loggerAPI_ = new LoggerAPI(this);
        dataTrackerAPI_ = new DataTrackerAPI(this);
    }

    private void destroyAPIs()
    {
        playerAPI_ = null;
        teleportAPI = null;
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
        config.options().copyDefaults(true);
        saveConfig();
    }

    private static SuperDarkCorePlugin instance_ = null;

    private PlayerAPI playerAPI_;

    private TeleportAPI teleportAPI;

    private LoggerAPI loggerAPI_;

    private FileConfiguration config;

    private DataTrackerAPI dataTrackerAPI_;
}
