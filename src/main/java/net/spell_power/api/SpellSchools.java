package net.spell_power.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.enchantment.Enchantments_SpellPowerMechanics;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.spell_power.internals.CustomEntityAttribute;
import net.spell_power.internals.SpellStatusEffect;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import static net.spell_power.api.SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE;

public class SpellSchools {

    // Registration

    /**
     * Default namespace for spell schools
     */
    public static final String DEFAULT_NAMESPACE = SpellPowerMod.ID;
    private static final LinkedHashMap<Identifier, SpellSchool> REGISTRY = new LinkedHashMap<>();

    public static SpellSchool register(SpellSchool school) {
        REGISTRY.put(school.id, school);
        return school;
    }

    public static Set<SpellSchool> all() {
        // Using linked hash set to preserve order
        return new LinkedHashSet<SpellSchool>(REGISTRY.values());
    }


    // Predefined Spell Schools

    public static final SpellSchool ARCANE = register(createMagic("arcane", 0xff66ff));
    public static final SpellSchool FIRE = register(createMagic("fire", 0xff3300));
    public static final SpellSchool FROST = register(createMagic("frost", 0xccffff));
    public static final SpellSchool HEALING = register(createMagic("healing", 0x66ff66));
    public static final SpellSchool LIGHTNING = register(createMagic("lightning", 0xffff99));
    public static final SpellSchool SOUL = register(createMagic("soul", 0x2dd4da));

    // School Creation

    public static SpellSchool createMagic(String name, int color) {
        return createMagic(new Identifier(DEFAULT_NAMESPACE, name.toLowerCase()), color);
    }


    public static SpellSchool createMagic(Identifier id, int color) {
        var powerEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, color);
        var translationPrefix = "attribute.name." + id.getNamespace() + ".";
        var attribute = new CustomEntityAttribute(translationPrefix + id.getPath(), 0, 0, 2048, id).setTracked(true);
        return createMagic(id, color, attribute, powerEffect);
    }

    public static SpellSchool createMagic(Identifier id, int color, EntityAttribute powerAttribute, StatusEffect powerEffect) {
        var school = new SpellSchool(
                id,
                color,
                DamageTypes.MAGIC, // TODO: Use custom damage type, SpellPowerMod.attributesConfig.value.use_vanilla_magic_damage_type
                powerAttribute,
                powerEffect);
        return configureAsMagic(school, powerAttribute);
    }


    public static SpellSchool configureAsMagic(SpellSchool school, EntityAttribute powerAttribute) {
        school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->
                query.entity().getAttributeValue(powerAttribute))
        );
        // Spell Power Enchantments added by Enchantments_SpellDamage.attach

        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attribute); // 110
            return (value / PERCENT_ATTRIBUTE_BASELINE);  // For example: 110/100 = 1.1
        }));
        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellPowerMechanics.HASTE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->  {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage  // 5
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attribute);    // 20
            return (value / PERCENT_ATTRIBUTE_BASELINE) - 1;  // For example: (125/100) - 1 = 0.25
        }));
        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellPowerMechanics.CRITICAL_CHANCE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage          // 50
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attribute);    // 110
            return (value / PERCENT_ATTRIBUTE_BASELINE);    // For example: 160/100 = 1.6
        }));
        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellPowerMechanics.CRITICAL_DAMAGE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));
        return school;
    }

    // Utility

    @Nullable public static SpellSchool getSchool(String idString) {
        var string = idString.toLowerCase(Locale.US);
        var id = new Identifier(string);
        // Replacing default namespace
        if (id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            id = new Identifier(DEFAULT_NAMESPACE, id.getPath());
        }
        return REGISTRY.get(id);
    }

    public static class IdTypeAdapter extends TypeAdapter<SpellSchool> {
        @Override
        public void write(JsonWriter jsonWriter, SpellSchool school) throws IOException {
            jsonWriter.value(school.id.toString());
        }

        @Override
        public SpellSchool read(JsonReader jsonReader) throws IOException {
            return getSchool(jsonReader.nextString());
        }
    }
}
