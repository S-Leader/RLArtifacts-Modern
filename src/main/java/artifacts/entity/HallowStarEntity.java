package artifacts.entity;

import artifacts.registry.ModEntityTypes;
import artifacts.registry.ModGameRules;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HallowStarEntity extends ThrowableProjectile {

    private int life;

    public HallowStarEntity(EntityType<? extends HallowStarEntity> entityType, Level level) {
        super(entityType, level);
        setNoGravity(true);
    }

    public HallowStarEntity(Level level, LivingEntity owner) {
        this(ModEntityTypes.HALLOW_STAR.get(), level);
        setOwner(owner);

        double offsetX = 8.0D * (2.0D * random.nextDouble() - 1.0D);
        double offsetZ = 8.0D * (2.0D * random.nextDouble() - 1.0D);
        double offsetY = 25.0D + random.nextDouble() * 15.0D;
        setPos(
                owner.getX() + offsetX - 1.0D + random.nextDouble() * 2.0D,
                owner.getY() + offsetY,
                owner.getZ() + offsetZ - 1.0D + random.nextDouble() * 2.0D
        );

        double length = Math.sqrt(offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ);
        setDeltaMovement(-offsetX * 1.5D / length, -offsetY * 1.5D / length, -offsetZ * 1.5D / length);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        Entity owner = getOwner();
        if (!level().isClientSide() && (life >= 200 || owner == null || !owner.isAlive())) {
            discard();
            return;
        }

        super.tick();
        life++;
        Vec3 movement = getDeltaMovement();
        level().addParticle(ParticleTypes.FIREWORK, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
        if (isInWater()) {
            for (int i = 0; i < 4; i++) {
                level().addParticle(
                        ParticleTypes.BUBBLE,
                        getX() - movement.x * 0.25D,
                        getY() - movement.y * 0.25D,
                        getZ() - movement.z * 0.25D,
                        movement.x,
                        movement.y,
                        movement.z
                );
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target != getOwner() && !(target instanceof HallowStarEntity) && super.canHitEntity(target);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            if (result instanceof EntityHitResult entityHitResult
                    && entityHitResult.getEntity() instanceof LivingEntity target) {
                target.hurt(
                        level().damageSources().indirectMagic(this, getOwner()),
                        ModGameRules.STAR_CLOAK_DAMAGE.get()
                );
            }
            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
        playSound(SoundEvents.WOOD_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 16; i++) {
                level().addParticle(
                        ParticleTypes.POOF,
                        getX() + random.nextGaussian(),
                        getY() - 0.5D * random.nextDouble(),
                        getZ() + random.nextGaussian(),
                        0.0D,
                        0.0D,
                        0.0D
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        life = tag.getInt("Life");
    }
}
