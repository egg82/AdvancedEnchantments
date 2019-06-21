package me.egg82.ae.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.bukkit.Location;

public class CollectionProvider {
    private CollectionProvider() {}

    private static ExpiringMap<UUID, Double> bleeding = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Double> getBleeding() { return bleeding; }

    private static Set<Location> explosive = new HashSet<>();
    public static Set<Location> getExplosive() { return explosive; }

    private static Set<UUID> fiery = new HashSet<>();
    public static Set<UUID> getFiery() { return fiery; }
}
