package org.iceanarchy.core.mixin.mixins;

import me.devcody.uberbukkit.util.math.Vec3i;
import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.iceanarchy.core.common.events.InventoryEvent;

public class MixinEntityPlayer
{
    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {IInventory.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void a(CallbackInfo ci)
    {
        IInventory inventory = (IInventory) ci.getParameters()[0];
        Bukkit.getPluginManager().callEvent(
                new InventoryEvent(
                        InventoryEvent.InventoryType.OPEN,
                        InventoryEvent.ContainerType.valueOf(
                                inventory.getName().toUpperCase()
                        ),
                        inventory
                )
        );
    }

    @Inject(info = @MethodInfo(_class = EntityPlayer.class, name = "a", sig = {IInventory.class, Vec3i.class}, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void b(CallbackInfo ci)
    {
        IInventory inventory = (IInventory) ci.getParameters()[0];
        Bukkit.getPluginManager().callEvent(
                new InventoryEvent(
                        InventoryEvent.InventoryType.OPEN,
                        InventoryEvent.ContainerType.valueOf(
                                inventory.getName().toUpperCase()
                        ), inventory
                )
        );
    }
}

