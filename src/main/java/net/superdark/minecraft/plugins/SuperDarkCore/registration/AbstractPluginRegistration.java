package net.superdark.minecraft.plugins.SuperDarkCore.registration;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPluginRegistration extends JavaPlugin
{
    private SuperDarkCorePlugin corePlugin_;

    public AbstractPluginRegistration(String pluginName)
    {
        this.corePlugin_ = SuperDarkCorePlugin.getInstance();

        corePlugin_.registerPlugin(this);

        corePlugin_.getLoggerService().log("Child plugin '" + pluginName + "' was found and registered.");
    }

}
