package artifacts.event;

import artifacts.item.UmbrellaItem;
import artifacts.item.wearable.belt.CloudInABottleItem;
import artifacts.item.wearable.hands.VampiricGloveItem;
import artifacts.item.wearable.hands.ElectricShockGloveItem;
import artifacts.registry.ModItems;
import artifacts.util.DamageSourceHelper;
import artifacts.item.wearable.head.DrinkingHatItem;
import artifacts.registry.ModGameRules;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.UUID;

public class ArtifactEventsForge {

    private static final AttributeModifier UMBRELLA_SLOW_FALLING = new AttributeModifier(
            UUID.fromString("a7a25453-2065-4a96-bc83-df600e13f390"),
            "artifacts:umbrella_slow_falling",
            -0.875,
            AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    public static void register() {
        MimicChestEvents.register();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, ArtifactEventsForge::onLivingDamage);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ArtifactEventsForge::onLivingFall);
        MinecraftForge.EVENT_BUS.addListener(ArtifactEventsForge::onLivingUpdate);
        MinecraftForge.EVENT_BUS.addListener(ArtifactEventsForge::onDrinkingHatItemUse);
    }

    private static void onLivingDamage(LivingDamageEvent event) {
        VampiricGloveItem.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount());
        LivingEntity attacker = DamageSourceHelper.getAttacker(event.getSource());
        if (attacker != null
                && event.getAmount() > 0
                && DamageSourceHelper.isMeleeAttack(event.getSource())
                && ModItems.ELECTRIC_SHOCK_GLOVE.get().isEquippedBy(attacker)) {
            ElectricShockGloveItem.spawnSparks(attacker);
        }
    }

    private static void onLivingFall(LivingFallEvent event) {
        event.setDistance(CloudInABottleItem.getReducedFallDistance(event.getEntity(), event.getDistance()));
    }

    private static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        AttributeInstance gravity = entity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        if (gravity == null) {
            return;
        }

        boolean isInWater = entity.isInWater();
        if (ModGameRules.UMBRELLA_IS_GLIDER.get()
                && !entity.onGround()
                && !isInWater
                && entity.getDeltaMovement().y < 0
                && !entity.hasEffect(MobEffects.SLOW_FALLING)
                && UmbrellaItem.isHoldingUmbrellaUpright(entity)) {
            if (!gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
                gravity.addTransientModifier(UMBRELLA_SLOW_FALLING);
            }
            entity.fallDistance = 0;
        } else if (gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
            gravity.removeModifier(UMBRELLA_SLOW_FALLING);
        }
    }

    private static void onDrinkingHatItemUse(LivingEntityUseItemEvent.Start event) {
        event.setDuration(DrinkingHatItem.getDrinkingHatUseDuration(event.getEntity(), event.getItem().getUseAnimation(), event.getDuration()));
    }
}
