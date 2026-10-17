package artifacts.integration;

import artifacts.Artifacts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;
import java.util.List;

public final class TalentsCompat {

    private static final String TALENTS_MOD_ID = "talents";
    private static final String UMBRELLA_ID = "artifacts:umbrella";

    private static boolean initializationAttempted;
    private static boolean available;
    private static boolean warned;

    private static Method getCapability;
    private static Method getRequirements;
    private static Method getRequiredAptitude;
    private static Method getRequiredLevel;
    private static Method getAptitudeLevel;

    private TalentsCompat() {
    }

    public static boolean canUseUmbrella(LivingEntity entity) {
        if (!(entity instanceof Player player) || !ModList.get().isLoaded(TALENTS_MOD_ID)) {
            return true;
        }
        if (!initialize()) {
            return true;
        }

        try {
            Object capability = getCapability.invoke(null, player);
            if (capability == null) {
                return true;
            }

            Object value = getRequirements.invoke(null, UMBRELLA_ID);
            if (!(value instanceof List<?> requirements)) {
                return true;
            }

            for (Object requirement : requirements) {
                Object aptitude = getRequiredAptitude.invoke(requirement);
                int requiredLevel = ((Number) getRequiredLevel.invoke(requirement)).intValue();
                int playerLevel = ((Number) getAptitudeLevel.invoke(capability, aptitude)).intValue();
                if (playerLevel < requiredLevel) {
                    return false;
                }
            }
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            warnOnce(exception);
            return true;
        }
    }

    private static synchronized boolean initialize() {
        if (initializationAttempted) {
            return available;
        }
        initializationAttempted = true;

        try {
            Class<?> capabilityClass = Class.forName("com.seniors.talents.common.capability.AptitudeCapability");
            Class<?> handlerClass = Class.forName("com.seniors.talents.handler.HandlerAptitude");
            Class<?> requirementClass = Class.forName("com.seniors.talents.client.core.Aptitudes");
            Class<?> aptitudeClass = Class.forName("com.seniors.talents.registry.aptitude.Aptitude");

            getCapability = capabilityClass.getMethod("get", Player.class);
            getRequirements = handlerClass.getMethod("getValue", String.class);
            getRequiredAptitude = requirementClass.getMethod("getAptitude");
            getRequiredLevel = requirementClass.getMethod("getAptitudeLvl");
            getAptitudeLevel = capabilityClass.getMethod("getAptitudeLevel", aptitudeClass);
            available = true;
        } catch (ReflectiveOperationException | LinkageError exception) {
            warnOnce(exception);
        }
        return available;
    }

    private static void warnOnce(Throwable throwable) {
        if (!warned) {
            warned = true;
            Artifacts.LOGGER.warn("Failed to query Talents item requirements; umbrella effects will remain enabled", throwable);
        }
    }
}
