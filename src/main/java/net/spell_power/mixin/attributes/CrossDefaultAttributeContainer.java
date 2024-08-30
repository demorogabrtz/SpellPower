package net.spell_power.mixin.attributes;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_power.internals.CrossFunctionalAttributes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(DefaultAttributeContainer.class)
public class CrossDefaultAttributeContainer implements CrossFunctionalAttributes.Fallback {

    // Inject to init tail

    @Shadow @Final private Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> instances;
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void init_Tail(Map par_instances, CallbackInfo ci) {
//        for(var entry: instances.entrySet()) {
//            var instance = entry.getValue();
//            ((CrossFunctionalAttributes.Provider)instance).setProxy(this);
//        }
//    }

//    @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
//    private void getValue_UpdateOthers(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
//        for(var entry: instances.entrySet()) {
//            var instance = entry.getValue();
//            ((CrossFunctionalAttributes.Provider)instance).setProxy(this) ;
//        }
//    }

//    @Override
//    public void connect() {
//        System.out.println("CrossDefaultAttributeContainer.connect " + instances.size());
//        for(var entry: instances.entrySet()) {
//            var instance = entry.getValue();
//            ((CrossFunctionalAttributes.Provider)instance).setProxy(this);
//        }
//    }
//
//    @Override
//    public List<CrossFunctionalAttributes.Provider> getCrossProvidersForPowered(RegistryEntry<EntityAttribute> attribute) {
//        var crossAttributes = CrossFunctionalAttributes.getPowered(attribute);
//        return crossAttributes.stream()
//                .map(instances::get)
//                .map(instance -> (CrossFunctionalAttributes.Provider)instance)
//                .toList();
//    }


    @Nullable private CrossFunctionalAttributes.Proxy proxy;

    @Override
    public void setProxy(CrossFunctionalAttributes.Proxy proxy) {
        this.proxy = proxy;
        for(var entry: instances.entrySet()) {
            var instance = entry.getValue();
            ((CrossFunctionalAttributes.Provider)instance).setProxy(proxy);
        }
    }

    @Override
    public Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> getAttributeInstances() {
        return instances;
    }
}
