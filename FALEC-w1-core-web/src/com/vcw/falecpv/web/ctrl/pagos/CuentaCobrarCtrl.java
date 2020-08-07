/**
 * 
 */
package com.vcw.falecpv.web.ctrl.pagos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.vista.VComprobantescredito;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.VComprobantescreditoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CuentaCobrarCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515265342249527817L;

	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private VComprobantescreditoServicio vComprobantescreditoServicio;
	
	private List<VComprobantescredito> vComprobantescreditoList;
	private VComprobantescredito vComprobantescreditoSelected;
	private List<Tipocomprobante> tipocomprobanteList;
	private Tipocomprobante tipocomprobante;
	private BigDecimal totalCobrar;
	private BigDecimal totalVencido;
	private BigDecimal totalProximo;
	private String criterioBusqueda;
	
	public CuentaCobrarCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			consultarTipoComprobante();
			tipocomprobante = null;
			totalCobrar = BigDecimal.ZERO;
			totalVencido = BigDecimal.ZERO;
			totalProximo = BigDecimal.ZERO;
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
		vComprobantescreditoList = null;
		vComprobantescreditoList = vComprobantescreditoServicio.getByCuentasCobrar(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), tipocomprobante,criterioBusqueda);
		totalizar();
	}
	
	public void consultarTipoComprobante()throws DaoException{
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(TipoComprobanteEnum.CUENTAS_COBRAR));
	}

	@Override
	public void buscar() {
		try {
			consultarTipoComprobante();
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public Boolean isFechaVencida(Date fecha) {
		try {
			if(fecha==null) return false;
			
			return (FechaUtil.comparaFechas(fecha, new Date())<0);
			
		} catch (ParseException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return false;
	}
	
	public void totalizar() {
		
		if(vComprobantescreditoList!=null) {
			
			totalCobrar = BigDecimal.valueOf(vComprobantescreditoList.stream().mapToDouble(x->x.getTotalpago().doubleValue()).sum() +
			vComprobantescreditoList.stream().mapToDouble(x->x.getAbono().doubleValue()).sum()*(-1)).setScale(2, RoundingMode.HALF_UP);
			
			List<VComprobantescredito> vencimientoList =  vComprobantescreditoList.stream().filter(x->isFechaVencida(x.getFechapago())).collect(Collectors.toList());
			totalVencido = BigDecimal.ZERO;
			for (VComprobantescredito v : vencimientoList) {
				v.getPagoList().sort(Comparator.comparing(Pago::getFechapago));
				for (Pago p : v.getPagoList()) {
					if(p.getValorpago().doubleValue()<p.getTotal().doubleValue() && isFechaVencida(p.getFechapago())) {
						totalVencido = totalVencido.add(p.getTotal().add(p.getValorpago().negate())).setScale(2, RoundingMode.HALF_UP);
					}
				}
			}
			
		}
		
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(vComprobantescreditoList==null || vComprobantescreditoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CuentaCobrar.xlsx");
			
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
			rowCliente.createCell(1).setCellValue(tipocomprobante==null?"TODOS":tipocomprobante.getComprobante());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(totalCobrar.doubleValue());
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(totalVencido.doubleValue());
			
			int fila = 9;
			
			for (VComprobantescredito p : vComprobantescreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(p.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(p.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalretencion().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalpago().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getAbono().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalpago().add(p.getAbono().negate()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getFechapago()!=null?FechaUtil.formatoFecha(p.getFechapago()):"");
				
				fila++;
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-CuentaCobrar-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(vComprobantescreditoList==null || vComprobantescreditoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CuentaCobrarDet.xlsx");
			
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
			rowCliente.createCell(1).setCellValue(tipocomprobante==null?"TODOS":tipocomprobante.getComprobante());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(totalCobrar.doubleValue());
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(totalVencido.doubleValue());
			
			int fila = 10;
			int filaCredito = 10;
			int filaOtros = 10;
			
			for (VComprobantescredito p : vComprobantescreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(p.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(p.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalretencion().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalpago().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getAbono().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getTotalpago().add(p.getAbono().negate()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(p.getFechapago()!=null?FechaUtil.formatoFecha(p.getFechapago()):"");
				
				// credito
				filaOtros = fila;
				filaCredito = fila;
				
				p.getPagoList().sort(Comparator.comparing(Pago::getFechapago));
				for (Pago pa : p.getPagoList()) {
					col = 12;
					
					rowCliente = sheet.getRow(filaCredito);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaCredito);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(FechaUtil.formatoFecha(pa.getFechapago()));
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getPlazo().intValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getValorpago().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(pa.getNotas());
					
					filaCredito++;
				}
				
				// otros pagos
				for (Pago pa : p.getPagoOtrosList()) {
					col = 17;
					
					rowCliente = sheet.getRow(filaOtros);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaOtros);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(pa.getTipopago().getNombre());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getValorentrega().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(pa.getCambio().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(pa.getNumerodocumento());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(pa.getNombrebanco());
					
					filaOtros++;
				}
				
				if(filaCredito>filaOtros) {
					fila = filaCredito;
				}else {
					fila = filaOtros;
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-CuentaCobrarDet-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}

	/**
	 * @return the vComprobantescreditoList
	 */
	public List<VComprobantescredito> getvComprobantescreditoList() {
		return vComprobantescreditoList;
	}

	/**
	 * @param vComprobantescreditoList the vComprobantescreditoList to set
	 */
	public void setvComprobantescreditoList(List<VComprobantescredito> vComprobantescreditoList) {
		this.vComprobantescreditoList = vComprobantescreditoList;
	}

	/**
	 * @return the vComprobantescreditoSelected
	 */
	public VComprobantescredito getvComprobantescreditoSelected() {
		return vComprobantescreditoSelected;
	}

	/**
	 * @param vComprobantescreditoSelected the vComprobantescreditoSelected to set
	 */
	public void setvComprobantescreditoSelected(VComprobantescredito vComprobantescreditoSelected) {
		this.vComprobantescreditoSelected = vComprobantescreditoSelected;
	}

	/**
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the tipocomprobanteList
	 */
	public List<Tipocomprobante> getTipocomprobanteList() {
		return tipocomprobanteList;
	}

	/**
	 * @param tipocomprobanteList the tipocomprobanteList to set
	 */
	public void setTipocomprobanteList(List<Tipocomprobante> tipocomprobanteList) {
		this.tipocomprobanteList = tipocomprobanteList;
	}

	/**
	 * @return the totalCobrar
	 */
	public BigDecimal getTotalCobrar() {
		return totalCobrar;
	}

	/**
	 * @param totalCobrar the totalCobrar to set
	 */
	public void setTotalCobrar(BigDecimal totalCobrar) {
		this.totalCobrar = totalCobrar;
	}

	/**
	 * @return the totalVencido
	 */
	public BigDecimal getTotalVencido() {
		return totalVencido;
	}

	/**
	 * @param totalVencido the totalVencido to set
	 */
	public void setTotalVencido(BigDecimal totalVencido) {
		this.totalVencido = totalVencido;
	}

	/**
	 * @return the totalProximo
	 */
	public BigDecimal getTotalProximo() {
		return totalProximo;
	}

	/**
	 * @param totalProximo the totalProximo to set
	 */
	public void setTotalProximo(BigDecimal totalProximo) {
		this.totalProximo = totalProximo;
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
