/**
 * 
 */
package com.vcw.falecpv.web.servicio.listener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.web.servicio.SriDispacher;

/**
 * @author cristianvillarreal
 *
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/SRIride") })
public class SriDispacherListener implements MessageListener {
	
	@Inject
	private SriDispacher sriDispacher;
	
	
	/**
	 *@author cristianvillarreal
	 */
	@Override
	public void onMessage(Message message) {
		System.out.println("Se procesa Comprobante SRI encolado...");
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage om = ((ObjectMessage) message);
				if (om.getObject() instanceof Cabecera) {
					sriDispacher.comprobanteSriDispacher((Cabecera)om.getObject());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
