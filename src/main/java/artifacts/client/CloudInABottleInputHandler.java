package artifacts.client;

import artifacts.item.wearable.belt.CloudInABottleItem;
import artifacts.network.DoubleJumpPacket;
import artifacts.network.NetworkHandler;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class CloudInABottleInputHandler {

    private static boolean canDoubleJump;
    private static boolean hasReleasedJumpKey;

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(CloudInABottleInputHandler::onClientTick);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft instance = Minecraft.getInstance();
        LocalPlayer player = instance.player;
        if (player != null && player.input != null) {
            handleCloudInABottleInput(player);
        }
    }

    private static void handleCloudInABottleInput(LocalPlayer player) {
        if ((player.onGround() || player.onClimbable()) && !player.isInWater()) {
            hasReleasedJumpKey = false;
            canDoubleJump = true;
        } else if (!player.input.jumping) {
            hasReleasedJumpKey = true;
        } else if (!player.getAbilities().flying && canDoubleJump && hasReleasedJumpKey) {
            canDoubleJump = false;
            boolean fart = ModItems.BOTTLED_FART.get().isEquippedBy(player);
            if ((fart || ModItems.CLOUD_IN_A_BOTTLE.get().isEquippedBy(player)) && ModGameRules.CLOUD_IN_A_BOTTLE_ENABLED.get()) {
                NetworkHandler.CHANNEL.sendToServer(new DoubleJumpPacket(fart));
                CloudInABottleItem.jump(player, fart);
            }
        }
    }
}
