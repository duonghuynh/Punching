package test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import javax.comm.*;

public class SimpleRead implements Runnable, SerialPortEventListener {

    static CommPortIdentifier portId;
    static Enumeration portList;

    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;
    Thread readThread;

    public static void main(String[] args) {
        System.out.println(Integer.toHexString(222222222));
        portList = CommPortIdentifier.getPortIdentifiers();
        SimpleRead reader = null;

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("getPortType(): " + portId.getPortType() + " - getName(): " + portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM16")) {
                    System.out.println("Listen on: " + portId.getName());
                    // if (portId.getName().equals("/dev/term/a")) {
                    reader = new SimpleRead();
                }
            }
        }

        Scanner lvScanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print("\r\n>> ");
                String cmd = lvScanner.nextLine();
                if (reader != null) {
                    System.out.println("Write to COM: " + cmd);
                    // reader.write(cmd);

                    reader.sendENQ();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("============================================================================");
                    // send data					
                    reader.sendReadMsg();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        lvScanner.close();
    }

    /**
     * @param reader
     */
    private void sendReadMsg() {
        byte buff[] = new byte[11];
        buff[0] = 0x02; // STX
        buff[1] = 0x30; // CMD read
        buff[2] = 0x31;
        buff[3] = 0x30;
        buff[4] = 0x46;
        buff[5] = 0x36;
        buff[6] = 0x30;
        buff[7] = 0x31;
        buff[8] = 0x03; // ETX
        buff[9] = 0x37;
        buff[10] = 0x34;

        System.out.println(new String(buff));
        write(buff, 11);
    }

    private void sendReadMsg2() {
        byte buff[] = "0230313046363031033734".getBytes();
        write(buff, buff.length);
    }

    /**
     * @param reader
     * @return
     */
    private void sendENQ() {
        byte buff[] = new byte[1];
        buff[0] = 0x05;
        write(buff, 1);
    }

    public SimpleRead() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {
            System.out.println(e);
        }
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            System.out.println(e);
        }
        serialPort.notifyOnDataAvailable(true);
        try {
            // serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            // MSComm1.Settings = "9600,e,7,1"
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_7, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(e);
        }
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
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
                        String hexStr = bytesToHex(readBuffer, numBytes);
                        System.out.println("RCV: " + hexStr);
                        System.out.println("   : " + new String(readBuffer, 0, numBytes));
                    }
                    // System.out.println("Read: " + new String(readBuffer));
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;
        }
    }

    public void write(String data) {
        try {
            outputStream.write(data.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean write(byte data[], int length) {
        System.out.println("PC> " + bytesToHex(data, length));
        try {
            outputStream.write(data, 0, length);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes, int length) {
        StringBuffer sb = new StringBuffer();
        char[] hexChars = new char[length * 2];
        for (int j = 0; j < length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            sb.append(hexChars[j * 2]);
            sb.append(hexChars[j * 2 + 1]);
            sb.append("H ");
        }
        // return new String(hexChars);
        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
