package com.kreidev.cmhoverpacks;

import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HoverpackBlock extends BacktankBlock {
    public HoverpackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends BacktankBlockEntity> getBlockEntityType() {
        return Hoverpacks.HOVERPACK_BLOCK_ENTITY.get();
    }
}
