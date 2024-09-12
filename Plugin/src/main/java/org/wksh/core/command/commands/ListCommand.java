package org.wksh.core.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.wksh.core.AnarchyCore;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements ICommandExecutor
{
    @Override
    public String description()
    {
        return "Shows the current online players on the server.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        int playerCount = AnarchyCore.plugin().getServer().getOnlinePlayers().length;
        String playerList = Arrays.stream(AnarchyCore.plugin().getServer().getOnlinePlayers())
                .map(Player::getName)
                .distinct()
                .collect(Collectors.joining(", "));

        String listMessage = AnarchyCore.plugin().configManager().config().getString("ListMessage");

        String formattedMessage = listMessage
                .replace("%player_count%", playerCount == 1 ? "1 player" : playerCount + " players")
                .replace("%player_list%", playerList);

        Common.sendMessage(sender, formattedMessage);
        return true;
    }
}
