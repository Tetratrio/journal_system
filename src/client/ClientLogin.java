package client;

import javax.net.ssl.SSLSocket;

import client.gui.GUI;

//Security stuff performed here before socket is sent to an instance of Client for communication with the server.
public class ClientLogin {

	
	public static void main(String[] args) {
		//args need to include at least port and IP of server. Maybe more info? dunno
		//Start a new clientlogin process here.
		
		SSLSocket dummySocket = null;
		new GUI(new Client(dummySocket));
	}
}
