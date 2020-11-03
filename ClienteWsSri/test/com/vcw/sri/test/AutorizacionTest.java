/**
 * 
 */
package com.vcw.sri.test;

import javax.xml.ws.WebServiceException;

import com.vcw.sri.ws.ClienteWsSriServicio;
import com.vcw.sri.ws.autorizacion.Mensaje;
import com.vcw.sri.ws.autorizacion.RespuestaComprobante;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;

/**
 * @author cristianvillarreal
 *
 */
public class AutorizacionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ClienteWsSriServicio wsAutorizacion = new ClienteWsSriServicio("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
		
		try {
			// Si EXISTE
			RespuestaComprobante rc = wsAutorizacion.autorizacionComprobanteFacade("0108202001179184765200120010010154049771540497719");
			valor(rc);
			rc = wsAutorizacion.autorizacionComprobanteFacade("1108202001179184765200120010010154049771540497719");
			valor(rc);
			rc = wsAutorizacion.autorizacionComprobanteFacade("x108202001179184765200120010010154049771540497719");
			valor(rc);
			
		} catch (AccesoWsdlSriException e) {
			System.out.println(e.getMessage());
		} catch(WebServiceException e) {
			System.out.println(e.getMessage());
			if(e.getMessage().contains("java.net.SocketException")) {
				System.out.println("NO EXISTE CONEXION");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void valor(RespuestaComprobante rc) {
		System.out.println("===========================================================");
		if(rc.getNumeroComprobantes()!=null) {
			if(Integer.valueOf(rc.getNumeroComprobantes())>0) {
				// si existe
				System.out.println("1. " + rc.getNumeroComprobantes());
				System.out.println("1. " + rc.getAutorizaciones().getAutorizacion().get(0).getEstado());
				System.out.println("1. " + rc.getAutorizaciones().getAutorizacion().get(0).getComprobante());
			}else {
				System.out.println("1. NO EXISTE" );
			}
		}else if(rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje()!=null && rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().size()>0) {
			Mensaje msj  = rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().get(0);
			System.out.println("Cor error: " + msj.getIdentificador());
			System.out.println("Mensaje: " + msj.getMensaje());
		}
	}

}
