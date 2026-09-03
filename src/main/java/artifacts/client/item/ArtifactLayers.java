package artifacts.client.item;

import artifacts.Artifacts;
import artifacts.client.item.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class ArtifactLayers {

    public static final ModelLayerLocation
            DRINKING_HAT = createLayerLocation("drinking_hat"),
            PANIC_NECKLACE = createLayerLocation("panic_necklace"),
            PENDANT = createLayerLocation("pendant"),
            LEGACY_AMULET = createLayerLocation("legacy_amulet"),

    CLOUD_IN_A_BOTTLE = createLayerLocation("cloud_in_a_bottle"),
            ANTIDOTE_VESSEL = createLayerLocation("antidote_vessel"),
            BUBBLE_WRAP = createLayerLocation("bubble_wrap"),
            STAR_CLOAK = createLayerLocation("star_cloak"),

    CLAWS_WIDE = createLayerLocation("claws_wide"),
            CLAWS_SLIM = createLayerLocation("claws_slim"),
            GLOVE_WIDE = createLayerLocation("glove_wide"),
            GLOVE_SLIM = createLayerLocation("glove_slim"),
            LEGACY_GLOVE_WIDE = createLayerLocation("legacy_glove_wide"),
            LEGACY_GLOVE_SLIM = createLayerLocation("legacy_glove_slim"),
            POCKET_PISTON_WIDE = createLayerLocation("pocket_piston_wide"),
            POCKET_PISTON_SLIM = createLayerLocation("pocket_piston_slim"),

    WHOOPEE_CUSHION = createLayerLocation("whoopee_cushion");

    public static ModelLayerLocation claws(boolean hasSlimArms) {
        return hasSlimArms ? CLAWS_SLIM : CLAWS_WIDE;
    }

    public static ModelLayerLocation glove(boolean hasSlimArms) {
        return hasSlimArms ? GLOVE_SLIM : GLOVE_WIDE;
    }

    public static ModelLayerLocation legacyGlove(boolean hasSlimArms) {
        return hasSlimArms ? LEGACY_GLOVE_SLIM : LEGACY_GLOVE_WIDE;
    }

    public static ModelLayerLocation pocketPiston(boolean hasSlimArms) {
        return hasSlimArms ? POCKET_PISTON_SLIM : POCKET_PISTON_WIDE;
    }

    public static ModelLayerLocation createLayerLocation(String name) {
        return new ModelLayerLocation(Artifacts.id(name), name);
    }

    private static Supplier<LayerDefinition> layer(Supplier<MeshDefinition> mesh, int textureWidth, int textureHeight) {
        return () -> LayerDefinition.create(mesh.get(), textureWidth, textureHeight);
    }

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRINKING_HAT, layer(HeadModel::createDrinkingHat, 64, 32));

        event.registerLayerDefinition(PANIC_NECKLACE, layer(NecklaceModel::createPanicNecklace, 64, 48));
        event.registerLayerDefinition(PENDANT, layer(NecklaceModel::createPendant, 64, 48));
        event.registerLayerDefinition(LEGACY_AMULET, layer(LegacyAmuletModel::createAmulet, 32, 16));

        event.registerLayerDefinition(CLOUD_IN_A_BOTTLE, layer(BeltModel::createCloudInABottle, 32, 32));
        event.registerLayerDefinition(ANTIDOTE_VESSEL, layer(BeltModel::createAntidoteVessel, 32, 32));
        event.registerLayerDefinition(BUBBLE_WRAP, layer(BubbleWrapModel::createBubbleWrap, 32, 32));
        event.registerLayerDefinition(STAR_CLOAK, layer(StarCloakModel::createStarCloak, 64, 64));

        event.registerLayerDefinition(CLAWS_WIDE, layer(() -> ArmsModel.createClaws(false), 32, 16));
        event.registerLayerDefinition(CLAWS_SLIM, layer(() -> ArmsModel.createClaws(true), 32, 16));
        event.registerLayerDefinition(GLOVE_WIDE, layer(() -> ArmsModel.createSleevedArms(false), 32, 32));
        event.registerLayerDefinition(GLOVE_SLIM, layer(() -> ArmsModel.createSleevedArms(true), 32, 32));
        event.registerLayerDefinition(LEGACY_GLOVE_WIDE, layer(() -> ArmsModel.createLegacySleevedArms(false), 64, 64));
        event.registerLayerDefinition(LEGACY_GLOVE_SLIM, layer(() -> ArmsModel.createLegacySleevedArms(true), 64, 64));
        event.registerLayerDefinition(POCKET_PISTON_WIDE, layer(() -> ArmsModel.createPocketPiston(false), 32, 16));
        event.registerLayerDefinition(POCKET_PISTON_SLIM, layer(() -> ArmsModel.createPocketPiston(true), 32, 16));

        event.registerLayerDefinition(WHOOPEE_CUSHION, layer(HeadModel::createWhoopeeCushion, 32, 16));
    }
}
