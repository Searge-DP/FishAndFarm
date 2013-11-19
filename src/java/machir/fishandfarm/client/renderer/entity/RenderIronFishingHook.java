package machir.fishandfarm.client.renderer.entity;

import machir.fishandfarm.entity.EntityIronFishHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderIronFishingHook extends Render {
    private static final ResourceLocation HOOK_TEXTURE = new ResourceLocation("textures/particle/particles.png");

    /**
     * Actually renders the fishing line and hook
     */
    public void doRenderFishHook(EntityIronFishHook entityIronFishHook, double x, double y, double z, float yaw, float partialTickTime)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(entityIronFishHook);
        Tessellator tessellator = Tessellator.instance;
        byte b0 = 1;
        byte b1 = 2;
        float f2 = (float)(b0 * 8 + 0) / 128.0F;
        float f3 = (float)(b0 * 8 + 8) / 128.0F;
        float f4 = (float)(b1 * 8 + 0) / 128.0F;
        float f5 = (float)(b1 * 8 + 8) / 128.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f2, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f3, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f3, (double)f4);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f2, (double)f4);
        tessellator.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        if (entityIronFishHook.angler != null)
        {
            float f9 = entityIronFishHook.angler.getSwingProgress(partialTickTime);
            float f10 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            Vec3 vec3 = entityIronFishHook.worldObj.getWorldVec3Pool().getVecFromPool(-0.5D, 0.03D, 0.8D);
            vec3.rotateAroundX(-(entityIronFishHook.angler.prevRotationPitch + (entityIronFishHook.angler.rotationPitch - entityIronFishHook.angler.prevRotationPitch) * partialTickTime) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-(entityIronFishHook.angler.prevRotationYaw + (entityIronFishHook.angler.rotationYaw - entityIronFishHook.angler.prevRotationYaw) * partialTickTime) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(f10 * 0.5F);
            vec3.rotateAroundX(-f10 * 0.7F);
            double d3 = entityIronFishHook.angler.prevPosX + (entityIronFishHook.angler.posX - entityIronFishHook.angler.prevPosX) * (double)partialTickTime + vec3.xCoord;
            double d4 = entityIronFishHook.angler.prevPosY + (entityIronFishHook.angler.posY - entityIronFishHook.angler.prevPosY) * (double)partialTickTime + vec3.yCoord;
            double d5 = entityIronFishHook.angler.prevPosZ + (entityIronFishHook.angler.posZ - entityIronFishHook.angler.prevPosZ) * (double)partialTickTime + vec3.zCoord;
            double d6 = entityIronFishHook.angler == Minecraft.getMinecraft().thePlayer ? 0.0D : (double)entityIronFishHook.angler.getEyeHeight();

            if (this.renderManager.options.thirdPersonView > 0 || entityIronFishHook.angler != Minecraft.getMinecraft().thePlayer)
            {
                float f11 = (entityIronFishHook.angler.prevRenderYawOffset + (entityIronFishHook.angler.renderYawOffset - entityIronFishHook.angler.prevRenderYawOffset) * partialTickTime) * (float)Math.PI / 180.0F;
                double d7 = (double)MathHelper.sin(f11);
                double d8 = (double)MathHelper.cos(f11);
                d3 = entityIronFishHook.angler.prevPosX + (entityIronFishHook.angler.posX - entityIronFishHook.angler.prevPosX) * (double)partialTickTime - d8 * 0.35D - d7 * 0.85D;
                d4 = entityIronFishHook.angler.prevPosY + d6 + (entityIronFishHook.angler.posY - entityIronFishHook.angler.prevPosY) * (double)partialTickTime - 0.45D;
                d5 = entityIronFishHook.angler.prevPosZ + (entityIronFishHook.angler.posZ - entityIronFishHook.angler.prevPosZ) * (double)partialTickTime - d7 * 0.35D + d8 * 0.85D;
            }

            double d9 = entityIronFishHook.prevPosX + (entityIronFishHook.posX - entityIronFishHook.prevPosX) * (double)partialTickTime;
            double d10 = entityIronFishHook.prevPosY + (entityIronFishHook.posY - entityIronFishHook.prevPosY) * (double)partialTickTime + 0.25D;
            double d11 = entityIronFishHook.prevPosZ + (entityIronFishHook.posZ - entityIronFishHook.prevPosZ) * (double)partialTickTime;
            double d12 = (double)((float)(d3 - d9));
            double d13 = (double)((float)(d4 - d10));
            double d14 = (double)((float)(d5 - d11));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(0);
            byte b2 = 16;

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float)i / (float)b2;
                tessellator.addVertex(x + d12 * (double)f12, y + d13 * (double)(f12 * f12 + f12) * 0.5D + 0.25D, z + d14 * (double)f12);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    /**
     * Returns the resource location
     * 
     * @param entityIronFishHook The fish hook to be rendered
     * @return
     */
    protected ResourceLocation getTexture(EntityIronFishHook entityIronFishHook)
    {
        return HOOK_TEXTURE;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getTexture((EntityIronFishHook)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        this.doRenderFishHook((EntityIronFishHook)entity, x, y, z, yaw, partialTickTime);
    }
}
