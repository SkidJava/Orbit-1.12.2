package client.manager.managers;

import client.event.EventTarget;
import client.event.events.render.*;
import client.gui.screen.GuiClientMainMenu;
import client.manager.AbstManager;
import client.utils.TimeHelper;
import com.google.common.base.Throwables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ScreenManager extends AbstManager {

    private GuiScreen openScreen;
    private GuiScreen closeScreen;
    private float closeSpeed = 0.02f;
    private float openSpeed = 0.01f;

    private TimeHelper fpsTimeHelper = new TimeHelper();

    public ScreenManager() {
        managers.eventManager.register(this);
    }

    @EventTarget
    public void onChangeGui(EventChangeScreen event) {
        openScreen = event.getCurrentScreen();
        closeScreen = event.getPreviousScreen();

        if(openScreen != null) {
            for (GuiButton button : openScreen.buttonList)
                button.scale = 0.0f;
        }

        if((openScreen instanceof GuiClientMainMenu && closeScreen instanceof GuiIngameMenu) || (closeScreen != null && ((openScreen != null && openScreen.buttonList.isEmpty() && !(closeScreen instanceof GuiClientMainMenu)) || closeScreen.buttonList.isEmpty()))) {
            closeScreen.closed = true;
            closeScreen.scale = 0.0f;
        }

        fpsTimeHelper.reset();
    }

    @EventTarget
    public void onUpdateScreen(EventUpdateScreen event) {
        if(closeScreen != null && !closeScreen.closed) {
            if(closeScreen.scale > 0.0f) {
                closeScreen.scale -= closeSpeed * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS());
                closeScreen.scale = closeScreen.scale > 0.0f ? closeScreen.scale : 0.0f;
            } else {
                closeScreen.closed = true;
            }

            if(closeScreen instanceof GuiIngameMenu) ((GuiIngameMenu)closeScreen).startY = -25 * (1 - closeScreen.scale);
            for(GuiButton button : closeScreen.buttonList)
                button.scale = closeScreen.scale;
        } else if(closeScreen  != null) {
            Minecraft.getMinecraft().displayGuiScreen(openScreen);
            if(closeScreen != null) closeScreen.closed = false;
            closeScreen = null;
        }else if(openScreen != null){
            if(openScreen.scale < 1.0f) {
                openScreen.scale += openSpeed * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS());
                openScreen.scale = openScreen.scale < 1.0f ? openScreen.scale : 1.0f;
            } else if(openScreen.scale >= 1.0f || openScreen.buttonList.isEmpty()){
                openScreen.closed = false;
                openScreen = null;
                return;
            }

            if(openScreen instanceof GuiIngameMenu) ((GuiIngameMenu)openScreen).startY = -25 * (1 - openScreen.scale);
            for(GuiButton button : openScreen.buttonList)
                button.scale = openScreen.scale;
        }

        fpsTimeHelper.reset();
    }

    private long start;
    private int oldWidth;
    private int oldHeight;

    @EventTarget
    public void onOpenInventory(EventOpenScreen event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
            boolean excluded = event.getScreen() == null || event.getScreen() instanceof GuiChat;
            if (!excluded) {
                oldWidth = event.getScreen().width;
                oldHeight = event.getScreen().height;
                er.loadShader(new ResourceLocation("shaders/post/fade_in_blur.json"));
                start = System.currentTimeMillis();
            } else if (er.isShaderActive() && excluded) {
                er.stopUseShader();
            }
        }
    }

    @EventTarget
    public void onRender(EventUpdateScreen event) {
        if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            if(Minecraft.getMinecraft().currentScreen.width != oldWidth || Minecraft.getMinecraft().currentScreen.height != oldHeight) {
                EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
                er.stopUseShader();
                oldWidth = Minecraft.getMinecraft().currentScreen.width;
                oldHeight = Minecraft.getMinecraft().currentScreen.height;
                er.loadShader(new ResourceLocation("shaders/post/fade_in_blur.json"));
            }

            ShaderGroup sg = Minecraft.getMinecraft().entityRenderer.getShaderGroup();
            try {
                List<Shader> shaders = ShaderGroup.listShaders;
                for (Shader s : shaders) {
                    ShaderUniform su = s.getShaderManager().getShaderUniform("Progress");
                    if (su != null) {
                        su.set(getProgress());
                    }
                }
            } catch (IllegalArgumentException e) {
                Throwables.propagate(e);
            }
        }
    }

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) 100, 1);
    }
}
