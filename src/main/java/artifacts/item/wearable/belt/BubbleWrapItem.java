package artifacts.item.wearable.belt;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class BubbleWrapItem extends WearableArtifactItem {

    public BubbleWrapItem() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return true;
    }

    @Override
    public void wornTick(LivingEntity entity, ItemStack stack) {
        super.wornTick(entity, stack);
        if (entity.level().isClientSide()) return;
        CompoundTag tag = stack.getOrCreateTag();
        boolean wasCompressed = tag.getBoolean("Compressed");
        if (wasCompressed && !entity.isShiftKeyDown()) {
            tag.putBoolean("Compressed", false);
        } else if (!wasCompressed && entity.isShiftKeyDown()) {
            tag.putBoolean("Compressed", true);
            if (entity.getRandom().nextInt(3) == 0) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.BUBBLE_WRAP.get(), SoundSource.PLAYERS, 1.0F, 0.9F + entity.getRandom().nextFloat() * 0.2F);
            }
        }
    }

    private void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide() && event.getSource().is(DamageTypes.FLY_INTO_WALL) && isEquippedBy(entity)) {
            event.setCanceled(true);
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return ModSoundEvents.BUBBLE_WRAP.get();
    }
}
