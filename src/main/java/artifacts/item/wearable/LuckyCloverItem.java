package artifacts.item.wearable;

import artifacts.Artifacts;
import artifacts.registry.ModGameRules;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class LuckyCloverItem extends WearableArtifactItem {

    public LuckyCloverItem() {
        addAttributeModifier(ArtifactAttributeModifier.create(
                Attributes.LUCK,
                UUID.fromString("aebc0384-0d15-44f7-8ebc-c41f251f84dd"),
                Artifacts.id("lucky_clover_luck_bonus").toString(),
                () -> ModGameRules.LUCKY_CLOVER_LUCK_BONUS.get().doubleValue()
        ));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
