package com.partyrock.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.gui.dialog.DropdownDialog;
import com.partyrock.gui.dialog.DropdownDialogObject;
import com.partyrock.gui.dialog.InputDialog;
import com.partyrock.gui.dialog.MessageDialog;

public class PartyToolkit {

    /**
     * Try catch wrapper for Thread.sleep
     * 
     * @param ms ms to delay
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean openQuestion(Shell parent, String message, String title) {
        MessageDialog dialog = new MessageDialog(parent, message, title, new String[] { "Yes", "No" });
        return dialog.open();
    }

    public static boolean openConfirm(Shell parent, String message, String title) {
        MessageDialog dialog = new MessageDialog(parent, message, title);
        return dialog.open();
    }

    public static String openInput(Shell parent, String message, String title) {
        InputDialog dialog = new InputDialog(parent, message, title);
        return dialog.open();
    }

    public static Object openDropdown(Shell parent, String message, String title,
            ArrayList<DropdownDialogObject> options) {
        DropdownDialog dialog = new DropdownDialog(parent, message, title, options);
        return dialog.open();
    }

    /**
     * Returns a BufferedImage from a given image type
     * 
     * @param img The image to convert
     * @return The image as a buffered images
     */
    public static BufferedImage getBufferedImage(Image img) {
        if (img == null)
            return null;

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        int w = img.getWidth(null);
        int h = img.getHeight(null);
        // draw original image to thumbnail image object and
        // scale it to the new size on-the-fly
        BufferedImage bufimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufimg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return bufimg;
    }
}
