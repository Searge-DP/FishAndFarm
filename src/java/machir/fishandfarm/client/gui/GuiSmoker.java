package machir.fishandfarm.client.gui;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.inventory.container.ContainerSmoker;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSmoker extends GuiContainer
{  
    private TileEntitySmoker smokerInventory;

    public GuiSmoker(InventoryPlayer inventoryPlayer, TileEntitySmoker tileEntitySmoker)
    {
        super(new ContainerSmoker(inventoryPlayer, tileEntitySmoker));
        this.smokerInventory = tileEntitySmoker;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     * 
     * @param x coordinate, not sure what exactly it is used for. They are affected by mouse and scaling
     * @param y coordinate, not sure what exactly it is used for. They are affected by mouse and scaling
     */
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String invName = this.smokerInventory.isInvNameLocalized() ? this.smokerInventory.getInvName() : I18n.getString(this.smokerInventory.getInvName());
        this.fontRenderer.drawString(invName, this.xSize / 2 - this.fontRenderer.getStringWidth(invName) / 2, 6, 4210752);
        this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ModInfo.SMOKER_GUI_TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.smokerInventory.isBurning())
        {
            i1 = this.smokerInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 80, l + 42 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }
    }
}