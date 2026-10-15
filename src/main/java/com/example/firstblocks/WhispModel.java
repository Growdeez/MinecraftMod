package com.example.firstblocks;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class WhispModel extends EntityModel<LivingEntityRenderState> {


    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath(
                            FirstBlocks.MODID,
                            "whisp"
                    ),
                    "main"
            );

    private final ModelPart bodyCenter;
    private final KeyframeAnimation idleAnimation;

    public WhispModel(ModelPart root) {
        super(root);
        this.bodyCenter = root.getChild("body_center");
        this.idleAnimation = WhispAnimation.idle.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body_center",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -6.0F, -12.0F, -6.0F,
                                12.0F, 12.0F, 12.0F
                        ),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        idleAnimation.apply((long)(state.ageInTicks * 50.0F), 1.0F);
    }
}