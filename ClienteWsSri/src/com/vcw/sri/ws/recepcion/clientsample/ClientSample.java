package com.vcw.sri.ws.recepcion.clientsample;

import com.vcw.sri.ws.recepcion.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        RecepcionComprobantesOfflineService service1 = new RecepcionComprobantesOfflineService();
	        System.out.println("Create Web Service...");
	        RecepcionComprobantesOffline port1 = service1.getRecepcionComprobantesOfflinePort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.validarComprobante(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        RecepcionComprobantesOffline port2 = service1.getRecepcionComprobantesOfflinePort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.validarComprobante(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
