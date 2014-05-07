package org.uncertweb.sos_db_client.view;

import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import org.uncertweb.sos_db_client.controller.FeraController;
import org.uncertweb.sos_db_client.model.FeraModel;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Resource;

public class FeraView implements IView {

	private IView parentView;

	private JPanel feraPanel;

	private JTextField textFieldInputFile;

	private JButton btnAdd;

	private JTextField textFieldFirstYearOfRun;

	private JButton btnExport;

	private List<JTextField> textFields;

	private List<JTextField> mandatoryTextFields;

	/**
	 * Create view.
	 */
	public FeraView(IView parentView) {

		this.parentView = parentView;
		initComponents();
		registerController();

	}

	public void initComponents() {

		ImageIcon addIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Add16.gif"));

		ImageIcon exportIcon = new ImageIcon(getClass().getResource(
				Resource.IMAGE_PATH + "Export16.gif"));

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

		GridBagConstraints gbc_lblFeraAddOn = new GridBagConstraints();
		gbc_lblFeraAddOn.gridwidth = 3;
		gbc_lblFeraAddOn.anchor = GridBagConstraints.WEST;
		gbc_lblFeraAddOn.insets = new Insets(0, 0, 5, 5);
		gbc_lblFeraAddOn.gridx = 0;
		gbc_lblFeraAddOn.gridy = 1;

		JLabel lblFeraAddOn = new JLabel("FERA Add-On");
		lblFeraAddOn.setFont(lblFeraAddOn.getFont().deriveFont(Font.BOLD));

		GridBagConstraints gbc_lblFirstYearOfRun = new GridBagConstraints();
		gbc_lblFirstYearOfRun.anchor = GridBagConstraints.WEST;
		gbc_lblFirstYearOfRun.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstYearOfRun.gridx = 0;
		gbc_lblFirstYearOfRun.gridy = 2;

		JLabel lblFirstYearOfRun = new JLabel("First year of run*");

		GridBagConstraints gbc_textFieldFirstYearOfRun = new GridBagConstraints();
		gbc_textFieldFirstYearOfRun.gridwidth = 2;
		gbc_textFieldFirstYearOfRun.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldFirstYearOfRun.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFirstYearOfRun.gridx = 1;
		gbc_textFieldFirstYearOfRun.gridy = 2;

		textFieldFirstYearOfRun = new JTextField(10);
		textFieldFirstYearOfRun
				.setToolTipText("Enter first year of run, take account of leap years (mandatory)");

		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.EAST;
		gbc_btnExport.gridwidth = 3;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 3;

		btnExport = new JButton("Export", exportIcon);
		btnExport.setToolTipText("Export");

		/**
		 * Add text fields to be reseted.
		 */
		textFields = new ArrayList<JTextField>();
		textFields.add(textFieldFirstYearOfRun);

		/**
		 * Add mandatory text fields to be checked if empty.
		 */
		mandatoryTextFields = new ArrayList<JTextField>();
		mandatoryTextFields.add(textFieldInputFile);
		mandatoryTextFields.add(textFieldFirstYearOfRun);

		IsMandatoryFieldEmpty();

		GridBagLayout gbl_feraPanel = new GridBagLayout();
		gbl_feraPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_feraPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_feraPanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_feraPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };

		feraPanel = new JPanel();
		feraPanel.setLayout(gbl_feraPanel);
		feraPanel.add(lblInputFile, gbc_lblInputFile);
		feraPanel.add(textFieldInputFile, gbc_textFieldInputFile);
		feraPanel.add(btnAdd, gbc_btnAdd);
		feraPanel.add(lblFeraAddOn, gbc_lblFeraAddOn);
		feraPanel.add(lblFirstYearOfRun, gbc_lblFirstYearOfRun);
		feraPanel.add(textFieldFirstYearOfRun, gbc_textFieldFirstYearOfRun);
		feraPanel.add(btnExport, gbc_btnExport);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		FeraModel feraModel = new FeraModel();
		new FeraController(this, feraModel);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public Component getRootComponent() {

		return feraPanel;

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
	public Map<String, String> getFeraParams() {

		Map<String, String> params = new HashMap<String, String>();

		params.put("firstYearOfRun", textFieldFirstYearOfRun.getText().trim());

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
				btnExport.setEnabled(false);
				return true;
			}
		}

		btnExport.setEnabled(true);
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
	public void addBtnExportListener(ActionListener l) {

		btnExport.addActionListener(l);

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
