package client.alt;

import client.gui.altmanager.GuiAccountManage;
import client.gui.altmanager.AddAccount;
import client.gui.screen.GuiClientMainMenu;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class LoginThread extends Thread {
    private Minecraft mc = Minecraft.getMinecraft();
    private String pass;
    private String email;

    public LoginThread(String email, String pass) {
        super("LoginThread");
        this.email = email;
        this.pass = pass;
        GuiClientMainMenu.accountScreen.info = "§aLogging In...";
        AddAccount.status = "§aLogging In...";
    }

    private Session createSession(String email, String pass) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(pass);
        try {
            authentication.logIn();
            return new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "legacy");
        }
        catch (AuthenticationException e) {
            return null;
        }
    }

    @Override
    public void run() {
        if (this.pass.equals("")) {
            this.mc.session = new Session(this.email, "", "", "legacy");
            GuiClientMainMenu.accountScreen.info = "§eLogged In: " + this.email;
            AddAccount.status = "§eLogged In: " + this.email;
            return;
        }
        Session session = this.createSession(this.email, this.pass);
        if (session == null) {
            GuiClientMainMenu.accountScreen.info = "§cLogin Failed";
            AddAccount.status = "§cLogin Failed";
        } else {
            this.mc.session = session;
            GuiClientMainMenu.accountScreen.info = "§aLogged In: " + session.getUsername();
            AddAccount.status = "§aLogged In: " + session.getUsername();
            GuiAccountManage.selectedAlt = new Alt(email, pass);
        }
    }
}
