package org.wksh.core.mixin.mixins;

import me.devcody.uberbukkit.util.math.Vec3i;
import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.*;
import org.wksh.core.common.Common;
import org.wksh.core.common.event.events.InventoryEvent;

public class MixinEntityPlayer
{
    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {IInventory.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void a(CallbackInfo ci)
    {
        IInventory inventory = (IInventory) ci.getParameters()[0];
        InventoryEvent event = new InventoryEvent(
                InventoryEvent.InventoryType.OPEN,
                InventoryEvent.ContainerType.valueOf(
                        inventory.getName().toUpperCase()
                ),
                inventory
        );

        Common.bus().post(event);
    }

    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {TileEntityFurnace.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void b(CallbackInfo ci)
    {
        TileEntityFurnace tif = (TileEntityFurnace) ci.getParameters()[0];
        InventoryEvent event = new InventoryEvent(
                InventoryEvent.InventoryType.OPEN,
                InventoryEvent.ContainerType.FURNACE,
                tif
        );

        Common.bus().post(event);
    }

    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {TileEntityDispenser.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void c(CallbackInfo ci)
    {
        TileEntityDispenser tid = (TileEntityDispenser) ci.getParameters()[0];
        InventoryEvent event = new InventoryEvent(
                InventoryEvent.InventoryType.OPEN,
                InventoryEvent.ContainerType.DISPENSER,
                tid
        );

        Common.bus().post(event);
    }

    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {IInventory.class, Vec3i.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void d(CallbackInfo ci)
    {
        IInventory inventory = (IInventory) ci.getParameters()[0];
        InventoryEvent event = new InventoryEvent(
                InventoryEvent.InventoryType.OPEN,
                InventoryEvent.ContainerType.valueOf(
                        inventory.getName().replace(" ", "_").toUpperCase()
                ),
                inventory
        );

        Common.bus().post(event);
    }
}