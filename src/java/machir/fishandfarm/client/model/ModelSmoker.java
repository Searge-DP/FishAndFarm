package machir.fishandfarm.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSmoker extends ModelBase
{
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
  
    public ModelSmoker()
    {
        textureWidth = 128;
        textureHeight = 64;
    
        float x = -8.0F;
        float z = -8.0F;
        
        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 22, 16);
        Shape1.setRotationPoint(-8F + x, 2F, -8F + z);
        Shape1.setTextureSize(128, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 64, 0);
        Shape2.addBox(0F, 0F, 0F, 14, 4, 14);
        Shape2.setRotationPoint(-7F + x, -2F, -7F + z);
        Shape2.setTextureSize(128, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 64, 18);
        Shape3.addBox(0F, 0F, 0F, 8, 3, 8);
        Shape3.setRotationPoint(-4F + x, -5F, -4F + z);
        Shape3.setTextureSize(128, 64);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
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