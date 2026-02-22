package com.kreidev.cmhoverpacks.mixin;

import com.kreidev.cmhoverpacks.FlightHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        Player self = (Player)(Object)this;

        AttributeInstance flightAttr = self.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (flightAttr != null
                && flightAttr.hasModifier(FlightHandler.FLIGHT_MODIFIER.id())
                && self.getAbilities().flying) {
            return;
        }

        super.setSprinting(sprinting);
    }

    @Inject(method = "getFlyingSpeed", at = @At("HEAD"), cancellable = true)
    public void getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        Player self = (Player)(Object)this;
        AttributeInstance flightAttr = self.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (flightAttr != null && flightAttr.hasModifier(FlightHandler.FLIGHT_MODIFIER.id())) {
            cir.setReturnValue(self.getAbilities().getFlyingSpeed() * 0.75f);
        }
    }
}
