package control;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.wimpi.modbus.ModbusDeviceIdentification;

import device.read.*;

public class DiagFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -3707693590333333107L;
	private JComboBox cmbType;
	private Device myDevice;
	private Container content;
	private JButton btnGet;
	private JTextField txtObjectId;
	private JTextArea txtResult;
		
	public DiagFrame(Device myDevice) {
		super();
		this.myDevice = myDevice;
		build();		
	}
	
	/**
	 * Build window
	 */
	private void build(){
		//Window properties
		this.setTitle("Diagnostic"); 

		this.setSize(300,400);
		this.setLocationRelativeTo(null); 				//Center
		this.setResizable(false);
		this.buildContentPanel();
		
	}
	
	/**
	 * Generate content panel
	 */
	private void buildContentPanel() {
		content = getContentPane();

		content.setLayout(new FlowLayout());
		content.setBackground(Color.WHITE);

		//List select
		Object[] elements = new Object[]{"1. Basic", "2. Regular", "3. Extended", "4. Specific"};
		cmbType = new JComboBox(elements);
		content.add(cmbType);

		//ObjectId
		txtObjectId = new JTextField();
		txtObjectId.setColumns(5);
		content.add(txtObjectId);
		
		//Button Get
		btnGet = new JButton("Get");
		btnGet.addActionListener(this);
		content.add(btnGet);	
		
		txtResult = new JTextArea();
		content.add(txtResult);	
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == btnGet){
			printDiagnostic();
		}
	}
	
	/**
	 * output content of diagnostic
	 */
	private void printDiagnostic() {
		int i ;
		int readCode;
		int objectId;
		
		if (!myDevice.isConnected()) {
			JOptionPane.showMessageDialog(this, "Device not connected");
			return;
		}
		
		readCode = cmbType.getSelectedIndex() + 1;
		
		objectId = Integer.parseInt(txtObjectId.getText());
		
		ModbusDeviceIdentification ident = myDevice.getDeviceIdentification(readCode, objectId);

		String result = "";
		for (i = 0; i < ident.getLength(); i++) {
			if (ident.getIdentification(i) != null && ident.getIdentification(i) != "") { 
				result += "[" + i + "] " + ident.getIdentification(i) + "\n";
			}
		}
		txtResult.setText(result);
	}
}
