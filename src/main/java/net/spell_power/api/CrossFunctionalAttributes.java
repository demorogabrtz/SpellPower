package net.spell_power.api;

import com.google.common.collect.HashMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrossFunctionalAttributes {
    /**
     * Register a cross-functional attributes.
     * - key: what is powered
     * - value: by what other attributes
     */
    private static final HashMultimap<RegistryEntry<EntityAttribute>, RegistryEntry<EntityAttribute>> cross = HashMultimap.create();

    public static void power(RegistryEntry<EntityAttribute> key, RegistryEntry<EntityAttribute> value) {
        cross.put(key, value);
    }

    public static Set<RegistryEntry<EntityAttribute>> getPowered(RegistryEntry<EntityAttribute> key) {
        return cross.get(key);
    }

    public interface Proxy {
        List<Provider> getCrossProvidersForPowered(RegistryEntry<EntityAttribute> attribute);
    }

    public interface Fallback {
        void setProxy(Proxy proxy);
        Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> getAttributeInstances();
    }

    public interface Provider {
        void setProxy(Proxy proxy);
        void updateIfNecessaryForCross();
        Collection<EntityAttributeModifier> getModifiersByOperation_Cross(EntityAttributeModifier.Operation operation);
    }
}
