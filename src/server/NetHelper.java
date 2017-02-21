package server;

import java.io.*;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;

public class NetHelper {
	
	public static ServerSocketFactory getServerSocketFactory(String keystore, String truststore, String password) {
    	SSLServerSocketFactory ssf = null;
    	try {
    		SSLContext ctx = SSLContext.getInstance("TLS");
    		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    		KeyStore ks = KeyStore.getInstance("JKS");
    		KeyStore ts = KeyStore.getInstance("JKS");
    		char[] passwordArray = password.toCharArray();
    		
    		ks.load(new FileInputStream(keystore), passwordArray);
    		ts.load(new FileInputStream(truststore), passwordArray);
    		
    		kmf.init(ks, passwordArray);
    		tmf.init(ts);
    		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    		
    		ssf = ctx.getServerSocketFactory();    		
    	} catch (Exception e) {
    		System.out.println("Failed to start server.");
    		System.out.println("Error message: " + e.getMessage());
    		System.out.println("Shutting down server...");
    		System.exit(1);
    	}
    	return ssf;
    }
	
	public static String getCertField(String cert, String field) {
		char[] charArray = cert.toCharArray();
		int startIndex, endIndex;
		field = field + "=";
		
		startIndex = cert.indexOf(field);
		startIndex += field.length();
		
		for (endIndex = startIndex; charArray[endIndex] != ','; ++endIndex) ;
		
		return cert.substring(startIndex, endIndex);
	}
}
