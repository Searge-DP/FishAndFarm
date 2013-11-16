package machir.fishandfarm.client.gui;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.inventory.container.ContainerStove;
import machir.fishandfarm.packet.StovePacket;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiStove extends GuiContainer
{  
    private TileEntityStove stoveInventory;

    public GuiStove(InventoryPlayer inventoryPlayer, TileEntityStove tileEntityStove)
    {
        super(new ContainerStove(inventoryPlayer, tileEntityStove));
        this.stoveInventory = tileEntityStove;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     * 
     * @param x coordinate, not sure what exactly it is used for. They are affected by mouse and scaling
     * @param y coordinate, not sure what exactly it is used for. They are affected by mouse and scaling
     */
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String invName = this.stoveInventory.isInvNameLocalized() ? this.stoveInventory.getInvName() : I18n.getString(this.stoveInventory.getInvName());
        this.fontRenderer.drawString(invName, this.xSize / 2 - this.fontRenderer.getStringWidth(invName) / 2, 6, 4210752);
        this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ModInfo.STOVE_GUI_TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.stoveInventory.isBurning())
        {
            i1 = this.stoveInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.stoveInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }
    
    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int x, int y, int mouseButton)
    {
    	super.mouseClicked(x, y, mouseButton);
    	//stoveInventory.sendPacket();
    }
}
