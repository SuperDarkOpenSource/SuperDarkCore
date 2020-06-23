package net.superdark.minecraft.plugins.SuperDarkCore;

import net.superdark.minecraft.plugins.SuperDarkCore.api.PlayerAPI;
import net.superdark.minecraft.plugins.SuperDarkCore.listeners.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperDarkCorePlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Keep our instance so we can use it later
        instance_ = this;

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
    }

    static SuperDarkCorePlugin getInstance()
    {
        return instance_;
    }

    public PlayerAPI getPlayerAPI()
    {
        return playerAPI_;
    }

    private void createAPIs()
    {
        playerAPI_ = new PlayerAPI(this);
    }

    private void destroyAPIs()
    {
        playerAPI_ = null;
    }

    private void createEvents()
    {
        new PlayerEvents(instance_, playerAPI_);
    }

    private static SuperDarkCorePlugin instance_ = null;

    private PlayerAPI playerAPI_;
}
