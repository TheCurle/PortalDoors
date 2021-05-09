package uk.gemwire.portaldoors.key;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import uk.gemwire.portaldoors.PortalDoors;

public class PortalDoorKey extends Item {
    public PortalDoorKey(Properties props) {
        super(props);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState usedState = context.getLevel().getBlockState(context.getClickedPos());
        // If used oak door
        if(usedState.getBlock() == Blocks.OAK_DOOR) {
            // make sure the door is closed
            if(usedState.hasProperty(DoorBlock.OPEN) && usedState.getValue(DoorBlock.OPEN))
                return super.useOn(context);

            // get the half used
            if (usedState.hasProperty(DoorBlock.HALF)) {
                DoubleBlockHalf doubleblockhalf = usedState.getValue(DoorBlock.HALF);
                Direction direction = usedState.getValue(DoorBlock.FACING);
                DoorHingeSide hinge = usedState.getValue(DoorBlock.HINGE);
                // if bottom half, set above
                if (doubleblockhalf == DoubleBlockHalf.LOWER) {
                    if (context.getLevel().getBlockState(context.getClickedPos().above()).getBlock() == Blocks.OAK_DOOR)
                        context.getLevel().setBlock(context.getClickedPos().above(), PortalDoors.PORTAL_BLOCK.get().defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER).setValue(DoorBlock.FACING, direction).setValue(DoorBlock.HINGE, hinge), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    // if upper half, set below
                } else if (doubleblockhalf == DoubleBlockHalf.UPPER) {
                    if (context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() == Blocks.OAK_DOOR)
                        context.getLevel().setBlock(context.getClickedPos().below(), PortalDoors.PORTAL_BLOCK.get().defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.FACING, direction).setValue(DoorBlock.HINGE, hinge), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
                // set target block to its variant. reduces code reuse
                context.getLevel().setBlock(context.getClickedPos(), PortalDoors.PORTAL_BLOCK.get().defaultBlockState().setValue(DoorBlock.HALF, doubleblockhalf).setValue(DoorBlock.FACING, direction).setValue(DoorBlock.HINGE, hinge), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
        }

        return super.useOn(context);

    }
}
