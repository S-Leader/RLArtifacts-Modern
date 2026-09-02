package artifacts.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySoundAtPlayerPacket {
    private final SoundEvent soundEvent;
    private final float volume;
    private final float pitch;
    private final long seed;

    public PlaySoundAtPlayerPacket(FriendlyByteBuf buffer) {
        this.soundEvent = BuiltInRegistries.SOUND_EVENT.get(buffer.readResourceLocation());
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.seed = buffer.readLong();
    }
    public PlaySoundAtPlayerPacket(SoundEvent soundEvent, float volume, float pitch, long seed) {
        this.soundEvent = soundEvent; this.volume = volume; this.pitch = pitch; this.seed = seed;
    }
    public static void sendSound(ServerPlayer player, SoundEvent soundEvent, float volume, float pitch) {
        long seed = player.level().random.nextLong();
        NetworkHandler.CHANNEL.sendToPlayer(player, new PlaySoundAtPlayerPacket(soundEvent, volume, pitch, seed));
        player.level().playSeededSound(player, player, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent), SoundSource.PLAYERS, volume, pitch, seed);
    }
    void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(BuiltInRegistries.SOUND_EVENT.getKey(soundEvent));
        buffer.writeFloat(volume); buffer.writeFloat(pitch); buffer.writeLong(seed);
    }
    void apply(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientNetworkHandler.playSound(soundEvent, volume, pitch, seed)));
        context.get().setPacketHandled(true);
    }
}
