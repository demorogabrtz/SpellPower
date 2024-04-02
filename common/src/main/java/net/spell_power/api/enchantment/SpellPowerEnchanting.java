package net.spell_power.api.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.*;
import java.util.function.BiFunction;
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

//    public static EnumSet<MagicSchool> relevantSchools(ItemStack stack, EquipmentSlot slot) {
//        var schools = EnumSet.noneOf(MagicSchool.class);
//        var attributes = stack.getAttributeModifiers(slot);
//        for (var entry: attributes.entries()) {
//            var attributeId = Registries.ATTRIBUTE.getId(entry.getKey());
//            for (var powerEntry: SpellAttributes.POWER.entrySet()) {
//                if (powerEntry.getValue().id.equals(attributeId)) {
//                    schools.add(powerEntry.getKey());
//                }
//            }
//        }
//        return schools;
//    }

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
                if (school.powerAttribute.equals(attribute)) {
                    schools.add(school);
                }
            }
        }
        return schools;
    }


    // Defines connection between enchantments and powered schools
//    public record AttributeBoost(Enchantment enchantment, BiFunction<Double, Integer, Double> amplifier) { }
//    private static final Multimap<MagicSchool, AttributeBoost> powerMap = HashMultimap.create();
//    public static void boostSchool(MagicSchool school, Enchantment enchantment, BiFunction<Double, Integer, Double> amplifier) {
//        powerMap.put(school, new AttributeBoost(enchantment, amplifier));
//    }
//    public static Collection<AttributeBoost> boostersFor(MagicSchool school) {
//        return powerMap.get(school);
//    }

//    static {
//        for(var entry: Enchantments_SpellPower.all.entrySet()) {
//            var enchantment = entry.getValue();
//            for (var school: enchantment.poweredSchools()) {
//                boostSchool(school, enchantment, enchantment::amplify);
//            }
//        }
//        boostSchool(MagicSchool.PHYSICAL_MELEE, Enchantments.SHARPNESS, (value, level) -> value * (1 + ((0.05) * level)));
//        boostSchool(MagicSchool.PHYSICAL_RANGED, Enchantments.POWER, (value, level) -> value * (1 + ((0.05) * level)));
//    }

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
