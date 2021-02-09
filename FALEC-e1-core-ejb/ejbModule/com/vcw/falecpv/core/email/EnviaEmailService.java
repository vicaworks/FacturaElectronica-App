package com.vcw.falecpv.core.email;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.apache.commons.beanutils.BeanUtils;

import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.email.dto.ConexionDto;
import com.vcw.falecpv.core.email.dto.EmailDto;
import com.vcw.falecpv.core.email.util.LogUtil;


/**
 * Controla el envio de correos y citas, ya sea utilizando o no la cola de
 * correos
 */
@Stateless
public class EnviaEmailService {

	@EJB
	private CitaService citaService;
	@EJB
	private EmailService correoService;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;

	/**
	 * Controla el envio de los correos en dependencia de la configuracion
	 * especificada
	 * 
	 * @param datosConexion
	 * @param datosCorreo
	 * @param enviarCola
	 * @param cantidadEnviarPorGrupo
	 */
	public void enviarCorreo(EmailDto datosCorreo, boolean enviarCola, int cantidadEnviarPorGrupo) throws Exception {
		// 1 - VALIDACIONES ANTES DE PROCESAR LOS DATOS
		// verificando el formato de cada uno
		if ((datosCorreo.getCorreosCC() == null || datosCorreo.getCorreosCC().isEmpty()) && (datosCorreo.getCorreosTo() == null || datosCorreo.getCorreosTo().isEmpty())) {
			throw new Exception("LOS CAMPOS TO Y CC NO PUEDEN ESTAR AMBOS NULOS");
		}
		// si no hay contenido
		if (datosCorreo.getContenido() == null || datosCorreo.getContenido().isEmpty()) {
			throw new Exception("DEBE ESCRIBIR UN TEXTO DE CONTENIDO");
		}
		validarFormatoCorreo(datosCorreo.getCorreosTo());
		validarFormatoCorreo(datosCorreo.getCorreosCC());
		// verificando que si es una cita tenga el objeto necesario
		if ((datosCorreo.isCita() || datosCorreo.isCancelacionCita()) && datosCorreo.getDatosCita() == null) {
			throw new Exception("SI SE VA A ENVIAR UNA CITA O UNA CANCELACION DE CITA, DEBE LLENAR EL OBJETO 'CitaDto' CON LOS PARAMETROS NECESARIOS");
		}

		// 2 - PROCESAMIENTO DE LOS DATOS
		try {
			Session session = null;
			if (datosCorreo != null && datosCorreo.getDatosConexion() != null)
				session = establecerPropiedades(datosCorreo.getDatosConexion());

			List<EmailDto> listaCorreos = dividirCorreos(datosCorreo, cantidadEnviarPorGrupo);
			for (EmailDto correoDto : listaCorreos) {
				//correoDto.setCorreosCC(new ArrayList<>());
				
				if (!enviarCola) {
					// envia mails directamente
					if (correoDto.isCita() || correoDto.isCancelacionCita())
						citaService.enviarCita(correoDto, session);
					else
						correoService.enviarCorreo(correoDto, session);
				} else {
					// envia el mail a la cola de correos
					encolarCorreo(correoDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (datosCorreo.getActualizaNegocio() != null) {
				datosCorreo.getActualizaNegocio().cambiarEstado(EstadoEnvioEmailEnum.ERROR);
			}
			if (datosCorreo.getLogPath() != null && datosCorreo.getLogPath().length() > 0)
				LogUtil.escribirLog(datosCorreo.getLogPath(), e);
			throw e;
		}
	}

	/**
	 * Establece las {@link Properties} de configuracion de mail
	 * 
	 * @param datosConexion
	 * @return objeto {@link Session} con las {@link Properties} insertadas
	 */
	private Session establecerPropiedades(ConexionDto datosConexion) throws Exception {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", datosConexion.getStarttls());
		properties.setProperty("mail.smtp.host", datosConexion.getHost());
		properties.setProperty("mail.smtp.port", datosConexion.getPort());
		properties.setProperty("mail.smtp.ssl.enable", datosConexion.getSsl());

		if (datosConexion.getMailOwner() != null && datosConexion.getMailOwner().length() > 0 && datosConexion.getMailOwnerPasw() != null && datosConexion.getMailOwnerPasw().length() > 0) {
			/*properties.setProperty("mail.user", datosConexion.getMailOwner());
			properties.setProperty("mail.password", datosConexion.getMailOwnerPasw());*/
			properties.setProperty("mail.smtp.auth", datosConexion.getAuth());

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(datosConexion.getMailOwner(), datosConexion.getMailOwnerPasw());
				}
			};
			return Session.getInstance(properties, auth);
		}
		return Session.getInstance(properties);
	}

	/**
	 * Efectua la division de los TO y CC en grupos de correos.
	 * 
	 * Si existe la misma cantidad de TO que CC, en cada correo de la lista que
	 * se devuelve hay la misma cantidad de TO que de CC.
	 * 
	 * Si hay mas TO que CC, hay tantos correos como la division de TO, y
	 * algunos se envian sin CC.
	 * 
	 * Si hay mas CC que TO, el ultimo de los correos contiene todos los CC que
	 * no cupieron en los TO.
	 * 
	 * @param datosCorreo
	 * @param cantidadEnviarPorGrupo
	 * @return lista con los correos a enviar, correctamente divididos
	 */
	private List<EmailDto> dividirCorreos(EmailDto datosCorreo, int cantidadEnviarPorGrupo) throws Exception {
		List<EmailDto> listaCorreos = new ArrayList<EmailDto>();

		// si no se espicifca division por grupos
		if (cantidadEnviarPorGrupo <= 0) {
			listaCorreos.add(datosCorreo);
			return listaCorreos;
		}

		if (datosCorreo.getCorreosTo() != null && !datosCorreo.getCorreosTo().isEmpty()) {
			int cantG = cantidadEnviarPorGrupo;
			EmailDto mail = copiarObjeto(datosCorreo);

			for (int i = 0; i < datosCorreo.getCorreosTo().size(); i++) {
				if (cantG == 0) {
					listaCorreos.add(mail);
					mail = copiarObjeto(datosCorreo);
					cantG = cantidadEnviarPorGrupo;
				}

				cantG -= 1;
				mail.getCorreosTo().add(datosCorreo.getCorreosTo().get(i));
				// TO <= CC ciclo normal, si es menor despues se agregan los
				// CC que faltan
				// Si hay mas TO que CC cuando se acabe la lista de CC no se
				// debe seguir insertando
				try {
					mail.getCorreosCC().add(datosCorreo.getCorreosCC().get(i));
				} catch (Exception e) {

				}
			}
			listaCorreos.add(mail);

			// cuando hay mas CC que TO, se le ponen al ultimo TO, todos los CC
			// que no se pusieron en ningun correo
			if (datosCorreo.getCorreosTo().size() < datosCorreo.getCorreosCC().size()) {
				List<String> ccNoAdjuntados = datosCorreo.getCorreosCC().subList(datosCorreo.getCorreosTo().size(), datosCorreo.getCorreosCC().size());
				listaCorreos.get(listaCorreos.size() - 1).getCorreosCC().addAll(ccNoAdjuntados);
			}
		}

		return listaCorreos;
	}

	/**
	 * @param correoOriginal
	 * @return objeto {@link EmailDto} copia del pasado por parametro, pero sin
	 *         la lista de TO ni de CC
	 */
	private EmailDto copiarObjeto(EmailDto correoOriginal) {
		try {
			EmailDto correoNuevo = (EmailDto) BeanUtils.cloneBean(correoOriginal);
			correoNuevo.setCorreosTo(new ArrayList<>());
			correoNuevo.setCorreosCC(new ArrayList<>());
			return correoNuevo;

		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Encola los correos que se indiquen, en la cola que se configure en el
	 * standalone
	 * 
	 * @param datosCorreo
	 */
	private void encolarCorreo(EmailDto datosCorreo) throws Exception {
		Connection connection = null;
		javax.jms.Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			MessageProducer messageProducer = session.createProducer(queue);
			// objeto a encolar
			ObjectMessage objectMessage = session.createObjectMessage();
			objectMessage.setObject(datosCorreo);

			messageProducer.send(objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Valida el formato del correo pasado por parametro
	 * 
	 * @param correo
	 * @return true si el formato es valido, false en el caso contrario
	 */
	private void validarFormatoCorreo(List<String> listaCorreo) throws Exception {
		if (listaCorreo != null && !listaCorreo.isEmpty()) {
			for (String mail : listaCorreo) {
				try {
					InternetAddress emailAddr = new InternetAddress(mail);
					emailAddr.validate();
				} catch (Exception ex) {
					throw new Exception("EL FORMATO DEL CORREO " + mail + " ES INVALIDO");
				}
			}
		}
	}
}
