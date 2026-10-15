
package com.example.firstblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DiscoBallBlockEntity extends BlockEntity {

    private static final int UPDATE_INTERVAL = 20;
    private static final int GRID_SIZE = 5;
    private static final int MAX_PARTICLES_PER_FACE = 10;
    private static final double PARTICLE_OFFSET = 0.015;

    private int particleTimer = 0;

    public DiscoBallBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISCO_BALL.get(), pos, state);
    }

    public static void clientTick(
            Level level,
            BlockPos pos,
            BlockState state,
            DiscoBallBlockEntity blockEntity
    ) {
        if (!state.getValue(DiscoBallBlock.POWERED)) {
            blockEntity.particleTimer = 0;
            return;
        }

        blockEntity.particleTimer++;

        if (blockEntity.particleTimer < UPDATE_INTERVAL) {
            return;
        }

        blockEntity.particleTimer = 0;

        RandomSource random = level.getRandom();

        // Все шесть граней обновляются в одном цикле.
        for (int face = 0; face < 6; face++) {
            spawnFaceParticles(level, pos, face, random);
        }
    }

    private static void spawnFaceParticles(
            Level level,
            BlockPos pos,
            int face,
            RandomSource random
    ) {
        int totalCells = GRID_SIZE * GRID_SIZE;
        int particleCount = random.nextInt(1, MAX_PARTICLES_PER_FACE + 1);

        // Индексы ячеек: 0..24.
        int[] cells = new int[totalCells];

        for (int i = 0; i < totalCells; i++) {
            cells[i] = i;
        }

        // Частичное перемешивание: выбираем уникальные ячейки.
        for (int i = 0; i < particleCount; i++) {
            int selected = random.nextInt(i, totalCells);

            int temp = cells[i];
            cells[i] = cells[selected];
            cells[selected] = temp;

            int cell = cells[i];

            int column = cell % GRID_SIZE;
            int row = cell / GRID_SIZE;

            double u = (column + 0.5) / GRID_SIZE;
            double v = (row + 0.5) / GRID_SIZE;

            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            double particleX;
            double particleY;
            double particleZ;

            switch (face) {
                case 0 -> { // Верх
                    particleX = x + u;
                    particleY = y + 1.0 + PARTICLE_OFFSET;
                    particleZ = z + v;
                }
                case 1 -> { // Низ
                    particleX = x + u;
                    particleY = y - PARTICLE_OFFSET;
                    particleZ = z + v;
                }
                case 2 -> { // Север
                    particleX = x + u;
                    particleY = y + v;
                    particleZ = z - PARTICLE_OFFSET;
                }
                case 3 -> { // Юг
                    particleX = x + u;
                    particleY = y + v;
                    particleZ = z + 1.0 + PARTICLE_OFFSET;
                }
                case 4 -> { // Восток
                    particleX = x + 1.0 + PARTICLE_OFFSET;
                    particleY = y + v;
                    particleZ = z + u;
                }
                default -> { // Запад, face 5
                    particleX = x - PARTICLE_OFFSET;
                    particleY = y + v;
                    particleZ = z + u;
                }
            }

            level.addParticle(
                    ModParticles.WHITE_SQUARE_PARTICLE.get(),
                    particleX,
                    particleY,
                    particleZ,
                    face,
                    0.0,
                    0.0
            );
        }
    }
}
