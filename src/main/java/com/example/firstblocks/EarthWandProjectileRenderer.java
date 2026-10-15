package com.example.firstblocks;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class EarthWandProjectileRenderer
        extends EntityRenderer<EarthWandProjectile, EntityRenderState> {

    public EarthWandProjectileRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void extractRenderState(
            EarthWandProjectile entity,
            EntityRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);
    }
}