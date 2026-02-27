package com.kreidev.cmhoverpacks;

import com.simibubi.create.*;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@Mod(Hoverpacks.MOD_ID)
public class Hoverpacks {
    public static final String MOD_ID = "cmhoverpacks";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MOD_ID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public static final ItemEntry<HoverpackItem> HOVERPACK_ITEM = REGISTRATE
            .item("hoverpack", (p) ->
                    new HoverpackItem(AllArmorMaterials.COPPER, p, Create.asResource("copper_diving"), Hoverpacks.HOVERPACK_PLACEABLE_ITEM))
            .model(AssetLookup.customGenericItemModel("_", "item"))
            .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
            .tag(ItemTags.CHEST_ARMOR)
            .register();

    public static final BlockEntry<HoverpackBlock> HOVERPACK_BLOCK = REGISTRATE
            .block("hoverpack", HoverpackBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .blockstate((ctx, prov) ->
                    prov.horizontalBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .transform(hoverpack(Hoverpacks.HOVERPACK_ITEM::get))
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

    // From BuilderTransformers.backtank() but no config setting
    @SuppressWarnings("removal")
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> hoverpack(Supplier<ItemLike> drop) {
        return b -> b.blockstate((c, p) -> p.horizontalBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                .transform(pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
//                .transform(CStress.setImpact(4.0))
                .loot((lt, block) -> {
                    LootTable.Builder builder = LootTable.lootTable();
                    LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
                    lt.add(block, builder.withPool(LootPool.lootPool()
                            .when(survivesExplosion)
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(drop.get())
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                            .include(AllDataComponents.BACKTANK_AIR)))));
                });
    }
}
