package com.example.firstblocks;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class WhispProjectileRenderer
        extends EntityRenderer<WhispProjectile, EntityRenderState> {

    public WhispProjectileRenderer(
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
            WhispProjectile entity,
            EntityRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);
    }
}
