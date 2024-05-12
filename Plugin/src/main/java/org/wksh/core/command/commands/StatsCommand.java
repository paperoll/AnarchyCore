package org.wksh.core.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.wksh.core.AnarchyCore;
import org.wksh.core.common.Common;
import org.wksh.core.common.boiler.interfaces.ICommandExecutor;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StatsCommand implements ICommandExecutor
{
    private double fileSize;

    @Override
    public String description()
    {
        return "Shows the world statistics.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        AnarchyCore.run(() -> {
            long start = System.currentTimeMillis();
            long size = 0L;
            List<String> list = new LinkedList<>();
            list.add("./world/region");
            list.add("./world_nether/DIM-1/region");
            for (String world : list) {
                File[] files = Objects.requireNonNull((new File(world)).listFiles());
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    }
                }
            }

            fileSize = (size / 1048576.0 / 1000.0);
            AnarchyCore.logger().info(String.format("Finished calculating world size in %sms", System.currentTimeMillis() - start));

            Calendar currentCalendar = Calendar.getInstance();
            int currentYear = currentCalendar.get(Calendar.YEAR);
            int currentMonth = currentCalendar.get(Calendar.MONTH);
            int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

            Calendar calendar = Calendar.getInstance();
            calendar.set(
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.year"),
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.month"),
                    AnarchyCore.plugin().configManager().config().getInt("StatsInfo.day")
            );
            int year = currentYear - calendar.get(Calendar.YEAR);
            int month = currentMonth - calendar.get(Calendar.MONTH);
            int day =  currentDay - calendar.get(Calendar.DAY_OF_MONTH);
            if (year < 0) {
                year = 0;
                month = 0;
                day = 0;
            }

            List<String> statsMessages = AnarchyCore.plugin().configManager().config().getStringList("StatsMessage");

            for (String message : statsMessages)
            {
                String formattedMessage = message
                        .replace("%years%", String.valueOf(year))
                        .replace("%months%", String.valueOf(month))
                        .replace("%days%", String.valueOf(day))
                        .replace("%fileSize%", new DecimalFormat("#.##").format(this.fileSize))
                        .replace("%totalPlayers%", String.valueOf(Objects.requireNonNull(new File("world/players/").listFiles()).length));

                Common.sendMessage(sender, formattedMessage);
            }
        });
        return true;
    }
}
