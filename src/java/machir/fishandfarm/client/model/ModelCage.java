package machir.fishandfarm.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCage extends ModelBase
{
    //fields
    ModelRenderer top;
    ModelRenderer middleBox;
    ModelRenderer bottom;
  
    public ModelCage()
    {
        textureWidth = 128;
        textureHeight = 64;
    
        float xOffset = -8.0F;
        float zOffset = -8.0F;
        
        top = new ModelRenderer(this, 0, 0);
        top.addBox(-8F, 20F, -8F, 16, 4, 16);
        top.setRotationPoint(0F + xOffset, 0F, 0F + zOffset);
        top.setTextureSize(128, 64);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        middleBox = new ModelRenderer(this, 0, 20);
        middleBox.addBox(-7F, 4F, -7F, 14, 9, 14);
        middleBox.setRotationPoint(0F + xOffset, 8F, 0F + zOffset);
        middleBox.setTextureSize(128, 64);
        middleBox.mirror = true;
        setRotation(middleBox, 0F, 0F, 0F);
        bottom = new ModelRenderer(this, 0, 0);
        bottom.addBox(-8F, 0F, -8F, 16, 4, 16);
        bottom.setRotationPoint(0F + xOffset, 8F, 0F + zOffset);
        bottom.setTextureSize(128, 64);
        bottom.mirror = true;
        setRotation(bottom, 0F, 0F, 0F);
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        top.render(f5);
        middleBox.render(f5);
        bottom.render(f5);
    }
  
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
  
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
