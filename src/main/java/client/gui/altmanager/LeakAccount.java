package client.gui.altmanager;

import java.io.IOException;
import java.net.URI;

import org.lwjgl.input.Keyboard;

import net.mcleaks.Callback;
import net.mcleaks.ChatColor;
import net.mcleaks.MCLeaks;
import net.mcleaks.ModApi;
import net.mcleaks.RedeemResponse;
import net.mcleaks.SessionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class LeakAccount extends GuiScreen {
    private final boolean sessionRestored;
    private final String message;
    private GuiTextField tokenField;
    private GuiButton restoreButton;

    public LeakAccount(boolean sessionRestored) {
        this(sessionRestored, (String) null);
    }

    public LeakAccount(boolean sessionRestored, String message) {
        this.sessionRestored = sessionRestored;
        this.message = message;
    }

    public void updateScreen() {
        this.tokenField.updateCursorCounter();
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.restoreButton = new GuiButton(0, this.width / 2 - 150, this.height / 4 + 96 + 18, 128, 20,
                this.sessionRestored ? "Session restored!" : "Restore Session");
        this.restoreButton.enabled = MCLeaks.savedSession != null;
        this.buttonList.add(this.restoreButton);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 18, this.height / 4 + 96 + 18, 168, 20, "Redeem Token"));
        this.buttonList
                .add(new GuiButton(2, this.width / 2 - 150, this.height / 4 + 120 + 18, 158, 20, "Get Token"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 12, this.height / 4 + 120 + 18, 138, 20, "Cancel"));
        this.tokenField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 118, 200, 20);
        this.tokenField.setFocused(true);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                MCLeaks.remove();
                SessionManager.setSession(MCLeaks.savedSession);
                MCLeaks.savedSession = null;
                Minecraft.getMinecraft().displayGuiScreen(new LeakAccount(true));
            } else if (button.id == 1) {
                if (this.tokenField.getText().length() != 16) {
                    Minecraft.getMinecraft().displayGuiScreen(
                            new LeakAccount(false, ChatColor.RED + "The token has to be 16 characters long!"));
                    return;
                }

                button.enabled = false;
                button.displayString = "Please wait ...";
                ModApi.redeem(this.tokenField.getText(), new Callback() {
                    public void done(Object o) {
                        if (o instanceof String) {
                            Minecraft.getMinecraft()
                                    .displayGuiScreen(new LeakAccount(false, ChatColor.RED + "" + o));
                        } else {
                            if (MCLeaks.savedSession == null) {
                                MCLeaks.savedSession = Minecraft.getMinecraft().getSession();
                            }

                            RedeemResponse response = (RedeemResponse) o;
                            MCLeaks.refresh(response.getSession(), response.getMcName());
                            Minecraft.getMinecraft().displayGuiScreen(new LeakAccount(false,
                                    ChatColor.GREEN + "Your token was redeemed successfully!"));
                        }
                    }
                });
            } else if (button.id == 2) {
                try {
                    Class throwable = Class.forName("java.awt.Desktop");
                    Object object = throwable.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    throwable.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI("https://mcleaks.net/get")});
                } catch (Throwable var4) {
                    var4.printStackTrace();
                }
            } else if (button.id == 3) {
                mc.displayGuiScreen(new GuiAccountManage());
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.tokenField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 15) {
            this.tokenField.setFocused(!this.tokenField.isFocused());
        }

        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(1));
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, ChatColor.WHITE + "- " + ChatColor.AQUA + "MCLeaks"
                        + ChatColor.WHITE + "." + ChatColor.AQUA + "net " + ChatColor.WHITE + "-", this.width / 2, 17,
                16777215);
        this.drawCenteredString(this.fontRendererObj, "Free minecraft accounts", this.width / 2, 32, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Status:", this.width / 2, 68, 16777215);
        this.drawCenteredString(this.fontRendererObj, MCLeaks.getStatus(), this.width / 2, 78, 16777215);
        this.drawString(this.fontRendererObj, "Token", this.width / 2 - 99, 105, 10526880);
        if (this.message != null) {
            this.drawCenteredString(this.fontRendererObj, this.message, this.width / 2, 158, 16777215);
        }

        this.tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}