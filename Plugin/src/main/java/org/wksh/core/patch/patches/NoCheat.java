package org.wksh.core.patch.patches;

import com.legacyminecraft.poseidon.event.PlayerReceivePacketEvent;
import net.minecraft.server.Packet19EntityAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoCheat implements Listener
{
    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerReceivePacketEvent event)
    {
        if (event.getPacket() instanceof Packet19EntityAction)
        {
            Packet19EntityAction packet = (Packet19EntityAction) event.getPacket();

            if (packet.animation == 3)
            {
                Player player = Bukkit.getPlayer(event.getUsername());
                if (player.hasBed())
                    return;

                event.setCancelled(true);
            }
        }
    }
}
