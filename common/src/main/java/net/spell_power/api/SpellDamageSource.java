package net.spell_power.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.mixin.DamageSourcesAccessor;

public class SpellDamageSource {
    public static DamageSource create(SpellSchool school, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            return player(school, player);
        } else {
            return mob(school, attacker);
        }
    }

    public static DamageSource mob(SpellSchool school, LivingEntity attacker) {
        return create(school, "mob", attacker);
    }

    public static DamageSource player(SpellSchool school, PlayerEntity attacker) {
        return create(school, "player", attacker);
    }

    private static DamageSource create(SpellSchool school, String name, Entity attacker) {
        var registry = ((DamageSourcesAccessor)attacker.getDamageSources()).getRegistry();
        return new DamageSource(registry.entryOf(school.damageType), attacker);
    }
}
