package com.example.firstblocks;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;

public class WhispAltarRenderer
        implements BlockEntityRenderer<WhispAltarBlockEntity, BlockEntityRenderState> {

    public WhispAltarRenderer(
            BlockEntityRendererProvider.Context context
    ) {
    }

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void submit(
            BlockEntityRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState camera
    ) {
        // На следующем шаге добавим сюда бирюзовое пламя.
    }
}