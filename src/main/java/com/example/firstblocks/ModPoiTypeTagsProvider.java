
package com.example.firstblocks;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class ModPoiTypeTagsProvider extends TagsProvider<PoiType> {

    public ModPoiTypeTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(
                output,
                Registries.POINT_OF_INTEREST_TYPE,
                lookupProvider,
                FirstBlocks.MODID
        );
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .add(ModProfessions.OLD_MAGE_POI);
    }
}
