package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to annotate Classes for Command Handling
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command
{
    /**
     * The name of the command
     */
    String Name();

    /**
     * A comma seperated list of aliases for this command
     */
    String Aliases() default "";
}
