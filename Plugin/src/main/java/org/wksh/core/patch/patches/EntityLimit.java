package org.wksh.core.patch.patches;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.wksh.core.AnarchyCore;

import java.util.HashMap;
import java.util.Map;

public class EntityLimit implements Listener
{
    private final Map<Chunk, Integer> vehicleCountByChunk = new HashMap<>();

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event)
    {
        Vehicle vehicle = event.getVehicle();
        Chunk chunk = vehicle.getLocation().getBlock().getChunk();

        vehicleCountByChunk.put(chunk, vehicleCountByChunk.getOrDefault(chunk, 0) + 1);

        int maxVehiclesPerChunk = AnarchyCore.plugin().configManager().config().getInt("Patches.VehiclesPerChunk");
        if (vehicleCountByChunk.getOrDefault(chunk, 0) > maxVehiclesPerChunk)
        {
            removeVehiclesFromChunk(chunk);
        }
    }

    private void removeVehiclesFromChunk(Chunk chunk)
    {
        Entity[] entities = chunk.getEntities();

        for (Entity entity : entities)
        {
            if (entity instanceof Vehicle)
            {
                entity.remove();
            }
        }

        vehicleCountByChunk.remove(chunk);
    }
}
