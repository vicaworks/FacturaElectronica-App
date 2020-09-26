/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.CotizacionServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CotizacionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4680408942516741116L;
	
	@EJB
	private CotizacionServicio cotizacionServicio;
	
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private String estado;
	private String comprobanteRender = "INICIO";
	private List<Cabecera> proformaList;
	private Cabecera proformaSelected;
	private BigDecimal totalCotizacion = BigDecimal.ZERO;
	private BigDecimal totalArchivados = BigDecimal.ZERO;
	private BigDecimal totalfacturados = BigDecimal.ZERO;
	private BigDecimal totalSeguimiento = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public CotizacionCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			estado = "SEGUIMIENTO";
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		proformaList = null;
		proformaList = cotizacionServicio.getByCriteria(desde, hasta, criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento(), estado);
		totalizar();
	}
	
	private void totalizar() {
		totalCotizacion = BigDecimal.ZERO;
		totalArchivados = BigDecimal.ZERO;
		totalfacturados = BigDecimal.ZERO;
		totalSeguimiento = BigDecimal.ZERO;
		if(proformaList!=null) {
			totalCotizacion = BigDecimal.valueOf(proformaList.size());
			totalArchivados = BigDecimal.valueOf(proformaList.stream().filter(x->x.getEstado().equals("ARCHIVADO")).count());
			totalSeguimiento = BigDecimal.valueOf(proformaList.stream().filter(x->x.getEstado().equals("SEGUIMIENTO")).count());
			totalfacturados = BigDecimal.valueOf(proformaList.stream().filter(x->x.getEstado().equals("FACTURADO")).count());
		}
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(proformaSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			cotizacionServicio.archivarCotizacion(proformaSelected.getIdcabecera());
			proformaSelected = null;
			CotizacionFormCtrl cotizacionFormCtrl = (CotizacionFormCtrl)AppJsfUtil.getManagedBean("cotizacionFormCtrl");
			cotizacionFormCtrl.nuevoFromMain();
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void switchProforma(String proforma) {
		comprobanteRender = proforma;
		System.out.println(proforma);
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(proformaList==null || proformaList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CotEmitidas.xlsx");
			
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
			rowCliente.createCell(1).setCellValue(estado);
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			int fila = 10;
			for (Cabecera v : proformaList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaVencimiento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getResumen());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getEnvioemail()==0?"N":"S");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalsinimpuestos().add(v.getTotaldescuento()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalconimpuestos().doubleValue());
				
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-CotEmitidas-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(proformaList==null || proformaList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CotEmitidasDetalle.xlsx");
			
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
			rowCliente.createCell(1).setCellValue(estado);
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			int filaDetalle = 11;
			int filaPago = 11;
			int fila = 11;
			
			for (Cabecera v : proformaList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaVencimiento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getResumen());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(v.getEnvioemail()==0?"N":"S");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalsinimpuestos().add(v.getTotaldescuento()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(v.getTotalconimpuestos().doubleValue());
				
				filaDetalle = fila;
				filaPago = fila;
				for(Detalle d : v.getDetalleList()) {
					
					col = 14;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoprincipal():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoauxiliar():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getDescripcion());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getCantidad().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPreciounitario().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPreciototalsinimpuesto().add(d.getDescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getDescuento().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPreciototalsinimpuesto().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getValoriva().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getValorice().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(d.getPreciototal().doubleValue());
					
					filaDetalle++;
					
				}
				if(v.getPagoList()!=null) {
					
					for (Pago p : v.getPagoList()) {
						col = 26;
						rowCliente = sheet.getRow(filaPago);
						if(rowCliente==null) {
							rowCliente = sheet.createRow(filaPago);
						}
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(p.getTipopago().getNombre());
						
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(p.getTotal().doubleValue());
						
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(p.getValorentrega().doubleValue());
						
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(p.getCambio().doubleValue());
						
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(p.getNumerodocumento());
						
						cell = rowCliente.createCell(col++);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(p.getNombrebanco());
						
						filaPago++;
						
					}
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-CotEmitidasDetalle-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the comprobanteRender
	 */
	public String getComprobanteRender() {
		return comprobanteRender;
	}

	/**
	 * @param comprobanteRender the comprobanteRender to set
	 */
	public void setComprobanteRender(String comprobanteRender) {
		this.comprobanteRender = comprobanteRender;
	}

	/**
	 * @return the proformaList
	 */
	public List<Cabecera> getProformaList() {
		return proformaList;
	}

	/**
	 * @param proformaList the proformaList to set
	 */
	public void setProformaList(List<Cabecera> proformaList) {
		this.proformaList = proformaList;
	}

	/**
	 * @return the totalCotizacion
	 */
	public BigDecimal getTotalCotizacion() {
		return totalCotizacion;
	}

	/**
	 * @param totalCotizacion the totalCotizacion to set
	 */
	public void setTotalCotizacion(BigDecimal totalCotizacion) {
		this.totalCotizacion = totalCotizacion;
	}

	/**
	 * @return the totalArchivados
	 */
	public BigDecimal getTotalArchivados() {
		return totalArchivados;
	}

	/**
	 * @param totalArchivados the totalArchivados to set
	 */
	public void setTotalArchivados(BigDecimal totalArchivados) {
		this.totalArchivados = totalArchivados;
	}

	/**
	 * @return the totalfacturados
	 */
	public BigDecimal getTotalfacturados() {
		return totalfacturados;
	}

	/**
	 * @param totalfacturados the totalfacturados to set
	 */
	public void setTotalfacturados(BigDecimal totalfacturados) {
		this.totalfacturados = totalfacturados;
	}

	/**
	 * @return the totalSeguimiento
	 */
	public BigDecimal getTotalSeguimiento() {
		return totalSeguimiento;
	}

	/**
	 * @param totalSeguimiento the totalSeguimiento to set
	 */
	public void setTotalSeguimiento(BigDecimal totalSeguimiento) {
		this.totalSeguimiento = totalSeguimiento;
	}

	/**
	 * @return the proformaSelected
	 */
	public Cabecera getProformaSelected() {
		return proformaSelected;
	}

	/**
	 * @param proformaSelected the proformaSelected to set
	 */
	public void setProformaSelected(Cabecera proformaSelected) {
		this.proformaSelected = proformaSelected;
	}

}
