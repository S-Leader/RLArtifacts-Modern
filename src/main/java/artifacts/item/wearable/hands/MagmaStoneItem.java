package artifacts.item.wearable.hands;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import artifacts.util.DamageSourceHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MagmaStoneItem extends WearableArtifactItem {

    public MagmaStoneItem() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return ModGameRules.MAGMA_STONE_FIRE_DURATION.get() > 0;
    }

    private void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker instanceof Player
                && isEquippedBy(attacker)
                && DamageSourceHelper.isMeleeAttack(damageSource)
                && !entity.fireImmune()) {
            entity.setSecondsOnFire(ModGameRules.MAGMA_STONE_FIRE_DURATION.get() / 20);
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
