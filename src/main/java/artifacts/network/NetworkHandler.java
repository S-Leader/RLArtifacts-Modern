package artifacts.network;

import artifacts.Artifacts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Collection;

public class NetworkHandler {

    private static final String PROTOCOL = "1";
    public static final Channel CHANNEL = new Channel();

    public static void register() {
        int id = 0;
        CHANNEL.channel.registerMessage(id++, BooleanGameRuleChangedPacket.class, BooleanGameRuleChangedPacket::encode, BooleanGameRuleChangedPacket::new, BooleanGameRuleChangedPacket::apply);
        CHANNEL.channel.registerMessage(id++, IntegerGameRuleChangedPacket.class, IntegerGameRuleChangedPacket::encode, IntegerGameRuleChangedPacket::new, IntegerGameRuleChangedPacket::apply);
        CHANNEL.channel.registerMessage(id++, DoubleJumpPacket.class, DoubleJumpPacket::encode, DoubleJumpPacket::new, DoubleJumpPacket::apply);
        CHANNEL.channel.registerMessage(id, PlaySoundAtPlayerPacket.class, PlaySoundAtPlayerPacket::encode, PlaySoundAtPlayerPacket::new, PlaySoundAtPlayerPacket::apply);
    }

    public static final class Channel {
        private final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                Artifacts.id("networking_channel"),
                () -> PROTOCOL,
                PROTOCOL::equals,
                PROTOCOL::equals
        );

        public void sendToServer(Object message) {
            channel.sendToServer(message);
        }

        public void sendToPlayer(ServerPlayer player, Object message) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), message);
        }

        public void sendToPlayers(Collection<ServerPlayer> players, Object message) {
            for (ServerPlayer player : players) {
                sendToPlayer(player, message);
            }
        }
    }
}
