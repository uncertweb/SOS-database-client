package org.uncertweb.sos_db_client.event;

/**
 * Implement event.
 * 
 * @author Bneedikt Klus
 */
public class ProgressEvent {

	private boolean isIndeterminate;

	private int currentValue;

	private String currentText;

	/**
	 * Create event.
	 * 
	 * @param isIndeterminate
	 *            true if progress bar should be set indeterminate, false if not
	 * @param currentValue
	 *            current value to be set
	 * @param currentText
	 *            textual indication of the percentage of the current task to be
	 *            set
	 */
	public ProgressEvent(boolean isIndeterminate, int currentValue,
			String currentText) {

		this.isIndeterminate = isIndeterminate;
		this.currentValue = currentValue;
		this.currentText = currentText;

	}

	/**
	 * Set progress bar indeterminate.
	 * 
	 * @return true if progress bar should be set indeterminate, false if not
	 */
	public boolean setIndeterminate() {

		return isIndeterminate;

	}

	/**
	 * Get current value.
	 * 
	 * @return current value
	 */
	public int getCurrentValue() {

		return currentValue;

	}

	/**
	 * Get textual indication of the percentage of the current task.
	 * 
	 * @return textual indication of the percentage of the current task
	 */
	public String getCurrentText() {

		return currentText;

	}

}
