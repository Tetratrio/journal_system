package server;

import common.*;

import java.io.BufferedReader;
import java.io.FileReader;
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
        loadData(path);
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
    
    private void loadData(String path) {
    	BufferedReader br = null;
    	try {
    		br = new BufferedReader(new FileReader(path));
    		while(true) {
    			int recordId, patientId, divisionId, doctorId, nurseId;
    			String data, eventLog;
    			StringBuilder sb;
    			String line = null;
    			line = br.readLine();
    			if (line == null) {
    				br.close();
    				return; // No more entries to read.
    			}
    			recordId = Integer.parseInt(line);
    			patientId = Integer.parseInt(br.readLine());
    			divisionId = Integer.parseInt(br.readLine());
    			doctorId = Integer.parseInt(br.readLine());
    			nurseId = Integer.parseInt(br.readLine());
    			sb = new StringBuilder();
    			line = null;
    			while (!(line = br.readLine()).equals("%")) {
    				sb.append(line + '\n');
    			}
    			data = sb.toString();
    			sb = new StringBuilder();
    			line = null;
    			while (!(line = br.readLine()).equals("%")) {
    				sb.append(line + '\n');
    			}
    			eventLog = sb.toString();
    			if (eventLog.length() > 0) {
    				eventLog = eventLog.substring(0, eventLog.length() - 1);
    			}
    			Record record = new Record(recordId, doctorId, nurseId, divisionId, patientId, data);
    			record.recordEvent(eventLog);
    			dbMap.put(recordId, record);
    		}
    	} catch (Exception e) {
    		System.out.println("Error when reading data from fiel.");
    		System.out.println(e.getMessage());
    		System.out.println("Exiting...");
    		System.exit(1);
    	}
    }

    //Fill this method if saving of data between runs of server is needed.
	public void save() {
		//If we ever want our data to be saved between different startups.
	}
}
