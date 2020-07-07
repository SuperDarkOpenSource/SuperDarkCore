package net.superdark.minecraft.plugins.SuperDarkCore.CommandHandler;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation will mark classes that define a Command and its aliases
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler
{
    /**
     * What plugin owns this class?
     */
    @NotNull
    Class<?> PluginOwner = SuperDarkCorePlugin.class;

    /**
     * What is the command name as given on the command line?
     */
    @NotNull
    String CommandName = "";

    /**
     * Comma separated list of this command's aliases
     */
    @NotNull
    String CommandAliases = "";
}
