package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.internals.SchoolFilteredEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.minecraft.enchantment.EnchantmentTarget.BREAKABLE;
import static net.spell_power.internals.AmplifierEnchantment.Operation.ADD;

public class Enchantments_SpellDamage {
    public static final String spellPowerName = "spell_power";
    public static final Identifier spellPowerId = new Identifier(SpellPowerMod.ID, spellPowerName);
    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
            Enchantment.Rarity.UNCOMMON,
            ADD,
            config().spell_power,
            Set.of(SpellSchools.ARCANE, SpellSchools.FIRE, SpellSchools.FROST, SpellSchools.HEALING, SpellSchools.LIGHTNING, SpellSchools.SOUL),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String soulfrostName = "soulfrost";
    public static final Identifier soulfrostId = new Identifier(SpellPowerMod.ID, soulfrostName);
    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().soulfrost,
            Set.of(SpellSchools.SOUL, SpellSchools.FROST),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String sunfireName = "sunfire";
    public static final Identifier sunfireId = new Identifier(SpellPowerMod.ID, sunfireName);
    public static final SchoolFilteredEnchantment SUNFIRE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().sunfire,
            Set.of(SpellSchools.ARCANE, SpellSchools.FIRE),
            BREAKABLE,
            EquipmentSlot.values());

    public static final String energizeName = "energize";
    public static final Identifier energizeId = new Identifier(SpellPowerMod.ID, energizeName);
    public static final SchoolFilteredEnchantment ENERGIZE = new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            ADD,
            config().energize,
            Set.of(SpellSchools.HEALING, SpellSchools.LIGHTNING),
            BREAKABLE,
            EquipmentSlot.values());

    public static final Map<Identifier, SchoolFilteredEnchantment> all;
    static {
        all = new HashMap<>();
        all.put(spellPowerId, SPELL_POWER);
        all.put(soulfrostId, SOULFROST);
        all.put(sunfireId, SUNFIRE);
        all.put(energizeId, ENERGIZE);

        for(var entry: all.entrySet()) {
            var enchantment = entry.getValue();
            EnchantmentRestriction.prohibit(enchantment, itemStack -> {
                var itemTypeRequirement = enchantment.config.requires;
                var typeMatches = itemTypeRequirement == null || itemTypeRequirement.matches(itemStack);
                var schoolMatches = SchoolFilteredEnchantment.schoolsIntersect(enchantment.poweredSchools(), itemStack);
                return !typeMatches || !schoolMatches;
            });
        }
    }

    private static EnchantmentsConfig config() {
        return SpellPowerMod.enchantmentConfig.value;
    }

    public static void attach() {
        for(var school: SpellSchools.all()) {
            var poweringEnchantments = all.entrySet().stream()
                    .filter(entry -> entry.getValue().poweredSchools().contains(school))
                    .map(Map.Entry::getValue)
                    .toList();
            school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.MULTIPLY, query -> {
                double value = 0;
                for (var enchantment: poweringEnchantments) {
                    var level = SpellPowerEnchanting.getEnchantmentLevel(enchantment, query.entity(), null);
                    value = enchantment.amplified(value, level);
                }
                return value;
            }));
        }
    }
}
