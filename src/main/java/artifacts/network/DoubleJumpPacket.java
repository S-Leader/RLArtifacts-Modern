package artifacts.network;

import artifacts.item.wearable.belt.CloudInABottleItem;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class DoubleJumpPacket {
    private static final ParticleOptions FART_PARTICLE = new DustParticleOptions(
            new Vector3f(194F / 255F, 209F / 255F, 131F / 255F),
            1.0F
    );

    private final boolean fart;

    public DoubleJumpPacket(FriendlyByteBuf buffer) {
        fart = buffer.readBoolean();
    }

    public DoubleJumpPacket() {
        this(false);
    }

    public DoubleJumpPacket(boolean fart) {
        this.fart = fart;
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(fart);
    }

    void apply(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null && ModGameRules.CLOUD_IN_A_BOTTLE_ENABLED.get()) {
            context.get().enqueueWork(() -> {
                boolean hasRequiredBottle = fart
                        ? ModItems.BOTTLED_FART.get().isEquippedBy(player)
                        : ModItems.CLOUD_IN_A_BOTTLE.get().isEquippedBy(player);
                if (!hasRequiredBottle) {
                    return;
                }
                CloudInABottleItem.jump(player, fart);
                ParticleOptions particleType = player.isInWater()
                        ? ParticleTypes.BUBBLE
                        : fart ? FART_PARTICLE : ParticleTypes.POOF;

                // Match the original 1.12.2 nine-particle footprint: the four
                // corners are pulled inward while the centre and cardinal
                // points remain on the one-block grid. Particles have no
                // random spread or upward velocity.
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        boolean corner = x != 0 && z != 0;
                        double offsetX = corner ? x / 1.5D : x;
                        double offsetZ = corner ? z / 1.5D : z;
                        player.serverLevel().sendParticles(
                                particleType,
                                player.getX() + offsetX,
                                player.getY(),
                                player.getZ() + offsetZ,
                                1,
                                0.0D,
                                0.0D,
                                0.0D,
                                0.0D
                        );
                    }
                }
            });
        }
        context.get().setPacketHandled(true);
    }
}
