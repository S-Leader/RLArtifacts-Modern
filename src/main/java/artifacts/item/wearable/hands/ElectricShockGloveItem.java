package artifacts.item.wearable.hands;

import artifacts.entity.ElectricSparkEntity;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Vampiric glove variant that sprays lingering, gravity-affected sparks.
 */
public class ElectricShockGloveItem extends VampiricGloveItem {

    public static void handleMeleeHit(LivingEntity target, LivingEntity attacker, float attackDamage) {
        ElectricShockGloveItem glove = (ElectricShockGloveItem) ModItems.ELECTRIC_SHOCK_GLOVE.get();
        if (glove.isOnCooldown(attacker)) {
            return;
        }

        boolean absorbedHealth = absorbHealth(target, attacker, attackDamage);
        int sparks = spawnSparks(attacker, attackDamage);
        if (absorbedHealth || sparks > 0) {
            glove.addCooldown(attacker, ModGameRules.ELECTRIC_SHOCK_GLOVE_COOLDOWN.get());
        }
    }

    private static int spawnSparks(LivingEntity attacker, float attackDamage) {
        if (!(attacker.level() instanceof ServerLevel level)) {
            return 0;
        }

        int minimum = ModGameRules.ELECTRIC_SHOCK_GLOVE_MIN_SPARKS.get();
        int maximum = Math.max(minimum, ModGameRules.ELECTRIC_SHOCK_GLOVE_MAX_SPARKS.get());
        int damagePerSpark = Math.max(1, ModGameRules.ELECTRIC_SHOCK_GLOVE_DAMAGE_PER_SPARK.get());
        int sparkCount = Mth.clamp((int) Math.floor(attackDamage / damagePerSpark), minimum, maximum);
        if (sparkCount <= 0) {
            return 0;
        }

        Vec3 look = attacker.getLookAngle().normalize();
        Vec3 origin = attacker.position()
                .add(0.0D, attacker.getBbHeight() * 0.62D, 0.0D)
                .add(look.scale(0.55D));

        for (int i = 0; i < sparkCount; i++) {
            ElectricSparkEntity spark = new ElectricSparkEntity(level, attacker);
            spark.setPos(origin.x, origin.y, origin.z);
            spark.setDeltaMovement(
                    look.x * 0.72D + attacker.getRandom().nextGaussian() * 0.12D,
                    look.y * 0.38D + 0.18D + attacker.getRandom().nextDouble() * 0.10D,
                    look.z * 0.72D + attacker.getRandom().nextGaussian() * 0.12D
            );
            level.addFreshEntity(spark);
        }
        return sparkCount;
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
