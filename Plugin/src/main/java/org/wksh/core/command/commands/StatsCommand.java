package org.wksh.core.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.wksh.core.AnarchyCore;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Stream;

public class StatsCommand implements ICommandExecutor
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    public String description()
    {
        return "Shows the world statistics.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        AnarchyCore.run(() ->
        {
            long start = System.currentTimeMillis();
            
            double fileSize = Stream.of("./world/region", "./world_nether/DIM-1/region")
                    .flatMap(world -> Stream.of(Objects.requireNonNull(new File(world).listFiles())))
                    .filter(File::isFile)
                    .mapToLong(File::length)
                    .sum() / 1048576.0 / 1000.0;
            AnarchyCore.logger().info(String.format("Finished calculating world size in %dms", System.currentTimeMillis() - start));
            
            Calendar current = Calendar.getInstance();
            Calendar configDate = Calendar.getInstance();
            configDate.set(
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.year"),
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.month"),
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.day")
            );

            for (String message : AnarchyCore.plugin().configManager().config().getStringList("StatsMessage"))
            {
                String formattedMessage = message
                        .replace("%years%", String.valueOf(Math.max(current.get(Calendar.YEAR) - configDate.get(Calendar.YEAR), 0)))
                        .replace("%months%", String.valueOf(Math.max(current.get(Calendar.MONTH) - configDate.get(Calendar.MONTH), 0)))
                        .replace("%days%", String.valueOf(Math.max(current.get(Calendar.DAY_OF_MONTH) - configDate.get(Calendar.DAY_OF_MONTH), 0)))
                        .replace("%fileSize%", decimalFormat.format(fileSize))
                        .replace("%totalPlayers%", String.valueOf(Objects.requireNonNull(new File("world/players/").listFiles()).length));

                Common.sendMessage(sender, formattedMessage);
            }
        });
        return true;
    }
}
