/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Node;
import org.primefaces.model.StreamedContent;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
import com.vcw.falecpv.core.servicio.ComprobanterecibidoServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestoServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestodetServicio;
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
public class CompRecRetencionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;
	
	@EJB
	private RetencionimpuestoServicio retencionimpuestoServicio;
	@EJB
	private RetencionimpuestodetServicio retencionimpuestodetServicio;
	@EJB
	private TipopagoServicio tipopagoServicio;
	@EJB
	private ComprobanteUtilServicio comprobanteUtilServicio;
	@EJB
	private EstablecimientoServicio establecimientoServicio;	
	@EJB
	private ComprobanterecibidoServicio comprobanterecibidoServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private boolean aplicarFechas=true;
	/**
	 * 
	 */
	public CompRecRetencionCtrl() {
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
			
			comprobanteRecibidoList = null;
			comprobanteRecibidoSeleccionList = null;
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
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvRetencion:compRetSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.RETENCION, desde, hasta, criterioBusqueda,aplicarFechas);
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
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RetencionesRecibidas.xlsx");
			
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
			
			int fila = 9;
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getRucEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getRazonSocialEmisor());
				
				
				Document document = XmlCommonsUtil.stringToDocument(c.getValorXml());
				List<Node> nodoList = XmlCommonsUtil.aplicarXpath(document, "//impuestos/impuesto");
				if(nodoList==null || nodoList.isEmpty()) {
					nodoList = XmlCommonsUtil.aplicarXpath(document, "//docsSustento/docSustento");
				}
				List<String> tipDocAsociado = new ArrayList<>();
				List<String> numDocAsociado = new ArrayList<>();
				for (Node node : nodoList) {
					String  codDocSustento = XmlCommonsUtil.valorXpath(node.asXML(), "//codDocSustento");
					String  numDocSustento = XmlCommonsUtil.valorXpath(node.asXML(), "//numDocSustento");
					
					if(!tipDocAsociado.contains(codDocSustento)) {
						tipDocAsociado.add(codDocSustento);
					}
					if(!numDocAsociado.contains(numDocSustento)) {
						numDocAsociado.add(numDocSustento);
					}
				}
				
				if(tipDocAsociado.size()==1) {
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(GenTipoDocumentoEnum.getByIdentificador(tipDocAsociado.get(0)));
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(ComprobanteHelper.formatNumDocumento(numDocAsociado.get(0)));
				}else {
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("");
				}
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotalrenta().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotalretencion().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				fila ++;
			}

			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-RetencionesRecibidas_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");

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
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RetencionesDetRecibidas.xlsx");
			
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
			for (Comprobanterecibido c : comprobanteRecibidoList) {
				
				row = sheet.createRow(fila);
				int col =0;
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(c.getSerieComprobante()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaEmision()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(c.getFechaAutorizacion()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getRucEmisor());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getRazonSocialEmisor());
				
				
				Document document = XmlCommonsUtil.stringToDocument(c.getValorXml());
				List<Node> nodoList = XmlCommonsUtil.aplicarXpath(document, "//impuestos/impuesto");
				if(nodoList==null || nodoList.isEmpty()) {
					nodoList = XmlCommonsUtil.aplicarXpath(document, "//docsSustento/docSustento");
				}
				List<String> tipDocAsociado = new ArrayList<>();
				List<String> numDocAsociado = new ArrayList<>();
				for (Node node : nodoList) {
					String  codDocSustento = XmlCommonsUtil.valorXpath(node.asXML(), "//codDocSustento");
					String  numDocSustento = XmlCommonsUtil.valorXpath(node.asXML(), "//numDocSustento");
					
					if(!tipDocAsociado.contains(codDocSustento)) {
						tipDocAsociado.add(codDocSustento);
					}
					if(!numDocAsociado.contains(numDocSustento)) {
						numDocAsociado.add(numDocSustento);
					}
				}
				
				if(tipDocAsociado.size()==1) {
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(GenTipoDocumentoEnum.getByIdentificador(tipDocAsociado.get(0)));
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(ComprobanteHelper.formatNumDocumento(numDocAsociado.get(0)));
				}else {
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("");
				}
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotalrenta().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(c.getTotalretencion().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				filaDt = fila;
				XmlComprobanteRetencion rt = XmlCommonsUtil.jaxbunmarshall(c.getValorXml(), new XmlComprobanteRetencion(),"UTF-8");
				if(rt.getImpuestoretencionList()==null) {
					rt.setImpuestoretencionList(new ArrayList<>());
				}
				for (XmlImpuestoRetencion i : rt.getImpuestoretencionList()) {
					
					col = 10;
					
					row = sheet.getRow(filaDt);
					if(row==null) {
						row = sheet.createRow(filaDt);
					}
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i.getCodDocSustento()!=null?i.getCodDocSustento():"");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i.getNumDocSustento()!=null?ComprobanteHelper.formatNumDocumento(i.getNumDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i.getFechaEmisionDocSustento()!=null?FechaUtil.formatoFecha(i.getFechaEmisionDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i.getCodigo());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					Retencionimpuesto retencionimpuesto = retencionimpuestoServicio.getByCodSri(i.getCodigo());
					cell.setCellValue(retencionimpuesto!=null?retencionimpuesto.getNombre():"");
					
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(i.getCodigoRetencion());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					Retencionimpuestodet retencionimpuestodet = retencionimpuestodetServicio.getByCodSri(i.getCodigo(), i.getCodigoRetencion());
					cell.setCellValue(retencionimpuestodet!=null?retencionimpuestodet.getNombre():"");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(i.getBaseImponible());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(i.getPorcentajeRetener());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(i.getValorRetenido());
					
					filaDt++;
					
				}
				
				fila += (filaDt - fila) + 1;
			}

			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-RetencionesDetRecibidas_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");

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
			fileUtilApp.setReportDir(AppConfiguracion.getString("dir.base.reporte").concat("compRecibidos/retencion/"));
			
			
			XmlComprobanteRetencion f = XmlCommonsUtil.jaxbunmarshall(cr.getValorXml(), new XmlComprobanteRetencion(),"UTF-8");
			
			if(f.getDocSustentoList()!=null && f.getImpuestoretencionList() == null) {
				// es la version 2 de retenciones cambiar de la v2 a la v1
				comprobanterecibidoServicio.conerterV2ToV1(f);
			}
			
			
			for (XmlImpuestoRetencion c : f.getImpuestoretencionList()) {
				comprobanteUtilServicio.populateImpuestoRetencion(c);
			}
			
			f.setFechaAutorizacion(cr.getFechaAutorizacion());
			f.setNumeroAutorizacion(cr.getNumeroAutorizacion());
			String nombredocumento= f.getInfoTributaria().getRazonSocial() + "-" + f.getInfoTributaria().getSecuencial() + ".pdf";
			String documento="CR-Retencion-V1";
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
			String nombreFisico = FileUtilApp.zipDirectory("Retencion");
			return FileUtilApp.downloadZip(nombreFisico, "Retencion");
			
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
