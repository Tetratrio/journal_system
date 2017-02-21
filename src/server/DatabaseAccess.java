package server;

import java.text.SimpleDateFormat;
import java.util.Date;

import common.*;

/**
 * Handles access to data
 */
public class DatabaseAccess {

    private Database db;

    /**
     * Singleton object
     */
    private static DatabaseAccess instance = null;
    public static DatabaseAccess getInstance() {
        if (instance == null) {
            instance = new DatabaseAccess();
        }
        return instance;
    }

    private DatabaseAccess() {
        db = new Database("data/database/db_save_file");
    }

    public Integer[] getRecordIdFromPatientId(int patientId) {
        return db.getRecordIdFromPatientId(patientId);
    }

    public Integer[] getRecordIdFromDoctorId(int doctorId) {
        return db.getRecordIdFromDoctorId(doctorId);
    }

    public Integer[] getRecordIdFromNurseId(int nurseId) {
        return db.getRecordIdFromNurseId(nurseId);
    }

    public Integer[] getRecordIdFromDivisionId(int divisionId) {
        return db.getRecordIdFromDivisionId(divisionId);
    }

    public void createRecord(IdentificationToken token, Record record) throws AccessDeniedException {
        if (token.getRole() != IdentificationToken.DOCTOR || token.getId() != record.getDoctorId()) {
            throw new AccessDeniedException();
        }
        db.addRecord(record);
    }

    public Record readRecord(IdentificationToken token, int recordId) throws AccessDeniedException {
        Record record = db.getRecord(recordId);

        if (token.getRole() == IdentificationToken.PATIENT) {
            if (token.getId() != record.getPatientId()) {
                throw new AccessDeniedException();
            }
        }

        if (token.getRole() == IdentificationToken.DOCTOR) {
            if (token.getId() != record.getDoctorId() && token.getDivisionId() != record.getDivisionId()) {
                throw new AccessDeniedException();
            }
        }

        if (token.getRole() == IdentificationToken.NURSE) {
            if (token.getId() != record.getNurseId() && token.getDivisionId() != record.getDivisionId()) {
                throw new AccessDeniedException();
            }
        }

        record.recordEvent("[" + getCurrentTimeStamp() + "] " + token.toString() + " : READ");
        return record;
    }

    public void writeRecord(IdentificationToken token, int recordId, String data) throws AccessDeniedException {
        Record record = db.getRecord(recordId);

        if (token.getRole() == IdentificationToken.PATIENT) {
            throw new AccessDeniedException();
        }

        if (token.getRole() == IdentificationToken.DOCTOR) {
            if (token.getId() != record.getDoctorId()) {
                throw new AccessDeniedException();
            }
        }

        if (token.getRole() == IdentificationToken.NURSE) {
            if (token.getId() != record.getNurseId()) {
                throw new AccessDeniedException();
            }
        }

        if (token.getRole() == IdentificationToken.GOVERNMENT) {
            throw new AccessDeniedException();
        }

        record.recordEvent("[" + getCurrentTimeStamp() + "] " + token.toString() + " : WRITE");
        record.changeData(data);
    }

    public void deleteRecord(IdentificationToken token, int recordId) throws AccessDeniedException {
        if (token.getRole() != IdentificationToken.GOVERNMENT) {
            throw new AccessDeniedException();
        }
        db.deleteRecord(recordId);
    }

	public void saveDatabase() {
		db.save();
	}
	
	private String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
