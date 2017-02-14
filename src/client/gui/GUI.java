package client.gui;

import client.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private Client client;
	
	private JTextArea journalText;
	private DefaultListModel<String> eventLog;
	private JLabel patientIdLabel;
	private JLabel doctorIdLabel;
	private JLabel nurseIdLabel;
	private JLabel divisionIdLabel;
	private JComboBox<String> searchSelect;
	private JTextField searchTextfield;
	private DefaultListModel<String> searchResult;
	
	public GUI(Client client) {
		JPanel journalPanel = new JPanel();
		JPanel listPanel = new JPanel();
		
		journalPanel.setLayout(new BoxLayout(journalPanel, BoxLayout.Y_AXIS));
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		JPanel infoSavePanel, eventInfoPanel;
		
		infoSavePanel = new JPanel();
		infoSavePanel.setLayout(new BoxLayout(infoSavePanel, BoxLayout.Y_AXIS));
		
		eventInfoPanel = new JPanel();
		eventInfoPanel.setLayout(new BoxLayout(eventInfoPanel, BoxLayout.X_AXIS));
		
		patientIdLabel = new JLabel("Patient ID: ");
		doctorIdLabel = new JLabel("Doctor ID: ");
		nurseIdLabel = new JLabel("Nurse ID: ");
		divisionIdLabel = new JLabel("Division ID: ");
		
		JButton saveChangesButton = new JButton("Save changes");
		saveChangesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO Save changes
			}	
		});
		
		infoSavePanel.add(patientIdLabel);
		infoSavePanel.add(doctorIdLabel);
		infoSavePanel.add(nurseIdLabel);
		infoSavePanel.add(divisionIdLabel);
		infoSavePanel.add(saveChangesButton);
		
		eventLog = new DefaultListModel<String>();
		JList<String> eventList = new JList<String>(eventLog);
		eventList.setSelectionModel(new DisableItemSelectionModel());
		
		eventInfoPanel.add(eventList);
		eventInfoPanel.add(infoSavePanel);
		
		JLabel journalLabel = new JLabel("Journal");
		journalText = new JTextArea();
		journalText.setLineWrap(true);
		journalText.setWrapStyleWord(true);
		
		journalPanel.add(journalLabel);
		journalPanel.add(journalText);
		journalPanel.add(eventInfoPanel);
		

		String[] searchStrings = {"Patient ID", "Doctor ID", "Nurse ID", "Division ID"};
		searchSelect = new JComboBox<String>(searchStrings);
		
		searchTextfield = new JTextField();
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO do da search man
			}
		});
		
		searchResult = new DefaultListModel<String>();
		JList<String> searchList = new JList<String>(searchResult);
		searchList.setSelectionModel(new DisableItemSelectionModel());
		
		JButton createButton = new JButton("New Journal");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO open new window for journal creation
			}
		});
		
		listPanel.add(searchSelect);
		listPanel.add(searchTextfield);
		listPanel.add(searchButton);
		listPanel.add(searchList);
		listPanel.add(createButton);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				//TODO close socket here?
				System.exit(0);
			}
		});
		
		
		//TODO set layoutmanager to GridBagLayout?
		//add journalPanel and listPanel to this JFrame
		//pack() or whatever + set size to something nice
		
		this.setVisible(true);
		
	}
}
