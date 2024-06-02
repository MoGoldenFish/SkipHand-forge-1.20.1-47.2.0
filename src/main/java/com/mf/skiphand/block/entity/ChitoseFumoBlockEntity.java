package com.mf.skiphand.block.entity;

import com.mf.skiphand.SkipHandMain;
import com.mf.skiphand.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ChitoseFumoBlockEntity extends BlockEntity implements GeoBlockEntity {
    protected static final RawAnimation DEPLOY = RawAnimation.begin().thenLoop("animation.model.flower");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ChitoseFumoBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CHITOSE_FUMO.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, this::deployAnimController));
    }
    protected <E extends GeoAnimatable> PlayState deployAnimController(final AnimationState<E> state) {
        return state.setAndContinue(DEPLOY);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
