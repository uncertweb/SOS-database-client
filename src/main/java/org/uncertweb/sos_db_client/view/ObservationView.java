package org.uncertweb.sos_db_client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.uncertweb.sos_db_client.controller.ObservationController;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.model.MainModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class ObservationView implements IView {

	private static final Logger LOG = LogManager.getLogger(ObservationView.class);

	private IView parentView;

	private JPanel observationPanel;

	private JTextField textFieldInputFile;

	private JButton btnAdd;

	private JComboBox comboBoxTimeStamp;

	private JComboBox comboBoxProcedureId;

	private JComboBox comboBoxFeatureOfInterestId;

	private JComboBox comboBoxPhenomenonId;

	private JComboBox comboBoxOfferingId;

	private JComboBox comboBoxTextValue;

	private JComboBox comboBoxNumericValue;

	private JComboBox comboBoxSpatialValue;

	private JComboBox comboBoxMimeType;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JComboBox> mandatoryComboBoxes;

	private List<JComboBox> inputFileComboBoxes;

	private List<JComboBox> databaseComboBoxes;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 * 
	 * @param parentView
	 *            parent view to be associated with
	 */
	public ObservationView(IView parentView) {

		this.parentView = parentView;
		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		/*
		 * Register this object on EventBus.
		 */
		AnnotationProcessor.process(this);

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
		gbc_btnAdd.anchor = GridBagConstraints.EAST;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 0;

		btnAdd = new JButton("Add", addIcon);
		btnAdd.setToolTipText("Add");

		GridBagConstraints gbc_lblObservationTable = new GridBagConstraints();
		gbc_lblObservationTable.anchor = GridBagConstraints.WEST;
		gbc_lblObservationTable.insets = new Insets(0, 0, 5, 5);
		gbc_lblObservationTable.gridwidth = 3;
		gbc_lblObservationTable.gridx = 0;
		gbc_lblObservationTable.gridy = 1;

		JLabel lblObservationTable = new JLabel("Observation table");
		lblObservationTable.setFont(lblObservationTable.getFont().deriveFont(Font.BOLD));

		GridBagConstraints gbc_lblTimeStamp = new GridBagConstraints();
		gbc_lblTimeStamp.anchor = GridBagConstraints.WEST;
		gbc_lblTimeStamp.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeStamp.gridx = 0;
		gbc_lblTimeStamp.gridy = 2;

		JLabel lblTimeStamp = new JLabel("time_stamp*");

		GridBagConstraints gbc_comboBoxTimeStamp = new GridBagConstraints();
		gbc_comboBoxTimeStamp.gridwidth = 2;
		gbc_comboBoxTimeStamp.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTimeStamp.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTimeStamp.gridx = 1;
		gbc_comboBoxTimeStamp.gridy = 2;

		comboBoxTimeStamp = new JComboBox();
		comboBoxTimeStamp.setEditable(true);
		comboBoxTimeStamp
				.setToolTipText("<html>Link to appropriate input file column or enter own value, <br />must be of format <i>yyyy-MM-dd'T'HH:mm:ssZ</i> (mandatory)</html>");

		GridBagConstraints gbc_lblProcedureId = new GridBagConstraints();
		gbc_lblProcedureId.anchor = GridBagConstraints.WEST;
		gbc_lblProcedureId.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcedureId.gridx = 0;
		gbc_lblProcedureId.gridy = 3;

		JLabel lblProcedureId = new JLabel("procedure_id*");

		GridBagConstraints gbc_comboBoxProcedureId = new GridBagConstraints();
		gbc_comboBoxProcedureId.gridwidth = 2;
		gbc_comboBoxProcedureId.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcedureId.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcedureId.gridx = 1;
		gbc_comboBoxProcedureId.gridy = 3;

		comboBoxProcedureId = new JComboBox();
		comboBoxProcedureId.setToolTipText("Choose procedure ID (mandatory)");

		GridBagConstraints gbc_lblFeatureOfInterestId = new GridBagConstraints();
		gbc_lblFeatureOfInterestId.anchor = GridBagConstraints.WEST;
		gbc_lblFeatureOfInterestId.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeatureOfInterestId.gridx = 0;
		gbc_lblFeatureOfInterestId.gridy = 4;

		JLabel lblFeatureOfInterestId = new JLabel("feature_of_interest_id*");

		GridBagConstraints gbc_comboBoxFeatureOfInterestId = new GridBagConstraints();
		gbc_comboBoxFeatureOfInterestId.gridwidth = 2;
		gbc_comboBoxFeatureOfInterestId.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFeatureOfInterestId.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFeatureOfInterestId.gridx = 1;
		gbc_comboBoxFeatureOfInterestId.gridy = 4;

		comboBoxFeatureOfInterestId = new JComboBox();
		comboBoxFeatureOfInterestId.setEditable(true);
		comboBoxFeatureOfInterestId.setToolTipText("Link to appropriate input file column or enter own value (mandatory)");

		GridBagConstraints gbc_lblPhenomenonId = new GridBagConstraints();
		gbc_lblPhenomenonId.anchor = GridBagConstraints.WEST;
		gbc_lblPhenomenonId.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhenomenonId.gridx = 0;
		gbc_lblPhenomenonId.gridy = 5;

		JLabel lblPhenomenonId = new JLabel("phenomenon_id*");

		GridBagConstraints gbc_comboBoxPhenomenonId = new GridBagConstraints();
		gbc_comboBoxPhenomenonId.gridwidth = 2;
		gbc_comboBoxPhenomenonId.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPhenomenonId.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPhenomenonId.gridx = 1;
		gbc_comboBoxPhenomenonId.gridy = 5;

		comboBoxPhenomenonId = new JComboBox();
		comboBoxPhenomenonId.setToolTipText("Choose phenomenon ID (mandatory)");

		GridBagConstraints gbc_lblOfferingId = new GridBagConstraints();
		gbc_lblOfferingId.anchor = GridBagConstraints.WEST;
		gbc_lblOfferingId.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfferingId.gridx = 0;
		gbc_lblOfferingId.gridy = 6;

		JLabel lblOfferingId = new JLabel("offering_id*");

		GridBagConstraints gbc_comboBoxOfferingId = new GridBagConstraints();
		gbc_comboBoxOfferingId.gridwidth = 2;
		gbc_comboBoxOfferingId.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxOfferingId.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxOfferingId.gridx = 1;
		gbc_comboBoxOfferingId.gridy = 6;

		comboBoxOfferingId = new JComboBox();
		comboBoxOfferingId.setToolTipText("Choose offering ID (mandatory)");

		GridBagConstraints gbc_lblTextValue = new GridBagConstraints();
		gbc_lblTextValue.anchor = GridBagConstraints.WEST;
		gbc_lblTextValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblTextValue.gridx = 0;
		gbc_lblTextValue.gridy = 7;

		JLabel lblTextValue = new JLabel("text_value");

		GridBagConstraints gbc_comboBoxTextValue = new GridBagConstraints();
		gbc_comboBoxTextValue.gridwidth = 2;
		gbc_comboBoxTextValue.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTextValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTextValue.gridx = 1;
		gbc_comboBoxTextValue.gridy = 7;

		comboBoxTextValue = new JComboBox();
		comboBoxTextValue.setEditable(true);
		comboBoxTextValue.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_lblNumericValue = new GridBagConstraints();
		gbc_lblNumericValue.anchor = GridBagConstraints.WEST;
		gbc_lblNumericValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumericValue.gridx = 0;
		gbc_lblNumericValue.gridy = 8;

		JLabel lblNumericValue = new JLabel("numeric_value");

		GridBagConstraints gbc_comboBoxNumericValue = new GridBagConstraints();
		gbc_comboBoxNumericValue.gridwidth = 2;
		gbc_comboBoxNumericValue.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxNumericValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxNumericValue.gridx = 1;
		gbc_comboBoxNumericValue.gridy = 8;

		comboBoxNumericValue = new JComboBox();
		comboBoxNumericValue.setEditable(true);
		comboBoxNumericValue.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_lblSpatialValue = new GridBagConstraints();
		gbc_lblSpatialValue.anchor = GridBagConstraints.WEST;
		gbc_lblSpatialValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpatialValue.gridx = 0;
		gbc_lblSpatialValue.gridy = 9;

		JLabel lblSpatialValue = new JLabel("spatial_value");

		GridBagConstraints gbc_comboBoxSpatialValue = new GridBagConstraints();
		gbc_comboBoxSpatialValue.gridwidth = 2;
		gbc_comboBoxSpatialValue.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxSpatialValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxSpatialValue.gridx = 1;
		gbc_comboBoxSpatialValue.gridy = 9;

		comboBoxSpatialValue = new JComboBox();
		comboBoxSpatialValue.setEditable(true);
		comboBoxSpatialValue.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_lblMimeType = new GridBagConstraints();
		gbc_lblMimeType.anchor = GridBagConstraints.WEST;
		gbc_lblMimeType.insets = new Insets(0, 0, 5, 5);
		gbc_lblMimeType.gridx = 0;
		gbc_lblMimeType.gridy = 10;

		JLabel lblMimeType = new JLabel("mime_type");

		GridBagConstraints gbc_comboBoxMimeType = new GridBagConstraints();
		gbc_comboBoxMimeType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxMimeType.gridwidth = 2;
		gbc_comboBoxMimeType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMimeType.gridx = 1;
		gbc_comboBoxMimeType.gridy = 10;

		comboBoxMimeType = new JComboBox();
		comboBoxMimeType.setEditable(true);
		comboBoxMimeType.setToolTipText("Link to appropriate input file column");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 3;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 11;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add combo boxes to be reseted.
		 */
		comboBoxes = new ArrayList<JComboBox>();
		comboBoxes.add(comboBoxTimeStamp);
		comboBoxes.add(comboBoxProcedureId);
		comboBoxes.add(comboBoxFeatureOfInterestId);
		comboBoxes.add(comboBoxPhenomenonId);
		comboBoxes.add(comboBoxOfferingId);
		comboBoxes.add(comboBoxTextValue);
		comboBoxes.add(comboBoxNumericValue);
		comboBoxes.add(comboBoxSpatialValue);
		comboBoxes.add(comboBoxMimeType);

		/**
		 * Add mandatory combo boxes to be checked if empty.
		 */
		mandatoryComboBoxes = new ArrayList<JComboBox>();
		mandatoryComboBoxes.add(comboBoxTimeStamp);
		mandatoryComboBoxes.add(comboBoxProcedureId);
		mandatoryComboBoxes.add(comboBoxFeatureOfInterestId);
		mandatoryComboBoxes.add(comboBoxPhenomenonId);
		mandatoryComboBoxes.add(comboBoxOfferingId);

		/**
		 * Add input file combo boxes.
		 */
		inputFileComboBoxes = new ArrayList<JComboBox>();
		inputFileComboBoxes.add(comboBoxTimeStamp);
		inputFileComboBoxes.add(comboBoxFeatureOfInterestId);
		inputFileComboBoxes.add(comboBoxTextValue);
		inputFileComboBoxes.add(comboBoxNumericValue);
		inputFileComboBoxes.add(comboBoxSpatialValue);
		inputFileComboBoxes.add(comboBoxMimeType);

		/**
		 * Add database combo boxes.
		 */
		databaseComboBoxes = new ArrayList<JComboBox>();
		databaseComboBoxes.add(comboBoxProcedureId);
		databaseComboBoxes.add(comboBoxPhenomenonId);
		databaseComboBoxes.add(comboBoxOfferingId);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldInputFile);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_observationPanel = new GridBagLayout();
		gbl_observationPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_observationPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_observationPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_observationPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		observationPanel = new JPanel();
		observationPanel.setLayout(gbl_observationPanel);
		observationPanel.add(lblInputFile, gbc_lblInputFile);
		observationPanel.add(textFieldInputFile, gbc_textFieldInputFile);
		observationPanel.add(btnAdd, gbc_btnAdd);
		observationPanel.add(lblObservationTable, gbc_lblObservationTable);
		observationPanel.add(lblTimeStamp, gbc_lblTimeStamp);
		observationPanel.add(comboBoxTimeStamp, gbc_comboBoxTimeStamp);
		observationPanel.add(lblProcedureId, gbc_lblProcedureId);
		observationPanel.add(comboBoxProcedureId, gbc_comboBoxProcedureId);
		observationPanel.add(lblFeatureOfInterestId, gbc_lblFeatureOfInterestId);
		observationPanel.add(comboBoxFeatureOfInterestId, gbc_comboBoxFeatureOfInterestId);
		observationPanel.add(lblPhenomenonId, gbc_lblPhenomenonId);
		observationPanel.add(comboBoxPhenomenonId, gbc_comboBoxPhenomenonId);
		observationPanel.add(lblOfferingId, gbc_lblOfferingId);
		observationPanel.add(comboBoxOfferingId, gbc_comboBoxOfferingId);
		observationPanel.add(lblTextValue, gbc_lblTextValue);
		observationPanel.add(comboBoxTextValue, gbc_comboBoxTextValue);
		observationPanel.add(lblNumericValue, gbc_lblNumericValue);
		observationPanel.add(comboBoxNumericValue, gbc_comboBoxNumericValue);
		observationPanel.add(lblSpatialValue, gbc_lblSpatialValue);
		observationPanel.add(comboBoxSpatialValue, gbc_comboBoxSpatialValue);
		observationPanel.add(lblMimeType, gbc_lblMimeType);
		observationPanel.add(comboBoxMimeType, gbc_comboBoxMimeType);
		observationPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		MainModel mainModel = new MainModel();
		new ObservationController(this, mainModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return observationPanel;

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
	public List<String> getObservationParams() {

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
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = UpdateEvent.class)
	public void addDatabaseComboBoxItems(UpdateEvent event) {

		String jdbcUrl = ApplicationProperties.getJdbcUrl();
		List<String> procedureIds;
		List<String> phenomenonIds;
		List<String> offeringIds;

		try {
			procedureIds = Repository.getDataSetIds(jdbcUrl, "procedure");
			phenomenonIds = Repository.getDataSetIds(jdbcUrl, "phenomenon");
			offeringIds = Repository.getDataSetIds(jdbcUrl, "offering");
		} catch(IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
			return;
		}

		for(JComboBox databaseComboBox : databaseComboBoxes) {

			/**
			 * Set combo box auto resize behaviour according to the size of the enclosing container instead of the size of the list
			 * elements.
			 */
			databaseComboBox.setPrototypeDisplayValue("");

			databaseComboBox.removeAllItems();
			databaseComboBox.addItem("");

			Utility.adjustScrollBar(databaseComboBox);

		}

		for(String procedureId : procedureIds) {
			comboBoxProcedureId.addItem(procedureId);
		}

		for(String phenomenonId : phenomenonIds) {
			comboBoxPhenomenonId.addItem(phenomenonId);
		}

		for(String offeringId : offeringIds) {
			comboBoxOfferingId.addItem(offeringId);
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
