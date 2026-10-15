package com.example.firstblocks;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;


public class ModItems {
    public static final DeferredRegister.Items ITEMS =
    DeferredRegister.createItems(FirstBlocks.MODID);

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", ModBlocks.EXAMPLE_BLOCK);

    public static final DeferredItem<BlockItem> MOSSY_EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mossy_example_block", ModBlocks.MOSSY_EXAMPLE_BLOCK);

    public static final DeferredItem<BlockItem> CONTAMINATED_AMBER_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(
                    "contaminated_amber_ore",
                    ModBlocks.CONTAMINATED_AMBER_ORE
            );

    public static final DeferredItem<BlockItem> DISCO_BALL =
            ITEMS.registerSimpleBlockItem(
                    "disco_ball",
                    ModBlocks.DISCO_BALL
            );

    public static final DeferredItem<Item> RAW_AMBER =
            ITEMS.registerSimpleItem("raw_amber");

    public static final DeferredItem<Item> REFINED_AMBER =
            ITEMS.registerSimpleItem("refined_amber");

    public static final DeferredItem<Item> EARTH_WAND =
            ITEMS.registerItem(
                    "earth_wand",
                    EarthWandItem::new);

    public static final DeferredItem<BlockItem> EXAMPLE_STAIRS_ITEM =
            ITEMS.registerSimpleBlockItem("example_stairs", ModBlocks.EXAMPLE_STAIRS);

    public static final DeferredItem<BlockItem> EXAMPLE_SLAB_ITEM =
            ITEMS.registerSimpleBlockItem("example_slab", ModBlocks.EXAMPLE_SLAB);

    public static final DeferredItem<BlockItem> EXAMPLE_WALL_ITEM =
            ITEMS.registerSimpleBlockItem("example_wall", ModBlocks.EXAMPLE_WALL);

    public static final DeferredItem<BlockItem> MOSSY_EXAMPLE_STAIRS_ITEM =
            ITEMS.registerSimpleBlockItem("mossy_example_stairs", ModBlocks.MOSSY_EXAMPLE_STAIRS);

    public static final DeferredItem<BlockItem> MOSSY_EXAMPLE_SLAB_ITEM =
            ITEMS.registerSimpleBlockItem("mossy_example_slab", ModBlocks.MOSSY_EXAMPLE_SLAB);

    public static final DeferredItem<BlockItem> MOSSY_EXAMPLE_WALL_ITEM =
            ITEMS.registerSimpleBlockItem("mossy_example_wall", ModBlocks.MOSSY_EXAMPLE_WALL);

    public static final DeferredItem<BlockItem> WHISP_ALTAR_ITEM =
            ITEMS.registerSimpleBlockItem(
                    "whisp_altar",
                    ModBlocks.WHISP_ALTAR
            );

}
