package net.spell_power.api;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.internals.CustomEntityAttribute;
import net.spell_power.internals.SpellStatusEffect;

import java.util.HashMap;

public class SpellPowerSecondaries {
    public final static float PERCENT_ATTRIBUTE_BASELINE = 100F;
    public static String translationPrefix() {
        return "attribute.name." + SpellPowerMod.ID + ".";
    }

    public static class Entry {
        public final String name;
        public final Identifier id;
        public final float defaultValue, min, max;
        public final CustomEntityAttribute attribute;
        public final StatusEffect boostEffect;
        public Entry(String name, float defaultValue, float min, float max) {
            this.name = name;
            this.id = new Identifier(SpellPowerMod.ID, name);
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.attribute = new CustomEntityAttribute(translationPrefix() + name, defaultValue, min, max, id);
            this.boostEffect = new SpellStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff);
        }
    }

    public static final HashMap<String, Entry> all = new HashMap<>();

    public static Entry entry(String name, float defaultValue, float min, float max) {
        var entry = new Entry(name, defaultValue, min, max);
        all.put(name, entry);
        return entry;
    }

    public static final Entry CRITICAL_CHANCE = entry("critical_chance", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10);
    public static final Entry CRITICAL_DAMAGE = entry("critical_damage", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10);
    public static final Entry HASTE = entry("haste", PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE, PERCENT_ATTRIBUTE_BASELINE * 10);

    static {
        HASTE.attribute.setTracked(true);
    }
}
