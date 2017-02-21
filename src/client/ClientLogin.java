package client;

import java.io.*;
import java.security.KeyStore;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.*;

import client.gui.ClientLoginGUI;
import client.gui.GUI;
import common.AccessDeniedException;

//Security stuff performed here before socket is sent to an instance of Client for communication with the server.
public class ClientLogin {
	private static final String CLIENT_CERT_PATH = "data/certificates/client/";
	private String address;
	private int port;
	
	public ClientLogin(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public void loginPrompt() {
		new ClientLoginGUI(this);
	}

	public void login(String username, String password) throws AccessDeniedException {
		
		if (!usernameExists(username)) {
			throw new AccessDeniedException();
		}
		
		String keystore, truststore;
		
		keystore = CLIENT_CERT_PATH + username + "keystore";
		truststore = CLIENT_CERT_PATH + username + "truststore";
		
		char[] passwordCharArray = password.toCharArray();
		
		SSLSocketFactory factory = null;
		SSLSocket socket = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore ts = KeyStore.getInstance("JKS");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			SSLContext ctx = SSLContext.getInstance("TLS");
			ks.load(new FileInputStream(keystore), passwordCharArray);
			ts.load(new FileInputStream(truststore), passwordCharArray);
			kmf.init(ks, passwordCharArray);
			tmf.init(ts);
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			factory = ctx.getSocketFactory();
			
			socket = (SSLSocket)factory.createSocket(address, port);
			socket.startHandshake(); //Is this needed?
		} catch (IOException ie) {
			if (ie.getCause() instanceof UnrecoverableKeyException) {
				throw new AccessDeniedException();
			} else {
				System.out.println("Failed to connect to server.");
	    		System.out.println("Error message: " + ie.getMessage());
	    		System.out.println("Shutting down client...");
	    		System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to connect to server.");
    		System.out.println("Error message: " + e.getMessage());
    		System.out.println("Shutting down client...");
    		System.exit(1);
		}
		new GUI(new Client(socket));
	}
	
	private boolean usernameExists(String username) {
		if (!username.matches("[0-9]+") && !username.equals("-")) {
			return false;
		}
		try {
			File folder = new File("data/certificates/client");
			File[] listOfFiles = folder.listFiles();
			
			for (int i = 0; i < listOfFiles.length; ++i) {
				if (listOfFiles[i].getName().contains(username)) {
					return true;
				}
			}
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
            System.out.println("Usage: Java -jar Client.jar [Address] [Port]");
            System.exit(1);
        }
		String address = args[0];
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Usage: Java -jar Client.jar [Address] [Port]");
            System.exit(1);
        }
        new ClientLogin(address, port).loginPrompt();
	}
}
