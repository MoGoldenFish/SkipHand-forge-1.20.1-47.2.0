package com.mf.skiphand.world.item;

import com.mf.skiphand.util.*;
import com.mf.skiphand.SkipHandMain;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mf.skiphand.client.renderer.item.SkipHandRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.item.ShieldItem;
import org.lwjgl.glfw.GLFW;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;


import static com.mf.skiphand.SkipHandMain.MODID;


import java.util.function.Consumer;

public final class SkipHand extends ShieldItem implements GeoItem, Vanishable {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final RawAnimation FUXK_ANIM = RawAnimation.begin().thenPlay("animation.model.usefuxk");
	private static final RawAnimation NORMAL_ANIM = RawAnimation.begin().thenPlay("animation.model.stand");
	private static final RawAnimation SKIP_ANIM = RawAnimation.begin().thenPlay("animation.model.skip");
	private static final RawAnimation SHIELD_ANIM = RawAnimation.begin().thenPlay("animation.model.shield");

	private static final RawAnimation HEAL_ANIM = RawAnimation.begin().thenPlay("animation.model.useheal");
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    public static KeyMapping FUXK_ON;
    public static KeyMapping ALTDOWN;
    private static final Logger LOGGER2 = LogUtils.getLogger();
    public SkipHand(Item.Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.0, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    /*public static void client(FMLClientSetupEvent e) {
        EVENT_BUS.addListener(SkipHand::tick);
    }*/
    @Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private SkipHandRenderer renderer;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if (this.renderer == null)
					this.renderer = new SkipHandRenderer();

				return this.renderer;
			}
		});
	}
    @Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "normal_controller", 20, state -> PlayState.CONTINUE)
				.triggerableAnim("normal_anim", NORMAL_ANIM)
				// We've marked the "box_open" animation as being triggerable from the server
				.setSoundKeyframeHandler(state -> {
					// Use helper method to avoid client-code in common class					
				}));
		controllers.add(new AnimationController<>(this, "fuxk_controller", 1, state -> PlayState.CONTINUE)
				.triggerableAnim("fuxk_anim", FUXK_ANIM)
				// We've marked the "box_open" animation as being triggerable from the server
				.setSoundKeyframeHandler(state -> {
					// Use helper method to avoid client-code in common class					
				}));
		controllers.add(new AnimationController<>(this, "skip_controller", 1, state -> PlayState.CONTINUE)
				.triggerableAnim("skip_anim", SKIP_ANIM)
				// We've marked the "box_open" animation as being triggerable from the server
				.setSoundKeyframeHandler(state -> {
					// Use helper method to avoid client-code in common class					
				}));
        controllers.add(new AnimationController<>(this, "shield_controller", 1, state -> PlayState.STOP)
				.triggerableAnim("shield_anim", SHIELD_ANIM)
				// We've marked the "box_open" animation as being triggerable from the server
				.setSoundKeyframeHandler(state -> {
					// Use helper method to avoid client-code in common class
				}));
        controllers.add(new AnimationController<>(this, "heal_controller", 1, state -> PlayState.CONTINUE)
                .triggerableAnim("heal_anim", HEAL_ANIM)
                // We've marked the "box_open" animation as being triggerable from the server
                .setSoundKeyframeHandler(state -> {
                    // Use helper method to avoid client-code in common class
                }));
	}
    @Override
    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (livingEntity) -> {
            livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    public static void keyBind(RegisterKeyMappingsEvent e) {
        FUXK_ON = new KeyMapping("key." + MODID + ".skiphand_fxck", GLFW.GLFW_MOUSE_BUTTON_MIDDLE  , "key.categories." + MODID);
        ALTDOWN = new KeyMapping("key." + MODID + ".skiphand_heal", GLFW.GLFW_KEY_LEFT_ALT  , "key.categories." + MODID);

        e.register(FUXK_ON);
        e.register(ALTDOWN);
    }

    public static boolean hasCurio(final LivingEntity entity, final Item curio) {
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(entity);

        if (optional.isPresent()) {
            ICuriosItemHandler itemHandler = optional.orElseThrow(IllegalStateException::new);

            // Iterate through all curio slots
            for (String identifier : itemHandler.getCurios().keySet()) {
                IDynamicStackHandler stackHandler = itemHandler.getCurios().get(identifier).getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty() && stack.getItem() == curio) {
                        return true;
                    }
                }
            }
        }

        return false;
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
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;  // 返回一个较长的持续时间
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (level instanceof ServerLevel serverLevel)
        {
        	triggerAnim(playerIn, GeoItem.getOrAssignId(playerIn.getItemInHand(handIn), serverLevel), "normal_controller", "normal_anim");
        	if (!playerIn.isSecondaryUseActive()) {

                playerIn.startUsingItem(handIn);



                //animation
            	RayTrace rayTrace = new RayTrace();
                LivingEntity entityInCrosshair = rayTrace.getEntityInCrosshair(50, 128);
                LOGGER2.info("maybe");
                Entity target = entityInCrosshair;
                if(target!=null&&FUXK_ON.isDown()&&!ALTDOWN.isDown())
            	{
                	if (!level.isClientSide) {
                		triggerAnim(playerIn, GeoItem.getOrAssignId(playerIn.getItemInHand(handIn), serverLevel), "fuxk_controller", "fuxk_anim");
                		teleportFUXK(playerIn);
                		return super.use(serverLevel, playerIn, handIn);
                	}
                	return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            	}
                else if(!FUXK_ON.isDown()&&ALTDOWN.isDown()&&hasCurio(playerIn,SkipHandMain.ITEM_Skiphand_HealUpgrade.get().asItem())
                        &&!playerIn.getCooldowns().isOnCooldown(getCurioById(playerIn,"item_skiphand_healupgrade"))
                        &&(playerIn.getHealth()!=playerIn.getMaxHealth())
                        )
                {
                    LOGGER2.info("heal!!!!!");
                    playerIn.heal(playerIn.getMaxHealth()/2);
                    playerIn.getCooldowns().addCooldown(getCurioById(playerIn,"item_skiphand_healupgrade"), 600);
                    triggerAnim(playerIn, GeoItem.getOrAssignId(playerIn.getItemInHand(handIn), serverLevel), "heal_controller", "heal_anim");
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
                }
                else if (ALTDOWN.isDown() && FUXK_ON.isDown() && hasCurio(playerIn, SkipHandMain.ITEM_Skiphand_PushUpgrade.get().asItem())
                        && !playerIn.getCooldowns().isOnCooldown(getCurioById(playerIn, "item_skiphand_pushupgrade"))
                        )
                {
                    triggerAnim(playerIn, GeoItem.getOrAssignId(playerIn.getItemInHand(handIn), serverLevel), "shield_controller", "shield_anim");
                    playerIn.getCooldowns().addCooldown(getCurioById(playerIn,"item_skiphand_pushupgrade"), 200);
                    doPush(playerIn);
                }
                else {
            		LOGGER2.info("failed");
            		return InteractionResultHolder.pass(stack);
            	}


        }else {
            HitResult result = playerIn.pick(128.0, 1.0F, false);
            if (result.getType() != HitResult.Type.BLOCK) {
                playerIn.displayClientMessage(Component.translatable(this.getDescriptionId() + ".teleport_failed"), true);
                return InteractionResultHolder.fail(stack);
            } else {
                if (!level.isClientSide) {
                    BlockPos startPos = ((BlockHitResult)result).getBlockPos();
                    BlockPos endPos = startPos.relative(((BlockHitResult)result).getDirection());
                    BlockPos posDown = startPos.below();
                    if (!level.isEmptyBlock(posDown) || !level.getBlockState(posDown).blocksMotion()) {
                        for(int tries = 0; tries < 3; ++tries) {
                            BlockPos checkPos = startPos.above(tries + 1);
                            if (level.isEmptyBlock(checkPos)) {
                                endPos = checkPos;
                                break;
                            }
                        }
                    }

                    level.playSound(null, playerIn.xo, playerIn.yo, playerIn.zo, SoundEvents.CHORUS_FRUIT_TELEPORT, playerIn.getSoundSource(), 1.0F, 1.0F);
                    playerIn.teleportTo((double)endPos.getX() + 0.5, endPos.getY(), (double)endPos.getZ() + 0.5);
                    level.playSound(null, endPos, SoundEvents.CHORUS_FRUIT_TELEPORT, playerIn.getSoundSource(), 1.0F, 1.0F);
                    triggerAnim(playerIn, GeoItem.getOrAssignId(playerIn.getItemInHand(handIn), serverLevel), "skip_controller", "skip_anim");
                    teleportAttack(playerIn);
                    playerIn.getCooldowns().addCooldown(this, 10);
                    //playerIn.getServer().getCommands().performPrefixedCommand(playerIn.createCommandSourceStack(), "/say test");
                    //return super.use(serverLevel, playerIn, handIn);
                }

                playerIn.fallDistance = 0.0F;
                playerIn.swing(handIn);
                playerIn.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
        }
    }
        return InteractionResultHolder.fail(stack);
    }

    public static void teleportAttack(LivingEntity attacker) {
        double radius = 3.0;
        int duration = 140 + attacker.getRandom().nextInt(60);
        DamageSource damageSource = attacker.damageSources().mobAttack(attacker);
        if (attacker instanceof Player) {
            radius = 4.0;
            duration = 100;
            damageSource = attacker.damageSources().playerAttack((Player) attacker);
        }

        for (Entity entity : attacker.level().getEntities(attacker, attacker.getBoundingBox().inflate(radius))) {
            if (entity instanceof LivingEntity && entity.hurt(damageSource, 4.0F) && attacker.getRandom().nextInt(3) == 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration));
            }

            double x = entity.getX() - attacker.getX();
            double z = entity.getZ() - attacker.getZ();
            double signX = x / Math.abs(x);
            double signZ = z / Math.abs(z);
            entity.setDeltaMovement((radius * signX * 2.0 - x) * 0.20000000298023224, 0.20000000298023224, (radius * signZ * 2.0 - z) * 0.20000000298023224);
            sendPlayerVelocityPacket(entity);
        }

    }
    public static void teleportFUXK(LivingEntity attacker) {
        double radius = 128.0;
        int duration = 30 + attacker.getRandom().nextInt(60);
        DamageSource damageSource = attacker.damageSources().mobAttack(attacker);
        if (attacker instanceof Player) {
            damageSource = attacker.damageSources().playerAttack((Player) attacker);
        }

        for (Entity entity : attacker.level().getEntities(attacker, attacker.getBoundingBox().inflate(radius))) {
            RayTrace rayTrace = new RayTrace();
            LivingEntity entityInCrosshair = rayTrace.getEntityInCrosshair(50, 128);

            if (entity instanceof LivingEntity && entity.equals(entityInCrosshair) /*&& entity.hurt(damageSource, 1.0F)*/) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration));

                // 计算位移向量
                double x = attacker.getX() - entity.getX(); // 计算攻击者到实体的x方向距离
                double z = attacker.getZ() - entity.getZ(); // 计算攻击者到实体的z方向距离
                double distance = Math.sqrt(x * x + z * z); // 计算攻击者到实体的距离
                double factor = 4.0 / distance; // 计算一个比例因子，用于使实体移动到攻击者的位置前方
                double deltaX = x * factor; // 计算x方向上的位移增量
                double deltaZ = z * factor; // 计算z方向上的位移增量

                // 设置实体的位移向量
                entity.setDeltaMovement(-0.1, 0.4, -0.1);
             // 获取玩家的位置
            	
                Vec3 playerPos = attacker.position();
                // 获取实体的位置
                Vec3 entityPos = entity.position();

                // 计算玩家朝向
                double yaw = Math.toRadians(attacker.getYRot());
                double pitch = Math.toRadians(attacker.getXRot());

                // 计算实体到玩家的方向向量
                double dx = playerPos.x() - entityPos.x();
                double dy = playerPos.y() - entityPos.y();
                double dz = playerPos.z() - entityPos.z();
                //double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                // 计算离玩家面对方向上2格远的位置
                double newX = playerPos.x() - 2.0 * Math.sin(yaw) * Math.cos(pitch);
                double newY = playerPos.y() + 1.0 * Math.sin(pitch);
                double newZ = playerPos.z() + 2.0 * Math.cos(yaw) * Math.cos(pitch);
                // 将实体移动到新位置
                entity.moveTo(newX,newY,newZ);

                // 发送玩家速度数据包
                sendPlayerVelocityPacket(entity);
            }
        }
    }
    public static void doPush(LivingEntity attacker) {
        double radius = 4.0;
        for (Entity entity : attacker.level().getEntities(attacker, attacker.getBoundingBox().inflate(radius))) {
            double x = entity.getX() - attacker.getX();
            double z = entity.getZ() - attacker.getZ();
            double signX = x / Math.abs(x);
            double signZ = z / Math.abs(z);
            entity.setDeltaMovement((radius * signX * 2.0 - x) * 0.20000000298023224, 0.5, (radius * signZ * 2.0 - z) * 0.20000000298023224);
            sendPlayerVelocityPacket(entity);
        }
    }
    public static void sendPlayerVelocityPacket(Entity entity) {
        if (entity instanceof ServerPlayer) {
            ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }
    @Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
    
}
