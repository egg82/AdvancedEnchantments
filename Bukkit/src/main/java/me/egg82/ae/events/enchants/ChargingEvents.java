package me.egg82.ae.events.enchants;

import java.util.Optional;
import me.egg82.ae.APIException;
import me.egg82.ae.api.AdvancedEnchantment;
import me.egg82.ae.api.BukkitEnchantableItem;
import me.egg82.ae.api.GenericEnchantableItem;
import me.egg82.ae.events.EventHolder;
import me.egg82.ae.services.entity.EntityItemHandler;
import me.egg82.ae.utils.PermissionUtil;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ChargingEvents extends EventHolder {
    public ChargingEvents(Plugin plugin) {
        events.add(
                BukkitEvents.subscribe(plugin, EntityDamageByEntityEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .filter(e -> e.getDamager().isOnGround())
                        .filter(e -> e.getDamager() instanceof Player)
                        .filter(e -> ((Player) e.getDamager()).isSprinting())
                        .filter(e -> PermissionUtil.canUseEnchant(e.getDamager(), "ae.enchant.charging"))
                        .handler(this::damage)
        );
    }

    private void damage(EntityDamageByEntityEvent event) {
        EntityItemHandler entityItemHandler = getItemHandler();
        if (entityItemHandler == null) {
            return;
        }

        LivingEntity from = (LivingEntity) event.getDamager();

        Optional<ItemStack> mainHand = entityItemHandler.getItemInMainHand(from);
        GenericEnchantableItem enchantableMainHand = mainHand.isPresent() ? BukkitEnchantableItem.fromItemStack(mainHand.get()) : null;

        boolean hasEnchantment;
        int level;
        try {
            hasEnchantment = api.anyHasEnchantment(AdvancedEnchantment.CHARGING, enchantableMainHand);
            level = api.getMaxLevel(AdvancedEnchantment.CHARGING, enchantableMainHand);
        } catch (APIException ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }

        if (!hasEnchantment) {
            return;
        }

        double damage = Math.max(10.0d, event.getDamage()) / 2.0d;
        damage += damage - (damage / (level + 0.3333333333333334d));
        event.setDamage(damage);
    }
}
