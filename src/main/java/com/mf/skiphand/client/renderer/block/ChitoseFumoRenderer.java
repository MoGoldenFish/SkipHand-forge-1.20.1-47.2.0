package com.mf.skiphand.client.renderer.block;

import com.mf.skiphand.block.entity.ChitoseFumoBlockEntity;
import com.mf.skiphand.client.model.block.ChitoseFumoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.example.client.model.block.GeckoHabitatModel;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import com.mojang.blaze3d.vertex.*;
public class ChitoseFumoRenderer extends GeoBlockRenderer<ChitoseFumoBlockEntity> {
    public ChitoseFumoRenderer() {
        super(new ChitoseFumoModel());
        // 设置方块的显示大小
        this.withScale(0.5f, 0.5f); // 例如，设置为一半大小

    }
    @Override
    public void preRender(PoseStack poseStack, ChitoseFumoBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        // 调整渲染位置
        poseStack.translate(0.5, 0, 0.5);
    }

}
