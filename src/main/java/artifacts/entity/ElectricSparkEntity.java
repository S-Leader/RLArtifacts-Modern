package artifacts.entity;

import artifacts.registry.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** A small projectile that falls, settles, and shocks each touching enemy once. */
public class ElectricSparkEntity extends ThrowableProjectile {

    private static final int MAX_LIFE = 80;
    private final Set<UUID> shockedEntities = new HashSet<>();
    private int life;
    private boolean resting;

    public ElectricSparkEntity(EntityType<? extends ElectricSparkEntity> type, Level level) {
        super(type, level);
    }

    public ElectricSparkEntity(Level level, LivingEntity owner) {
        this(ModEntityTypes.ELECTRIC_SPARK.get(), level);
        setOwner(owner);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        if (!level().isClientSide() && life >= MAX_LIFE) {
            discard();
            return;
        }

        super.tick();
        life++;

        if (level().isClientSide()) {
            int particleCount = resting ? 2 : 1;
            for (int i = 0; i < particleCount; i++) {
                level().addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        getX() + (random.nextDouble() - 0.5D) * 0.18D,
                        getY() + random.nextDouble() * 0.12D,
                        getZ() + (random.nextDouble() - 0.5D) * 0.18D,
                        0.0D, 0.01D, 0.0D
                );
            }
        } else {
            shockTouchingEnemies();
        }
    }

    private void shockTouchingEnemies() {
        Entity owner = getOwner();
        for (LivingEntity target : level().getEntitiesOfClass(
                LivingEntity.class,
                getBoundingBox().inflate(0.32D, 0.20D, 0.32D),
                target -> target.isAlive()
                        && target != owner
                        && !shockedEntities.contains(target.getUUID())
                        && (owner == null || !owner.isAlliedTo(target))
        )) {
            if (target.hurt(level().damageSources().indirectMagic(this, owner), 3.0F)) {
                shockedEntities.add(target.getUUID());
                level().broadcastEntityEvent(this, (byte) 3);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!resting) {
            resting = true;
            setNoGravity(true);
            setDeltaMovement(Vec3.ZERO);
            Vec3 normal = Vec3.atLowerCornerOf(result.getDirection().getNormal()).scale(0.02D);
            Vec3 location = result.getLocation().add(normal);
            setPos(location.x, location.y, location.z);
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target != getOwner() && !(target instanceof ElectricSparkEntity) && super.canHitEntity(target);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                level().addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        getX() + random.nextGaussian() * 0.18D,
                        getY() + random.nextDouble() * 0.25D,
                        getZ() + random.nextGaussian() * 0.18D,
                        random.nextGaussian() * 0.03D,
                        0.04D,
                        random.nextGaussian() * 0.03D
                );
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Life", life);
        tag.putBoolean("Resting", resting);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        life = tag.getInt("Life");
        resting = tag.getBoolean("Resting");
        setNoGravity(resting);
    }
}
