package org.uncertweb.sos_db_client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.DatabaseSettingsController;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class DatabaseSettingsView implements IView {

	private JPanel databaseSettingsPanel;

	private JTextField textFieldHost;

	private JTextField textFieldPort;

	private JTextField textFieldDatabase;

	private JTextField textFieldUser;

	private JPasswordField passwordFieldPasswd;

	private JCheckBox checkBoxSaveSettings;

	private JButton btnConnect;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 */
	public DatabaseSettingsView() {

		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		ImageIcon connectIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Refresh16.gif"));

		Map<String, String> params = ApplicationProperties
				.readDatabaseSettingsProperties();

		GridBagConstraints gbc_lblPostgisSpatialDatabase = new GridBagConstraints();
		gbc_lblPostgisSpatialDatabase.insets = new Insets(0, 0, 5, 0);
		gbc_lblPostgisSpatialDatabase.anchor = GridBagConstraints.WEST;
		gbc_lblPostgisSpatialDatabase.gridwidth = 2;
		gbc_lblPostgisSpatialDatabase.gridx = 0;
		gbc_lblPostgisSpatialDatabase.gridy = 0;

		JLabel lblPostgisSpatialDatabase = new JLabel(
				"PostGIS spatial database");
		lblPostgisSpatialDatabase.setFont(lblPostgisSpatialDatabase.getFont()
				.deriveFont(Font.BOLD));

		GridBagConstraints gbc_lblHost = new GridBagConstraints();
		gbc_lblHost.anchor = GridBagConstraints.WEST;
		gbc_lblHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHost.gridx = 0;
		gbc_lblHost.gridy = 1;

		JLabel lblHost = new JLabel("host*");

		GridBagConstraints gbc_textFieldHost = new GridBagConstraints();
		gbc_textFieldHost.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldHost.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldHost.gridx = 1;
		gbc_textFieldHost.gridy = 1;

		textFieldHost = new JTextField(params.get("host"));
		textFieldHost.setToolTipText("Enter host (mandatory)");

		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.WEST;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 2;

		JLabel lblPort = new JLabel("port*");

		GridBagConstraints gbc_textFieldPort = new GridBagConstraints();
		gbc_textFieldPort.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPort.gridx = 1;
		gbc_textFieldPort.gridy = 2;

		textFieldPort = new JTextField(params.get("port"));
		textFieldPort.setToolTipText("Enter port (mandatory)");

		GridBagConstraints gbc_lblDatabase = new GridBagConstraints();
		gbc_lblDatabase.anchor = GridBagConstraints.WEST;
		gbc_lblDatabase.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatabase.gridx = 0;
		gbc_lblDatabase.gridy = 3;

		JLabel lblDatabase = new JLabel("database*");

		GridBagConstraints gbc_textFieldDatabase = new GridBagConstraints();
		gbc_textFieldDatabase.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldDatabase.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDatabase.gridx = 1;
		gbc_textFieldDatabase.gridy = 3;

		textFieldDatabase = new JTextField(params.get("database"));
		textFieldDatabase.setToolTipText("Enter database name (mandatory)");

		GridBagConstraints gbc_lblUser = new GridBagConstraints();
		gbc_lblUser.anchor = GridBagConstraints.WEST;
		gbc_lblUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblUser.gridx = 0;
		gbc_lblUser.gridy = 4;

		JLabel lblUser = new JLabel("user*");

		GridBagConstraints gbc_textFieldUser = new GridBagConstraints();
		gbc_textFieldUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUser.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldUser.gridx = 1;
		gbc_textFieldUser.gridy = 4;

		textFieldUser = new JTextField(params.get("user"));
		textFieldUser.setToolTipText("Enter username (mandatory)");

		GridBagConstraints gbc_lblPasswd = new GridBagConstraints();
		gbc_lblPasswd.anchor = GridBagConstraints.WEST;
		gbc_lblPasswd.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasswd.gridx = 0;
		gbc_lblPasswd.gridy = 5;

		JLabel lblPasswd = new JLabel("passwd");

		GridBagConstraints gbc_passwordFieldPasswd = new GridBagConstraints();
		gbc_passwordFieldPasswd.insets = new Insets(0, 0, 5, 0);
		gbc_passwordFieldPasswd.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldPasswd.gridx = 1;
		gbc_passwordFieldPasswd.gridy = 5;

		passwordFieldPasswd = new JPasswordField(params.get("passwd"));
		passwordFieldPasswd.setToolTipText("Enter password");

		GridBagConstraints gbc_checkBoxSaveSettings = new GridBagConstraints();
		gbc_checkBoxSaveSettings.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxSaveSettings.anchor = GridBagConstraints.WEST;
		gbc_checkBoxSaveSettings.gridwidth = 2;
		gbc_checkBoxSaveSettings.gridx = 0;
		gbc_checkBoxSaveSettings.gridy = 6;

		checkBoxSaveSettings = new JCheckBox("Save database settings", false);
		checkBoxSaveSettings
				.setToolTipText("Save database settings for next session, the password is stored in plain text");

		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.EAST;
		gbc_btnConnect.gridwidth = 2;
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 7;

		btnConnect = new JButton("Connect", connectIcon);
		btnConnect.setToolTipText("Connect");

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldHost);
		mandatoryTextFields.add(textFieldPort);
		mandatoryTextFields.add(textFieldDatabase);
		mandatoryTextFields.add(textFieldUser);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_databaseSettingsPanel = new GridBagLayout();
		gbl_databaseSettingsPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_databaseSettingsPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		gbl_databaseSettingsPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_databaseSettingsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		databaseSettingsPanel = new JPanel();
		databaseSettingsPanel.setLayout(gbl_databaseSettingsPanel);
		databaseSettingsPanel.add(lblPostgisSpatialDatabase,
				gbc_lblPostgisSpatialDatabase);
		databaseSettingsPanel.add(lblHost, gbc_lblHost);
		databaseSettingsPanel.add(textFieldHost, gbc_textFieldHost);
		databaseSettingsPanel.add(lblPort, gbc_lblPort);
		databaseSettingsPanel.add(textFieldPort, gbc_textFieldPort);
		databaseSettingsPanel.add(lblDatabase, gbc_lblDatabase);
		databaseSettingsPanel.add(textFieldDatabase, gbc_textFieldDatabase);
		databaseSettingsPanel.add(lblUser, gbc_lblUser);
		databaseSettingsPanel.add(textFieldUser, gbc_textFieldUser);
		databaseSettingsPanel.add(lblPasswd, gbc_lblPasswd);
		databaseSettingsPanel.add(passwordFieldPasswd, gbc_passwordFieldPasswd);
		databaseSettingsPanel.add(checkBoxSaveSettings,
				gbc_checkBoxSaveSettings);
		databaseSettingsPanel.add(btnConnect, gbc_btnConnect);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		new DatabaseSettingsController(this);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return databaseSettingsPanel;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public Map<String, String> getDatabaseSettingsParams() {

		char[] _passwd = passwordFieldPasswd.getPassword();
		StringBuffer passwd = new StringBuffer();

		for (int i = 0; i < _passwd.length; i++) {
			passwd.append(_passwd[i]);
		}

		Map<String, String> params = new HashMap<String, String>();

		params.put("host", textFieldHost.getText().trim());
		params.put("port", textFieldPort.getText().trim());
		params.put("database", textFieldDatabase.getText().trim());
		params.put("user", textFieldUser.getText().trim());
		params.put("passwd", passwd.toString().trim());

		return params;

	}

	/**
	 * Check if combo box is selected.
	 * 
	 * @return true if combo box is selected, false if not
	 */
	public boolean IsCheckBoxSaveSettingsSelected() {

		return checkBoxSaveSettings.isSelected();

	}

	/**
	 * Check if at least one mandatory field is empty.
	 * 
	 * @return true if at least one mandatory field is empty, false if not
	 */
	public boolean IsMandatoryFieldEmpty() {

		for (JTextField mandatoryTextField : mandatoryTextFields) {
			String text = mandatoryTextField.getText();
			if (text.trim().isEmpty()) {
				btnConnect.setEnabled(false);
				return true;
			}
		}

		btnConnect.setEnabled(true);
		return false;

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addBtnConnectListener(ActionListener l) {

		btnConnect.addActionListener(l);

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addMandatoryTextFieldsListener(DocumentListener l) {

		for (JTextField mandatoryTextField : mandatoryTextFields) {
			mandatoryTextField.getDocument().addDocumentListener(l);
		}

	}

}
