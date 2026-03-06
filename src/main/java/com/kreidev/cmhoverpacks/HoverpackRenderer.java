package com.kreidev.cmhoverpacks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.kreidev.cmhoverpacks.Hoverpacks.resLoc;

public class HoverpackRenderer extends KineticBlockEntityRenderer<BacktankBlockEntity> {

    public static final PartialModel LEFT_BRACE = PartialModel.of(resLoc("block/hoverpack/left_brace"));
    public static final PartialModel LEFT_RING = PartialModel.of(resLoc("block/hoverpack/left_ring"));
    public static final PartialModel LEFT_FAN = PartialModel.of(resLoc("block/hoverpack/left_fan"));
    public static final PartialModel RIGHT_BRACE = PartialModel.of(resLoc("block/hoverpack/right_brace"));
    public static final PartialModel RIGHT_RING = PartialModel.of(resLoc("block/hoverpack/right_ring"));
    public static final PartialModel RIGHT_FAN = PartialModel.of(resLoc("block/hoverpack/right_fan"));

    public static final Vec3 LEFT_RING_PIVOT = new Vec3(0.75/16f, 0, 4.25/16f);
    public static final Vec3 RIGHT_RING_PIVOT = new Vec3(15.25/16f, 0, 4.25/16f);


    public HoverpackRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(BacktankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        BlockState state = getRenderedBlockState(be);
        RenderType type = getRenderType(be, state);
        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(type), light);

        BlockState blockState = be.getBlockState();

        SuperByteBuffer cogs = CachedBuffers.partial(getCogsModel(blockState), blockState);
        cogs.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter()
                .translate(0, 6.5f / 16, 11f / 16)
                .rotate(AngleHelper.rad(be.getSpeed() / 4f * AnimationTickHolder.getRenderTime(be.getLevel()) % 360),
                        Direction.EAST)
                .translate(0, -6.5f / 16, -11f / 16);
        cogs.light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        SuperByteBuffer leftBrace = CachedBuffers.partial(LEFT_BRACE, blockState);
        leftBrace.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter();
        leftBrace.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));

        SuperByteBuffer leftRing = CachedBuffers.partial(LEFT_RING, blockState);
        leftRing.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter()
                .translate(LEFT_RING_PIVOT)
                .rotateYDegrees(90)
                .translate(LEFT_RING_PIVOT.reverse());
        leftRing.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));

        SuperByteBuffer leftFan = CachedBuffers.partial(LEFT_FAN, blockState);
        leftFan.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter()
                .translate(LEFT_RING_PIVOT)
                .rotateYDegrees(90)
                .translate(LEFT_RING_PIVOT.reverse());
        leftFan.light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));

        SuperByteBuffer rightBrace = CachedBuffers.partial(RIGHT_BRACE, blockState);
        rightBrace.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter();
        rightBrace.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));

        SuperByteBuffer rightRing = CachedBuffers.partial(RIGHT_RING, blockState);
        rightRing.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter()
                .translate(RIGHT_RING_PIVOT)
                .rotateYDegrees(-90)
                .translate(RIGHT_RING_PIVOT.reverse());
        rightRing.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));

        SuperByteBuffer rightFan = CachedBuffers.partial(RIGHT_FAN, blockState);
        rightFan.center()
                .rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
                .uncenter()
                .translate(RIGHT_RING_PIVOT)
                .rotateYDegrees(-90)
                .translate(RIGHT_RING_PIVOT.reverse());
        rightFan.light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));

    }

    @Override
    protected SuperByteBuffer getRotatedModel(BacktankBlockEntity be, BlockState state) {
        return CachedBuffers.partial(getShaftModel(state), state);
    }

    public static PartialModel getCogsModel(BlockState state) {
        if (AllBlocks.NETHERITE_BACKTANK.has(state)) {
            return AllPartialModels.NETHERITE_BACKTANK_COGS;
        }
        return AllPartialModels.COPPER_BACKTANK_COGS;
    }

    public static PartialModel getShaftModel(BlockState state) {
        if (AllBlocks.NETHERITE_BACKTANK.has(state)) {
            return AllPartialModels.NETHERITE_BACKTANK_SHAFT;
        }
        return AllPartialModels.COPPER_BACKTANK_SHAFT;
    }

    public static void init() {}
}
