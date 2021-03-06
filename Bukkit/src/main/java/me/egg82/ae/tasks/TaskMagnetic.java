package me.egg82.ae.tasks;

import java.util.Optional;
import me.egg82.ae.APIException;
import me.egg82.ae.EnchantAPI;
import me.egg82.ae.api.AdvancedEnchantment;
import me.egg82.ae.api.BukkitEnchantableItem;
import me.egg82.ae.utils.LocationUtil;
import me.egg82.ae.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskMagnetic implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EnchantAPI api = EnchantAPI.getInstance();

    public TaskMagnetic() { }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!PermissionUtil.canUseEnchant(player, "ae.enchant.magnetic")) {
                continue;
            }

            Optional<EntityEquipment> equipment = Optional.ofNullable(player.getEquipment());
            if (!equipment.isPresent()) {
                continue;
            }

            int level;
            try {
                level = api.getMaxLevel(AdvancedEnchantment.MAGNETIC,
                        BukkitEnchantableItem.fromItemStack(equipment.get().getHelmet()),
                        BukkitEnchantableItem.fromItemStack(equipment.get().getChestplate()),
                        BukkitEnchantableItem.fromItemStack(equipment.get().getLeggings()),
                        BukkitEnchantableItem.fromItemStack(equipment.get().getBoots())
                        );
            } catch (APIException ex) {
                logger.error(ex.getMessage(), ex);
                continue;
            }

            if (level < 0) {
                continue;
            }

            Location playerLocation = player.getLocation();
            double distance = 2.5d + level;

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, 2.0d, distance)) {
                if (e instanceof Item || e instanceof ExperienceOrb) {
                    if (e instanceof Item) {
                        if (((Item) e).getPickupDelay() > 0) {
                            continue;
                        }
                    }

                    Vector v = playerLocation.toVector().subtract(e.getLocation().toVector()).normalize().multiply(level * 0.035d);
                    if (LocationUtil.isFinite(v)) {
                        e.setVelocity(v);
                    }
                }
            }
        }
    }
}
