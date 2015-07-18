/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import org.apache.log4j.Logger;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

/**
 *
 * @author admin
 */
public class Plc implements SerialPortEventListener {

    final static Logger log = Logger.getLogger(Plc.class);

    public static char STX = 0x2;
    public static char ETX = 0x3;
    public static char EOT = 0x4;
    public static char ENQ = 0x5;
    public static char ACK = 0x6;
    public static char LF = 0xA;
    public static char CL = 0xC;
    public static char CR = 0xD;
    public static char NAK = 0x15;

    static CommPortIdentifier portId;
    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;
    boolean isReceived = false;
    String lastResponse;

    String MSG_WAIT = "3";
    String STATION_ADDRESS = "00";
    String PLC_NUM = "FF";
    String COMMAND_WRITE_WORD = "WW";

    public static Plc getInstance() {
        return PlcHolder.INSTANCE;
    }

    private static class PlcHolder {

        private static final Plc INSTANCE = new Plc();
    }

    public static String checkSum(String command) {
        long total = 0;
        for (int i = 0; i < command.length(); i++) {
            total = total + command.charAt(i);
        }
        String hexStr = Long.toHexString(total);
        return hexStr.substring(hexStr.length() - 2).toUpperCase();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String formatHexStr(int value, int length) {
        String strValue = Integer.toHexString(value).toUpperCase();
        strValue = "000000000000000000000" + strValue;
        strValue = strValue.substring(strValue.length() - length);
        return strValue.toUpperCase();
    }

    /**
     * Low word FIRST
     * @param value
     * @return 
     */
    public static String formatHex8(int value) {
        String str8Value = formatHexStr(value, 8);
        String result = str8Value.substring(4, 8) + str8Value.substring(0, 4);
        return result;
    }

    public static String formatPossition(int position){
        String strPosition = String.valueOf(position);
        strPosition = "0000" + strPosition;
        strPosition = "D" + strPosition.substring(strPosition.length() - 4);
        return strPosition;
    }
    
    public boolean isOpenComm() {
        return (outputStream != null);
    }

    public void closeComm() {
        try {
            outputStream.close();
            inputStream.close();
            serialPort.close();

            outputStream = null;
            inputStream = null;
            serialPort = null;
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public boolean openComm() {
        Setting setting = Environment.getInstance().getSetting();
        try {
            portId = CommPortIdentifier.getPortIdentifier(setting.getComPort());
        } catch (NoSuchPortException ex) {
            log.error(ex);
            return false;
        }
        try {
            serialPort = (SerialPort) portId.open("Punching", 2000);
        } catch (PortInUseException ex) {
            log.error(ex);
            return false;
        }
        try {
            // default values
            int baudRate = 9600;
            int dataLength = SerialPort.DATABITS_8;
            int stopBit = SerialPort.STOPBITS_1;
            int parity = SerialPort.PARITY_NONE;

            // from setting
            try {
                baudRate = Integer.parseInt(setting.getComSpeed());
            } catch (NumberFormatException ex) {
            }
            if (setting.getParity().equalsIgnoreCase("Even")) {
                parity = SerialPort.PARITY_EVEN;
            } else if (setting.getParity().equalsIgnoreCase("Odd")) {
                parity = SerialPort.PARITY_ODD;
            }
            if (setting.getDataLength().equalsIgnoreCase("7-bit")) {
                dataLength = SerialPort.DATABITS_7;
            }
            if (setting.getStopBit().equalsIgnoreCase("2-bit")) {
                stopBit = SerialPort.STOPBITS_2;
            }

            serialPort.setSerialPortParams(baudRate, dataLength, stopBit, parity);
        } catch (UnsupportedCommOperationException ex) {
            log.error(ex);
            return false;
        }

        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException ex) {
            log.error(ex);
            return false;
        }

        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            System.out.println(e);
        }
        serialPort.notifyOnDataAvailable(true);
        return true;
    }

    public boolean writePLC(int value, int position) {
        // format 500 as 01F4
        String strValue = formatHexStr(value, 4);

        String strPosition = formatPossition(position);

        log.info("Write: " + strPosition + "-" + value + "(Hex:" + strValue + ")");

        return writePLC(strValue, "01", strPosition);
    }

    public boolean writePLC32(int value, int position) {
        // format 500 as 000001F4
        String strValue = formatHex8(value);

        String strPosition = formatPossition(position);

        log.info("Write: " + strPosition + "-" + value + "(Hex:" + strValue + ")");

        // write 2 words
        return writePLC(strValue, "02", strPosition);
    }

    public boolean writeHole(int y, int x, int t, int position) {
        // format 500 as 000001F4
        String strValue = formatHex8(y) + formatHex8(x) + formatHexStr(t, 4);

        String strPosition = formatPossition(position);

        log.info("Write: " + strPosition + "-(" + y + "," + x + "," + t + ") Hex:" + strValue);

        // write 2 words
        return writePLC(strValue, "05", strPosition);
    }

    public boolean writePLC(String value, String length, String position) {
        String writeCmd = buildCommand(STATION_ADDRESS, PLC_NUM, COMMAND_WRITE_WORD, MSG_WAIT, position, length, value, true, 4);
        log.debug("Command: " + writeCmd);
        byte buff[] = writeCmd.getBytes();
        try {
            outputStream.write(buff);
        } catch (IOException ex) {
            log.error(ex);
        }
        return true;
    }

    public String getResponse(boolean clear) {
        String response = lastResponse;
        if (clear) {
            lastResponse = null;
            isReceived = false;
        }
        return response;
    }

    public String buildCommand(String stationAddr, String plcNum, String command,
            String msgWait, String headDevice, String numDevices, String writeData, boolean sumCheck, int protocol) {
        // Command starts with ENQ followed by Station address,
        // PLC / PC number, command type and message wait time
        String plcCommand = ENQ + stationAddr + plcNum + command + msgWait;
        if (command.equals("BR") || command.equals("WR")) {
            //Add head device & number of devices
            plcCommand = plcCommand + headDevice + numDevices;
        } else if (command.equals("BW") || command.equals("WW")) {
            //Add head device, number of devices and write data
            plcCommand = plcCommand + headDevice + numDevices + writeData;
        } else if (command.equals("BT") || command.equals("WT") || command.equals("TT")) {
            //Add Number of devices and write data
            plcCommand = plcCommand + numDevices + writeData;
        } else if (command.equals("GW")) {
            // Add write data only
            plcCommand = plcCommand + writeData;
        }

        if (sumCheck) {
            plcCommand = plcCommand + checkSum(plcCommand.substring(1)); // the command without ENQ
        }

        if (protocol == 4) {
            plcCommand = plcCommand + CR + LF;
        }
        return plcCommand;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[1000];

                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                        lastResponse = new String(readBuffer, 0, numBytes);
                        isReceived = true;
                        System.out.println("RCV: " + lastResponse);
                        log.info("RCV: " + lastResponse);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;
        }
    }

    public static void main(String args[]) {
        System.out.println(Integer.toHexString(222222222));
        System.out.println(formatHexStr(222222222, 8));
        System.out.println(formatHex8(222222222));
    }
}
