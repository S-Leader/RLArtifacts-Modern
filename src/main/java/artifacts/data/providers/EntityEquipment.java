package artifacts.data.providers;

import artifacts.loot.ConfigValueChance;
import artifacts.registry.ModItems;
import artifacts.registry.ModLootTables;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.HashSet;
import java.util.Set;

public class EntityEquipment {

    private final LootTables lootTables;
    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    public EntityEquipment(LootTables lootTables) {
        this.lootTables = lootTables;
    }

    public void addLootTables() {
        entityTypes.clear();
        addItems(EntityType.ZOMBIE, ModItems.PLASTIC_DRINKING_HAT.get(), ModItems.BUBBLE_WRAP.get());
        addItems(EntityType.HUSK, ModItems.VAMPIRIC_GLOVE.get(), ModItems.THORN_PENDANT.get());
        addItems(EntityType.SKELETON, ModItems.FLAME_PENDANT.get());
        addItems(EntityType.STRAY, ModItems.PANIC_NECKLACE.get());
        addItems(EntityType.WITHER_SKELETON, ModItems.FIRE_GAUNTLET.get(), ModItems.MAGMA_STONE.get(), ModItems.ANTIDOTE_VESSEL.get());
        addItems(EntityType.ZOMBIFIED_PIGLIN, ModItems.FIRE_GAUNTLET.get());
        addItems(EntityType.PIGLIN_BRUTE, ModItems.POWER_GLOVE.get());
        addItems(EntityType.GHAST, ModItems.STAR_CLOAK.get());

        if (!entityTypes.equals(ModLootTables.ENTITY_EQUIPMENT.keySet())) {
            throw new IllegalStateException(Sets.symmetricDifference(entityTypes, ModLootTables.ENTITY_EQUIPMENT.keySet()).toString());
        }
    }

    public void addItems(EntityType<?> entityType, Item... items) {
        if (!ModLootTables.ENTITY_EQUIPMENT.containsKey(entityType)) {
            throw new IllegalArgumentException("Missing entity equipment entity: %s".formatted(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)));
        }
        LootPool.Builder pool = LootPool.lootPool();
        for (Item item : items) {
            pool.add(item(item));
        }
        addEquipment(entityType, pool);
    }

    protected static LootPoolSingletonContainer.Builder<?> item(Item item) {
        return LootItem.lootTableItem(item).setWeight(1);
    }

    public void addEquipment(EntityType<?> entityType, LootPool.Builder pool) {
        entityTypes.add(entityType);
        LootTable.Builder builder = LootTable.lootTable().withPool(pool.when(ConfigValueChance.entityEquipmentChance()));
        lootTables.addLootTable(ModLootTables.entityEquipmentLootTable(entityType).getPath(), builder, LootContextParamSets.ALL_PARAMS);
    }
}
