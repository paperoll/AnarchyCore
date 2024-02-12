package org.iceanarchy.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.iceanarchy.core.common.Common;
import org.iceanarchy.core.listener.MinecartDupeFix;
import org.iceanarchy.core.logger.Logger;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;

public final class IceAnarchy extends JavaPlugin {

    public static final long startTime = System.currentTimeMillis();

    @Getter
    private static IceAnarchy instance;

    @Getter
    private Logger logger = Logger.getLogger("IceAnarchyCore");

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(IceAnarchy.getInstance(), runnable);
    }

    @Override
    public void onEnable() {
        instance = this;
        MinecartDupeFix vehicleListener = new MinecartDupeFix();
        getServer().getPluginManager().registerEvents(vehicleListener, this);
        loadMixins();
        logger.info("Initializing commands!");
        registerCommand("reloadconfig", (sender, command, label, args) -> {
            System.out.println("tester");
            Common.sendMessage(sender, "&aReloaded core config.");
            return true;
        }, true, "rlconfig", "rlc", "rlconf");
    }

    private void loadMixins() {
        File mixinJar = new File(".", "mixins-temp.jar");
        if (mixinJar.exists()) mixinJar.delete();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("mixins.dat");
            if (is == null) throw new RuntimeException("The plugin jar is missing the mixins");
            Files.copy(is, mixinJar.toPath());
            URLClassLoader ccl = new URLClassLoader(new URL[]{mixinJar.toURI().toURL()});
            Class<?> mixinMainClass = Class.forName(String.format("%s.mixin.MixinMain", getClass().getPackage().getName()), true, ccl);
            Object instance = mixinMainClass.newInstance();
            Method mainM = instance.getClass().getDeclaredMethod("init", JavaPlugin.class);
            mainM.invoke(instance, this);
        } catch (Throwable t) {
            logger.error(String.format("Failed to load mixins due to %s. Please see the stacktrace below for more info", t.getClass().getName()));
            t.printStackTrace();
        } finally {
            if (mixinJar.exists()) mixinJar.delete();
        }
    }

    @Override
    public void onDisable() {

    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String name, CommandExecutor command, boolean setAlias, String... aliases) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, this);
            if (setAlias) pluginCommand.setAliases(Arrays.asList(aliases));
            pluginCommand.setExecutor(command);
            Field f = CraftServer.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) f.get(Bukkit.getServer());
            commandMap.register(name, pluginCommand);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
