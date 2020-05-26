package model;

import java.io.Serializable;

public class PacketAck implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int clientId = -1;
    private int seqNo = -1;
    private int transmissionType = -1;
    private int isLastPacket = 0;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(int transmission_type) {
        this.transmissionType = transmission_type;
    }

    public int isLastPacket() {
        return isLastPacket;
    }

    public void setIsLastPacket(int is_last_packet) {
        this.isLastPacket = is_last_packet;
    }

    @Override
    public String toString() {
        return new StringBuffer("client_id = ")
                .append(clientId)
                .append("\n")
                .append("seq_no = ")
                .append(seqNo)
                .append("\n")
                .append("transmission_type = ")
                .append(transmissionType)
                .append("\n")
                .append("is_last_packet = ")
                .append(isLastPacket)
                .append("\n")
                .toString();
    }
}
