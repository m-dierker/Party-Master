package com.partyrock.comm.uc;

import com.partyrock.comm.Communicator;
import com.partyrock.comm.serial.SerialCommunicator;
import com.partyrock.settings.SectionSettings;

/**
 * An arduino directly attached to the computer we will communicate with via serial on 9600
 * 
 * @author Matthew
 * 
 */
public class LocalArduino extends Microcontroller {

    private String serialPort;
    private SerialCommunicator communicator;
    private boolean isValid;

    /**
     * Constructs the local arduino, and attempts to establish a serial connection immediately
     * 
     * @param name
     * @param port
     */
    public LocalArduino(String name, String internalID, String port) {
        super(name, internalID);

        establishNewConnectionToPort(port);
    }

    @Override
    public Communicator getCommunicator() {
        return communicator;
    }

    public String getPort() {
        return serialPort;
    }

    @Override
    public MicrocontrollerType getType() {
        return MicrocontrollerType.LOCAL_ARDUINO;
    }

    @Override
    public void establishNewConnectionToPort(String port) {
        if (communicator != null) {
            communicator.disconnect();
        }

        this.serialPort = port;

        communicator = new SerialCommunicator(port);

        if (!(this.isValid = communicator.connect())) {
            System.err.println("Unable to make a successful connection to the Arduino on port " + port);
        }
    }

    public void saveChildData(SectionSettings settings) {
        settings.put("mc_class", "LocalArduino");
        settings.put("ard_port", getPort());
    }
}
