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
        this.token = new IdentificationToken(socket);
        dbAccess = DatabaseAccess.getInstance();
    }
    public void run() {
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ie) {
            connectionError(ie);
            return;
        }

        try {
            while (!this.isInterrupted()) {
            	try {
            		oos.reset(); //Throw away cached objects since they might change in the server.
                    byte request = ois.readByte();
                    
                    int recordId = 0;
                    Record record = null;
                    Integer[] recordIdList = null;

                    switch (request) {
                        case Protocol.CREATE:
                            record = (Record) ois.readObject();
                            dbAccess.createRecord(token, record);
                            oos.writeByte(Protocol.ACCESS_GRANTED);
                            oos.flush();
                            break;
                        case Protocol.READ:
                            recordId = ois.readInt();
                            record = dbAccess.readRecord(token, recordId);
                            oos.writeByte(Protocol.ACCESS_GRANTED);
                            oos.writeObject(record);
                            oos.flush();
                            break;
                        case Protocol.WRITE:
                            recordId = ois.readInt();
                            String data = (String) ois.readObject();
                            dbAccess.writeRecord(token, recordId, data);
                            oos.writeByte(Protocol.ACCESS_GRANTED);
                            oos.flush();
                            break;
                        case Protocol.DELETE:
                            recordId = ois.readInt();
                            dbAccess.deleteRecord(token, recordId);
                            oos.writeByte(Protocol.ACCESS_GRANTED);
                            oos.flush();
                            break;
                        case Protocol.PATIENTID_LIST:
                            int patientId = ois.readInt();
                            recordIdList = dbAccess.getRecordIdFromPatientId(patientId);
                            oos.writeInt(recordIdList.length);
                            for (Integer i : recordIdList) {
                                oos.writeInt(i);
                            }
                            oos.flush();
                            break;
                        case Protocol.DOCTORID_LIST:
                            int doctorId = ois.readInt();
                            recordIdList = dbAccess.getRecordIdFromDoctorId(doctorId);
                            oos.writeInt(recordIdList.length);
                            for (Integer i : recordIdList) {
                                oos.writeInt(i);
                            }
                            oos.flush();
                            break;
                        case Protocol.NURSEID_LIST:
                            int nurseId = ois.readInt();
                            recordIdList = dbAccess.getRecordIdFromNurseId(nurseId);
                            oos.writeInt(recordIdList.length);
                            for (Integer i : recordIdList) {
                                oos.writeInt(i);
                            }
                            oos.flush();
                            break;
                        case Protocol.DIVISIONID_LIST:
                            int divisionId = ois.readInt();
                            recordIdList = dbAccess.getRecordIdFromDivisionId(divisionId);
                            oos.writeInt(recordIdList.length);
                            for (Integer i : recordIdList) {
                                oos.writeInt(i);
                            }
                            oos.flush();
                            break;
                    }
            	} catch (AccessDeniedException ae) {
            		oos.writeByte(Protocol.ACCESS_DENIED);
                    oos.flush();
            	}
            	
            }
        } catch (IOException e) {
            connectionError(e);
            return;
        } catch (ClassNotFoundException ce) {
            System.out.println("Class not found exception recieved");
            System.out.println("Printing info...");
            System.out.println(ce.getMessage());
            System.out.println("Closing server...");
            System.exit(1);
        }
    }

    private void connectionError(Exception e) {
        System.out.println("Server: Lost connection to client " + token.toString());
        System.out.println("Closing session for " + token.toString());
        try {
            socket.close();
        } catch (Exception ee) {
            //do nothing
        }
        return;
    }
}
