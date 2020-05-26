package network;

import model.NetworkData;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkHelper {

    private static NetworkData networkData = null;
    private static DatagramSocket socket = null;
    private static InetAddress address = null;
    private static DatagramPacket receivedDataPacket = null;
    public static final int MAX_BUFFER_SIZE = 65507;
    private static boolean IS_SERVER_RUNNING = false;

    public NetworkHelper(NetworkData networkData) {
        NetworkHelper.networkData = networkData;
    }

    public void initConnection() {
        try {
            socket = new DatagramSocket(networkData.getPortNumber());
            IS_SERVER_RUNNING = true;
        } catch (IOException e) {
            IS_SERVER_RUNNING = false;
            e.printStackTrace();
        }
    }


    public Object receiveDataFromClient() {
        Object object = null;
        byte[] arrData = new byte[MAX_BUFFER_SIZE];
        receivedDataPacket = new DatagramPacket(arrData, MAX_BUFFER_SIZE);
        try {
            socket.receive(receivedDataPacket);
            arrData = receivedDataPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(arrData);
            ObjectInputStream is = new ObjectInputStream(in);
            object = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void sendAckToClient(byte[] arrAck) {
        try {
            receivedDataPacket = new DatagramPacket(arrAck, arrAck.length, receivedDataPacket.getAddress(), receivedDataPacket.getPort());
            socket.send(receivedDataPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isServerRunning() {
        return IS_SERVER_RUNNING && socket != null;
    }

}
