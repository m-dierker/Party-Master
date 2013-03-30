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
    private File file;
    private ArrayList<BufferedImage> images;
    String chosenFilename;
    private int lastImage = -1;

    public FaviconAnimator(LightMaster master, int startTime, String internalID, ArrayList<ElementController> panels,
            double duration) {
        super(master, startTime, internalID, panels, duration);

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

            int timeSegment = (int) (percentage * (images.size()));
            if ((timeSegment) < images.size()) {
                if (lastImage >= timeSegment) {
                    return;
                }
                lastImage = timeSegment;
                BufferedImage favicon = images.get(timeSegment);
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
        images = new ArrayList<BufferedImage>();

        for (int i = 0; i < files.length; i++) {
            file = new File(files[i].getPath());
            if (!file.getName().endsWith(".ico")) {
                continue;
            }
            // Process all files in the folder to be buffered images stored in an array
            try {
                BufferedImage img = ICODecoder.read(file).get(0);

                if (img.getHeight() != 16 || img.getWidth() != 16) {
                    img = (BufferedImage) img.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                }

                images.add(img);
            } catch (IOException e) {
                System.out.println("Error reading image file");
                e.printStackTrace();
            }
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
