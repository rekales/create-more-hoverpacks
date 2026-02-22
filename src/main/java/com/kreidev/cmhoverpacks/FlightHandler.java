package com.kreidev.cmhoverpacks;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.kreidev.cmhoverpacks.Hoverpacks.resLoc;

public class FlightHandler {

    public static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(resLoc("hoverpack_flight"), 1.0,
            AttributeModifier.Operation.ADD_VALUE);

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof HoverpackItem) {
                AttributeInstance flightAttr = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                if (flightAttr != null && !flightAttr.hasModifier(FLIGHT_MODIFIER.id())) {
                    flightAttr.addTransientModifier(FLIGHT_MODIFIER);
                }
            } else {
                AttributeInstance flightAttr = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                if (flightAttr != null && flightAttr.hasModifier(FLIGHT_MODIFIER.id())) {
                    flightAttr.removeModifier(FLIGHT_MODIFIER.id());
                }
            }
        }
    }
}
