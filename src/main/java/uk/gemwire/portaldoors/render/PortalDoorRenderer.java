package uk.gemwire.portaldoors.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import uk.gemwire.portaldoors.door.PortalDoorTileEntity;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class PortalDoorRenderer extends TileEntityRenderer<PortalDoorTileEntity> {

    public static boolean RENDERING_PORTAL = false;
    public static Framebuffer PORTAL_FRAMEBUFFER = null;

    public PortalDoorRenderer(TileEntityRendererDispatcher renderer) {
        super(renderer);
    }

    @Override
    public void render(PortalDoorTileEntity te, float partialtick, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int light, int somethingelse) {
        if(RENDERING_PORTAL) return;
        if(te.getBlockState().getValue(DoorBlock.OPEN)) return;
        if(te.getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) return;

        Minecraft mc = Minecraft.getInstance();
        Framebuffer fb = mc.getMainRenderTarget();
        // Ensure the framebuffer is proper
        if(PORTAL_FRAMEBUFFER == null) {
            Framebuffer portalfb = new Framebuffer(fb.width, fb.height, true, Minecraft.ON_OSX);
            PORTAL_FRAMEBUFFER = portalfb;
        }
        if (PORTAL_FRAMEBUFFER.height != fb.height && PORTAL_FRAMEBUFFER.width != fb.width)
            PORTAL_FRAMEBUFFER.resize(fb.width, fb.height, Minecraft.ON_OSX);
        stack.pushPose();

        // Set new framebuffer
        try {
            ObfuscationReflectionHelper.findField(Minecraft.class, "field_147124"+"_at").set(mc, PORTAL_FRAMEBUFFER);
        } catch (IllegalAccessException ignored) {}
        PORTAL_FRAMEBUFFER.bindWrite(true);

        GlStateManager._clearColor(1, 0, 1, 1);
        GlStateManager._clearDepth(1);
        GlStateManager._clear(
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT,
                Minecraft.ON_OSX
        );
        GL11.glDisable(GL11.GL_STENCIL_TEST);

        // Render

        Entity camera = mc.cameraEntity;
        ClientWorld world = mc.level;
        GameRenderer gameRenderer = mc.gameRenderer;
        ActiveRenderInfo render_meta = gameRenderer.getMainCamera();

        Vector3d eyePos = camera.getEyePosition(0f);

        camera.setPosRaw(te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ());

        //camera.setPosRaw()
        WorldRenderer renderer = mc.levelRenderer;

        NetworkPlayerInfo player = mc.getConnection().getPlayerInfo(Minecraft.getInstance().player.getGameProfile().getId());
        GameType gameMode = player.getGameMode();
        player.setGameMode(GameType.SPECTATOR);

        boolean clipping = camera.noPhysics;
        camera.noPhysics = true;

        boolean renderHand = true;

        try {
            // renderHand = gameRenderer.renderHand
            renderHand = (boolean) ObfuscationReflectionHelper.findField(GameRenderer.class, "field_175074"+"_C").get(gameRenderer);
            // gameRenderer.renderHand = false
            ObfuscationReflectionHelper.findField(GameRenderer.class, "field_175074"+"_C").set(gameRenderer, false);
        } catch (IllegalAccessException ignored) {}

        GlStateManager._matrixMode(GL11.GL_MODELVIEW);
        GlStateManager._pushMatrix();

        // render stuff
        RENDERING_PORTAL = true;
        try {
            mc.getProfiler().push("render_portal_door");
            gameRenderer.renderLevel(partialtick, Util.getNanos(), new MatrixStack());
            mc.getProfiler().pop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        RENDERING_PORTAL = false;

        try {
            // gameRenderer.renderHand = oldRenderHand
            ObfuscationReflectionHelper.findField(GameRenderer.class, "field_175074"+"_C").set(gameRenderer, renderHand);
        } catch (Throwable ignored) {}

        camera.noPhysics = clipping;
        player.setGameMode(gameMode);
        camera.setPosRaw(eyePos.x, eyePos.y, eyePos.z);

        // Reinstate prior framebuffer
        try {
            ObfuscationReflectionHelper.findField(Minecraft.class, "field_147124"+"_at").set(mc, fb);
        } catch (IllegalAccessException ignored) {}
        PORTAL_FRAMEBUFFER.unbindWrite();
        fb.bindWrite(true);
        stack.popPose();

        // Draw the rendered framebuffer into the world
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(stack.last().pose());

        GlStateManager._enableTexture();
        GlStateManager._activeTexture(GL13.GL_TEXTURE0);

        PORTAL_FRAMEBUFFER.bindRead();
        GlStateManager._texParameter(3553, 10241, 9729);
        GlStateManager._texParameter(3553, 10240, 9729);
        GlStateManager._texParameter(3553, 10242, 10496);
        GlStateManager._texParameter(3553, 10243, 10496);

        Vector3d portalplayeroffset = eyePos.subtract(new Vector3d(
                te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ()
        ));

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuilder();

        buffer.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);

        // Build the quad in worldspace
        // 0.5 -1.5
        // -0.5 -1.5
        // 0.5 0.5
        // -0.5 0.5
        // https://github.com/qouteall/ImmersivePortalsModForForge/blob/11f4665ac1a56a01d5049aedcccdb45bd9abc7e8/src/main/java/com/qouteall/immersive_portals/render/ViewAreaRenderer.java#L255

        PORTAL_FRAMEBUFFER.unbindRead();

        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.popMatrix();

    }
}
