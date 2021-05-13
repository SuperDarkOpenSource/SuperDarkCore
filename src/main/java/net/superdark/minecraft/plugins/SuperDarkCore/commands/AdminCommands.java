package net.superdark.minecraft.plugins.SuperDarkCore.commands;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import net.superdark.minecraft.plugins.SuperDarkCore.reflection.MinecraftCommandAnnotation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@MinecraftCommandAnnotation(CommandName = "sdadmin", Aliases = "")
public class AdminCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        SuperDarkCorePlugin.getInstance().getServer().broadcastMessage("Command");
        return true;
    }
}
