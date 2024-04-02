package net.spell_power.config;

import net.spell_power.api.SpellPowerSecondaries;
import net.spell_power.internals.SpellStatusEffect;

import java.util.Map;

public class AttributesConfig {
    public boolean use_vanilla_magic_damage_type = true;
    public double base_spell_critical_chance_percentage = 5;
    public double base_spell_critical_damage_percentage = 50;
    public int status_effect_raw_id_starts_at = 730;
    public float spell_power_effect_bonus_per_stack = 0.1F;
    public Map<String, SpellStatusEffect.Config> secondary_effects;

    public static AttributesConfig defaults() {
        var config = new AttributesConfig();
        config.secondary_effects = Map.of(
                SpellPowerSecondaries.CRITICAL_CHANCE.name, new SpellStatusEffect.Config("0e0ddd12-0646-42b7-8daf-36b4ccf524df", 0.05F),
                SpellPowerSecondaries.CRITICAL_DAMAGE.name, new SpellStatusEffect.Config("0612ed2a-3ce5-11ed-b878-0242ac120002", 0.1F),
                SpellPowerSecondaries.HASTE.name, new SpellStatusEffect.Config("092f4f58-3ce5-11ed-b878-0242ac120002", 0.05F)
        );
        return config;
    }
}
