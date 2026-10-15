package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public class ModBlockTags {

    public static final TagKey<Block> CONTAMINATED_AMBER_ORE_REPLACEABLES =
            TagKey.create(
                    Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "contaminated_amber_ore_replaceables"
                    )
            );
}
