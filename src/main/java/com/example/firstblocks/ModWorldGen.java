
package com.example.firstblocks;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModWorldGen {

    // ==================================================
    // PINK STONE
    // ==================================================

    public static final ResourceKey<Feature> PINK_STONE =
            ResourceKey.create(
                    Registries.FEATURE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "pink_stone"
                    )
            );

    public static final ResourceKey<PlacedFeature> PINK_STONE_PLACED =
            ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "pink_stone"
                    )
            );

    public static final ResourceKey<BiomeModifier> ADD_PINK_STONE =
            ResourceKey.create(
                    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "add_pink_stone"
                    )
            );

    // ==================================================
    // CONTAMINATED AMBER ORE
    // ==================================================

    public static final ResourceKey<Feature> CONTAMINATED_AMBER_ORE =
            ResourceKey.create(
                    Registries.FEATURE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "contaminated_amber_ore"
                    )
            );

    public static final ResourceKey<PlacedFeature> CONTAMINATED_AMBER_ORE_PLACED =
            ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "contaminated_amber_ore"
                    )
            );

    public static final ResourceKey<BiomeModifier> ADD_CONTAMINATED_AMBER_ORE =
            ResourceKey.create(
                    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "add_contaminated_amber_ore"
                    )
            );

    // ==================================================
    // OLD MAGIC VILLAGE TEMPLATE POOLS
    // ==================================================

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_START_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/start"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_PATH_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/path"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_HOUSE_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/house"
                    )
            );

    // ==================================================
    // OLD MAGIC VILLAGE STRUCTURE
    // ==================================================

    public static final ResourceKey<Structure> OLD_MAGIC_VILLAGE =
            ResourceKey.create(
                    Registries.STRUCTURE,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village"
                    )
            );

    public static final ResourceKey<StructureSet> OLD_MAGIC_VILLAGE_SET =
            ResourceKey.create(
                    Registries.STRUCTURE_SET,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_VILLAGERS_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/villagers"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_WHISP_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/whisp"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> OLD_MAGIC_VILLAGE_ALTAR_TREE_POOL =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "old_magic_village/altar_tree"
                    )
            );

    // ==================================================
    // REGISTRY BUILDER
    // ==================================================

    public static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()

                    // ---------- FEATURES ----------

                    .add(Registries.FEATURE, bootstrap -> {

                        bootstrap.register(
                                PINK_STONE,
                                new OreFeature(
                                        new BlockMatchTest(Blocks.STONE),
                                        ModBlocks.EXAMPLE_BLOCK.get().defaultBlockState(),
                                        20
                                )
                        );

                        bootstrap.register(
                                CONTAMINATED_AMBER_ORE,
                                new OreFeature(
                                        new BlockMatchTest(ModBlocks.EXAMPLE_BLOCK.get()),
                                        ModBlocks.CONTAMINATED_AMBER_ORE.get().defaultBlockState(),
                                        20
                                )
                        );
                    })

                    // ---------- PLACED FEATURES ----------

                    .add(Registries.PLACED_FEATURE, bootstrap -> {

                        HolderGetter<Feature> features =
                                bootstrap.lookup(Registries.FEATURE);

                        bootstrap.register(
                                PINK_STONE_PLACED,
                                new PlacedFeature(
                                        features.getOrThrow(PINK_STONE),
                                        List.of(
                                                CountPlacement.of(15),
                                                InSquarePlacement.spread(),
                                                HeightRangePlacement.uniform(
                                                        VerticalAnchor.absolute(16),
                                                        VerticalAnchor.absolute(64)
                                                ),
                                                BiomeFilter.biome()
                                        )
                                )
                        );

                        bootstrap.register(
                                CONTAMINATED_AMBER_ORE_PLACED,
                                new PlacedFeature(
                                        features.getOrThrow(CONTAMINATED_AMBER_ORE),
                                        List.of(
                                                CountPlacement.of(15),
                                                InSquarePlacement.spread(),
                                                HeightRangePlacement.uniform(
                                                        VerticalAnchor.absolute(16),
                                                        VerticalAnchor.absolute(64)
                                                ),
                                                BiomeFilter.biome()
                                        )
                                )
                        );
                    })

                    // ---------- BIOME MODIFIERS ----------

                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {

                        HolderGetter<Biome> biomes =
                                bootstrap.lookup(Registries.BIOME);

                        HolderGetter<PlacedFeature> placedFeatures =
                                bootstrap.lookup(Registries.PLACED_FEATURE);

                        bootstrap.register(
                                ADD_PINK_STONE,
                                new AddFeaturesBiomeModifier(
                                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                                        HolderSet.direct(
                                                placedFeatures.getOrThrow(PINK_STONE_PLACED)
                                        ),
                                        GenerationStep.Decoration.LOCAL_MODIFICATIONS
                                )
                        );

                        bootstrap.register(
                                ADD_CONTAMINATED_AMBER_ORE,
                                new AddFeaturesBiomeModifier(
                                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                                        HolderSet.direct(
                                                placedFeatures.getOrThrow(CONTAMINATED_AMBER_ORE_PLACED)
                                        ),
                                        GenerationStep.Decoration.UNDERGROUND_ORES
                                )
                        );
                    })

                    // ---------- TEMPLATE POOLS ----------

                    .add(Registries.TEMPLATE_POOL, bootstrap -> {

                        HolderGetter<StructureTemplatePool> pools =
                                bootstrap.lookup(Registries.TEMPLATE_POOL);

                        HolderGetter<StructureProcessorList> processors =
                                bootstrap.lookup(Registries.PROCESSOR_LIST);

                        Holder<StructureTemplatePool> emptyPool =
                                pools.getOrThrow(
                                        ResourceKey.create(
                                                Registries.TEMPLATE_POOL,
                                                Identifier.fromNamespaceAndPath(
                                                        "minecraft",
                                                        "empty"
                                                )
                                        )
                                );

                        Holder<StructureProcessorList> emptyProcessors =
                                processors.getOrThrow(
                                        ResourceKey.create(
                                                Registries.PROCESSOR_LIST,
                                                Identifier.fromNamespaceAndPath(
                                                        "minecraft",
                                                        "empty"
                                                )
                                        )
                                );

                        // Стартовый пул: центральное дерево
                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_START_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:center_town_tree_1",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                        // Пул дорог
                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_PATH_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:path_1",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                ),
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:path_2",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                        // Пул домов
                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_HOUSE_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:old_magic_house_3",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                ),
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:old_magic_house_1",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                ),
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:whisp_altar_1",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                        // Пул жителей
                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_VILLAGERS_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "minecraft:village/plains/villagers/unemployed",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_WHISP_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.single(
                                                                "firstblocks:old_magic_village/whisp",
                                                                emptyProcessors
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                        HolderGetter<PlacedFeature> placedFeatures =
                                bootstrap.lookup(Registries.PLACED_FEATURE);

                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_ALTAR_TREE_POOL,
                                new StructureTemplatePool(
                                        emptyPool,
                                        List.of(
                                                Pair.of(
                                                        projection -> StructurePoolElement.feature(
                                                                placedFeatures.getOrThrow(
                                                                        ResourceKey.create(
                                                                                Registries.PLACED_FEATURE,
                                                                                Identifier.fromNamespaceAndPath(
                                                                                        "minecraft",
                                                                                        "oak"
                                                                                )
                                                                        )
                                                                )
                                                        ).apply(projection),
                                                        1
                                                )
                                        ),
                                        StructureTemplatePool.Projection.RIGID
                                )
                        );

                    })

                    // ---------- STRUCTURE ----------

                    .add(Registries.STRUCTURE, bootstrap -> {

                        HolderGetter<Biome> biomes =
                                bootstrap.lookup(Registries.BIOME);

                        HolderGetter<StructureTemplatePool> pools =
                                bootstrap.lookup(Registries.TEMPLATE_POOL);

                        bootstrap.register(
                                OLD_MAGIC_VILLAGE,
                                new JigsawStructure(
                                        new Structure.StructureSettings(

                                                HolderSet.direct(
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "plains")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "sunflower_plains")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "forest")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "flower_forest")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "birch_forest")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "old_growth_birch_forest")
                                                        )),
                                                        biomes.getOrThrow(ResourceKey.create(
                                                                Registries.BIOME,
                                                                Identifier.fromNamespaceAndPath("minecraft", "dark_forest")
                                                        ))
                                                ),

                                                java.util.Map.of(),
                                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                                TerrainAdjustment.BEARD_THIN
                                        ),

                                        pools.getOrThrow(OLD_MAGIC_VILLAGE_START_POOL),

                                        // Максимальная глубина соединения элементов
                                        12,

                                        ConstantHeight.of(VerticalAnchor.absolute(0)),

                                        false,

                                        Heightmap.Types.WORLD_SURFACE_WG
                                )
                        );
                    })

                    // ---------- STRUCTURE SET ----------

                    .add(Registries.STRUCTURE_SET, bootstrap -> {

                        HolderGetter<Structure> structures =
                                bootstrap.lookup(Registries.STRUCTURE);

                        bootstrap.register(
                                OLD_MAGIC_VILLAGE_SET,
                                new StructureSet(
                                        structures.getOrThrow(OLD_MAGIC_VILLAGE),
                                        new RandomSpreadStructurePlacement(
                                                32,
                                                8,
                                                RandomSpreadType.LINEAR,
                                                18472631
                                        )
                                )
                        );
                    })
                    .add(Registries.VILLAGER_TRADE, ModTradeBootstrap::bootstrapTrades)
                    .add(Registries.TRADE_SET, ModTradeBootstrap::bootstrapTradeSets);
}
