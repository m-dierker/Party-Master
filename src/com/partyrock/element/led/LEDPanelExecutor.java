package com.partyrock.element.led;

import java.io.IOException;

import org.eclipse.swt.graphics.Color;

import com.partyrock.comm.CommListener;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.element.ElementExecutor;

public class LEDPanelExecutor extends ElementExecutor implements CommListener {
    private LEDPanelController controller;

    public LEDPanelExecutor(LEDPanelController controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public void setMicrocontroller(Microcontroller uc) {
        super.setMicrocontroller(uc);
        uc.getCommunicator().addListener(this);
    }

    @SuppressWarnings("static-access")
    public void transmit() {
        if (getMicrocontroller() == null) {
            return;
        }

        // Sync byte
        // byte[] sync = { 0 };
        // try {
        // getMicrocontroller().getCommunicator().getWriter().getOutputStream().write(sync);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        for (int r = 0; r < controller.getPanelHeight(); r++) {
            for (int c = 0; c < controller.getPanelWidth(); c++) {
                Color color = controller.getColor(r, c);
                int red = (color.getRed() / (256 / controller.LEVELS_OF_COLOR));
                // System.out.println("Green: " + color.getGreen());
                int green = (color.getGreen() / (256 / controller.LEVELS_OF_COLOR));
                int blue = (color.getBlue() / (256 / controller.LEVELS_OF_COLOR));
                // System.out.println(blue);
                int c2 = (((green & 0x0007) << 5) | (blue & 0x001F));
                int c1 = (((red & 0x1F) << 2) | (green & 0x18 >>> 3));

                if (c1 == 128) {
                    c1 = 129;
                }

                if (c2 == 128) {
                    c2 = 129;
                }

                // if (r == 0 && c == 0) {
                // System.out.println((int) c1 + " - " + (int) c2);
                // }
                byte[] b = { (byte) (c1 - 128), (byte) (c2 - 128) };
                try {
                    getMicrocontroller().getCommunicator().getWriter().getOutputStream().write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // PartyToolkit.sleep(13);
                // getMicrocontroller().sendMsg((char) c2 + "");
                // PartyToolkit.sleep(13);
            }
        }
    }

    @Override
    public void onCommMessage(String msg) {
        System.out.println("(LED Driver) --> " + msg);
    }
}
