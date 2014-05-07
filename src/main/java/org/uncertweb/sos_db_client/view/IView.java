package org.uncertweb.sos_db_client.view;

import java.awt.Component;

/**
 * Implement interface.
 * 
 * @author Benedikt Klus
 */
public interface IView {

	/**
	 * Initialize components.
	 */
	public void initComponents();

	/**
	 * Register controller.
	 */
	public void registerController();

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	public Component getRootComponent();

}
