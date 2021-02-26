/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.ConfiguracionGeneralEnum;
import com.vcw.falecpv.core.modelo.persistencia.Segopcion;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.core.servicio.seg.SegperfilServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named(value="appSessionCtrl")
@SessionScoped
public class AppSessionCtrl implements Serializable {

	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private SegperfilServicio segperfilServicio;
	
	private String nombreEstablecimiento;
	private String nombreEmpresa;
	private String nombreDisplay;
	private Boolean administrador;
	private List<Segopcion> segopcionList;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6792152616234338008L;

	/**
	 * 
	 */
	public AppSessionCtrl() {
	}
	
	private void establecerdatosIniciales() {
		try {
			
			if (AppJsfUtil.getLoginUser()!=null) {
				// 1. datos del usuario
				Usuario usuario = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser());
				nombreDisplay = usuario.getNombrepantalla();
				administrador = usuario.getAdministrador().equals("S")?true:false;
				nombreEstablecimiento = usuario.getEstablecimiento().getNombrecomercial();
				nombreEmpresa = usuario.getEstablecimiento().getEmpresa().getNombrecomercial();
				
				// 2. guardar en session los datos de empresa y establecimeinto
				FacesUtil.getHttpSession(false).setAttribute("establecimiento", usuario.getEstablecimiento());
				FacesUtil.getHttpSession(false).setAttribute("usuario", usuario);
				
				// 3. perfiles de acceso
				segopcionList = segperfilServicio.getPerfilOpcionAcceso(usuario.getIdusuario(),ConfiguracionGeneralEnum.SISTEMA_ID.getId());
				FacesUtil.getHttpSession(false).setAttribute("segopcionList", segopcionList);
				
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(null,TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @param identificador
	 * @return
	 */
	public boolean accesoDisable(String identificador) {
		if(segopcionList!=null) {
			Segopcion op = segopcionList.stream().filter(x->x.getIdentificador().equals(identificador)).findFirst().orElse(null);
			if(op!=null)
				return false;
		}
		return true;
	}
	
	/**
	 * @param identificador
	 * @return
	 */
	public boolean accesoRender(String identificador) {
		if(segopcionList!=null) {
			Segopcion op = segopcionList.stream().filter(x->x.getIdentificador().equals(identificador)).findFirst().orElse(null);
			if(op!=null)
				return true;
		}
		return false;
	}
	
	public String getFormatoMoneda() {
		return "$ #,##0.00";
	}
	
	public String getFormatoPorcentaje() {
		return "#,##0.00";
	}
	
	public String getFormatoInteger() {
		return "#,##0";
	}
	
	public String getFormatoNumber() {
		return "#,##0.00";
	}
	
	public String getFormatoNumberDecimalOpcional() {
		return "#,##0.##";
	}
	
	public String getLabelComprobante(String identificador) {
		
		if(Arrays.asList(new String[] {"07","03"}).contains(identificador)) {
			return "PROVEEDOR";
		}
		
		return "CLIENTE";
		
	}
	
	public boolean isAnularFactura(String estado) {
		ComprobanteEstadoEnum estadoEnum = ComprobanteEstadoEnum.getByEstado(estado);
		List<ComprobanteEstadoEnum> lista = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.ANULADO,ComprobanteEstadoEnum.RECIBIDO_SRI,ComprobanteEstadoEnum.PENDIENTE});
		
		return lista.contains(estadoEnum);
	}
	
	public boolean isComprobantes(String estado) {
		ComprobanteEstadoEnum estadoEnum = ComprobanteEstadoEnum.getByEstado(estado);
		List<ComprobanteEstadoEnum> lista = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ANULADO,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI});
		
		return lista.contains(estadoEnum);
	}
	
	public boolean isEnviarSri(String estado) {
		ComprobanteEstadoEnum estadoEnum = ComprobanteEstadoEnum.getByEstado(estado);
		List<ComprobanteEstadoEnum> lista = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ANULADO});
		
		return lista.contains(estadoEnum);
	}
	
	public boolean isEnviarEmail(String estado) {
		ComprobanteEstadoEnum estadoEnum = ComprobanteEstadoEnum.getByEstado(estado);
		List<ComprobanteEstadoEnum> lista = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ANULADO,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI,ComprobanteEstadoEnum.PENDIENTE,ComprobanteEstadoEnum.RECHAZADO_SRI,ComprobanteEstadoEnum.RECIBIDO_SRI});
		
		return lista.contains(estadoEnum);
	}
	
	public String formatoCadena(String cadena,int longitud,String caracter) {
		return TextoUtil.leftPadTexto(cadena, longitud, caracter);
	}
	
	
	/**
	 * @return the nombreEstablecimiento
	 */
	public String getNombreEstablecimiento() {
		
		if(nombreEstablecimiento==null) {
			establecerdatosIniciales();
		}
		
		return nombreEstablecimiento;
	}

	/**
	 * @param nombreEstablecimiento the nombreEstablecimiento to set
	 */
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}

	/**
	 * @return the nombreDisplay
	 */
	public String getNombreDisplay() {
		if (nombreDisplay==null) {
			establecerdatosIniciales();
		}
		return nombreDisplay;
	}

	/**
	 * @param nombreDisplay the nombreDisplay to set
	 */
	public void setNombreDisplay(String nombreDisplay) {
		this.nombreDisplay = nombreDisplay;
	}

	/**
	 * @return the usuarioServicio
	 */
	public UsuarioServicio getUsuarioServicio() {
		return usuarioServicio;
	}

	/**
	 * @param usuarioServicio the usuarioServicio to set
	 */
	public void setUsuarioServicio(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	/**
	 * @return the administrador
	 */
	public Boolean getAdministrador() {
		if(administrador==null) {
			establecerdatosIniciales();
		}
		return administrador;
	}

	/**
	 * @param administrador the administrador to set
	 */
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	/**
	 * @return the nombreEmpresa
	 */
	public String getNombreEmpresa() {
		if(nombreEmpresa==null) {
			establecerdatosIniciales();
		}
		return nombreEmpresa;
	}

	/**
	 * @param nombreEmpresa the nombreEmpresa to set
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	/**
	 * @return the segopcionList
	 */
	public List<Segopcion> getSegopcionList() {
		return segopcionList;
	}

	/**
	 * @param segopcionList the segopcionList to set
	 */
	public void setSegopcionList(List<Segopcion> segopcionList) {
		this.segopcionList = segopcionList;
	}	

}
