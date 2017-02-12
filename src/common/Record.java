package common;

public class Record {
    private int recordId;
    private int doctorId;
    private int nurseId;
    private int divisionId;
    private int patientId;

    private String data;

    private String eventLog;

    public Record(int recordId, int doctorId, int nurseId, int divisionId, int patientId, String data) {
        this.recordId = recordId;
        this.doctorId = doctorId;
        this.nurseId = nurseId;
        this.divisionId = divisionId;
        this.patientId = patientId;
        this.data = data;
        eventLog = "";
    }

    public int getRecordId() {
        return recordId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getNurseId() {
        return nurseId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getData() {
        return data;
    }

    public void changeData(String data) {
        this.data = data;
    }

    public void recordEvent(String event) {
        eventLog += event + '\n';
    }
}
