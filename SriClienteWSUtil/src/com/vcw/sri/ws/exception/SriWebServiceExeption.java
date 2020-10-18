/**
 * 
 */
package com.vcw.sri.ws.exception;

/**
 * @author cristianvillarreal
 *
 */
public class SriWebServiceExeption extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8363383858813394577L;

	/**
	 * 
	 */
	public SriWebServiceExeption() {
	}

	/**
	 * @param message
	 */
	public SriWebServiceExeption(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SriWebServiceExeption(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SriWebServiceExeption(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SriWebServiceExeption(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
