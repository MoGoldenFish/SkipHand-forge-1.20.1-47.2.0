package com.mf.skiphand.client.renderer.item;

import net.minecraft.resources.ResourceLocation;
import com.mf.skiphand.world.item.SkipHand;
import com.mf.skiphand.SkipHandMain;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SkipHandRenderer extends GeoItemRenderer<SkipHand> {
	public SkipHandRenderer() {
		super(new DefaultedItemGeoModel<>(new ResourceLocation(SkipHandMain.MODID, "skip_hand")));
	}
}
