/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.StreamedContent;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CompRecFacturaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	public CompRecFacturaCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			super.totalizarComprobantesRecibidos();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvFactura:compFacSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.FACTURA, desde, hasta, criterioBusqueda);
			super.totalizarComprobantesRecibidos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(comprobanteRecibidoList==null || comprobanteRecibidoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-FacRecibidas.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			int fila = 9;
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRucEmisor());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRazonSocialEmisor());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalsinimpuestos().add(c.getTotaldescuento()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				fila++;
			}
			fila++;
			// totales
			rowCliente = sheet.createRow(fila);
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-FacRecibidas-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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

}
