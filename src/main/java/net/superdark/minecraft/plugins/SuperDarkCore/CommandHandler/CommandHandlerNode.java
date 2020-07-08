package net.superdark.minecraft.plugins.SuperDarkCore.CommandHandler;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * This annotation will handle the methods that handle command "Nodes"
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandlerNode
{
    /**
     * Path represented by this function
     */
    @NotNull String Path = "";

    /**
     * TRUE when  this function wants to handle input itself (event when incorrect)
     * FALSE when this Node function wants the automatic input handling
     */
    Boolean PassInvalidParametersToNode = false;
}
