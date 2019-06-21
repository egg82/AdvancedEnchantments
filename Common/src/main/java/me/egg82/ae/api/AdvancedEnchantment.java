package me.egg82.ae.api;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import me.egg82.ae.api.curses.AdherenceCurse;
import me.egg82.ae.api.curses.EnderCurse;
import me.egg82.ae.api.curses.LeechingCurse;
import me.egg82.ae.api.curses.PacifismCurse;
import me.egg82.ae.api.enchantments.*;

public abstract class AdvancedEnchantment extends GenericEnchantment {
    private static final Set<AdvancedEnchantment> allEnchantments = new HashSet<>(); // Needs to be set BEFORE the enchants are defined, else NPE

    public static final AdvancedEnchantment AERIAL = new AerialEnchantment();
    public static final AdvancedEnchantment BEHEADING = new BeheadingEnchantment();
    public static final AdvancedEnchantment BLEEDING = new BleedingEnchantment();
    public static final AdvancedEnchantment BLINDING = new BlindingEnchantment();
    public static final AdvancedEnchantment CHARGING = new ChargingEnchantment();
    public static final AdvancedEnchantment DISARMING = new DisarmingEnchantment();
    public static final AdvancedEnchantment EXPLOSIVE = new ExplosiveEnchantment();
    public static final AdvancedEnchantment FIERY = new FieryEnchantment();
    public static final AdvancedEnchantment FREEZING = new FreezingEnchantment();
    public static final AdvancedEnchantment MAGNETIC = new MagneticEnchantment();
    /*public static final AdvancedEnchantment MIRAGE = new MirageEnchantment();
    public static final AdvancedEnchantment MULTISHOT = new MultishotEnchantment();
    public static final AdvancedEnchantment POISONOUS = new PoisonousEnchantment();
    public static final AdvancedEnchantment REPAIRING = new RepairingEnchantment();
    public static final AdvancedEnchantment SMELTING = new SmeltingEnchantment();*/
    public static final AdvancedEnchantment THUNDEROUS = new ThunderousEnchantment();
    public static final AdvancedEnchantment VAMPIRIC = new VampiricEnchantment();

    public static final AdvancedEnchantment ADHERENCE_CURSE = new AdherenceCurse();
    public static final AdvancedEnchantment ENDER_CURSE = new EnderCurse();
    public static final AdvancedEnchantment LEECHING_CURSE = new LeechingCurse();
    public static final AdvancedEnchantment PACIFISM_CURSE = new PacifismCurse();

    public static Set<AdvancedEnchantment> values() { return ImmutableSet.copyOf(allEnchantments); }

    public static Optional<AdvancedEnchantment> getByName(String name) {
        for (AdvancedEnchantment enchantment : allEnchantments) {
            if (enchantment.name.equalsIgnoreCase(name) || enchantment.friendlyName.equalsIgnoreCase(name)) {
                return Optional.of(enchantment);
            }
        }
        return Optional.empty();
    }

    public static Optional<AdvancedEnchantment> getByUuid(UUID uuid) {
        for (AdvancedEnchantment enchantment : allEnchantments) {
            if (enchantment.uuid.equals(uuid)) {
                return Optional.of(enchantment);
            }
        }
        return Optional.empty();
    }

    protected AdvancedEnchantment(UUID uuid, String name, String friendlyName, boolean isCurse, int minLevel, int maxLevel) {
        super(uuid, name, friendlyName, isCurse, minLevel, maxLevel, null);
        allEnchantments.add(this);
    }
}
