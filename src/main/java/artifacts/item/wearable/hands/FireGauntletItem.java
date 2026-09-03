package artifacts.item.wearable.hands;

import artifacts.Artifacts;
import artifacts.item.wearable.ArtifactAttributeModifier;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import artifacts.util.DamageSourceHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.UUID;

public class FireGauntletItem extends WearableArtifactItem {

    public FireGauntletItem() {
        addAttributeModifier(ArtifactAttributeModifier.create(
                Attributes.ATTACK_SPEED,
                UUID.fromString("0e2a81e5-8b5a-47a8-883c-7d4d971147ac"),
                Artifacts.id("fire_gauntlet_attack_speed_bonus").toString(),
                ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS
        ));
        addAttributeModifier(ArtifactAttributeModifier.create(
                Attributes.ATTACK_DAMAGE,
                UUID.fromString("715e2422-d2e9-473c-aaf9-2d194b284818"),
                Artifacts.id("fire_gauntlet_attack_damage_bonus").toString(),
                () -> (double) ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS.get()
        ));
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return ModGameRules.FIRE_GAUNTLET_FIRE_DURATION.get() > 0;
    }

    private void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (isEquippedBy(attacker) && DamageSourceHelper.isMeleeAttack(damageSource) && !entity.fireImmune()) {
            entity.setSecondsOnFire(ModGameRules.FIRE_GAUNTLET_FIRE_DURATION.get() / 20);
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }
}
