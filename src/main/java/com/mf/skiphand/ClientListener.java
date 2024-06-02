package com.mf.skiphand;
import com.mf.skiphand.client.renderer.block.ChitoseFumoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import com.mf.skiphand.registry.BlockEntityRegistry;
import com.mf.skiphand.registry.BlockRegistry;
@Mod.EventBusSubscriber(modid = SkipHandMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientListener {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BlockEntityRegistry.CHITOSE_FUMO.get(), context -> new ChitoseFumoRenderer());
    }

    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.CHITOSE_FUMO.get(), RenderType.translucent());
    }
}