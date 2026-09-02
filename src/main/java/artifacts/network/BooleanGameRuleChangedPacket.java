package artifacts.network;

import artifacts.registry.ModGameRules;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BooleanGameRuleChangedPacket {
    private final String key;
    private final boolean value;
    public BooleanGameRuleChangedPacket(FriendlyByteBuf buffer) { key = buffer.readUtf(); value = buffer.readBoolean(); }
    public BooleanGameRuleChangedPacket(String key, boolean value) { this.key = key; this.value = value; }
    void encode(FriendlyByteBuf buffer) { buffer.writeUtf(key); buffer.writeBoolean(value); }
    void apply(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ModGameRules.updateValue(key, value));
        context.get().setPacketHandled(true);
    }
}
