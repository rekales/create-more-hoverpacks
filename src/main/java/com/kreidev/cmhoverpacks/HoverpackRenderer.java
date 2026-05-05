package com.kreidev.cmhoverpacks;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class HoverpackRenderer extends KineticBlockEntityRenderer<BacktankBlockEntity> {

    public HoverpackRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public static void init() {}
}
