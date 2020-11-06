/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import com.vcw.falecpv.core.modelo.xml.XmlDetalle;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlPago;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
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
public class CompRecFacturaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private ComprobanteUtilServicio comprobanteUtilServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private boolean aplicarFechas=true;
	
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
			
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvFactura:compFacSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.FACTURA, desde, hasta, criterioBusqueda,aplicarFechas);
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
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(comprobanteRecibidoList==null || comprobanteRecibidoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-FacDetRecibidas.xlsx");
			
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
			
			int fila = 10;
			int filaDetalle = 10;
			int filaPago = 10;
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
				
				
				// detalle
				filaDetalle = fila;
				filaPago = fila;
				XmlFactura f = XmlCommonsUtil.jaxbunmarshall(c.getValorXml(), new XmlFactura());
				for (XmlDetalle d : f.getDetalleList()) {
					
					col = 11;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getCodigoPrincipal());
					
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getDescripcion());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getCantidad());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPrecioUnitario());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getDescuento());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPrecioTotalSinImpuesto());
					
					Double ice = 0d;
					Double iva = 0d;
					for (XmlImpuesto i : d.getImpuestoList()) {
						if(i.getCodigo().equals("2")) {
							iva += i.getValor();
						}
						if(i.getCodigo().equals("3")) {
							ice += i.getValor();
						}
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(ice);
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(iva);
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPrecioTotalSinImpuesto() + ice + iva);
					
					filaDetalle++;
				}
				
				if(f.getInfoFactura().getPagoList()==null) {
					f.getInfoFactura().setPagoList(new ArrayList<>());
				}
				
				for (XmlPago p : f.getInfoFactura().getPagoList()) {
					col = 20;
					rowCliente = sheet.getRow(filaPago);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaPago);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(p.getFormaPago());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(tipopagoServicio.tipopagoSri(p.getFormaPago()));
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(p.getTotal());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(p.getPlazo()!=null?p.getPlazo():0);
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(p.getUnidadTiempo()!=null?p.getUnidadTiempo():"");
					
					filaPago++;
					
				}
				
				if(filaDetalle>filaPago) {
					fila = filaDetalle;
				}else {
					fila = filaPago;
				}
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-FacDetRecibidas-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
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
			
			System.out.println(cr.getSerieComprobante());
			
			Map<File, String> adjuntos = new HashMap<>();
			
			FileUtilApp fileUtilApp = new FileUtilApp();
			fileUtilApp.setReportDir(AppConfiguracion.getString("dir.base.reporte").concat("compRecibidos/factura/"));
			
			
			XmlFactura f = XmlCommonsUtil.jaxbunmarshall(cr.getValorXml(), new XmlFactura());
			f.setFechaAutorizacion(cr.getFechaAutorizacion());
			f.setNumeroAutorizacion(cr.getNumeroAutorizacion());
			
			if(f.getInfoFactura().getPagoList()!=null) {
				for (XmlPago p : f.getInfoFactura().getPagoList()) {
					p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
				}
			}
			
			// totalizar el comprobante
			f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteFactura(f, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), true));
			
			
			String nombredocumento= f.getInfoTributaria().getRazonSocial() + "-" + f.getInfoTributaria().getSecuencial() + ".pdf";
			String documento="CR-Factura-V1";
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
	
	
//	public void descargarSeleccion2() {
//		try {
//			
//			if(comprobanteRecibidoSeleccionList==null || comprobanteRecibidoSeleccionList.isEmpty()) {
//				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN REGISTROS SELECCIONADOS.");
//				return;
//			}
//			
//			Map<File, String> map = new HashMap<>();
//			
//			for (Comprobanterecibido comprobanterecibido : comprobanteRecibidoSeleccionList) {
//				map.putAll(getAdjuntos(comprobanterecibido));
//			}
//			
//			if (comprobanteRecibidoSeleccionList.size() == 1) {
//				for (Map.Entry<File, String> map2 : map.entrySet()) {
//					Faces.sendFile(map2.getKey(), true);
//				}
//			}else {
//				FileUtilApp.setFileMap(map);
//				Faces.sendFile(new File(FileUtilApp.zipDirectory("Facturas")), true);
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
//		}
//	}
	
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
