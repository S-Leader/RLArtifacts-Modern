package artifacts.network;

import artifacts.item.wearable.belt.CloudInABottleItem;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpPacket {
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
                for (int i = 0; i < 20; ++i) {
                    double motionX = player.getRandom().nextGaussian() * 0.02;
                    double motionY = player.getRandom().nextGaussian() * 0.02 + 0.20;
                    double motionZ = player.getRandom().nextGaussian() * 0.02;
                    ParticleOptions particleType = player.isInWater() ? ParticleTypes.BUBBLE : ParticleTypes.POOF;
                    player.serverLevel().sendParticles(particleType, player.getX(), player.getY(), player.getZ(), 1, motionX, motionY, motionZ, 0.15);
                }
            });
        }
        context.get().setPacketHandled(true);
    }
}
