package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

import org.bukkit.command.CommandExecutor;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * For now, this reflection class will work. We can keep this and refactor it later for finer control over commands, while adding
 * a new way to create commands in the future if we need/want something more complicated.
 */
public class CommandReflection
{
    public static Map<String, CommandExecutor> getCommands(String packageLocation)
    {
        Reflections reflections = new Reflections(packageLocation);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(MinecraftCommandAnnotation.class);
        Map<String, CommandExecutor> commands = new HashMap<>();

        for(Class<?> cls : classes)
        {
            MinecraftCommandAnnotation annotation = cls.getAnnotation(MinecraftCommandAnnotation.class);

            try {
                commands.put(annotation.CommandName(), (CommandExecutor) cls.getConstructor().newInstance());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return commands;
    }
}
