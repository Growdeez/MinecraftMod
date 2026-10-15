package com.example.firstblocks;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WhispProjectile extends Projectile {

    private static final float NORMAL_DAMAGE = 6.0F;
    private static final float ORBITAL_DAMAGE = 2.0F;

    private static final double SPEED = 1.2;

    private static final double ORBIT_RADIUS = 1.5;
    private static final double ORBIT_HEIGHT = 1.1;
    private static final double ORBIT_ANGULAR_SPEED = 0.12;

    private static final int MAX_LIFETIME = 200;

    private boolean orbital;
    private int orbitIndex;

    public boolean isOrbital() {
        return this.orbital;
    }

    public int getOrbitIndex() {
        return this.orbitIndex;
    }

    public WhispProjectile(
            EntityType<? extends WhispProjectile> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    // Обычный снаряд, направленный в цель.
    public WhispProjectile(
            ServerLevel level,
            LivingEntity owner,
            LivingEntity target
    ) {
        super(ModEntities.WHISP_PROJECTILE.get(), level);

        this.setOwner(owner);

        this.setPos(
                owner.getX(),
                owner.getEyeY() - 0.2,
                owner.getZ()
        );

        Vec3 direction = target.position()
                .add(0.0, target.getBbHeight() * 0.5, 0.0)
                .subtract(this.position())
                .normalize();

        this.shoot(
                direction.x,
                direction.y,
                direction.z,
                (float) SPEED,
                0.0F
        );
    }

    // Орбитальный снаряд.
    public WhispProjectile(
            ServerLevel level,
            LivingEntity owner,
            int orbitIndex
    ) {
        super(ModEntities.WHISP_PROJECTILE.get(), level);

        this.setOwner(owner);
        this.orbital = true;
        this.orbitIndex = Math.floorMod(orbitIndex, 3);

        this.setPos(
                owner.getX() + ORBIT_RADIUS,
                owner.getY() + owner.getBbHeight() * 0.5,
                owner.getZ()
        );

        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void defineSynchedData(
            SynchedEntityData.Builder builder
    ) {
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            if (tickCount % 2 == 0) {
                level().addParticle(
                        ModParticles.EARTH_WAND_PARTICLE.get(),
                        getX(),
                        getY(),
                        getZ(),
                        0.0,
                        0.0,
                        0.0
                );
            }

            return;
        }

        if (tickCount > MAX_LIFETIME) {
            discard();
            return;
        }

        if (orbital) {
            updateOrbitMovement();
        }

        HitResult hitResult =
                net.minecraft.world.entity.projectile.ProjectileUtil
                        .getHitResultOnMoveVector(
                                this,
                                this::canHitEntity
                        );

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
            return;
        }

        Vec3 movement = this.getDeltaMovement();

        this.setPos(
                getX() + movement.x,
                getY() + movement.y,
                getZ() + movement.z
        );
    }

    private void updateOrbitMovement() {
        Entity owner = getOwner();

        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }

        // Каждый снаряд занимает отдельный сектор окружности.
        double angle =
                tickCount * ORBIT_ANGULAR_SPEED
                        + orbitIndex * (Math.PI * 2.0 / 3.0);

        Vec3 nextPosition = new Vec3(
                owner.getX() + Math.cos(angle) * ORBIT_RADIUS,
                owner.getY()
                        + owner.getBbHeight() * 0.5
                        + Math.sin(angle * 2.0) * ORBIT_HEIGHT,
                owner.getZ() + Math.sin(angle) * ORBIT_RADIUS
        );

        // Движение к следующей точке орбиты.
        this.setDeltaMovement(
                nextPosition.subtract(this.position())
        );
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity target = hitResult.getEntity();
        Entity owner = getOwner();

        if (target instanceof LivingEntity livingTarget
                && target != owner
                && livingTarget.isAlive()) {

            LivingEntity livingOwner =
                    owner instanceof LivingEntity mob ? mob : null;

            DamageSource damageSource = livingOwner != null
                    ? damageSources().mobProjectile(this, livingOwner)
                    : damageSources().magic();

            float damage = orbital
                    ? ORBITAL_DAMAGE
                    : NORMAL_DAMAGE;

            livingTarget.hurtServer(
                    serverLevel,
                    damageSource,
                    damage
            );
        }

        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if (!level().isClientSide()) {
            discard();
        }
    }
}
