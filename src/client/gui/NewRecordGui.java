package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import client.Client;
import common.AccessDeniedException;
import common.Record;

@SuppressWarnings("serial")
public class NewRecordGui extends JFrame {

	private Client client;
	
	JTextField patientId, doctorId, nurseId, divisionId;
	JTextArea journalText;
	
	public NewRecordGui(Client client) {
		this.client = client;
		createGuiWithDumpsterCode();

	}
	
	private void confirm() {
		try {
			Record record = new Record(0, Integer.parseInt(doctorId.getText()), Integer.parseInt(nurseId.getText()), Integer.parseInt(divisionId.getText()), Integer.parseInt(patientId.getText()), journalText.getText());
			client.createRecord(record);
		} catch (AccessDeniedException ae) {
			JOptionPane.showMessageDialog(this, "Access Denied");
			cancel();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to create a new record, make sure ID fields only contain numbers.");
			return;
		}
		JOptionPane.showMessageDialog(this, "Successfully created a new journal! To see the new journal make a new search");
		cancel();
	}
	
	private void cancel() {
		this.dispose();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Classic gui dumpster code with tons of workarounds. Big mess. Ignore and move on.
	 */
	private void createGuiWithDumpsterCode() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		JPanel p1,p2,p3,p4;
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
		p4.setLayout(new BoxLayout(p4, BoxLayout.X_AXIS));

		JLabel patientLabel = new JLabel("Patient ID");
		JLabel doctorLabel = new JLabel("Doctor ID");
		JLabel nurseLabel = new JLabel("Nurse ID");
		JLabel divisionLabel = new JLabel("Division ID");
		
		patientId = new JTextField();
		doctorId = new JTextField();
		nurseId = new JTextField();
		divisionId = new JTextField();
		
		p1.add(patientLabel);
		p1.add(patientId);
		p1.add(doctorLabel);
		p1.add(doctorId);
		
		p2.add(divisionLabel);
		p2.add(divisionId);
		p2.add(nurseLabel);
		p2.add(nurseId);
		
		p3.add(p1);
		p3.add(p2);
		
		main.add(p3);
		
		JPanel p = new JPanel();
		TitledBorder tBorder = new TitledBorder("Journal");
		p.setBorder(tBorder);
		
		
		journalText = new JTextArea(30,20);
		journalText.setLineWrap(true);
		journalText.setWrapStyleWord(true);
		
		p.add(journalText);
		
		main.add(p);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirm();
			}	
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}	
		});
		
		p4.add(confirmButton);
		p4.add(cancelButton);
		
		main.add(p4);
		
		this.add(main);
		
		this.setTitle("Create new journal entry");
		pack();
		this.setVisible(true);
	}
	
}
