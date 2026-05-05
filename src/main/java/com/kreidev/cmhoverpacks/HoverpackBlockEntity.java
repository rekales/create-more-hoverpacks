package com.kreidev.cmhoverpacks;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HoverpackBlockEntity extends BacktankBlockEntity{

    public HoverpackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

}
