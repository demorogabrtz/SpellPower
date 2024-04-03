package net.spell_power.api.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SpellPowerEnchanting {

    // MARK: Armor
    private static final ArrayList<ArmorItem> armor = new ArrayList<>();

    public static void registerArmor(ArmorItem... items) {
        for(var item: items) {
            registerArmor(item);
        }
    }

    public static void registerArmor(List<ArmorItem> items) {
        for(var item: items) {
            registerArmor(item);
        }
    }

    public static void registerArmor(ArmorItem item) {
        armor.add(item);
    }

    public static boolean isArmorRegistered(Item item) {
        return armor.contains(item);
    }

    private static final ArrayList<Function<ItemStack, Boolean>> weaponConditions = new ArrayList<>();

    public static void allowForWeapon(Function<ItemStack, Boolean> condition) {
        weaponConditions.add(condition);
    }

    public static boolean isAllowedForWeapon(ItemStack stack) {
        for (var condition: weaponConditions) {
            if (condition.apply(stack)) {
                return true;
            }
        }
        return false;
    }

    public static Set<SpellSchool> relevantSchools(ItemStack stack) {
        var item = stack.getItem();
        EquipmentSlot slot = EquipmentSlot.MAINHAND;
        if (item instanceof ArmorItem armor) {
            slot = armor.getSlotType();
        }
        return relevantSchools(stack, slot);
    }

    public static Set<SpellSchool> relevantSchools(ItemStack stack, EquipmentSlot slot) {
        var schools = new HashSet<SpellSchool>();
        var attributes = stack.getAttributeModifiers(slot);
        for (var entry: attributes.entries()) {
            var attribute = entry.getKey();
            for (var school: SpellSchools.all()) {
                if (school.attribute.equals(attribute)) {
                    schools.add(school);
                }
            }
        }
        return schools;
    }

    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity entity, ItemStack provisionedWeapon) {
        int level;
        if (SpellPowerMod.enchantmentConfig.value.allow_stacking) {
            level = getEnchantmentLevelEquipmentSum(enchantment, entity);
        } else {
            level = EnchantmentHelper.getEquipmentLevel(enchantment, entity);
        }
        if (provisionedWeapon != null && !provisionedWeapon.isEmpty()) {
            var mainHandStack = entity.getMainHandStack();
            if (mainHandStack != null && !mainHandStack.isEmpty()) {
                level -= EnchantmentHelper.getLevel(enchantment, mainHandStack);
            }
            level += EnchantmentHelper.getLevel(enchantment, provisionedWeapon);
        }
        return level;
    }

    public static int getEnchantmentLevelEquipmentSum(Enchantment enchantment, LivingEntity entity) {
        int level = 0;
        for (var itemStack: entity.getArmorItems()) {
            level += EnchantmentHelper.getLevel(enchantment, itemStack);
        }
        var mainHandStack = entity.getMainHandStack();
        if (mainHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, mainHandStack);
        }
        var offHandStack = entity.getOffHandStack();
        if (offHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, offHandStack);
        }
        return level;
    }
}
