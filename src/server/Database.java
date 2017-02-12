package server;

import common.*;
import java.util.*;

/**
 * Handles data
 */
public class Database {
    private HashMap<Integer,Record> dbMap;

    /**
     * Create empty database
     */
    public Database() {
        dbMap = new HashMap<Integer,Record>();
    }

    /**
     * Create database and load data from file
     */
    public Database(String path) {
        dbMap = new HashMap<Integer,Record>();
        //TODO: Load data from path
    }

    public synchronized Record getRecord(int recordId) {
        return dbMap.get(recordId);
    }

    public synchronized void addRecord(Record record) {
        int recordId = (int) Math.random()*100000;
        while (dbMap.containsKey(recordId)) {
            recordId = (int) Math.random()*100000;
        }

        record = new Record(recordId, record.getDoctorId(), record.getNurseId(), record.getDivisionId(), record.getPatientId(), record.getData());
        dbMap.put(recordId, record);
    }

    public synchronized void deleteRecord(int recordId) {
        dbMap.remove(recordId);
    }

    public synchronized Integer[] getRecordIdFromPatientId(int patientId) {
        ArrayList<Integer> recordIdList = new ArrayList<Integer>();

        for (Map.Entry<Integer,Record> e : dbMap.entrySet()) {
            if (e.getValue().getPatientId() == patientId) {
                recordIdList.add(e.getValue().getRecordId());
            }
        }

        return (Integer[]) recordIdList.toArray();
    }


    public synchronized Integer[] getRecordIdFromDoctorId(int doctorId) {
        ArrayList<Integer> recordIdList = new ArrayList<Integer>();

        for (Map.Entry<Integer,Record> e : dbMap.entrySet()) {
            if (e.getValue().getDoctorId() == doctorId) {
                recordIdList.add(e.getValue().getRecordId());
            }
        }

        return (Integer[]) recordIdList.toArray();
    }


    public synchronized Integer[] getRecordIdFromNurseId(int nurseId) {
        ArrayList<Integer> recordIdList = new ArrayList<Integer>();

        for (Map.Entry<Integer,Record> e : dbMap.entrySet()) {
            if (e.getValue().getNurseId() == nurseId) {
                recordIdList.add(e.getValue().getRecordId());
            }
        }

        return (Integer[]) recordIdList.toArray();
    }


    public synchronized Integer[] getRecordIdFromDivisionId(int divisionId) {
        ArrayList<Integer> recordIdList = new ArrayList<Integer>();

        for (Map.Entry<Integer,Record> e : dbMap.entrySet()) {
            if (e.getValue().getDivisionId() == divisionId) {
                recordIdList.add(e.getValue().getRecordId());
            }
        }

        return (Integer[]) recordIdList.toArray();
    }
}
