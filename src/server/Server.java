package server;

import java.net.*;
import javax.net.ServerSocketFactory;
import javax.net.ssl.*;

public class Server implements Runnable {
	private static final String KEYSTORE = "data/certificates/server/serverkeystore";
	private static final String TRUSTSTORE = "data/certificates/server/servertruststore";
	private static final String PASSWORD = "password";
	
    private ServerSocket serverSocket;

    public Server(int port) {
    	try {
    		ServerSocketFactory ssf = NetHelper.getServerSocketFactory(KEYSTORE, TRUSTSTORE, PASSWORD);
            ServerSocket ss = ssf.createServerSocket(port);
            ((SSLServerSocket)ss).setNeedClientAuth(true);
            this.serverSocket = ss;
    	} catch (Exception e) {
    		System.out.println("Failed to start server.");
    		System.out.println("Error message: " + e.getMessage());
    		System.out.println("Shutting down server...");
    		System.exit(1);
    	}
    	
    }

    public void run() {
    	System.out.println("Server started, accepting incoming connections...");
        try {
            while (!Thread.interrupted()) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                new Thread(new Session(socket)).start();
            }
        } catch (Exception e) {
        
        }
        try {
        	serverSocket.close();
        } catch (Exception e) {
        	//whatever
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Java -jar Server.jar [Port]");
            System.exit(1);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Usage: Java -jar Server.jar [Port]");
            System.exit(1);
        }
        
        Thread server = new Thread(new Server(port));

        Thread hook = new Thread(new Closer(server));
        Runtime.getRuntime().addShutdownHook(hook);
        
        server.start();
    }

    private static class Closer implements Runnable {
        Thread server;
        public Closer(Thread server) {
            this.server = server;
        }
        public void run() {
        	System.out.println("Shutting down server and closing server socket...");
        	DatabaseAccess.getInstance().saveDatabase();
            try {
                server.interrupt();
            } catch (Exception e) {
                //whatever
            }
        }
    }
    
}
