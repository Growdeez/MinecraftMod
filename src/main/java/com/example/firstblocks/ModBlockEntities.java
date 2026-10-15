
package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    FirstBlocks.MODID
            );

    public static final Supplier<BlockEntityType<DiscoBallBlockEntity>> DISCO_BALL =
            BLOCK_ENTITY_TYPES.register(
                    "disco_ball",
                    () -> new BlockEntityType<>(
                            DiscoBallBlockEntity::new,
                            false,
                            ModBlocks.DISCO_BALL.get()
                    )
            );

    public static final Supplier<BlockEntityType<WhispAltarBlockEntity>> WHISP_ALTAR =
            BLOCK_ENTITY_TYPES.register(
                    "whisp_altar",
                    () -> new BlockEntityType<>(
                            WhispAltarBlockEntity::new,
                            false,
                            ModBlocks.WHISP_ALTAR.get()
                    )
            );
}
