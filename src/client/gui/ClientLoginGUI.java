package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientLoginGUI extends JFrame {
	
	
	private JTextField usernameTextfield;
	private JPasswordField passwordTextfield;
	
	public ClientLoginGUI() {
		createGuiWithDumpsterCode();
	}
	
	private void login() {
		
	}
	
	private void exit() {
		System.exit(0);
	}
	
	/**
	 * Classic gui dumpster code with tons of workarounds. Big mess. Ignore and move on.
	 */
	private void createGuiWithDumpsterCode() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JLabel usernameLabel = new JLabel("User ID");
		JLabel passwordLabel = new JLabel("Password");
		
		usernameTextfield = new JTextField();
		passwordTextfield = new JPasswordField();
		
		JButton loginButton = new JButton("Login");
		JButton exitButton = new JButton("Exit");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
	}
}
