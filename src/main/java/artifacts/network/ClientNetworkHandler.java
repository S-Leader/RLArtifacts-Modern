package artifacts.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientNetworkHandler {
    private ClientNetworkHandler() {
    }

    public static void playSound(SoundEvent soundEvent, float volume, float pitch, long seed) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.level().playSeededSound(player, player, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent), SoundSource.PLAYERS, volume, pitch, seed);
        }
    }
}
