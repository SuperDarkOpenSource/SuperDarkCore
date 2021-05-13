package net.superdark.minecraft.plugins.SuperDarkCore.reflection;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MinecraftCommandAnnotation
{
    String CommandName();
    String[] Aliases();
}
