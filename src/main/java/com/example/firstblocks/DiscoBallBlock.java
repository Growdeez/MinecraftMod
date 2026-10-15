
package com.example.firstblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;



public class DiscoBallBlock extends Block implements EntityBlock {

    public static final BooleanProperty POWERED =
            BlockStateProperties.POWERED;


    public DiscoBallBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(POWERED, false)
        );
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (!level.isClientSide()) {
            return null;
        }

        if (type != ModBlockEntities.DISCO_BALL.get()) {
            return null;
        }

        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof DiscoBallBlockEntity discoBall) {
                DiscoBallBlockEntity.clientTick(
                        tickLevel,
                        pos,
                        tickState,
                        discoBall
                );
            }
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DiscoBallBlockEntity(pos, state);
    }




    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean powered = context.getLevel()
                .hasNeighborSignal(context.getClickedPos());

        return this.defaultBlockState()
                .setValue(POWERED, powered);
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            net.minecraft.world.level.redstone.Orientation orientation,
            boolean movedByPiston
    ) {
        super.neighborChanged(
                state, level, pos, block, orientation, movedByPiston
        );

        updatePower(state, level, pos);
    }

    private void updatePower(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        if (level.isClientSide()) {
            return;
        }

        boolean powered = level.hasNeighborSignal(pos);

        if (state.getValue(POWERED) != powered) {
            System.out.println(
                    "[DiscoBall] Redstone state changed at " + pos
                            + ": " + state.getValue(POWERED)
                            + " -> " + powered
            );

            level.setBlock(
                    pos,
                    state.setValue(POWERED, powered),
                    Block.UPDATE_ALL
            );
        }
    }
}
