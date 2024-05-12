package org.wksh.core.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.wksh.core.AnarchyCore;
import org.wksh.core.command.CommandManager;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.util.Map;

public class HelpCommand implements ICommandExecutor
{
    private final CommandManager manager;

    public HelpCommand(CommandManager manager)
    {
        this.manager = manager;
    }

    @Override
    public String description()
    {
        return "Gives details on available commands to players.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        for (Map.Entry<String, Object> entry : manager.commandMap().entrySet())
        {
            String helpMessage = AnarchyCore.plugin().configManager().config().getString("HelpMessage");

            String formattedMessage = helpMessage
                    .replace("%command%", entry.getKey())
                    .replace("%description%", manager.getDescription(entry.getValue()));
            Common.sendMessage(sender, formattedMessage);
        }
        return true;
    }
}
