package artifacts.registry;

import artifacts.Artifacts;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModLootTables {

    public static final ResourceLocation SACRIFICIAL_REWARD = Artifacts.id("sacrificial_reward");

    public static final Map<EntityType<?>, ResourceLocation> ENTITY_EQUIPMENT;

    static {
        ENTITY_EQUIPMENT = new HashMap<>();
        List.of(
                EntityType.ZOMBIE,
                EntityType.HUSK,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.PIGLIN_BRUTE,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.GHAST
        ).forEach(type -> ENTITY_EQUIPMENT.put(type, entityEquipmentLootTable(type)));
    }

    public static ResourceLocation entityEquipmentLootTable(EntityType<?> entityType) {
        return Artifacts.id("entity_equipment/%s", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath());
    }
}
