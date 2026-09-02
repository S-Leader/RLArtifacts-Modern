package artifacts;

import artifacts.config.ModConfig;
import artifacts.curio.WearableArtifactCurio;
import artifacts.entity.MimicEntity;
import artifacts.event.ArtifactEventsForge;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.network.NetworkHandler;
import artifacts.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

@Mod(Artifacts.MOD_ID)
public class Artifacts {

    public static final String MOD_ID = "artifacts";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ModConfig CONFIG;

    public Artifacts() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        NetworkHandler.register();

        ModSoundEvents.SOUND_EVENTS.register(modBus);
        ModLootConditions.LOOT_CONDITIONS.register(modBus);
        ModLootFunctions.LOOT_FUNCTIONS.register(modBus);
        ModPlacementModifierTypes.PLACEMENT_MODIFIERS.register(modBus);
        ModItems.CREATIVE_MODE_TABS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntityTypes.ENTITY_TYPES.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new ArtifactsClient(modBus);
        }

        ModLootModifiers.LOOT_MODIFIERS.register(modBus);
        modBus.addListener(this::onEntityAttributes);

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);

        registerConfig();
        ArtifactEventsForge.register();
    }

    private void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof WearableArtifactItem item) {
            event.addCapability(CuriosCapability.ID_ITEM, CurioItemCapability.createProvider(new WearableArtifactCurio(item, event.getObject())));
        }
    }

    private void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MIMIC.get(), MimicEntity.createMobAttributes().build());
    }

    private void onServerStarted(ServerStartedEvent event) {
        ModGameRules.onServerStarted(event.getServer());
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModGameRules.onPlayerJoinLevel(player);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation id(String path, String... args) {
        return new ResourceLocation(MOD_ID, String.format(path, (Object[]) args));
    }

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String path) {
        return ResourceKey.create(registry, id(path));
    }
}
