package uk.gemwire.portaldoors.door;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PortalDoorBlock extends DoorBlock {

    public PortalDoorBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PortalDoorTileEntity();
    }
}
