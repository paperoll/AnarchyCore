package org.wksh.core.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.wksh.core.chat.listener.CommandWhitelist;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.Manager;
import org.wksh.core.common.boiler.interfaces.IgnoreInfo;

import java.util.HashMap;

public class ChatManager extends Manager implements Listener, IgnoreInfo
{
    private final HashMap<Player, Player> replyTargets = new HashMap<>();

    @Override
    public void init(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getPluginManager().registerEvents(new CommandWhitelist(), plugin);
    }

    public void sendWhisper(Player sender, Player recipient, String message)
    {
        Common.sendMessage(sender, String.format("&dTo %s: %s", recipient.getName(), message));
        Common.sendMessage(recipient, String.format("&dFrom %s: %s", sender.getName(), message));
        setReplyTarget(recipient, sender);
    }

    public Player getReplyTarget(Player player)
    {
        return replyTargets.getOrDefault(player, null);
    }

    public void setReplyTarget(Player player, Player target)
    {
        if (target == null)
        {
            replyTargets.remove(player);
            return;
        }
        replyTargets.put(player, target);
    }

    @EventHandler(priority = Event.Priority.High)
    public void onLeave(PlayerQuitEvent event)
    {
        replyTargets.remove(replyTargets.get(event.getPlayer()));
    }

    @Override
    public void onShutdown(JavaPlugin plugin)
    {
    }
}
