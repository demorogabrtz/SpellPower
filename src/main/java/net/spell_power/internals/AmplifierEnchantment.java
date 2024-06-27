package net.spell_power.internals;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.tinyconfig.models.EnchantmentConfig;
import org.jetbrains.annotations.Nullable;

public class AmplifierEnchantment extends Enchantment {
    public Operation operation;
    @Nullable
    protected Identifier tagId;

    public enum Operation {
        ADD, MULTIPLY;
    }

    public EnchantmentConfig config;

    public double amplified(double value, int level) {
        switch (operation) {
            case ADD -> {
                return value += ((float)level) * config.bonus_per_level;
            }
            case MULTIPLY -> {
                return value *= 1F + ((float)level) * config.bonus_per_level;
            }
        }
        assert true;
        return 0F;
    }

    public AmplifierEnchantment(Rarity weight, Operation operation, EnchantmentConfig config, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
        this.operation = operation;
        this.config = config;
    }

    public AmplifierEnchantment requireTag(Identifier tagId) {
        this.tagId = tagId;
        return this;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return super.isAvailableForEnchantedBookOffer() && config.enabled;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return super.isAvailableForRandomSelection() && config.enabled;
    }

    public int getMaxLevel() {
        if (!config.enabled) {
            return 0;
        }
        return config.max_level;
    }

    public int getMinPower(int level) {
        return config.min_cost + (level - 1) * config.step_cost;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        var otherIsRanged = other.target == EnchantmentTarget.BOW || other.target == EnchantmentTarget.CROSSBOW;
        return !otherIsRanged && super.canAccept(other);
    }

    public boolean matchesRequiredTag(ItemStack stack) {
        if (tagId == null) {
            return true;
        }
        return stack.isIn(TagKey.of(RegistryKeys.ITEM, tagId));
    }
}
