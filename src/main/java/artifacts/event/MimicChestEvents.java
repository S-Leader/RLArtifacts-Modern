package artifacts.event;

import artifacts.Artifacts;
import artifacts.entity.MimicEntity;
import artifacts.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

/**
 * Restores RLArtifacts 1.12's unlooted-chest mimic replacement mechanic.
 */
public final class MimicChestEvents {

    private MimicChestEvents() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, MimicChestEvents::onRightClickBlock);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, MimicChestEvents::onBlockBreak);
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getUseBlock() == Event.Result.DENY
                || event.getLevel().isClientSide()
                || event.getEntity().isSpectator()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        if (state.getBlock() instanceof ChestBlock && ChestBlock.isChestBlockedAt(event.getLevel(), pos)) {
            return;
        }

        if (tryReplaceUnlootedChest(event.getEntity(), pos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof Level level)
                || level.isClientSide()
                || event.getPlayer().isSpectator()) {
            return;
        }

        if (tryReplaceUnlootedChest(event.getPlayer(), event.getPos())) {
            event.setCanceled(true);
        }
    }

    private static boolean tryReplaceUnlootedChest(Player player, BlockPos pos) {
        Level level = player.level();
        if (!level.dimension().equals(Level.OVERWORLD)
                || Artifacts.CONFIG.common.getUnlootedChestMimicChance() <= 0.0D) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(state.getBlock() instanceof ChestBlock)
                || !(blockEntity instanceof ChestBlockEntity chest)
                || !chest.saveWithoutMetadata().contains("LootTable", Tag.TAG_STRING)) {
            return false;
        }

        // The original generated the loot before rolling. If the roll fails,
        // the chest opens/breaks normally and can never be rolled a second time.
        chest.unpackLootTable(player);
        if (level.random.nextDouble() > Artifacts.CONFIG.common.getUnlootedChestMimicChance()) {
            return false;
        }

        MimicEntity mimic = ModEntityTypes.MIMIC.get().create(level);
        if (mimic == null) {
            return false;
        }

        mimic.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        if (state.hasProperty(ChestBlock.FACING)) {
            mimic.setFacing(state.getValue(ChestBlock.FACING));
        }
        mimic.setPersistenceRequired();
        mimic.setTarget(player);
        level.removeBlock(pos, false);
        level.addFreshEntity(mimic);
        return true;
    }
}
