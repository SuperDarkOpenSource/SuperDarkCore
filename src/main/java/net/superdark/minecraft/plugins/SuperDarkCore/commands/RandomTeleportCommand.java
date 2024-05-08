package net.superdark.minecraft.plugins.SuperDarkCore.commands;

import net.superdark.minecraft.plugins.SuperDarkCore.reflection.Command;
import net.superdark.minecraft.plugins.SuperDarkCore.reflection.CommandHandler;
import net.superdark.minecraft.plugins.SuperDarkCore.services.TeleportService;
import net.superdark.minecraft.plugins.SuperDarkCore.util.Random;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.util.TimeZone;


@Command(Name = "rtp", Aliases = "randomteleport")
public class RandomTeleportCommand
{
    @CommandHandler(Path = "")
    public static void BaseTestCommand(CommandSender sender)
    {
        if(!(sender instanceof Player player))
        {
            sender.sendMessage("You need to be a player to use this command.");
            return;
        }
        sender.sendMessage("Teleporting...");
        TeleportService.TeleportTypes.SafeTeleportAboveGround(player.getWorld(), player, Random.RandomRange(-10000, 10000), Random.RandomRange(-10000, 10000));
    }
}
