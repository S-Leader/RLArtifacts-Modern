package artifacts.registry;

import artifacts.Artifacts;
import artifacts.entity.ElectricSparkEntity;
import artifacts.entity.HallowStarEntity;
import artifacts.entity.MimicEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Artifacts.MOD_ID);

    public static final RegistrySupplier<EntityType<MimicEntity>> MIMIC = RegistrySupplier.of(ENTITY_TYPES.register("mimic",
            () -> EntityType.Builder.of(MimicEntity::new, MobCategory.MISC)
                    .sized(14 / 16F, 14 / 16F)
                    .clientTrackingRange(8)
                    .build(Artifacts.id("mimic").toString())
    ));

    public static final RegistrySupplier<EntityType<HallowStarEntity>> HALLOW_STAR = RegistrySupplier.of(ENTITY_TYPES.register("hallow_star",
            () -> EntityType.Builder.<HallowStarEntity>of(HallowStarEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(Artifacts.id("hallow_star").toString())
    ));

    public static final RegistrySupplier<EntityType<ElectricSparkEntity>> ELECTRIC_SPARK = RegistrySupplier.of(ENTITY_TYPES.register("electric_spark",
            () -> EntityType.Builder.<ElectricSparkEntity>of(ElectricSparkEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(Artifacts.id("electric_spark").toString())
    ));
}
