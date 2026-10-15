package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public class ModTradeData {

    public static final ResourceKey<VillagerTrade> OLD_MAGE_AMBER_EXCHANGE =
            ResourceKey.create(
                    Registries.VILLAGER_TRADE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage/1/amber_exchange"
                    )
            );

    public static final ResourceKey<TradeSet> OLD_MAGE_LEVEL_1 =
            ResourceKey.create(
                    Registries.TRADE_SET,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage/level_1"
                    )
            );


}