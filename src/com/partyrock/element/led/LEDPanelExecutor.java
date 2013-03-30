package com.partyrock.element.led;

import java.io.IOException;

import org.eclipse.swt.graphics.Color;

import com.partyrock.comm.CommListener;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.config.PartyConstants;
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

    public void sendPixel(int r, int c) {
        byte pos_byte = (byte) ((r << 4) | c);

        // Sync byte and position byte
        byte[] sync = { 0, pos_byte };
        try {
            getMicrocontroller().getCommunicator().getWriter().getOutputStream().write(sync);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Color bytes
        Color color = controller.getColor(r, c);

        int red = (color.getRed() / (256 / LEDPanelController.LEVELS_OF_COLOR));
        int green = (color.getGreen() / (256 / LEDPanelController.LEVELS_OF_COLOR));
        int blue = (color.getBlue() / (256 / LEDPanelController.LEVELS_OF_COLOR));
        int c2 = (((green & 0x0007) << 5) | (blue & 0x001F));
        int c1 = (((red & 0x1F) << 2) | ((green & 0x0018) >> 3));

        if (r == 4 && c == 1) {
            System.out.println(Integer.toBinaryString(c1));
            System.out.println(red);
        }

        if (red <= PartyConstants.LED_PANEL_BLACK_THRESHOLD && green <= PartyConstants.LED_PANEL_BLACK_THRESHOLD
                && blue <= PartyConstants.LED_PANEL_BLACK_THRESHOLD) {
            System.out.println("Blackout! on " + r + ", " + c);
            red = green = blue = 0;
        }

        if (c1 == 128) {
            System.out.println("special case");
            c1 = 129;
        }

        if (c2 == 128) {
            System.out.println("special case");
            c2 = 129;
        }

        byte[] b = { (byte) (c1 - 128), (byte) (c2 - 128) };

        try {
            getMicrocontroller().getCommunicator().getWriter().getOutputStream().write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transmit() {
        if (getMicrocontroller() == null) {
            return;
        }
    }

    @Override
    public void onCommMessage(String msg) {
        // System.out.println("(LED Driver) --> " + msg);
    }
}
