package org.uncertweb.sos_db_client.event;

/**
 * Implement event.
 * 
 * @author Benedikt Klus
 */
public class TabbedPaneEvent {

	private boolean isEnabled;

	/**
	 * Create event.
	 * 
	 * @param isEnabled
	 *            true if tabbed pane should be enabled, false if not
	 */
	public TabbedPaneEvent(boolean isEnabled) {

		this.isEnabled = isEnabled;

	}

	/**
	 * Check if tabbed pane is enabled.
	 * 
	 * @return true if tabbed pane is enabled, false if not
	 */
	public boolean isEnabled() {

		return isEnabled;

	}

}
