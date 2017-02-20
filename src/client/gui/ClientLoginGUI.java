package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientLogin;
import common.AccessDeniedException;

@SuppressWarnings("serial")
public class ClientLoginGUI extends JFrame {
	
	private ClientLogin clientLogin;
	
	private JTextField usernameTextfield;
	private JPasswordField passwordTextfield;
	
	public ClientLoginGUI(ClientLogin clientLogin) {
		this.clientLogin = clientLogin;
		createGuiWithDumpsterCode();
	}
	
	private void login() {
		try {
			clientLogin.login(usernameTextfield.getText(), new String(passwordTextfield.getPassword()));
		} catch (AccessDeniedException ae) {
			JOptionPane.showMessageDialog(this, "Username or password is incorrect.");
			usernameTextfield.setText("");
			passwordTextfield.setText("");
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Something went wrong, try again.");
			usernameTextfield.setText("");
			passwordTextfield.setText("");
			return;
		}
		this.dispose();
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
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		buttonPanel.add(loginButton);
		buttonPanel.add(exitButton);
		
		JPanel usernamePanel = new JPanel();
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		
		usernamePanel.add(usernameLabel);
		passwordPanel.add(passwordLabel);
		
		mainPanel.add(usernamePanel);
		mainPanel.add(usernameTextfield);
		mainPanel.add(passwordPanel);
		mainPanel.add(passwordTextfield);
		mainPanel.add(buttonPanel);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		this.add(mainPanel);
		this.pack();
		this.setTitle("Journal System Login");
		this.setSize(300, 140);
		this.setVisible(true);
	}
}
