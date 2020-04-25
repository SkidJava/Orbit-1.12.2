package client.gui.altmanager;

import java.io.IOException;

import client.alt.Alt;
import client.gui.GuiPasswordField;
import client.manager.managers.AccountManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class EditAccount extends GuiScreen {
    private final GuiAccountManage manager;
    private GuiTextField nameField;
    private GuiPasswordField passField;
    private String status = "Waiting...";

    public EditAccount(GuiAccountManage manager) {
        this.manager = manager;
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                AccountManager.addAlt(this.getPositionInAltList(this.manager.selectedAlt), new Alt(nameField.getText(), passField.getText()));
                AccountManager.removeAlt(this.manager.selectedAlt);
                AccountManager.saveAccounts();
                this.status = "§aRenamed!";
                break;
            case 1:
                mc.displayGuiScreen(this.manager);
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "Rename Alt", width / 2, 10, -1);
        drawCenteredString(this.fontRendererObj, this.status, width / 2, 20, -1);
        this.nameField.drawTextBox();
        this.passField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Rename"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, this.fontRendererObj, width / 2 - 100, 76, 200, 20);
        this.passField = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        nameField.setText(this.manager.selectedAlt.email);
        passField.setText(this.manager.selectedAlt.pass);
    }

    protected void keyTyped(char par1, int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.passField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t') && (this.nameField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
        }
        if (par1 == '\r') {
            actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        this.nameField.mouseClicked(par1, par2, par3);
        this.passField.mouseClicked(par1, par2, par3);
    }

    public int getPositionInAltList(Alt alt) {
        int i = 0;
        while (i < AccountManager.accountList.size() - 1) {
            if (AccountManager.accountList.get(i).equals(alt)) {
                return i;
            }
            ++i;
        }
        return 0;
    }
}