package com.example.firstblocks;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        event.registerEntityRenderer(
                ModEntities.EARTH_WAND_PROJECTILE.get(),
                EarthWandProjectileRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.WHISP_PROJECTILE.get(),
                WhispProjectileRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.WHISP.get(),
                WhispRenderer::new
        );

        event.registerBlockEntityRenderer(
                ModBlockEntities.WHISP_ALTAR.get(),
                WhispAltarRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event
    ) {
        event.registerLayerDefinition(
                WhispModel.LAYER_LOCATION,
                WhispModel::createBodyLayer
        );
    }
}