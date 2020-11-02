/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;
import com.vcw.falecpv.core.modelo.xml.XmlPago;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.constante.ExportarFileEnum;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.FileUtilApp;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CompRecNotaDebitoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private ComprobanteUtilServicio comprobanteUtilServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private boolean aplicarFechas=true;
	
	/**
	 * 
	 */
	public CompRecNotaDebitoCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			aplicarFechas=true;
			super.totalizarComprobantesRecibidos();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvNotaDebito:compNDSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.NOTA_DEBITO, desde, hasta, criterioBusqueda,aplicarFechas);
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
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotDebitoRecibidas.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row row = sheet.getRow(3);
			row.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			row = sheet.getRow(4);
			row.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			row = sheet.getRow(5);
			row.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(6);
			row.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 9;
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				Cell cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRucEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRazonSocialEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//codDocModificado")!=null?GenTipoDocumentoEnum.getByIdentificador(XmlCommonsUtil.valorXpath(c.getValorXml(), "//codDocModificado")):"");
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//numDocModificado"));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalsinimpuestos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalconimpuestos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				fila ++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotDebitoRecibidas-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(comprobanteRecibidoList==null || comprobanteRecibidoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotDebitoDetRecibidas.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row row = sheet.getRow(3);
			row.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			row = sheet.getRow(4);
			row.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			row = sheet.getRow(5);
			row.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(6);
			row.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 9;
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				Cell cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRucEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getRazonSocialEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//codDocModificado")!=null?GenTipoDocumentoEnum.getByIdentificador(XmlCommonsUtil.valorXpath(c.getValorXml(), "//codDocModificado")):"");
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//numDocModificado"));
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalsinimpuestos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalconimpuestos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				fila ++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotDebitoDetRecibidas-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void showRide() {
		try {
			
			for (Map.Entry<File, String> map : getAdjuntos(comprobanteRecibidoSelected).entrySet()) {
				
				FileUtilApp.moveFile(map.getKey(), "temp");
				rideCtrl.setUrl("../../temp/" + map.getKey().getName());
				
			}
			
			AppJsfUtil.showModalRender("dlgRide", "formRide");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private Map<File, String> getAdjuntos(Comprobanterecibido cr){
		try {
			
			Map<File, String> adjuntos = new HashMap<>();
			
			FileUtilApp fileUtilApp = new FileUtilApp();
			fileUtilApp.setReportDir(AppConfiguracion.getString("dir.base.reporte").concat("compRecibidos/notaDebito/"));
			
			
			XmlNotaDebito f = XmlCommonsUtil.jaxbunmarshall(cr.getValorXml(), new XmlNotaDebito());
			f.setFechaAutorizacion(cr.getFechaAutorizacion());
			f.setNumeroAutorizacion(cr.getNumeroAutorizacion());
			
			// tipo de comprobante
			f.getInfoNotaDebito().setComprobanteModificado(tipocomprobanteServicio
					.getByTipoDocumento(
							GenTipoDocumentoEnum.getEnumByIdentificador(f.getInfoNotaDebito().getCodDocModificado()))
					.getComprobante());
			
			for (XmlPago p : f.getInfoNotaDebito().getPagoList()) {
				p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
			}
			
			// totalizar el comprobante
			f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteNotaDebito(f, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			
			
			String nombredocumento= f.getInfoTributaria().getRazonSocial() + "-" + f.getInfoTributaria().getSecuencial() + ".pdf";
			String documento="CR-NotaDebito-V1";
			adjuntos.put(fileUtilApp.getFileFile(documento, f, ExportarFileEnum.PDF),nombredocumento);
			
			return adjuntos;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	public StreamedContent getDescargarSeleccion() {
		try {
			
			if(comprobanteRecibidoSeleccionList==null || comprobanteRecibidoSeleccionList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN REGISTROS SELECCIONADOS.");
				return null;
			}
			
			Map<File, String> map = new HashMap<>();
			
			for (Comprobanterecibido comprobanterecibido : comprobanteRecibidoSeleccionList) {
				map.putAll(getAdjuntos(comprobanterecibido));
			}
			
			if (comprobanteRecibidoSeleccionList.size() == 1) {
				for (Map.Entry<File, String> map2 : map.entrySet()) {
					return AppJsfUtil.downloadFile(map2.getKey(), map2.getValue());
				}
			} 
			
			FileUtilApp.setFileMap(map);
			String nombreFisico = FileUtilApp.zipDirectory("Facturas");
			return FileUtilApp.downloadZip(nombreFisico, "Facturas");
			
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

	/**
	 * @return the aplicarFechas
	 */
	public boolean isAplicarFechas() {
		return aplicarFechas;
	}

	/**
	 * @param aplicarFechas the aplicarFechas to set
	 */
	public void setAplicarFechas(boolean aplicarFechas) {
		this.aplicarFechas = aplicarFechas;
	}
	
	

}
