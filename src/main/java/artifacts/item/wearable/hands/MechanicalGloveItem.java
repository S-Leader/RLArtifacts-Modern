package artifacts.item.wearable.hands;

import artifacts.Artifacts;
import artifacts.item.wearable.ArtifactAttributeModifier;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

/**
 * Combines the Power Glove's damage bonus and the Feral Claws' speed bonus.
 */
public class MechanicalGloveItem extends WearableArtifactItem {

    public MechanicalGloveItem() {
        addAttributeModifier(ArtifactAttributeModifier.create(
                Attributes.ATTACK_SPEED,
                UUID.fromString("66132757-820e-41f2-ab1c-4bf13173736e"),
                Artifacts.id("mechanical_glove_attack_speed_bonus").toString(),
                ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS
        ));
        addAttributeModifier(ArtifactAttributeModifier.create(
                Attributes.ATTACK_DAMAGE,
                UUID.fromString("a82c0055-bafd-46fc-92bc-e7e2e06e0dfb"),
                Artifacts.id("mechanical_glove_attack_damage_bonus").toString(),
                () -> (double) ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS.get()
        ));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }
}
