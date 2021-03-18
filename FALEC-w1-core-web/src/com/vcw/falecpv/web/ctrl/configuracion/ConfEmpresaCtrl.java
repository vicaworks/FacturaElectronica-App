/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.EtiquetaModuloEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.modelo.dto.ConfEstablecimientoDto;
import com.vcw.falecpv.core.modelo.dto.SmtpDto;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Etiqueta;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.EtiquetaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ConfEmpresaCtrl extends BaseCtrl {
	
	@EJB
	private EmpresaServicio empresaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private EtiquetaServicio etiquetaServicio;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6793595774124920786L;
	
	private Establecimiento establecimientoSelected;
	private Empresa empresaSelected;
	private UploadedFile file;
	private SmtpDto smtpDto;
	private ConfEstablecimientoDto confEstablecimientoDto;
	private List<Etiqueta> etiqueta1List;
	private List<Etiqueta> etiqueta2List;
	private Etiqueta etiquetaSelected;
	private boolean cotizacionAutorizacion = false;
	private boolean cotizacionEtiquetas = false;
	
	
	

	/**
	 * 
	 */
	public ConfEmpresaCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			empresaSelected = empresaServicio.consultarByPk(establecimientoMain.getEmpresa().getIdempresa());
			consultarSmtp();
			consultarConfiguracionEstablecimiento();
			consultarEtiquetaAnular();
			consultarEtiquetaTarea();
			consultarParametrosCotizacion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void upload(FileUploadEvent event)
	{
		file = event.getFile();
		try {
			if(file != null)
            {
				byte[] bytes;
		        bytes = IOUtils.toByteArray(file.getInputStream());
		        empresaSelected.setNombrearchivo(file.getFileName());
		        empresaSelected.setArchivofirmaelectronica(bytes);
		        AppJsfUtil.ajaxUpdate("fsvConfiguracion:TabConfEmpresa:frmCertificado");
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void limpiarArchivo() {
		try {
			empresaSelected.setArchivofirmaelectronica(null);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarArchivoFirma() {
		try {
			
			if(empresaSelected.getArchivofirmaelectronica()==null) {
				AppJsfUtil.addErrorMessage("fsvConfiguracion:TabConfEmpresa:frmCertificado:intNameFileFirmaDigital", "ERROR", "NO EXISTE ARCHIVO, DEBE SELECCIONAR.");
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),new Date())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", msg.getString("error.comprarfecha"));
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),empresaSelected.getFechaemision())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", msg.getString("error.comprarfecha2"));
				return;
			}
			
			empresaSelected.setUpdated(new Date());
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); 
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			AppJsfUtil.addInfoMessage("frmCertificado","OK", "CERTIFICADO ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarSmtp()throws DaoException{
		smtpDto = empresaServicio.getSmtpByEmpresa(empresaSelected.getIdempresa());
	}
	
	public void cambiarSmtp() {
		try {
			
			if(!smtpDto.isSmtpPropio()) {
				empresaServicio.guardarSmtp(smtpDto, empresaSelected.getIdempresa());
				consultarSmtp();
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmSMTP", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarSmtp() {
		try {
			
			empresaServicio.guardarSmtp(smtpDto, empresaSelected.getIdempresa());
			consultarSmtp();
			
			AppJsfUtil.addInfoMessage("frmSMTP","OK", "SERVIDOR SMTP ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmSMTP", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarConfiguracionEstablecimiento() throws DaoException{
		confEstablecimientoDto = establecimientoServicio.getConfiguracion(establecimientoMain.getIdestablecimiento());
	}
	
	public void cambioEstablecimiento() {
		try {
			consultarConfiguracionEstablecimiento();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public void guardarEmailTesting() {
		try {
			establecimientoServicio.guardarTestCorreos(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
			AppJsfUtil.addInfoMessage("frmEstEmailTest","OK", "PRUEBAS EMAIL ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstEmailTest", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarEmailPlantilla() {
		try {
			
			if(!(boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PERMISO_MODIFICACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmEstEmailpl", "ERROR", msg.getString("error.permiso"));
				consultarConfiguracionEstablecimiento();
				AppJsfUtil.updateComponente("fsvConfiguracion:TabConfEstablecimiento:frmEstEmailpl");
				return;
			}
			
			establecimientoServicio.guardarPlantillaEmail(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
			AppJsfUtil.addInfoMessage("frmEstEmailpl","OK", "PLANTILLA EMAIL ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstEmailpl", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarPlantillaComprobanteElectronico() {
		try {
			if(!(boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PERMISO_MODIFICACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmPlantilla", "ERROR", msg.getString("error.permiso"));
				consultarConfiguracionEstablecimiento();
				AppJsfUtil.updateComponente("fsvConfiguracion:TabConfEstablecimiento:frmPlantilla");
				return;
			}
			establecimientoServicio.guardarPlantillaComprobanteElectronico(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
			AppJsfUtil.addInfoMessage("frmPlantilla","OK", "PLANTILLAS COMPROBANTES ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmPlantilla", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cambiarEmailTesting() {
		try {
			
			if(!confEstablecimientoDto.isEmailTesting()) {
				establecimientoServicio.guardarTestCorreos(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
				consultarConfiguracionEstablecimiento();
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstEmailTest", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cambiarPlantillaEmail() {
		try {
			
			if(!confEstablecimientoDto.isPlantillaEmailEstablecimiento()) {
				establecimientoServicio.guardarPlantillaEmail(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
				consultarConfiguracionEstablecimiento();
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstEmailTest", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarEtiquetaTarea() throws DaoException {
		etiqueta1List = null;
		etiqueta1List = etiquetaServicio.getEtiquetas(establecimientoMain.getEmpresa().getIdempresa(),EtiquetaModuloEnum.TAREA_COTIZACION.toString());
	}
	
	public void consultarEtiquetaAnular() throws DaoException {
		etiqueta2List = null;
		etiqueta2List = etiquetaServicio.getEtiquetas(establecimientoMain.getEmpresa().getIdempresa(),EtiquetaModuloEnum.ANULAR_COTIZACION.toString());
	}
	
	public void consultarParametrosCotizacion()throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		cotizacionAutorizacion = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_AUTORIZACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa());
		cotizacionEtiquetas = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_ETIQUETAS_PREDEFIN, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa());
		
	}
	
	public void guardarParametrosCotizacion() {
		try {
			
			ParametroGenericoEmpresa pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_AUTORIZACION);
			pg.setValor(cotizacionAutorizacion?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_ETIQUETAS_PREDEFIN);
			pg.setValor(cotizacionEtiquetas?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			consultarParametrosCotizacion();
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmConfCotizacion", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the empresaSelected
	 */
	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	/**
	 * @param empresaSelected the empresaSelected to set
	 */
	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	/**
	 * @return the establecimientoSelected
	 */
	public Establecimiento getEstablecimientoSelected() {
		return establecimientoSelected;
	}

	/**
	 * @param establecimientoSelected the establecimientoSelected to set
	 */
	public void setEstablecimientoSelected(Establecimiento establecimientoSelected) {
		this.establecimientoSelected = establecimientoSelected;
	}

	/**
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * @return the smtpDto
	 */
	public SmtpDto getSmtpDto() {
		return smtpDto;
	}

	/**
	 * @param smtpDto the smtpDto to set
	 */
	public void setSmtpDto(SmtpDto smtpDto) {
		this.smtpDto = smtpDto;
	}

	/**
	 * @return the confEstablecimientoDto
	 */
	public ConfEstablecimientoDto getConfEstablecimientoDto() {
		return confEstablecimientoDto;
	}

	/**
	 * @param confEstablecimientoDto the confEstablecimientoDto to set
	 */
	public void setConfEstablecimientoDto(ConfEstablecimientoDto confEstablecimientoDto) {
		this.confEstablecimientoDto = confEstablecimientoDto;
	}

	/**
	 * @return the etiqueta1List
	 */
	public List<Etiqueta> getEtiqueta1List() {
		return etiqueta1List;
	}

	/**
	 * @param etiqueta1List the etiqueta1List to set
	 */
	public void setEtiqueta1List(List<Etiqueta> etiqueta1List) {
		this.etiqueta1List = etiqueta1List;
	}

	/**
	 * @return the etiqueta2List
	 */
	public List<Etiqueta> getEtiqueta2List() {
		return etiqueta2List;
	}

	/**
	 * @param etiqueta2List the etiqueta2List to set
	 */
	public void setEtiqueta2List(List<Etiqueta> etiqueta2List) {
		this.etiqueta2List = etiqueta2List;
	}

	/**
	 * @return the etiquetaSelected
	 */
	public Etiqueta getEtiquetaSelected() {
		return etiquetaSelected;
	}

	/**
	 * @param etiquetaSelected the etiquetaSelected to set
	 */
	public void setEtiquetaSelected(Etiqueta etiquetaSelected) {
		this.etiquetaSelected = etiquetaSelected;
	}

	/**
	 * @return the cotizacionAutorizacion
	 */
	public boolean isCotizacionAutorizacion() {
		return cotizacionAutorizacion;
	}

	/**
	 * @param cotizacionAutorizacion the cotizacionAutorizacion to set
	 */
	public void setCotizacionAutorizacion(boolean cotizacionAutorizacion) {
		this.cotizacionAutorizacion = cotizacionAutorizacion;
	}

	/**
	 * @return the cotizacionEtiquetas
	 */
	public boolean isCotizacionEtiquetas() {
		return cotizacionEtiquetas;
	}

	/**
	 * @param cotizacionEtiquetas the cotizacionEtiquetas to set
	 */
	public void setCotizacionEtiquetas(boolean cotizacionEtiquetas) {
		this.cotizacionEtiquetas = cotizacionEtiquetas;
	}

}
