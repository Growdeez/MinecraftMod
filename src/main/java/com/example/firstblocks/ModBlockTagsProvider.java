package com.example.firstblocks;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.minecraft.tags.BlockTags;


public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(
                output,
                lookupProvider,
                FirstBlocks.MODID
        );
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.WALLS)
                .add(
                        ModBlocks.EXAMPLE_WALL.getKey(),
                        ModBlocks.MOSSY_EXAMPLE_WALL.getKey()
                );


        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        ModBlocks.EXAMPLE_BLOCK.getKey(),
                        ModBlocks.CONTAMINATED_AMBER_ORE.getKey()
                );
        tag(ModBlockTags.CONTAMINATED_AMBER_ORE_REPLACEABLES)
                .add(ModBlocks.EXAMPLE_BLOCK.getKey());
    }
}