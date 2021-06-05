package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This should be annotated on a public static method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandHandler
{
    /**
     * The path of this command (using periods). See documentation and examples for more information.
     */
    String Path();

    /**
     * What permission level does the sender need?
     */
    CommandPermissionLevel PermissionLevel() default CommandPermissionLevel.ALL;
}
