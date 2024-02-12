package org.iceanarchy.core.common;

import net.minecraft.server.Packet3Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.iceanarchy.core.common.boiler.interfaces.ScheduledTask;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Common {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(3);

    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    public static ChatColor getTPSColor(double tps) {
        if (tps >= 18.0D) {
            return ChatColor.GREEN;
        } else {
            return tps >= 13.0D ? ChatColor.YELLOW : ChatColor.RED;
        }
    }

    public static void registerTasks(JavaPlugin plugin, Class<?>... classes) {
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(ScheduledTask.class)) continue;
                ScheduledTask task = method.getAnnotation(ScheduledTask.class);
                service.scheduleWithFixedDelay(() -> invokeMethod(method, plugin), 0L, task.delay(), TimeUnit.MILLISECONDS);
            }
        }
    }

    private static void invokeMethod(Method method, JavaPlugin plugin) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            try {
                method.invoke(null);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void sendMessage(Object object, String message) {
        try {
            Method sendMessageM = object.getClass().getDeclaredMethod("sendMessage", String.class);
            sendMessageM.setAccessible(true);
            sendMessageM.invoke(object, translateAltColorCodes(message));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void sendChatPacket(Player player, String data) {
        ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(new Packet3Chat(translateAltColorCodes(data)));
    }

    public static String translateAltColorCodes(String input) {
        return doCharCodeThing('&', input);
    }

    public static int getBlocksAwayFrom(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity target) {
        if (entity == null || target == null) return -1;
        Location entityLocation = entity.getLocation();
        Location targetLocation = target.getLocation();
        return getBlocksAwayFrom(entityLocation, targetLocation);
    }

    public static int getBlocksAwayFrom(Location location1, Location location2) {
        return (int) Math.sqrt((Math.sqrt(location1.getBlockX() - location2.getBlockX()) + Math.sqrt(location1.getBlockZ() - location2.getBlockZ())));
    }

    private static String doCharCodeThing(char altColorChar, String textToTranslate)
    {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++)
        {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
            {
                b[i] = '\u00A7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}

