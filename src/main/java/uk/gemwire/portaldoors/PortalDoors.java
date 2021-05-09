package uk.gemwire.portaldoors;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.gemwire.portaldoors.door.PortalDoorBlock;
import uk.gemwire.portaldoors.door.PortalDoorTileEntity;
import uk.gemwire.portaldoors.key.PortalDoorKey;

@Mod(PortalDoors.ID)
public class PortalDoors {
    public static final String ID = "portaldoors";

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);

    public static final RegistryObject<PortalDoorKey> PORTAL_KEY = ITEMS.register("portal_key", () ->
            new PortalDoorKey(new Item.Properties().tab(ItemGroup.TAB_MISC))
    );

    public static final RegistryObject<PortalDoorBlock> PORTAL_BLOCK = BLOCKS.register("portal_door", () ->
            new PortalDoorBlock(AbstractBlock.Properties.of(Material.WOOD))
    );

    public static final RegistryObject<TileEntityType<PortalDoorTileEntity>> PORTAL_TE = TILES.register("portal", () ->
            TileEntityType.Builder.of(PortalDoorTileEntity::new, PORTAL_BLOCK.get()).build(null)
    );

    public PortalDoors() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        TILES.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
