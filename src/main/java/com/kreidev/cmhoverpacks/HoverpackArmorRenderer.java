package com.kreidev.cmhoverpacks;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HoverpackArmorRenderer extends GeoArmorRenderer<HoverpackItem> {

    public HoverpackArmorRenderer() {
        super(new HoverpackGeoModel());
    }
}
