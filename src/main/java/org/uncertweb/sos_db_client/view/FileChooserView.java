package org.uncertweb.sos_db_client.view;

import javax.swing.JFileChooser;

import org.uncertweb.sos_db_client.model.SupportedFilesFilter;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class FileChooserView implements IView {

	private IView parentView;

	private JFileChooser fileChooser;

	private int returnVal;

	/**
	 * Create view.
	 * 
	 * @param parentView
	 *            parent view to be associated with
	 */
	public FileChooserView(IView parentView) {

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
		fileChooser.setFileFilter(new SupportedFilesFilter());
		fileChooser.setMultiSelectionEnabled(true);

		/**
		 * Get the return state of the file chooser on popdown.
		 */
		returnVal = fileChooser.showOpenDialog(parentView.getRootComponent());

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
