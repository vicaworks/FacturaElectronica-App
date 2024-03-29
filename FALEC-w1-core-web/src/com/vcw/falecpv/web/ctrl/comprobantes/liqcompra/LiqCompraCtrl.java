/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.liqcompra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.LiqCompraServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class LiqCompraCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4702498949971426306L;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private LiqCompraServicio liqCompraServicio;
	
	@EJB
	private DetalleServicio detalleServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> liqCompraList;
	private Cabecera liqCompraSelected;
	private boolean seleccion = false;
	private TotalesDto totalesDto = new TotalesDto();
	
	/**
	 * 
	 */
	public LiqCompraCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -60);
			criterioBusqueda = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		seleccion = false;
		AppJsfUtil.limpiarFiltrosDataTable("formMain:liqCompraDT");
		liqCompraList = null;
		liqCompraList = liqCompraServicio.getByLiqCompraCriteria(desde, hasta, criterioBusqueda, establecimientoMain.getIdestablecimiento(), estado);
		totalizar();
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
	
	private void totalizar() {
		totalesDto = new TotalesDto();
		
		if(liqCompraList!=null) {			
			totalesDto.setSubtotal(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setDescuento(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaldescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotalsinimpuestos(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIva(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaliva().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIce(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalice().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotal(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setRetencion(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getValorretenido().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setApagar(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getValorapagar().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setPago(BigDecimal.valueOf(liqCompraList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalPagadoSum().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
		}
		
	}

	@Override
	public void eliminar() {
		try {
			
			if(liqCompraSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			String analisis = cabeceraServicio.analizarEstadoComprobante(liqCompraSelected.getIdcabecera(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraServicio.anularById(liqCompraSelected.getIdcabecera());
			liqCompraSelected = null;
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaLiqCompra() {
		try {
			
			LiqCompraFormCtrl liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
			liqCompraFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			liqCompraFormCtrl.nuevaLiqCompra();
			
			return "./liqCompraForm.jsf?faces-redirect=true";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public String editarLiqCompra(String idLiqCompra) {
		try {
			
			LiqCompraFormCtrl liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
			liqCompraFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			String editar = liqCompraFormCtrl.editar(idLiqCompra);
			
			if(editar==null) {
				return "./liqCompraForm.jsf?faces-redirect=true";
			}
			
			AppJsfUtil.addErrorMessage("formMain","ERROR",editar);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public StreamedContent getFileLCDetalle() {
		try {
			
			if(liqCompraList==null || liqCompraList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-LiqComprasDet.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 10;
			int filaDetalle = 10;
			int filaPago = 10;
			for (Cabecera lc : liqCompraList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getValorretenido().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalPagadoSum().doubleValue());
				
				// detalle de compra
				filaDetalle = fila;
				filaPago = fila;
				
				for (Detalle d : lc.getDetalleList()) {
					col = 13;
					
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(d.getDescripcion());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getCantidad().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getPreciounitario().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getDescuento().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getPreciototalsinimpuesto().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getValoriva().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getPreciototal().doubleValue());
					
					filaDetalle++;
					
				}
				
				for (Pago p : lc.getPagoList()) {
					col = 20;
					rowCliente = sheet.getRow(filaPago);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaPago);
					}
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getTipopago().getNombre());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getValorentrega().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getCambio().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getNumerodocumento());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-LiqComprasDet-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileLC() {
		try {
			
			if(liqCompraList==null || liqCompraList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-LiqCompras.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 9;
			for (Cabecera lc : liqCompraList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getValorretenido().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalPagadoSum().doubleValue());
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-LiqCompras-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void changeSeleccion() {
		try {
			
			if(liqCompraList!=null) {
				for (Cabecera v : liqCompraList) {
					if(!v.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && !v.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
						v.setSeleccion(this.seleccion);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmarEnviar() {
		try {
			
			if(liqCompraList==null || liqCompraList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			
			for (Cabecera ventasQuery : liqCompraList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				Cabecera c = new Cabecera();
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				c.setIdcabecera(ventasQuery.getIdcabecera());
				c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
				sriDispacher.queue_comprobanteSriDispacher(c);
				
			}
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FIRMAR Y ENVIAR COMPROBANTE SRI", msg.getString("mensaje.enviarComprobante"));
	        PrimeFaces.current().dialog().showMessageDynamic(message,true);
			
	        consultar();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void enviarEmail() {
		try {
			
			if(liqCompraList==null || liqCompraList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			for (Cabecera cabecera : liqCompraList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				if(cabecera.getEstado().equals(ComprobanteEstadoEnum.AUTORIZADO.toString())) {
					emailComprobanteServicio.enviarComprobanteFacade(null, null, null, cabecera.getIdcabecera(), null, null, null, null,true);
				}
			}
			AppJsfUtil.addInfoMessage("formMain", "OK", "LOS CORREOS HAN SIDO ENVIADOS CORRECTAMENTE, SOLO LOS COMPROBANTES QUE ESTAN EN ESTADO AUTORIZADO.");
	        consultar();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the liqCompraList
	 */
	public List<Cabecera> getLiqCompraList() {
		return liqCompraList;
	}

	/**
	 * @param liqCompraList the liqCompraList to set
	 */
	public void setLiqCompraList(List<Cabecera> liqCompraList) {
		this.liqCompraList = liqCompraList;
	}

	/**
	 * @return the liqCompraSelected
	 */
	public Cabecera getLiqCompraSelected() {
		return liqCompraSelected;
	}

	/**
	 * @param liqCompraSelected the liqCompraSelected to set
	 */
	public void setLiqCompraSelected(Cabecera liqCompraSelected) {
		this.liqCompraSelected = liqCompraSelected;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the totalesDto
	 */
	public TotalesDto getTotalesDto() {
		return totalesDto;
	}

	/**
	 * @param totalesDto the totalesDto to set
	 */
	public void setTotalesDto(TotalesDto totalesDto) {
		this.totalesDto = totalesDto;
	}
	
	
	

}
