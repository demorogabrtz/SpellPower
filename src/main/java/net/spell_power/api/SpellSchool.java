package net.spell_power.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

public class SpellSchool {
    // Given
    public final Identifier id;
    public final int color;
    public final EntityAttribute attribute;
    public Manage attributeManagement = Manage.INTERNAL;
    @Nullable public final StatusEffect powerEffect;
    public Manage powerEffectManagement = Manage.INTERNAL;
    public final RegistryKey<DamageType> damageType;

    public SpellSchool(Identifier id, int color, RegistryKey<DamageType> damageType, EntityAttribute attribute, @Nullable StatusEffect powerEffect) {
        this.id = id;
        this.color = color;
        this.damageType = damageType;
        this.attribute = attribute;
        this.powerEffect = powerEffect;
    }

    // Sources
    public enum Manage {
        INTERNAL, EXTERNAL;
        public boolean isInternal() { return this == INTERNAL; }
    }
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
}