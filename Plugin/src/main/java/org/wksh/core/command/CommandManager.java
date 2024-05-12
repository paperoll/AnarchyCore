package org.wksh.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.wksh.core.AnarchyCore;
import org.wksh.core.chat.commands.ReplyCommand;
import org.wksh.core.chat.commands.WhisperCommand;
import org.wksh.core.command.commands.HelpCommand;
import org.wksh.core.command.commands.ListCommand;
import org.wksh.core.command.commands.StatsCommand;
import org.wksh.core.common.boiler.Manager;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandManager extends Manager
{
    private final HashMap<String, Object> commandMap = new HashMap<>();

    @Override
    public void init(JavaPlugin plugin)
    {
        registerCommand("stats", new StatsCommand(), true, "worldstats", "stats");
        registerCommand("list", new ListCommand(), true, "online", "l");
        registerCommand("help", new HelpCommand(this), false);
        registerCommand("whisper", new WhisperCommand(), true, "w", "msg", "message", "tell", "t");
        registerCommand("reply", new ReplyCommand(), true, "r");
    }

    @Override
    public void onShutdown(JavaPlugin plugin)
    {
    }

    public boolean isCommandValid(String commandEntered)
    {
        if (commandEntered.equalsIgnoreCase("register") || commandEntered.equalsIgnoreCase("login") || commandEntered.equalsIgnoreCase("changepw"))
        {
            return true;
        }
        for (Map.Entry<String, Object> entry : commandMap.entrySet())
        {
            Command command = Bukkit.getPluginCommand(entry.getKey());
            if (command.getName().equalsIgnoreCase(commandEntered) || command.getAliases().contains(commandEntered))
            {
                return true;
            }
        }
        return false;
    }

    public String getDescription(Object cmd)
    {
        try
        {
            Method m = cmd.getClass().getDeclaredMethod("description");
            m.setAccessible(true);
            return (String) m.invoke(cmd);
        } catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    public void registerCommand(String name, ICommandExecutor executor, boolean setAlias, String... aliases)
    {
        AnarchyCore.plugin().registerCommand(name, executor::onCommand, setAlias, aliases);
        this.commandMap.put(name, executor);
    }

    public HashMap<String, Object> commandMap()
    {
        return this.commandMap;
    }
}
