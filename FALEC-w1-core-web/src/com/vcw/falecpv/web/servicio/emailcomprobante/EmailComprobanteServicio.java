/**
 * 
 */
package com.vcw.falecpv.web.servicio.emailcomprobante;

import java.io.File;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.email.EmailService;
import com.vcw.falecpv.core.email.EnviaEmailService;
import com.vcw.falecpv.core.email.EstadoEmailComprobante;
import com.vcw.falecpv.core.email.dto.EmailDto;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.FirmaElectronicaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;
import com.vcw.falecpv.web.servicio.RideServicio;
import com.vcw.falecpv.web.util.MessageWebUtil;
import com.vcw.falecpv.web.util.VelocityTemplateUtil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EmailComprobanteServicio {
	
	@Inject
	private EnviaEmailService enviaEmailService;
	
	@Inject
	private EmailService emailService;

	@Inject
	private RideServicio rideServicio;
	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private DocElectronicoProxy docElectronicoProxy;
	
	@Inject
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private EstadoEmailComprobante estadoEmailComprobante;
	
	@Inject
	private FirmaElectronicaServicio firmaElectronicaServicio;
	
	protected MessageWebUtil msg = new MessageWebUtil();

	public void enviarComprobanteFacade(byte[] xmlDocElectronico,byte[] ride, Map<String, byte[]> otrosAdjuntos,String idCabecera,Cabecera cabecera,String subject,String content,List<String> toList,boolean mailReceptor)throws DaoException{
		try {
			
			if(cabecera==null) {
				cabecera = cabeceraServicio.consultarByPk(idCabecera);
			}
			
			if(content!=null && content.trim().length() > 0) {
				cabecera.setContenidoEmail(content);
			}
			
			
			if(xmlDocElectronico==null) {
				// consultar documento xml
				xmlDocElectronico = firmaElectronicaServicio.firmarXmlFacade(
						cabecera.getEstablecimiento().getEmpresa().getIdempresa(),
						docElectronicoProxy.getDocElectronicoFacade(cabecera.getIdcabecera(),
																cabecera.getEstablecimiento().getIdestablecimiento(),
																GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador())
																)
						).getBytes();
			}
			
			if(ride==null) {
				ride = rideServicio.generarRideFacade(idCabecera);
			}
			
			EmailDto emailDto = emailService.configurarCorreo(cabecera.getEstablecimiento().getEmpresa().getIdempresa(), cabecera.getIdusuario());
			if(subject!=null) {
				emailDto.setAsunto(subject);
			}else {
				emailDto.setAsunto(
						subject = msg.getString("label.comprobanteelectronico.subject", 
								new Object[] {
										cabecera.getTipocomprobante().getComprobante(),
										ComprobanteHelper.formatNumDocumento(cabecera.getNumdocumento()),
										FechaUtil.formatoFecha(cabecera.getFechaemision())
										}
						)
					);
			}
			
			// email del cliente
			if(cabecera.getCliente()!=null && cabecera.getCliente().getCorreoelectronico()!=null && cabecera.getCliente().getCorreoelectronico().trim().length()>0 && mailReceptor) {
				String em[] = cabecera.getCliente().getCorreoelectronico().split(",");
				for (String email : em) {
					emailDto.getCorreosTo().add(email);
				}
			}
			
			// email del transportista
			if(cabecera.getTransportista()!=null && cabecera.getTransportista().getEmail()!=null && cabecera.getTransportista().getEmail().trim().length()>0 && mailReceptor) {
				String em[] = cabecera.getTransportista().getEmail().split(",");
				for (String email : em) {
					emailDto.getCorreosTo().add(email);
				}
			}
			
			// emails extras
			if(toList!=null && !toList.isEmpty()) {
				for (String email : toList) {
					emailDto.getCorreosTo().add(email);
				}
			}
			
			// adjuntos
			
			File file = new File("/app/temp/" + ComprobanteHelper.formatNumDocumento(cabecera.getNumdocumento()) + ".xml");
			FileUtils.deleteQuietly(file);
			FileUtils.writeByteArrayToFile(file, xmlDocElectronico);
			emailDto.getAdjuntos().add(file);
			
			file = new File("/app/temp/" + ComprobanteHelper.formatNumDocumento(cabecera.getNumdocumento()) + ".pdf");
			FileUtils.deleteQuietly(file);
			FileUtils.writeByteArrayToFile(file, ride);
			emailDto.getAdjuntos().add(file);
			
			if(otrosAdjuntos!=null && !otrosAdjuntos.isEmpty()) {
				for (Map.Entry<String, byte[]> f : otrosAdjuntos.entrySet()) {
					file = new File("/app/temp/" + f.getKey());
					FileUtils.deleteQuietly(file);
					FileUtils.writeByteArrayToFile(file, f.getValue());
					emailDto.getAdjuntos().add(file);
				}
			}
			
			// contenido
			emailDto.setContenido(contenidoFacade(cabecera));
			
			// verificar si tiene correo test
			emailService.congigurarCorreoTest(emailDto, cabecera.getEstablecimiento().getEmpresa().getIdempresa(), cabecera.getEstablecimiento().getIdestablecimiento());
			
			
			estadoEmailComprobante.setIdCabecera(idCabecera);
			emailDto.setActualizaNegocio(estadoEmailComprobante);
			emailDto.getActualizaNegocio().cambiarEstado(EstadoEnvioEmailEnum.PROCESO);
			
			// verifica si existo TO elemet 
			if(emailDto.getCorreosTo()!=null && !emailDto.getCorreosTo().isEmpty()) {
				enviaEmailService.enviarCorreo(emailDto, true, 20);
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @return
	 * @throws DaoException
	 */
	private String contenidoFacade(Cabecera cabecera)throws DaoException{
		try {
			
			String plantilla = null;
			String contenido = null;
			
			// verificar si tiene plantilla personalizada
			if((boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO, TipoRetornoParametroGenerico.BOOLEAN, cabecera.getEstablecimiento().getIdestablecimiento())) {
				plantilla = parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO_PLANTILLA, TipoRetornoParametroGenerico.STRING, cabecera.getEstablecimiento().getIdestablecimiento());
			}else {
				plantilla = parametroGenericoServicio.consultarParametro(PGEmpresaEnum.EMAIL_CONTENIDO_PLANTILLA, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING);
			}
			
			switch (plantilla) {
			case "compElectronicov1.vm":
				contenido = comprovanteElectronicoV1(cabecera,"compElectronicov1.vm");
				break;

			default:
				break;
			}
			
			
			return contenido;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param nombrePLantilla
	 * @return
	 */
	private String comprovanteElectronicoV1(Cabecera cabecera,String nombrePLantilla) {
		
		Template template= new Template();
		template= VelocityTemplateUtil.obtenerContext(nombrePLantilla);
		VelocityContext context = new VelocityContext();
		// todos los datos a la plantilla
		context.put("contenidoEmail", cabecera.getContenidoEmail()==null?"":cabecera.getContenidoEmail());
		context.put("emisorRazonSocial", TextoUtil.stringToHTMLString(cabecera.getEstablecimiento().getEmpresa().getRazonsocial()));
		context.put("emisorNombreComercial", TextoUtil.stringToHTMLString(cabecera.getEstablecimiento().getNombrecomercial()));
		context.put("emisorRuc", cabecera.getEstablecimiento().getEmpresa().getRuc());
		context.put("emisorDireccion", TextoUtil.stringToHTMLString(cabecera.getEstablecimiento().getDireccionestablecimiento()));
		context.put("receptorNumComprobante", sustituirCaracteres(cabecera.getTipocomprobante().getComprobante()) + " : " + ComprobanteHelper.formatNumDocumento(cabecera.getNumdocumento()));
		context.put("compFecha",(new SimpleDateFormat("dd/MM/yyyy")).format(cabecera.getFechaemision()));
		
		NumberFormat df = DecimalFormat.getCurrencyInstance(new Locale("es", "EC"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		switch (GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador())) {
		case FACTURA:case LIQUIDACION_COMPRA:case NOTA_CREDITO:case NOTA_DEBITO:
			context.put("receptorRazonSocial", TextoUtil.stringToHTMLString(cabecera.getCliente().getRazonsocial()));
			context.put("compValor",df.format(cabecera.getTotalconimpuestos().doubleValue()));
			break;
		case RETENCION:
			context.put("receptorRazonSocial", TextoUtil.stringToHTMLString(cabecera.getCliente().getRazonsocial()));
			context.put("compValor",df.format(cabecera.getTotalretencion().doubleValue()));
			break;
		case GUIA_REMISION:
			context.put("receptorRazonSocial", TextoUtil.stringToHTMLString(cabecera.getTransportista().getRazonsocial()));
			context.put("compValor","$ 0.00");
			break;
		default:
			break;
		}
		context.put("vcwAnio", FechaUtil.getAnio(new Date()));
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	private String sustituirCaracteres(String cadena) {
		if(cadena!=null) {
			String[] mayusculasTile = msg.getString("mayuscula.tile").split(",");
			String[] mayusculasTileRemplazar = new String[] {"&Aacute;","&Eacute;","&Iacute;","&Oacute;","&Uacute;"};
			
			for (int i = 0; i < 5; i++) {
				cadena = cadena.replace(mayusculasTile[i],mayusculasTileRemplazar[i]);
			}
		}
		return cadena;
	}
	
}
