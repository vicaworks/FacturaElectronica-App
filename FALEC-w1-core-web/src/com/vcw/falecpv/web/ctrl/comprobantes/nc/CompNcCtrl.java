/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.NotaCreditoServicio;
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
public class CompNcCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1948894985826004161L;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private NotaCreditoServicio notaCreditoServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> notaCreditoList;
	private Cabecera notaCreditoSelected;
	
	/**
	 * 
	 */
	public CompNcCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			criterioBusqueda = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		notaCreditoList = null;
		notaCreditoList = notaCreditoServicio.getByCriteria(desde, hasta, criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento(),estado);
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
	
	@Override
	public void eliminar() {
		try {
			
			
			String analizar = notaCreditoServicio.analizarEstado(notaCreditoSelected.getIdcabecera(), notaCreditoSelected.getEstablecimiento().getIdestablecimiento(), "ANULAR");
			
			if(analizar!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analizar);
				return;
			}
			
			cabeceraServicio.anularById(notaCreditoSelected.getIdcabecera());
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void editar() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaNotaCredito() {
		try {
			
			//retencionFormCtrl.nuevaRetencionDispacher();
			
			NotaCreditoCtrl notaCreditoCtrl = (NotaCreditoCtrl) AppJsfUtil.getManagedBean("notaCreditoCtrl");
			notaCreditoCtrl.setCallModule("NOTACREDITO");
			notaCreditoCtrl.nuevaNotaCredito();
			
			
			return "./notacredito_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public StreamedContent getFileNCDetalle() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotCreditoDet.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
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
			for (Cabecera lc : notaCreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());

				filaDetalle = fila;
				filaPago = fila;
				for (Detalle d : lc.getDetalleList()) {
					
					col=14;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoprincipal():"");
					
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
				for (Pago p : lc.getPagoList()) {
					col = 22;
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotCreditoDet-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileNC() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotCredito.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 9;
			for (Cabecera lc : notaCreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());

				fila++;
				
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotCredito-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
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
	 * @return the notaCreditoList
	 */
	public List<Cabecera> getNotaCreditoList() {
		return notaCreditoList;
	}

	/**
	 * @param notaCreditoList the notaCreditoList to set
	 */
	public void setNotaCreditoList(List<Cabecera> notaCreditoList) {
		this.notaCreditoList = notaCreditoList;
	}

	/**
	 * @return the notaCreditoSelected
	 */
	public Cabecera getNotaCreditoSelected() {
		return notaCreditoSelected;
	}

	/**
	 * @param notaCreditoSelected the notaCreditoSelected to set
	 */
	public void setNotaCreditoSelected(Cabecera notaCreditoSelected) {
		this.notaCreditoSelected = notaCreditoSelected;
	}

}
