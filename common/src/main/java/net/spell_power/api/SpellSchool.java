package net.spell_power.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.spell_power.internals.SpellStatusEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

public class SpellSchool {
    // Given
    public final Identifier id;
    public final int color;
    public final EntityAttribute powerAttribute;
    @Nullable public final StatusEffect powerEffect;
    private Identifier damageType;

    public SpellSchool(Identifier id, int color, Identifier damageType, EntityAttribute powerAttribute, @Nullable StatusEffect powerEffect) {
        this.id = id;
        this.color = color;
        this.damageType = damageType;
        this.powerAttribute = powerAttribute;
        this.powerEffect = powerEffect;
    }

    // Sources
    public enum Apply { ADD, MULTIPLY }
    public record QueryArgs(LivingEntity entity) { }
    public record Source(Apply apply, Function<QueryArgs, Double> function) { }
    public enum Trait { POWER, HASTE, CRIT_CHANCE, CRIT_DAMAGE }
    private static HashMap<Trait, ArrayList<Source>> emptyTraits() {
        var map = new HashMap<Trait, ArrayList<Source>>();
        for (var trait: Trait.values()) {
            map.put(trait, new ArrayList<>());
        }
        return map;
    }
    private HashMap<Trait, ArrayList<Source>> sources = emptyTraits();

    public void addSource(Trait trait, Source source) {
        sources.get(trait).add(source);
        sources.get(trait).sort(Comparator.comparingInt(a -> a.apply.ordinal()));
    }

    public double getValue(Trait trait, QueryArgs query) {
        var value = 0F;
        var multiplier = 1F;
        for (var source: sources.get(trait)) {
            switch (source.apply) {
                case ADD -> value += source.function.apply(query);
                case MULTIPLY -> multiplier += source.function.apply(query);
            };
        }
        value *= multiplier;
        return value;
    }

    // Config

    public void apply(Config config) {
    }

    public static class Config {
        public SpellStatusEffect.Config power_effect;
    }
}