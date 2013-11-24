package machir.fishandfarm.entity;

import java.util.logging.Level;
import java.util.logging.Logger;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.tileentity.TileEntityCage;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCageBobber extends Entity implements IEntityAdditionalSpawnData{
    private int bobberPosRotationIncrements;
    private double bobberX;
    private double bobberY;
    private double bobberZ;
    private double bobberYaw;
    private double bobberPitch;
    
    public TileEntityCage cage = null;

    public EntityCageBobber(World world)
    {
        super(world);
        this.ignoreFrustumCheck = true;
    }

    @SideOnly(Side.CLIENT)
    public EntityCageBobber(World world, TileEntityCage cage)
    {
        this(world);
        this.setPosition(cage.xCoord + 0.5, cage.yCoord + 1.0, cage.zCoord + 0.5);
        this.ignoreFrustumCheck = true;
        this.cage = cage;
    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the passed in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight 
     * 
     * @param distance The distance the entity is away from the hook
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double renderDistance = this.boundingBox.getAverageEdgeLength() * 4.0D;
        renderDistance *= 64.0D;
        return distance < renderDistance * renderDistance;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.onEntityUpdate();

        if (this.bobberPosRotationIncrements > 0)
        {
            double d0 = this.posX + (this.bobberX - this.posX) / (double)this.bobberPosRotationIncrements;
            double d1 = this.posY + (this.bobberY - this.posY) / (double)this.bobberPosRotationIncrements;
            double d2 = this.posZ + (this.bobberZ - this.posZ) / (double)this.bobberPosRotationIncrements;
            double d3 = MathHelper.wrapAngleTo180_double(this.bobberYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.bobberPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.bobberPitch - (double)this.rotationPitch) / (double)this.bobberPosRotationIncrements);
            --this.bobberPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            if (!this.worldObj.isRemote)
            {
                TileEntityCage tileEntity = (TileEntityCage) worldObj.getBlockTileEntity(cage.xCoord, cage.yCoord, cage.zCoord);
                
                // If the cage does not exist, remove the bobber
                if (tileEntity == null)
                {
                    this.setDead();
                    return;
                }
                
                // While it's in the water, float up
                if (this.isInsideOfMaterial(Material.water)) {
                    this.motionY = 0.05F;
                } else {
                    this.motionY = 0.0F;
                }
            }
        }
        this.posY += motionY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead()
    {
        this.isDead = true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        int[] cageCoords = nbttagcompound.getIntArray("CageCoords");
        if (cageCoords.length == 3) {
            cage = (TileEntityCage) worldObj.getBlockTileEntity(cageCoords[0], cageCoords[1], cageCoords[2]);
            if (cage != null) {
                return;
            }
        } 
        Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "Failed to load cage bobber data");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        if (cage != null) {
            int[] cageCoords = { cage.xCoord, cage.yCoord, cage.zCoord };
            nbttagcompound.setIntArray("CageCoords", cageCoords);
        } else {
            Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "Failed to store cage bobber data");
        }
    }

    @Override
    public void writeSpawnData(ByteArrayDataOutput data) {
        if (this.cage != null) {
            data.writeInt(this.cage.xCoord);
            data.writeInt(this.cage.yCoord);
            data.writeInt(this.cage.zCoord);
        }
    }

    @Override
    public void readSpawnData(ByteArrayDataInput data) {
        int x = data.readInt();
        int y = data.readInt();
        int z = data.readInt();
        TileEntity tileEntity = this.worldObj.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityCage) {
            this.cage = (TileEntityCage)tileEntity;
        }
    }
}
