package com.partyrock.anim.ledpanels;

/**
 * Plays animations from favicons. Add a folder to the Party Rock Icons folder with ico files.
 * 
 * @author Ehsan Razfar and Emily Tran
 * 
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import net.sf.image4j.codec.ico.ICODecoder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.settings.Saver;
import com.partyrock.settings.SectionSettings;
import com.partyrock.system.OSDetector;

public class FaviconAnimator extends ElementAnimation {
    // Instructions: Go to the path and edit to be the favicon folder desired
    private BufferedImage favicon;
    private File file;
    BufferedImage[] images;
    String chosenFilename;
    private int lastImage = -1;

    public FaviconAnimator(LightMaster master, int startTime, String internalID, ArrayList<ElementController> panels,
            double duration) {
        super(master, startTime, internalID, panels, duration);
        favicon = null;

        needsIncrements();
    }

    /**
     * Called when the animation is run
     */
    @Override
    public void trigger() {
        for (ElementController element : getElements()) {
            LEDPanelController panel = (LEDPanelController) element;

            for (int r = 0; r < panel.getPanelHeight(); r++) {
                for (int c = 0; c < panel.getPanelWidth(); c++) {
                    // initialize board to black
                    panel.setColor(r, c, sysColor(0, 0, 0));
                }
            }

        }
    }

    public void increment(double percentage) {
        // For every element we're given
        for (ElementController controller : getElements()) {
            // We only put LEDS in our getSupportedTypes(), so that's all we're going to get.
            LEDPanelController panel = (LEDPanelController) controller;

            int timeSegment = (int) (percentage * (images.length));
            if ((timeSegment) < images.length) {
                if (lastImage >= timeSegment) {
                    return;
                }
                lastImage = timeSegment;
                favicon = images[timeSegment];
                for (int r = 0; r < panel.getPanelHeight(); r++) {
                    for (int c = 0; c < panel.getPanelWidth(); c++) {
                        int color = favicon.getRGB(c, r);
                        panel.setColor(r, c, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
                    }
                }
                panel.transmit();
            }
        }
    }

    @Override
    public void setup(Shell window) {
        String choosertitle = "Choose a folder for favicons";
        DirectoryDialog dialog = new DirectoryDialog(window, SWT.OPEN);
        dialog.setText(choosertitle);
        dialog.setFilterPath("Party Rock Icons");
        String fullFolderName = dialog.open();

        if (fullFolderName == null) {
            return;
        }

        String folderName;
        if (OSDetector.isWindows()) {
            folderName = fullFolderName.substring(fullFolderName.lastIndexOf("\\") + 1);
        } else {
            folderName = fullFolderName.substring(fullFolderName.lastIndexOf("/") + 1);
        }

        System.out.println(folderName);
        if (folderName != null) {
            chosenFilename = folderName;
            loadFavicons();
        }
    }

    private void loadFavicons() {
        /* Edit the File path to the folder with all favicons, make sure no subfolders */
        File folder = new File("Party Rock Icons/" + chosenFilename);
        File[] files = folder.listFiles();
        images = new BufferedImage[files.length];

        for (int i = 0; i < files.length; i++) {
            file = new File(files[i].getPath());
            // Process all files in the folder to be buffered images stored in an array
            try {
                images[i] = ICODecoder.read(file).get(0);
                if (images[i].getHeight() != 16 || images[i].getWidth() != 16) {

                    images[i] = (BufferedImage) images[i].getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                }
            } catch (IOException e) {
                System.out.println("Error reading image file");
                e.printStackTrace();
            }
            favicon = images[i];
        }

        /*
         * TODO: Did not properly call .delete() on any files or close them
         */

    }

    /**
     * Returns the types of elements this animation supports
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LEDS);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {
        settings.put("file", Saver.saveLocalFile(file));
    }

    @Override
    protected void loadSettings(SectionSettings settings) {
        file = Saver.loadLocalFile(settings.get("file"), this);

        loadFavicons();
    }

}
