package machir.fishandfarm.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelStove extends ModelBase
{
	//fields
    ModelRenderer StoveBox;
  
    public ModelStove()
    {
    	textureWidth = 64;
    	textureHeight = 32;
    
    	StoveBox = new ModelRenderer(this, 0, 0);
    	StoveBox.addBox(-7F, 0F, -7F, 14, 10, 14);
    	StoveBox.setRotationPoint(0F, 14F, 0F);
    	StoveBox.setTextureSize(64, 32);
    	StoveBox.mirror = true;
    	setRotation(StoveBox, 0F, 0F, 0F);
    }
    
 	@Override
 	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  	{
 		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		StoveBox.render(f5);
  	}
  
 	private void setRotation(ModelRenderer model, float x, float y, float z)
 	{
 		model.rotateAngleX = x;
 		model.rotateAngleY = y;
 		model.rotateAngleZ = z;
 	}
  
 	@Override
 	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
 	{
 		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
 	}	
}