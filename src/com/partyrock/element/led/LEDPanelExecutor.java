package com.partyrock.element.led;

import java.io.IOException;

import org.eclipse.swt.graphics.Color;

import com.partyrock.element.ElementExecutor;
import com.partyrock.tools.PartyToolkit;

public class LEDPanelExecutor extends ElementExecutor {
    private LEDPanelController controller;

    public LEDPanelExecutor(LEDPanelController controller) {
        super(controller);
        this.controller = controller;
    }

    @SuppressWarnings("static-access")
    public void transmit() {
        for (int r = 0; r < controller.getPanelHeight(); r++) {
            for (int c = 0; c < controller.getPanelWidth(); c++) {
                if (r >= 8 || c >= 8)
                    continue;
                Color color = controller.getColor(r, c);
                int red = (color.getRed() / (256 / controller.LEVELS_OF_COLOR));
                System.out.println("Green: " + color.getGreen());
                int green = (color.getGreen() / (256 / controller.LEVELS_OF_COLOR));
                int blue = (color.getBlue() / (256 / controller.LEVELS_OF_COLOR));
                System.out.println(blue);
                int c2 = (((green & 0x0007) << 5) | (blue & 0x001F));
                int c1 = (((red & 0x1F) << 2) | (green & 0x18 >>> 3));

                // if (r == 0 && c == 0) {
                System.out.println((int) c1 + " - " + (int) c2);
                // }
                byte[] b = { (byte) (c1 - 128), (byte) (c2 - 128) };
                try {
                    getMicrocontroller().getCommunicator().getWriter().getOutputStream().write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PartyToolkit.sleep(15);
                // getMicrocontroller().sendMsg((char) c2 + "");
                PartyToolkit.sleep(15);
            }
        }
    }
}
