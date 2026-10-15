
package com.example.firstblocks;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class WhispRenderer extends MobRenderer<
        Whisp,
        LivingEntityRenderState,
        WhispModel
        > {

    public WhispRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new WhispModel(context.bakeLayer(WhispModel.LAYER_LOCATION)),
                0.2F
        );
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return Identifier.fromNamespaceAndPath(
                FirstBlocks.MODID,
                "textures/entity/whisp.png"
        );
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
