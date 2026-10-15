package com.example.firstblocks;


import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModReloadableRegistries {

    public static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()
                    .add(
                            Registries.LOOT_TABLE,
                            new LootTableProvider(
                                    Set.of(),
                                    List.of(
                                            new LootTableProvider.SubProviderEntry(
                                                    ModBlockLootSubProvider::new,
                                                    LootContextParamSets.BLOCK
                                            )
                                    )
                            )
                    );
}
