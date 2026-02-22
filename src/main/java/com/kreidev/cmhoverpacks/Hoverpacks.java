package com.kreidev.cmhoverpacks;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;

@Mod(Hoverpacks.MOD_ID)
public class Hoverpacks {
    public static final String MOD_ID = "cmhoverpacks";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MOD_ID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public static final BlockEntry<HoverpackBlock> HOVERPACK_BLOCK = REGISTRATE
            .block("hoverpack", HoverpackBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .blockstate((ctx, prov) ->
                    prov.horizontalBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .register();

    public static final BlockEntityEntry<BacktankBlockEntity> HOVERPACK_BLOCK_ENTITY = REGISTRATE
            .blockEntity("hoverpack", BacktankBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::backtank)
            .validBlocks(HOVERPACK_BLOCK)
            .renderer(() -> BacktankRenderer::new)
            .register();

    // I'm not sure why there needs to be a separate item for this.
    public static final ItemEntry<BacktankItem.BacktankBlockItem> HOVERPACK_PLACEABLE_ITEM = REGISTRATE
            .item("hoverpack_placeable", p ->
                    new BacktankItem.BacktankBlockItem(HOVERPACK_BLOCK.get(), Hoverpacks.HOVERPACK_ITEM::get, p))
            .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
            .register();

    public static final ItemEntry<HoverpackItem> HOVERPACK_ITEM = REGISTRATE
            .item("hoverpack", (p) ->
                    new HoverpackItem(AllArmorMaterials.COPPER, p, resLoc("hoverpack"), HOVERPACK_PLACEABLE_ITEM))
            .model(AssetLookup.customGenericItemModel("_", "item"))
            .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
            .tag(ItemTags.CHEST_ARMOR)
            .register();


    public Hoverpacks(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(Hoverpacks::clientInit);
        modEventBus.addListener(CommonConfig::onLoad);
        modEventBus.addListener(CommonConfig::onReload);

        NeoForge.EVENT_BUS.addListener(FlightHandler::onPlayerTick);
    }

    public static void clientInit(final FMLClientSetupEvent event) {

    }


    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
