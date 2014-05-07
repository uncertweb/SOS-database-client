package org.uncertweb.sos_db_client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.OfferingController;
import org.uncertweb.sos_db_client.model.MainModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class OfferingView implements IView {

	private JPanel offeringPanel;

	private JTextField textFieldOfferingId;

	private JTextField textFieldOfferingName;

	private JButton btnImport;

	private List<JTextField> textFields;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 */
	public OfferingView() {

		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		ImageIcon importIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Import16.gif"));

		GridBagConstraints gbc_lblOfferingTable = new GridBagConstraints();
		gbc_lblOfferingTable.anchor = GridBagConstraints.WEST;
		gbc_lblOfferingTable.gridwidth = 2;
		gbc_lblOfferingTable.insets = new Insets(0, 0, 5, 0);
		gbc_lblOfferingTable.gridx = 0;
		gbc_lblOfferingTable.gridy = 0;

		JLabel lblOfferingTable = new JLabel("Offering table");
		lblOfferingTable.setFont(lblOfferingTable.getFont().deriveFont(
				Font.BOLD));

		GridBagConstraints gbc_lblOfferingId = new GridBagConstraints();
		gbc_lblOfferingId.anchor = GridBagConstraints.WEST;
		gbc_lblOfferingId.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfferingId.gridx = 0;
		gbc_lblOfferingId.gridy = 1;

		JLabel lblOfferingId = new JLabel("offering_id*");

		GridBagConstraints gbc_textFieldOfferingId = new GridBagConstraints();
		gbc_textFieldOfferingId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldOfferingId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOfferingId.gridx = 1;
		gbc_textFieldOfferingId.gridy = 1;

		textFieldOfferingId = new JTextField(10);
		textFieldOfferingId
				.setToolTipText("<html>Enter offering ID, e.g. <i>WATER_SPEED</i> (mandatory)</html>");

		GridBagConstraints gbc_lblOfferingName = new GridBagConstraints();
		gbc_lblOfferingName.anchor = GridBagConstraints.WEST;
		gbc_lblOfferingName.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfferingName.gridx = 0;
		gbc_lblOfferingName.gridy = 2;

		JLabel lblOfferingName = new JLabel("offering_name");

		GridBagConstraints gbc_textFieldOfferingName = new GridBagConstraints();
		gbc_textFieldOfferingName.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldOfferingName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOfferingName.gridx = 1;
		gbc_textFieldOfferingName.gridy = 2;

		textFieldOfferingName = new JTextField(10);
		textFieldOfferingName
				.setToolTipText("<html>Enter offering name, e.g. <i>The waterspeed at a gage in a river</i></html>");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 2;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 3;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldOfferingId);
		textFields.add(textFieldOfferingName);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldOfferingId);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_offeringPanel = new GridBagLayout();
		gbl_offeringPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_offeringPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_offeringPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_offeringPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };

		offeringPanel = new JPanel();
		offeringPanel.setLayout(gbl_offeringPanel);
		offeringPanel.add(lblOfferingTable, gbc_lblOfferingTable);
		offeringPanel.add(lblOfferingId, gbc_lblOfferingId);
		offeringPanel.add(textFieldOfferingId, gbc_textFieldOfferingId);
		offeringPanel.add(lblOfferingName, gbc_lblOfferingName);
		offeringPanel.add(textFieldOfferingName, gbc_textFieldOfferingName);
		offeringPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		MainModel mainModel = new MainModel();
		new OfferingController(this, mainModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return offeringPanel;

	}

	/**
	 * Reset fields.
	 */
	public void reset() {

		for (JTextField textField : textFields) {
			textField.setText("");
		}

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getOfferingParams() {

		List<String> params = new ArrayList<String>();

		for (JTextField textField : textFields) {
			params.add(textField.getText().trim());
		}

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Check if at least one mandatory field is empty.
	 * 
	 * @return true if at least one mandatory field is empty, false if not
	 */
	public boolean IsMandatoryFieldEmpty() {

		for (JTextField mandatoryTextField : mandatoryTextFields) {
			if (mandatoryTextField.getText().trim().isEmpty()) {
				btnImport.setEnabled(false);
				return true;
			}
		}

		btnImport.setEnabled(true);
		return false;

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addBtnImportListener(ActionListener l) {

		btnImport.addActionListener(l);

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
