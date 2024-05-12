package org.wksh.core.patch.patches;

import com.legacyminecraft.poseidon.event.PlayerReceivePacketEvent;
import com.legacyminecraft.poseidon.event.PlayerSendPacketEvent;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoFall implements Listener
{
    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerReceivePacketEvent event)
    {
        if (event.getPacket() instanceof Packet10Flying || event.getPacket() instanceof Packet11PlayerPosition || event.getPacket() instanceof Packet12PlayerLook)
        {
            Packet10Flying packet = (Packet10Flying) event.getPacket();
            boolean isOnGround = this.isPlayerOffGround(Bukkit.getPlayer(event.getUsername()));

            if (packet.g != isOnGround)
            {
                packet.g = isOnGround;
            }
        }
    }

    private boolean isPlayerOffGround(Player player)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        AxisAlignedBB playerAABB = entityPlayer.boundingBox.clone().b(-0.01D, -0.01D, -0.01D).a(0.0D, -0.1D, 0.0D);
        WorldServer worldServer = entityPlayer.getWorldServer();
        return worldServer.b(playerAABB);
    }
}
