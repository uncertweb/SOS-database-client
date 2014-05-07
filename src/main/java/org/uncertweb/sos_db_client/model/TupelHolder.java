package org.uncertweb.sos_db_client.model;

/**
 * Implement immutable holder type for two values.
 * 
 * @author Benedikt Klus
 * 
 * @param <X1>
 *            type of first value
 * @param <X2>
 *            type of second value
 */
public class TupelHolder<X1, X2> {

	private final X1 first;

	private final X2 second;

	/**
	 * Create holder type.
	 * 
	 * @param first
	 *            first value to be stored
	 * @param second
	 *            second value to be stored
	 */
	public TupelHolder(X1 first, X2 second) {

		this.first = first;
		this.second = second;

	}

	/**
	 * Get first value.
	 * 
	 * @return first value
	 */
	public X1 getFirst() {

		return first;

	}

	/**
	 * Get second value.
	 * 
	 * @return second value
	 */
	public X2 getSecond() {

		return second;

	}

}
