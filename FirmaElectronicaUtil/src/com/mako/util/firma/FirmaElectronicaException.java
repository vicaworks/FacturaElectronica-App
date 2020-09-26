/**
 * 
 */
package com.mako.util.firma;

/**
 * @author cristianvillarreal
 *
 */
public class FirmaElectronicaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2459308579875604766L;

	/**
	 * 
	 */
	public FirmaElectronicaException() {
	}

	/**
	 * @param message
	 */
	public FirmaElectronicaException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FirmaElectronicaException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FirmaElectronicaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FirmaElectronicaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
