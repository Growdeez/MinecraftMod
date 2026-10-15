package com.example.firstblocks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(FirstBlocks.MODID)
public class FirstBlocks {

    public static final String MODID = "firstblocks";

    public FirstBlocks(
            IEventBus modEventBus,
            ModContainer modContainer
    ) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.register(ModDataGenerators.class);
        modEventBus.register(ModEntityRenderers.class);
        modEventBus.register(ModParticleProviders.class);
        modEventBus.register(ModEntityAttributes.class);
        NeoForge.EVENT_BUS.register(WhispVillageDefenseHandler.class);


        NeoForge.EVENT_BUS.register(EarthWandDrawingEffectManager.class);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        modEventBus.register(ModProfessions.class);
    }
}