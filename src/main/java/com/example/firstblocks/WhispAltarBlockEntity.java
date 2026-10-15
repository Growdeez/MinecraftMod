package com.example.firstblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.UUID;


public class WhispAltarBlockEntity extends BlockEntity {

    private UUID whispUUID;
    private long respawnAtGameTime;

    public static void serverTick(
            net.minecraft.world.level.Level level,
            net.minecraft.core.BlockPos pos,
            net.minecraft.world.level.block.state.BlockState state,
            WhispAltarBlockEntity blockEntity
    ) {
        // Если у алтаря уже есть UUID, проверяем его виспа.
        if (blockEntity.whispUUID != null) {
            net.minecraft.world.entity.Entity entity =
                    level.getEntity(blockEntity.whispUUID);

            if (entity instanceof Whisp whisp && whisp.isAlive()) {
                return;
            }

            // Пока не сбрасываем UUID, если сущность не найдена.
            return;
        }

        // Ждём окончания кулдауна перед созданием нового виспа.
        if (blockEntity.whispUUID == null
                && level.getGameTime() < blockEntity.respawnAtGameTime) {
            return;
        }

        // Создаём виспа, если UUID ещё нет.
        Whisp whisp = ModEntities.WHISP.get().create(
                level,
                EntitySpawnReason.TRIGGERED
        );

        if (whisp == null) {
            return;
        }

        // Устанавливаем позицию над алтарём.
        whisp.setPos(
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5
        );

        System.out.println(whisp.position());

        // Запоминаем, к какому алтарю привязан висп.
        whisp.setAltarPos(pos);

        // Добавляем сущность в мир и сохраняем её UUID.
        if (level.addFreshEntity(whisp)) {
            blockEntity.whispUUID = whisp.getUUID();
            System.out.println(whisp.getUUID());
            blockEntity.setChanged();
        }
    }


    public WhispAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WHISP_ALTAR.get(), pos, state);

    }

    public static void clientTick(
            net.minecraft.world.level.Level level,
            net.minecraft.core.BlockPos pos,
            net.minecraft.world.level.block.state.BlockState state,
            WhispAltarBlockEntity blockEntity
    ) {
        if (level.getRandom().nextInt(3) != 0) {
            return;
        }

        // искры
        if (level.getRandom().nextInt(5) == 0) {
            double x = pos.getX() + 0.25 + level.getRandom().nextDouble() * 0.5;
            double y = pos.getY() + 0.72 + level.getRandom().nextDouble() * 0.18;
            double z = pos.getZ() + 0.25 + level.getRandom().nextDouble() * 0.5;

            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.END_ROD,
                    x, y, z,
                    (level.getRandom().nextDouble() - 0.5) * 0.06,
                    0.01 + level.getRandom().nextDouble() * 0.025,
                    (level.getRandom().nextDouble() - 0.5) * 0.06
            );
        }

        double[][] positions = {
                {0.38, 0.70, 0.38},
                {0.62, 0.70, 0.38},
                {0.38, 0.70, 0.62},
                {0.62, 0.70, 0.62},
                {0.50, 0.70, 0.50}
        };

        for (double[] p : positions) {
            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
                    pos.getX() + p[0],
                    pos.getY() + p[1],
                    pos.getZ() + p[2],
                    0.0,
                    0.015,
                    0.0
            );
        }
    }

    public void onWhispDeath(UUID deadWhispUUID) {
        if (deadWhispUUID.equals(this.whispUUID)) {
            this.whispUUID = null;

            // 30 секунд = 600 игровых тиков.
            this.respawnAtGameTime = this.level.getGameTime() + 600;

            this.setChanged();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        if (this.whispUUID != null) {
            output.putString("WhispUUID", this.whispUUID.toString());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        String uuidString = input.getStringOr("WhispUUID", "");

        if (uuidString.isEmpty()) {
            this.whispUUID = null;
            return;
        }

        try {
            this.whispUUID = UUID.fromString(uuidString);
        } catch (IllegalArgumentException exception) {
            this.whispUUID = null;
        }
    }
}