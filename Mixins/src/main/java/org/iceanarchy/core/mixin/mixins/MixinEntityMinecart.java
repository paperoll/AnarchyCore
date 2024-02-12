package org.iceanarchy.core.mixin.mixins;

import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecart;
import org.bukkit.Bukkit;

public class MixinEntityMinecart
{
    @Inject(info = @MethodInfo(_class = EntityMinecart.class, name = "a", sig = {EntityHuman.class}, rtype = boolean.class), at = @At(pos = At.Position.LINE, line = 871))
    public static void mixinTick(CallbackInfo ci)
    {
//        Entity entity = (Entity) ci.getParameters()[0];
//        Bukkit.broadcastMessage(String.valueOf(entity));
//        System.out.println("meowmewoeowemwoewmeowmewoemwoewmeowmewoeewmeowmoewmeow");
//        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
    }
}

