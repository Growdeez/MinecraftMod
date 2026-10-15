package com.example.firstblocks;

import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.data.recipes.RecipeProvider;

public class ModRecipes {

    public static final MultiRegistryBootstrap BOOTSTRAP =
            RecipeProvider.asBootstrap(ModRecipeProvider::new);
}