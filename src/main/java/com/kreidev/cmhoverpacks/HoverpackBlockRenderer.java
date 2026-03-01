package com.kreidev.cmhoverpacks;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HoverpackBlockRenderer extends GeoBlockRenderer<HoverpackBlockEntity> {
    public HoverpackBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new HoverpackBlockModel());
    }
}
