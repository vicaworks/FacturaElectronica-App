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
import org.primefaces.util.IOUtils;

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
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
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
	private boolean cotizacionProductoDetalle = false;
	private boolean cotizacionUsuarioVisualizacion = false;
	
	
	

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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void limpiarArchivo() {
		try {
			empresaSelected.setArchivofirmaelectronica(null);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void guardarArchivoFirma() {
		try {
			
			if(empresaSelected.getArchivofirmaelectronica()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe archivo debe seleccionar", 
						Message.ERROR);
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),new Date())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "Error", msg.getString("error.comprarfecha"));
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),empresaSelected.getFechaemision())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "Error", msg.getString("error.comprarfecha2"));
				return;
			}
			
			empresaSelected.setUpdated(new Date());
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); 
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Certificado almacenado correctamente", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void guardarSmtp() {
		try {
			
			empresaServicio.guardarSmtp(smtpDto, empresaSelected.getIdempresa());
			consultarSmtp();
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Servidor SMTP almacendo correctamente", 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);		
		}
	}
	
	public void guardarEmailTesting() {
		try {
			establecimientoServicio.guardarTestCorreos(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Email de pruebas almacenado correctamente", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void guardarEmailPlantilla() {
		try {
			
			if(!(boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PERMISO_MODIFICACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmEstEmailpl", "ERROR", msg.getString("error.permiso"));
				consultarConfiguracionEstablecimiento();
				AppJsfUtil.updateComponente("fsvConfiguracion:tabEnviEmailPl:frmEstEmailpl");
				return;
			}
			
			establecimientoServicio.guardarPlantillaEmail(establecimientoMain.getIdestablecimiento(), confEstablecimientoDto);
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Plantilla de email almacenada correctamente", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	public void guardarEstablecimiento() {
		try {
			
			if(!(boolean)parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PERMISO_MODIFICACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmEnvEmail", "ERROR", msg.getString("error.permiso"));
				consultarConfiguracionEstablecimiento();
				AppJsfUtil.updateComponente("fsvConfiguracion:TabConfEstablecimiento:frmEnvEmail");
				return;
			}
			
			establecimientoServicio.actualizar(establecimientoMain);
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Datos actualizados correctamente", 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Plantillas de comprobantes almacenados correctamente.", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
		cotizacionProductoDetalle = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_PRODUCTO_DESCRIPCION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa());
		cotizacionUsuarioVisualizacion = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_VENDEDOR_VISUALIZACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa());
		
	}
	
	public void guardarParametrosCotizacion() {
		try {
			
			ParametroGenericoEmpresa pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_AUTORIZACION);
			pg.setValor(cotizacionAutorizacion?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_ETIQUETAS_PREDEFIN);
			pg.setValor(cotizacionEtiquetas?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_PRODUCTO_DESCRIPCION);
			pg.setValor(cotizacionProductoDetalle?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			
			pg = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEmpresa(establecimientoMain.getEmpresa().getIdempresa(), PGEmpresaEnum.COTIZACION_VENDEDOR_VISUALIZACION);
			pg.setValor(cotizacionUsuarioVisualizacion?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(pg);
			
			consultarParametrosCotizacion();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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

	/**
	 * @return the cotizacionProductoDetalle
	 */
	public boolean isCotizacionProductoDetalle() {
		return cotizacionProductoDetalle;
	}

	/**
	 * @param cotizacionProductoDetalle the cotizacionProductoDetalle to set
	 */
	public void setCotizacionProductoDetalle(boolean cotizacionProductoDetalle) {
		this.cotizacionProductoDetalle = cotizacionProductoDetalle;
	}

	/**
	 * @return the cotizacionUsuarioVisualizacion
	 */
	public boolean isCotizacionUsuarioVisualizacion() {
		return cotizacionUsuarioVisualizacion;
	}

	/**
	 * @param cotizacionUsuarioVisualizacion the cotizacionUsuarioVisualizacion to set
	 */
	public void setCotizacionUsuarioVisualizacion(boolean cotizacionUsuarioVisualizacion) {
		this.cotizacionUsuarioVisualizacion = cotizacionUsuarioVisualizacion;
	}

}
