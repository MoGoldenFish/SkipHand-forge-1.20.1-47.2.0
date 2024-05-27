package com.mf.skiphand.mixin;
import com.mf.skiphand.client.gui.HudRenderHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class UpdateUIMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateCooldownBar(CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null) {
            HudRenderHelper.getCurioCooldown(mc.player);
        }
    }
}

