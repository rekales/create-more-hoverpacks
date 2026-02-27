package com.kreidev.cmhoverpacks;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.kreidev.cmhoverpacks.Hoverpacks.resLoc;

public class HoverpackGeoModel extends GeoModel<HoverpackItem> {

    @Override
    public ResourceLocation getModelResource(HoverpackItem animatable) {
        return resLoc("geo/hoverpack.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(HoverpackItem animatable) {
        return resLoc("animations/hoverpack.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(HoverpackItem animatable) {
        return resLoc("textures/copper_hoverpack.png");
    }
}
