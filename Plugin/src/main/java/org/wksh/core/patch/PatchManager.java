package org.wksh.core.patch;

import org.bukkit.plugin.java.JavaPlugin;
import org.wksh.core.AnarchyCore;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.Manager;
import org.wksh.core.patch.patches.*;

public class PatchManager extends Manager
{
    @Override
    public void init(JavaPlugin plugin)
    {
        AnarchyCore main = (AnarchyCore) plugin;
        main.registerListener(new Illegals());
        Common.bus().register(new Illegals());
        if (AnarchyCore.plugin().configManager().config().getInt("Patches.VehiclesPerChunk") != -1)
            main.registerListener(new EntityLimit());

        if (AnarchyCore.plugin().configManager().config().getBoolean("Patches.MinecartDupePatch"))
            main.registerListener(new MinecartDupe());

        if (AnarchyCore.plugin().configManager().config().getBoolean("Patches.SpawnTPPatch"))
            main.registerListener(new SpawnTP());

        if (AnarchyCore.plugin().configManager().config().getBoolean("Patches.NetherRoofPatch"))
            main.registerListener(new NetherRoof());

        if (AnarchyCore.plugin().configManager().config().getBoolean("Patches.NCBypassPatch"))
            main.registerListener(new NoCheat());

        if (AnarchyCore.plugin().configManager().config().getBoolean("Patches.NoFallPatch"))
            main.registerListener(new NoFall());
    }

    @Override
    public void onShutdown(JavaPlugin plugin)
    {
    }
}