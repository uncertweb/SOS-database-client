package org.uncertweb.sos_db_client.event;

/**
 * Implement event.
 * 
 * @author Benedikt Klus
 */
public class ErrorMessageEvent {

	private Exception e;

	/**
	 * Create event.
	 * 
	 * @param e
	 *            exception to be handled
	 */
	public ErrorMessageEvent(Exception e) {

		this.e = e;

	}

	/**
	 * Get exception.
	 * 
	 * @return exception
	 */
	public Exception getException() {

		return e;

	}

}
