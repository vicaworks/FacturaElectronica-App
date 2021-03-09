package com.vcw.falecpv.web.servicio.emailcomprobante;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPlantillasEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.web.util.VelocityTemplateUtil;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;

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

}
