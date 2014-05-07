package org.uncertweb.sos_db_client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.PhenomenonController;
import org.uncertweb.sos_db_client.model.MainModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class PhenomenonView implements IView {

	private JPanel phenomenonPanel;

	private JTextField textFieldPhenomenonId;

	private JTextField textFieldPhenomenonDescription;

	private JTextField textFieldUnit;

	private JComboBox comboBoxValueType;

	private JTextField textFieldCompositePhenomenonId;

	private JTextField textFieldOmApplicationSchemaLink;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JComboBox> mandatoryComboBoxes;

	private List<JTextField> textFields;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 */
	public PhenomenonView() {

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

		GridBagConstraints gbc_lblPhenomenonTable = new GridBagConstraints();
		gbc_lblPhenomenonTable.gridwidth = 2;
		gbc_lblPhenomenonTable.anchor = GridBagConstraints.WEST;
		gbc_lblPhenomenonTable.insets = new Insets(0, 0, 5, 0);
		gbc_lblPhenomenonTable.gridx = 0;
		gbc_lblPhenomenonTable.gridy = 0;

		JLabel lblPhenomenonTable = new JLabel("Phenomenon table");
		lblPhenomenonTable.setFont(lblPhenomenonTable.getFont().deriveFont(
				Font.BOLD));

		GridBagConstraints gbc_lblPhenomenonId = new GridBagConstraints();
		gbc_lblPhenomenonId.anchor = GridBagConstraints.WEST;
		gbc_lblPhenomenonId.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhenomenonId.gridx = 0;
		gbc_lblPhenomenonId.gridy = 1;

		JLabel lblPhenomenonId = new JLabel("phenomenon_id*");

		GridBagConstraints gbc_textFieldPhenomenonId = new GridBagConstraints();
		gbc_textFieldPhenomenonId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPhenomenonId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPhenomenonId.gridx = 1;
		gbc_textFieldPhenomenonId.gridy = 1;

		textFieldPhenomenonId = new JTextField(10);
		textFieldPhenomenonId
				.setToolTipText("Enter phenomenon ID, should be the URN of the phenomenon as specified by the OGC (mandatory)");

		GridBagConstraints gbc_lblPhenomenonDescription = new GridBagConstraints();
		gbc_lblPhenomenonDescription.anchor = GridBagConstraints.WEST;
		gbc_lblPhenomenonDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhenomenonDescription.gridx = 0;
		gbc_lblPhenomenonDescription.gridy = 2;

		JLabel lblPhenomenonDescription = new JLabel("phenomenon_description");

		GridBagConstraints gbc_textFieldPhenomenonDescription = new GridBagConstraints();
		gbc_textFieldPhenomenonDescription.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPhenomenonDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPhenomenonDescription.gridx = 1;
		gbc_textFieldPhenomenonDescription.gridy = 2;

		textFieldPhenomenonDescription = new JTextField(10);
		textFieldPhenomenonDescription
				.setToolTipText("<html>Enter phenomenon description, e.g. <i>water speed</i></html>");

		GridBagConstraints gbc_lblUnit = new GridBagConstraints();
		gbc_lblUnit.anchor = GridBagConstraints.WEST;
		gbc_lblUnit.insets = new Insets(0, 0, 5, 5);
		gbc_lblUnit.gridx = 0;
		gbc_lblUnit.gridy = 3;

		JLabel lblUnit = new JLabel("unit*");

		GridBagConstraints gbc_textFieldUnit = new GridBagConstraints();
		gbc_textFieldUnit.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUnit.gridx = 1;
		gbc_textFieldUnit.gridy = 3;

		textFieldUnit = new JTextField(10);
		textFieldUnit
				.setToolTipText("<html>Enter unit, e.g. <i>m/s</i> (mandatory)</html>");

		GridBagConstraints gbc_lblValueType = new GridBagConstraints();
		gbc_lblValueType.anchor = GridBagConstraints.WEST;
		gbc_lblValueType.insets = new Insets(0, 0, 5, 5);
		gbc_lblValueType.gridx = 0;
		gbc_lblValueType.gridy = 4;

		JLabel lblValueType = new JLabel("valuetype*");

		GridBagConstraints gbc_comboBoxValueType = new GridBagConstraints();
		gbc_comboBoxValueType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxValueType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxValueType.gridx = 1;
		gbc_comboBoxValueType.gridy = 4;

		comboBoxValueType = new JComboBox(new String[] { "", "uncertaintyType",
				"booleanType", "countType", "textType" });
		comboBoxValueType
				.setToolTipText("<html>Choose <i>uncertaintyType</i> for uncertain, <i>countType</i> for numerical or <br /><i>booleanType</i> and <i>textType</i> for textual (categorical) values (mandatory)</html>");

		GridBagConstraints gbc_lblCompositePhenomenonId = new GridBagConstraints();
		gbc_lblCompositePhenomenonId.anchor = GridBagConstraints.WEST;
		gbc_lblCompositePhenomenonId.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompositePhenomenonId.gridx = 0;
		gbc_lblCompositePhenomenonId.gridy = 5;

		JLabel lblCompositePhenomenonId = new JLabel("composite_phenomenon_id");

		GridBagConstraints gbc_textFieldCompositePhenomenonId = new GridBagConstraints();
		gbc_textFieldCompositePhenomenonId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldCompositePhenomenonId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCompositePhenomenonId.gridx = 1;
		gbc_textFieldCompositePhenomenonId.gridy = 5;

		textFieldCompositePhenomenonId = new JTextField(10);
		textFieldCompositePhenomenonId
				.setToolTipText("Enter composite phenomenon ID");

		GridBagConstraints gbc_lblOmApplicationSchemaLink = new GridBagConstraints();
		gbc_lblOmApplicationSchemaLink.anchor = GridBagConstraints.EAST;
		gbc_lblOmApplicationSchemaLink.insets = new Insets(0, 0, 5, 5);
		gbc_lblOmApplicationSchemaLink.gridx = 0;
		gbc_lblOmApplicationSchemaLink.gridy = 6;

		JLabel lblOmApplicationSchemaLink = new JLabel(
				"om_application_schema_link");

		GridBagConstraints gbc_textFieldOmApplicationSchemaLink = new GridBagConstraints();
		gbc_textFieldOmApplicationSchemaLink.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldOmApplicationSchemaLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOmApplicationSchemaLink.gridx = 1;
		gbc_textFieldOmApplicationSchemaLink.gridy = 6;

		textFieldOmApplicationSchemaLink = new JTextField(10);
		textFieldOmApplicationSchemaLink
				.setToolTipText("Enter O&M application schema link");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 2;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 7;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add combo boxes to be reseted.
		 */
		comboBoxes = new ArrayList<JComboBox>();
		comboBoxes.add(comboBoxValueType);

		/**
		 * Add mandatory combo boxes to be checked if empty.
		 */
		mandatoryComboBoxes = new ArrayList<JComboBox>();
		mandatoryComboBoxes.add(comboBoxValueType);

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldPhenomenonId);
		textFields.add(textFieldPhenomenonDescription);
		textFields.add(textFieldUnit);
		textFields.add(textFieldCompositePhenomenonId);
		textFields.add(textFieldOmApplicationSchemaLink);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldPhenomenonId);
		mandatoryTextFields.add(textFieldUnit);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_phenomenonPanel = new GridBagLayout();
		gbl_phenomenonPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_phenomenonPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_phenomenonPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_phenomenonPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		phenomenonPanel = new JPanel();
		phenomenonPanel.setLayout(gbl_phenomenonPanel);
		phenomenonPanel.add(lblPhenomenonTable, gbc_lblPhenomenonTable);
		phenomenonPanel.add(lblPhenomenonId, gbc_lblPhenomenonId);
		phenomenonPanel.add(textFieldPhenomenonId, gbc_textFieldPhenomenonId);
		phenomenonPanel.add(lblPhenomenonDescription,
				gbc_lblPhenomenonDescription);
		phenomenonPanel.add(textFieldPhenomenonDescription,
				gbc_textFieldPhenomenonDescription);
		phenomenonPanel.add(lblUnit, gbc_lblUnit);
		phenomenonPanel.add(textFieldUnit, gbc_textFieldUnit);
		phenomenonPanel.add(lblValueType, gbc_lblValueType);
		phenomenonPanel.add(comboBoxValueType, gbc_comboBoxValueType);
		phenomenonPanel.add(lblCompositePhenomenonId,
				gbc_lblCompositePhenomenonId);
		phenomenonPanel.add(textFieldCompositePhenomenonId,
				gbc_textFieldCompositePhenomenonId);
		phenomenonPanel.add(lblOmApplicationSchemaLink,
				gbc_lblOmApplicationSchemaLink);
		phenomenonPanel.add(textFieldOmApplicationSchemaLink,
				gbc_textFieldOmApplicationSchemaLink);
		phenomenonPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		MainModel mainModel = new MainModel();
		new PhenomenonController(this, mainModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return phenomenonPanel;

	}

	/**
	 * Reset fields.
	 */
	public void reset() {

		for (JComboBox comboBox : comboBoxes) {
			comboBox.setSelectedItem("");
		}

		for (JTextField textField : textFields) {
			textField.setText("");
		}

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getPhenomenonParams() {

		List<String> params = new ArrayList<String>();

		params.add(textFieldPhenomenonId.getText().trim());
		params.add(textFieldPhenomenonDescription.getText().trim());
		params.add(textFieldUnit.getText().trim());
		params.add(comboBoxValueType.getSelectedItem().toString().trim());
		params.add(textFieldCompositePhenomenonId.getText().trim());
		params.add(textFieldOmApplicationSchemaLink.getText().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Check if at least one mandatory field is empty.
	 * 
	 * @return true if at least one mandatory field is empty, false if not
	 */
	public boolean IsMandatoryFieldEmpty() {

		for (JComboBox mandatoryComboBox : mandatoryComboBoxes) {
			Object selectedItem = mandatoryComboBox.getSelectedItem();
			if (selectedItem == null
					|| selectedItem.toString().trim().isEmpty()) {
				btnImport.setEnabled(false);
				return true;
			}
		}

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
	public void addMandatoryComboBoxesListener(ItemListener l) {

		for (JComboBox mandatoryComboBox : mandatoryComboBoxes) {
			mandatoryComboBox.addItemListener(l);
		}

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
