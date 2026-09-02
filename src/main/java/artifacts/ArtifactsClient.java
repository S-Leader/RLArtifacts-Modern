package artifacts;

import artifacts.client.ArmRenderHandler;
import artifacts.client.ArtifactCooldownOverlayRenderer;
import artifacts.client.CloudInABottleInputHandler;
import artifacts.client.UmbrellaArmPoseHandler;
import artifacts.client.hallowstar.HallowStarModel;
import artifacts.client.hallowstar.HallowStarRenderer;
import artifacts.client.item.ArtifactLayers;
import artifacts.client.item.ArtifactRenderers;
import artifacts.client.mimic.MimicRenderer;
import artifacts.client.mimic.model.MimicChestLayerModel;
import artifacts.client.mimic.model.MimicModel;
import artifacts.mixins.accessors.client.LivingEntityRendererAccessor;
import artifacts.registry.ModEntityTypes;
import artifacts.registry.ModItems;
import artifacts.registry.ModLootTables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.client.render.CuriosLayer;

import java.util.Set;

public class ArtifactsClient {

    public ArtifactsClient(IEventBus modBus) {
        CloudInABottleInputHandler.register();

        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onRegisterLayerDefinitions);
        modBus.addListener(this::onRegisterRenderers);
        modBus.addListener(this::onRegisterGuiOverlays);
        modBus.addListener(this::onAddLayers);

        ArmRenderHandler.setup();
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.UMBRELLA.get(), Artifacts.id("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0));
        ArtifactRenderers.register();
        UmbrellaArmPoseHandler.setup();
    }

    public void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "artifact_cooldowns", ArtifactCooldownOverlayRenderer::render);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void onAddLayers(EntityRenderersEvent.AddLayers event) {
        Set<EntityType<?>> entities = ModLootTables.ENTITY_EQUIPMENT.keySet();
        loop:
        for (EntityType<?> entity : entities) {
            EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entity);
            if (renderer == null) {
                continue;
            }
            LivingEntityRenderer livingEntityRenderer = (LivingEntityRenderer<?, ?>) renderer;
            for (RenderLayer<?, ?> layer : ((LivingEntityRendererAccessor<?, ?>) livingEntityRenderer).getLayers()) {
                if (layer instanceof CuriosLayer<?, ?>) {
                    continue loop;
                }
            }
            livingEntityRenderer.addLayer(new CuriosLayer<>(livingEntityRenderer));
        }
    }

    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ArtifactLayers.register(event);
        event.registerLayerDefinition(MimicModel.LAYER_LOCATION, MimicModel::createLayer);
        event.registerLayerDefinition(MimicChestLayerModel.LAYER_LOCATION, MimicChestLayerModel::createLayer);
        event.registerLayerDefinition(HallowStarModel.LAYER_LOCATION, HallowStarModel::createLayer);
    }

    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.MIMIC.get(), MimicRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.HALLOW_STAR.get(), HallowStarRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ELECTRIC_SPARK.get(), NoopRenderer::new);
    }
}
