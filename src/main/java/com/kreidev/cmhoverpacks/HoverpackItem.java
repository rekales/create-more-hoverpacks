package com.kreidev.cmhoverpacks;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class HoverpackItem extends BacktankItem {

    public HoverpackItem(Holder<ArmorMaterial> material, Properties properties, ResourceLocation textureLoc, Supplier<BacktankBlockItem> placeable) {
        super(material, properties, textureLoc, placeable);
    }

    public static void setRemainingAir(ItemStack stack, int air) {
        stack.set(AllDataComponents.BACKTANK_AIR, Math.max(air, 0));
    }
}
