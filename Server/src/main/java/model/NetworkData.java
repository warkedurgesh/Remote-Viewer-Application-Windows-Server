package model;

public class NetworkData {

    private String clientName;
    private int portNumber;
    private String fileName;
    private int noOfPartition;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoOfPartition() {
        return noOfPartition;
    }

    public void setNoOfPartition(int noOfPartition) {
        this.noOfPartition = noOfPartition;
    }
}
