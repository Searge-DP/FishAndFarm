package machir.fishandfarm.client.gui;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.inventory.container.ContainerCage;
import machir.fishandfarm.tileentity.TileEntityCage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

public class GuiCage extends GuiContainer
{
    private TileEntityCage cageInventory;

    public GuiCage(InventoryPlayer inventory, TileEntityCage tileentitycage)
    {
        super(new ContainerCage(inventory, tileentitycage));
        cageInventory = tileentitycage;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString("Cage", 78, 14, 0x404040);
        fontRenderer.drawString("Inventory", 14, (ySize - 96) + 2, 0x404040);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        mc.renderEngine.bindTexture(ModInfo.CAGE_GUI_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        
        this.drawString(this.fontRenderer, "Bait", j + 64 - (fontRenderer.getStringWidth("Bait") / 2), k + 54, 0x7F7F7F);
    }
}