package com.example.firstblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;

public class Whisp extends Mob {

    private static final double FLIGHT_RADIUS = 16.0;
    private static final double DEFENSE_RADIUS = 24.0;
    private static final double MIN_GROUND_HEIGHT = 1.25;
    private static final double MAX_GROUND_HEIGHT = 2.0;
    private static final double ARRIVAL_DISTANCE_SQR = 0.36;

    private double spawnX;
    private double spawnZ;
    private boolean spawnPositionInitialized;
    private BlockPos altarPos;

    public Whisp(EntityType<? extends Whisp> type, Level level) {
        super(type, level);
        this.moveControl = new WhispMoveControl(this);
    }

    public void setAltarPos(BlockPos altarPos) {
        this.altarPos = altarPos;
    }

    public boolean isWithinDefenseRadius(LivingEntity target) {
        if (this.altarPos == null) {
            return false;
        }

        double centerX = this.altarPos.getX() + 0.5;
        double centerZ = this.altarPos.getZ() + 0.5;

        double dx = target.getX() - centerX;
        double dz = target.getZ() - centerZ;

        return dx * dx + dz * dz
                <= DEFENSE_RADIUS * DEFENSE_RADIUS;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        if (this.level().isClientSide() || this.altarPos == null) {
            return;
        }

        if (this.level().getBlockEntity(this.altarPos)
                instanceof WhispAltarBlockEntity altar) {
            altar.onWhispDeath(this.getUUID());
        }
    }



    @Override
    public void checkDespawn() {
        // Висп привязан к алтарю и не должен исчезать по стандартным правилам.
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        if (this.altarPos != null) {
            output.putInt("AltarX", this.altarPos.getX());
            output.putInt("AltarY", this.altarPos.getY());
            output.putInt("AltarZ", this.altarPos.getZ());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        if (input.getInt("AltarX").isPresent()
                && input.getInt("AltarY").isPresent()
                && input.getInt("AltarZ").isPresent()) {

            this.altarPos = new BlockPos(
                    input.getIntOr("AltarX", 0),
                    input.getIntOr("AltarY", 0),
                    input.getIntOr("AltarZ", 0)
            );
        } else {
            this.altarPos = null;
        }
    }

    @Override
    protected void registerGoals() {
        // Поведение движения.
        this.goalSelector.addGoal(1, new WhispAttackGoal(this));
        this.goalSelector.addGoal(2, new RandomFlightGoal(this));

        // Выбор целей для атаки.
        this.targetSelector.addGoal(
                1,
                new NearestAttackableTargetGoal<>(
                        this,
                        Mob.class,
                        5,
                        false,
                        false,
                        (target, level) ->
                                target instanceof Enemy

                                        && this.isWithinDefenseRadius(target)
                )
        );
    }

    @Override
    public void travel(Vec3 input) {
        this.travelFlying(input, 0.02F);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.spawnPositionInitialized) {
            if (this.altarPos != null) {
                this.spawnX = this.altarPos.getX() + 0.5;
                this.spawnZ = this.altarPos.getZ() + 0.5;
            } else {
                this.spawnX = this.getX();
                this.spawnZ = this.getZ();
            }

            this.spawnPositionInitialized = true;
        }
    }


    private static class RandomFlightGoal extends Goal {

        private final Whisp whisp;
        private Vec3 target;

        private int waitTicks;
        private int checkTicks;
        private int stuckTicks;

        private Vec3 lastCheckedPosition = Vec3.ZERO;

        public RandomFlightGoal(Whisp whisp) {
            this.whisp = whisp;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.target = null;
            this.waitTicks = 0;
            this.checkTicks = 0;
            this.stuckTicks = 0;
            this.lastCheckedPosition = this.whisp.position();

            this.chooseNewTarget();
        }



        @Override
        public void tick() {
            if (this.target == null) {
                this.whisp.setDeltaMovement(Vec3.ZERO);

                if (this.waitTicks > 0) {
                    this.waitTicks--;
                } else {
                    this.chooseNewTarget();
                }

                return;
            }

            // Проверяем, не застрял ли висп.
            this.checkTicks++;

            if (this.checkTicks >= 10) {
                this.checkTicks = 0;

                Vec3 currentPosition = this.whisp.position();

                double movedDistanceSqr =
                        currentPosition.distanceToSqr(
                                this.lastCheckedPosition
                        );

                this.lastCheckedPosition = currentPosition;

                if (movedDistanceSqr < 0.04) {
                    this.stuckTicks += 10;
                } else {
                    this.stuckTicks = 0;
                }

                if (this.stuckTicks >= 30) {
                    this.target = null;
                    this.stuckTicks = 0;
                    this.waitTicks = 0;

                    this.whisp.setDeltaMovement(Vec3.ZERO);
                    this.whisp.getMoveControl().setWantedPosition(
                            this.whisp.getX(),
                            this.whisp.getY(),
                            this.whisp.getZ(),
                            0.0
                    );

                    this.chooseNewTarget();
                    return;
                }
            }

            Vec3 difference = this.target.subtract(
                    this.whisp.position()
            );

            if (difference.lengthSqr() <= ARRIVAL_DISTANCE_SQR) {
                this.target = null;
                this.whisp.setDeltaMovement(Vec3.ZERO);

                this.whisp.getMoveControl().setWantedPosition(
                        this.whisp.getX(),
                        this.whisp.getY(),
                        this.whisp.getZ(),
                        0.0
                );

                this.waitTicks = 20
                        + this.whisp.getRandom().nextInt(81);

                this.checkTicks = 0;
                this.stuckTicks = 0;
                this.lastCheckedPosition = this.whisp.position();

                return;
            }

            this.whisp.getMoveControl().setWantedPosition(
                    this.target.x,
                    this.target.y,
                    this.target.z,
                    1.0
            );
        }




        private void chooseNewTarget() {
            for (int i = 0; i < 30; i++) {
                double angle = this.whisp.getRandom().nextDouble()
                        * Math.PI * 2.0;

                double radius = Math.sqrt(
                        this.whisp.getRandom().nextDouble()
                ) * FLIGHT_RADIUS;

                double x = this.whisp.spawnX
                        + Math.cos(angle) * radius;

                double z = this.whisp.spawnZ
                        + Math.sin(angle) * radius;

                Double surfaceY = findSurfaceY(
                        this.whisp, x, z
                );

                if (surfaceY == null) {
                    continue;
                }

                double y = surfaceY
                        + MIN_GROUND_HEIGHT
                        + this.whisp.getRandom().nextDouble()
                        * (MAX_GROUND_HEIGHT - MIN_GROUND_HEIGHT);

                Vec3 candidate = new Vec3(x, y, z);

                if (candidate.distanceToSqr(
                        this.whisp.position()
                ) <= 1.0) {
                    continue;
                }

                // Не выбираем цель, если само место занято блоком.
                if (!this.whisp.level().noCollision(
                        this.whisp,
                        this.whisp.getBoundingBox()
                                .move(
                                        candidate.subtract(
                                                this.whisp.position()
                                        )
                                )
                )) {
                    continue;
                }

                this.target = candidate;
                this.checkTicks = 0;
                this.stuckTicks = 0;
                this.lastCheckedPosition = this.whisp.position();

                this.whisp.getMoveControl().setWantedPosition(
                        x, y, z, 1.0
                );

                return;
            }

            this.target = null;
            this.waitTicks = 20;
            this.whisp.setDeltaMovement(Vec3.ZERO);
        }

        private static Double findSurfaceY(
                Whisp whisp,
                double x,
                double z
        ) {
            int blockX = Mth.floor(x);
            int blockZ = Mth.floor(z);

            int surfaceY = whisp.level().getHeight(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    blockX,
                    blockZ
            );

            return (double) surfaceY;
        }
    }

    private static class WhispMoveControl
            extends MoveControl<Whisp> {

        public WhispMoveControl(Whisp whisp) {
            super(whisp);
        }

        @Override
        public void tick() {
            this.mob.setNoGravity(true);

            if (this.operation != Operation.MOVE_TO) {
                this.mob.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 difference = new Vec3(
                    this.wantedX - this.mob.getX(),
                    this.wantedY - this.mob.getY(),
                    this.wantedZ - this.mob.getZ()
            );

            if (difference.lengthSqr()
                    <= ARRIVAL_DISTANCE_SQR) {
                this.operation = Operation.WAIT;
                this.mob.setDeltaMovement(Vec3.ZERO);
                return;
            }

            if (difference.horizontalDistanceSqr() > 1.0E-6) {
                float targetYaw = (float) (
                        Mth.atan2(difference.z, difference.x)
                                * (180.0F / (float) Math.PI)
                ) - 90.0F;

                this.mob.setYRot(
                        Mth.rotLerp(
                                0.2F,
                                this.mob.getYRot(),
                                targetYaw
                        )
                );
            }

            double speed = this.mob.getAttributeValue(
                    Attributes.FLYING_SPEED
            );

            Vec3 movement = difference.normalize()
                    .scale(speed * 0.5);

            // Не двигаемся внутрь твёрдого препятствия.
            if (!this.mob.level().noCollision(
                    this.mob,
                    this.mob.getBoundingBox().move(movement)
            )) {
                this.mob.setDeltaMovement(Vec3.ZERO);
                this.operation = Operation.WAIT;
                return;
            }

            this.mob.setDeltaMovement(movement);
        }
    }
}
