package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.minecraft.core.RegistrySetBuilder;

public class ModDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        event.createWorldRegistryObjects(ModWorldGen.BUILDER);
        RegistrySetBuilder reloadableBuilder =
                new RegistrySetBuilder()
                        .add(
                                Registries.LOOT_TABLE,
                                ModLootTables.BOOTSTRAP
                        )
                        .add(
                                ModRecipes.BOOTSTRAP
                        );

        event.createReloadableRegistryObjects(reloadableBuilder);

        event.createProvider(
                ModBlockTagsProvider::new
        );

        event.addProvider(
                new ModModelProvider(
                        event.getGenerator().getPackOutput()
                )
        );

        event.createProvider(
                ModPoiTypeTagsProvider::new
        );

    }
}