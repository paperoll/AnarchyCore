package org.iceanarchy.core.mixin;

import lombok.Getter;
import me.txmc.rtmixin.RtMixin;
import org.bukkit.plugin.java.JavaPlugin;
import org.iceanarchy.core.mixin.mixins.MixinEntityMinecart;
import org.iceanarchy.core.mixin.mixins.MixinEntityPlayer;

import com.diogonunes.jcolor.Attribute;
import static com.diogonunes.jcolor.Ansi.colorize;

import java.lang.instrument.Instrumentation;

public class MixinMain {

    @Getter
    private JavaPlugin javaPlugin;

    @Getter
    private static MixinMain instance;

    public void init(JavaPlugin plugin) throws Throwable {
        instance = this;
        this.javaPlugin = plugin;
        plugin.getServer().getLogger().info(colorize("Initializing mixins...", Attribute.GREEN_TEXT(), Attribute.BOLD()));
        Instrumentation inst = RtMixin.attachAgent().orElseThrow(() -> new RuntimeException("Failed to attach agent"));
        plugin.getServer().getLogger().info(colorize(String.format("Successfully attached agent and got instrumentation instance: %s!", inst.getClass().getName()), Attribute.GREEN_TEXT(), Attribute.BOLD()));
        long start = System.currentTimeMillis();
        //Register your mixins here
        RtMixin.processMixins(MixinEntityPlayer.class);
        RtMixin.processMixins(MixinEntityMinecart.class);
//        RtMixin.processMixins(MixinMinecraftServer.class);
//        RtMixin.processMixins(MixinWorld.class);
        //---
        plugin.getServer().getLogger().info(colorize(String.format("Preformed all mixins in: %dms!", (System.currentTimeMillis() - start)), Attribute.GREEN_TEXT(), Attribute.BOLD()));
    }
}
