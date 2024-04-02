package net.spell_power.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.attributes.CustomEntityAttribute;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;
import net.spell_power.api.enchantment.Enchantments_SpellPower;
import net.spell_power.internals.SpellStatusEffect;

import java.util.HashMap;

import static net.spell_power.api.attributes.SpellAttributes.PERCENT_ATTRIBUTE_BASELINE;

public class SpellSchools {
    public static final SpellSchool ARCANE = register(createMagic("arcane", 0xff66ff));
    public static final SpellSchool FIRE = register(createMagic("fire", 0xff3300));
    public static final SpellSchool FROST = register(createMagic("frost", 0xccffff));
    public static final SpellSchool HEALING = register(createMagic("healing", 0x66ff66));
    public static final SpellSchool LIGHTNING = register(createMagic("lightning", 0xffff99));
    public static final SpellSchool SOUL = register(createMagic("soul", 0x2dd4da));

    public static final HashMap REGISTRY = new HashMap<Identifier, SpellSchool>();

    public static SpellSchool createMagic(String name, int color) {
        return createMagic(new Identifier(SpellPowerMod.ID, name.toLowerCase()), color);
    }

    public static SpellSchool createMagic(Identifier id, int color) {
        var statusEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, color);
        var translationPrefix = "attribute.name." + id.getNamespace() + ".";
        var attribute = new CustomEntityAttribute(translationPrefix + id.getPath(), 0, 0, 2048, id).setTracked(true);
        var school = new SpellSchool(
                id,
                color,
                id, // FIXME
                attribute,
                statusEffect);

        school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD,
                query -> query.entity().getAttributeValue(attribute)));
        // TODO: Spell Power Enchantments

        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(EntityAttributes_SpellPower.HASTE); // For example: 110 (with +10% modifier)
            return (value / PERCENT_ATTRIBUTE_BASELINE);  // For example: 110/100 = 1.1
        }));
        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellPower.HASTE;
            var level = getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplify(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->  {
            var value = query.entity().getAttributeValue(EntityAttributes_SpellPower.CRITICAL_CHANCE);
            return (value / PERCENT_ATTRIBUTE_BASELINE);  // For example: 110/100 = 1.1
        }));
        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellPower.CRITICAL_CHANCE;
            var level = getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplify(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD,
                query -> query.entity().getAttributeValue(EntityAttributes_SpellPower.CRITICAL_DAMAGE)));

        return school;
    }

    public static SpellSchool register(SpellSchool school) {
        REGISTRY.put(school.id, school);
        return school;
    }

    private static int getEnchantmentLevelEquipmentSum(Enchantment enchantment, LivingEntity entity) {
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
