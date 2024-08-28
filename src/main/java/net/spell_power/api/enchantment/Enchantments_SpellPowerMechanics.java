package net.spell_power.api.enchantment;

public class Enchantments_SpellPowerMechanics {

//    // Rating enchants
//
//    public static final Identifier criticalChanceId = Identifier.of(SpellPowerMod.ID, SpellPowerMechanics.CRITICAL_CHANCE.name);
//    public static final AmplifierEnchantment CRITICAL_CHANCE = new AmplifierEnchantment(
//            Enchantment.Rarity.UNCOMMON,
//            ADD,
//            config().critical_chance,
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_critical_chance"));
//
//    public static final Identifier criticalDamageId = Identifier.of(SpellPowerMod.ID, SpellPowerMechanics.CRITICAL_DAMAGE.name);
//    public static final AmplifierEnchantment CRITICAL_DAMAGE = new AmplifierEnchantment(
//            Enchantment.Rarity.UNCOMMON,
//            ADD,
//            config().critical_damage,
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_critical_damage"));
//
//    public static final Identifier hasteId = Identifier.of(SpellPowerMod.ID, SpellPowerMechanics.HASTE.name);
//    public static final AmplifierEnchantment HASTE = new AmplifierEnchantment(
//            Enchantment.Rarity.RARE,
//            ADD,
//            config().haste,
//            BREAKABLE,
//            EquipmentSlot.values())
//            .requireTag(Identifier.of(SpellPowerMod.ID, "enchant_haste"));
//
//    // Resistance
//
//    public static EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
//    public static final String magicProtectionName = "magic_protection";
//    public static final Identifier magicProtectionId = Identifier.of(SpellPowerMod.ID, magicProtectionName);
//    public static final MagicProtectionEnchantment MAGIC_PROTECTION = new MagicProtectionEnchantment(Enchantment.Rarity.RARE, config().magic_protection, ALL_ARMOR);
//
//    // Helpers
//
//    public static final Map<Identifier, Enchantment> all;
//    static {
//        Map<Identifier, AmplifierEnchantment> secondaries = new HashMap<>();
//        secondaries.put(criticalChanceId, CRITICAL_CHANCE);
//        secondaries.put(criticalDamageId, CRITICAL_DAMAGE);
//        secondaries.put(hasteId, HASTE);
//
//        all = new HashMap<>();
//        all.putAll(secondaries);
//        all.put(magicProtectionId, MAGIC_PROTECTION);
//
//        for(var entry: secondaries.entrySet()) {
//            var enchantment = entry.getValue();
//            EnchantmentRestriction.prohibit(enchantment, itemStack -> {
//                var typeMatches = enchantment.matchesRequiredTag(itemStack);
//                return !typeMatches;
//            });
//        }
//    }
//
//    private static EnchantmentsConfig config() {
//        return SpellPowerMod.enchantmentConfig.value;
//    }
}
