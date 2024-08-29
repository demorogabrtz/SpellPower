package net.spell_power;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_power.api.*;
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
            .schemaVersion(4)
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
//            secondary.boostEffect.addAttributeModifier(
//                    secondary.attribute,
//                    uuid,
//                    bonus_per_stack,
//                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
//            Registry.register(Registries.STATUS_EFFECT, effectRawId++, id.toString(), secondary.boostEffect);
        }
    }

    /**
     * For internal use only!
     */
    public static void registerAttributes() {
        for (var entry : SpellPowerMechanics.all.entrySet()) {
            entry.getValue().registerAttribute();
        }
        for(var resistance: SpellResistance.Attributes.all) {
            resistance.registerAttribute();
        }

        var genericSpellSchool = SpellSchools.GENERIC;
        for(var school: SpellSchools.all()) {
            school.registerAttribute();
            if (school != genericSpellSchool && school.ownsAttribute()) {
                CrossFunctionalAttributes.power(school.attributeEntry, genericSpellSchool.attributeEntry);
            }
        }
    }

    /**
     * For internal use on ly!
     */
    public static void registerStatusEffects() {
//        for(var school: SpellSchools.all()) {
//            var id = school.id;
//            if (school.powerEffectManagement.isInternal()) {
//                if (school.boostEffect != null && Registries.STATUS_EFFECT.get(id) == null) {
//                    var config = attributesConfig.value.spell_power_effect;
//                    school.boostEffect.addAttributeModifier(
//                            school.attribute,
//                            config.uuid,
//                            config.bonus_per_stack,
//                            EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
//                    Registry.register(Registries.STATUS_EFFECT, effectRawId++, id.toString(), school.boostEffect);
//                }
//            }
//        }
    }

    public static AttributesConfig.AttributeScope attributeScopeOverride = null;
    public static AttributesConfig.AttributeScope attributeScope() {
        return attributeScopeOverride != null ? attributeScopeOverride : attributesConfig.value.attributes_container_injection_scope;
    }
}