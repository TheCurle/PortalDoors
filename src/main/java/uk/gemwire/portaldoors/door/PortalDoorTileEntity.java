package uk.gemwire.portaldoors.door;

import net.minecraft.tileentity.TileEntity;
import uk.gemwire.portaldoors.PortalDoors;

public class PortalDoorTileEntity extends TileEntity {
    public PortalDoorTileEntity() {
        super(PortalDoors.PORTAL_TE.get());
    }
}
