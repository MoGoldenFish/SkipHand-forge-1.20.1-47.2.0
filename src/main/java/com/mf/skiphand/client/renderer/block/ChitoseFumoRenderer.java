package com.mf.skiphand.client.renderer.block;

import com.mf.skiphand.block.entity.ChitoseFumoBlockEntity;
import com.mf.skiphand.client.model.block.ChitoseFumoModel;
import software.bernie.example.client.model.block.GeckoHabitatModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
public class ChitoseFumoRenderer extends GeoBlockRenderer<ChitoseFumoBlockEntity> {
    public ChitoseFumoRenderer() {
        super(new ChitoseFumoModel());
    }
}
