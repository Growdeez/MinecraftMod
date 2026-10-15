package com.example.firstblocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EarthWandProjectile extends ThrowableItemProjectile {

    public EarthWandProjectile(
            EntityType<? extends EarthWandProjectile> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    public EarthWandProjectile(
            ServerLevel level,
            LivingEntity owner,
            ItemStack itemStack
    ) {
        super(ModEntities.EARTH_WAND_PROJECTILE.get(), level);

        this.setPos(
                owner.getX() + 1.0,
                owner.getEyeY() - 0.1,
                owner.getZ()
        );

        this.setOwner(owner);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EARTH_WAND.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide() && tickCount % 3 == 0) {
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
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!level().isClientSide()) {

            ServerLevel serverLevel = (ServerLevel) level();

            // =========================
            // ВЗРЫВ
            // =========================

            level().explode(
                    this,
                    Explosion.getDefaultDamageSource(level(), this),
                    null,
                    getX(),
                    getY(),
                    getZ(),
                    1.0F,
                    false,
                    Level.ExplosionInteraction.NONE,
                    ModParticles.EARTH_WAND_EXPLOSION_PARTICLE.get(),
                    ModParticles.EARTH_WAND_EXPLOSION_PARTICLE.get(),
                    WeightedList.of(),
                    SoundEvents.GENERIC_EXPLODE
            );

            // =========================
            // ОБЫЧНЫЕ ЧАСТИЦЫ ВЗРЫВА
            // =========================

            serverLevel.sendParticles(
                    ModParticles.EARTH_WAND_EXPLOSION_PARTICLE.get(),
                    getX(),
                    getY(),
                    getZ(),
                    15,
                    0.4,
                    0.4,
                    0.4,
                    0.15
            );

            // =========================
            // ЗАПУСК РИСУНКА
            // =========================

            /*EarthWandDrawingEffect drawingEffect =
                    new EarthWandDrawingEffect(
                            serverLevel,
                            getX(),
                            getY(),
                            getZ()
                    );

            EarthWandDrawingEffectManager.addEffect(drawingEffect);*/

            discard();
        }
    }
}