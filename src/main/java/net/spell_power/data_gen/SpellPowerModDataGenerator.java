package net.spell_power.data_gen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchools;

import java.util.concurrent.CompletableFuture;

public class SpellPowerModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(EnchantmentGenerator::new);
    }

    private static class EnchantmentGenerator extends FabricDynamicRegistryProvider {

        public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            var spell_power = "spell_power";
            var eid = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpellPowerMod.ID, spell_power));

            Enchantment.Builder builder = Enchantment.builder(Enchantment.definition(
                    registries.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.ACACIA_LOGS),
                    1, 5,
                    Enchantment.leveledCost(2, 3),
                    Enchantment.leveledCost(3, 4),
                    5,
                    AttributeModifierSlot.MAINHAND))
                    .addEffect(
                            EnchantmentEffectComponentTypes.ATTRIBUTES,
                            new AttributeEnchantmentEffect(
                                    Identifier.of(SpellPowerMod.ID, spell_power),
                                    SpellSchools.GENERIC.attributeEntry,
                                    EnchantmentLevelBasedValue.linear(1),
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );

            entries.add(eid, builder.build(eid.getValue()));
        }

        @Override
        public String getName() {
            return "ench";
        }
    }
}