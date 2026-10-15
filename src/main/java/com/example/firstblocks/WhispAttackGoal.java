
package com.example.firstblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class WhispAttackGoal extends Goal {

    private static final double STOP_DISTANCE_SQR = 9.0;
    private static final double FLIGHT_HEIGHT = 1.5;
    private static final double MAX_TARGET_DISTANCE_SQR = 1600.0;

    private static final int REPATH_INTERVAL = 20;
    private static final int MAX_SEARCH_NODES = 3000;
    private static final int SEARCH_PADDING = 6;

    private static final int SHOOT_COOLDOWN = 40;
    private static final double SHOOT_RANGE_SQR = 400.0;
    private static final double MIN_SHOOT_RANGE_SQR = 9.0;

    private int shootCooldown;

    private static final double ORBITAL_ENTER_DISTANCE_SQR = 16.0;
    private static final double ORBITAL_EXIT_DISTANCE_SQR = 36.0;
    private static final int ORBITAL_SPAWN_INTERVAL = 8;

    private boolean closeDefenseMode;
    private int orbitalSpawnCooldown;

    private final Whisp whisp;

    private LivingEntity target;

    private List<Vec3> path = new ArrayList<>();
    private int waypointIndex;
    private int repathCooldown;
    private int escapeAttempts;

    private int stuckCheckTimer;
    private int stuckTicks;
    private Vec3 lastCheckedPosition = Vec3.ZERO;

    private static final double HOLD_DISTANCE = 12.0;
    private static final double HOLD_DISTANCE_SQR =
            HOLD_DISTANCE * HOLD_DISTANCE;

    public WhispAttackGoal(Whisp whisp) {
        this.whisp = whisp;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity currentTarget = this.whisp.getTarget();

        if (!isValidTarget(currentTarget)) {
            if (currentTarget != null) {
                this.whisp.setTarget(null);
            }

            return false;
        }

        this.target = currentTarget;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return isValidTarget(this.target)
                && this.whisp.isWithinDefenseRadius(this.target)
                && this.whisp.getTarget() == this.target
                && this.whisp.distanceToSqr(this.target)
                <= MAX_TARGET_DISTANCE_SQR;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.stuckCheckTimer = 0;
        this.path.clear();
        this.waypointIndex = 0;
        this.repathCooldown = 0;
        this.stuckTicks = 0;
        this.lastCheckedPosition = this.whisp.position();
        this.shootCooldown = 20;
    }



    private void maintainOrbitalProjectiles() {
        if (!(this.whisp.level()
                instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }

        if (this.orbitalSpawnCooldown > 0) {
            this.orbitalSpawnCooldown--;
            return;
        }

        // Проверяем существующие орбитальные снаряды этого виспа.
        List<WhispProjectile> orbitals = serverLevel.getEntitiesOfClass(
                WhispProjectile.class,
                this.whisp.getBoundingBox().inflate(3.0),
                projectile -> projectile.isAlive()
                        && projectile.getOwner() == this.whisp
                        && projectile.isOrbital()
        );

        boolean[] occupiedSlots = new boolean[3];

        for (WhispProjectile projectile : orbitals) {
            int index = projectile.getOrbitIndex();

            if (index >= 0 && index < occupiedSlots.length) {
                occupiedSlots[index] = true;
            }
        }

        // Создаём только один недостающий снаряд за раз.
        for (int i = 0; i < occupiedSlots.length; i++) {
            if (occupiedSlots[i]) {
                continue;
            }

            WhispProjectile projectile = new WhispProjectile(
                    serverLevel,
                    this.whisp,
                    i
            );

            serverLevel.addFreshEntity(projectile);
            this.orbitalSpawnCooldown = ORBITAL_SPAWN_INTERVAL;
            return;
        }

        // Все три снаряда уже существуют.
        this.orbitalSpawnCooldown = 0;
    }



    @Override
    public void tick() {
        if (!isValidTarget(this.target)) {
            return;
        }

        faceTarget();

        double distanceSqr = this.whisp.distanceToSqr(this.target);

// Включаем орбитальную защиту на расстоянии менее 4 блоков.
        if (!this.closeDefenseMode
                && distanceSqr <= ORBITAL_ENTER_DISTANCE_SQR) {
            this.closeDefenseMode = true;
        }

// Выключаем её, когда противник отдаляется дальше 6 блоков.
        if (this.closeDefenseMode
                && distanceSqr >= ORBITAL_EXIT_DISTANCE_SQR) {
            this.closeDefenseMode = false;
        }

        if (this.closeDefenseMode) {
            maintainOrbitalProjectiles();
        } else {
            tryShoot();
        }


        Vec3 currentPosition = this.whisp.position();

        Vec3 targetPosition = new Vec3(
                this.target.getX(),
                this.target.getY() + FLIGHT_HEIGHT,
                this.target.getZ()
        );

        double distanceToTargetSqr =
                this.whisp.distanceToSqr(this.target);

        if (distanceToTargetSqr <= HOLD_DISTANCE_SQR
                && this.whisp.hasLineOfSight(this.target)
                && isSegmentClear(currentPosition, targetPosition)) {

            this.path.clear();
            this.waypointIndex = 0;
            this.whisp.setDeltaMovement(Vec3.ZERO);

            this.whisp.getMoveControl().setWantedPosition(
                    this.whisp.getX(),
                    this.whisp.getY(),
                    this.whisp.getZ(),
                    0.0
            );

            return;
        }

        // Проверяем фактическое перемещение каждые 10 тиков.
        if (++this.stuckCheckTimer >= 10) {
            this.stuckCheckTimer = 0;

            currentPosition = this.whisp.position();

            double moved = currentPosition.distanceToSqr(
                    this.lastCheckedPosition
            );

            this.lastCheckedPosition = currentPosition;

            if (moved < 0.04) {
                this.stuckTicks += 10;
            } else {
                this.stuckTicks = 0;
                this.escapeAttempts = 0;
            }

            // Висп почти не перемещается уже 30 тиков.
            if (this.stuckTicks >= 30) {
                this.stuckTicks = 0;

                this.path.clear();
                this.waypointIndex = 0;
                this.repathCooldown = REPATH_INTERVAL;

                this.whisp.setDeltaMovement(Vec3.ZERO);

                Vec3 escapePoint = tryEscapeFromObstacle();

                if (escapePoint != null) {
                    // Временно двигаемся к точке освобождения.
                    this.path.add(escapePoint);
                    this.waypointIndex = 0;
                } else {
                    // Если рядом нет свободной точки,
                    // разрешаем повторный поиск маршрута.
                    this.repathCooldown = 0;
                }

                return;
            }
        }

        boolean directPath =
                this.whisp.hasLineOfSight(this.target)
                        && isSegmentClear(
                        currentPosition,
                        targetPosition
                );

        if (directPath) {
            this.path.clear();
            this.waypointIndex = 0;

            this.whisp.getMoveControl().setWantedPosition(
                    targetPosition.x,
                    targetPosition.y,
                    targetPosition.z,
                    1.0
            );

            return;
        }

        if (this.repathCooldown > 0) {
            this.repathCooldown--;
        }

        // Строим новый маршрут, если старого нет
        // или он был сброшен из-за препятствия.
        if (this.path.isEmpty()
                || this.waypointIndex >= this.path.size()
                || this.repathCooldown <= 0) {

            List<Vec3> newPath = findPath(
                    currentPosition,
                    targetPosition
            );

            this.repathCooldown = REPATH_INTERVAL;

            if (newPath == null || newPath.isEmpty()) {
                this.path.clear();
                this.whisp.setDeltaMovement(Vec3.ZERO);
                return;
            }

            this.path = newPath;
            this.waypointIndex = 0;
        }

        Vec3 waypoint = this.path.get(this.waypointIndex);

        // Проверяем, что промежуточная точка всё ещё доступна.
        if (!isSegmentClear(currentPosition, waypoint)) {
            this.path.clear();
            this.repathCooldown = 0;
            this.whisp.setDeltaMovement(Vec3.ZERO);
            return;
        }

        if (currentPosition.distanceToSqr(waypoint) < 0.8) {
            this.waypointIndex++;

            if (this.waypointIndex >= this.path.size()) {
                this.path.clear();
                this.repathCooldown = 0;
                return;
            }

            waypoint = this.path.get(this.waypointIndex);
        }

        this.whisp.getMoveControl().setWantedPosition(
                waypoint.x,
                waypoint.y,
                waypoint.z,
                1.0
        );
    }



    @Override
    public void stop() {
        this.stuckCheckTimer = 0;
        this.whisp.setDeltaMovement(Vec3.ZERO);

        this.whisp.getMoveControl().setWantedPosition(
                this.whisp.getX(),
                this.whisp.getY(),
                this.whisp.getZ(),
                0.0
        );

        if (this.whisp.getTarget() == this.target) {
            this.whisp.setTarget(null);
        }

        this.target = null;
        this.path.clear();
        this.waypointIndex = 0;
        this.repathCooldown = 0;
        this.stuckTicks = 0;
        this.closeDefenseMode = false;
        this.orbitalSpawnCooldown = 0;
    }

    private void tryShoot() {
        if (this.shootCooldown > 0) {
            this.shootCooldown--;
            return;
        }

        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        double distanceSqr = this.whisp.distanceToSqr(this.target);

        if (distanceSqr > SHOOT_RANGE_SQR) {
            return;
        }

        if (!this.whisp.hasLineOfSight(this.target)) {
            return;
        }

        if (!(this.whisp.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }

        WhispProjectile projectile = new WhispProjectile(
                serverLevel,
                this.whisp,
                this.target
        );

        serverLevel.addFreshEntity(projectile);

        this.shootCooldown = SHOOT_COOLDOWN;
    }

    private boolean isValidTarget(LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (target == this.whisp) {
            return false;
        }

        if (target instanceof Player player) {
            return !player.isCreative() && !player.isSpectator();
        }

        return true;
    }

    // Проверяет свободное пространство между двумя точками.
    private boolean isSegmentClear(Vec3 from, Vec3 to) {
        Vec3 difference = to.subtract(from);

        double distance = difference.length();
        int steps = Math.max(1, (int) Math.ceil(distance / 0.4));

        AABB originalBox = this.whisp.getBoundingBox();

        for (int i = 1; i <= steps; i++) {
            double fraction = (double) i / steps;
            Vec3 point = from.add(difference.scale(fraction));

            Vec3 offset = point.subtract(this.whisp.position());

            if (!this.whisp.level().noCollision(
                    this.whisp,
                    originalBox.move(offset)
            )) {
                return false;
            }
        }

        return true;
    }

    // A*: ищет путь по свободным позициям в трёхмерном пространстве.
    private List<Vec3> findPath(Vec3 start, Vec3 destination) {
        BlockPos startPos = BlockPos.containing(start);
        BlockPos goalPos = BlockPos.containing(destination);

        if (startPos.equals(goalPos)) {
            return Collections.singletonList(destination);
        }

        int minX = Math.min(startPos.getX(), goalPos.getX())
                - SEARCH_PADDING;
        int maxX = Math.max(startPos.getX(), goalPos.getX())
                + SEARCH_PADDING;

        int minY = Math.max(
                this.whisp.level().getMinY(),
                Math.min(startPos.getY(), goalPos.getY())
                        - SEARCH_PADDING
        );

        int maxY = Math.min(
                this.whisp.level().getMaxY() - 1,
                Math.max(startPos.getY(), goalPos.getY())
                        + SEARCH_PADDING
        );

        int minZ = Math.min(startPos.getZ(), goalPos.getZ())
                - SEARCH_PADDING;
        int maxZ = Math.max(startPos.getZ(), goalPos.getZ())
                + SEARCH_PADDING;

        PriorityQueue<PathNode> open = new PriorityQueue<>(
                Comparator.comparingDouble(PathNode::f)
        );

        Map<BlockPos, Double> cost = new HashMap<>();
        Map<BlockPos, BlockPos> parent = new HashMap<>();
        Set<BlockPos> closed = new HashSet<>();

        open.add(new PathNode(startPos, 0.0,
                heuristic(startPos, goalPos)));
        cost.put(startPos, 0.0);

        int visited = 0;

        while (!open.isEmpty() && visited < MAX_SEARCH_NODES) {
            PathNode current = open.poll();
            BlockPos currentPos = current.pos();

            if (!closed.add(currentPos)) {
                continue;
            }

            visited++;

            if (currentPos.equals(goalPos)) {
                return reconstructPath(
                        parent,
                        startPos,
                        goalPos,
                        destination
                );
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }

                        BlockPos next = currentPos.offset(dx, dy, dz);

                        if (next.getX() < minX
                                || next.getX() > maxX
                                || next.getY() < minY
                                || next.getY() > maxY
                                || next.getZ() < minZ
                                || next.getZ() > maxZ) {
                            continue;
                        }

                        if (closed.contains(next)) {
                            continue;
                        }

                        Vec3 nextPoint = centerOf(next);

                        if (!isPositionClear(nextPoint)) {
                            continue;
                        }

                        // Не позволяем диагональному шагу
                        // проходить сквозь угол блока.
                        if (!isSegmentClear(
                                centerOf(currentPos),
                                nextPoint
                        )) {
                            continue;
                        }

                        double stepCost = Math.sqrt(
                                dx * dx + dy * dy + dz * dz
                        );

                        double newCost =
                                cost.get(currentPos) + stepCost;

                        if (newCost < cost.getOrDefault(
                                next, Double.POSITIVE_INFINITY
                        )) {
                            cost.put(next, newCost);
                            parent.put(next, currentPos);

                            open.add(new PathNode(
                                    next,
                                    newCost,
                                    newCost + heuristic(next, goalPos)
                            ));
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean isPositionClear(Vec3 position) {
        Vec3 offset = position.subtract(this.whisp.position());

        return this.whisp.level().noCollision(
                this.whisp,
                this.whisp.getBoundingBox().move(offset)
        );
    }

    private List<Vec3> reconstructPath(
            Map<BlockPos, BlockPos> parent,
            BlockPos start,
            BlockPos goal,
            Vec3 destination
    ) {
        List<Vec3> result = new ArrayList<>();
        BlockPos current = goal;

        while (!current.equals(start)) {
            result.add(centerOf(current));

            current = parent.get(current);

            if (current == null) {
                return Collections.emptyList();
            }
        }

        Collections.reverse(result);

        // Последняя точка — позиция над противником.
        result.add(destination);

        return result;
    }

    private Vec3 tryEscapeFromObstacle() {
        Vec3 current = this.whisp.position();

        // Сначала пробуем смещение на 1 блок,
        // затем на 1.5 и 2 блока.
        double[] distances = {1.0, 1.5, 2.0};

        // Вверх, вниз, в стороны и по диагонали.
        int[][] directions = {
                {2, 0, 0},
                {-2, 0, 0},
                {0, 0, 2},
                {0, 0, -2},
                {1, 0, 1},
                {1, 0, -1},
                {-1, 0, 1},
                {-1, 0, -1},
                {0, 1, 0},
                {0, -1, 0},
                {1, 1, 0},
                {-1, 1, 0},
                {0, 1, 1},
                {0, 1, -1}
        };

        // Меняем порядок направлений между попытками,
        // чтобы не повторять постоянно один и тот же вариант.
        int offset = this.escapeAttempts++ % directions.length;

        for (double distance : distances) {
            for (int i = 0; i < directions.length; i++) {
                int[] direction =
                        directions[(i + offset) % directions.length];

                Vec3 candidate = current.add(
                        direction[0] * distance,
                        direction[1] * distance,
                        direction[2] * distance
                );

                // Не выходим за вертикальные границы мира.
                if (candidate.y < this.whisp.level().getMinY()
                        || candidate.y >= this.whisp.level().getMaxY()) {
                    continue;
                }

                // Проверяем, помещается ли вся сущность
                // в конечной позиции.
                if (!isPositionClear(candidate)) {
                    continue;
                }

                // Проверяем, свободен ли путь до точки.
                //if (!isSegmentClear(current, candidate)) {
                //    continue;
                //}

                return candidate;
            }
        }

        return null;
    }

    private static Vec3 centerOf(BlockPos pos) {
        return new Vec3(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        );
    }

    private static double heuristic(BlockPos a, BlockPos b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private void faceTarget() {
        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        double dx = this.target.getX() - this.whisp.getX();
        double dy = this.target.getEyeY()
                - (this.whisp.getY() + this.whisp.getBbHeight() * 0.5);
        double dz = this.target.getZ() - this.whisp.getZ();

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (
                Mth.atan2(dz, dx) * (180.0 / Math.PI) - 90.0
        );

        float targetPitch = (float) (
                -Mth.atan2(dy, horizontalDistance) * (180.0 / Math.PI)
        );

        this.whisp.setYRot(
                Mth.rotLerp(0.2F, this.whisp.getYRot(), targetYaw)
        );

        this.whisp.setXRot(
                Mth.rotLerp(0.2F, this.whisp.getXRot(), targetPitch)
        );

        this.whisp.setYHeadRot(this.whisp.getYRot());
        this.whisp.yBodyRot = this.whisp.getYRot();
    }

    private record PathNode(BlockPos pos, double g, double f) {
    }
}
