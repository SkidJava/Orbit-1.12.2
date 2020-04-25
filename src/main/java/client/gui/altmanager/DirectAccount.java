package client.gui.altmanager;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import client.alt.LoginThread;
import client.gui.GuiPasswordField;
import client.gui.screen.GuiClientMainMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public final class DirectAccount extends GuiScreen {
    private GuiPasswordField password;
    private GuiScreen previousScreen;
    private LoginThread thread;
    private GuiTextField username;
    private GuiTextField importType;

    public DirectAccount(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty()) login(this.username.getText(), this.password.getText());
                else if (!this.importType.getText().isEmpty()) login(this.importType.getText().split(":")[0], this.importType.getText().split(":")[1]);
                break;
            }
            case 1: {
                mc.displayGuiScreen(this.previousScreen);
            }
            case 2: {
                Toolkit kit = Toolkit.getDefaultToolkit();
                String[] clip = {"" , ""};
                try {
                    String src = (String) kit.getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!src.contains(":")) return;
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

    public void drawScreen(int x, int y, float z) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.importType.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        drawCenteredString(mc.fontRendererObj, "§7Waiting...",width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        if (this.importType.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Email:pass", width / 2 - 96, 146, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int h = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, h + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, h + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiButton(2, width / 2 + 105, 140, 20, 20, "clip"));
        this.username = new GuiTextField(h, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.importType = new GuiTextField(h, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
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

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
        this.importType.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        this.importType.updateCursorCounter();
    }

    public void login(String email, String pass) {
        mc.displayGuiScreen(this.previousScreen);
        GuiClientMainMenu.accountScreen.info = "§aAlt Direct Logged";
        new LoginThread(email, pass).start();
    }
}
