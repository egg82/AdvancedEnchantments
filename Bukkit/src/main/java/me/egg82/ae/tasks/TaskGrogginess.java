package me.egg82.ae.tasks;

import java.util.Optional;
import me.egg82.ae.APIException;
import me.egg82.ae.EnchantAPI;
import me.egg82.ae.api.AdvancedEnchantment;
import me.egg82.ae.api.BukkitEnchantableItem;
import me.egg82.ae.api.GenericEnchantableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskGrogginess implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EnchantAPI api = EnchantAPI.getInstance();

    public TaskGrogginess() { }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<EntityEquipment> equipment = Optional.ofNullable(player.getEquipment());
            if (!equipment.isPresent()) {
                return;
            }

            GenericEnchantableItem enchantableHelmet = BukkitEnchantableItem.fromItemStack(equipment.get().getHelmet());
            GenericEnchantableItem enchantableChestplate = BukkitEnchantableItem.fromItemStack(equipment.get().getChestplate());
            GenericEnchantableItem enchantableLeggings = BukkitEnchantableItem.fromItemStack(equipment.get().getLeggings());
            GenericEnchantableItem enchantableBoots = BukkitEnchantableItem.fromItemStack(equipment.get().getBoots());

            boolean hasEnchantment;
            int level;
            try {
                hasEnchantment = api.anyHasEnchantment(AdvancedEnchantment.GROGGINESS_CURSE,
                        enchantableHelmet,
                        enchantableChestplate,
                        enchantableLeggings,
                        enchantableBoots);
                level = api.getMaxLevel(AdvancedEnchantment.GROGGINESS_CURSE,
                        enchantableHelmet,
                        enchantableChestplate,
                        enchantableLeggings,
                        enchantableBoots);
            } catch (APIException ex) {
                logger.error(ex.getMessage(), ex);
                return;
            }

            if (!hasEnchantment || level <= 0) {
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 61, level - 1, true, false), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 61, level - 1, true, false), true);
        }
    }
}
