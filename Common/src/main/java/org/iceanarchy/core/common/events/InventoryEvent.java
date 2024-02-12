package org.iceanarchy.core.common.events;

import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntityFurnace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class InventoryEvent extends Event implements Cancellable
{
    private boolean cancelled;

    private final InventoryType inventoryType;
    private final ContainerType containerType;
    private final IInventory inventory;

    public InventoryEvent(InventoryType inventoryType, ContainerType containerType, IInventory inventory)
    {
        super(Type.INVENTORY_CHANGE);
        this.inventoryType = inventoryType;
        this.containerType = containerType;
        this.inventory = inventory;
    }

    public InventoryType getInventoryType()
    {
        return this.inventoryType;
    }

    public ContainerType getContainerType()
    {
        return this.containerType;
    }

    public IInventory getInventory()
    {
        return this.inventory;
    }

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

    public enum InventoryType
    {
        OPEN,
        CLOSE;

        private InventoryType() {}
    }

    public enum ContainerType
    {
        MINECART,
        CHEST;

        private ContainerType() {}
    }
}
