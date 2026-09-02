package artifacts.item.wearable.body;

import artifacts.entity.HallowStarEntity;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import artifacts.util.DamageSourceHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class StarCloakItem extends WearableArtifactItem {

    public StarCloakItem() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return ModGameRules.STAR_CLOAK_MAX_STARS.get() > 0 && ModGameRules.STAR_CLOAK_DAMAGE.get() > 0;
    }

    private void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (!(entity instanceof Player player)
                || entity.level().isClientSide()
                || !isEquippedBy(player)
                || isOnCooldown(player)
                || !(damageSource.getEntity() instanceof Mob)) {
            return;
        }

        boolean allowedAttack = ModGameRules.STAR_CLOAK_ALLOW_INDIRECT_ATTACKS.get()
                || DamageSourceHelper.isMeleeAttack(damageSource)
                && entity.level().canSeeSky(entity.blockPosition().above());
        if (!allowedAttack) {
            return;
        }

        int minimum = ModGameRules.STAR_CLOAK_MIN_STARS.get();
        int maximum = Math.max(minimum, ModGameRules.STAR_CLOAK_MAX_STARS.get());
        int stars = minimum + (maximum > minimum ? entity.getRandom().nextInt(maximum - minimum + 1) : 0);
        for (int i = 0; i < stars; i++) {
            entity.level().addFreshEntity(new HallowStarEntity(entity.level(), player));
        }
        if (stars > 0) {
            addCooldown(player, ModGameRules.STAR_CLOAK_COOLDOWN.get());
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
