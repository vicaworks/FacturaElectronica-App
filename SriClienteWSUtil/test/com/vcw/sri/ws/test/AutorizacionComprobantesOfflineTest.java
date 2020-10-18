/**
 * 
 */
package com.vcw.sri.ws.test;

import com.servitec.common.util.PojoUtil;
import com.vcw.sri.ws.cliente.SriMetodos;
import com.vcw.sri.ws.cliente.SriWsServicio;
import com.vcw.sri.ws.cliente.dto.Mensaje;
import com.vcw.sri.ws.cliente.dto.RespuestaComprobante;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;
import com.vcw.sri.ws.exception.SriWebServiceExeption;

/**
 * @author cristianvillarreal
 *
 */
public class AutorizacionComprobantesOfflineTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String claveAccesoComprobante = "0110202001179184765200120010010160319631603196310";
		
		SriWsServicio ws = new SriWsServicio();
		ws.setWsdl("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
		ws.setMetodoPost(SriMetodos.getAutorizacionComprobantesOfflinePost(claveAccesoComprobante));
		ws.setHost("cel.sri.gob.ec");
		
		
			RespuestaComprobante respuestaComprobante;
			try {
				respuestaComprobante = ws.autorizacionComprobante();
	//			System.out.println(XmlCommonsUtil.jaxbMarshall(respuestaComprobante, true, false));
					System.out.println(respuestaComprobante.getAutorizacionList().get(0).getEstado());
					for (Mensaje	mensaje : respuestaComprobante.getAutorizacionList().get(0).getMensajeList()) {
						System.out.println(PojoUtil.toString(mensaje));
					}
					System.out.println(respuestaComprobante.getAutorizacionList().get(0).getComprobante());
			} catch (AccesoWsdlSriException e) {
				e.printStackTrace();
			} catch (SriWebServiceExeption e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
	}

}
