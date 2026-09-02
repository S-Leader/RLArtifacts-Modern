package artifacts.data.providers;

import artifacts.Artifacts;
import artifacts.loot.RollLootTableModifier;
import artifacts.loot.ArtifactRarityAdjustedChance;
import artifacts.loot.ConfigValueChance;
import artifacts.registry.ModLootTables;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LootModifiers implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final List<Builder> lootBuilders = new ArrayList<>();
    private final PackOutput packOutput;
    private final Map<String, JsonElement> toSerialize = new HashMap<>();

    public LootModifiers(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    private void addLoot() {
        ModLootTables.INJECTED_LOOT_TABLES.stream()
                .forEach(lootTable -> builder(lootTable, 0.15F).artifact(1));

        ModLootTables.ARCHAEOLOGY_LOOT_TABLES.forEach(lootTable -> archaeologyBuilder(lootTable).artifact(1));
    }

    protected Builder builder(ResourceLocation lootTable, float baseChance) {
        if (!ModLootTables.INJECTED_LOOT_TABLES.contains(lootTable)) {
            throw new IllegalArgumentException("Missing injected loot table: %s".formatted(lootTable));
        }
        Builder builder = new Builder(lootTable);
        builder.lootPoolCondition(ArtifactRarityAdjustedChance.adjustedChance(baseChance));
        builder.lootModifierCondition(LootTableIdCondition.builder(lootTable));
        lootBuilders.add(builder);
        return builder;
    }

    protected Builder archaeologyBuilder(ResourceLocation lootTable) {
        if (!ModLootTables.ARCHAEOLOGY_LOOT_TABLES.contains(lootTable)) {
            throw new IllegalArgumentException("Missing archaeology loot table: %s".formatted(lootTable));
        }
        Builder builder = new Builder(lootTable).replace();
        builder.lootModifierCondition(LootTableIdCondition.builder(lootTable));
        builder.lootModifierCondition(ConfigValueChance.archaeologyChance());
        lootBuilders.add(builder);
        return builder;
    }

    protected void start() {
        addLoot();
        for (Builder lootBuilder : lootBuilders) {
            add("inject/" + lootBuilder.getName(), lootBuilder.build());
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path modifierFolderPath = packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Artifacts.MOD_ID).resolve("loot_modifiers");
        List<ResourceLocation> entries = new ArrayList<>();
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

        toSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
            entries.add(new ResourceLocation(Artifacts.MOD_ID, name));
            Path modifierPath = modifierFolderPath.resolve(name + ".json");
            futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
        }));

        JsonObject forgeJson = new JsonObject();
        forgeJson.addProperty("replace", false);
        forgeJson.add("entries", GSON.toJsonTree(entries.stream().map(ResourceLocation::toString).collect(Collectors.toList())));
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    public <T extends IGlobalLootModifier> void add(String modifier, T instance) {
        JsonElement json = IGlobalLootModifier.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, ignored -> {});
        toSerialize.put(modifier, json);
    }

    @Override
    public String getName() {
        return "Global Loot Modifiers : " + Artifacts.MOD_ID;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    protected static class Builder {

        private final ResourceLocation lootTable;
        private final LootPool.Builder lootPool = LootPool.lootPool();
        private final List<LootItemCondition> conditions = new ArrayList<>();
        private boolean replace;
        private LootContextParamSet paramSet = LootContextParamSets.CHEST;

        private Builder(ResourceLocation lootTable) {
            this.lootTable = lootTable;
        }

        private RollLootTableModifier build() {
            return new RollLootTableModifier(conditions.toArray(new LootItemCondition[]{}), Artifacts.id("inject/%s", lootTable.getPath()), replace);
        }

        protected LootTable.Builder createLootTable() {
            return new LootTable.Builder().withPool(lootPool);
        }

        public LootContextParamSet getParameterSet() {
            return paramSet;
        }

        protected String getName() {
            return lootTable.getPath();
        }

        private Builder parameterSet(LootContextParamSet paramSet) {
            this.paramSet = paramSet;
            return this;
        }

        public Builder replace() {
            replace = true;
            return this;
        }

        private Builder lootPoolCondition(LootItemCondition.Builder condition) {
            lootPool.when(condition);
            return this;
        }

        private Builder lootModifierCondition(LootItemCondition.Builder condition) {
            conditions.add(condition.build());
            return this;
        }

        private Builder item(Item item, int weight) {
            lootPool.add(LootTables.item(item, weight));
            return this;
        }

        private Builder artifact(int weight) {
            lootPool.add(LootTables.artifact(weight));
            return this;
        }


    }
}
