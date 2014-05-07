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
import org.uncertweb.sos_db_client.controller.RelationshipController;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.model.RelationshipModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class RelationshipView implements IView {

	private static final Logger LOG = LogManager.getLogger(RelationshipView.class);

	private JPanel relationshipPanel;

	private JTextField textFieldFoiOffLeft;

	private JComboBox comboBoxFoiOffRight;

	private JComboBox comboBoxPhenOffLeft;

	private JComboBox comboBoxPhenOffRight;

	private JComboBox comboBoxProcFoiLeft;

	private JTextField textFieldProcFoiRight;

	private JComboBox comboBoxProcOffLeft;

	private JComboBox comboBoxProcOffRight;

	private JComboBox comboBoxProcPhenLeft;

	private JComboBox comboBoxProcPhenRight;

	private JComboBox comboBoxProcProcLeft;

	private JComboBox comboBoxProcProcRight;

	private JButton btnImport;

	private List<JComboBox> comboBoxes;

	private List<JTextField> textFields;

	/**
	 * Create view.
	 */
	public RelationshipView() {

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

		ImageIcon importIcon = new ImageIcon(getClass().getResource(Resource.IMAGE_PATH + "Import16.gif"));

		GridBagConstraints gbc_lblRelationshipTables = new GridBagConstraints();
		gbc_lblRelationshipTables.anchor = GridBagConstraints.WEST;
		gbc_lblRelationshipTables.gridwidth = 3;
		gbc_lblRelationshipTables.insets = new Insets(0, 0, 5, 0);
		gbc_lblRelationshipTables.gridx = 0;
		gbc_lblRelationshipTables.gridy = 0;

		JLabel lblRelationshipTables = new JLabel("Relationship tables");
		lblRelationshipTables.setFont(lblRelationshipTables.getFont().deriveFont(Font.BOLD));

		/**
		 * foi_off.
		 */
		GridBagConstraints gbc_lblFoiOff = new GridBagConstraints();
		gbc_lblFoiOff.anchor = GridBagConstraints.WEST;
		gbc_lblFoiOff.insets = new Insets(0, 0, 5, 5);
		gbc_lblFoiOff.gridx = 0;
		gbc_lblFoiOff.gridy = 1;

		JLabel lblFoiOff = new JLabel("foi_off");

		GridBagConstraints gbc_textFieldFoiOffLeft = new GridBagConstraints();
		gbc_textFieldFoiOffLeft.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldFoiOffLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFoiOffLeft.gridx = 1;
		gbc_textFieldFoiOffLeft.gridy = 1;

		textFieldFoiOffLeft = new JTextField(10);
		textFieldFoiOffLeft.setToolTipText("Enter Feature of Interest ID prefix, must end with an underscore");

		GridBagConstraints gbc_comboBoxFoiOffRight = new GridBagConstraints();
		gbc_comboBoxFoiOffRight.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFoiOffRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFoiOffRight.gridx = 2;
		gbc_comboBoxFoiOffRight.gridy = 1;

		comboBoxFoiOffRight = new JComboBox();
		comboBoxFoiOffRight.setToolTipText("Choose offering ID");

		/**
		 * phen_off.
		 */
		GridBagConstraints gbc_lblPhenOff = new GridBagConstraints();
		gbc_lblPhenOff.anchor = GridBagConstraints.WEST;
		gbc_lblPhenOff.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhenOff.gridx = 0;
		gbc_lblPhenOff.gridy = 2;

		JLabel lblPhenOff = new JLabel("phen_off");

		GridBagConstraints gbc_comboBoxPhenOffLeft = new GridBagConstraints();
		gbc_comboBoxPhenOffLeft.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPhenOffLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPhenOffLeft.gridx = 1;
		gbc_comboBoxPhenOffLeft.gridy = 2;

		comboBoxPhenOffLeft = new JComboBox();
		comboBoxPhenOffLeft.setToolTipText("Choose phenomenon ID");

		GridBagConstraints gbc_comboBoxPhenOffRight = new GridBagConstraints();
		gbc_comboBoxPhenOffRight.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPhenOffRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPhenOffRight.gridx = 2;
		gbc_comboBoxPhenOffRight.gridy = 2;

		comboBoxPhenOffRight = new JComboBox();
		comboBoxPhenOffRight.setToolTipText("Choose offering ID");

		/**
		 * proc_foi.
		 */
		GridBagConstraints gbc_lblProcFoi = new GridBagConstraints();
		gbc_lblProcFoi.anchor = GridBagConstraints.WEST;
		gbc_lblProcFoi.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcFoi.gridx = 0;
		gbc_lblProcFoi.gridy = 3;

		JLabel lblProcFoi = new JLabel("proc_foi");

		GridBagConstraints gbc_comboBoxProcFoiLeft = new GridBagConstraints();
		gbc_comboBoxProcFoiLeft.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcFoiLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcFoiLeft.gridx = 1;
		gbc_comboBoxProcFoiLeft.gridy = 3;

		comboBoxProcFoiLeft = new JComboBox();
		comboBoxProcFoiLeft.setToolTipText("Choose procedure ID");

		GridBagConstraints gbc_textFieldProcFoiRight = new GridBagConstraints();
		gbc_textFieldProcFoiRight.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldProcFoiRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldProcFoiRight.gridx = 2;
		gbc_textFieldProcFoiRight.gridy = 3;

		textFieldProcFoiRight = new JTextField(10);
		textFieldProcFoiRight.setToolTipText("Enter Feature of Interest ID prefix, must end with an underscore");

		/**
		 * proc_off.
		 */
		GridBagConstraints gbc_lblProcOff = new GridBagConstraints();
		gbc_lblProcOff.anchor = GridBagConstraints.WEST;
		gbc_lblProcOff.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcOff.gridx = 0;
		gbc_lblProcOff.gridy = 4;

		JLabel lblProcOff = new JLabel("proc_off");

		GridBagConstraints gbc_comboBoxProcOffLeft = new GridBagConstraints();
		gbc_comboBoxProcOffLeft.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcOffLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcOffLeft.gridx = 1;
		gbc_comboBoxProcOffLeft.gridy = 4;

		comboBoxProcOffLeft = new JComboBox();
		comboBoxProcOffLeft.setToolTipText("Choose procedure ID");

		GridBagConstraints gbc_comboBoxProcOffRight = new GridBagConstraints();
		gbc_comboBoxProcOffRight.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcOffRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcOffRight.gridx = 2;
		gbc_comboBoxProcOffRight.gridy = 4;

		comboBoxProcOffRight = new JComboBox();
		comboBoxProcOffRight.setToolTipText("Choose offering ID");

		/**
		 * proc_phen.
		 */
		GridBagConstraints gbc_lblProcPhen = new GridBagConstraints();
		gbc_lblProcPhen.anchor = GridBagConstraints.WEST;
		gbc_lblProcPhen.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcPhen.gridx = 0;
		gbc_lblProcPhen.gridy = 5;

		JLabel lblProcPhen = new JLabel("proc_phen");

		GridBagConstraints gbc_comboBoxProcPhenLeft = new GridBagConstraints();
		gbc_comboBoxProcPhenLeft.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcPhenLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcPhenLeft.gridx = 1;
		gbc_comboBoxProcPhenLeft.gridy = 5;

		comboBoxProcPhenLeft = new JComboBox();
		comboBoxProcPhenLeft.setToolTipText("Choose procedure ID");

		GridBagConstraints gbc_comboBoxProcPhenRight = new GridBagConstraints();
		gbc_comboBoxProcPhenRight.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcPhenRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcPhenRight.gridx = 2;
		gbc_comboBoxProcPhenRight.gridy = 5;

		comboBoxProcPhenRight = new JComboBox();
		comboBoxProcPhenRight.setToolTipText("Choose phenomenon ID");

		/**
		 * proc_proc.
		 */
		GridBagConstraints gbc_lblProcProc = new GridBagConstraints();
		gbc_lblProcProc.anchor = GridBagConstraints.WEST;
		gbc_lblProcProc.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcProc.gridx = 0;
		gbc_lblProcProc.gridy = 6;

		JLabel lblProcProc = new JLabel("proc_proc");

		GridBagConstraints gbc_comboBoxProcProcLeft = new GridBagConstraints();
		gbc_comboBoxProcProcLeft.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcProcLeft.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcProcLeft.gridx = 1;
		gbc_comboBoxProcProcLeft.gridy = 6;

		comboBoxProcProcLeft = new JComboBox();
		comboBoxProcProcLeft.setToolTipText("Choose parent procedure ID");

		GridBagConstraints gbc_comboBoxProcProcRight = new GridBagConstraints();
		gbc_comboBoxProcProcRight.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxProcProcRight.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxProcProcRight.gridx = 2;
		gbc_comboBoxProcProcRight.gridy = 6;

		comboBoxProcProcRight = new JComboBox();
		comboBoxProcProcRight.setToolTipText("Choose child procedure ID");

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.anchor = GridBagConstraints.EAST;
		gbc_btnImport.gridwidth = 3;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 7;

		btnImport = new JButton("Import", importIcon);
		btnImport.setToolTipText("Import");

		/**
		 * Add combo boxes to be reseted.
		 */
		comboBoxes = new ArrayList<JComboBox>();
		comboBoxes.add(comboBoxFoiOffRight);
		comboBoxes.add(comboBoxPhenOffLeft);
		comboBoxes.add(comboBoxPhenOffRight);
		comboBoxes.add(comboBoxProcFoiLeft);
		comboBoxes.add(comboBoxProcOffLeft);
		comboBoxes.add(comboBoxProcOffRight);
		comboBoxes.add(comboBoxProcPhenLeft);
		comboBoxes.add(comboBoxProcPhenRight);
		comboBoxes.add(comboBoxProcProcLeft);
		comboBoxes.add(comboBoxProcProcRight);

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldFoiOffLeft);
		textFields.add(textFieldProcFoiRight);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_relationshipPanel = new GridBagLayout();
		gbl_relationshipPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_relationshipPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_relationshipPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_relationshipPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		relationshipPanel = new JPanel();
		relationshipPanel.setLayout(gbl_relationshipPanel);
		relationshipPanel.add(lblRelationshipTables, gbc_lblRelationshipTables);
		relationshipPanel.add(lblFoiOff, gbc_lblFoiOff);
		relationshipPanel.add(textFieldFoiOffLeft, gbc_textFieldFoiOffLeft);
		relationshipPanel.add(comboBoxFoiOffRight, gbc_comboBoxFoiOffRight);
		relationshipPanel.add(lblPhenOff, gbc_lblPhenOff);
		relationshipPanel.add(comboBoxPhenOffLeft, gbc_comboBoxPhenOffLeft);
		relationshipPanel.add(comboBoxPhenOffRight, gbc_comboBoxPhenOffRight);
		relationshipPanel.add(lblProcFoi, gbc_lblProcFoi);
		relationshipPanel.add(comboBoxProcFoiLeft, gbc_comboBoxProcFoiLeft);
		relationshipPanel.add(textFieldProcFoiRight, gbc_textFieldProcFoiRight);
		relationshipPanel.add(lblProcOff, gbc_lblProcOff);
		relationshipPanel.add(comboBoxProcOffLeft, gbc_comboBoxProcOffLeft);
		relationshipPanel.add(comboBoxProcOffRight, gbc_comboBoxProcOffRight);
		relationshipPanel.add(lblProcPhen, gbc_lblProcPhen);
		relationshipPanel.add(comboBoxProcPhenLeft, gbc_comboBoxProcPhenLeft);
		relationshipPanel.add(comboBoxProcPhenRight, gbc_comboBoxProcPhenRight);
		relationshipPanel.add(lblProcProc, gbc_lblProcProc);
		relationshipPanel.add(comboBoxProcProcLeft, gbc_comboBoxProcProcLeft);
		relationshipPanel.add(comboBoxProcProcRight, gbc_comboBoxProcProcRight);
		relationshipPanel.add(btnImport, gbc_btnImport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		RelationshipModel relationshipModel = new RelationshipModel();
		new RelationshipController(this, relationshipModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JPanel getRootComponent() {

		return relationshipPanel;

	}

	/**
	 * Reset fields.
	 */
	public void reset() {

		for(JTextField textField : textFields) {
			textField.setText("");
		}

		for(JComboBox comboBox : comboBoxes) {
			comboBox.setSelectedItem("");
		}

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getFoiOffParams() {

		List<String> params = new ArrayList<String>();

		params.add(textFieldFoiOffLeft.getText().trim());
		params.add(comboBoxFoiOffRight.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getPhenOffParams() {

		List<String> params = new ArrayList<String>();

		params.add(comboBoxPhenOffLeft.getSelectedItem().toString().trim());
		params.add(comboBoxPhenOffRight.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getProcFoiParams() {

		List<String> params = new ArrayList<String>();

		params.add(comboBoxProcFoiLeft.getSelectedItem().toString().trim());
		params.add(textFieldProcFoiRight.getText().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getProcOffParams() {

		List<String> params = new ArrayList<String>();

		params.add(comboBoxProcOffLeft.getSelectedItem().toString().trim());
		params.add(comboBoxProcOffRight.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getProcPhenParams() {

		List<String> params = new ArrayList<String>();

		params.add(comboBoxProcPhenLeft.getSelectedItem().toString().trim());
		params.add(comboBoxProcPhenRight.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Get parameters.
	 * 
	 * @return parameters
	 */
	public List<String> getProcProcParams() {

		List<String> params = new ArrayList<String>();

		params.add(comboBoxProcProcLeft.getSelectedItem().toString().trim());
		params.add(comboBoxProcProcRight.getSelectedItem().toString().trim());

		Utility.setEmptyStringsToNull(params);

		return params;

	}

	/**
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = UpdateEvent.class)
	public void addComboBoxItems(UpdateEvent event) {

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

		for(JComboBox comboBox : comboBoxes) {

			/**
			 * Set combo box auto resize behaviour according to the size of the enclosing container instead of the size of the list
			 * elements.
			 */
			comboBox.setPrototypeDisplayValue("");

			comboBox.removeAllItems();
			comboBox.addItem("");

			Utility.adjustScrollBar(comboBox);

		}

		for(String offeringId : offeringIds) {
			comboBoxFoiOffRight.addItem(offeringId);
			comboBoxPhenOffRight.addItem(offeringId);
			comboBoxProcOffRight.addItem(offeringId);
		}

		for(String phenomenonId : phenomenonIds) {
			comboBoxPhenOffLeft.addItem(phenomenonId);
			comboBoxProcPhenRight.addItem(phenomenonId);
		}

		for(String procedureId : procedureIds) {
			comboBoxProcFoiLeft.addItem(procedureId);
			comboBoxProcOffLeft.addItem(procedureId);
			comboBoxProcPhenLeft.addItem(procedureId);
			comboBoxProcProcLeft.addItem(procedureId);
			comboBoxProcProcRight.addItem(procedureId);
		}

	}

	/**
	 * Check if at least one mandatory field is empty.
	 * 
	 * @return true if at least one mandatory field is empty, false if not
	 */
	public boolean IsMandatoryFieldEmpty() {

		boolean isAllEmpty = true;

		boolean isLeftEmpty = textFieldFoiOffLeft.getText().trim().isEmpty();
		boolean isRightEmpty = comboBoxFoiOffRight.getSelectedItem() == null
				|| comboBoxFoiOffRight.getSelectedItem().toString().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		isLeftEmpty = comboBoxPhenOffLeft.getSelectedItem() == null || comboBoxPhenOffLeft.getSelectedItem().toString().trim().isEmpty();
		isRightEmpty = comboBoxPhenOffRight.getSelectedItem() == null || comboBoxPhenOffRight.getSelectedItem().toString().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		isLeftEmpty = comboBoxProcFoiLeft.getSelectedItem() == null || comboBoxProcFoiLeft.getSelectedItem().toString().trim().isEmpty();
		isRightEmpty = textFieldProcFoiRight.getText().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		isLeftEmpty = comboBoxProcOffLeft.getSelectedItem() == null || comboBoxProcOffLeft.getSelectedItem().toString().trim().isEmpty();
		isRightEmpty = comboBoxProcOffRight.getSelectedItem() == null || comboBoxProcOffRight.getSelectedItem().toString().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		isLeftEmpty = comboBoxProcPhenLeft.getSelectedItem() == null || comboBoxProcPhenLeft.getSelectedItem().toString().trim().isEmpty();
		isRightEmpty = comboBoxProcPhenRight.getSelectedItem() == null
				|| comboBoxProcPhenRight.getSelectedItem().toString().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		isLeftEmpty = comboBoxProcProcLeft.getSelectedItem() == null || comboBoxProcProcLeft.getSelectedItem().toString().trim().isEmpty();
		isRightEmpty = comboBoxProcProcRight.getSelectedItem() == null
				|| comboBoxProcProcRight.getSelectedItem().toString().trim().isEmpty();

		if(isLeftEmpty != isRightEmpty) {
			btnImport.setEnabled(false);
			return true;
		}

		else if(!isLeftEmpty && !isRightEmpty) {
			isAllEmpty = false;
		}

		if(isAllEmpty) {
			btnImport.setEnabled(false);
			return true;
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

		for(JComboBox comboBox : comboBoxes) {
			comboBox.addItemListener(l);
		}

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addMandatoryTextFieldsListener(DocumentListener l) {

		for(JTextField textField : textFields) {
			textField.getDocument().addDocumentListener(l);
		}

	}

}
