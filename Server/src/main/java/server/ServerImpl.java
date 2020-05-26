/*ServerImpl conatins all the business logic*/
package server;

import model.*;
import network.NetworkHelper;

import java.io.*;

public class ServerImpl {
    private NetworkData networkData = null;
    private NetworkHelper networkHelper = null;
    private static Listener listener = null;
    public static String userName="", password = "";
    private static ImageChunksMetaData[] arrImagesChunkData = null;
    File f;
    FileOutputStream fo = null;

    public ServerImpl(Listener listener) {
        ServerImpl.listener = listener;
    }


    /*Init the UDP server*/
    protected void initServer() {
        NetworkData networkData = setNetworkData();
        networkHelper = new NetworkHelper(networkData);
        networkHelper.initConnection();
        listener.onServerInitializedSuccessfully();
    }

    /*All the requests are handled in a new thread
    * According to the current state of communication with client server will respond
    * Current server writes the images when its gets the byte[]*/
    protected void receiveDataFromClient() {
        new Thread(() -> {
            try {
                while (true) {
                    Object receivedObj = networkHelper.receiveDataFromClient();
                    if (receivedObj == null) return; //TODO Ask for retransmission

                    PacketAck packetAck = new PacketAck();
                    if (receivedObj instanceof EstablishConnection) {
                        EstablishConnection establishConnection = (EstablishConnection) receivedObj;
                        packetAck.setClientId((establishConnection.getClientId()));
                        packetAck.setSeqNo(establishConnection.getSeqNo());
                        packetAck.setTransmissionType(establishConnection.getTransmissionType());
                System.out.println("EstablishConnection received\n");
                        listener.onDataReceivedFromClient(packetAck);
                        userName = establishConnection.getProjectName();
                        password = establishConnection.getProjectPassword();
                        //TODO save EstablishConnection data on server side
                    } else if (receivedObj instanceof ImageMetaData) {
                        ImageMetaData imageMetaData = (ImageMetaData) receivedObj;
                        packetAck.setClientId((imageMetaData.getClientId()));
                        packetAck.setSeqNo(imageMetaData.getSeqNo());
                        packetAck.setTransmissionType(imageMetaData.getTransmissionType());
                System.out.println("ImageMetaData received\n");
                        arrImagesChunkData = imageMetaData.getArrImageChunks();
                        listener.onDataReceivedFromClient(packetAck);
                        //TODO save ImageMetaData data on server side
                    } else if (receivedObj instanceof DataTransfer) {
                        DataTransfer dataTransfer = (DataTransfer) receivedObj;
                        packetAck.setClientId((dataTransfer.getClientId()));
                        packetAck.setSeqNo(dataTransfer.getSeqNo());
                        packetAck.setTransmissionType(dataTransfer.getTransmissionType());
                        packetAck.setIsLastPacket(dataTransfer.getIsLastPacket());

                        try {
                            if (dataTransfer.getIsFirstPacketOfImageBlock() == 1) {
                                f = new File(arrImagesChunkData[dataTransfer.getCurrentImageSeqNo() - 1].getImageName());
                                fo = new FileOutputStream(f);
                            }
                            fo.write(dataTransfer.getArrImage());
                            listener.onDataReceivedFromClient(packetAck);
                            System.out.println("DataTransfer received\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (dataTransfer.getIsLastPacket() == 1) {
                            listener.onDataReceivedFromClient(packetAck);
                        }
                        //TODO save/update DataTransfer data on server side
                    } else {
                        //TODO object corrupt or not identified.
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    /*Converting the model to byte array*/
    protected void sendAckToClient(PacketAck packetAck) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(outputStream);
            os.writeObject(packetAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkHelper.sendAckToClient(outputStream.toByteArray());
    }

    public static ImageChunksMetaData[] getArrImagesChunkData() {
        return arrImagesChunkData;
    }

    interface Listener {

        void onServerInitializedSuccessfully();

        void onDataReceivedFromClient(PacketAck packetAck);

    }

    private static NetworkData setNetworkData() {
        NetworkData networkData = new NetworkData();
        networkData.setPortNumber(5555);
        return networkData;
    }





}
