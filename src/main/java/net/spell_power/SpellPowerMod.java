package net.spell_power;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.api.enchantment.Enchantments_SpellPowerMechanics;
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
            .validate(AttributesConfig::isValid)
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

        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            var id = secondary.id;

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

        for(var entry: Enchantments_SpellPowerMechanics.all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
        for(var entry: Enchantments_SpellPower.all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
        enchantmentConfig.value.apply();
        Enchantments_SpellPower.attach();
    }

    public static void registerAttributes() {
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            Registry.register(Registries.ATTRIBUTE, secondary.id, secondary.attribute);
        }
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (school.attributeManagement.isInternal()) {
                if (school.attribute != null) {
                    Registry.register(Registries.ATTRIBUTE, id, school.attribute);
                }
            }
        }
    }

    public static void registerSchoolSpecificContent() {
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (school.attributeManagement.isInternal()) {
                if (school.attribute != null && Registries.ATTRIBUTE.get(id) == null) {
                    Registry.register(Registries.ATTRIBUTE, id, school.attribute);
                }
            }
            if (school.powerEffectManagement.isInternal()) {
                if (school.boostEffect != null && Registries.STATUS_EFFECT.get(id) == null) {
                    school.boostEffect.addAttributeModifier(
                            school.attribute,
                            "0e0ddd12-0646-42b7-8daf-36b4ccf524df",
                            attributesConfig.value.spell_power_effect_bonus_per_stack,
                            EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                    Registry.register(Registries.STATUS_EFFECT, effectRawId++, id.toString(), school.boostEffect);
                }
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