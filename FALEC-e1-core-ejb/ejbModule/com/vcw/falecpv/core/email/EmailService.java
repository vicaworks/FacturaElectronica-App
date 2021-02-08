package com.vcw.falecpv.core.email;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmailEnum;
import com.vcw.falecpv.core.email.dto.ConexionDto;
import com.vcw.falecpv.core.email.dto.EmailDto;
import com.vcw.falecpv.core.email.util.LogUtil;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;

@Stateless
public class EmailService {
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private  ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@Inject
	private UsuarioServicio usuarioServicio;
	
	@Inject
	private EmpresaServicio empresaServicio;

	/**
	 * Envia correo utilizando los parametros definidas
	 * 
	 * @param correo
	 * @param session
	 */
	public void enviarCorreo(EmailDto correo, Session session) throws Exception {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setSubject(correo.getAsunto());
			message.setFrom(new InternetAddress(correo.getDatosConexion().getMailFrom(), correo.getDatosConexion().getMailFromName()));
			message.setReplyTo(new Address[]{new InternetAddress(correo.getDatosConexion().getMailFromReplyTo(), correo.getDatosConexion().getMailFromNameReplyTo())});

			// adicionando todos los destinatarios
			if (correo.getCorreosTo() != null)
				for (String mail : correo.getCorreosTo()) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.trim()));
				}

			// adicionando todos los con copia
			if (correo.getCorreosCC() != null)
				for (String mail : correo.getCorreosCC()) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail.trim()));
				}
			
			
			// adiciona copia oculta
			if (correo.getCorreosCCo() != null)
				for (String mail : correo.getCorreosCCo()) {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(mail.trim()));
				}
			
			
			if (correo.getAdjuntos() == null || correo.getAdjuntos().size() == 0) {
				// si no tiene adjuntos se agrega solo el contenido del correo
				message.setContent(correo.getContenido(), correo.isCorreoHtml() ? "text/html" : "text/plain; charset=UTF-8");
			} else {
				// si tiene adjuntos se debe armar un correo en varias BodyPart
				// 1era parte del mensaje que tiene el texto
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(correo.getContenido(), correo.isCorreoHtml() ? "text/html" : "text/plain; charset=UTF-8");
				// se pone en multipart el mensaje en html
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				if (correo.getAdjuntos() != null)
					for (File adj : correo.getAdjuntos()) {
						// 2da parte con cada adjunto
						if(adj != null) {
							messageBodyPart = new MimeBodyPart();
							DataSource source = new FileDataSource(adj);
							messageBodyPart.setDataHandler(new DataHandler(source));
							
							if (correo.isCambiarNombre()) {
								messageBodyPart.setFileName(correo.getNombreAdjunto());
							}else
								messageBodyPart.setFileName(adj.getName());
							
							messageBodyPart.setHeader("Content-ID", "<" + adj.getName() + ">");
							multipart.addBodyPart(messageBodyPart);
						}
					}
				// se le ponen al mensaje todas las partes
				message.setContent(multipart);

			}
			Transport.send(message);
			
			if (correo.getActualizaNegocio() != null) {
				correo.getActualizaNegocio().cambiarEstado(EstadoEnvioEmailEnum.ENVIADO);
			}

		} catch (Exception e) {
			if (correo.getLogPath() != null && correo.getLogPath().length() > 0)
				LogUtil.escribirLog(correo.getLogPath(), e);
			throw e;
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * @param idEmpresa
	 * @param idusuario
	 * @return
	 * @throws DaoException
	 */
	public EmailDto configurarCorreo(String idEmpresa,String idusuario)throws DaoException{
		try {
			Usuario usuario = usuarioServicio.consultarByPk(idusuario);
			Empresa empresa = empresaServicio.consultarByPk(idEmpresa);
			
//			Pattern pattern = Pattern
//					.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			
			EmailDto emailDto = new EmailDto();
			
			emailDto.setCorreosTo(new ArrayList<>());
			emailDto.setCorreosCC(new ArrayList<>());
			emailDto.setCorreosCCo(new ArrayList<>());
			emailDto.setCorreoHtml(true);
			emailDto.setCita(false);
			emailDto.setCancelacionCita(false);
			emailDto.setAdjuntos(new ArrayList<>());
			
			ConexionDto conexionDto = new ConexionDto();
			
			// 1 verifca si  tiene SMTP propio
			if((boolean)parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_PROPIO, TipoRetornoParametroGenerico.BOOLEAN, idEmpresa)) {
				conexionDto.setAuth(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_AUTH, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setHost(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_SERVER, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setMailOwner(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_USER, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setMailOwnerPasw(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_PASSWORD, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setPort(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_PORT, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setSsl(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_SSL, TipoRetornoParametroGenerico.STRING, idEmpresa));
				conexionDto.setStarttls(parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmailEnum.SERVER_SMTP_START_TLS, TipoRetornoParametroGenerico.STRING, idEmpresa));
				
			}else {
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_AUTH, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_SERVER, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_USER, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_PASSWORD, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_PORT, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_SSL, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
				conexionDto.setAuth(parametroGenericoServicio.consultarParametro(PGEmailEnum.SMTP_START_TLS, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING));
			}
			// mail from
			conexionDto.setMailFrom(conexionDto.getMailOwner());
			conexionDto.setMailFromName(empresa.getNombrecomercial());
			
			// mail responder
			if(usuario.getCorreoelectronico()!=null && usuario.getCorreoelectronico().length()>0) {
				String[] email = usuario.getCorreoelectronico().split(",");
				conexionDto.setMailFromReplyTo(email[0]);
			}else {
				conexionDto.setMailFromReplyTo(conexionDto.getMailOwner());
			}
			conexionDto.setMailFromNameReplyTo(usuario.getNombre());
			emailDto.setDatosConexion(conexionDto);
			
			return emailDto;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * @param idEmpresa
	 * @param idusuario
	 * @throws DaoException
	 */
	public void congigurarCorreotest(EmailDto emailDto, String idEmpresa,String idEstablecimiento)throws DaoException{
		try {
			
			// vaciar todos los correos to, CC, Cco
			emailDto.setCorreosTo(new ArrayList<>());
			emailDto.setCorreosCC(new ArrayList<>());
			emailDto.setCorreosCCo(new ArrayList<>());
			
			// verifica si el correo es test
			if((boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.SERVER_SMTP_TEST, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento)) {
				// consular el corrreo test
				String emails[] = ((String) parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.SERVER_SMTP_TEST_EMAIL, TipoRetornoParametroGenerico.STRING, idEstablecimiento)).split(",");
				Pattern pattern = Pattern
						.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
								+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				for (String e : emails) {
					Matcher mather = pattern.matcher(e);
					if(mather.find()) {
						emailDto.getCorreosTo().add(e);
					}
				}
				
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
}