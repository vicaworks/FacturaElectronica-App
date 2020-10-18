/**
 * 
 */
package com.vcw.sri.ws.exception;

/**
 * @author cristianvillarreal
 *
 */
public class AccesoWsdlSriException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1660805546309418487L;

	/**
	 * 
	 */
	public AccesoWsdlSriException() {
	}

	/**
	 * @param message
	 */
	public AccesoWsdlSriException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AccesoWsdlSriException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AccesoWsdlSriException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AccesoWsdlSriException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
