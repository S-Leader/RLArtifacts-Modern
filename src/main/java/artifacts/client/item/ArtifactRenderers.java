package artifacts.client.item;

import artifacts.client.item.model.*;
import artifacts.client.item.renderer.*;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

import java.util.function.Supplier;

public class ArtifactRenderers {

    public static void register() {
        // head
        register(ModItems.PLASTIC_DRINKING_HAT.get(), () -> new GenericArtifactRenderer("plastic_drinking_hat", new HeadModel(bakeLayer(ArtifactLayers.DRINKING_HAT))));

        // necklace
        register(ModItems.PANIC_NECKLACE.get(), () -> new LegacyAmuletArtifactRenderer("panic_necklace", new LegacyAmuletModel(bakeLayer(ArtifactLayers.PANIC_NECKLACE))));
        register(ModItems.SHOCK_PENDANT.get(), () -> new LegacyAmuletArtifactRenderer("shock_pendant", new LegacyAmuletModel(bakeLayer(ArtifactLayers.PENDANT))));
        register(ModItems.FLAME_PENDANT.get(), () -> new LegacyAmuletArtifactRenderer("flame_pendant", new LegacyAmuletModel(bakeLayer(ArtifactLayers.PENDANT))));
        register(ModItems.THORN_PENDANT.get(), () -> new LegacyAmuletArtifactRenderer("thorn_pendant", new LegacyAmuletModel(bakeLayer(ArtifactLayers.PENDANT))));
        register(ModItems.ULTIMATE_PENDANT.get(), () -> new LegacyAmuletArtifactRenderer("ultimate_pendant", new LegacyAmuletModel(bakeLayer(ArtifactLayers.ULTIMATE_PENDANT))));
        register(ModItems.SACRIFICIAL_AMULET.get(), () -> new LegacyAmuletArtifactRenderer("sacrificial_amulet", new LegacyAmuletModel(bakeLayer(ArtifactLayers.LEGACY_AMULET))));

        // belt
        register(ModItems.CLOUD_IN_A_BOTTLE.get(), () -> new BeltArtifactRenderer("cloud_in_a_bottle", BeltModel.createCloudInABottleModel()));
        register(ModItems.BOTTLED_FART.get(), () -> new BeltArtifactRenderer("bottled_fart", BeltModel.createCloudInABottleModel()));
        register(ModItems.ANTIDOTE_VESSEL.get(), () -> new BeltArtifactRenderer("antidote_vessel", BeltModel.createAntidoteVesselModel()));
        register(ModItems.BUBBLE_WRAP.get(), () -> new BubbleWrapArtifactRenderer(new BubbleWrapModel(bakeLayer(ArtifactLayers.BUBBLE_WRAP))));
        register(ModItems.WHOOPEE_CUSHION.get(), () -> new GenericArtifactRenderer("whoopee_cushion", new HeadModel(bakeLayer(ArtifactLayers.WHOOPEE_CUSHION))));

        // hands
        register(ModItems.FERAL_CLAWS.get(), () -> new GloveArtifactRenderer("feral_claws", "feral_claws", ArmsModel::createClawsModel));
        register(ModItems.POWER_GLOVE.get(), () -> new GloveArtifactRenderer("power_glove", ArmsModel::createGloveModel));
        register(ModItems.MECHANICAL_GLOVE.get(), () -> new GloveArtifactRenderer("mechanical_glove", ArmsModel::createLegacyGloveModel));
        register(ModItems.FIRE_GAUNTLET.get(), () -> new GlowingGloveArtifactRenderer("fire_gauntlet", ArmsModel::createGloveModel));
        register(ModItems.POCKET_PISTON.get(), () -> new GloveArtifactRenderer("pocket_piston", ArmsModel::createPocketPistonModel));
        register(ModItems.VAMPIRIC_GLOVE.get(), () -> new GloveArtifactRenderer("vampiric_glove", ArmsModel::createGloveModel));
        register(ModItems.ELECTRIC_SHOCK_GLOVE.get(), () -> new GloveArtifactRenderer("electric_shock_glove", ArmsModel::createGloveModel));
        register(ModItems.MAGMA_STONE.get(), () -> new FullBrightGloveArtifactRenderer(
                "fire_gauntlet/fire_gauntlet_wide_overlay",
                "fire_gauntlet/fire_gauntlet_slim_overlay",
                ArmsModel::createGloveModel
        ));

        // body
        register(ModItems.STAR_CLOAK.get(), () -> new GlowingArtifactRenderer("star_cloak", new StarCloakModel(bakeLayer(ArtifactLayers.STAR_CLOAK))));
    }

    public static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
    }

    public static void register(WearableArtifactItem item, Supplier<ArtifactRenderer> rendererSupplier) {
        PlatformServices.platformHelper.registerArtifactRenderer(item, rendererSupplier);
    }
}
