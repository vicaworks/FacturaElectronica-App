/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Retencion;
import com.vcw.falecpv.core.modelo.persistencia.Retenciondetalle;
import com.vcw.falecpv.core.servicio.RetencionServicio;
import com.vcw.falecpv.core.servicio.RetenciondetalleServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
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
	private RetencionServicio retencionServicio;
	
	@EJB
	private RetenciondetalleServicio retenciondetalleServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Retencion> retencionList;
	private RetencionFrmCtrl retencionFormCtrl;
	private Retencion retencionSelected;
	
	
	/**
	 * 
	 */
	public RetencionMainCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -90);
			criterioBusqueda = null;
			retencionFormCtrl = (RetencionFrmCtrl) AppJsfUtil.getManagedBean("retencionFrmCtrl");
			consultarRetenciones();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarRetenciones()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:retencionDT");
		retencionList = null;
		retencionList = retencionServicio.getRetencionDao()
				.getByCriteria(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), desde, hasta, criterioBusqueda);
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
	
	@Override
	public void eliminar() {
		try {
			
			retencionSelected = retencionServicio.consultarByPk(retencionSelected.getIdretencion());
			
			if(retencionSelected == null) return;
			
			String analisisEstado = retencionServicio.analizarEstado(retencionSelected.getIdretencion(), retencionSelected.getEstablecimiento().getIdestablecimiento(), "ANULAR");
			
			if(analisisEstado!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
				return;
			}
			
			retencionServicio.anularRetencion(retencionSelected.getIdretencion());
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
			
			retencionFormCtrl.nuevaRetencionDispacher();
			
			return "./retencion_form.jsf?faces-redirect=true";
			
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
			cell.setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(hasta));
			
			row = sheet.getRow(6);
			cell = row.createCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			int fila = 10;
			int filaDt = 10;
			
			for (Retencion r : retencionList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				// datos de la cabecera
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(r.getFechaemision()));
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getAnio());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getMes());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getNumcomprobante());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getNumautorizacion());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getTipocomprobante().getComprobante());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getProveedor().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getProveedor().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getProveedor().getEstado());
				
				cell = row.createCell(col++);
				cell.setCellValue(r.getTotalretencion().doubleValue());
				
				filaDt = fila + 1;
				for (Retenciondetalle rd : retenciondetalleServicio.getByRetencion(r.getIdretencion())) {
					
					col = 11;
					
					row = sheet.getRow(filaDt);
					if(row==null) {
						row = sheet.createRow(filaDt);
					}
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getRetencionimpuestodet().getCodigo());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getRetencionimpuestodet().getNombre());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getBaseimponible().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getRetencionimpuestodet().getValor().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellValue(rd.getValor().doubleValue());
					
					filaDt++;
					
				}
				
				fila += (filaDt - fila);
				
			}
			
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-Retenciones_" + AppJsfUtil.getEstablecimiento().getNombrecomercial()+".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
		
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
	 * @return the retencionList
	 */
	public List<Retencion> getRetencionList() {
		return retencionList;
	}

	/**
	 * @param retencionList the retencionList to set
	 */
	public void setRetencionList(List<Retencion> retencionList) {
		this.retencionList = retencionList;
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
	 * @return the retencionSelected
	 */
	public Retencion getRetencionSelected() {
		return retencionSelected;
	}

	/**
	 * @param retencionSelected the retencionSelected to set
	 */
	public void setRetencionSelected(Retencion retencionSelected) {
		this.retencionSelected = retencionSelected;
	}
	

}