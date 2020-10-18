/**
 * 
 */
package com.vcw.sri.ws.cliente;

import java.io.Serializable;

/**
 * @author cristianvillarreal
 *
 */
public class SriMetodos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 733068851151497813L;

	/**
	 * 
	 */
	public SriMetodos() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param claveAccesoComprobante
	 * @return
	 */
	public static String getAutorizacionComprobantesOfflinePost(String claveAccesoComprobante) {
		
//		String post = "<?xml version='1.0' encoding='utf-8' standalone='no'?> <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ec=\"http://ec.gob.sri.ws.autorizacion\">" + 
//		   "<soapenv:Header/>" +
//		   "<soapenv:Body>" +
//		   "   <ec:autorizacionComprobante>" +
//		   "      <claveAccesoComprobante>" + claveAccesoComprobante + "</claveAccesoComprobante>" +
//		   "   </ec:autorizacionComprobante>" +
//		   "</soapenv:Body>" +
//		   "</soapenv:Envelope>";
		
		String post = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ec=\"http://ec.gob.sri.ws.autorizacion\">" +
        		"    <soapenv:Header/>" +
        		"    <soapenv:Body>" +
        		"    <ec:autorizacionComprobante>" +
        		"    <claveAccesoComprobante>"+claveAccesoComprobante+"</claveAccesoComprobante> " +
        		"    </ec:autorizacionComprobante>" +
        		"    </soapenv:Body>" +
        		"    </soapenv:Envelope>";

		
		return post;
		
	}
	

}
