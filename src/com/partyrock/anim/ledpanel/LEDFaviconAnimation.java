package com.partyrock.anim.ledpanel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import net.sf.image4j.codec.ico.ICODecoder;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.settings.SectionSettings;
import com.partyrock.tools.PartyToolkit;
import com.partyrock.tools.net.NetManager;

public class LEDFaviconAnimation extends ElementAnimation {

    private BufferedImage favicon;
    private String url;

    public LEDFaviconAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> panels, double duration) {
        super(master, startTime, internalID, panels, duration);
        favicon = null;
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
                    int color = favicon.getRGB(c, r);
                    panel.setColor(r, c, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
                }
            }
            panel.transmit();
        }
    }

    @Override
    public void setup(Shell window) {
        url = null;

        while (url == null || url.trim().equals("")) {
            url = PartyToolkit.openInput(window, "Enter the URL of the website to display the favicon of",
                    "Favicon URL");
        }

        if (!url.endsWith("favicon.ico")) {
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += "favicon.ico";
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        downloadFavicon();
    }

    private void downloadFavicon() {
        // Download the favicon to the computer
        File tmp_favicon = new File("tmp-favicon.ico");
        NetManager.downloadURLToFile(url, tmp_favicon);

        // Read it in as a BufferedImage
        try {
            favicon = ICODecoder.read(tmp_favicon).get(0);
            if (favicon.getHeight() != 16 || favicon.getWidth() != 16) {
                favicon = (BufferedImage) favicon.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            System.out.println("Error reading image file");
            e.printStackTrace();
        }

        // Delete the file
        tmp_favicon.delete();
    }

    /**
     * Returns the types of elements this animation supports
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LEDS);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {
        settings.put("url", url);
    }

    @Override
    protected void loadSettings(SectionSettings settings) {
        url = settings.get("url");

        // We have to download the image again
        downloadFavicon();
    }
}
