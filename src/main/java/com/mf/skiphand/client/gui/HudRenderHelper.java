package com.mf.skiphand.client.gui;

import com.mf.skiphand.SkipHandMain;
import com.mf.skiphand.world.item.SkipHand;
import com.mf.skiphand.config.Location;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jline.utils.Log;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import static com.mf.skiphand.config.Config.*;


@Mod.EventBusSubscriber(modid = SkipHandMain.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class HudRenderHelper {

    private static final ResourceLocation HEAL_UPGRADE = new ResourceLocation(SkipHandMain.MODID, "textures/gui/heal_ring_gui.png");
    private static final ResourceLocation PUSH_UPGRADE = new ResourceLocation(SkipHandMain.MODID, "textures/gui/push_upgrade_gui.png");
    private static final ResourceLocation JUMP_UPGRADE = new ResourceLocation(SkipHandMain.MODID, "textures/gui/jump_upgrade_gui.png");
    private static final ResourceLocation FXCK_UPGRADE = new ResourceLocation(SkipHandMain.MODID, "textures/gui/fxck_upgrade_gui.png");

    public static void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight
            , float HealUpgradecooldownPercent,float PushUpgradecooldownPercent
            ,float hasHealUpgrade,float hasPushUpdate,float hasJumpUpdate,float JumpCooldownPercent
            ,int jumptimes,float hasFxckUpdate,float FxckUpgradecooldownPercent) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        float guiSize = (float) mc.getWindow().getGuiScale();

        int xOffset = (int) (hudX.get() / guiSize);
        int yOffset = (int) ((hudY.get()) / guiSize);
        int x = 1;
        int y = 1;

        int stringHeight = (mc.font.lineHeight);
        int iconDim = stringHeight - 1;
        int offsetDim = 2;

        int stringWidth = 2 + iconDim + offsetDim;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (enableMod.get()) {
            Location hudLoc = hudLocation.get();
            switch (hudLoc) {
                case TOP_LEFT -> {
                    x = offsetDim;
                    y = 0;
                }
                case TOP_CENTER -> {
                    x = screenWidth / 2 - offsetDim;
                    y = 0;
                }
                case TOP_RIGHT -> {
                    x = screenWidth / 5;
                    y = 0;
                }
                case BOTTOM_LEFT -> {
                    x = offsetDim;
                    y = screenHeight - iconDim - (2 * offsetDim);
                }
                case BOTTOM_RIGHT -> {
                    x = screenWidth - stringWidth - offsetDim;
                    y = screenHeight - iconDim - (2 * offsetDim);
                }
            }

            // 检查玩家是否拥有指定的饰品

            if (hasHealUpgrade==1) {
                // 玩家拥有指定的饰品，渲染 UI
                if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.renderDebug) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(0.4F, 0.4F, 0.4F);

                    int iconX = x + xOffset;
                    int iconY = y + yOffset + offsetDim;
                    //int cooldownBarWidth = 16;
                    //int cooldownBarHeight = 2;
                    //int cooldownBarX = cooldownBarWidth/2;
                    //int cooldownBarY = iconY+(iconDim/2);

                    //guiGraphics.fill(cooldownBarX, cooldownBarY, cooldownBarX + cooldownBarWidth, cooldownBarY + cooldownBarHeight, 0xFFFFFFFF);
                    int filledHeight = (int) ((1-HealUpgradecooldownPercent)*64);
                    int cooldownDstY = 64 - filledHeight;
                    guiGraphics.blit(HEAL_UPGRADE, iconX+64, iconY+cooldownDstY, 129, cooldownDstY, 64, 64-cooldownDstY, 256, 64);
                    // 渲染HEAL_UPGRADE的背景部分
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, HEAL_UPGRADE);
                    guiGraphics.blit(HEAL_UPGRADE, iconX, iconY, 0, 0, 128, 64, 256, 64);
                    if(filledHeight==64)
                    {
                        guiGraphics.blit(HEAL_UPGRADE, iconX, iconY, 192, 0, 64, 64, 256, 64);
                    }





                    guiGraphics.pose().popPose();
                }
            }
            if (hasPushUpdate==1) {
                // 玩家拥有指定的饰品，渲染 UI
                if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.renderDebug) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(0.4F, 0.4F, 0.4F);

                    int iconX = x + xOffset;
                    int iconY = y + yOffset + offsetDim+64;
                    int filledHeight = (int) ((1-PushUpgradecooldownPercent)*64);
                    int cooldownDstY = 64 - filledHeight;
                    guiGraphics.blit(PUSH_UPGRADE, iconX+64, iconY+cooldownDstY, 129, cooldownDstY, 64, 64-cooldownDstY, 256, 64);
                    // 渲染HEAL_UPGRADE的背景部分
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, PUSH_UPGRADE);
                    guiGraphics.blit(PUSH_UPGRADE, iconX, iconY, 0, 0, 128, 64, 256, 64);
                    if(filledHeight==64)
                    {
                        guiGraphics.blit(PUSH_UPGRADE, iconX, iconY, 192, 0, 64, 64, 256, 64);
                    }
                    guiGraphics.pose().popPose();
                }
            }
            if (hasJumpUpdate>0) {
                // 玩家拥有指定的饰品，渲染 UI
                if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.renderDebug) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(0.4F, 0.4F, 0.4F);

                    int iconX = x + xOffset;
                    int iconY = y + yOffset + offsetDim+128;
                    if(jumptimes==0)
                    {
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 64, 64, 64, 64, 256, 64);
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 128, 64, 64, 64, 256, 64);
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 192, 64, 64, 64, 256, 64);
                    } else if (jumptimes==1) {
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 64, 64, 64, 64, 256, 64);
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 128, 64, 64, 64, 256, 64);
                    } else if (jumptimes==2) {
                        guiGraphics.blit(JUMP_UPGRADE, iconX, iconY, 64, 64, 64, 64, 256, 64);
                    }
                    int cooldownBarWidth = 64;
                    int cooldownBarHeight = 3;
                    int cooldownBarX = 0+ xOffset;
                    int cooldownBarY = iconY+66;
                    if(jumptimes!=0) {
                        guiGraphics.fill(cooldownBarX, cooldownBarY, cooldownBarX + cooldownBarWidth, cooldownBarY + cooldownBarHeight, 0xFFFFFFFF);

                        // 使用传入的冷却百分比进行渲染
                        int filledWidth = (int) (JumpCooldownPercent * cooldownBarWidth);
                        guiGraphics.fill(cooldownBarX, cooldownBarY, cooldownBarX + filledWidth, cooldownBarY + cooldownBarHeight, 0xFF000000);
                    }
                    // 渲染HEAL_UPGRADE的背景部分
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, JUMP_UPGRADE);

                    guiGraphics.pose().popPose();
                }
            }
            if (hasFxckUpdate==1) {
                // 玩家拥有指定的饰品，渲染 UI
                if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.renderDebug) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(0.4F, 0.4F, 0.4F);

                    int iconX = x + xOffset;
                    int iconY = y + yOffset + offsetDim+192;
                    int filledHeight = (int) ((1-FxckUpgradecooldownPercent)*64);
                    int cooldownDstY = 64 - filledHeight;
                    guiGraphics.blit(FXCK_UPGRADE, iconX, iconY+cooldownDstY, 64, cooldownDstY, 64, 64-cooldownDstY, 256, 64);
                    // 渲染HEAL_UPGRADE的背景部分
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, FXCK_UPGRADE);
                    guiGraphics.blit(FXCK_UPGRADE, iconX, iconY, 0, 0, 64, 64, 256, 64);

                    guiGraphics.pose().popPose();
                }
            }


        }
    }


    public static float getCurioCooldown(Player player,String itemid) {
        Item curio = getCurioById(player, itemid);

        return player.getCooldowns().getCooldownPercent(curio, 0.0f);
    }
    public static float hasCurio(final LivingEntity entity, final Item curio) {
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(entity);

        if (optional.isPresent()) {
            ICuriosItemHandler itemHandler = optional.orElseThrow(IllegalStateException::new);

            // Iterate through all curio slots
            for (String identifier : itemHandler.getCurios().keySet()) {
                IDynamicStackHandler stackHandler = itemHandler.getCurios().get(identifier).getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty() && stack.getItem() == curio) {
                        return 1f;
                    }
                }
            }
        }

        return 0f;
    }
    public static Item getCurioById(final LivingEntity entity, final String itemId) {
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(entity);

        if (optional.isPresent()) {
            ICuriosItemHandler itemHandler = optional.orElseThrow(IllegalStateException::new);

            // Iterate through all curio slots
            for (String identifier : itemHandler.getCurios().keySet()) {
                IDynamicStackHandler stackHandler = itemHandler.getCurios().get(identifier).getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty() && stack.getItem().toString().equals(itemId)) {
                        return stack.getItem();
                    }
                }
            }
        }

        return null;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
        int screenWidth = event.getGuiGraphics().guiWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();
        //Log.info(screenWidth);
        int jumptimes=SkipHand.getJumptimes();
        float cooldownPercent=SkipHand.getCooldown()/50f;
        //Log.info(jumptimes);
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        float hasHealUpgrade = hasCurio(player, SkipHandMain.ITEM_Skiphand_HealUpgrade.get());
        float hasPushUpdate = hasCurio(player, SkipHandMain.ITEM_Skiphand_PushUpgrade.get());
        float hasJumpUpdate = hasCurio(player, SkipHandMain.ITEM_Skiphand_JumpUpgrade.get())
                                + hasCurio(player, SkipHandMain.ITEM_Skiphand_JumpUltimateUpgrade.get());
        float hasFxckUpdate = hasCurio(player, SkipHandMain.ITEM_Skiphand_FxckUpgrade.get());
        float HealUpgradecooldownPercent = getCurioCooldown(player,"item_skiphand_healupgrade"); // 获取冷却百分比
        float PushUpgradecooldownPercent = getCurioCooldown(player,"item_skiphand_pushupgrade"); // 获取冷却百分比
        float FxckUpgradecooldownPercent = getCurioCooldown(player,"item_skiphand_fxckupgrade"); // 获取冷却百分比
        render(event.getGuiGraphics(), screenWidth, screenHeight, HealUpgradecooldownPercent,PushUpgradecooldownPercent
                ,hasHealUpgrade,hasPushUpdate,hasJumpUpdate,cooldownPercent,jumptimes,hasFxckUpdate,FxckUpgradecooldownPercent); // 传递冷却百分比给渲染方法
    }


}
