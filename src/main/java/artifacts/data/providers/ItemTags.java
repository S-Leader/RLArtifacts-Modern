package artifacts.data.providers;

import artifacts.Artifacts;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {

    public static final TagKey<Item>
            ARTIFACTS = createTag("artifacts"),
            HEAD = createTag("slot/head"),
            NECKLACE = createTag("slot/necklace"),
            HANDS = createTag("slot/hands"),
            BELT = createTag("slot/belt"),
            BODY = createTag("slot/body"),
            CHARM = createTag("slot/charm"),
            RING = createTag("slot/ring");

    public static final TagKey<Item> ORIGINS_SHIELDS = TagKey.create(Registries.ITEM, new ResourceLocation("origins", "shields"));

    private static TagKey<Item> createTag(String name) {
        return TagKey.create(Registries.ITEM, Artifacts.id(name));
    }

    public ItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTags, Artifacts.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ARTIFACTS).add(BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(Artifacts.MOD_ID))
                .filter(item -> item != ModItems.MIMIC_SPAWN_EGG.get()).toList().toArray(new Item[]{})
        );
        tag(HEAD).add(
                ModItems.PLASTIC_DRINKING_HAT.get()
        );
        tag(NECKLACE).add(
                ModItems.PANIC_NECKLACE.get(),
                ModItems.SHOCK_PENDANT.get(),
                ModItems.FLAME_PENDANT.get(),
                ModItems.THORN_PENDANT.get(),
                ModItems.ULTIMATE_PENDANT.get(),
                ModItems.SACRIFICIAL_AMULET.get()
        );
        tag(HANDS).add(
                ModItems.FERAL_CLAWS.get(),
                ModItems.POWER_GLOVE.get(),
                ModItems.MECHANICAL_GLOVE.get(),
                ModItems.FIRE_GAUNTLET.get(),
                ModItems.POCKET_PISTON.get(),
                ModItems.VAMPIRIC_GLOVE.get(),
                ModItems.ELECTRIC_SHOCK_GLOVE.get()
        );
        tag(BELT).add(
                ModItems.CLOUD_IN_A_BOTTLE.get(),
                ModItems.BOTTLED_FART.get(),
                ModItems.ANTIDOTE_VESSEL.get(),
                ModItems.BUBBLE_WRAP.get(),
                ModItems.WHOOPEE_CUSHION.get()
        );
        tag(BODY).add(ModItems.STAR_CLOAK.get());
        tag(CHARM).add(ModItems.LUCKY_CLOVER.get());
        tag(RING).add(ModItems.MAGMA_STONE.get());

        tag(ORIGINS_SHIELDS).add(
                ModItems.UMBRELLA.get()
        );

        tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(BuiltInRegistries.ITEM.stream()
                .filter(item -> item instanceof WearableArtifactItem artifactItem && artifactItem.makesPiglinsNeutral())
                .toArray(Item[]::new)
        );
    }
}
