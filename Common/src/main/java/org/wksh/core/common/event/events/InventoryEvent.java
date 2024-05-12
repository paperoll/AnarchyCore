package org.wksh.core.common.event.events;

import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityFurnace;
import org.wksh.core.common.event.Event;

public class InventoryEvent extends Event
{
    private final InventoryType inventoryType;
    private ContainerType containerType;

    private TileEntityDispenser tileEntityDispenser;
    private TileEntityFurnace tileEntityFurnace;
    private IInventory inventory;

    public InventoryEvent(InventoryType inventoryType, ContainerType containerType, TileEntityDispenser tileEntityDispenser)
    {
        this.inventoryType = inventoryType;
        this.containerType = containerType;
        this.tileEntityDispenser = tileEntityDispenser;
    }

    public InventoryEvent(InventoryType inventoryType, ContainerType containerType, TileEntityFurnace tileEntityFurnace)
    {
        this.inventoryType = inventoryType;
        this.containerType = containerType;
        this.tileEntityFurnace = tileEntityFurnace;
    }

    public InventoryEvent(InventoryType inventoryType, ContainerType containerType, IInventory inventory)
    {
        this.inventoryType = inventoryType;
        this.containerType = containerType;
        this.inventory = inventory;
    }

    public InventoryEvent(InventoryType inventoryType)
    {
        this.inventoryType = inventoryType;
    }

    public InventoryType getInventoryType()
    {
        return this.inventoryType;
    }

    public ContainerType getContainerType()
    {
        return this.containerType;
    }

    public TileEntityDispenser getTileEntityDispenser()
    {
        return this.tileEntityDispenser;
    }

    public TileEntityFurnace getTileEntityFurnace()
    {
        return this.tileEntityFurnace;
    }

    public IInventory getInventory()
    {
        return this.inventory;
    }

    public enum InventoryType
    {
        OPEN,
        CLOSE;

        private InventoryType() {}
    }

    public enum ContainerType
    {
        LARGE_CHEST,
        DISPENSER,
        MINECART,
        FURNACE,
        CHEST;

        private ContainerType() {}
    }
}
