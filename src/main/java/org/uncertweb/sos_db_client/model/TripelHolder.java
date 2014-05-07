package org.uncertweb.sos_db_client.model;

/**
 * Implement immutable holder type for three values.
 * 
 * @author Benedikt Klus
 * 
 * @param <X1>
 *            type of first value
 * @param <X2>
 *            type of second value
 * @param <X3>
 *            type of third value
 */
public class TripelHolder<X1, X2, X3> {

	private final X1 first;

	private final X2 second;

	private final X3 third;

	/**
	 * Create holder type.
	 * 
	 * @param first
	 *            first value to be stored
	 * @param second
	 *            second value to be stored
	 * @param third
	 *            third value to be stored
	 */
	public TripelHolder(X1 first, X2 second, X3 third) {

		this.first = first;
		this.second = second;
		this.third = third;

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

	/**
	 * Get third value.
	 * 
	 * @return third value
	 */
	public X3 getThird() {

		return third;

	}

}
