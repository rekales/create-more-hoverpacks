package com.kreidev.cmhoverpacks;

import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.foundation.particle.AirParticleData;
import net.minecraft.util.Mth;
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

            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BacktankItem) {
                Hoverpacks.LOGGER.debug(BacktankItem.getRemainingAir(player.getItemBySlot(EquipmentSlot.CHEST))+"");
            }

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
        } else {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BacktankItem && player.getAbilities().flying) {
                if (level.getGameTime()%2 == 0) {
                    for (int i = 0; i < 2; i++) {  // i == 0 : right
                        double xRand = (player.getRandom().nextDouble() - 0.5);
                        double zRand = (player.getRandom().nextDouble() - 0.5);

                        Vec3 spawnOffset = new Vec3(
                                xRand * 0.65,
                                0,
                                zRand  * 0.65
                        );
                        spawnOffset = spawnOffset.xRot(Mth.DEG_TO_RAD * 45);
                        if (i == 0)
                            spawnOffset = spawnOffset.add(0.8,1.1,0.5);
                        else {
                            spawnOffset = spawnOffset.add(-0.8,1.1,0.5);
                        }
                        spawnOffset = spawnOffset.yRot( Mth.DEG_TO_RAD * (180-player.yBodyRot));

                        Vec3 deltaVec = new Vec3(
                                xRand * 1.5,
                                -4,
                                zRand * 1.5
                        );
                        deltaVec = deltaVec.xRot(Mth.DEG_TO_RAD * 45);
                        if (i == 0)
                            deltaVec = deltaVec.yRot( Mth.DEG_TO_RAD * (170-player.yBodyRot));
                        else {
                            deltaVec = deltaVec.yRot( Mth.DEG_TO_RAD * (190-player.yBodyRot));
                        }

                        level.addParticle(new AirParticleData(1f, 0.3F),
                                player.getX() + spawnOffset.x, player.getY() + spawnOffset.y, player.getZ() + spawnOffset.z,
                                deltaVec.x, deltaVec.y, deltaVec.z
                        );
                    }
                }
            }
        }
    }
}
