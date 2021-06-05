package net.superdark.minecraft.plugins.SuperDarkCore.reflection;

public enum CommandPermissionLevel
{
    /**
     * Only the Server's console can use this command.
     */
    CONSOLE_ONLY,

    /**
     * Anyone who can send a command, can use this command.
     */
    ALL,

    /**
     * Only players can use this command
     */
    PLAYERS_ONLY,

    /**
     * Only players who are SuperDarkCore admins and use this command
     */
    ADMINS_ONLY,
}
