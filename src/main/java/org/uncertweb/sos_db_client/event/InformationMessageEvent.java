package org.uncertweb.sos_db_client.event;

/**
 * Implement event.
 * 
 * @author Benedikt Klus
 */
public class InformationMessageEvent {

	private String message;

	/**
	 * Create event.
	 * 
	 * @param message
	 *            message to be displayed
	 */
	public InformationMessageEvent(String message) {

		this.message = message;

	}

	/**
	 * Get message.
	 * 
	 * @return message
	 */
	public String getMessage() {

		return message;

	}

}
