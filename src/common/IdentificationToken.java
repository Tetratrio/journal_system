package common;

public class IdentificationToken {
    public static final int PATIENT = 0;
    public static final int NURSE = 1;
    public static final int DOCTOR = 2;
    public static final int GOVERNMENT = 3;

    private int role;
    private int id;
    private int division;

    public IdentificationToken(int role, int id) {
        this.role = role;
        this.id = id;
    }

    public IdentificationToken(int role, int id, int division) {
        this.role = role;
        this.id = id;
        this.division = division;
    }

    public int getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public int getDivisionId() {
        return division;
    }

    public String toString() {
        String id = null;
        switch (this.role) {
            case PATIENT:
                id = "Patient " + id;
                break;
            case DOCTOR:
                id = "Doctor " + id;
                break;
            case NURSE:
                id = "Nurse " + id;
                break;
            case GOVERNMENT:
                id = "Govenrment agency";
                break;
        }

        return id;
    }
}
