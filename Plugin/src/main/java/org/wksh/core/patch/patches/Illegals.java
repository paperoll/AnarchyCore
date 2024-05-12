package org.wksh.core.patch.patches;

import com.legacyminecraft.poseidon.event.PlayerReceivePacketEvent;
import com.legacyminecraft.poseidon.event.PlayerSendPacketEvent;
import net.minecraft.server.Packet100OpenWindow;
import net.minecraft.server.Packet101CloseWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.wksh.core.AnarchyCore;
import org.wksh.core.common.event.bus.annotation.SubscribeEvent;
import org.wksh.core.common.event.events.InventoryEvent;

import java.util.List;
import java.util.stream.IntStream;

public class Illegals implements Listener
{
    private final List<Integer> illegalIds = AnarchyCore.plugin().configManager().config().getIntList("Patches.IllegalsPatch.Ids");
    private final boolean checkDurability =  AnarchyCore.plugin().configManager().config().getBoolean("Patches.IllegalsPatch.Durability");
    private final boolean checkInfinites =  AnarchyCore.plugin().configManager().config().getBoolean("Patches.IllegalsPatch.Infinites");
    private final boolean log =  AnarchyCore.plugin().configManager().config().getBoolean("Patches.IllegalsPatch.Log");

    @EventHandler(priority = Event.Priority.Monitor)
    public void onChunkLoad(ChunkLoadEvent event)
    {
        if (!event.isNewChunk())
        {
            for (BlockState state : event.getChunk().getTileEntities())
            {
                Inventory inventory = (state instanceof Chest) ? ((Chest) state).getInventory() :
                        (state instanceof Dispenser) ? ((Dispenser) state).getInventory() :
                                (state instanceof Furnace) ? ((Furnace) state).getInventory() : null;

                this.checkInventory(inventory, state.getBlock().getType().toString(), state.getBlock().getLocation().toString());
            }

            for (Entity entity : event.getChunk().getEntities())
            {
                if (entity instanceof StorageMinecart)
                {
                    StorageMinecart minecart = (StorageMinecart) entity;
                    Inventory inventory = minecart.getInventory();

                    this.checkInventory(inventory, minecart.toString(), minecart.getLocation().toString());
                }
            }
        }
    }

    @SubscribeEvent
    public void onInventory(InventoryEvent event)
    {
        switch (event.getInventoryType())
        {
            case OPEN:
            {
                switch (event.getContainerType())
                {
                    case LARGE_CHEST:
                    case MINECART:
                    case CHEST:
                    {
                        net.minecraft.server.ItemStack[] items = event.getInventory().getContents();
                        IntStream.range(0, items.length)
                                .filter(i -> items[i] != null && items[i].getItem() != null && Material.getMaterial(items[i].id) != Material.AIR && isIllegal(items[i]))
                                .forEach(i ->
                                {
                                    this.log("Illegal item [{}|x{}|{}] removed from {}!", Material.getMaterial(items[i].id), items[i].count, items[i].damage, event.getContainerType());
                                    event.getInventory().setItem(i, null);
                                });
                        break;
                    }
                    case DISPENSER:
                    {
                        net.minecraft.server.ItemStack[] items = event.getTileEntityDispenser().getContents();
                        IntStream.range(0, items.length)
                                .filter(i -> items[i] != null && items[i].getItem() != null && Material.getMaterial(items[i].id) != Material.AIR && isIllegal(items[i]))
                                .forEach(i ->
                                {
                                    this.log("Illegal item [{}|x{}|{}] removed from {}!", Material.getMaterial(items[i].id), items[i].count, items[i].damage, event.getContainerType());
                                    event.getTileEntityDispenser().setItem(i, null);
                                });
                        break;
                    }
                    case FURNACE:
                    {
                        net.minecraft.server.ItemStack[] items = event.getTileEntityFurnace().getContents();
                        IntStream.range(0, items.length)
                                .filter(i -> items[i] != null && items[i].getItem() != null && Material.getMaterial(items[i].id) != Material.AIR && isIllegal(items[i]))
                                .forEach(i ->
                                {
                                    this.log("Illegal item [{}|x{}|{}] removed from {}!", Material.getMaterial(items[i].id), items[i].count, items[i].damage, event.getContainerType());
                                    event.getTileEntityFurnace().setItem(i, null);
                                });
                        break;
                    }
                    default:
                    {
                        AnarchyCore.logger().error("Unhandled container type: {}", event.getContainerType());
                        break;
                    }
                }
                break;
            }
            case CLOSE:
            {
                // no event for this yet
                break;
            }
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onFurnaceSmelt(FurnaceSmeltEvent event)
    {
        if (checkInfinites && event.getSource().getAmount() <= 0)
        {
            event.getSource().setAmount(1);
            this.log("Prevented furnace from using infinite item as fuel at {}!", event.getFurnace().getLocation());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onFurnaceBurn(FurnaceBurnEvent event)
    {
        if (checkInfinites && event.isBurning() && event.getFuel().getAmount() <= 0)
        {
            event.getFuel().setAmount(1);
            this.log("Prevented infinite {} from being used as furnace fuel at {}!", event.getFuel().getType(), event.getFurnace().getLocation());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!event.getPlayer().getItemInHand().getType().equals(Material.FLINT_AND_STEEL) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.BED) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.SIGN) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.WOOD_DOOR) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.WATER_BUCKET) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.LAVA_BUCKET) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.IRON_DOOR) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.REDSTONE) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.WOOD_HOE) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.SEEDS) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.CAKE) &&
                !event.getPlayer().getItemInHand().getType().equals(Material.DIODE))
        {
            if (illegalIds.contains(event.getItemInHand().getType().getId()) ||
                    (checkDurability && event.getItemInHand().getType().getMaxDurability() > 0 && event.getItemInHand().getDurability() > event.getItemInHand().getType().getMaxDurability()) ||
                    (checkDurability && event.getPlayer().getItemInHand().getDurability() < 0) ||
                    (checkInfinites && (event.getItemInHand().getAmount() > event.getItemInHand().getMaxStackSize() || event.getItemInHand().getAmount() <= 0)))
            {
                event.setCancelled(true);
                this.checkInventory(event.getPlayer().getInventory(), event.getPlayer().getName(), event.getPlayer().getLocation().toString());
            }
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onBlockDamage(BlockDamageEvent event)
    {
        if (event.getPlayer().getItemInHand().getType() != Material.AIR)
        {
            if (illegalIds.contains(event.getPlayer().getItemInHand().getType().getId()) ||
                    (checkDurability && event.getPlayer().getItemInHand().getType().getMaxDurability() > 0 && event.getPlayer().getItemInHand().getDurability() > event.getPlayer().getItemInHand().getType().getMaxDurability()) ||
                    (checkDurability && event.getPlayer().getItemInHand().getDurability() < 0) ||
                    (checkInfinites && (event.getPlayer().getItemInHand().getAmount() > event.getPlayer().getItemInHand().getMaxStackSize() || event.getPlayer().getItemInHand().getAmount() <= 0)))
            {
                event.setCancelled(true);
                this.checkInventory(event.getPlayer().getInventory(), event.getPlayer().getName(), event.getPlayer().getLocation().toString());
            }
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if (illegalIds.contains(event.getItem().getItemStack().getType().getId()) ||
                (checkDurability && event.getItem().getItemStack().getType().getMaxDurability() > 0 && event.getItem().getItemStack().getDurability() > event.getItem().getItemStack().getType().getMaxDurability()) ||
                (checkDurability && event.getItem().getItemStack().getDurability() < 0) ||
                (checkInfinites && (event.getItem().getItemStack().getAmount() > event.getItem().getItemStack().getMaxStackSize() || event.getItem().getItemStack().getAmount() <= 0)))
        {
            this.log("Prevented a player from picking up {}x{} at {}!", event.getItem().getItemStack().getAmount(), event.getItem().getItemStack().getType(), event.getPlayer().getLocation());
            event.getItem().setFireTicks(20);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerSendPacketEvent event)
    {
        CraftPlayer player = ((CraftPlayer) Bukkit.getPlayer(event.getUsername()));

        if (event.getPacket() instanceof Packet100OpenWindow)
        {
            // removes illegals from the players inventory when they open any container except their inventory
            this.checkInventory(player.getInventory(), player.getHandle().activeContainer.toString(), player.getLocation().toString());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPacket(PlayerReceivePacketEvent event)
    {
        CraftPlayer player = ((CraftPlayer) Bukkit.getPlayer(event.getUsername()));

        if (event.getPacket() instanceof Packet101CloseWindow)
        {
            // removes illegals from the player's inventory on close since I couldn't find a place to add it to inventory event
            this.checkInventory(player.getInventory(), player.getHandle().activeContainer.toString(), player.getLocation().toString());
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onBlockDispense(BlockDispenseEvent event)
    {
        if (illegalIds.contains(event.getItem().getType().getId()) ||
                (checkDurability && event.getItem().getType().getMaxDurability() > 0 && event.getItem().getDurability() > event.getItem().getType().getMaxDurability()) ||
                (checkDurability && event.getItem().getDurability() < 0))
        {
            this.log("Cancelled a dispenser from firing 1x{} at {}!", event.getItem().getType(), event.getBlock().getLocation());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        this.checkInventory(event.getPlayer().getInventory(), ((CraftPlayer) event.getPlayer()).getHandle().activeContainer.toString(), event.getPlayer().getLocation().toString());
    }

    @EventHandler(priority = Event.Priority.Monitor)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.checkInventory(event.getPlayer().getInventory(), ((CraftPlayer) event.getPlayer()).getHandle().activeContainer.toString(), event.getPlayer().getLocation().toString());
    }

    private void checkInventory(Inventory inventory, String containerType, String location)
    {
        if (inventory != null)
        {
            for (int i = 0; i < inventory.getSize(); i++)
            {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR)
                {
                    boolean isIllegal = illegalIds.contains(item.getType().getId()) ||
                            (checkDurability && item.getType().getMaxDurability() > 0 && item.getDurability() > item.getType().getMaxDurability()) ||
                            (checkDurability && item.getDurability() < 0) ||
                            (checkInfinites && (item.getAmount() > item.getMaxStackSize() || item.getAmount() <= 0));

                    String message = isIllegal ? "Illegal item [{}|x{}|{}] removed from {} at {}" : null;
                    if (message != null)
                    {
                        this.log(message, item.getType(), item.getAmount(), item.getDurability(), containerType, location);
                        inventory.setItem(i, null);
                    }
                }
            }
        }
    }

    private boolean isIllegal(net.minecraft.server.ItemStack item)
    {
        return illegalIds.contains(Material.getMaterial(item.id).getId()) ||
                (checkDurability && Material.getMaterial(item.id).getMaxDurability() > 0 && item.damage > Material.getMaterial(item.id).getMaxDurability()) ||
                (checkDurability && item.damage < 0) ||
                (checkInfinites && (item.count > item.getMaxStackSize() || item.count <= 0));
    }

    private void log(Object message, Object... arguments)
    {
        if (this.log)
        {
            AnarchyCore.logger().warn(message.toString(), arguments);
        }
    }
}
