package net.superdark.minecraft.plugins.SuperDarkCore;

import net.superdark.minecraft.plugins.SuperDarkCore.reflection.CommandReflection;
import net.superdark.minecraft.plugins.SuperDarkCore.registration.BaseSuperDarkPlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.services.*;
import net.superdark.minecraft.plugins.SuperDarkCore.listeners.PlayerEvents;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperDarkCorePlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Keep our instance so we can use it later
        instance_ = this;
        loadDefaultConfig();

        // Create our services.
        loadServices();

        //Register our events
        createEvents();
        registerCommands("net.superdark.minecraft.plugins.SuperDarkCore.commands", this); //Package location for SuperDarkCore commands.

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

    /**
     * Uses reflection to pull commands from a package location and register them.
     * Each command will be registered to their respective plugins.
     * @param packageLocation Full string pack location. For example: net.superdark.minecraft.plugins.SuperDarkCore.commands
     * @param plugin The plugin to register the commands for.
     */
    public void registerCommands(String packageLocation, JavaPlugin plugin)
    {
        for(Map.Entry<String, CommandExecutor> entry : CommandReflection.getCommands(packageLocation).entrySet())
        {
            if(entry.getKey() == null)
            {
                this.getLogger().severe("There was a command that name that was null, and it was not added to executable commands.");
                continue;
            }

            if(entry.getValue() == null)
            {
                this.getLogger().severe("The command passed was null. The command will not be registered. Re-check your classes are annotated correctly and extend CommandExecutor or contact an admin.");
                continue;
            }
            plugin.getCommand(entry.getKey()).setExecutor(entry.getValue());
        }
    }

    /**
     * Flushes all data saved to RAM to disk.
     */
    private void flush()
    {
        //Flush PlayerDataObjects to disk
        dataTrackerAPI_.flush();

        //Flush the logs
        loggerService_.flush();
    }

    //Getters
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

    public void registerPlugin(BaseSuperDarkPlugin plugin)
    {
        this.registeredPlugins.add(plugin);
    }

    private List<BaseSuperDarkPlugin> getRegisteredPlugins()
    {
        return this.registeredPlugins;
    }

    //Variables
    private static SuperDarkCorePlugin instance_ = null;

    private List<BaseSuperDarkPlugin> registeredPlugins = new ArrayList<>();

    private PlayerService playerService_;

    private TeleportService teleportService_;

    private LoggerService loggerService_;

    private FileConfiguration config;

    private DataTrackerService dataTrackerAPI_;

    private WebhookService webhookService_;
}
