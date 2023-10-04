/**
 * 
 */
package com.vcw.falecpv.web.ctrl.pagos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
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
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.query.PagosQuery;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PagosMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7546960343866409050L;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private Date desde;
	private Date hasta;
	private Tipopago tipopagoSelected;
	private List<PagosQuery> pagosQuerieList;
	private List<PagosQuery> pagosQueryTotal;

	/**
	 * 
	 */
	public PagosMainCtrl() {
		
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			tipopagoSelected = null;
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -10);
			populateTipoPago();
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void buscar() {
		try {
			AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
			consultar();
			populateTipoPago();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultar() throws DaoException {
		pagosQuerieList = null;
		pagosQuerieList = pagoServicio.getPagos(establecimientoMain.getIdestablecimiento(), desde, hasta,tipopagoSelected);
		pagosQueryTotal = null;
		pagosQueryTotal = pagoServicio.getPagosTotal(establecimientoMain.getIdestablecimiento(), desde, hasta,tipopagoSelected);
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(pagosQuerieList==null || pagosQuerieList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-PagosEmitidos.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			int fila = 8;
			
			for (PagosQuery p : pagosQuerieList) {
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(p.getUpdated()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(p.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(p.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getNombre());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getValorretenido().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotal().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getValorentrega().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getCambio().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getNumerodocumento());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getNombrebanco());
				
				fila++;
			}
			
			fila++;
			rowCliente = sheet.createRow(fila);
			rowCliente.createCell(0).setCellValue("TOTALES");
			fila++;
			for (PagosQuery p : pagosQueryTotal) {
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getNombre());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotal().doubleValue());
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-PagosEmitidos-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
		return null;
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
	 * @return the tipopagoServicio
	 */
	public TipopagoServicio getTipopagoServicio() {
		return tipopagoServicio;
	}

	/**
	 * @param tipopagoServicio the tipopagoServicio to set
	 */
	public void setTipopagoServicio(TipopagoServicio tipopagoServicio) {
		this.tipopagoServicio = tipopagoServicio;
	}

	/**
	 * @return the tipopagoSelected
	 */
	public Tipopago getTipopagoSelected() {
		return tipopagoSelected;
	}

	/**
	 * @param tipopagoSelected the tipopagoSelected to set
	 */
	public void setTipopagoSelected(Tipopago tipopagoSelected) {
		this.tipopagoSelected = tipopagoSelected;
	}

	/**
	 * @return the pagosQuerieList
	 */
	public List<PagosQuery> getPagosQuerieList() {
		return pagosQuerieList;
	}

	/**
	 * @param pagosQuerieList the pagosQuerieList to set
	 */
	public void setPagosQuerieList(List<PagosQuery> pagosQuerieList) {
		this.pagosQuerieList = pagosQuerieList;
	}

	/**
	 * @return the pagosQueryTotal
	 */
	public List<PagosQuery> getPagosQueryTotal() {
		return pagosQueryTotal;
	}

	/**
	 * @param pagosQueryTotal the pagosQueryTotal to set
	 */
	public void setPagosQueryTotal(List<PagosQuery> pagosQueryTotal) {
		this.pagosQueryTotal = pagosQueryTotal;
	}

	
}
