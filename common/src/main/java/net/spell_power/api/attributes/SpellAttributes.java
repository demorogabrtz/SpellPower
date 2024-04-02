package net.spell_power.api.attributes;

import net.spell_power.api.MagicSchool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellAttributes {
    public final static float PERCENT_ATTRIBUTE_BASELINE = 100F;
    public final static SpellAttributeEntry CRITICAL_CHANCE = new SpellAttributeEntry("critical_chance", PERCENT_ATTRIBUTE_BASELINE);
    public final static SpellAttributeEntry CRITICAL_DAMAGE = new SpellAttributeEntry("critical_damage", PERCENT_ATTRIBUTE_BASELINE);
    public final static SpellAttributeEntry HASTE = new SpellAttributeEntry("haste", PERCENT_ATTRIBUTE_BASELINE);

    public static final Map<String, SpellAttributeEntry> all;
    static {
        all = new HashMap<>();
        List.of(CRITICAL_CHANCE, CRITICAL_DAMAGE, HASTE).forEach(family -> {
            all.put(family.name, family);
        });
    }
}
