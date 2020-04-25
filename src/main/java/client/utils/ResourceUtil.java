package client.utils;

import client.ClientInfo;
import net.minecraft.util.ResourceLocation;
import optifine.Config;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {

    public static Font getFontFile(String filename) {
        Font font = null;
        try {
            ResourceLocation resourcelocation = new ResourceLocation(ClientInfo .getResouceLocation() + "fonts/" + filename);

            if (Config.getDefaultResourcePack().resourceExists(resourcelocation))
            {
                InputStream inputstream = Config.getDefaultResourcePack().getInputStream(resourcelocation);
                font = Font.createFont(Font.TRUETYPE_FONT, inputstream);
                inputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return font;
    }
}
