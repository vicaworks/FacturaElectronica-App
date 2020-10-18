/**
 * 
 */
package com.vcw.sri.ws.cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.sri.ws.cliente.dto.Autorizacion;
import com.vcw.sri.ws.cliente.dto.Mensaje;
import com.vcw.sri.ws.cliente.dto.RespuestaComprobante;
import com.vcw.sri.ws.cliente.util.SriXmlUtil;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;
import com.vcw.sri.ws.exception.SriWebServiceExeption;

/**
 * @author cristianvillarreal
 *
 */
public class SriWsServicio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8352862294265613326L;

	private String wsdl;
	private String metodoPost;
	private String host ;//= "cel.sri.gob.ec"; // celcer.sri.gob.ec

	/**
	 * 
	 */
	public SriWsServicio() {
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws DatatypeConfigurationException 
	 * @throws DocumentException 
	 */
	@SuppressWarnings("static-access")
	public RespuestaComprobante autorizacionComprobante()throws AccesoWsdlSriException,SriWebServiceExeption, IOException, DocumentException, DatatypeConfigurationException, JAXBException {
		
		// 1 validar accesos
		SriXmlUtil.validarAccesosWsSri("AutorizacionComprobantesOffline", wsdl, host, metodoPost);
				
		RespuestaComprobante respuestaComprobante = null;
		
		HttpURLConnection conn = null;
		URL sriURL = null;
		try {

			sriURL = new URL(wsdl);
			conn = (HttpURLConnection) sriURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

			
			DataOutputStream reqStreamOut = new DataOutputStream ( conn.getOutputStream () );
			reqStreamOut.writeBytes (metodoPost);
			reqStreamOut.flush ();
			reqStreamOut.close ();
			
			System.out.println("==== " + conn.getResponseCode());
			
			 if (conn.getResponseCode() == conn.HTTP_OK) {
				 StringBuilder sb = new StringBuilder();
				 
				 java.io.BufferedReader rd = new java.io.BufferedReader(
						 new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
				 String line = "";
				 while ((line = rd.readLine()) != null)
					 sb.append(line);
				 
				 conn.disconnect();
				 respuestaComprobante = populateRespuesta(sb.toString());
				 
			 }else {
				 SriXmlUtil.errorHttp(conn.getResponseCode(), "AutorizacionComprobantesOffline");
			 }
			 

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new AccesoWsdlSriException(e);
		} 

		return respuestaComprobante;
	}
	
	/**
	 * @param soapXml
	 * @return
	 * @throws DocumentException
	 * @throws DatatypeConfigurationException
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 */
	private RespuestaComprobante populateRespuesta(String soapXml) throws DocumentException, DatatypeConfigurationException, UnsupportedEncodingException, JAXBException {
		
		// se instancia el documento xml
		
		Document docXml = XmlCommonsUtil.stringToDocument(soapXml);
			
		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();
		respuestaComprobante.setClaveAccesoConsultada(valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//claveAccesoConsultada")));
		respuestaComprobante.setNumeroComprobantes(valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//numeroComprobantes")));
		respuestaComprobante.setAutorizacionList(new ArrayList<>());
		Autorizacion autorizacion = new Autorizacion();
		autorizacion.setAmbiente(valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/ambiente")));
		autorizacion.setEstado(valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/estado")));
		autorizacion.setNumeroAutorizacion(valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/estado")));
		String fecha = valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/fechaAutorizacion"));
		if(fecha!=null) {
			autorizacion.setFechaAutorizacion(DatatypeFactory.newInstance()
				    .newXMLGregorianCalendar(fecha));
		}
		
		String comprobante = valorNodo(XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/comprobante"));
		autorizacion.setComprobante(comprobante);
		
		autorizacion.setMensajeList(new ArrayList<>());
		List<Node> msgList = XmlCommonsUtil.aplicarXpath(docXml, "//autorizaciones/autorizacion/mensajes/mensaje");
		for (Node msg : msgList) {
			autorizacion.getMensajeList().add(XmlCommonsUtil.jaxbunmarshall(msg.asXML(), new Mensaje()));
		}
		respuestaComprobante.getAutorizacionList().add(autorizacion);
		return respuestaComprobante;
	}
	
	private String valorNodo(List<Node> nodoList) {
		if(nodoList!=null && !nodoList.isEmpty()) {
			return nodoList.get(0).getStringValue();
		}
		return null;
	}
	
	/**
	 * @return the wsdl
	 */
	public String getWsdl() {
		return wsdl;
	}

	/**
	 * @param wsdl the wsdl to set
	 */
	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	/**
	 * @return the metodoPost
	 */
	public String getMetodoPost() {
		return metodoPost;
	}

	/**
	 * @param metodoPost the metodoPost to set
	 */
	public void setMetodoPost(String metodoPost) {
		this.metodoPost = metodoPost;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

}
