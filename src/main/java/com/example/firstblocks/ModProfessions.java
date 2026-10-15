
package com.example.firstblocks;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;

public class ModProfessions {

    public static final ResourceKey<PoiType> OLD_MAGE_POI =
            ResourceKey.create(
                    Registries.POINT_OF_INTEREST_TYPE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage_poi"
                    )
            );

    public static final ResourceKey<VillagerProfession> OLD_MAGE =
            ResourceKey.create(
                    Registries.VILLAGER_PROFESSION,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage"
                    )
            );

    // Регистрация собственного POI
    @SubscribeEvent
    public static void registerPoi(RegisterEvent event) {
        event.register(
                Registries.POINT_OF_INTEREST_TYPE,
                helper -> helper.register(
                        OLD_MAGE_POI,
                        new PoiType(
                                ImmutableSet.of(),
                                1,
                                1
                        )
                )
        );
    }
    @SubscribeEvent
    public static void extendPoiTypes(ExtendPoiTypesEvent event) {
        System.out.println("[FirstBlocks] ExtendPoiTypesEvent FIRED");

        event.addBlockToPoi(OLD_MAGE_POI, Blocks.BOOKSHELF);

        System.out.println(
                "[FirstBlocks] BOOKSHELF registered for OLD_MAGE_POI"
        );
    }

    // Регистрация профессии
    @SubscribeEvent
    public static void registerProfession(RegisterEvent event) {
        event.register(
                Registries.VILLAGER_PROFESSION,
                helper -> helper.register(
                        OLD_MAGE,
                        new VillagerProfession(
                                Component.translatable(
                                        "entity.firstblocks.villager.old_mage"
                                ),
                                poi -> poi.is(OLD_MAGE_POI),
                                poi -> poi.is(OLD_MAGE_POI),
                                ImmutableSet.<Item>of(),
                                ImmutableSet.<Block>of(),
                                SoundEvents.VILLAGER_WORK_LIBRARIAN,
                                Int2ObjectMap.ofEntries(
                                        Int2ObjectMap.entry(
                                                1,
                                                ModTradeData.OLD_MAGE_LEVEL_1
                                        )
                                )
                        )
                )
        );
    }


    @SubscribeEvent
    public static void checkPoi(
            net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event
    ) {
        event.enqueueWork(() -> {
            var registry =
                    net.minecraft.core.registries.BuiltInRegistries.POINT_OF_INTEREST_TYPE;

            var poi = registry.get(
                    net.minecraft.resources.Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_mage_poi"
                    )
            );

            System.out.println(
                    "[FirstBlocks] OLD_MAGE_POI registered: " + (poi != null)
            );

            var state =
                    net.minecraft.world.level.block.Blocks.BOOKSHELF.defaultBlockState();

            var mappedPoi =
                    net.minecraft.world.entity.ai.village.poi.PoiTypes.forState(state);

            System.out.println(
                    "[FirstBlocks] BOOKSHELF POI: " + mappedPoi
            );

            System.out.println(
                    "[FirstBlocks] Is old mage POI: " +
                            mappedPoi.map(holder -> holder.is(OLD_MAGE_POI)).orElse(false)
            );
        });
    }



}
