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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.ProcedureController;
import org.uncertweb.sos_db_client.model.MainModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class ProcedureView implements IView {

	private JPanel procedurePanel;

	private JTextField textFieldProcedureId;

	private JTextField textFieldDescriptionUrl;

	private JTextField textFieldDescriptionType;

	private JTextField textFieldSmlFile;

	private JTextField textFieldActualPosition;

	private JComboBox comboBoxActive;

	private JComboBox comboBoxMobile;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JTextField> textFields;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 */
	public ProcedureView() {

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

		GridBagConstraints gbc_lblProcedureTable = new GridBagConstraints();
		gbc_lblProcedureTable.anchor = GridBagConstraints.WEST;
		gbc_lblProcedureTable.gridwidth = 2;
		gbc_lblProcedureTable.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcedureTable.gridx = 0;
		gbc_lblProcedureTable.gridy = 0;

		JLabel lblProcedureTable = new JLabel("Procedure table");
		lblProcedureTable.setFont(lblProcedureTable.getFont().deriveFont(
				Font.BOLD));

		GridBagConstraints gbc_lblProcedureId = new GridBagConstraints();
		gbc_lblProcedureId.anchor = GridBagConstraints.WEST;
		gbc_lblProcedureId.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcedureId.gridx = 0;
		gbc_lblProcedureId.gridy = 1;

		JLabel lblProcedureId = new JLabel("procedure_id*");

		GridBagConstraints gbc_textFieldProcedureId = new GridBagConstraints();
		gbc_textFieldProcedureId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldProcedureId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldProcedureId.gridx = 1;
		gbc_textFieldProcedureId.gridy = 1;

		textFieldProcedureId = new JTextField(10);
		textFieldProcedureId
				.setToolTipText("Enter procedure ID, should be the URN of the procedure as specified by the OGC (mandatory)");

		GridBagConstraints gbc_lblDescriptionUrl = new GridBagConstraints();
		gbc_lblDescriptionUrl.anchor = GridBagConstraints.WEST;
		gbc_lblDescriptionUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescriptionUrl.gridx = 0;
		gbc_lblDescriptionUrl.gridy = 2;

		JLabel lblDescriptionUrl = new JLabel("description_url");

		GridBagConstraints gbc_textFieldDescriptionUrl = new GridBagConstraints();
		gbc_textFieldDescriptionUrl.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldDescriptionUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDescriptionUrl.gridx = 1;
		gbc_textFieldDescriptionUrl.gridy = 2;

		textFieldDescriptionUrl = new JTextField(10);
		textFieldDescriptionUrl
				.setToolTipText("<html>Enter description URL, e.g. <i>standard/uw-sensor-1.xml</i></html>");

		GridBagConstraints gbc_lblDescriptionType = new GridBagConstraints();
		gbc_lblDescriptionType.anchor = GridBagConstraints.WEST;
		gbc_lblDescriptionType.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescriptionType.gridx = 0;
		gbc_lblDescriptionType.gridy = 3;

		JLabel lblDescriptionType = new JLabel("description_type");

		GridBagConstraints gbc_textFieldDescriptionType = new GridBagConstraints();
		gbc_textFieldDescriptionType.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldDescriptionType.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDescriptionType.gridx = 1;
		gbc_textFieldDescriptionType.gridy = 3;

		textFieldDescriptionType = new JTextField(10);
		textFieldDescriptionType
				.setToolTipText("<html>Enter description type, e.g. <i>text/xml;subtype=\"SensorML/1.0.1\"</i></html>");

		GridBagConstraints gbc_lblSmlFile = new GridBagConstraints();
		gbc_lblSmlFile.anchor = GridBagConstraints.WEST;
		gbc_lblSmlFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSmlFile.gridx = 0;
		gbc_lblSmlFile.gridy = 4;

		JLabel lblSmlFile = new JLabel("sml_file");

		GridBagConstraints gbc_textFieldSmlFile = new GridBagConstraints();
		gbc_textFieldSmlFile.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldSmlFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSmlFile.gridx = 1;
		gbc_textFieldSmlFile.gridy = 4;

		textFieldSmlFile = new JTextField(10);
		textFieldSmlFile.setToolTipText("Enter SML file");

		GridBagConstraints gbc_lblActualPosition = new GridBagConstraints();
		gbc_lblActualPosition.anchor = GridBagConstraints.WEST;
		gbc_lblActualPosition.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualPosition.gridx = 0;
		gbc_lblActualPosition.gridy = 5;

		JLabel lblActualPosition = new JLabel("actual_position");

		GridBagConstraints gbc_textFieldActualPosition = new GridBagConstraints();
		gbc_textFieldActualPosition.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldActualPosition.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldActualPosition.gridx = 1;
		gbc_textFieldActualPosition.gridy = 5;

		textFieldActualPosition = new JTextField(10);
		textFieldActualPosition.setToolTipText("Enter actual position");

		GridBagConstraints gbc_lblActive = new GridBagConstraints();
		gbc_lblActive.anchor = GridBagConstraints.WEST;
		gbc_lblActive.insets = new Insets(0, 0, 5, 5);
		gbc_lblActive.gridx = 0;
		gbc_lblActive.gridy = 6;

		JLabel lblActive = new JLabel("active");

		GridBagConstraints gbc_comboBoxActive = new GridBagConstraints();
		gbc_comboBoxActive.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxActive.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxActive.gridx = 1;
		gbc_comboBoxActive.gridy = 6;

		comboBoxActive = new JComboBox(new String[] { "", "true", "false" });
		comboBoxActive
				.setToolTipText("<html>Choose <i>true</i> if sensor is collecting data at the moment, <i>false</i> if not</html>");

		GridBagConstraints gbc_lblMobile = new GridBagConstraints();
		gbc_lblMobile.anchor = GridBagConstraints.WEST;
		gbc_lblMobile.insets = new Insets(0, 0, 5, 5);
		gbc_lblMobile.gridx = 0;
		gbc_lblMobile.gridy = 7;

		JLabel lblMobile = new JLabel("mobile");

		GridBagConstraints gbc_comboBoxMobile = new GridBagConstraints();
		gbc_comboBoxMobile.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxMobile.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMobile.gridx = 1;
		gbc_comboBoxMobile.gridy = 7;

		comboBoxMobile = new JComboBox(new String[] { "", "true", "false" });
		comboBoxMobile
				.setToolTipText("<html>Choose <i>true</i> if sensor is mobile, <i>false</i> if not</html>");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 2;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 8;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add combo boxes to be reseted.
		 */
		comboBoxes = new ArrayList<JComboBox>();
		comboBoxes.add(comboBoxActive);
		comboBoxes.add(comboBoxMobile);

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldProcedureId);
		textFields.add(textFieldDescriptionUrl);
		textFields.add(textFieldDescriptionType);
		textFields.add(textFieldSmlFile);
		textFields.add(textFieldActualPosition);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldProcedureId);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_procedurePanel = new GridBagLayout();
		gbl_procedurePanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_procedurePanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0 };
		gbl_procedurePanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_procedurePanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		procedurePanel = new JPanel();
		procedurePanel.setLayout(gbl_procedurePanel);
		procedurePanel.add(lblProcedureTable, gbc_lblProcedureTable);
		procedurePanel.add(lblProcedureId, gbc_lblProcedureId);
		procedurePanel.add(textFieldProcedureId, gbc_textFieldProcedureId);
		procedurePanel.add(lblDescriptionUrl, gbc_lblDescriptionUrl);
		procedurePanel
				.add(textFieldDescriptionUrl, gbc_textFieldDescriptionUrl);
		procedurePanel.add(lblDescriptionType, gbc_lblDescriptionType);
		procedurePanel.add(textFieldDescriptionType,
				gbc_textFieldDescriptionType);
		procedurePanel.add(lblSmlFile, gbc_lblSmlFile);
		procedurePanel.add(lblActualPosition, gbc_lblActualPosition);
		procedurePanel
				.add(textFieldActualPosition, gbc_textFieldActualPosition);
		procedurePanel.add(lblActive, gbc_lblActive);
		procedurePanel.add(comboBoxActive, gbc_comboBoxActive);
		procedurePanel.add(lblMobile, gbc_lblMobile);
		procedurePanel.add(comboBoxMobile, gbc_comboBoxMobile);
		procedurePanel.add(textFieldSmlFile, gbc_textFieldSmlFile);
		procedurePanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		MainModel mainModel = new MainModel();
		new ProcedureController(this, mainModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return procedurePanel;

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
	public List<String> getProcedureParams() {

		List<String> params = new ArrayList<String>();

		params.add(textFieldProcedureId.getText().trim());
		params.add(textFieldDescriptionUrl.getText().trim());
		params.add(textFieldDescriptionType.getText().trim());
		params.add(textFieldSmlFile.getText().trim());
		params.add(textFieldActualPosition.getText().trim());
		params.add(comboBoxActive.getSelectedItem().toString().trim());
		params.add(comboBoxMobile.getSelectedItem().toString().trim());

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
	 * Add listener
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addBtnImportListener(ActionListener l) {

		btnImport.addActionListener(l);

	}

	/**
	 * Add listener
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
