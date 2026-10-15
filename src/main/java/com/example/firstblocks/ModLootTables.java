package com.example.firstblocks;

import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTables {

    public static final SingleRegistryBootstrap<LootTable> BOOTSTRAP =
            new LootTableProvider(
                    Set.of(),
                    List.of(
                            new LootTableProvider.SubProviderEntry(
                                    ModBlockLootSubProvider::new,
                                    LootContextParamSets.BLOCK
                            ),
                            new LootTableProvider.SubProviderEntry(
                                    ModChestLootSubProvider::new,
                                    LootContextParamSets.CHEST
                            )
                    )
            );
}