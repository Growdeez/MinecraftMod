package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;

public class ModChestLootSubProvider implements LootTableSubProvider {

    private final LootTableSubProvider.Context output;

    public ModChestLootSubProvider(LootTableSubProvider.Context output) {
        this.output = output;
    }

    @Override
    public void run() {
        this.output.accept(
                ResourceKey.create(
                        Registries.LOOT_TABLE,
                        Identifier.fromNamespaceAndPath(
                                "firstblocks",
                                "chests/old_magic_village"
                        )
                ),
                LootTable.lootTable()
                        .withPool(
                                net.minecraft.world.level.storage.loot.LootPool.lootPool()

                                        .add(
                                                LootItem.lootTableItem(
                                                        ModItems.REFINED_AMBER.get()
                                                )
                                        )
                        )
        );
    }
}