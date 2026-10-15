package com.example.firstblocks;

import java.util.Optional;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.data.worldgen.BootstrapContext;


public class ModTradeBootstrap {

    public static void bootstrapTrades(
            BootstrapContext<VillagerTrade> context
    ) {
        context.register(
                ModTradeData.OLD_MAGE_AMBER_EXCHANGE,
                VillagerTrade.builder(
                        new TradeCost(ModItems.RAW_AMBER.get(), 4),
                        new ItemStackTemplate(ModItems.REFINED_AMBER.get()),
                        4,
                        2,
                        0.0F
                ).build()
        );
    }

    public static void bootstrapTradeSets(
            BootstrapContext<TradeSet> context
    ) {
        var tradeLookup = context.lookup(Registries.VILLAGER_TRADE);

        var trade = tradeLookup.getOrThrow(
                ModTradeData.OLD_MAGE_AMBER_EXCHANGE
        );

        context.register(
                ModTradeData.OLD_MAGE_LEVEL_1,
                new TradeSet(
                        HolderSet.direct(trade),
                        ContextIntProviders.exactly(1),
                        false,
                        Optional.empty()
                )
        );
    }
}