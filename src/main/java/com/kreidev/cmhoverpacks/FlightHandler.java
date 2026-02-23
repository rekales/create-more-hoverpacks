package com.kreidev.cmhoverpacks;

import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.kreidev.cmhoverpacks.Hoverpacks.resLoc;

public class FlightHandler {

    public static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(resLoc("hoverpack_flight"), 1.0,
            AttributeModifier.Operation.ADD_VALUE);

    public static final Map<UUID, Vec3> PREVIOUS_POSITIONS = new HashMap<>();
    public static final Map<UUID, Double> DECIMAL_DECREMENTS = new HashMap<>();

    public static final float AIR_PER_BLOCK = 1F;  // TODO: config
    public static final float AIR_PER_SECOND = 3F;  // TODO: config

    public static void updateTankAir(Player player, ItemStack hoverpack) {
        UUID id = player.getUUID();
        PREVIOUS_POSITIONS.putIfAbsent(id, player.position());
        DECIMAL_DECREMENTS.putIfAbsent(id, 0.0);

        Vec3 delta = player.position().subtract(PREVIOUS_POSITIONS.get(id));
        delta = delta.y >= 0 ? delta : new Vec3(delta.x, 0, delta.z);  // Don't count down direction for fuel cost

        double remainingDecrement = DECIMAL_DECREMENTS.get(id) + delta.length() * AIR_PER_BLOCK + AIR_PER_SECOND/2;
        int decrement = (int) remainingDecrement;
        HoverpackItem.setRemainingAir(hoverpack, BacktankItem.getRemainingAir(hoverpack) - decrement);
        remainingDecrement -= decrement;

        DECIMAL_DECREMENTS.put(id, remainingDecrement);
        PREVIOUS_POSITIONS.put(id, player.position());
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            if (level.getGameTime() % 10 != 2) return;
            ItemStack hoverpack = player.getItemBySlot(EquipmentSlot.CHEST);

            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof HoverpackItem
                    && BacktankItem.getRemainingAir(hoverpack) > 0) {
                AttributeInstance flightAttr = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                if (flightAttr != null && !flightAttr.hasModifier(FLIGHT_MODIFIER.id())) {
                    flightAttr.addTransientModifier(FLIGHT_MODIFIER);
                }

                if (player.getAbilities().flying) {
                    updateTankAir(player, hoverpack);
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
