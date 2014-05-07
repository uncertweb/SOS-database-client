package org.uncertweb.sos_db_client.view;

import javax.swing.JFileChooser;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class FileSaverView implements IView {

	private IView parentView;

	private JFileChooser fileChooser;

	private int returnVal;

	/**
	 * Create view.
	 * 
	 * @param parentView
	 *            parent view to be associated with
	 */
	public FileSaverView(IView parentView) {

		this.parentView = parentView;
		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		/**
		 * Get the return state of the file chooser on popdown.
		 */
		returnVal = fileChooser.showSaveDialog(parentView.getRootComponent());

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JFileChooser getRootComponent() {

		return fileChooser;

	}

	/**
	 * Get return value.
	 * 
	 * @return return value
	 */
	public int getReturnVal() {

		return returnVal;

	}

}
