package client;

import javax.net.ssl.SSLSocket;

import client.gui.ClientLoginGUI;
import client.gui.GUI;
import common.AccessDeniedException;

//Security stuff performed here before socket is sent to an instance of Client for communication with the server.
public class ClientLogin {
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
		// Do login, throw new AccessDeniedException() if username/password incorrect
		
		//dummy code for error removal, remove this!
		int i = 0;
		if (i == 0) throw new AccessDeniedException();
		//End of dummy code
		
		SSLSocket socket = null;//Get the SSL socket
		
		new GUI(new Client(socket));
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
