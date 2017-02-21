package server;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;

public class IdentificationToken {
    public static final int PATIENT = 0;
    public static final int NURSE = 1;
    public static final int DOCTOR = 2;
    public static final int GOVERNMENT = 3;

    private int role;
    private int id;
    private int division;
    
    public IdentificationToken(SSLSocket socket) {
    	
    	try {
    		SSLSession session = socket.getSession();
        	X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
        	
        	String subject = cert.getSubjectDN().getName();
        	        	
        	String roleString = NetHelper.getCertField(subject, "OU");
        	String idString = NetHelper.getCertField(subject, "CN");
        	String divisionString = NetHelper.getCertField(subject, "O");
        	
        	
        	if (roleString.equals("patient")) {
        		this.role = PATIENT;
        	} else if (roleString.equals("doctor")) {
        		this.role = DOCTOR;
        	} else if (roleString.equals("nurse")) {
        		this.role = NURSE;
        	} else if (roleString.equals("government")) {
        		this.role = GOVERNMENT;
        	}
        	        	
        	this.id = Integer.parseInt(idString);
        	
        	if (this.role != GOVERNMENT) {
        		this.division = Integer.parseInt(divisionString);
        	}
        	
    	} catch (Exception e) {
    		System.out.println("Failed getting client certificate information.");
    		System.out.println("Error message: " + e.getMessage());
    		System.out.println("Shutting down server...");
    		System.exit(1);
    	}
    	
    }

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
                id = "Patient " + this.id;
                break;
            case DOCTOR:
                id = "Doctor " + this.id;
                break;
            case NURSE:
                id = "Nurse " + this.id;
                break;
            case GOVERNMENT:
                id = "Govenrment agency";
                break;
        }

        return id;
    }
}
