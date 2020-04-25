package client.gui.altmanager;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import client.alt.Alt;
import client.alt.LoginThread;
import client.gui.GuiPasswordField;
import client.gui.screen.GuiClientMainMenu;
import client.manager.managers.AccountManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class AddAccount extends GuiScreen {
    public static String status = "§7Waiting...";
    private final GuiAccountManage manager;
    private GuiPasswordField password;
    private GuiTextField username;
    private GuiTextField importType;

    public AddAccount(GuiAccountManage manager) {
        this.manager = manager;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case -1: {
                if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty()) {
                    LoginThread thread = new LoginThread(this.username.getText(), this.password.getText());
                    thread.start();
                    break;
                } else if (!this.importType.getText().isEmpty()) {
                    LoginThread thread = new LoginThread(this.importType.getText().split(":")[0], this.importType.getText().split(":")[1]);
                    thread.start();
                    break;
                }
                break;
            }
            case 0: {
                if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty()) add(this.username.getText(), this.password.getText());
                else if (!this.importType.getText().isEmpty()) add(this.importType.getText().split(":")[0], this.importType.getText().split(":")[1]);
                break;
            }
            case 1: {
                mc.displayGuiScreen(this.manager);
            }
            case 2: {
                Toolkit kit = Toolkit.getDefaultToolkit();
                String[] clip = {"" , ""};
                try {
                    String src = (String) kit.getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!src.contains(":")) {
                        this.status = "§cYour copied text is wrong!";
                        return;
                    }
                    clip = src.replace("\n", "").split(":");
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.username.setText(clip[0]);
                this.password.setText(clip[1]);
            }
        }
    }

    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.importType.drawTextBox();
        drawCenteredString(this.fontRendererObj, "Add Alt", width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        if (this.importType.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Email:pass", width / 2 - 96, 146, -7829368);
        }
        drawCenteredString(this.fontRendererObj, this.status, width / 2, 30, -1);
        super.drawScreen(i, j, f);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, width / 2 - 100, height / 4 + 68 + 12, "Try"));
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        this.buttonList.add(new GuiButton(2, width / 2 + 105, 140, 20, 20, "clip"));
        this.username = new GuiTextField(this.eventButton, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.importType = new GuiTextField(this.eventButton, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
    }

    protected void keyTyped(char c, int key) {
        this.username.textboxKeyTyped(c, key);
        this.password.textboxKeyTyped(c, key);
        this.importType.textboxKeyTyped(c, key);
        if ((c == '\t') && ((this.username.isFocused()) || (this.password.isFocused()) || (this.importType.isFocused()))) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.importType.setFocused(!this.importType.isFocused());
        }
        if (c == '\r') {
            actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
        this.importType.mouseClicked(par1, par2, par3);
    }

    public void add(String email, String pass) {
        AddAccount.this.status = "§7Logging in...";

        AccountManager.addAlt(0, new Alt(email, pass));
        mc.displayGuiScreen(this.manager);
        AccountManager.saveAccounts();
        GuiClientMainMenu.accountScreen.initGui();
        GuiClientMainMenu.accountScreen.info = "§aAlt Added";
    }
}
