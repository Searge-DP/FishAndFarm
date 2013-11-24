package machir.fishandfarm.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSmoker extends ModelBase
{
    //fields
    ModelRenderer Ironbandbacklower;
    ModelRenderer Ironbandleftlower;
    ModelRenderer Ironbandfrontlower;
    ModelRenderer Ironbandrightlower;
    ModelRenderer Ironbandbackupper;
    ModelRenderer Ironbandfrontupper;
    ModelRenderer Ironbandleftupper;
    ModelRenderer Ironbandrightupper;
    ModelRenderer Frontupper;
    ModelRenderer Frontlower;
    ModelRenderer Back;
    ModelRenderer Rightside;
    ModelRenderer Top;
    ModelRenderer Leftside;
    ModelRenderer Bottom;
    ModelRenderer Shelftop;
    ModelRenderer Shelfmiddle;
    ModelRenderer Shelfbottom;
  
    public ModelSmoker()
    {
        textureWidth = 128;
        textureHeight = 64;
        
        float xOffset = -8.0F;
        float zOffset = -8.0F;
    
        Ironbandbacklower = new ModelRenderer(this, 56, 0);
        Ironbandbacklower.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandbacklower.setRotationPoint(0F + xOffset, 18F, 0F + zOffset);
        Ironbandbacklower.setTextureSize(128, 64);
        Ironbandbacklower.mirror = true;
        setRotation(Ironbandbacklower, 0F, 0F, 0F);
        Ironbandleftlower = new ModelRenderer(this, 56, 0);
        Ironbandleftlower.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandleftlower.setRotationPoint(0F + xOffset, 18F, 0F + zOffset);
        Ironbandleftlower.setTextureSize(128, 64);
        Ironbandleftlower.mirror = true;
        setRotation(Ironbandleftlower, 0F, 1.570796F, 0F);
        Ironbandfrontlower = new ModelRenderer(this, 56, 0);
        Ironbandfrontlower.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandfrontlower.setRotationPoint(0F + xOffset, 18F, 0F + zOffset);
        Ironbandfrontlower.setTextureSize(128, 64);
        Ironbandfrontlower.mirror = true;
        setRotation(Ironbandfrontlower, 0F, 3.141593F, 0F);
        Ironbandrightlower = new ModelRenderer(this, 56, 0);
        Ironbandrightlower.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandrightlower.setRotationPoint(0F + xOffset, 18F, 0F + zOffset);
        Ironbandrightlower.setTextureSize(128, 64);
        Ironbandrightlower.mirror = true;
        setRotation(Ironbandrightlower, 0F, -1.570796F, 0F);
        Ironbandbackupper = new ModelRenderer(this, 56, 0);
        Ironbandbackupper.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandbackupper.setRotationPoint(0F + xOffset, 4F, 0F + zOffset);
        Ironbandbackupper.setTextureSize(128, 64);
        Ironbandbackupper.mirror = true;
        setRotation(Ironbandbackupper, 0F, 0F, 0F);
        Ironbandfrontupper = new ModelRenderer(this, 56, 0);
        Ironbandfrontupper.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandfrontupper.setRotationPoint(0F + xOffset, 4F, 0F + zOffset);
        Ironbandfrontupper.setTextureSize(128, 64);
        Ironbandfrontupper.mirror = true;
        setRotation(Ironbandfrontupper, 0F, 3.141593F, 0F);
        Ironbandleftupper = new ModelRenderer(this, 56, 0);
        Ironbandleftupper.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandleftupper.setRotationPoint(0F + xOffset, 4F, 0F + zOffset);
        Ironbandleftupper.setTextureSize(128, 64);
        Ironbandleftupper.mirror = true;
        setRotation(Ironbandleftupper, 0F, 1.570796F, 0F);
        Ironbandrightupper = new ModelRenderer(this, 56, 0);
        Ironbandrightupper.addBox(-7F, 0F, 7F, 14, 2, 1);
        Ironbandrightupper.setRotationPoint(0F + xOffset, 4F, 0F + zOffset);
        Ironbandrightupper.setTextureSize(128, 64);
        Ironbandrightupper.mirror = true;
        setRotation(Ironbandrightupper, 0F, -1.570796F, 0F);
        Frontupper = new ModelRenderer(this, 0, 0);
        Frontupper.addBox(-5F, -7F, -7F, 10, 3, 2);
        Frontupper.setRotationPoint(0F + xOffset, 9F, 0F + zOffset);
        Frontupper.setTextureSize(128, 64);
        Frontupper.mirror = true;
        setRotation(Frontupper, 0F, 0F, 0F);
        Frontlower = new ModelRenderer(this, 0, 17);
        Frontlower.addBox(-5F, -7F, -7F, 10, 3, 2);
        Frontlower.setRotationPoint(0F + xOffset, 26F, 0F + zOffset);
        Frontlower.setTextureSize(128, 64);
        Frontlower.mirror = true;
        setRotation(Frontlower, 0F, 0F, 0F);
        Back = new ModelRenderer(this, 0, 0);
        Back.addBox(-7F, -7F, -7F, 14, 20, 2);
        Back.setRotationPoint(0F + xOffset, 9F, 0F + zOffset);
        Back.setTextureSize(128, 64);
        Back.mirror = true;
        setRotation(Back, 0F, 3.141593F, 0F);
        Rightside = new ModelRenderer(this, 2, 0);
        Rightside.addBox(-5F, -7F, -7F, 12, 20, 2);
        Rightside.setRotationPoint(0F + xOffset, 9F, 0F + zOffset);
        Rightside.setTextureSize(128, 64);
        Rightside.mirror = true;
        setRotation(Rightside, 0F, 1.570796F, 0F);
        Top = new ModelRenderer(this, 0, 28);
        Top.addBox(-6F, -8F, -6F, 12, 2, 12);
        Top.setRotationPoint(0F + xOffset, 8F, 0F + zOffset);
        Top.setTextureSize(128, 64);
        Top.mirror = true;
        setRotation(Top, 0F, 1.570796F, 0F);
        Leftside = new ModelRenderer(this, 2, 0);
        Leftside.addBox(-7F, -7F, -7F, 12, 20, 2);
        Leftside.setRotationPoint(0F + xOffset, 9F, 0F + zOffset);
        Leftside.setTextureSize(128, 64);
        Leftside.mirror = true;
        setRotation(Leftside, 0F, -1.570796F, 0F);
        Bottom = new ModelRenderer(this, 48, 28);
        Bottom.addBox(-6F, -8F, -6F, 12, 2, 12);
        Bottom.setRotationPoint(0F + xOffset, 30F, 0F + zOffset);
        Bottom.setTextureSize(128, 64);
        Bottom.mirror = true;
        setRotation(Bottom, 0F, 1.570796F, 0F);
        Shelftop = new ModelRenderer(this, 48, 42);
        Shelftop.addBox(-6F, -8F, -6F, 12, 1, 12);
        Shelftop.setRotationPoint(0F + xOffset, 17F, 0F + zOffset);
        Shelftop.setTextureSize(128, 64);
        Shelftop.mirror = true;
        setRotation(Shelftop, 0F, 1.570796F, 0F);
        Shelfmiddle = new ModelRenderer(this, 48, 42);
        Shelfmiddle.addBox(-6F, -8F, -6F, 12, 1, 12);
        Shelfmiddle.setRotationPoint(0F + xOffset, 20F, 0F + zOffset);
        Shelfmiddle.setTextureSize(128, 64);
        Shelfmiddle.mirror = true;
        setRotation(Shelfmiddle, 0F, 1.570796F, 0F);
        Shelfbottom = new ModelRenderer(this, 48, 42);
        Shelfbottom.addBox(-6F, -8F, -6F, 12, 1, 12);
        Shelfbottom.setRotationPoint(0F + xOffset, 23F, 0F + zOffset);
        Shelfbottom.setTextureSize(128, 64);
        Shelfbottom.mirror = true;
        setRotation(Shelfbottom, 0F, 1.570796F, 0F);
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Ironbandbacklower.render(f5);
        Ironbandleftlower.render(f5);
        Ironbandfrontlower.render(f5);
        Ironbandrightlower.render(f5);
        Ironbandbackupper.render(f5);
        Ironbandfrontupper.render(f5);
        Ironbandleftupper.render(f5);
        Ironbandrightupper.render(f5);
        Frontupper.render(f5);
        Frontlower.render(f5);
        Back.render(f5);
        Rightside.render(f5);
        Top.render(f5);
        Leftside.render(f5);
        Bottom.render(f5);
        Shelftop.render(f5);
        Shelfmiddle.render(f5);
        Shelfbottom.render(f5);
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