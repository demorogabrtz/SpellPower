package net.spell_power.fabric;

import net.spell_power.SpellPowerMod;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellPowerMod.init();
    }
}