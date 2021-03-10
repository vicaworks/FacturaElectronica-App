package com.vcw.falecpv.web.servicio.emailcomprobante;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPlantillasEnum;
import com.vcw.falecpv.core.email.EmailService;
import com.vcw.falecpv.core.email.EnviaEmailService;
import com.vcw.falecpv.core.email.EstadoEmailComprobante;
import com.vcw.falecpv.core.email.dto.EmailDto;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.FileDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.util.VelocityTemplateUtil;

/**
 * 
 * @author cristianvillarreal
 *
 */
@Stateless
public class EmailCotizacionServicio {
	
	@Inject
	private UsuarioServicio usuarioServicio;
	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private EstadoEmailComprobante estadoEmailComprobante;
	
	@Inject
	private EnviaEmailService enviaEmailService;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCotizacion
	 * @param idUsuario
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public String getContenidoFacade(String idCotizacion,String idUsuario,String idEmpresa)throws DaoException{
		try {
			
			Cabecera cotizacion = cabeceraServicio.consultarByPk(idCotizacion);
			Usuario usuario = usuarioServicio.consultarByPk(idUsuario);
			
			//1 verifica si existe plantilla propia de la empresa
			String nombrePLantilla = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.PLANTILLA_EMAIL_COTIZACION, TipoRetornoParametroGenerico.STRING, idEmpresa);
			if(nombrePLantilla!=null && nombrePLantilla.trim().length()==1) {
				nombrePLantilla = parametroGenericoServicio.consultarParametro(PGPlantillasEnum.PLANTILLA_EMAIL_COTIZACION, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING);
			}
			
			return armarContenido(cotizacion, usuario, nombrePLantilla);
			
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cotizacion
	 * @param usuario
	 * @param nombrePLantilla
	 * @return
	 */
	private String armarContenido(Cabecera cotizacion,Usuario usuario,String nombrePLantilla) {
		Template template= new Template();
		template= VelocityTemplateUtil.obtenerContext(nombrePLantilla);
		VelocityContext context = new VelocityContext();
		NumberFormat df = DecimalFormat.getCurrencyInstance(new Locale("es", "EC"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		switch (nombrePLantilla) {
		case "cotizacionV1.vm":
			context.put("cliente", TextoUtil.stringToHTMLString(cotizacion.getCliente().getRazonsocial()));
			context.put("numcotizacion", ComprobanteHelper.formatNumDocumento(cotizacion.getNumdocumento()));
			context.put("valor", df.format(cotizacion.getTotalconimpuestos().doubleValue()));
			context.put("firmaemail", (usuario.getFirmaemail()==null?"":usuario.getFirmaemail()));
			
			break;

		default:
			break;
		}
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idUsuario
	 * @param emailDto
	 * @param idCotizacion
	 * @throws DaoException
	 */
	public void enviarCotizacionEmail(String idUsuario,EmailDto emailDto,Cabecera cotizacion)throws DaoException{
		try {
			
			
			// transformar files byte to adjuntos
			emailDto.setAdjuntos(new ArrayList<>());
			if(emailDto.getFileDtos()!=null) {
				for (FileDto f : emailDto.getFileDtos()) {
					File file = new File("/app/temp/" + f.getNombre());
					FileUtils.deleteQuietly(file);
					FileUtils.writeByteArrayToFile(file, f.getFileByte());
					emailDto.getAdjuntos().add(file);
				}
			}
			
			
			if(emailDto.isSeleccion()) {
				// generar proforma file
				// TODO implementar ride cotizacion
				System.out.println("TODO : GENERA LA PROFORMA");
			}
			String temp = emailDto.getContenido();
			emailDto.setContenido("<style>p{padding: 0px;margin: 0px;}</style>" + emailDto.getContenido());
			emailDto.setContenido(new String(emailDto.getContenido().getBytes(), Charset.forName("ISO-8859-1")));
			
			// verificar si tiene correo test
			emailService.congigurarCorreoTest(emailDto, cotizacion.getEstablecimiento().getEmpresa().getIdempresa(), cotizacion.getEstablecimiento().getIdestablecimiento());
			estadoEmailComprobante.setIdCabecera(cotizacion.getIdcabecera());
			emailDto.setActualizaNegocio(estadoEmailComprobante);
			emailDto.getActualizaNegocio().cambiarEstado(EstadoEnvioEmailEnum.PROCESO);
			
			// verifica si existo TO elemet 
			if(emailDto.getCorreosTo()!=null && !emailDto.getCorreosTo().isEmpty()) {
				enviaEmailService.enviarCorreo(emailDto, true, 20);
			}
			emailDto.setContenido(temp);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	
}
