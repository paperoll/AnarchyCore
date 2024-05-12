package org.wksh.core.patch.patches;

import com.legacyminecraft.poseidon.event.PlayerReceivePacketEvent;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.Packet101CloseWindow;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.wksh.core.AnarchyCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinecartDupe implements Listener
{
    private final Map<UUID, Integer> playerMinecartMap = new HashMap<>();

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() instanceof StorageMinecart)
        {
            this.playerMinecartMap.put(event.getPlayer().getUniqueId(), event.getRightClicked().getEntityId());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onVehicleMove(VehicleMoveEvent event)
    {
        if (event.getVehicle() instanceof StorageMinecart)
        {
            UUID playerUUID = this.findKey(this.playerMinecartMap, event.getVehicle().getEntityId());
            Player player = AnarchyCore.plugin().getServer().getPlayer(playerUUID);

            if (player != null && player.isOnline())
            {
                if (Math.sqrt(event.getVehicle().getLocation().distanceSquared(player.getLocation())) > 10.0)
                {
                    player.sendPacket(player, new Packet101CloseWindow(((CraftPlayer) player).getHandle().activeContainer.windowId));
                }
            }
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerReceivePacketEvent event)
    {
        if (event.getPacket() instanceof Packet101CloseWindow)
        {
            Player player = Bukkit.getPlayer(event.getUsername());
            Packet101CloseWindow packet = (Packet101CloseWindow) event.getPacket();

            if (packet.a == ((CraftPlayer) player).getHandle().activeContainer.windowId)
            {
                Integer minecartEntityId = this.playerMinecartMap.get(player.getUniqueId());
                if (minecartEntityId != null)
                {
                    this.playerMinecartMap.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onVehicleEnter(VehicleEnterEvent event)
    {
        if (((CraftPlayer) event.getEntered()).getHandle().activeContainer instanceof ContainerChest)
        {
            event.setCancelled(true);
        }
    }

    private UUID findKey(Map<UUID, Integer> map, Integer value)
    {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
