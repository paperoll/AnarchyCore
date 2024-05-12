package org.wksh.core.common.boiler;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Manager
{
    public abstract void init(JavaPlugin plugin);

    public abstract void onShutdown(JavaPlugin plugin);
}