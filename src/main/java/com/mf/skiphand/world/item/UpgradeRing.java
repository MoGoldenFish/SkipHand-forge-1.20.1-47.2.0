package com.mf.skiphand.world.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

import com.mf.skiphand.world.item.generic.ItemBaseCurio;
public class UpgradeRing extends ItemBaseCurio{
    public UpgradeRing() {
        super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
    }
    @Override
    public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return DropRule.ALWAYS_KEEP;
    }

}
