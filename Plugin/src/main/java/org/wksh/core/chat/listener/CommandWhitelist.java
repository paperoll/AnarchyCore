package org.wksh.core.chat.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.wksh.core.AnarchyCore;
import org.wksh.core.command.CommandManager;
import org.wksh.core.common.Common;

import java.util.List;

public class CommandWhitelist implements Listener
{

    private final CommandManager manager = AnarchyCore.plugin().commandManager();

    @EventHandler(priority = Event.Priority.Monitor)
    public void onAsyncChat(PlayerCommandPreprocessEvent event)
    {
        if (!event.getMessage().startsWith("/")) return;
        if (event.getPlayer().isOp()) return;
        if (event.getMessage().toLowerCase().startsWith("/kill"))
        {
            event.setCancelled(true);
            event.getPlayer().setHealth(0);
            return;
        }
        if (event.getMessage().split(" ")[0].replace("/", "").equalsIgnoreCase("register"))
        {
            List<String> firstJoinerMessage = AnarchyCore.plugin().configManager().config().getStringList("FirstJoinerMessage");

            for (String message : firstJoinerMessage)
            {
                Common.sendMessage(event.getPlayer(), message);
            }
        }
        if (!manager.isCommandValid(event.getMessage().split(" ")[0].replace("/", "")))
        {
            event.setCancelled(true);
            Common.sendMessage(event.getPlayer(), "&fUnknown command. Type \"help\" for help.");
        }
    }
}