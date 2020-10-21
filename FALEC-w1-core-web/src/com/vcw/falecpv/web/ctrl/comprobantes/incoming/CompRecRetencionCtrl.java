/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import com.vcw.falecpv.core.servicio.RetencionimpuestoServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestodetServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
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

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	public CompRecRetencionCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			comprobanteRecibidoList = null;
			comprobanteRecibidoSeleccionList = null;
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
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.RETENCION, desde, hasta, criterioBusqueda);
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
				
				
				Document document = XmlCommonsUtil.stringToDocument(c.getValorXml());
				List<Node> nodoList = XmlCommonsUtil.aplicarXpath(document, "//impuestos/impuesto");
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
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(GenTipoDocumentoEnum.getByIdentificador(tipDocAsociado.get(0)));
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(ComprobanteHelper.formatNumDocumento(numDocAsociado.get(0)));
				}else {
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue("");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue("");
				}
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalrenta().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalretencion().doubleValue());
				
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
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-RetencionesRecibidas_" + AppJsfUtil.getEstablecimiento().getNombrecomercial()+".xlsx");

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
				
				
				Document document = XmlCommonsUtil.stringToDocument(c.getValorXml());
				List<Node> nodoList = XmlCommonsUtil.aplicarXpath(document, "//impuestos/impuesto");
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
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(GenTipoDocumentoEnum.getByIdentificador(tipDocAsociado.get(0)));
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(ComprobanteHelper.formatNumDocumento(numDocAsociado.get(0)));
				}else {
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue("");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue("");
				}
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalrenta().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(c.getTotalretencion().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getNumeroAutorizacion());
				
				cell = row.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(c.getClaveAcceso());
				
				filaDt = fila;
				XmlComprobanteRetencion rt = XmlCommonsUtil.jaxbunmarshall(c.getValorXml(), new XmlComprobanteRetencion());
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
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(i.getCodDocSustento()!=null?i.getCodDocSustento():"");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(i.getNumDocSustento()!=null?ComprobanteHelper.formatNumDocumento(i.getNumDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(i.getFechaEmisionDocSustento()!=null?FechaUtil.formatoFecha(i.getFechaEmisionDocSustento()):"");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(i.getCodigo());
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					Retencionimpuesto retencionimpuesto = retencionimpuestoServicio.getByCodSri(i.getCodigo());
					cell.setCellValue(retencionimpuesto!=null?retencionimpuesto.getNombre():"");
					
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(i.getCodigoRetencion());
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					Retencionimpuestodet retencionimpuestodet = retencionimpuestodetServicio.getByCodSri(i.getCodigo(), i.getCodigoRetencion());
					cell.setCellValue(retencionimpuestodet!=null?retencionimpuestodet.getNombre():"");
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(i.getBaseImponible());
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(i.getPorcentajeRetener());
					
					cell = row.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
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
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-RetencionesDetRecibidas_" + AppJsfUtil.getEstablecimiento().getNombrecomercial()+".xlsx");

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
