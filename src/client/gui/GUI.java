package client.gui;

import client.*;
import common.AccessDeniedException;
import common.Record;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private JList<String> searchList;
	
	public static void main(String[] args) {
		new GUI(null);
	}
	
	public GUI(Client client) {
		this.client = client;
		
		createGuiWithDumpsterCode();
		
	}
	
	private void notifyUserOfAccessDenied() {
		JOptionPane.showMessageDialog(this, "Access Denied");
	}
	
	private void saveChanges() {
		try {
			client.setRecord(Integer.parseInt(searchResult.getElementAt(searchList.getSelectedIndex())), journalText.getText());
		} catch (AccessDeniedException ae) {
			notifyUserOfAccessDenied();
		} catch (Exception e) {
			//do nothing
		}
	}
	
	private void search() {
		try {
			int searchFor = Integer.parseInt(searchTextfield.getText());
			int[] searchResultArray = null;
			switch (searchSelect.getSelectedIndex()) {
				case 0:
					searchResultArray = client.getRecordIdFromPatientId(searchFor);
					break;
				case 1:
					searchResultArray = client.getRecordIdFromDoctorId(searchFor);
					break;
				case 2:
					searchResultArray = client.getRecordIdFromNurseId(searchFor);
					break;
				case 3:
					searchResultArray = client.getRecordIdFromDivisionId(searchFor);
					break;
				default:
					break;
			}
			searchList.setSelectedIndex(-1);
			searchList.removeAll();
			for (int i = 0; i < searchResultArray.length; ++i) {
				searchResult.addElement(Integer.toString(searchResultArray[i]));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Search failed! Check search clause");
		}
	}
	
	private void createNewRecord() {
		new NewRecordGui(client);
	}
	
	
	private void loadRecord(int recordId) {
		Record record = null;
		try {
			record = client.getRecord(recordId);
		} catch (AccessDeniedException ae) {
			notifyUserOfAccessDenied();
		} catch (Exception e) {
			//never gonna get here? catch here to be safe anyway
		}
		journalText.setText(record.getData());
		patientIdLabel.setText(Integer.toString(record.getPatientId()));
		doctorIdLabel.setText(Integer.toString(record.getDoctorId()));
		nurseIdLabel.setText(Integer.toString(record.getNurseId()));
		divisionIdLabel.setText(Integer.toString(record.getDivisionId()));

		eventLog.clear();
		
		String[] lines = record.getEventLog().split("\\r?\\n");
		
		for (String s : lines) {
			eventLog.addElement(s);
		}
	}
	
	private void deleteRecord() {
		int index = searchList.getSelectedIndex();
		if (index != -1 && index < searchResult.size()) {
			try {
				client.deleteRecord(Integer.parseInt(searchResult.getElementAt(index)));
			} catch (AccessDeniedException ae) {
				notifyUserOfAccessDenied();
			} catch (Exception e) {
				//never gonna get here? catch here to be safe anyway
			}
			journalText.setText("");
			patientIdLabel.setText("");
			doctorIdLabel.setText("");
			nurseIdLabel.setText("");
			divisionIdLabel.setText("");
			
			eventLog.clear();
			
			searchResult.remove(index);
			
			JOptionPane.showMessageDialog(this, "Successfully deleted record " + index + ".");
		}
		
	}
	
	
	/**
	 * Classic gui dumpster code with tons of workarounds. Big mess. Ignore and move on.
	 */
	private void createGuiWithDumpsterCode() {
		JPanel journalPanel = new JPanel();
		JPanel listPanel = new JPanel();
		
		journalPanel.setLayout(new BoxLayout(journalPanel, BoxLayout.Y_AXIS));
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		
		JPanel saveDeletePanel, infoPanel, infoSavePanel, eventPanel, eventInfoPanel;
		
		saveDeletePanel = new JPanel();
		saveDeletePanel.setLayout(new BoxLayout(saveDeletePanel, BoxLayout.X_AXIS));
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		infoSavePanel = new JPanel();
		infoSavePanel.setLayout(new BoxLayout(infoSavePanel, BoxLayout.Y_AXIS));
		
		eventPanel = new JPanel();
		eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
		
		eventInfoPanel = new JPanel();
		eventInfoPanel.setLayout(new BoxLayout(eventInfoPanel, BoxLayout.X_AXIS));
		
		String edge = "   ";
		
		JButton saveChangesButton = new JButton("Save changes");
		saveChangesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChanges();
			}	
		});
		
		saveDeletePanel.add(saveChangesButton);
		
		JButton deleteRecordButton = new JButton("Delete");
		deleteRecordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRecord();
			}	
		});
				
		saveDeletePanel.add(deleteRecordButton);
		
		patientIdLabel = new JLabel("Patient ID: ");
		doctorIdLabel = new JLabel("Doctor ID: ");
		nurseIdLabel = new JLabel("Nurse ID: ");
		divisionIdLabel = new JLabel("Division ID: ");
		
		TitledBorder infoBorder = new TitledBorder("Info");
		infoPanel.setBorder(infoBorder);
		
		infoPanel.add(patientIdLabel);
		infoPanel.add(doctorIdLabel);
		infoPanel.add(nurseIdLabel);
		infoPanel.add(divisionIdLabel);
		infoPanel.add(new JLabel("                                                "));
				
		infoSavePanel.add(new JLabel(edge));
		infoSavePanel.add(saveDeletePanel);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(infoPanel);
		infoSavePanel.add(p);

		JLabel eventLabel = new JLabel("Event Log");
		
		eventLog = new DefaultListModel<String>();
		JList<String> eventList = new JList<String>(eventLog);
		eventList.setSelectionModel(new DisableItemSelectionModel());
		JScrollPane eventScrollPane = new JScrollPane(eventList);
		
		eventPanel.add(eventLabel);
		eventPanel.add(eventScrollPane);
		
		eventInfoPanel.add(eventPanel);
		eventInfoPanel.add(infoSavePanel);
		
		JLabel journalLabel = new JLabel("Journal");
		journalText = new JTextArea(30,20);
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
				search();
			}
		});
		
		searchResult = new DefaultListModel<String>();
		searchList = new JList<String>(searchResult);
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane searchScrollPane = new JScrollPane(searchList);
		
		searchList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = searchList.getSelectedIndex();
				if (index != -1 && index < searchResult.size()) {
					loadRecord(Integer.parseInt(searchResult.getElementAt(index)));
				}
				
			}
			
		});
		
		JButton createButton = new JButton("New Journal");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewRecord();
			}
		});
		
		listPanel.add(new JLabel(" "));
		listPanel.add(searchSelect);
		listPanel.add(searchTextfield);
		listPanel.add(searchButton);
		
		JPanel workaroundP = new JPanel();
		workaroundP.setLayout(new BoxLayout(workaroundP, BoxLayout.X_AXIS));
		workaroundP.add(searchScrollPane);
		
		JPanel pad = new JPanel();
		pad.setLayout(new BoxLayout(pad, BoxLayout.Y_AXIS));
		
		int padSize = 40;
		
		for (int i = 0; i < padSize; ++i)
			pad.add(new JLabel(" "));
		
		workaroundP.add(pad);
		
		listPanel.add(workaroundP);
		listPanel.add(createButton);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				//TODO close socket here?
				System.exit(0);
			}
		});
		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(new JLabel(edge));
		mainPanel.add(journalPanel);
		mainPanel.add(new JLabel(edge));
		mainPanel.add(listPanel);
		mainPanel.add(new JLabel(edge));
		this.add(mainPanel);
		this.pack();
		this.setTitle("Journal System");
		
		this.setVisible(true);
	}
}
