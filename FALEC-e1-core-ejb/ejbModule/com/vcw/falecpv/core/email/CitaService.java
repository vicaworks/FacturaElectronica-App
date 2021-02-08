package com.vcw.falecpv.core.email;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.email.dto.EmailDto;
import com.vcw.falecpv.core.email.util.LogUtil;

@Stateless
public class CitaService {

	/**
	 * Envia un correo de tipo Cita
	 * 
	 * @param correo
	 * @param session
	 */
	public void enviarCita(EmailDto correo, Session session) throws Exception {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setSubject(correo.getAsunto());
			message.addHeaderLine("method=REQUEST");
			message.addHeaderLine("charset=\"UTF-8\"");
			message.addHeaderLine("component=VEVENT");
			message.setFrom(new InternetAddress(correo.getDatosConexion().getMailFrom(), correo.getDatosConexion().getMailFromName()));
			message.setReplyTo(new Address[]{new InternetAddress(correo.getDatosConexion().getMailFrom(), correo.getDatosConexion().getMailFromName())});

			// adicionando todos los con copia
			if (correo.getCorreosCC() != null)
				for (String mail : correo.getCorreosCC()) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail.trim()));
				}

			// adicionando todos los destinatarios
			if (correo.getCorreosTo() != null) {
				for (String mail : correo.getCorreosTo()) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.trim()));

					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setContent(correo.getContenido(), correo.isCorreoHtml() ? "text/html" : "text/plain");
					// se pone en multipart el mensaje en html
					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart);

					// 2da parte con la cita
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.addHeader("Content-Class", "url:content-classes:calendarmessage");
					messageBodyPart.addHeader("Content-ID", "calendar_message");

					String method = "";
					String status = correo.isCita() ? "CONFIRMED" : "CANCELLED";
					if (correo.getDatosConexion().getHost().equals("smtp.gmail.com")) {
						method = correo.isCita() ? "PUBLISH" : "CANCEL";
						messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(construirCita(correo, mail, method, status), "text/calendar;method=" + method)));
					} else {
						method = correo.isCita() ? "REQUEST" : "CANCEL";
						messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(construirCita(correo, mail, method, status), "text/calendar;method=" + method)));
					}
					multipart.addBodyPart(messageBodyPart);

					// partes para los adjuntos
					if (correo.getAdjuntos() != null)
						for (File adj : correo.getAdjuntos()) {
							// 2da parte con cada adjunto
							messageBodyPart = new MimeBodyPart();
							DataSource source = new FileDataSource(adj);
							messageBodyPart.setDataHandler(new DataHandler(source));
							messageBodyPart.setFileName(adj.getName());
							multipart.addBodyPart(messageBodyPart);
						}
					// se le ponen al mensaje todas las partes
					message.setContent(multipart);

					Transport.send(message);
					
					if (correo.getActualizaNegocio() != null) {
						correo.getActualizaNegocio().cambiarEstado(EstadoEnvioEmailEnum.ENVIADO);
					}
				}
			}

		} catch (Exception e) {
			if (correo.getLogPath() != null && correo.getLogPath().length() > 0)
				LogUtil.escribirLog(correo.getLogPath(), e);
			throw e;
		}
	}

	/**
	 * Construye el contenido de la Cita o Cancelacion
	 * 
	 * @param correo
	 * @param to
	 * @param method
	 * @param status
	 * @return contenido de la Cita
	 */
	private static String construirCita(EmailDto correo, String to, String method, String status) {
		String fini = new SimpleDateFormat("yyyyMMdd").format(correo.getDatosCita().getFechaHoraInicio()) + "T" + new SimpleDateFormat("HHmmss").format(correo.getDatosCita().getFechaHoraInicio());
		String ffin = new SimpleDateFormat("yyyyMMdd").format(correo.getDatosCita().getFechaHoraFin()) + "T" + new SimpleDateFormat("HHmmss").format(correo.getDatosCita().getFechaHoraFin());
		String fcre = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "T" + new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String utcTime = sdf.format(new Date());

		String texto = "BEGIN:VCALENDAR\n" + "VERSION:2.0\n" + "PRODID:-//Schedule a Meeting\n" + "METHOD:" + method + "\n" + "CALSCALE:GREGORIAN\n" + "BEGIN:VEVENT\n" + "DTSTART:" + fini + "\n" + "DTEND:" + ffin + "\n" + "DTSTAMP:" + utcTime + "\n" + ((status.equals("CONFIRMED")) ? ("ORGANIZER;CN=" + to + ":mailto:" + to + "\n") : "") + "UID:0000000000111111111100000000011\n" + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=" + to + ":mailto:" + to + "\n" + "CREATED:" + utcTime + "\n" + "DESCRIPTION:" + correo.getContenido() + "\n" + "LAST-MODIFIED:" + fcre + "\n" + "LOCATION:" + correo.getDatosCita().getLugar() + "\n" + "SEQUENCE:0\n" + "STATUS:" + status + "\n" + "SUMMARY:" + correo.getAsunto() + "\n" + "TRANSP:OPAQUE\n" + "END:VEVENT\n" + "END:VCALENDAR\n";

		return texto;
	}

}
