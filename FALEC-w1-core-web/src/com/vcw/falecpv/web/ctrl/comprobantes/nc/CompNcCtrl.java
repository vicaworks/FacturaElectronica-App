/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nc;

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
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.NotaCreditoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
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
	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> notaCreditoList;
	private Cabecera notaCreditoSelected;
	private boolean seleccion = false;
	private TotalesDto totalesDto = new TotalesDto();
	
	/**
	 * 
	 */
	public CompNcCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			criterioBusqueda = null;
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
		AppJsfUtil.limpiarFiltrosDataTable("formMain:notaCreditoDT");
		seleccion = false;
		notaCreditoList = null;
		notaCreditoList = notaCreditoServicio.getByCriteria(desde, hasta, criterioBusqueda, establecimientoMain.getIdestablecimiento(),estado);
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
	
	private void totalizar() {
		totalesDto = new TotalesDto();
		
		if(notaCreditoList!=null) {
			totalesDto.setSubtotal(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));			
			totalesDto.setDescuento(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaldescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));			
			totalesDto.setTotalsinimpuestos(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));			
			totalesDto.setIva(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaliva().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIce(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalice().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));			
			totalesDto.setTotal(BigDecimal.valueOf(notaCreditoList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalconimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));		
		
		}
		
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(notaCreditoSelected==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe registro seleccionado", 
						Message.ERROR);
				return;
			}
			
			String analisis = cabeceraServicio.analizarEstadoComprobante(notaCreditoSelected.getIdcabecera(), "ANULAR");
			if(analisis!=null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						analisis, 
						Message.ERROR);
				return;
			}
			
			cabeceraServicio.anularById(notaCreditoSelected.getIdcabecera());
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	public String nuevaNotaCredito() {
		try {
			
			//retencionFormCtrl.nuevaRetencionDispacher();
			
			NotaCreditoCtrl notaCreditoCtrl = (NotaCreditoCtrl) AppJsfUtil.getManagedBean("notaCreditoCtrl");
			notaCreditoCtrl.setEstablecimientoMain(this.establecimientoMain);
			notaCreditoCtrl.setCallModule("NOTACREDITO");
			notaCreditoCtrl.nuevaNotaCredito();
			
			
			return "./notacredito_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
		return null;
	}
	
	public String editarLiqCompra(String idNotaCredito) {
		try {
			
			NotaCreditoCtrl notaCreditoCtrl = (NotaCreditoCtrl) AppJsfUtil.getManagedBean("notaCreditoCtrl");
			notaCreditoCtrl.setEstablecimientoMain(this.establecimientoMain);
			String editar = notaCreditoCtrl.editar(idNotaCredito);
			
			if(editar==null) {
				return "./notacredito_form.jsf?faces-redirect=true";
			}
			
			getMessageCommonCtrl().crearMensaje("Error", 
					editar, 
					Message.ERROR);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
		return null;
	}
	
	
	public StreamedContent getFileNCDetalle() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotCreditoDet.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
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
			for (Cabecera lc : notaCreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
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
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoprincipal():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getDescripcion());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getCantidad().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getPreciounitario().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getDescuento().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getPreciototalsinimpuesto().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getValoriva().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getValorice().doubleValue());
					
					cell = rowCliente.createCell(col++);
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
					cell.setCellValue(p.getTipopago().getNombre());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(p.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(p.getValorentrega().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(p.getCambio().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(p.getNumerodocumento());
					
					cell = rowCliente.createCell(col++);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-NotCreditoDet-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return null;
	}
	
	public StreamedContent getFileNC() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotCredito.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
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
			for (Cabecera lc : notaCreditoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalsinimpuestos().add(lc.getTotaldescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(lc.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-NotCredito-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return null;
	}
	
	public void changeSeleccion() {
		try {
			
			if(notaCreditoList!=null) {
				for (Cabecera v : notaCreditoList) {
					if(!v.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && !v.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
						v.setSeleccion(this.seleccion);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void firmarEnviar() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.stream().filter(x->x.isSeleccion()).count()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen comprobantes seleccionados", 
						Message.ERROR);
				return;
			}
			
			for (Cabecera ventasQuery : notaCreditoList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void enviarEmail() {
		try {
			
			if(notaCreditoList==null || notaCreditoList.stream().filter(x->x.isSeleccion()).count()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen comprobantes seleccionados", 
						Message.ERROR);
				return;
			}
			for (Cabecera cabecera : notaCreditoList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				if(cabecera.getEstado().equals(ComprobanteEstadoEnum.AUTORIZADO.toString())) {
					emailComprobanteServicio.enviarComprobanteFacade(null, null, null, cabecera.getIdcabecera(), null, null, null, null,true);
				}
			}
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Los correos han sido enviados correctamente, solo los comprobantes que están en estado autorizado.", 
					Message.OK);
	        consultar();
	        
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
