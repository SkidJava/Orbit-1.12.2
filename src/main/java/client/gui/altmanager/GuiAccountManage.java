package client.gui.altmanager;

import java.io.IOException;
import java.util.*;

import client.alt.Alt;
import client.alt.Ban;
import client.alt.LoginThread;
import client.gui.screen.GuiClientMainMenu;
import client.manager.managers.AccountManager;
import client.manager.managers.BanManager;
import client.manager.managers.StarManager;
import client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAccountManage extends GuiScreen {
    public static Alt selectedAlt = null;
    private boolean reverseSort = false;
    private GuiButton login, remove, session, rename, star, removeBan;
    private LoginThread loginThread;
    public static final ResourceLocation sort = new ResourceLocation("akiba/icons/sort.png");
    private int offset;
    public String info = "§bWaiting...";
    public GuiScreen currentScreen;

    public GuiAccountManage() {
        AccountManager.loadAccounts();
        AccountManager.saveAccounts();
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    mc.displayGuiScreen(new GuiClientMainMenu());
                } else if ((!this.info.equals("§eLogging in...")) && (!this.info.equals("§cDo not hit back! §eLogging in..."))) {
                    mc.displayGuiScreen(new GuiClientMainMenu());
                } else {
                    info = "§cDo not hit back! §eLogging in...";
                }
                break;
            }
            case 1: {
                String user = this.selectedAlt.email;
                String pass = this.selectedAlt.pass;
                this.loginThread = new LoginThread(user, pass);
                this.loginThread.start();
                break;
            }
            case 2: {
                if (this.loginThread != null) this.loginThread = null;
                if (BanManager.getBan(GuiAccountManage.this.selectedAlt) != null) BanManager.removeBan(GuiAccountManage.this.selectedAlt);
                AccountManager.removeAlt(GuiAccountManage.this.selectedAlt);
                this.info = "§aRemoved.";
                this.selectedAlt = null;
                AccountManager.saveAccounts();
                break;
            }
            case 3: {
                mc.displayGuiScreen(new AddAccount(this));
                break;
            }
            case 4: {
                mc.displayGuiScreen(new DirectAccount(this));
                break;
            }
            case 5: {
                mc.displayGuiScreen(new EditAccount(this));
                break;
            }
            case 11: {
                //mc.displayGuiScreen(new TheAltening(this));
                break;
            }
            case 6: {
                if (AccountManager.accountList.size() < 1) return;
                Random random = new Random();
                int randomInt = random.nextInt(AccountManager.accountList.size() - 1);
                Alt alt = AccountManager.accountList.get(randomInt);
                LoginThread thread = new LoginThread(alt.email, alt.pass);
                thread.start();
                break;
            }
            case 8: {
                reverseSort = !reverseSort;
                Collections.reverse(AccountManager.accountList);
                break;
            }
            case 9: {
                BanManager.removeBan(this.selectedAlt);
                BanManager.saveBans();
                break;
            }
            case 10: {
                StarManager.setStar(selectedAlt, !StarManager.getStar(selectedAlt).starred);
                StarManager.saveStars();
                break;
            }
            case 77: {
                this.mc.displayGuiScreen(new LeakAccount(false));
                break;
            }
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        if (Mouse.hasWheel()) {
            int y = Mouse.getDWheel();
            if (y < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (y > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.drawDefaultBackground();
        RenderUtils.drawBorderedRect(8.0F, 8.0F, 12.0F + fontRendererObj.getStringWidth(mc.session.getUsername()), 20.0F, 1.0F,525883, Integer.MIN_VALUE);
        drawString(this.fontRendererObj, mc.session.getUsername(), 10, 10, -7829368);
//        drawString(this.fontRendererObj, "Current Service: " + service.currentService.name(), 10, 20, -7829368);

        drawCenteredString(this.fontRendererObj, "Account Manager - " + AccountManager.accountList.size() + " alts", width / 2, 10, -1);
        drawCenteredString(this.fontRendererObj, this.info, width / 2, 20, -1);
        RenderUtils.drawBorderedRect(50.0F, 33.0F, width - 50, height - 53, 1.0F,-16777216, Integer.MIN_VALUE);
        GL11.glPushMatrix();
        prepareScissorBox(0.0F, 33.0F, width, height - 50);
        GL11.glEnable(3089);
        int y = 38;

        Iterator list = AccountManager.accountList.iterator();
        for (; ; ) {
            if (!list.hasNext()) {
                GL11.glDisable(3089);
                GL11.glPopMatrix();
                super.drawScreen(par1, par2, par3);
                if (this.selectedAlt == null) {
                    this.login.enabled = false;
                    this.remove.enabled = false;
                    this.rename.enabled = false;
                    this.star.enabled = false;
                } else {
                    this.login.enabled = true;
                    this.remove.enabled = true;
                    this.rename.enabled = true;
                    this.star.enabled = true;
                }

                if (this.selectedAlt != null) {
                    if (BanManager.getBan(this.selectedAlt) == null) {
                        this.removeBan.enabled = false;
                    } else {
                        this.removeBan.enabled = true;
                    }
                }

                if (Keyboard.isKeyDown(200)) {
                    this.offset -= 26;
                    if (this.offset < 0) {
                        this.offset = 0;
                    }
                } else if (Keyboard.isKeyDown(208)) {
                    this.offset += 26;
                    if (this.offset < 0) {
                        this.offset = 0;
                    }
                }
                return;
            }
            Alt alt = (Alt) list.next();
            if (isAltInArea(y)) {
                String name = StarManager.getStar(alt).starred ? "§e" + alt.email : alt.email;
                String pass;
                if (alt.email.endsWith("@alt.com")) {
                    pass = "The Altening";
                } else {
                    pass = "Mojang";
                }
                /*if (alt.pass.equals("")) {
                    pass = "§cCracked";
                } else {
                    pass = alt.pass.replaceAll(".", "*");
                }*/
                if (alt == this.selectedAlt) {
                    if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
                        RenderUtils.drawBorderedRect(52.0F, y - this.offset - 4, width - 52,y - this.offset + 20, 1.0F, -16777216, -2142943931);
                    } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
                        RenderUtils.drawBorderedRect(52.0F, y - this.offset - 4, width - 52,y - this.offset + 20, 1.0F, -16777216, -2142088622);
                    } else {
                        RenderUtils.drawBorderedRect(52.0F, y - this.offset - 4, width - 52,y - this.offset + 20, 1.0F, -16777216, -2144259791);
                    }
                } else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
                    RenderUtils.drawBorderedRect(52.0F, y - this.offset - 4, width - 52,y - this.offset + 20, 1.0F, -16777216, -2146101995);
                } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtils.drawBorderedRect(52.0F, y - this.offset - 4, width - 52,y - this.offset + 20, 1.0F, -16777216, -2145180893);
                }
                drawCenteredString(this.fontRendererObj, name, width / 2, y - this.offset, -1);
                drawCenteredString(this.fontRendererObj, pass, width / 2, y - this.offset + 10, 5592405);

                if (StarManager.getStar(alt).starred) {
                    drawCenteredString(this.fontRendererObj, "§eStarred", width / 2 + 130, y - this.offset, -1);
                }
                if (BanManager.getBan(alt) != null) {
                    Ban ban = BanManager.getBan(alt);
                    if (ban != null) {
                        if (ban.date.before(new Date())) {
                            BanManager.removeBan(ban);
                            return;
                        }
                        drawCenteredString(this.fontRendererObj, "§b" + BanManager.countdown(ban), width / 2 + 130, y - this.offset + 10, -1);
                    }
                }
                y += 26;
            }
        }
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 24, 70, 20, "Cancel"));
        this.buttonList.add(this.login = new GuiButton(1, width / 2 - 154, height - 48, 70, 20, "Login"));
        this.buttonList.add(this.remove = new GuiButton(2, width / 2 - 78, height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiButton(3, width / 2 + 4, height - 48, 70, 20, "Add"));
        this.buttonList.add(new GuiButton(77, width / 2 - 78, height - 48, 70, 20, "McLeak"));
        this.buttonList.add(this.rename = new GuiButton(5, width / 2 - 154, height - 24, 70, 20, "Edit"));
        this.buttonList.add(this.removeBan = new GuiButton(9, width / 2 + 4, height - 24, 70, 20, "Remove Ban"));
        this.buttonList.add(this.star = new GuiButton(10, width / 2 + 4 + 76, height - 48, 70, 20, "Protect"));

//        this.buttonList.add(new GuiButton(11, width / 2 - 206, height - 48, 50, 20, "Altening"));
//        this.buttonList.add(new GuiButton(7, width / 2 - 206, height - 24, 50, 20, "Switch"));
//        this.buttonList.add(new GuiButton(6, width / 2 + 4, height - 24, 70, 20, "Random"));

        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
        this.star.enabled = false;
        this.removeBan.enabled = false;
    }

    private boolean isAltInArea(int y) {
        return y - this.offset <= height - 50;
    }

    private boolean isMouseOverAlt(int x, int y, int y1) {
        return (x >= 52) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33) && (x <= width) && (
            y <= height - 50);
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset;

        Minecraft.getMinecraft();
        for (Iterator var7 = AccountManager.accountList.iterator(); var7.hasNext(); y += 26) {
            Alt e = (Alt) var7.next();
            if (isMouseOverAlt(par1, par2, y)) {
                if (e == this.selectedAlt) {
                    actionPerformed((GuiButton) this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = e;
            }
        }
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException var71) {
            var71.printStackTrace();
        }
    }

    public void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor),
            (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }
}
