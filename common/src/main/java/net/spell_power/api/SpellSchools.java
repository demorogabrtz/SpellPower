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
import net.spell_power.api.attributes.CustomEntityAttribute;
import net.spell_power.api.enchantment.Enchantments_SpellBase;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.spell_power.internals.SpellStatusEffect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import static net.spell_power.api.SpellPowerSecondaries.PERCENT_ATTRIBUTE_BASELINE;

public class SpellSchools {

    // Predefined Spell Schools

    public static final SpellSchool ARCANE = register(createMagic("arcane", 0xff66ff));
    public static final SpellSchool FIRE = register(createMagic("fire", 0xff3300));
    public static final SpellSchool FROST = register(createMagic("frost", 0xccffff));
    public static final SpellSchool HEALING = register(createMagic("healing", 0x66ff66));
    public static final SpellSchool LIGHTNING = register(createMagic("lightning", 0xffff99));
    public static final SpellSchool SOUL = register(createMagic("soul", 0x2dd4da));

    // Registration

    private static final HashMap<Identifier, SpellSchool> REGISTRY = new HashMap<>();

    public static SpellSchool register(SpellSchool school) {
        REGISTRY.put(school.id, school);
        return school;
    }

    public static Set<SpellSchool> all() {
        return Set.copyOf(REGISTRY.values());
    }

    // School Creation

    public static SpellSchool createMagic(String name, int color) {
        return createMagic(new Identifier(SpellPowerMod.ID, name.toLowerCase()), color);
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

        school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->
            query.entity().getAttributeValue(powerAttribute))
        );
        // Spell Power Enchantments added by Enchantments_SpellDamage.attach

        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerSecondaries.HASTE.attribute); // For example: 110 (with +10% modifier)
            return (value / PERCENT_ATTRIBUTE_BASELINE);  // For example: 110/100 = 1.1
        }));
        school.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellBase.HASTE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query ->  {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
            + query.entity().getAttributeValue(SpellPowerSecondaries.CRITICAL_CHANCE.attribute);
            return (value / PERCENT_ATTRIBUTE_BASELINE);  // For example: 110/100 = 1.1
        }));
        school.addSource(SpellSchool.Trait.CRIT_CHANCE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellBase.CRITICAL_CHANCE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));

        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerSecondaries.CRITICAL_DAMAGE.attribute);
            return (value / PERCENT_ATTRIBUTE_BASELINE);
        }));
        school.addSource(SpellSchool.Trait.CRIT_DAMAGE, new SpellSchool.Source(SpellSchool.Apply.ADD, query -> {
            var enchantment = Enchantments_SpellBase.CRITICAL_DAMAGE;
            var level = SpellPowerEnchanting.getEnchantmentLevelEquipmentSum(enchantment, query.entity());
            return enchantment.amplified(0, level);
        }));

        return school;
    }

    // Utility

    public static class IdTypeAdapter extends TypeAdapter<SpellSchool> {
        @Override
        public void write(JsonWriter jsonWriter, SpellSchool school) throws IOException {
            jsonWriter.value(school.id.toString());
        }

        @Override
        public SpellSchool read(JsonReader jsonReader) throws IOException {
            var string = jsonReader.nextString().toLowerCase(Locale.US);
            var id = new Identifier(string);
            // Replacing default namespace
            if (id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
                id = new Identifier(SpellPowerMod.ID, id.getPath());
            }
            return REGISTRY.get(id);
        }
    }
}
