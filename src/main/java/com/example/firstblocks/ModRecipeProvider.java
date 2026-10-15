package com.example.firstblocks;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(
            BootstrapContext<Recipe<?>> recipeOutput,
            BootstrapContext<Advancement> advancementOutput
    ) {
        super(recipeOutput, advancementOutput);
    }

    private void modStonecutterResultFromBase(
            RecipeCategory category,
            ItemLike result,
            ItemLike base,
            int count
    ) {
        ResourceKey<Recipe<?>> recipeId =
                ResourceKey.create(
                        Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(
                                FirstBlocks.MODID,
                                getConversionRecipeName(result, base) + "_stonecutting"
                        )
                );

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(base),
                        category,
                        result,
                        count
                )
                .unlockedBy(
                        getHasName(base),
                        this.has(base)
                )
                .save(this.output, recipeId);
    }

    private void modStonecutterResultFromBase(
            RecipeCategory category,
            ItemLike result,
            ItemLike base
    ) {
        modStonecutterResultFromBase(category, result, base, 1);
    }

    @Override
    protected void buildRecipes() {

        BlockFamily family = new BlockFamily.Builder(
                ModBlocks.EXAMPLE_BLOCK.get()
        )
                .stairs(ModBlocks.EXAMPLE_STAIRS.get())
                .slab(ModBlocks.EXAMPLE_SLAB.get())
                .wall(ModBlocks.EXAMPLE_WALL.get())
                .dontGenerateSmeltingRecipe()
                .getFamily();

        BlockFamily mossy_family = new BlockFamily.Builder(
                ModBlocks.MOSSY_EXAMPLE_BLOCK.get()
        )
                .stairs(ModBlocks.MOSSY_EXAMPLE_STAIRS.get())
                .slab(ModBlocks.MOSSY_EXAMPLE_SLAB.get())
                .wall(ModBlocks.MOSSY_EXAMPLE_WALL.get())
                .dontGenerateSmeltingRecipe()
                .getFamily();

        // Обычный крафт:
        // 6 example_block -> 4 example_stairs
        generateRecipes(
                family,
                FeatureFlagSet.of(FeatureFlags.VANILLA)
        );

        generateRecipes(
                mossy_family,
                FeatureFlagSet.of(FeatureFlags.VANILLA)
        );

        // Стоункаттер крафт:
        // 1 example_block -> 1 example_stairs
        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.EXAMPLE_STAIRS.get(),
                ModBlocks.EXAMPLE_BLOCK.get()
        );

        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.EXAMPLE_SLAB.get(),
                ModBlocks.EXAMPLE_BLOCK.get(),
                2
        );

        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.EXAMPLE_WALL.get(),
                ModBlocks.EXAMPLE_BLOCK.get()
        );

        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.MOSSY_EXAMPLE_WALL.get(),
                ModBlocks.MOSSY_EXAMPLE_BLOCK.get()
        );

        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.MOSSY_EXAMPLE_STAIRS.get(),
                ModBlocks.MOSSY_EXAMPLE_BLOCK.get()
        );

        modStonecutterResultFromBase(
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.MOSSY_EXAMPLE_SLAB.get(),
                ModBlocks.MOSSY_EXAMPLE_BLOCK.get(),
                2
        );



        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_AMBER.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        CookingBookCategory.BLOCKS,
                        ModItems.REFINED_AMBER.get(),
                        0.2F,
                        400
                )
                .unlockedBy(
                        getHasName(ModItems.RAW_AMBER.get()),
                        has(ModItems.RAW_AMBER.get())
                )
                .save(output);

        this.shaped(RecipeCategory.COMBAT, ModItems.EARTH_WAND.get())
                .define('G', Items.GOLD_INGOT)
                .define('A', ModItems.REFINED_AMBER.get())
                .define('S', ItemTags.SAPLINGS)
                .pattern("AG ")
                .pattern("GS ")
                .pattern("  S")
                .unlockedBy(
                        "has_refined_amber",
                        this.has(ModItems.REFINED_AMBER.get())
                )
                .save(this.output);

        this.shaped(RecipeCategory.COMBAT, ModItems.DISCO_BALL.get())
                .define('I', Items.IRON_INGOT)
                .define('A', ModItems.REFINED_AMBER.get())
                .define('L', Items.LAPIS_LAZULI)
                .define('R', Items.REDSTONE)
                .define('E', Items.EMERALD)
                .define('Q', Items.QUARTZ)


                .pattern("IAI")
                .pattern("EQL")
                .pattern("IRI")
                .unlockedBy(
                        "has_refined_amber",
                        this.has(ModItems.REFINED_AMBER.get())
                )
                .save(this.output);

        this.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.MOSSY_EXAMPLE_BLOCK_ITEM.get())
                .define('E', ModItems.EXAMPLE_BLOCK_ITEM.get())
                .define('M', Items.MOSS_BLOCK)

                .pattern("   ")
                .pattern("EM ")
                .pattern("   ")
                .unlockedBy(
                        "has_example_block",
                        this.has(ModItems.EXAMPLE_BLOCK_ITEM.get())
                )

                .save(this.output);

    }
}