package uk.gemwire.portaldoors;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.gemwire.portaldoors.door.PortalDoorBlock;
import uk.gemwire.portaldoors.door.PortalDoorTileEntity;

@Mod(PortalDoors.ID)
public class PortalDoors {
    public static final String ID = "portaldoors";

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);

    public static final RegistryObject<PortalDoorBlock> PORTAL_BLOCK = BLOCKS.register("portal_door", () ->
            new PortalDoorBlock(AbstractBlock.Properties.of(Material.WOOD))
    );

    public static final RegistryObject<TileEntityType<?>> PORTAL_TE = TILES.register("portal", () ->
            TileEntityType.Builder.of(PortalDoorTileEntity::new, PORTAL_BLOCK.get()).build(null)
    );
}
