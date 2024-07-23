package net.spell_power.api;

import com.google.gson.annotations.JsonAdapter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.spell_power.api.misc.SpellSchoolJSONAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

@JsonAdapter(SpellSchoolJSONAdapter.class)
public class SpellSchool {
    public enum Archetype { ARCHERY, MAGIC, MELEE }
    public final Archetype archetype;
    /**
     * ID of the:
     * - Spell School itself
     * - Powering Entity Attribute if managed internally
     * - Powering Status Effect if managed internally
     */
    public final Identifier id;

    /**
     * Theme color of the spell school.
     * Format: 0xRRGGBB. For example, 0xff0000 is red, 0x00ff00 is green, 0x0000ff is blue.
     * Used for:
     * - Cast bar tinting
     * - Boosting status effect color
     */
    public final int color;

    /**
     * Entity attribute that powers this spell school
     */
    public final EntityAttribute attribute;
    /**
     * Specifies who is responsible for registering the attribute
     * - INTERNAL: Spell Power mod will register it
     * - EXTERNAL: Your mod will register it
     */
    public Manage attributeManagement = Manage.INTERNAL;

    /**
     * Status effect that boosts this spell school.
     * Maybe left null, if status effect that boosts the respective attribute already exists.
     * (Like how vanilla Strength boosts attack damage)
     */
    @Nullable public final StatusEffect boostEffect;
    /**
     * Specifies who is responsible for registering the status effect
     * - INTERNAL: Spell Power mod will register it
     * - EXTERNAL: Your mod will register it
     */
    public Manage powerEffectManagement = Manage.INTERNAL;

    /**
     * Spells of this school deal this type of damage
     */
    public final RegistryKey<DamageType> damageType;

    public SpellSchool(Archetype archetype, Identifier id, int color, RegistryKey<DamageType> damageType, EntityAttribute attribute) {
        this(archetype, id, color, damageType, attribute, null);
    }

    public SpellSchool(Archetype archetype, Identifier id, int color, RegistryKey<DamageType> damageType, EntityAttribute attribute, @Nullable StatusEffect boostEffect) {
        this.archetype = archetype;
        this.id = id;
        this.color = color;
        this.damageType = damageType;
        this.attribute = attribute;
        this.boostEffect = boostEffect;
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

    public void addSource(Trait trait, Apply apply, Function<QueryArgs, Double> function) {
        addSource(trait, new Source(apply, function));
    }

    public void addSource(Trait trait, Source source) {
        sources.get(trait).add(source);
        sources.get(trait).sort(Comparator.comparingInt(a -> a.apply.ordinal()));
    }

    public double getValue(Trait trait, QueryArgs query) {
        var traitSources = sources.get(trait);
        var value = 0F;
        switch (trait) {
            // Base value
            case POWER, CRIT_CHANCE -> { value = 0; }
            case HASTE, CRIT_DAMAGE -> { value = 1; }
        }
        var multiplier = 1F;
        for (var source: traitSources) {
            switch (source.apply) {
                case ADD -> value += source.function.apply(query);
                case MULTIPLY -> multiplier += source.function.apply(query);
            };
        }
        value *= multiplier;
        return value;
    }
}