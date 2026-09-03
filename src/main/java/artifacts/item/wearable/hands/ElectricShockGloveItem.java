package artifacts.item.wearable.hands;

import artifacts.entity.ElectricSparkEntity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Vampiric glove variant that sprays lingering, gravity-affected sparks.
 */
public class ElectricShockGloveItem extends VampiricGloveItem {

    public static void spawnSparks(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel level)) {
            return;
        }

        Vec3 look = attacker.getLookAngle().normalize();
        Vec3 origin = attacker.position()
                .add(0.0D, attacker.getBbHeight() * 0.62D, 0.0D)
                .add(look.scale(0.55D));

        for (int i = 0; i < 6; i++) {
            ElectricSparkEntity spark = new ElectricSparkEntity(level, attacker);
            spark.setPos(origin.x, origin.y, origin.z);
            spark.setDeltaMovement(
                    look.x * 0.72D + attacker.getRandom().nextGaussian() * 0.12D,
                    look.y * 0.38D + 0.18D + attacker.getRandom().nextDouble() * 0.10D,
                    look.z * 0.72D + attacker.getRandom().nextGaussian() * 0.12D
            );
            level.addFreshEntity(spark);
        }
    }

    @Override
    public boolean hasNonCosmeticEffects() {
        return true;
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        super.addEffectsTooltip(stack, tooltip);
        tooltip.add(tooltipLine("sparks"));
    }
}
