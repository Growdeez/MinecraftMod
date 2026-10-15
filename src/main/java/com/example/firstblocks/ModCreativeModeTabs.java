package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.chat.Component;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FirstBlocks.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FIRST_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register("first_blocks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.firstblocks"))
                    .icon(() -> ModItems.EXAMPLE_BLOCK_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.EXAMPLE_BLOCK_ITEM.get());
                        output.accept(ModItems.EXAMPLE_STAIRS_ITEM.get());
                        output.accept(ModItems.EXAMPLE_SLAB_ITEM.get());
                        output.accept(ModItems.EXAMPLE_WALL_ITEM.get());
                        output.accept(ModItems.MOSSY_EXAMPLE_BLOCK_ITEM.get());
                        output.accept(ModItems.MOSSY_EXAMPLE_STAIRS_ITEM.get());
                        output.accept(ModItems.MOSSY_EXAMPLE_SLAB_ITEM.get());
                        output.accept(ModItems.MOSSY_EXAMPLE_WALL_ITEM.get());
                        output.accept(ModItems.CONTAMINATED_AMBER_ORE_ITEM.get());
                        output.accept(ModItems.RAW_AMBER.get());
                        output.accept(ModItems.REFINED_AMBER.get());
                        output.accept(ModItems.EARTH_WAND.get());
                        output.accept(ModItems.DISCO_BALL.get());
                        output.accept(ModItems.WHISP_ALTAR_ITEM.get());
                    })
                    .build());
}
