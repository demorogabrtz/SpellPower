package net.spell_power.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;

public class SpellPowerTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> ALL = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(SpellPowerMod.ID, "all"));
    }
    public static class Enchantments {
        public static final TagKey<Enchantment> REQUIRES_MATCHING_ATTRIBUTE = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "requires_matching_attribute"));
        public static final TagKey<Enchantment> MULTI_SCHOOL = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, "multi_school"));
    }
}
