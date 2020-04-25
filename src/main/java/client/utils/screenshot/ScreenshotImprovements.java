package client.utils.screenshot;

import client.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.nio.IntBuffer;

public class ScreenshotImprovements {
    public static void upload(final String filename) {
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "screenshots\\" + filename);
        if (!f.exists()) {
            PlayerUtil.tellPlayer("Could not find file!");
            return;
        }

        /*try {
            JSONObject jsonObject = new JSONObject(Uploader.upload(f));
            String link = "https://imgur.com/"+ jsonObject.getJSONObject("data").getString("id");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(link), null);
            PlayerUtil.tellPlayer("Copied link.");
        } catch (JSONException e) {
            e.printStackTrace();
            PlayerUtil.tellPlayer("Upload failed.");
        }*/
    }

    public static void delete(final String filename) {
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "screenshots\\" + filename);
        if (!f.exists()) {
            PlayerUtil.tellPlayer("Could not find file!");
            return;
        }
        if (!f.delete()) {
            f.deleteOnExit();
            PlayerUtil.tellPlayer("Could not delete yet!");
        }
        else {
            PlayerUtil.tellPlayer("Deleted screenshot!");
        }
    }

    public static void copy(final String filename) {
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "screenshots\\" + filename);
        if (!f.exists()) {
            PlayerUtil.tellPlayer("Could not find file!");
            return;
        }
        final Toolkit tolkit = Toolkit.getDefaultToolkit();
        tolkit.getSystemClipboard().setContents(new ImageSelection(tolkit.getImage(f.getAbsolutePath())), null);
        PlayerUtil.tellPlayer("Copied screenshot!");
    }
    private static class ImageSelection implements Transferable
    {
        private Image image;

        public ImageSelection(final Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return this.image;
        }
    }

    public static void saveScreenshot(final File gameDirectory, int width, int height, final Framebuffer buffer) {
        final File file1 = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
        file1.mkdir();
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }
        final int i = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        }
        else {
            GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        new Thread(new AsyncScreenshotSaver(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), new File(Minecraft.getMinecraft().mcDataDir, "screenshots"))).start();
    }

    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
}
