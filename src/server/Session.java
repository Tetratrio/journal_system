package server;

import common.*;
import javax.net.ssl.*;
import java.io.*;

public class Session extends Thread {
    private SSLSocket socket;
    private IdentificationToken token;
    private DatabaseAccess dbAccess;

    public Session(SSLSocket socket) {
        this.socket = socket;
        //Create new id token from info found in client certificate
        dbAccess = DatabaseAccess.getInstance();
    }
    public void run() {
            ObjectInputStream ois = null;

            ObjectOutputStream oos = null;

        try {
            ois = new ObjectInputStream(socket.getInputStream());

            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ie) {
            connectionError(ie);
            return;
        }

        try {
            while (!this.isInterrupted()) {
                byte request = ois.readByte();

                int recordId = 0;
                Record record = null;
                Integer[] recordIdList = null;

                switch (request) {
                    case Protocol.CREATE:
                        record = (Record) ois.readObject();
                        dbAccess.createRecord(token, record);
                        oos.writeByte(Protocol.ACCESS_GRANTED);
                        break;
                    case Protocol.READ:
                        recordId = ois.readInt();
                        record = dbAccess.readRecord(token, recordId);
                        oos.writeByte(Protocol.ACCESS_GRANTED);
                        oos.writeObject(record);
                        break;
                    case Protocol.WRITE:
                        recordId = ois.readInt();
                        String data = (String) ois.readObject();
                        dbAccess.writeRecord(token, recordId, data);
                        oos.writeByte(Protocol.ACCESS_GRANTED);
                        break;
                    case Protocol.DELETE:
                        recordId = ois.readInt();
                        dbAccess.deleteRecord(token, recordId);
                        oos.writeByte(Protocol.ACCESS_GRANTED);
                        break;
                    case Protocol.PATIENTID_LIST:
                        int patientId = ois.readInt();
                        recordIdList = dbAccess.getRecordIdFromPatientId(patientId);
                    case Protocol.DOCTORID_LIST:
                        int doctorId = ois.readInt();
                        recordIdList = dbAccess.getRecordIdFromDoctorId(doctorId);
                    case Protocol.NURSEID_LIST:
                        int nurseId = ois.readInt();
                        recordIdList = dbAccess.getRecordIdFromNurseId(nurseId);
                    case Protocol.DIVISIONID_LIST:
                        int divisionId = ois.readInt();
                        recordIdList = dbAccess.getRecordIdFromDivisionId(divisionId);

                        oos.writeInt(recordIdList.length);
                        for (Integer i : recordIdList) {
                            oos.writeInt(i);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            connectionError(e);
            return;
        } catch (AccessDeniedException ae) {
            try {
                oos.writeByte(Protocol.ACCESS_DENIED);
            } catch (IOException iie) {
                connectionError(iie);
                return;
            }
        } catch (ClassNotFoundException ce) {
            System.out.println("Class not found exception recieved");
            System.out.println("Printing info...");
            System.out.println(ce.getMessage());
            System.out.println("Closing server...");
            System.exit(1);
        }
    }

    private void connectionError(Exception e) {
        System.out.println("Server: Connection problem");
        System.out.println(e.getMessage());
        try {
            socket.close();
        } catch (Exception ee) {
            //do nothing
        }
        return;
    }
}
