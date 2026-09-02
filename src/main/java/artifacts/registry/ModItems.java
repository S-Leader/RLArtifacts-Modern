package artifacts.registry;

import artifacts.Artifacts;
import artifacts.item.UmbrellaItem;
import artifacts.item.wearable.LuckyCloverItem;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.item.wearable.WhoopeeCushionItem;
import artifacts.item.wearable.belt.*;
import artifacts.item.wearable.body.StarCloakItem;
import artifacts.item.wearable.hands.*;
import artifacts.item.wearable.head.DrinkingHatItem;
import artifacts.item.wearable.necklace.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Artifacts.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Artifacts.MOD_ID);
    private static final List<RegistrySupplier<? extends Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = RegistrySupplier.of(CREATIVE_MODE_TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("%s.creative_tab".formatted(Artifacts.MOD_ID)))
                    .icon(() -> new ItemStack(ModItems.STAR_CLOAK.get()))
                    .displayItems((parameters, output) -> CREATIVE_TAB_ITEMS.forEach(item -> output.accept(item.get())))
                    .build()
    ));

    public static final RegistrySupplier<Item> MIMIC_SPAWN_EGG = register("mimic_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.MIMIC::get, 0x805113, 0x212121, new Item.Properties()));
    public static final RegistrySupplier<Item> UMBRELLA = register("umbrella", UmbrellaItem::new);

    // head
    public static final RegistrySupplier<DrinkingHatItem> PLASTIC_DRINKING_HAT = register("plastic_drinking_hat", () -> new DrinkingHatItem(ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER, ModGameRules.PLASTIC_DRINKING_HAT_EATING_DURATION_MULTIPLIER, false));

    // necklace
    public static final RegistrySupplier<WearableArtifactItem> PANIC_NECKLACE = register("panic_necklace", PanicNecklaceItem::new);
    public static final RegistrySupplier<WearableArtifactItem> SHOCK_PENDANT = register("shock_pendant", ShockPendantItem::new);
    public static final RegistrySupplier<WearableArtifactItem> FLAME_PENDANT = register("flame_pendant", FlamePendantItem::new);
    public static final RegistrySupplier<WearableArtifactItem> THORN_PENDANT = register("thorn_pendant", ThornPendantItem::new);
    public static final RegistrySupplier<WearableArtifactItem> SACRIFICIAL_AMULET = register("sacrificial_amulet", SacrificialAmuletItem::new);

    // belt
    public static final RegistrySupplier<WearableArtifactItem> CLOUD_IN_A_BOTTLE = register("cloud_in_a_bottle", CloudInABottleItem::new);
    public static final RegistrySupplier<WearableArtifactItem> ANTIDOTE_VESSEL = register("antidote_vessel", AntidoteVesselItem::new);
    public static final RegistrySupplier<WearableArtifactItem> BUBBLE_WRAP = register("bubble_wrap", BubbleWrapItem::new);
    public static final RegistrySupplier<WearableArtifactItem> WHOOPEE_CUSHION = register("whoopee_cushion", WhoopeeCushionItem::new);

    // hands
    public static final RegistrySupplier<WearableArtifactItem> FERAL_CLAWS = register("feral_claws", FeralClawsItem::new);
    public static final RegistrySupplier<WearableArtifactItem> POWER_GLOVE = register("power_glove", PowerGloveItem::new);
    public static final RegistrySupplier<WearableArtifactItem> MECHANICAL_GLOVE = register("mechanical_glove", MechanicalGloveItem::new);
    public static final RegistrySupplier<WearableArtifactItem> FIRE_GAUNTLET = register("fire_gauntlet", FireGauntletItem::new);
    public static final RegistrySupplier<WearableArtifactItem> POCKET_PISTON = register("pocket_piston", PocketPistonItem::new);
    public static final RegistrySupplier<WearableArtifactItem> VAMPIRIC_GLOVE = register("vampiric_glove", VampiricGloveItem::new);
    public static final RegistrySupplier<WearableArtifactItem> ELECTRIC_SHOCK_GLOVE = register("electric_shock_glove", ElectricShockGloveItem::new);
    public static final RegistrySupplier<WearableArtifactItem> MAGMA_STONE = register("magma_stone", MagmaStoneItem::new);

    // charm
    public static final RegistrySupplier<WearableArtifactItem> LUCKY_CLOVER = register("lucky_clover", LuckyCloverItem::new);

    // body
    public static final RegistrySupplier<WearableArtifactItem> STAR_CLOAK = register("star_cloak", StarCloakItem::new);

    private static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> supplier) {
        RegistrySupplier<T> registered = RegistrySupplier.of(ITEMS.register(name, supplier));
        CREATIVE_TAB_ITEMS.add(registered);
        return registered;
    }
}
