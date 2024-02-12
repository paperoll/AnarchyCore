package org.iceanarchy.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryTransactionEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.iceanarchy.core.common.events.InventoryEvent;

public class MinecartDupeFix implements Listener
{
    @EventHandler(priority = Event.Priority.Monitor)
    public void onInventory(InventoryEvent event)
    {
        System.out.println("1");
        if (event.getInventoryType().equals(InventoryEvent.InventoryType.OPEN))
        {
            System.out.println("2");
            System.out.println(event.getInventory().getName());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onInventoryTransaction(PlayerChatEvent event)
    {
        System.out.println(event.getMessage());
    }
}
