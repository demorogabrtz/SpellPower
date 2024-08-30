package net.spell_power;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_power.api.*;
import net.spell_power.config.AttributesConfig;
import net.spell_power.internals.CrossFunctionalAttributes;
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

    private static int effectRawId = 730;

    @Override
    public void onInitialize() {
        attributesConfig.refresh();
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
     * For internal use only!
     */
    public static void registerStatusEffects() {
        var modifierId = Identifier.of(ID, "potion_effect");
        var bonus_per_stack = 0.1F;
        for(var school: SpellSchools.all()) {
            var id = school.id;
            if (school.ownedBoostEffect != null && school.attributeEntry != null) {
                school.ownedBoostEffect.addAttributeModifier(
                        school.attributeEntry,
                        modifierId,
                        bonus_per_stack,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

                Registry.register(Registries.STATUS_EFFECT, id.toString(), school.ownedBoostEffect);
            }
        }

        for(var entry: SpellPowerMechanics.all.entrySet()) {
            var secondary = entry.getValue();
            var id = secondary.id;

            var config = attributesConfig.value.secondary_effects.get(secondary.name);
            if (config != null) {
                bonus_per_stack = config.bonus_per_stack;
            }
            secondary.boostEffect.addAttributeModifier(
                    secondary.attributeEntry,
                    modifierId,
                    bonus_per_stack,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            Registry.register(Registries.STATUS_EFFECT, id.toString(), secondary.boostEffect);
        }
    }

    public static AttributesConfig.AttributeScope attributeScopeOverride = null;
    public static AttributesConfig.AttributeScope attributeScope() {
        return attributeScopeOverride != null ? attributeScopeOverride : attributesConfig.value.attributes_container_injection_scope;
    }
}