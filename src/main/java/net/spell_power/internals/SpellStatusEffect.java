package net.spell_power.internals;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellStatusEffect extends StatusEffect {
    public SpellStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static class Config {
        public String uuid = "e8222db4-6c3c-4bbe-bacb-6e8d07e96e8b";
        public float bonus_per_stack = 0.1F;

        public Config() { }

        public Config(String uuid, float bonus_per_stack) {
            this.uuid = uuid;
            this.bonus_per_stack = bonus_per_stack;
        }
    }
}
