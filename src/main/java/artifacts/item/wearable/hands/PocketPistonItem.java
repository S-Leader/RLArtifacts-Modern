package artifacts.item.wearable.hands;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import artifacts.util.DamageSourceHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class PocketPistonItem extends WearableArtifactItem {

    public PocketPistonItem() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return ModGameRules.POCKET_PISTON_KNOCKBACK_STRENGTH.get() > 0;
    }

    private void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && isEquippedBy(attacker)) {
            double knockbackBonus = ModGameRules.POCKET_PISTON_KNOCKBACK_STRENGTH.get();
            entity.knockback(knockbackBonus, Mth.sin((float) (attacker.getYRot() * (Math.PI / 180))), -Mth.cos((float) (attacker.getYRot() * (Math.PI / 180))));
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.PISTON_EXTEND;
    }
}
