package net.spell_power;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_power.api.SpellPowerSecondaries;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.enchantment.Enchantments_SpellBase;
import net.spell_power.api.enchantment.Enchantments_SpellDamage;
import net.spell_power.config.AttributesConfig;
import net.spell_power.config.EnchantmentsConfig;
import net.tinyconfig.ConfigManager;

public class SpellPowerMod implements ModInitializer {
    public static final String ID = "spell_power";

    public static final ConfigManager<AttributesConfig> attributesConfig = new ConfigManager<AttributesConfig>
            ("attributes", AttributesConfig.defaults())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static final ConfigManager<EnchantmentsConfig> enchantmentConfig = new ConfigManager<EnchantmentsConfig>
            ("enchantments", new EnchantmentsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .schemaVersion(3)
            .build();

    private static int effectRawId = 730;

    @Override
    public void onInitialize() {
        attributesConfig.refresh();
        enchantmentConfig.refresh();
        effectRawId = attributesConfig.value.status_effect_raw_id_starts_at;
        SpellSchools.all(); // Trigger static initialization

        for(var entry: SpellPowerSecondaries.all.entrySet()) {
            var secondary = entry.getValue();
            var id = secondary.id;
            Registry.register(Registries.ATTRIBUTE, id, secondary.attribute);

            var uuid = "0e0ddd12-0646-42b7-8daf-36b4ccf524df";
            var bonus_per_stack = 0.1F;
            var config = attributesConfig.value.secondary_effects.get(secondary.name);
            if (config != null) {
                uuid = config.uuid;
                bonus_per_stack = config.bonus_per_stack;
            }
            secondary.boostEffect.addAttributeModifier(
                    secondary.attribute,
                    uuid,
                    bonus_per_stack,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
            Registry.register(Registries.STATUS_EFFECT, effectRawId++, id.toString(), secondary.boostEffect);
        }

        for(var entry: Enchantments_SpellBase.all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
        for(var entry: Enchantments_SpellDamage.all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
        enchantmentConfig.value.apply();
        Enchantments_SpellDamage.attach();
    }

    public static void registerSchoolSpecificContent() {
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (Registries.ATTRIBUTE.get(id) == null) {
                Registry.register(Registries.ATTRIBUTE, id, school.powerAttribute);
            }
            if (school.powerEffect != null && Registries.STATUS_EFFECT.get(id) == null) {
                school.powerEffect.addAttributeModifier(
                        school.powerAttribute,
                        "0e0ddd12-0646-42b7-8daf-36b4ccf524df",
                        attributesConfig.value.spell_power_effect_bonus_per_stack,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                Registry.register(Registries.STATUS_EFFECT, effectRawId++, id.toString(), school.powerEffect);
            }
        }
    }

    // No need for imperative registration. The presence of data file will automatically get them registered.
//    private record DamageTypeEntry(Identifier id, RegistryKey<DamageType> key) { }
//    public static void registerDamageTypes(Registerable<DamageType> registry) {
//        var damageTypeEntries = Arrays.stream(MagicSchool.values())
//                .map(MagicSchool::damageTypeId)
//                .distinct()
//                .map(id -> {
//                    return new DamageTypeEntry(id, RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id));
//                }).toList();
//        for(var entry: damageTypeEntries) {
//            registry.register(entry.key(), new DamageType("player", 0.1F));
//        }
//    }
}