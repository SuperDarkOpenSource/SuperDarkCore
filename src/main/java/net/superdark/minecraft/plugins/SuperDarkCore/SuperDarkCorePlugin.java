package net.superdark.minecraft.plugins.SuperDarkCore;

import org.bukkit.plugin.java.JavaPlugin;

public class SuperDarkCorePlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Keep our instance so we can use it later
        instance_ = this;
    }

    @Override
    public void onDisable()
    {
        // Don't leave memory leaking
        instance_ = null;
    }

    private static SuperDarkCorePlugin instance_ = null;
}
