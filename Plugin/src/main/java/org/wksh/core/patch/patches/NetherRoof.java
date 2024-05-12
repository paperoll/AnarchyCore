package org.wksh.core.patch.patches;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.wksh.core.common.Common;

import java.util.HashMap;
import java.util.UUID;

public class NetherRoof implements Listener
{
    private final HashMap<UUID, Long> lastDamageTime = new HashMap<>();

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER && event.getPlayer().getLocation().getY() >= 128)
        {
            long lastDamage = lastDamageTime.getOrDefault(event.getPlayer().getUniqueId(), 0L);

            if (System.currentTimeMillis() - lastDamage >= 500)
            {
                event.getPlayer().setHealth(Math.max(0, event.getPlayer().getHealth() - 1));
                Common.sendMessage(event.getPlayer(), "&cYou are being damaged by the nether roof!");
                lastDamageTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}