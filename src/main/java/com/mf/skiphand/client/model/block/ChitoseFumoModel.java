package com.mf.skiphand.client.model.block;
import com.mf.skiphand.SkipHandMain;
import com.mf.skiphand.block.entity.ChitoseFumoBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;
public class ChitoseFumoModel extends DefaultedBlockGeoModel<ChitoseFumoBlockEntity> {
    public ChitoseFumoModel() {
        super(new ResourceLocation(SkipHandMain.MODID, "chitose_fumo"));
    }

    @Override
    public RenderType getRenderType(ChitoseFumoBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
