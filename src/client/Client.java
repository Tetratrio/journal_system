package client;

import common.*;
import java.io.*;
import javax.net.ssl.SSLSocket;

public class Client {
	private SSLSocket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
	
    public Client(SSLSocket socket) {
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ie) {
            connectionError(ie);
        }
    }

    public void createRecord(Record record) throws AccessDeniedException {
        try {
            oos.writeByte(Protocol.CREATE);
            oos.writeObject(record);
            byte result = ois.readByte();
            if (result == Protocol.ACCESS_DENIED) {
                throw new AccessDeniedException();
            }
        } catch (Exception e) {
            connectionError(e);
        }
    }

    public Record getRecord(int recordId) throws AccessDeniedException {
        Record record = null;
        try {
            oos.writeByte(Protocol.READ);
            oos.writeInt(recordId);
            byte result = ois.readByte();
            if (result == Protocol.ACCESS_DENIED) {
                throw new AccessDeniedException();
            }
            record = (Record) ois.readObject();
        } catch (Exception e) {
            connectionError(e);
        }
        return record;
    }

    public void setRecord(int recordId, String data) throws AccessDeniedException {
        try {
            oos.writeByte(Protocol.WRITE);
            oos.writeInt(recordId);
            oos.writeObject(data);
            byte result = ois.readByte();
            if (result == Protocol.ACCESS_DENIED) {
                throw new AccessDeniedException();
            }
        } catch (Exception e) {
            connectionError(e);
        }
    }

    public void deleteRecord(int recordId) throws AccessDeniedException {
        try {
            oos.writeByte(Protocol.DELETE);
            oos.writeInt(recordId);
            byte result = ois.readByte();
            if (result == Protocol.ACCESS_DENIED) {
                throw new AccessDeniedException();
            }
        } catch (Exception e) {
            connectionError(e);
        }
    }

    public int[] getRecordIdFromPatientId(int patientId) {
        int [] recordIdList = null;
        try {
            oos.writeByte(Protocol.PATIENTID_LIST);
            oos.writeInt(patientId);
            recordIdList = getIntArray();
        } catch (Exception e) {
            connectionError(e);
        }
        return recordIdList;
    }

    public int[] getRecordIdFromDoctorId(int doctorId) {
        int [] recordIdList = null;
        try {
            oos.writeByte(Protocol.DOCTORID_LIST);
            oos.writeInt(doctorId);
            recordIdList = getIntArray();
        } catch (Exception e) {
            connectionError(e);
        }
        return recordIdList;
    }

    public int[] getRecordIdFromNurseId(int nurseId) {
        int [] recordIdList = null;
        try {
            oos.writeByte(Protocol.NURSEID_LIST);
            oos.writeInt(nurseId);
            recordIdList = getIntArray();
        } catch (Exception e) {
            connectionError(e);
        }
        return recordIdList;
    }


    public int[] getRecordIdFromDivisionId(int divisionId) {
        int [] recordIdList = null;
        try {
            oos.writeByte(Protocol.DIVISIONID_LIST);
            oos.writeInt(divisionId);
            recordIdList = getIntArray();
        } catch (Exception e) {
            connectionError(e);
        }
        return recordIdList;
    }


    private int[] getIntArray() throws IOException {
        int size = ois.readInt();
        int[] intArray = new int[size];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = ois.readInt();
        }
        return intArray;
    }

    private void connectionError(Exception e) {
        System.out.println("Connection problems");
        System.out.println(e.getMessage());
        try {
            socket.close();
        } catch (Exception ee) {
            //do nothing
        }
        System.exit(1);//Close client
    }
}
