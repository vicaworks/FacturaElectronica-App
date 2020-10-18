/**
 * 
 */
package com.vcw.sri.ws.cliente.util;

import com.vcw.sri.ws.exception.AccesoWsdlSriException;

/**
 * @author cristianvillarreal
 *
 */
/**
 * @author cristianvillarreal
 *
 */
public class SriXmlUtil {

	/**
	 * 
	 */
	public SriXmlUtil() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param servicioSri
	 * @param wsdl
	 * @param host
	 * @param metodoPost
	 * @throws AccesoWsdlSriException
	 */
	public static void validarAccesosWsSri(String servicioSri,String wsdl,String host,String metodoPost)throws AccesoWsdlSriException {
		if( wsdl==null || wsdl.isEmpty()) {
			throw new AccesoWsdlSriException("WSDL de acceso a " + servicioSri  + " no existe.");
		}
		if( host==null || host.isEmpty()) {
			throw new AccesoWsdlSriException("HOST de acceso a " + servicioSri  + " no existe.");
		}
		if( metodoPost==null || metodoPost.isEmpty()) {
			throw new AccesoWsdlSriException("METODO POST de acceso " + servicioSri  + " no existe.");
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param errorCode
	 * @param servicioSri
	 * @throws AccesoWsdlSriException
	 */
	public static void errorHttp(int errorCode,String servicioSri)throws AccesoWsdlSriException{
		if(errorCode>=300 && errorCode<400) {
			throw new AccesoWsdlSriException("HTTP ERROR CODE: " + errorCode + " TIPO: REDIRECCIONES, ACCESO AL WEB SERVICE : " + servicioSri);
		}
		if(errorCode>=400 && errorCode<500) {
			throw new AccesoWsdlSriException("HTTP ERROR CODE: " + errorCode + " TIPO: ERRORES DEL CLIENTE, ACCESO AL WEB SERVICE : " + servicioSri);
		}
		if(errorCode>=500) {
			throw new AccesoWsdlSriException("HTTP ERROR CODE: " + errorCode + " TIPO: ERRORES DEL SERVIDOR, ACCESO AL WEB SERVICE : " + servicioSri);
		}
	}


}
