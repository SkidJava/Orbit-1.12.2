package client.gui.screen;

import client.ClientInfo;
import client.gui.altmanager.GuiAccountManage;
import client.gui.button.GuiButtonTexture;
import client.manager.Managers;
import client.utils.font.CharFontRenderer;
import client.utils.render.ParticleGenerator;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class GuiClientMainMenu extends GuiScreen
{
    public static GuiAccountManage accountScreen = new GuiAccountManage();

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    private int scaleSetting = -1;

    /**
     * The Object object utilized as a thread lock when performing non thread-safe operations
     */
    private final Object threadLock = new Object();
    public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";

    /** Width of openGLWarning2 */
    private int openGLWarning2Width;

    /** Width of openGLWarning1 */
    private int openGLWarning1Width;

    /** Left x coordinate of the OpenGL warning */
    private int openGLWarningX1;

    /** Top y coordinate of the OpenGL warning */
    private int openGLWarningY1;

    /** Right x coordinate of the OpenGL warning */
    private int openGLWarningX2;

    /** Bottom y coordinate of the OpenGL warning */
    private int openGLWarningY2;

    /** OpenGL graphics card warning. */
    private String openGLWarning1;

    /** OpenGL graphics card warning. */
    private String openGLWarning2;

    /** Link to the Mojang Support about minimum requirements */
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");

    private final ResourceLocation BackGround = new ResourceLocation(ClientInfo .getResouceLocation() + "textures/background.png");

    private final ResourceLocation logo = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/logo.png");
    private final ResourceLocation single = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/single.png");
    private final ResourceLocation multi = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/multi.png");
    private final ResourceLocation account = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/account.png");
    private final ResourceLocation setting = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/setting.png");
    private final ResourceLocation exit = new ResourceLocation(ClientInfo .getResouceLocation() + "icons/exit.png");

    private CharFontRenderer clientFont;
    private CharFontRenderer verFont;

    private ParticleGenerator pGen;

    public GuiClientMainMenu()
    {
        this.openGLWarning2 = MORE_INFO_TEXT;
        IResource iresource = null;

        try
        {
            List<String> list = Lists.newArrayList();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }
        }
        catch (IOException var8)
        {
        }
        finally
        {
            IOUtils.closeQuietly(iresource);
        }
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
            this.openGLWarning1 = I18n.format("title.oldgl1");
            this.openGLWarning2 = I18n.format("title.oldgl2");
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-thePlayer
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the button (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        if(pGen == null)
            this.pGen = new ParticleGenerator(150, this.width, this.height);

        if(clientFont == null)
            clientFont = Managers.getManagers().fontManager.getFont("orbitron.ttf", 60);
        if(verFont == null)
            verFont = Managers.getManagers().fontManager.getFont("orbitron-m.ttf", 30);

        int j = this.height / 4 + 48;

        int buttonWidth = 50;
        int buttonSpaceWidth = 30;

        int buttonSumWidth = buttonWidth + buttonSpaceWidth;

        this.buttonList.add(new GuiButtonTexture(1, this.width / 2 - (int)(buttonWidth * 1.5f) - (int)(buttonSpaceWidth * 1.5f) - buttonWidth, this.height / 2 + 75, buttonWidth, buttonWidth, single));
        this.buttonList.add(new GuiButtonTexture(2, this.width / 2 - buttonWidth / 2 - buttonSpaceWidth / 2 - buttonWidth, this.height / 2 + 75, buttonWidth, buttonWidth, multi));
        this.buttonList.add(new GuiButtonTexture(0, this.width / 2, this.height / 2 + 75, buttonWidth, buttonWidth, setting));
        this.buttonList.add(new GuiButtonTexture(3, this.width / 2 + buttonWidth / 2 + buttonSpaceWidth / 2 + buttonWidth, this.height / 2 + 75, buttonWidth, buttonWidth, account));
        this.buttonList.add(new GuiButtonTexture(4, this.width / 2 + (int)(buttonWidth * 1.5f) + (int)(buttonSpaceWidth * 1.5f) + buttonWidth, this.height / 2 + 75, buttonWidth, buttonWidth, exit));

        synchronized (this.threadLock)
        {
            this.openGLWarning1Width = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
            this.openGLWarningX1 = (this.width - k) / 2;
            this.openGLWarningY1 = (this.buttonList.get(0)).yPosition - 24;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
        }

        this.mc.setConnectedToRealms(false);
    }

    /**
     * Adds Singleplayer and Multiplayer button on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {

        //this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        //this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for button)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }

        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if(button.id == 3) {
            mc.displayGuiScreen(accountScreen);
        }

        if (button.id == 4)
        {
            this.mc.shutdown();
        }
    }

    private void switchToRealms()
    {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result && id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 12)
        {
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13)
        {
            if (result)
            {
                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable1)
                {
                    LOGGER.error("Couldn't open link", throwable1);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution scaledRes)
    {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(BackGround);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }


    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);

        if(scaleSetting == -1) {
            scaleSetting = mc.gameSettings.guiScale;
            mc.gameSettings.guiScale = 0;

            int w = scaledresolution.getScaledWidth();
            int h = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, w, h);
        }

        GlStateManager.disableAlpha();
        renderPumpkinOverlay(scaledresolution);
        GlStateManager.enableAlpha();

        int i = 274;
        int j = this.width / 2 - 137;
        int k = 30;
        int l = new Color(0, 0, 0, 0).getRGB();
        int i1 = new Color(50, 98, 145, 150).getRGB();

        if(pGen != null)
            this.pGen.drawParticles(this.width, this.height);

        if (l != 0 || i1 != 0)
        {
            this.drawGradientRect(0, 0, this.width, this.height, l, i1);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty())
        {
            drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, (this.buttonList.get(0)).yPosition - 12, -1);
        }

        mc.getTextureManager().bindTexture(logo);
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableBlend();
        drawScaledCustomSizeModalRect(this.width / 2 - 40, this.height / 2 - 75, 0.0F, 0.0F, 1024, 1024, 80, 80, 1024, 1024);

        if(clientFont != null) {
            GlStateManager.scale(1.5f, 1.5f, 1.5f);
            clientFont.drawCenteredStringWithShadow(ClientInfo.getClientName(), (this.width / 2.0f) / 1.5f, (this.height / 2.0f + 20) / 1.5f, new Color(255, 255, 255, 255).getRGB());
            GlStateManager.scale(1.0f / 1.5f, 1.0f / 1.5f, 1.0f / 1.5f);
        }

        if(verFont != null)
            verFont.drawStringWithShadow(ClientInfo.getClientBuild(), this.width - verFont.getWidth(ClientInfo.getClientBuild()) - 2, this.height - verFont.getHeight(), new Color(224, 224, 224, 255).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.threadLock)
        {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        mc.gameSettings.guiScale = scaleSetting;
        scaleSetting = -1;
    }
}
