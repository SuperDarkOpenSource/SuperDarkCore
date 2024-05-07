package net.superdark.minecraft.plugins.SuperDarkCore.commands;

import net.superdark.minecraft.plugins.SuperDarkCore.reflection.CommandHandler;
import net.superdark.minecraft.plugins.SuperDarkCore.reflection.Command;
import net.superdark.minecraft.plugins.SuperDarkCore.reflection.CommandPermissionLevel;
import net.superdark.minecraft.plugins.SuperDarkCore.services.TeleportService;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(Name = "test", Aliases = "t,t1")
public class TestCommand
{

    @CommandHandler(Path = "")
    public static void BaseTestCommand(CommandSender sender)
    {
        sender.sendMessage("This is the base Test Command!");
    }

    @CommandHandler(Path = "a.b", PermissionLevel = CommandPermissionLevel.ADMINS_ONLY)
    public static void BaseTest2(CommandSender sender)
    {
        sender.sendMessage("This is another test message!");
    }

    @CommandHandler(Path = "give.{Player}.{Int}")
    public static void GivePlayerItem(CommandSender sender, Player player, int amount)
    {
        sender.sendMessage("No");

        player.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
        player.sendMessage("Have " + amount + " diamonds!!!");
    }

    @CommandHandler(Path = "{Int}.{Int}", PermissionLevel = CommandPermissionLevel.PLAYERS_ONLY)
    public static void Test2(CommandSender sender, int a, int b) {
        for (int i = 0; i < a; i++) {
            sender.sendMessage("Hello World " + b);
        }
        if (!(sender instanceof Player player)) return;
        TeleportService.TeleportTypes.SafeTeleportAboveGround(player.getWorld(), player, 100.0, 100.0);
    }
}
