package artifacts.registry;

import artifacts.Artifacts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Artifacts.MOD_ID);

    public static final RegistrySupplier<SoundEvent>
            POP = register("generic.pop"),
            MIMIC_HURT = register("entity.mimic.hurt"),
            MIMIC_DEATH = register("entity.mimic.death"),
            MIMIC_OPEN = register("entity.mimic.open"),
            MIMIC_CLOSE = register("entity.mimic.close"),
            FART = register("item.whoopee_cushion.fart"),
            BUBBLE_WRAP = register("item.bubble_wrap.pop"),
            WATER_STEP = register("block.water.step");

    private static RegistrySupplier<SoundEvent> register(String name) {
        return RegistrySupplier.of(SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Artifacts.id(name))));
    }
}
