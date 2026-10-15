package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class ModPoiTypes {

    public static final ResourceKey<PoiType> OLD_MAGE_POI =
            ResourceKey.create(
                    Registries.POINT_OF_INTEREST_TYPE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage_poi"
                    )
            );
}