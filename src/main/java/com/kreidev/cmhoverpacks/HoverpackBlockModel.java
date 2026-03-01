package com.kreidev.cmhoverpacks;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.kreidev.cmhoverpacks.Hoverpacks.resLoc;

public class HoverpackBlockModel extends GeoModel<HoverpackBlockEntity> {

    @Override
    public ResourceLocation getModelResource(HoverpackBlockEntity animatable) {
        return resLoc("geo/hoverpack.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(HoverpackBlockEntity animatable) {
        return resLoc("animations/hoverpack.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(HoverpackBlockEntity animatable) {
        return resLoc("textures/copper_hoverpack.png");
    }
}
