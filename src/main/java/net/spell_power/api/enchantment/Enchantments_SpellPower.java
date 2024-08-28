package net.spell_power.api.enchantment;

import net.spell_power.SpellPowerMod;
import net.spell_power.config.EnchantmentsConfig;

public class Enchantments_SpellPower {
//    public static final String spellPowerName = "spell_power";
//    public static final Identifier spellPowerId = Identifier.of(SpellPowerMod.ID, spellPowerName);
//    public static final SchoolFilteredEnchantment SPELL_POWER = new SchoolFilteredEnchantment(
//            Enchantment.Rarity.UNCOMMON,
//            ADD,
//            config().spell_power,
//            Set.of(SpellSchools.ARCANE, SpellSchools.FIRE, SpellSchools.FROST, SpellSchools.HEALING, SpellSchools.LIGHTNING, SpellSchools.SOUL),
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_spell_power_generic"));
//
//    public static final String soulfrostName = "soulfrost";
//    public static final Identifier soulfrostId = Identifier.of(SpellPowerMod.ID, soulfrostName);
//    public static final SchoolFilteredEnchantment SOULFROST = new SchoolFilteredEnchantment(
//            Enchantment.Rarity.RARE,
//            ADD,
//            config().soulfrost,
//            Set.of(SpellSchools.SOUL, SpellSchools.FROST),
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_spell_power_soulfrost"));
//
//    public static final String sunfireName = "sunfire";
//    public static final Identifier sunfireId = Identifier.of(SpellPowerMod.ID, sunfireName);
//    public static final SchoolFilteredEnchantment SUNFIRE = new SchoolFilteredEnchantment(
//            Enchantment.Rarity.RARE,
//            ADD,
//            config().sunfire,
//            Set.of(SpellSchools.ARCANE, SpellSchools.FIRE),
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_spell_power_sunfire"));
//
//    public static final String energizeName = "energize";
//    public static final Identifier energizeId = Identifier.of(SpellPowerMod.ID, energizeName);
//    public static final SchoolFilteredEnchantment ENERGIZE = new SchoolFilteredEnchantment(
//            Enchantment.Rarity.RARE,
//            ADD,
//            config().energize,
//            Set.of(SpellSchools.HEALING, SpellSchools.LIGHTNING),
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_spell_power_energize"));
//
//    public static final Map<Identifier, SchoolFilteredEnchantment> all;
//    static {
//        all = new HashMap<>();
//        all.put(spellPowerId, SPELL_POWER);
//        all.put(soulfrostId, SOULFROST);
//        all.put(sunfireId, SUNFIRE);
//        all.put(energizeId, ENERGIZE);
//
//        for(var entry: all.entrySet()) {
//            var enchantment = entry.getValue();
//            EnchantmentRestriction.prohibit(enchantment, itemStack -> {
//                var typeMatches = enchantment.matchesRequiredTag(itemStack);
//                var schoolMatches = !enchantment.requiresRelatedAttributes() || SchoolFilteredEnchantment.schoolsIntersect(enchantment.poweredSchools(), itemStack);
//                return !typeMatches || !schoolMatches;
//            });
//        }
//    }

    private static EnchantmentsConfig config() {
        return SpellPowerMod.enchantmentConfig.value;
    }
}
