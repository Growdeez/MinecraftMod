package com.example.firstblocks;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
    DeferredRegister.createBlocks(FirstBlocks.MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK =
    BLOCKS.registerSimpleBlock("example_block",
            properties -> properties
                    .strength(3.0F)
                    .requiresCorrectToolForDrops()
    );



    public static final DeferredBlock<DiscoBallBlock> DISCO_BALL =
            BLOCKS.registerBlock(
                    "disco_ball",
                    properties -> new DiscoBallBlock(
                            properties.strength(1.0F)
                    )
            );

    public static final DeferredBlock<Block> CONTAMINATED_AMBER_ORE =
    BLOCKS.registerSimpleBlock(
            "contaminated_amber_ore",
            properties -> properties
                    .strength(3.0F)
                    .requiresCorrectToolForDrops()
    );


    public static final DeferredBlock<StairBlock> EXAMPLE_STAIRS =
            BLOCKS.registerBlock(
                    "example_stairs",
                    props -> new StairBlock(
                            EXAMPLE_BLOCK.get().defaultBlockState(),
                            props.strength(1.0F)
                    )
            );

    public static final DeferredBlock<SlabBlock> EXAMPLE_SLAB =
            BLOCKS.registerBlock(
                    "example_slab",
                    properties -> new SlabBlock(
                            properties.strength(1.0F)
                    )
            );

    public static final DeferredBlock<WallBlock> EXAMPLE_WALL =
            BLOCKS.registerBlock(
                    "example_wall",
                    properties ->  new WallBlock(
                            properties.strength(1.0F)
                    )
            );

    public static final DeferredBlock<Block> MOSSY_EXAMPLE_BLOCK =
            BLOCKS.registerSimpleBlock("mossy_example_block",
                    properties -> properties.strength(1.0F)
            );

    public static final DeferredBlock<Block> MOSSY_EXAMPLE_STAIRS =
            BLOCKS.registerBlock("mossy_example_stairs",
                    props -> new StairBlock(
                            MOSSY_EXAMPLE_BLOCK.get().defaultBlockState(),
                            props.strength(1.0F)
                    )
            );
    public static final DeferredBlock<SlabBlock> MOSSY_EXAMPLE_SLAB =
            BLOCKS.registerBlock(
                    "mossy_example_slab",
                    properties -> new SlabBlock(
                            properties.strength(1.0F)
                    )
            );

    public static final DeferredBlock<WallBlock> MOSSY_EXAMPLE_WALL =
            BLOCKS.registerBlock(
                    "mossy_example_wall",
                    properties ->  new WallBlock(
                            properties.strength(1.0F)
                    )
            );

    public static final DeferredBlock<WhispAltarBlock> WHISP_ALTAR =
            BLOCKS.registerBlock(
                    "whisp_altar",
                    properties -> new WhispAltarBlock(
                            properties
                                    .strength(2.0F)
                                    .noOcclusion()
                    )
            );
}
