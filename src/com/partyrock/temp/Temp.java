package com.partyrock.temp;

import com.partyrock.comm.CommListener;
import com.partyrock.comm.serial.SerialCommunicator;
import com.partyrock.tools.PartyToolkit;

/**
 * This is a quick file for testing whatever, including that the git repository is working correctly. Feel free to test
 * anything in this file.
 * 
 * @author Matthew
 */
public class Temp implements CommListener {
    public static void main(String... args) throws Exception {
        new Temp();

    }

    public Temp() throws Exception {
        SerialCommunicator comm = new SerialCommunicator("/dev/tty.usbmodemfa131");
        // SerialPort port = new SerialPort("/dev/tty.usbmodemfa131");
        // port.openPort();
        // port.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
        // SerialPort.PARITY_NONE);
        comm.addListener(this);
        comm.connect();
        PartyToolkit.sleep(5000);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                // char ch = 0;
                // ch |= (r << 5);
                // ch |= (c);
                // comm.sendMsg(ch + "");
                // PartyToolkit.sleep(5);
                byte[] b = { 124 - 128, 128 - 128 };
                comm.getWriter().getOutputStream().write(b);
                // port.writeBytes(((char) 124 + "").getBytes());
                PartyToolkit.sleep(15);
                // port.writeBytes(((char) 0 + "").getBytes());
                // comm.sendMsg((char) 0xE0 + "");
                PartyToolkit.sleep(15);
                // comm.sendMsg("a");
                // port.writeBytes(((char) 124 + (char) 0 + "").getBytes());
                // PartyToolkit.sleep(40);
                // PartyToolkit.sleep(1);
            }
        }

        // port.clo

        // char c1 = 124;
        // char c2 = 0;
        // int c = (c1 << 8) | c2;
        // char red = (char) (((c & 0x7C00) >> 10) * 8);
        // char green = (char) (((c & 0x03E0) >> 5) * 8);
        // char blue = (char) ((c & 0x001F) * 8);
        //
        // System.out.println((int) red + ", " + (int) green + ", " + (int) blue);

        // while (true) {
        // comm.sendMsg((char) 49 + "");
        // PartyToolkit.sleep(1000);
        // comm.sendMsg((char) 48 + "");
        // PartyToolkit.sleep(1000);
        // }
    }

    @Override
    public void onCommMessage(String msg) {
        System.out.println("<-- " + msg);

    }
}
