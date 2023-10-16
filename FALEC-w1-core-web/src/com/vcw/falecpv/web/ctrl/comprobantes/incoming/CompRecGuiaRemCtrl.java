/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
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
import com.vcw.falecpv.core.modelo.xml.XmlDestinatario;
import com.vcw.falecpv.core.modelo.xml.XmlDestinatarioDetalle;
import com.vcw.falecpv.core.modelo.xml.XmlGuiaRemision;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.constante.ExportarFileEnum;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.FileUtilApp;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CompRecGuiaRemCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;
	
	@EJB
	private ComprobanteUtilServicio comprobanteUtilServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private boolean aplicarFechas=true;
	
	/**
	 * 
	 */
	public CompRecGuiaRemCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			
			Calendar cl = Calendar.getInstance();
			cl.setTime(new Date());
			cl.add(Calendar.MONTH, -1);
			cl.set(Calendar.DATE, 1);
			desde = cl.getTime();
			cl.add(Calendar.MONTH, 1);
			cl.add(Calendar.DATE, -1);
			hasta = cl.getTime();
			
			aplicarFechas=true;
			super.totalizarComprobantesRecibidos();
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
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvGuiaRemision:compGRSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.GUIA_REMISION, desde, hasta, criterioBusqueda,aplicarFechas);
			super.totalizarComprobantesRecibidos();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(comprobanteRecibidoList==null || comprobanteRecibidoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-GuiaRemisionRecibidas.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
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
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//rucTransportista"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//razonSocialTransportista"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//placa"));
				
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//dirPartida"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//fechaIniTransporte"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//fechaFinTransporte"));
				
				cell = row.createCell(col++);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-GuiaRemisionRecibidas-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return null;
	}
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(comprobanteRecibidoList==null || comprobanteRecibidoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-GuiaRemisionDetRecibidas.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
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
			
			
			int fila = 10;
			int filaDestinatario = fila;
			int filaDetalle = fila;
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				Cell cell = row.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//rucTransportista"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//razonSocialTransportista"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//placa"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//dirPartida"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//fechaIniTransporte"));
				
				cell = row.createCell(col++);
				cell.setCellValue(XmlCommonsUtil.valorXpath(c.getValorXml(), "//fechaFinTransporte"));
				
				cell = row.createCell(col++);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellValue(c.getClaveAcceso());
				
				
				XmlGuiaRemision g = XmlCommonsUtil.jaxbunmarshall(c.getValorXml(), new XmlGuiaRemision(),"UTF-8");
				filaDestinatario = fila;
				
				
				for (XmlDestinatario d : g.getDestinatarioList()) {
					
					col = 9;
					
					row = sheet.getRow(filaDestinatario);
					if(row==null) {
						row = sheet.createRow(filaDestinatario);
					}
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getIdentificacionDestinatario());
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getRazonSocialDestinatario());
					
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getDirDestinatario()!=null?d.getDirDestinatario():d.getDirDestinatario());
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getMotivoTraslado()!=null?d.getMotivoTraslado():"");
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getCodEstabDestino()!=null?d.getCodEstabDestino():"");
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getCodDocSustento()!=null?GenTipoDocumentoEnum.getByIdentificador(d.getCodDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getNumDocSustento()!=null?ComprobanteHelper.formatNumDocumento(d.getNumDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getFechaEmisionDocSustento()!=null?FechaUtil.formatoFecha(d.getFechaEmisionDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellValue(d.getNumAutDocSustento()!=null?d.getNumAutDocSustento():"");
					
					
					filaDetalle = filaDestinatario;
					for (XmlDestinatarioDetalle dd : d.getDestinatarioDetallesList()) {
						
						col = 18;
						
						row = sheet.getRow(filaDetalle);
						if(row==null) {
							row = sheet.createRow(filaDetalle);
						}
						
						cell = row.createCell(col++);
						cell.setCellValue(dd.getCodigoInterno());
						
						cell = row.createCell(col++);
						cell.setCellValue(dd.getDescripcion());
						
						cell = row.createCell(col++);
						cell.setCellValue(dd.getCantidad());
						
						
						filaDetalle++;
					}
					
					filaDestinatario = filaDetalle;
					
				}
				
				if(filaDetalle>filaDestinatario) {
					fila = filaDetalle;
				}else {
					fila = filaDestinatario;
				}
				fila ++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-GuiaRemisionDetRecibidas-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private Map<File, String> getAdjuntos(Comprobanterecibido cr){
		try {
			
			Map<File, String> adjuntos = new HashMap<>();
			
			FileUtilApp fileUtilApp = new FileUtilApp();
			fileUtilApp.setReportDir(AppConfiguracion.getString("dir.base.reporte").concat("compRecibidos/guiaRem/"));
			
			
			XmlGuiaRemision f = XmlCommonsUtil.jaxbunmarshall(cr.getValorXml(), new XmlGuiaRemision(),"UTF-8");
			f.setFechaAutorizacion(cr.getFechaAutorizacion());
			f.setNumeroAutorizacion(cr.getNumeroAutorizacion());
			comprobanteUtilServicio.populateGuiaRemision(f);
			String nombredocumento= f.getInfoTributaria().getRazonSocial() + "-" + f.getInfoTributaria().getSecuencial() + ".pdf";
			String documento="CR-GuiaRem-V1";
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
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen registros seleccionados", 
						Message.ERROR);
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
			String nombreFisico = FileUtilApp.zipDirectory("GuiaRemision");
			return FileUtilApp.downloadZip(nombreFisico, "GuiaRemision");
			
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
