package uk.gemwire.portaldoors.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.gemwire.portaldoors.PortalDoors;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = PortalDoors.ID, value = Dist.CLIENT, bus = Bus.MOD)
public class RenderEvents {

    @SubscribeEvent
    public static void registerRender(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(PortalDoors.PORTAL_TE.get(), PortalDoorRenderer::new);
    }
}
