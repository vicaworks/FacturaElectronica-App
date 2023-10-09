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
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.vista.VComprobantespagarcredito;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.VComprobantespagarcreditoServicio;
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
public class CuentaPagarCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515265342249527817L;

	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private VComprobantespagarcreditoServicio vComprobantespagarcreditoServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private List<VComprobantespagarcredito> vComprobantespagarcreditoList;
	private VComprobantespagarcredito vComprobantespagarcreditoSelected;
	private String idTipocomprobante = "T";
	private BigDecimal totalPagar;
	private BigDecimal totalVencido;
	private String criterioBusqueda;
	private String criterioCliente;
	private Date desde;
	private Date hasta;
	
	public CuentaPagarCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			totalPagar = BigDecimal.ZERO;
			totalVencido = BigDecimal.ZERO;
			idTipocomprobante = "T";
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -31);
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultar()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
		vComprobantespagarcreditoList = null;
		vComprobantespagarcreditoList = vComprobantespagarcreditoServicio.getByCuentasPagar(establecimientoMain.getIdestablecimiento(),desde, hasta, idTipocomprobante,criterioBusqueda,criterioCliente);
		totalizar();
	}
	
	@Override
	public void buscar() {
		try {
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public Boolean isFechaVencida(Date fecha) {
		try {
			if(fecha==null) return false;
			
			return (FechaUtil.comparaFechas(fecha, new Date())<0);
			
		} catch (ParseException e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return false;
	}
	
	public void totalizar() {
		
		if(vComprobantespagarcreditoList!=null) {
			
			totalPagar = BigDecimal.valueOf(vComprobantespagarcreditoList.stream().mapToDouble(x->x.getTotalpago().doubleValue()).sum() +
					vComprobantespagarcreditoList.stream().mapToDouble(x->x.getAbono().doubleValue()).sum()*(-1)).setScale(2, RoundingMode.HALF_UP);
			
			List<VComprobantespagarcredito> vencimientoList =  vComprobantespagarcreditoList.stream().filter(x->isFechaVencida(x.getFechapago())).collect(Collectors.toList());
			totalVencido = BigDecimal.ZERO;
			for (VComprobantespagarcredito v : vencimientoList) {
				v.getPagoList().sort(Comparator.comparing(Pago::getFechapago));
				for (Pago p : v.getPagoList()) {
					if(p.getValorpago().doubleValue()<p.getTotal().doubleValue() && isFechaVencida(p.getFechapago())) {
						totalVencido = totalVencido.add(p.getTotal().add(p.getValorpago().negate())).setScale(2, RoundingMode.HALF_UP);
					}
				}
			}
			
		}
		
	}
	
	public void seleccionarOpcion() {
		try {
			
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
			
			if(vComprobantespagarcreditoList==null || vComprobantespagarcreditoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CuentaPagar.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(idTipocomprobante==null?"TODOS":"");
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(totalPagar.doubleValue());
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(totalVencido.doubleValue());
			
			int fila = 9;
			
			for (VComprobantespagarcredito p : vComprobantespagarcreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(p.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(p.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalretencion().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalpago().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getAbono().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalpago().add(p.getAbono().negate()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-CuentaPagar-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
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
			
			if(vComprobantespagarcreditoList==null || vComprobantespagarcreditoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CuentaPagarDet.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(idTipocomprobante==null?"TODOS":"");
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(totalPagar.doubleValue());
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(totalVencido.doubleValue());
			
			int fila = 10;
			int filaCredito = 10;
			int filaOtros = 10;
			
			for (VComprobantespagarcredito p : vComprobantespagarcreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(p.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(p.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalretencion().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalpago().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getAbono().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(p.getTotalpago().add(p.getAbono().negate()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
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
					cell.setCellValue(FechaUtil.formatoFecha(pa.getFechapago()));
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getPlazo().intValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getValorpago().doubleValue());
					
					cell = rowCliente.createCell(col++);
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
					cell.setCellValue(pa.getTipopago().getNombre());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getValorentrega().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getCambio().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(pa.getNumerodocumento());
					
					cell = rowCliente.createCell(col++);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-CuentaPagarDet-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
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
	 * @return the vComprobantespagarcreditoList
	 */
	public List<VComprobantespagarcredito> getvComprobantespagarcreditoList() {
		return vComprobantespagarcreditoList;
	}

	/**
	 * @param vComprobantespagarcreditoList the vComprobantespagarcreditoList to set
	 */
	public void setvComprobantespagarcreditoList(List<VComprobantespagarcredito> vComprobantespagarcreditoList) {
		this.vComprobantespagarcreditoList = vComprobantespagarcreditoList;
	}

	/**
	 * @return the vComprobantespagarcreditoSelected
	 */
	public VComprobantespagarcredito getvComprobantespagarcreditoSelected() {
		return vComprobantespagarcreditoSelected;
	}

	/**
	 * @param vComprobantespagarcreditoSelected the vComprobantespagarcreditoSelected to set
	 */
	public void setvComprobantespagarcreditoSelected(VComprobantespagarcredito vComprobantespagarcreditoSelected) {
		this.vComprobantespagarcreditoSelected = vComprobantespagarcreditoSelected;
	}

	/**
	 * @return the idTipocomprobante
	 */
	public String getIdTipocomprobante() {
		return idTipocomprobante;
	}

	/**
	 * @param idTipocomprobante the idTipocomprobante to set
	 */
	public void setIdTipocomprobante(String idTipocomprobante) {
		this.idTipocomprobante = idTipocomprobante;
	}

	/**
	 * @return the totalPagar
	 */
	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	/**
	 * @param totalPagar the totalPagar to set
	 */
	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
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
	 * @return the criterioCliente
	 */
	public String getCriterioCliente() {
		return criterioCliente;
	}

	/**
	 * @param criterioCliente the criterioCliente to set
	 */
	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
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

}
