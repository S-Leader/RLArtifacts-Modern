package artifacts.item.wearable.necklace;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Combines the Thorn, Flame and Shock Pendant effects from the original mod. */
public class UltimatePendantItem extends WearableArtifactItem {

    @Override
    protected boolean hasNonCosmeticEffects() {
        return ModGameRules.THORN_PENDANT_STRIKE_CHANCE.get() > 0
                || ModGameRules.FLAME_PENDANT_STRIKE_CHANCE.get() > 0
                || ModGameRules.SHOCK_PENDANT_STRIKE_CHANCE.get() > 0
                || ModGameRules.SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE.get();
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("description"));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_CHAIN;
    }
}
