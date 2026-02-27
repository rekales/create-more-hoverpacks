package com.kreidev.cmhoverpacks.mixin;

import com.kreidev.cmhoverpacks.HoverpackItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.equipment.armor.BacktankArmorLayer;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BacktankArmorLayer.class, remap = false)
public class BacktankArmorLayerMixin {

    @SuppressWarnings("AmbiguousMixinReference")
    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/equipment/armor/BacktankItem;getWornBy(Lnet/minecraft/world/entity/Entity;)Lcom/simibubi/create/content/equipment/armor/BacktankItem;"
            )
    )
    private static BacktankItem render(Entity entity, Operation<BacktankItem> original) {
        BacktankItem item = original.call(entity);
        return item instanceof HoverpackItem ? null : item;
    }

}
