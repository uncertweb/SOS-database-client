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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.UncertaintyController;
import org.uncertweb.sos_db_client.model.UncertaintyModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class UncertaintyView implements IView {

	private IView parentView;

	private JPanel uncertaintyPanel;

	private JTextField textFieldInputFile;

	private JButton btnAdd;

	private JTextField textFieldObservationId;

	private JTextField textFieldGmlIdentifier;

	private JTextField textFieldValueUnit;

	private JTextField textFieldWeight;

	private JComboBox comboBoxContinuousValues;

	private JComboBox comboBoxCategoricalValues;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JComboBox> inputFileComboBoxes;

	private List<JTextField> textFields;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 * 
	 * @param parentView
	 *            parent view to be associated with
	 */
	public UncertaintyView(IView parentView) {

		this.parentView = parentView;
		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		ImageIcon addIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Add16.gif"));

		ImageIcon importIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Import16.gif"));

		GridBagConstraints gbc_lblInputFile = new GridBagConstraints();
		gbc_lblInputFile.anchor = GridBagConstraints.WEST;
		gbc_lblInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblInputFile.gridx = 0;
		gbc_lblInputFile.gridy = 0;

		JLabel lblInputFile = new JLabel("Input file(s)*");

		GridBagConstraints gbc_textFieldInputFile = new GridBagConstraints();
		gbc_textFieldInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldInputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldInputFile.gridx = 1;
		gbc_textFieldInputFile.gridy = 0;

		textFieldInputFile = new JTextField(10);
		textFieldInputFile.setEditable(false);

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 0;

		btnAdd = new JButton("Add", addIcon);
		btnAdd.setToolTipText("Add");

		GridBagConstraints gbc_lblUncertaintyTables = new GridBagConstraints();
		gbc_lblUncertaintyTables.gridwidth = 3;
		gbc_lblUncertaintyTables.anchor = GridBagConstraints.WEST;
		gbc_lblUncertaintyTables.insets = new Insets(0, 0, 5, 5);
		gbc_lblUncertaintyTables.gridx = 0;
		gbc_lblUncertaintyTables.gridy = 1;

		JLabel lblUncertaintyTables = new JLabel("Uncertainty tables");
		lblUncertaintyTables.setFont(lblUncertaintyTables.getFont().deriveFont(
				Font.BOLD));

		GridBagConstraints gbc_lblObservationId = new GridBagConstraints();
		gbc_lblObservationId.anchor = GridBagConstraints.WEST;
		gbc_lblObservationId.insets = new Insets(0, 0, 5, 5);
		gbc_lblObservationId.gridx = 0;
		gbc_lblObservationId.gridy = 2;

		JLabel lblObservationId = new JLabel("observation_id*");

		GridBagConstraints gbc_textFieldObservationId = new GridBagConstraints();
		gbc_textFieldObservationId.gridwidth = 2;
		gbc_textFieldObservationId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldObservationId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldObservationId.gridx = 1;
		gbc_textFieldObservationId.gridy = 2;

		textFieldObservationId = new JTextField(10);
		textFieldObservationId
				.setToolTipText("Enter observation ID range, use hyphen as delimiter (mandatory)");

		GridBagConstraints gbc_lblGmlIdentifier = new GridBagConstraints();
		gbc_lblGmlIdentifier.anchor = GridBagConstraints.WEST;
		gbc_lblGmlIdentifier.insets = new Insets(0, 0, 5, 5);
		gbc_lblGmlIdentifier.gridx = 0;
		gbc_lblGmlIdentifier.gridy = 3;

		JLabel lblGmlIdentifier = new JLabel("gml_identifier*");

		GridBagConstraints gbc_textFieldGmlIdentifier = new GridBagConstraints();
		gbc_textFieldGmlIdentifier.gridwidth = 2;
		gbc_textFieldGmlIdentifier.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldGmlIdentifier.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldGmlIdentifier.gridx = 1;
		gbc_textFieldGmlIdentifier.gridy = 3;

		textFieldGmlIdentifier = new JTextField(10);
		textFieldGmlIdentifier
				.setToolTipText("Enter GML identifier (mandatory)");

		GridBagConstraints gbc_lblValueUnit = new GridBagConstraints();
		gbc_lblValueUnit.anchor = GridBagConstraints.WEST;
		gbc_lblValueUnit.insets = new Insets(0, 0, 5, 5);
		gbc_lblValueUnit.gridx = 0;
		gbc_lblValueUnit.gridy = 4;

		JLabel lblValueUnit = new JLabel("value_unit*");

		GridBagConstraints gbc_textFieldValueUnit = new GridBagConstraints();
		gbc_textFieldValueUnit.gridwidth = 2;
		gbc_textFieldValueUnit.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldValueUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldValueUnit.gridx = 1;
		gbc_textFieldValueUnit.gridy = 4;

		textFieldValueUnit = new JTextField(10);
		textFieldValueUnit.setToolTipText("Enter value unit (mandatory)");

		GridBagConstraints gbc_lblWeight = new GridBagConstraints();
		gbc_lblWeight.anchor = GridBagConstraints.WEST;
		gbc_lblWeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeight.gridx = 0;
		gbc_lblWeight.gridy = 5;

		JLabel lblWeight = new JLabel("weight");

		GridBagConstraints gbc_textFieldWeight = new GridBagConstraints();
		gbc_textFieldWeight.gridwidth = 2;
		gbc_textFieldWeight.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldWeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldWeight.gridx = 1;
		gbc_textFieldWeight.gridy = 5;

		textFieldWeight = new JTextField(10);
		textFieldWeight.setToolTipText("Enter weight");

		GridBagConstraints gbc_lblContinuousValues = new GridBagConstraints();
		gbc_lblContinuousValues.anchor = GridBagConstraints.WEST;
		gbc_lblContinuousValues.insets = new Insets(0, 0, 5, 5);
		gbc_lblContinuousValues.gridx = 0;
		gbc_lblContinuousValues.gridy = 6;

		JLabel lblContinuousValues = new JLabel("continuous_values");

		GridBagConstraints gbc_comboBoxContinuousValues = new GridBagConstraints();
		gbc_comboBoxContinuousValues.gridwidth = 2;
		gbc_comboBoxContinuousValues.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxContinuousValues.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxContinuousValues.gridx = 1;
		gbc_comboBoxContinuousValues.gridy = 6;

		comboBoxContinuousValues = new JComboBox();
		comboBoxContinuousValues
				.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_lblCategoricalValues = new GridBagConstraints();
		gbc_lblCategoricalValues.anchor = GridBagConstraints.WEST;
		gbc_lblCategoricalValues.insets = new Insets(0, 0, 5, 5);
		gbc_lblCategoricalValues.gridx = 0;
		gbc_lblCategoricalValues.gridy = 7;

		JLabel lblCategoricalValues = new JLabel("categorical_values");

		GridBagConstraints gbc_comboBoxCategoricalValues = new GridBagConstraints();
		gbc_comboBoxCategoricalValues.gridwidth = 2;
		gbc_comboBoxCategoricalValues.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxCategoricalValues.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxCategoricalValues.gridx = 1;
		gbc_comboBoxCategoricalValues.gridy = 7;

		comboBoxCategoricalValues = new JComboBox();
		comboBoxCategoricalValues
				.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 3;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 8;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add combo boxes to be reseted.
		 */
		comboBoxes = new ArrayList<JComboBox>();
		comboBoxes.add(comboBoxContinuousValues);
		comboBoxes.add(comboBoxCategoricalValues);

		/**
		 * Add input file combo boxes.
		 */
		inputFileComboBoxes = new ArrayList<JComboBox>();
		inputFileComboBoxes.add(comboBoxContinuousValues);
		inputFileComboBoxes.add(comboBoxCategoricalValues);

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldObservationId);
		textFields.add(textFieldGmlIdentifier);
		textFields.add(textFieldValueUnit);
		textFields.add(textFieldWeight);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldInputFile);
		mandatoryTextFields.add(textFieldObservationId);
		mandatoryTextFields.add(textFieldGmlIdentifier);
		mandatoryTextFields.add(textFieldValueUnit);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_uncertaintyPanel = new GridBagLayout();
		gbl_uncertaintyPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_uncertaintyPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		gbl_uncertaintyPanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_uncertaintyPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		uncertaintyPanel = new JPanel();
		uncertaintyPanel.setLayout(gbl_uncertaintyPanel);
		uncertaintyPanel.add(lblInputFile, gbc_lblInputFile);
		uncertaintyPanel.add(textFieldInputFile, gbc_textFieldInputFile);
		uncertaintyPanel.add(btnAdd, gbc_btnAdd);
		uncertaintyPanel.add(lblUncertaintyTables, gbc_lblUncertaintyTables);
		uncertaintyPanel.add(lblObservationId, gbc_lblObservationId);
		uncertaintyPanel
				.add(textFieldObservationId, gbc_textFieldObservationId);
		uncertaintyPanel.add(lblGmlIdentifier, gbc_lblGmlIdentifier);
		uncertaintyPanel
				.add(textFieldGmlIdentifier, gbc_textFieldGmlIdentifier);
		uncertaintyPanel.add(lblValueUnit, gbc_lblValueUnit);
		uncertaintyPanel.add(textFieldValueUnit, gbc_textFieldValueUnit);
		uncertaintyPanel.add(lblWeight, gbc_lblWeight);
		uncertaintyPanel.add(textFieldWeight, gbc_textFieldWeight);
		uncertaintyPanel.add(lblContinuousValues, gbc_lblContinuousValues);
		uncertaintyPanel.add(comboBoxContinuousValues,
				gbc_comboBoxContinuousValues);
		uncertaintyPanel.add(lblCategoricalValues, gbc_lblCategoricalValues);
		uncertaintyPanel.add(comboBoxCategoricalValues,
				gbc_comboBoxCategoricalValues);
		uncertaintyPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		UncertaintyModel uncertaintyModel = new UncertaintyModel();
		new UncertaintyController(this, uncertaintyModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return uncertaintyPanel;

	}

	/**
	 * Get parent view.
	 * 
	 * @return parent view
	 */
	public IView getParentView() {

		return parentView;

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
	 * Set text.
	 * 
	 * @param text
	 *            text to be set
	 */
	public void setTextFieldInputFileText(String text) {

		textFieldInputFile.setText(text);

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public Map<String, String> getUncertaintyParams() {

		Map<String, String> params = new HashMap<String, String>();

		params.put("observation_id", textFieldObservationId.getText().trim());
		params.put("gml_identifier", textFieldGmlIdentifier.getText().trim());
		params.put("value_unit", textFieldValueUnit.getText().trim());
		params.put("weight", textFieldWeight.getText().trim());
		params.put("continuous_values", comboBoxContinuousValues
				.getSelectedItem().toString().trim());
		params.put("categorical_values", comboBoxCategoricalValues
				.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Add combo box items.
	 * 
	 * @param items
	 *            combo box items to be added
	 */
	public void addInputFileComboBoxItems(List<String> items) {

		for (JComboBox inputFileComboBox : inputFileComboBoxes) {

			/**
			 * Set combo box auto resize behaviour according to the size of the
			 * enclosing container instead of the size of the list elements.
			 */
			inputFileComboBox.setPrototypeDisplayValue("");

			inputFileComboBox.removeAllItems();
			inputFileComboBox.addItem("");

			for (String item : items) {
				inputFileComboBox.addItem(item);
			}

			Utility.adjustScrollBar(inputFileComboBox);

		}

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
	public void addBtnAddListener(ActionListener l) {

		btnAdd.addActionListener(l);

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
