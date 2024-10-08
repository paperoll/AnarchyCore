package org.wksh.core.chat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.wksh.core.AnarchyCore;
import org.wksh.core.chat.ChatManager;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.util.Arrays;

public class ReplyCommand implements ICommandExecutor
{
    private final ChatManager manager = AnarchyCore.plugin().chatManager();

    @Override
    public String description()
    {
        return "Reply to the last person that messaged you.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            Player target = manager.getReplyTarget(player);
            if (target != null)
            {
                if (args.length > 0)
                {
                    manager.sendWhisper(player, target, String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
                } else
                {
                    Common.sendMessage(player, String.format("&c/%s <message>", label));
                }
            } else
            {
                Common.sendMessage(player, "&cYou have nobody to reply to");
            }
        }
        return true;
    }
}