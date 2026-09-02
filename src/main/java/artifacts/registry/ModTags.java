package artifacts.registry;

import artifacts.Artifacts;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> CAMPSITE_CHESTS = create(Registries.BLOCK, "campsite_chests");
    public static final TagKey<MobEffect> ANTIDOTE_VESSEL_CANCELLABLE = create(Registries.MOB_EFFECT, "antidote_vessel_cancellable");

    private static <T> TagKey<T> create(ResourceKey<Registry<T>> registry, String id) {
        return TagKey.create(registry, Artifacts.id(id));
    }

    // yeet 🤠
    public static <T> HolderSet<T> getTag(TagKey<T> tagKey) {
        // noinspection unchecked
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(tagKey.registry().location());
        // noinspection ConstantConditions
        return registry.getOrCreateTag(tagKey);
    }

    public static <T> boolean isInTag(T value, TagKey<T> tagKey) {
        // noinspection unchecked
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(tagKey.registry().location());
        // noinspection ConstantConditions
        return registry.getOrCreateTag(tagKey).contains(registry.wrapAsHolder(value));
    }
}
