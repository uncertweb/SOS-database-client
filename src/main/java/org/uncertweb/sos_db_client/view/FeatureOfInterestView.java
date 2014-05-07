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

import org.uncertweb.sos_db_client.controller.FeatureOfInterestController;
import org.uncertweb.sos_db_client.model.FeatureOfInterestModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class FeatureOfInterestView implements IView {

	private IView parentView;

	private JPanel featureOfInterestPanel;

	private JTextField textFieldInputFile;

	private JButton btnAdd;

	private JComboBox comboBoxFeatureOfInterestId;

	private JComboBox comboBoxFeatureOfInterestName;

	private JComboBox comboBoxFeatureOfInterestDescription;

	private JComboBox comboBoxGeom;

	private JComboBox comboBoxFeatureType;

	private JComboBox comboBoxSchemaLink;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JComboBox> mandatoryComboBoxes;

	private List<JComboBox> inputFileComboBoxes;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 * 
	 * @param parentView
	 *            parent view to be associated with
	 */
	public FeatureOfInterestView(IView parentView) {

		this.parentView = parentView;
		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		ImageIcon addIcon = new ImageIcon(getClass().getResource(Resource.IMAGE_PATH + "Add16.gif"));

		ImageIcon importIcon = new ImageIcon(getClass().getResource(Resource.IMAGE_PATH + "Import16.gif"));

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

		GridBagConstraints gbc_lblFeatureOfInterestTable = new GridBagConstraints();
		gbc_lblFeatureOfInterestTable.gridwidth = 3;
		gbc_lblFeatureOfInterestTable.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureOfInterestTable.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureOfInterestTable.gridx = 0;
		gbc_lblFeatureOfInterestTable.gridy = 1;

		JLabel lblFeatureOfInterestTable = new JLabel("Feature of Interest table");
		lblFeatureOfInterestTable.setFont(lblFeatureOfInterestTable.getFont().deriveFont(Font.BOLD));

		GridBagConstraints gbc_lblFeatureOfInterestId = new GridBagConstraints();
		gbc_lblFeatureOfInterestId.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureOfInterestId.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureOfInterestId.gridx = 0;
		gbc_lblFeatureOfInterestId.gridy = 2;

		JLabel lblFeatureOfInterestId = new JLabel("feature_of_interest_id*");

		GridBagConstraints gbc_comboBoxFeatureOfInterestId = new GridBagConstraints();
		gbc_comboBoxFeatureOfInterestId.gridwidth = 2;
		gbc_comboBoxFeatureOfInterestId.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFeatureOfInterestId.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFeatureOfInterestId.gridx = 1;
		gbc_comboBoxFeatureOfInterestId.gridy = 2;

		comboBoxFeatureOfInterestId = new JComboBox();
		comboBoxFeatureOfInterestId.setToolTipText("Link to appropriate input file column (mandatory)");

		GridBagConstraints gbc_lblFeatureOfInterestName = new GridBagConstraints();
		gbc_lblFeatureOfInterestName.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureOfInterestName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureOfInterestName.gridx = 0;
		gbc_lblFeatureOfInterestName.gridy = 3;

		JLabel lblFeatureOfInterestName = new JLabel("feature_of_interest_name*");

		GridBagConstraints gbc_comboBoxFeatureOfInterestName = new GridBagConstraints();
		gbc_comboBoxFeatureOfInterestName.gridwidth = 2;
		gbc_comboBoxFeatureOfInterestName.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFeatureOfInterestName.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFeatureOfInterestName.gridx = 1;
		gbc_comboBoxFeatureOfInterestName.gridy = 3;

		comboBoxFeatureOfInterestName = new JComboBox();
		comboBoxFeatureOfInterestName.setEditable(true);
		comboBoxFeatureOfInterestName.setToolTipText("Link to appropriate input file column or enter own value (mandatory)");

		GridBagConstraints gbc_lblFeatureOfInterestDescription = new GridBagConstraints();
		gbc_lblFeatureOfInterestDescription.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureOfInterestDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureOfInterestDescription.gridx = 0;
		gbc_lblFeatureOfInterestDescription.gridy = 4;

		JLabel lblFeatureOfInterestDescription = new JLabel("feature_of_interest_description");

		GridBagConstraints gbc_comboBoxFeatureOfInterestDescription = new GridBagConstraints();
		gbc_comboBoxFeatureOfInterestDescription.gridwidth = 2;
		gbc_comboBoxFeatureOfInterestDescription.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFeatureOfInterestDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFeatureOfInterestDescription.gridx = 1;
		gbc_comboBoxFeatureOfInterestDescription.gridy = 4;

		comboBoxFeatureOfInterestDescription = new JComboBox();
		comboBoxFeatureOfInterestDescription.setEditable(true);
		comboBoxFeatureOfInterestDescription.setToolTipText("Link to appropriate input file column or enter own value");

		GridBagConstraints gbc_lblGeom = new GridBagConstraints();
		gbc_lblGeom.anchor = GridBagConstraints.WEST;
		gbc_lblGeom.insets = new Insets(0, 0, 5, 5);
		gbc_lblGeom.gridx = 0;
		gbc_lblGeom.gridy = 5;

		JLabel lblGeom = new JLabel("geom*");

		GridBagConstraints gbc_comboBoxGeom = new GridBagConstraints();
		gbc_comboBoxGeom.gridwidth = 2;
		gbc_comboBoxGeom.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxGeom.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxGeom.gridx = 1;
		gbc_comboBoxGeom.gridy = 5;

		comboBoxGeom = new JComboBox();
		comboBoxGeom.setToolTipText("Link to appropriate input file column (mandatory)");

		GridBagConstraints gbc_lblFeatureType = new GridBagConstraints();
		gbc_lblFeatureType.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureType.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureType.gridx = 0;
		gbc_lblFeatureType.gridy = 6;

		JLabel lblFeatureType = new JLabel("feature_type*");

		GridBagConstraints gbc_comboBoxFeatureType = new GridBagConstraints();
		gbc_comboBoxFeatureType.gridwidth = 2;
		gbc_comboBoxFeatureType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFeatureType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFeatureType.gridx = 1;
		gbc_comboBoxFeatureType.gridy = 6;

		comboBoxFeatureType = new JComboBox(new String[] { "", "sa:SamplingPoint", "sa:SamplingSurface" });
		comboBoxFeatureType
				.setToolTipText("<html>Choose <i>sa:SamplingPoint</i> for features with point as geometry<br />or <i>sa:SamplingSurface</i> for features with polygon as geometry (mandatory)</html>");

		GridBagConstraints gbc_lblSchemaLink = new GridBagConstraints();
		gbc_lblSchemaLink.anchor = GridBagConstraints.WEST;
		gbc_lblSchemaLink.insets = new Insets(0, 0, 5, 5);
		gbc_lblSchemaLink.gridx = 0;
		gbc_lblSchemaLink.gridy = 7;

		JLabel lblSchemaLink = new JLabel("schema_link");

		GridBagConstraints gbc_comboBoxSchemaLink = new GridBagConstraints();
		gbc_comboBoxSchemaLink.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxSchemaLink.gridwidth = 2;
		gbc_comboBoxSchemaLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxSchemaLink.gridx = 1;
		gbc_comboBoxSchemaLink.gridy = 7;

		comboBoxSchemaLink = new JComboBox();
		comboBoxSchemaLink.setEditable(true);
		comboBoxSchemaLink.setToolTipText("Link to appropriate input file column or enter own value");

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
		comboBoxes.add(comboBoxFeatureOfInterestId);
		comboBoxes.add(comboBoxFeatureOfInterestName);
		comboBoxes.add(comboBoxFeatureOfInterestDescription);
		comboBoxes.add(comboBoxGeom);
		comboBoxes.add(comboBoxFeatureType);
		comboBoxes.add(comboBoxSchemaLink);

		/**
		 * Add mandatory combo boxes to be checked if empty.
		 */
		mandatoryComboBoxes = new ArrayList<JComboBox>();
		mandatoryComboBoxes.add(comboBoxFeatureOfInterestId);
		mandatoryComboBoxes.add(comboBoxFeatureOfInterestName);
		mandatoryComboBoxes.add(comboBoxGeom);
		mandatoryComboBoxes.add(comboBoxFeatureType);

		/**
		 * Add input file combo boxes.
		 */
		inputFileComboBoxes = new ArrayList<JComboBox>();
		inputFileComboBoxes.add(comboBoxFeatureOfInterestId);
		inputFileComboBoxes.add(comboBoxFeatureOfInterestName);
		inputFileComboBoxes.add(comboBoxFeatureOfInterestDescription);
		inputFileComboBoxes.add(comboBoxGeom);
		inputFileComboBoxes.add(comboBoxSchemaLink);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldInputFile);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_featureOfInterestPanel = new GridBagLayout();
		gbl_featureOfInterestPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_featureOfInterestPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_featureOfInterestPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_featureOfInterestPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		featureOfInterestPanel = new JPanel();
		featureOfInterestPanel.setLayout(gbl_featureOfInterestPanel);
		featureOfInterestPanel.add(lblInputFile, gbc_lblInputFile);
		featureOfInterestPanel.add(textFieldInputFile, gbc_textFieldInputFile);
		featureOfInterestPanel.add(btnAdd, gbc_btnAdd);
		featureOfInterestPanel.add(lblFeatureOfInterestTable, gbc_lblFeatureOfInterestTable);
		featureOfInterestPanel.add(lblFeatureOfInterestId, gbc_lblFeatureOfInterestId);
		featureOfInterestPanel.add(comboBoxFeatureOfInterestId, gbc_comboBoxFeatureOfInterestId);
		featureOfInterestPanel.add(lblFeatureOfInterestName, gbc_lblFeatureOfInterestName);
		featureOfInterestPanel.add(comboBoxFeatureOfInterestName, gbc_comboBoxFeatureOfInterestName);
		featureOfInterestPanel.add(lblFeatureOfInterestDescription, gbc_lblFeatureOfInterestDescription);
		featureOfInterestPanel.add(comboBoxFeatureOfInterestDescription, gbc_comboBoxFeatureOfInterestDescription);
		featureOfInterestPanel.add(lblGeom, gbc_lblGeom);
		featureOfInterestPanel.add(comboBoxGeom, gbc_comboBoxGeom);
		featureOfInterestPanel.add(lblFeatureType, gbc_lblFeatureType);
		featureOfInterestPanel.add(comboBoxFeatureType, gbc_comboBoxFeatureType);
		featureOfInterestPanel.add(lblSchemaLink, gbc_lblSchemaLink);
		featureOfInterestPanel.add(comboBoxSchemaLink, gbc_comboBoxSchemaLink);
		featureOfInterestPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		FeatureOfInterestModel featureOfInterestModel = new FeatureOfInterestModel();
		new FeatureOfInterestController(this, featureOfInterestModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return featureOfInterestPanel;

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

		for(JComboBox comboBox : comboBoxes) {
			comboBox.setSelectedItem("");
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
	public List<String> getFeatureOfInterestParams() {

		List<String> params = new ArrayList<String>();

		for(JComboBox comboBox : comboBoxes) {
			params.add(comboBox.getSelectedItem().toString().trim());
		}

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

		for(JComboBox inputFileComboBox : inputFileComboBoxes) {

			/**
			 * Set combo box auto resize behaviour according to the size of the enclosing container instead of the size of the list
			 * elements.
			 */
			inputFileComboBox.setPrototypeDisplayValue("");

			inputFileComboBox.removeAllItems();
			inputFileComboBox.addItem("");

			for(String item : items) {
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

		for(JComboBox mandatoryComboBox : mandatoryComboBoxes) {
			Object selectedItem = mandatoryComboBox.getSelectedItem();
			if(selectedItem == null || selectedItem.toString().trim().isEmpty()) {
				btnImport.setEnabled(false);
				return true;
			}
		}

		for(JTextField mandatoryTextField : mandatoryTextFields) {
			if(mandatoryTextField.getText().trim().isEmpty()) {
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
	public void addMandatoryComboBoxesListener(ItemListener l) {

		for(JComboBox mandatoryComboBox : mandatoryComboBoxes) {
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

		for(JTextField mandatoryTextField : mandatoryTextFields) {
			mandatoryTextField.getDocument().addDocumentListener(l);
		}

	}

}