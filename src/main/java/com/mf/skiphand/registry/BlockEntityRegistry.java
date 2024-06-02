package com.mf.skiphand.registry;
import com.mf.skiphand.SkipHandMain;
import com.mf.skiphand.block.entity.ChitoseFumoBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.GeckoLib;
public final class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkipHandMain.MODID);

    public static final RegistryObject<BlockEntityType<ChitoseFumoBlockEntity>> CHITOSE_FUMO = TILES
            .register("chitose", () -> BlockEntityType.Builder
                    .of(ChitoseFumoBlockEntity::new, BlockRegistry.CHITOSE_FUMO.get()).build(null));

}