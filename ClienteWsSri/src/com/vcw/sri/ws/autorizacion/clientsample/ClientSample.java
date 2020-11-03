package com.vcw.sri.ws.autorizacion.clientsample;

import com.vcw.sri.ws.autorizacion.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        AutorizacionComprobantesOfflineService service1 = new AutorizacionComprobantesOfflineService();
	        System.out.println("Create Web Service...");
	        AutorizacionComprobantesOffline port1 = service1.getAutorizacionComprobantesOfflinePort();
	        System.out.println("Call Web Service Operation...");
	        RespuestaComprobante rc = port1.autorizacionComprobante("0108202001179184765200120010010154049771540497719");
	        System.out.println("Server said: " + rc.getNumeroComprobantes());
	        //Please input the parameters instead of 'null' for the upper method!
	
//	        System.out.println("Server said: " + port1.autorizacionComprobanteLote(null));
//	        //Please input the parameters instead of 'null' for the upper method!
//	
//	        System.out.println("Create Web Service...");
//	        AutorizacionComprobantesOffline port2 = service1.getAutorizacionComprobantesOfflinePort();
//	        System.out.println("Call Web Service Operation...");
//	        System.out.println("Server said: " + port2.autorizacionComprobante(null));
//	        //Please input the parameters instead of 'null' for the upper method!
//	
//	        System.out.println("Server said: " + port2.autorizacionComprobanteLote(null));
//	        //Please input the parameters instead of 'null' for the upper method!
//	
//	        System.out.println("***********************");
//	        System.out.println("Call Over!");
	}
}
