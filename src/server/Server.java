package server;

import java.io.IOException;
import java.net.ServerSocket;
import javax.net.ssl.SSLSocket;

public class Server implements Runnable {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //Security stuff performed here before socket is sent to a new session for communication with client.
    public void run() {
        try {
            while (!Thread.interrupted()) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                new Thread(new Session(socket)).start();
            }
        } catch (IOException ie) {
            return;//Do something else here?
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Server Port");
            System.exit(1);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Usage: Server Port");
            System.exit(1);
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Failed to create server socket on port " + port);
            System.exit(1);
        }
        new Thread(new Server(serverSocket)).start();

        Thread hook = new Thread(new Closer(serverSocket));
        Runtime.getRuntime().addShutdownHook(hook);
    }

    private static class Closer implements Runnable {
        ServerSocket serverSocket;
        public Closer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }
        public void run() {
        	DatabaseAccess.getInstance().saveDatabase();
            try {
                serverSocket.close();
            } catch (IOException ie) {
                //whatever
            }
        }
    }
}
