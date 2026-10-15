package com.example.firstblocks;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;


public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, FirstBlocks.MODID);
    }

    @Override
    protected void registerModels(
            BlockModelGenerators blockModels,
            ItemModelGenerators itemModels
    ) {

        blockModels.family(ModBlocks.EXAMPLE_BLOCK.get())
                .stairs(ModBlocks.EXAMPLE_STAIRS.get())
                .slab(ModBlocks.EXAMPLE_SLAB.get())
                .wall(ModBlocks.EXAMPLE_WALL.get());

        blockModels.family(ModBlocks.MOSSY_EXAMPLE_BLOCK.get())
                .stairs(ModBlocks.MOSSY_EXAMPLE_STAIRS.get())
                .slab(ModBlocks.MOSSY_EXAMPLE_SLAB.get())
                .wall(ModBlocks.MOSSY_EXAMPLE_WALL.get());


        blockModels.createTrivialCube(
                ModBlocks.CONTAMINATED_AMBER_ORE.get()
        );


        var discoBall = ModBlocks.DISCO_BALL.get();

        var discoBallModel = TexturedModel.CUBE.create(
                discoBall,
                blockModels.modelOutput
        );

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        discoBall,
                        BlockModelGenerators.plainVariant(discoBallModel)
                ).with(
                        PropertyDispatch.modify(DiscoBallBlock.POWERED)
                                .select(false, BlockModelGenerators.NOP)
                                .select(true, BlockModelGenerators.NOP)
                )
        );

        itemModels.generateFlatItem(
                ModItems.RAW_AMBER.get(),
                ModelTemplates.FLAT_ITEM
        );

        itemModels.generateFlatItem(
                ModItems.REFINED_AMBER.get(),
                ModelTemplates.FLAT_ITEM
        );

        ExtendedModelTemplateBuilder wandModel =
                ExtendedModelTemplateBuilder.of(ModelTemplates.FLAT_ITEM);

        // Третье лицо — правая рука
        wandModel.transform(
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                transform -> transform
                        .rotation(0.0F, -90.0F, -50.0F)
                        .translation(0.0F, 5.0F, 0.5F)
                        .scale(0.85F)
        );

        // Третье лицо — левая рука
        wandModel.transform(
                ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                transform -> transform
                        .rotation(0.0F, 90.0F, 50.0F)
                        .translation(0.0F, 5.0F, 0.5F)
                        .scale(0.85F)
        );

        // Первое лицо — правая рука
        wandModel.transform(
                ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                transform -> transform
                        .rotation(0.0F, -90.0F, -55.0F)
                        .translation(1.13F, 3.2F, 1.13F)
                        .scale(0.68F)
        );

        // Первое лицо — левая рука
        wandModel.transform(
                ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                transform -> transform
                        .rotation(0.0F, 90.0F, -205.0F)
                        .translation(1.13F, 3.2F, 1.13F)
                        .scale(0.68F)
        );

        itemModels.generateFlatItem(
                ModItems.EARTH_WAND.get(),
                wandModel.build()
        );

        var whispAltar = ModBlocks.WHISP_ALTAR.get();

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        whispAltar,
                        BlockModelGenerators.plainVariant(
                                ModelLocationUtils.getModelLocation(whispAltar)
                        )
                )
        );


    }
}