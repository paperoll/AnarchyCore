package org.wksh.core.patch.patches;

import com.legacyminecraft.poseidon.event.PlayerReceivePacketEvent;
import com.legacyminecraft.poseidon.event.PlayerSendPacketEvent;
import net.minecraft.server.Packet10Flying;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpawnTP implements Listener
{
    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerReceivePacketEvent event)
    {
        if (event.getPacket() instanceof Packet10Flying)
        {
            Packet10Flying packet = (Packet10Flying) event.getPacket();

            if (Double.isNaN(packet.x) || Double.isNaN(packet.stance) || Double.isNaN(packet.y) || Double.isNaN(packet.z))
            {
                event.setCancelled(true);
            }
        }
    }
}
