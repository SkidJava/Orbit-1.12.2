package client.utils.screenshot;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.logging.log4j.LogManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsyncScreenshotSaver implements Runnable {
    private int width;
    private int height;
    private int[] pixelValues;
    private Framebuffer frameBuffer;
    private File screenshotDir;

    public AsyncScreenshotSaver(final int width, final int height, final int[] pixelValues, final Framebuffer frameBuffer, final File screenshotDir) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.frameBuffer = frameBuffer;
        this.screenshotDir = screenshotDir;
    }

    @Override
    public void run() {
        processPixelValues(this.pixelValues, this.width, this.height);
        BufferedImage bufferedimage = null;
        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedimage = new BufferedImage(this.frameBuffer.framebufferWidth, this.frameBuffer.framebufferHeight, 1);
                int k;
                for (int j = k = this.frameBuffer.framebufferTextureHeight - this.frameBuffer.framebufferHeight; k < this.frameBuffer.framebufferTextureHeight; ++k) {
                    for (int l = 0; l < this.frameBuffer.framebufferWidth; ++l) {
                        bufferedimage.setRGB(l, k - j, this.pixelValues[k * this.frameBuffer.framebufferTextureWidth + l]);
                    }
                }
            }
            else {
                bufferedimage = new BufferedImage(this.width, this.height, 1);
                bufferedimage.setRGB(0, 0, this.width, this.height, this.pixelValues, 0, this.width);
            }
            final File file2 = getTimestampedPNGFileForDirectory(this.screenshotDir);
            ImageIO.write(bufferedimage, "png", file2);

            ITextComponent ichatcomponent = new TextComponentString("Saved Screenshot!");
            ichatcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            ichatcomponent.getStyle().setUnderlined(Boolean.valueOf(true));
            ichatcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Open §b" + file2.getName())));

            ITextComponent copyBtn = new TextComponentString("§6[Copy]§r");
            copyBtn.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".ss copy " + file2.getName()));
            copyBtn.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Copy §b" + file2.getName() + "§r to clipboard")));

            ITextComponent delBtn = new TextComponentString("§7[Delete]§r");
            delBtn.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".ss delete " + file2.getName()));
            delBtn.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Delete §b" + file2.getName())));

            ITextComponent upBtn = new TextComponentString("§e[Upload]§r");
            upBtn.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".ss upload " + file2.getName()));
            upBtn.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Upload §b" + file2.getName() + "§r to imgur.com")));

            GuiIngame.getChatGUI().printChatMessage(
                new TextComponentTranslation("").appendSibling(ichatcomponent)
                    .appendText(" ").appendSibling(copyBtn)
                    .appendText(" ").appendSibling(delBtn)
                    .appendText(" ").appendSibling(upBtn));
        }
        catch (Exception exception) {
            LogManager.getLogger().warn("Couldn't save screenshot", exception);
            GuiIngame.getChatGUI().printChatMessage(new TextComponentTranslation("screenshot.failure", new Object[] { exception.getMessage() }));
        }
    }

    private static File getTimestampedPNGFileForDirectory(final File gameDirectory) {
        final String s = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int i = 1;
        File file1;
        while (true) {
            file1 = new File(gameDirectory, s + ((i == 1) ? "" : ("_" + i)) + ".png");
            if (!file1.exists()) {
                break;
            }
            ++i;
        }
        return file1;
    }

    private static void processPixelValues(final int[] p_147953_0_, final int p_147953_1_, final int p_147953_2_) {
        final int[] aint = new int[p_147953_1_];
        for (int i = p_147953_2_ / 2, j = 0; j < i; ++j) {
            System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
            System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
        }
    }
}
