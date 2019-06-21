package me.egg82.ae.commands.internal;

import java.util.Optional;
import me.egg82.ae.api.*;
import me.egg82.ae.services.entity.EntityItemHandler;
import me.egg82.ae.utils.LogUtil;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetCommand implements Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final CommandSender sender;
    private final String enchant;
    private final String level;

    public SetCommand(CommandSender sender, String enchant, String level) {
        this.sender = sender;
        this.enchant = enchant;
        this.level = level;
    }

    public void run() {
        Optional<GenericEnchantment> en = getEnchantment(enchant);

        if (!en.isPresent()) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Could not get an enchantment from the name provided.");
            return;
        }

        int l = level == null ? en.get().getMinLevel() : Integer.parseInt(level);

        if (l < en.get().getMinLevel()) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Level cannot be < " + en.get().getMinLevel());
            return;
        }
        if (l < en.get().getMaxLevel()) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Level cannot be > " + en.get().getMaxLevel());
            return;
        }

        if (!(sender instanceof LivingEntity)) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Could not get the item to enchant.");
            return;
        }

        EntityItemHandler entityItemHandler;
        try {
            entityItemHandler = ServiceLocator.get(EntityItemHandler.class);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Internal error.");
            return;
        }

        LivingEntity e = (LivingEntity) sender;

        Optional<ItemStack> mainHand = entityItemHandler.getItemInMainHand(e);
        Optional<ItemStack> offHand = entityItemHandler.getItemInOffHand(e);

        Optional<GenericEnchantableItem> enchantableMainHand = Optional.ofNullable(mainHand.isPresent() ? BukkitEnchantableItem.fromItemStack(mainHand.get()) : null);
        Optional<GenericEnchantableItem> enchantableOffHand = Optional.ofNullable(offHand.isPresent() ? BukkitEnchantableItem.fromItemStack(offHand.get()) : null);

        if (enchantableMainHand.isPresent()) {
            enchantableMainHand.get().setEnchantmentLevel(en.get(), l);
        } else if (enchantableOffHand.isPresent()) {
            enchantableOffHand.get().setEnchantmentLevel(en.get(), l);
        } else {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You are not holding anything to enchant.");
        }
    }

    private Optional<GenericEnchantment> getEnchantment(String enchantment) {
        for (Enchantment e : Enchantment.values()) {
            if (e.getName().equalsIgnoreCase(enchantment)) {
                return Optional.of(BukkitEnchantment.fromEnchant(e));
            }
        }

        for (AdvancedEnchantment e : AdvancedEnchantment.values()) {
            if (e.getName().equalsIgnoreCase(enchantment)) {
                return Optional.of(e);
            }
        }

        return Optional.empty();
    }
}
