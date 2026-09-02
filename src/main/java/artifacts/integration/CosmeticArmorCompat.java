package artifacts.integration;

import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

public class CosmeticArmorCompat {

    public static boolean areBootsHidden(Player player) {
        try {
            Class<?> apiClass = Class.forName("lain.mods.cos.api.CosArmorAPI");
            Method getData = apiClass.getMethod("getCAStacksClient", java.util.UUID.class);
            Object data = getData.invoke(null, player.getUUID());
            Method isSkinArmor = data.getClass().getMethod("isSkinArmor", int.class);
            return Boolean.TRUE.equals(isSkinArmor.invoke(data, 0));
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
}
