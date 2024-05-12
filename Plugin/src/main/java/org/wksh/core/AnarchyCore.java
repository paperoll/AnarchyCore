package org.wksh.core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.wksh.core.chat.ChatManager;
import org.wksh.core.command.CommandManager;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.Manager;
import org.wksh.core.config.ConfigManager;
import org.wksh.core.logger.Logger;
import org.wksh.core.patch.PatchManager;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AnarchyCore extends JavaPlugin
{
    private static final Logger logger = Logger.getLogger("AnarchyCore");

    private static AnarchyCore plugin;


    private CommandManager commandManager;
    private ConfigManager configManager;
    private ChatManager chatManager;
    private List<Manager> managers;


    public static final long startTime = System.currentTimeMillis();

    public static void run(Runnable runnable)
    {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(AnarchyCore.plugin(), runnable);
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        configManager = new ConfigManager();
        this.loadMixins();
        logger.info("Initializing commands!");
        commandManager = new CommandManager();
        chatManager = new ChatManager();
        managers = new ArrayList<>();
        this.initializeManagers(chatManager, commandManager, new PatchManager());
        this.registerCommand("reloadconfig", (sender, command, label, args) ->
        {
            Common.sendMessage(sender, "&aReloaded core config.");
            return true;
        }, true, "rlconfig", "rlc", "rlconf");
    }

    private void initializeManagers(Manager... managers)
    {
        for (Manager manager : managers)
        {
            this.managers.add(manager);
            manager.init(this);
        }
    }

    private void loadMixins()
    {
        File mixinJar = new File("./", "mixins-temp.jar");
        if (mixinJar.exists()) mixinJar.delete();
        try
        {
            InputStream is = getClass().getClassLoader().getResourceAsStream("mixins.dat");
            if (is == null) throw new RuntimeException("The plugin jar is missing the mixins");
            Files.copy(is, mixinJar.toPath());
            URLClassLoader ccl = new URLClassLoader(new URL[]{mixinJar.toURI().toURL()});
            Class<?> mixinMainClass = Class.forName(String.format("%s.mixin.MixinMain", getClass().getPackage().getName()), true, ccl);
            Object instance = mixinMainClass.newInstance();
            Method mainM = instance.getClass().getDeclaredMethod("init", JavaPlugin.class);
            mainM.invoke(instance, this);
        } catch (Throwable t)
        {
            logger.error(String.format("Failed to load mixins due to %s. Please see the stacktrace below for more info", t.getClass().getName()));
            throw new RuntimeException(t);
        } finally
        {
            if (mixinJar.exists()) mixinJar.delete();
        }
    }

    @Override
    public void onDisable()
    {
    }

    public void registerListener(Listener listener)
    {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String name, CommandExecutor command, boolean setAlias, String... aliases)
    {
        try
        {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, this);
            if (setAlias) pluginCommand.setAliases(Arrays.asList(aliases));
            pluginCommand.setExecutor(command);
            Field f = CraftServer.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) f.get(Bukkit.getServer());
            commandMap.register(name, pluginCommand);
        } catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    public ChatManager chatManager()
    {
        return this.chatManager;
    }

    public CommandManager commandManager()
    {
        return this.commandManager;
    }

    public ConfigManager configManager()
    {
        return this.configManager;
    }

    public static Logger logger()
    {
        return logger;
    }

    public static AnarchyCore plugin()
    {
        return plugin;
    }
}
