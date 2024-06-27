package net.spell_power.config;

import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.api.enchantment.Enchantments_SpellPowerMechanics;
import net.tinyconfig.models.EnchantmentConfig;
import net.tinyconfig.versioning.VersionableConfig;

public class EnchantmentsConfig extends VersionableConfig {

    public boolean allow_stacking = true;
    // Fields

    public PowerEnchantmentConfig spell_power = new PowerEnchantmentConfig(false, 5, 10, 9, 0.05F);
    public PowerEnchantmentConfig soulfrost = new PowerEnchantmentConfig(true, 5, 10, 9, 0.03F);
    public PowerEnchantmentConfig sunfire = new PowerEnchantmentConfig(true, 5, 10, 9, 0.03F);
    public PowerEnchantmentConfig energize = new PowerEnchantmentConfig(true, 5, 10, 9, 0.03F);

    public EnchantmentConfig critical_chance = new EnchantmentConfig(5, 10, 12, 0.02F);
    public EnchantmentConfig critical_damage = new EnchantmentConfig(5, 10, 12, 0.05F);
    public EnchantmentConfig haste = new EnchantmentConfig(5, 15, 17, 0.04F);
    public EnchantmentConfig magic_protection = new EnchantmentConfig(4, 3, 6, 2);

    // Helper

    public void apply() {
        Enchantments_SpellPowerMechanics.CRITICAL_CHANCE.config = critical_chance;
        Enchantments_SpellPowerMechanics.CRITICAL_DAMAGE.config = critical_damage;
        Enchantments_SpellPowerMechanics.HASTE.config = haste;
        Enchantments_SpellPowerMechanics.MAGIC_PROTECTION.config = magic_protection;
        Enchantments_SpellPower.SPELL_POWER.config = spell_power;
        Enchantments_SpellPower.SOULFROST.config = soulfrost;
        Enchantments_SpellPower.SUNFIRE.config = sunfire;
        Enchantments_SpellPower.ENERGIZE.config = energize;
    }

    public static class PowerEnchantmentConfig extends EnchantmentConfig {
        public boolean requires_related_attributes = false;
        public PowerEnchantmentConfig(boolean requires_related_attributes, int max_level, int min_cost, int step_cost, float bonus_per_level) {
            super(max_level, min_cost, step_cost, bonus_per_level);
            this.requires_related_attributes = requires_related_attributes;
        }
    }
}