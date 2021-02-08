package com.vcw.falecpv.core.email;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.vcw.falecpv.core.email.dto.EmailDto;


/**
 * @author cristianvillarreal
 *
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/VCWQueue") })
public class EmailListener implements MessageListener {

	@EJB
	private EnviaEmailService enviaEmailService;

	/**
	 *@author cristianvillarreal
	 */
	@Override
	public void onMessage(Message message) {
		System.out.println("Se procesa el elemento EMAIL encolado...");
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage om = ((ObjectMessage) message);
				if (om.getObject() instanceof EmailDto) {
					EmailDto cd = (EmailDto) om.getObject();
					enviaEmailService.enviarCorreo(cd, false, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
