package com.example.firstblocks;

import java.util.Set;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.fml.common.Mod;

public class ModBlockLootSubProvider extends BlockLootSubProvider {

    public ModBlockLootSubProvider(LootTableSubProvider.Context output) {
        super(
                Set.of(),
                FeatureFlags.DEFAULT_FLAGS,
                output
        );
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.EXAMPLE_BLOCK.get());
        dropSelf(ModBlocks.EXAMPLE_STAIRS.get());
        dropSelf(ModBlocks.DISCO_BALL.get());
        dropSelf(ModBlocks.EXAMPLE_SLAB.get());
        dropSelf(ModBlocks.EXAMPLE_WALL.get());
        dropSelf(ModBlocks.MOSSY_EXAMPLE_BLOCK.get());
        dropSelf(ModBlocks.MOSSY_EXAMPLE_SLAB.get());
        dropSelf(ModBlocks.MOSSY_EXAMPLE_STAIRS.get());
        dropSelf(ModBlocks.MOSSY_EXAMPLE_WALL.get());

        add(
                ModBlocks.CONTAMINATED_AMBER_ORE.get(),
                createSingleItemTable(ModItems.RAW_AMBER.get())
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(
                ModBlocks.EXAMPLE_BLOCK.get(),
                ModBlocks.EXAMPLE_STAIRS.get(),
                ModBlocks.DISCO_BALL.get(),
                ModBlocks.EXAMPLE_SLAB.get(),
                ModBlocks.EXAMPLE_WALL.get(),
                ModBlocks.CONTAMINATED_AMBER_ORE.get(),
                ModBlocks.MOSSY_EXAMPLE_BLOCK.get(),
                ModBlocks.MOSSY_EXAMPLE_SLAB.get(),
                ModBlocks.MOSSY_EXAMPLE_STAIRS.get(),
                ModBlocks.MOSSY_EXAMPLE_WALL.get()
        );
    }
}