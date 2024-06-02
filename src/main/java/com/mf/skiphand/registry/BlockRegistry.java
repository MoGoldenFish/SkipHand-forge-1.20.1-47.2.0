package com.mf.skiphand.registry;

import com.mf.skiphand.SkipHandMain;
import com.mf.skiphand.block.ChitoseFumoBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.GeckoLib;
public final class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            SkipHandMain.MODID);

    public static final RegistryObject<ChitoseFumoBlock> CHITOSE_FUMO = BLOCKS.register("chitose_fumo",
            ChitoseFumoBlock::new);

}
