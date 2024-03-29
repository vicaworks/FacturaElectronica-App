/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;
import com.vcw.falecpv.core.servicio.CabeceraRetencionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.common.RideCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class RetencionMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3490916972455247661L;

	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private CabeceraRetencionServicio cabeceraRetencionServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;
	
	@EJB
	private RetencionimpuestoServicio retencionimpuestoServicio;
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> retencionList;
	private RetencionFrmCtrl retencionFormCtrl;
	private Cabecera retencionSelected;
	private boolean seleccion = false;
	private TotalesDto totalesDto = new TotalesDto();
	
	/**
	 * 
	 */
	public RetencionMainCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			criterioBusqueda = null;
			retencionFormCtrl = (RetencionFrmCtrl) AppJsfUtil.getManagedBean("retencionFrmCtrl");
			consultarRetenciones();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarRetenciones()throws DaoException{
		seleccion = false;		
		AppJsfUtil.limpiarFiltrosDataTable("formMain:retencionDT");
		retencionList = null;
		retencionList = cabeceraServicio.getCabeceraDao().getByRetencionCriteria(desde, hasta, criterioBusqueda, establecimientoMain.getIdestablecimiento(),estado);
		totalizar();
	}
	
	@Override
	public void refrescar() {
		try {
			
			consultarRetenciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() throws DaoException {
		totalesDto = new TotalesDto();
		
		if(retencionList!=null) {
			totalesDto.setTotalRetenido(BigDecimal.valueOf(retencionList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalretencion().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			
			totalesDto.setSubtotalDtoList(
					retencionimpuestoServicio.getResumentRetencionImpuestoByIdCabecera(retencionList.stream().filter(x->!x.getEstado().equals("ANULADO")).map(x->x.getIdcabecera()).collect(Collectors.joining("','")))
					);			
		}
		
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(retencionSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			String analisis = cabeceraServicio.analizarEstadoComprobante(retencionSelected.getIdcabecera(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraRetencionServicio.anularRetencion(retencionSelected.getIdcabecera());
			consultarRetenciones();
			
			// datos de la pantalla de comppras
			AdquisicionMainCtrl adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			adquisicionMainCtrl.consultarAdquisiciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	
	public String editarRetencion(String idRetencion) {
		try {
			retencionFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			retencionFormCtrl.editarRetencion(idRetencion);
			
			return "./retencion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			return null;
		}
	}

	public String nuevaRetencion() {
		try {
			
			retencionFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			retencionFormCtrl.nuevaRetencionDispacher();
			
			return "./retencion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public StreamedContent getFileRetencionDetalle() {
		
		try {
			
			if(retencionList==null || retencionList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RetencionesDet.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos de la cabecera
			Row row = sheet.getRow(3);
			Cell cell = row.createCell(1);
			row.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(hasta));
			
			row = sheet.getRow(6);
			cell = row.createCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			List<Impuestoretencion> impuestoretencionAllList = new ArrayList<>();
			
			int fila = 10;
			int filaDt = 10;
			
			for (Cabecera r : retencionList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				// datos de la cabecera
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(r.getNumdocumento()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(r.getFechaemision()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getEstado());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getCliente().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getCliente().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getTipocomprobanteretencion().getComprobante());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(r.getNumdocasociado()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(r.getFechaemisiondocasociado()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getPeriodofiscal());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(r.getTotalretencion().doubleValue());
				
				filaDt = fila;
				
				List<Impuestoretencion> impuestoretencionList = cabeceraRetencionServicio.getDetalleById(r.getIdcabecera()); 
				impuestoretencionAllList.addAll(impuestoretencionList);
				for (Impuestoretencion rd : impuestoretencionList) {
					
					col = 10;
					
					row = sheet.getRow(filaDt);
					if(row==null) {
						row = sheet.createRow(filaDt);
					}
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(rd.getCodigo());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(rd.getRetencionimpuestodet().getRetencionimpuesto().getNombre());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getRetencionimpuestodet().getNombre());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getBaseimponible().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getRetencionimpuestodet().getValor().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getValorretenido().doubleValue());
					
					filaDt++;
					
				}
				
				fila += (filaDt - fila);
				
			}
			
			// totales por tipo de impuesto
			
			if(!impuestoretencionAllList.isEmpty()) {
				List<String> codImpuestoList = impuestoretencionAllList.stream().map(x->x.getCodigo()).distinct().collect(Collectors.toList()); 
				for (String imp : codImpuestoList) {
					
					row = sheet.getRow(++fila);
					row = sheet.createRow(fila);
					// codigo
					cell = row.createCell(10);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(imp);
					
					// descripcion
					cell = row.createCell(11);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(impuestoretencionAllList.stream().filter(x->x.getCodigo().equals(imp)).findFirst().orElse(null).getRetencionimpuestodet().getRetencionimpuesto().getNombre());
					
					// total
					cell = row.createCell(12);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(impuestoretencionAllList.stream().filter(x->x.getCodigo().equals(imp)).mapToDouble(x->x.getValorretenido().doubleValue()).sum());
					
				}
			}
			
			
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-RetencionesDet_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial()+".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
		
	}
	
	public StreamedContent getFileRetencion() {
		
		try {
			
			if(retencionList==null || retencionList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-Retenciones.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos de la cabecera
			Row row = sheet.getRow(3);
			Cell cell = row.createCell(1);
			cell.setCellValue(establecimientoMain.getNombrecomercial());
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(hasta));
			
			row = sheet.getRow(6);
			cell = row.createCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			int fila = 9;
			
			for (Cabecera r : retencionList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				// datos de la cabecera
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(r.getNumdocumento()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(r.getFechaemision()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getEstado());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getCliente().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getCliente().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getTipocomprobanteretencion().getComprobante());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(r.getNumdocasociado()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(r.getFechaemisiondocasociado()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getPeriodofiscal());
				
				// detalles de cada impuesto los totales
				r.setImpuestoretencionList(cabeceraRetencionServicio.getDetalleById(r.getIdcabecera()));
				// renta
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getImpuestoretencionList().stream().filter(x->x.getCodigo().equals("1")).mapToDouble(x->x.getValorretenido().doubleValue()).sum());
				
				// iva
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(r.getImpuestoretencionList().stream().filter(x->x.getCodigo().equals("2")).mapToDouble(x->x.getValorretenido().doubleValue()).sum());
				
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(r.getTotalretencion().doubleValue());
				
				fila ++;
				
			}
			
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-Retenciones_" + establecimientoMain.getCodigoestablecimiento() + "_" +  establecimientoMain.getNombrecomercial()+".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
		
	}
	
	public void changeSeleccion() {
		try {
			
			if(retencionList!=null) {
				for (Cabecera v : retencionList) {
					if(!v.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && !v.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
						v.setSeleccion(this.seleccion);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmarEnviar() {
		try {
			
			if(retencionList==null || retencionList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			
			for (Cabecera r : retencionList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				Cabecera c = new Cabecera();
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				c.setIdcabecera(r.getIdcabecera());
				c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
				sriDispacher.queue_comprobanteSriDispacher(c);
				
			}
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FIRMAR Y ENVIAR COMPROBANTE SRI", msg.getString("mensaje.enviarComprobante"));
	        PrimeFaces.current().dialog().showMessageDynamic(message,true);
			
	        consultarRetenciones();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void enviarEmail() {
		try {
			
			if(retencionList==null || retencionList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			for (Cabecera cabecera : retencionList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				if(cabecera.getEstado().equals(ComprobanteEstadoEnum.AUTORIZADO.toString())) {
					emailComprobanteServicio.enviarComprobanteFacade(null, null, null, cabecera.getIdcabecera(), null, null, null, null,true);
				}
			}
			AppJsfUtil.addInfoMessage("formMain", "OK", "LOS CORREOS HAN SIDO ENVIADOS CORRECTAMENTE, SOLO LOS COMPROBANTES QUE ESTAN EN ESTADO AUTORIZADO.");
	        consultarRetenciones();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the desde
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @param desde the desde to set
	 */
	public void setDesde(Date desde) {
		this.desde = desde;
	}

	/**
	 * @return the hasta
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * @param hasta the hasta to set
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}
	
	/**
	 * @return the retencionFormCtrl
	 */
	public RetencionFrmCtrl getRetencionFormCtrl() {
		return retencionFormCtrl;
	}

	/**
	 * @param retencionFormCtrl the retencionFormCtrl to set
	 */
	public void setRetencionFormCtrl(RetencionFrmCtrl retencionFormCtrl) {
		this.retencionFormCtrl = retencionFormCtrl;
	}

	/**
	 * @return the retencionList
	 */
	public List<Cabecera> getRetencionList() {
		return retencionList;
	}

	/**
	 * @param retencionList the retencionList to set
	 */
	public void setRetencionList(List<Cabecera> retencionList) {
		this.retencionList = retencionList;
	}

	/**
	 * @return the retencionSelected
	 */
	public Cabecera getRetencionSelected() {
		return retencionSelected;
	}

	/**
	 * @param retencionSelected the retencionSelected to set
	 */
	public void setRetencionSelected(Cabecera retencionSelected) {
		this.retencionSelected = retencionSelected;
	}

	/**
	 * @return the rideCtrl
	 */
	public RideCtrl getRideCtrl() {
		return rideCtrl;
	}

	/**
	 * @param rideCtrl the rideCtrl to set
	 */
	public void setRideCtrl(RideCtrl rideCtrl) {
		this.rideCtrl = rideCtrl;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the totalesDto
	 */
	public TotalesDto getTotalesDto() {
		return totalesDto;
	}

	/**
	 * @param totalesDto the totalesDto to set
	 */
	public void setTotalesDto(TotalesDto totalesDto) {
		this.totalesDto = totalesDto;
	}

}
