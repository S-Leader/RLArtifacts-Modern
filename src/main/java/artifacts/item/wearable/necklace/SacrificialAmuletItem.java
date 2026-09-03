package artifacts.item.wearable.necklace;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModLootTables;
import artifacts.util.DamageSourceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;

/**
 * The RLArtifacts 1.12 sacrificial amulet: 99 kills consume it and roll its reward table.
 */
public class SacrificialAmuletItem extends WearableArtifactItem {

    public static final String CHARGE_TAG = "Sacrificial Amulet Charge";

    public SacrificialAmuletItem() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDeath);
    }

    @Override
    protected boolean hasNonCosmeticEffects() {
        return ModGameRules.SACRIFICIAL_AMULET_REQUIRED_KILLS.get() > 0;
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("description"));
        tooltip.add(tooltipLine(
                "charge",
                stack.getOrCreateTag().getInt(CHARGE_TAG),
                ModGameRules.SACRIFICIAL_AMULET_REQUIRED_KILLS.get()
        ).withStyle(ChatFormatting.DARK_RED));
    }

    private void onLivingDeath(LivingDeathEvent event) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(event.getSource());
        if (!(attacker instanceof Player player) || !(player.level() instanceof ServerLevel level)) {
            return;
        }

        ItemStack amulet = findAllEquippedBy(player).findFirst().orElse(ItemStack.EMPTY);
        if (amulet.isEmpty()) {
            return;
        }

        int requiredKills = ModGameRules.SACRIFICIAL_AMULET_REQUIRED_KILLS.get();
        int charge = amulet.getOrCreateTag().getInt(CHARGE_TAG) + 1;
        if (requiredKills > 0 && charge >= requiredKills) {
            amulet.shrink(1);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE,
                    SoundSource.PLAYERS, 0.5F, 0.8F + player.getRandom().nextFloat() * 0.4F);

            LootTable table = level.getServer().getLootData().getLootTable(ModLootTables.SACRIFICIAL_REWARD);
            LootParams params = new LootParams.Builder(level)
                    .withLuck(player.getLuck())
                    .create(LootContextParamSets.EMPTY);
            table.getRandomItems(params, player.getRandom().nextLong(), stack -> player.spawnAtLocation(stack, 0.5F));
        } else {
            amulet.getOrCreateTag().putInt(CHARGE_TAG, charge);
        }
    }
}
