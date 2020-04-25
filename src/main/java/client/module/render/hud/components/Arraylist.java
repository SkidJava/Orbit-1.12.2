package client.module.render.hud.components;

import client.event.EventTarget;
import client.event.events.render.EventRender2D;
import client.module.Module;
import client.module.render.hud.HUD;
import client.utils.ColorUtils;
import client.utils.TimeHelper;
import client.utils.render.RenderUtils;

import java.util.ArrayList;

public class Arraylist extends HUD {

    public TimeHelper flagTime = new TimeHelper();
    public boolean colorFlag = false;
    public TimeHelper fpsTimeHelper = new TimeHelper();
    private float blueColor = 255, changeTime = 2000;
    private float speed = 0.25f;
    private DrawPos pos = DrawPos.TOP;

    public Arraylist() {
        super("ArrayList", true, true);
    }

    @EventTarget
    public void onRender(EventRender2D e) {
		if (flagTime.hasReached(changeTime)) {
			colorFlag = ! colorFlag;
			flagTime.reset();
		}

		/*blueColor = colorFlag ? blueColor - 0.05f * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS()): blueColor + 0.5f * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS());
		if (blueColor > 255) blueColor = 255;
		else if (blueColor < 100) blueColor = 100;
		hudFont.drawStringWithShadow("test", 90, 4, new Color(0, 0, (int)blueColor, 255).getRGB());*/
		
        //if (managers.settingManager.getSetting(HUD.class, "ArrayList").getBooleanValue()) {
            ArrayList<Module> mods = managers.moduleManager.getVisibleMods();

            mods.sort((o1, o2) -> hudFont.getWidth(o2.getName()) - hudFont.getWidth(o1.getName()));
            float y = 2;
            float tempX = 0;
            for (Module m : mods) {
                m.anim = m.anim + speed
                        * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS());
                if (m.anim > hudFont.getWidth(m.getName())) m.anim = hudFont.getWidth(m.getName());

                RenderUtils.drawRect(e.width - m.anim - 5, y - 2, e.width - 1, y + (hudFont.getHeight()),
                        ColorUtils.backColor);
                RenderUtils.drawRect(e.width - m.anim - 5 - 1, y - 2, e.width - m.anim - 5, y + (hudFont.getHeight()) + 1,
                        ColorUtils.arrayColor);
                RenderUtils.drawRect(e.width - 1, y - 2, e.width, y + (hudFont.getHeight()), ColorUtils.arrayColor);
                RenderUtils.drawRect(e.width - tempX - 5, pos == DrawPos.TOP ? y - 2 : y + hudFont.getHeight(),
                        e.width - m.anim - 5, pos == DrawPos.TOP ? y - 1 : y + hudFont.getHeight() + 1,
                        ColorUtils.arrayColor);
                hudFont.drawStringWithShadow(m.getName(), e.width - m.anim - 3, y, ColorUtils.arrayColor);
                tempX = m.anim;
                y = pos == DrawPos.TOP ? y + hudFont.getHeight() + 2 : y - hudFont.getHeight() - 2;
            }

            if (mods.size() != 0) {
                RenderUtils.drawRect(e.width - mods.get(0).anim - 5 - 1f, pos == DrawPos.TOP ? 0 : e.height, e.width,
                        pos == DrawPos.TOP ? 1 : e.height - 1, ColorUtils.arrayColor);
                RenderUtils.drawRect(e.width - tempX - 5 - 1f, y - 2,
                        e.width, y - 1, ColorUtils.arrayColor);
            }

            fpsTimeHelper.reset();
        //}

    }

    public enum DrawPos {
        TOP, BOTTOM,
    }
}
