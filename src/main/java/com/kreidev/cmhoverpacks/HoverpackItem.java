package com.kreidev.cmhoverpacks;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class HoverpackItem extends BacktankItem implements GeoItem {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public HoverpackItem(Holder<ArmorMaterial> material, Properties properties, ResourceLocation textureLoc, Supplier<BacktankBlockItem> placeable) {
        super(material, properties, textureLoc, placeable);
    }

    public static void setRemainingAir(ItemStack stack, int air) {
        stack.set(AllDataComponents.BACKTANK_AIR, Math.max(air, 0));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "rings", 0, state -> {
            if (state.getData(DataTickets.ENTITY) instanceof Player player && player.getAbilities().flying) {
                return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("deploy"));
            }
            return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("retract"));
        }));
        controllers.add(new AnimationController<>(this, "fans", 0, state -> {
            if (state.getData(DataTickets.ENTITY) instanceof Player player && player.getAbilities().flying) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("start_fan").thenLoop("fan"));
            }
            return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("end_fan"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new HoverpackArmorRenderer();
                }
                return this.renderer;
            }
        });
        GeoItem.super.createGeoRenderer(consumer);
    }
}
