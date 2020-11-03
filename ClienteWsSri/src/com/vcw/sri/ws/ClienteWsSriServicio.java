/**
 * 
 */
package com.vcw.sri.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.WebServiceException;

import com.vcw.sri.ws.autorizacion.AutorizacionComprobantesOffline;
import com.vcw.sri.ws.autorizacion.AutorizacionComprobantesOfflineService;
import com.vcw.sri.ws.autorizacion.RespuestaComprobante;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;
import com.vcw.sri.ws.exception.SriWebServiceExeption;
import com.vcw.sri.ws.recepcion.RecepcionComprobantesOffline;
import com.vcw.sri.ws.recepcion.RecepcionComprobantesOfflineService;
import com.vcw.sri.ws.recepcion.RespuestaSolicitud;

/**
 * @author cristianvillarreal
 *
 */
public class ClienteWsSriServicio {

	private String wsdlAcceso;
	

	public ClienteWsSriServicio(String wsdlAcceso) {
		super();
		this.wsdlAcceso = wsdlAcceso;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param claveAcceso
	 * @return
	 * @throws AccesoWsdlSriException
	 * @throws SriWebServiceExeption
	 */
	public RespuestaComprobante autorizacionComprobanteFacade(String claveAcceso)throws AccesoWsdlSriException,WebServiceException{
		
		URL wsdlLocation = null;
		try {
			wsdlLocation = new URL(this.wsdlAcceso);
		} catch (MalformedURLException e) {
			throw new AccesoWsdlSriException("ERROR DIRECCION WS SRI : " + this.wsdlAcceso); 
		}
		
		AutorizacionComprobantesOfflineService service1 = new AutorizacionComprobantesOfflineService(wsdlLocation);
		AutorizacionComprobantesOffline port1 = service1.getAutorizacionComprobantesOfflinePort();
		return port1.autorizacionComprobante(claveAcceso);
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xml
	 * @return
	 * @throws AccesoWsdlSriException
	 * @throws WebServiceException
	 */
	public RespuestaSolicitud validarComprobanteFacade(String xml)throws AccesoWsdlSriException,WebServiceException{
		URL wsdlLocation = null;
		try {
			wsdlLocation = new URL(this.wsdlAcceso);
		} catch (MalformedURLException e) {
			throw new AccesoWsdlSriException("ERROR DIRECCION WS SRI : " + this.wsdlAcceso); 
		}
		RecepcionComprobantesOfflineService service1 = new RecepcionComprobantesOfflineService(wsdlLocation);
        RecepcionComprobantesOffline port1 = service1.getRecepcionComprobantesOfflinePort();
        return port1.validarComprobante(xml.getBytes());
	}

	/**
	 * @return the wsdlAcceso
	 */
	public String getWsdlAcceso() {
		return wsdlAcceso;
	}

	/**
	 * @param wsdlAcceso the wsdlAcceso to set
	 */
	public void setWsdlAcceso(String wsdlAcceso) {
		this.wsdlAcceso = wsdlAcceso;
	}
	
	
	
	
}
