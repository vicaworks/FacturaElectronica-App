/**
 * 
 */
package com.vcw.falecpv.web.ctrl.facturacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.FacturaServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
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
public class PunVentaEmitidoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -717803422305069540L;

	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private FabricanteServicio fabricanteServicio;
	
	@EJB
	private CategoriaServicio categoriaServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private FacturaServicio facturaServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;

	private List<Usuario> usuarioList;
	private Usuario usuarioSelected;	
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQueryList;
	private TotalesDto totalesDto = new TotalesDto();
	private VentasQuery ventasQuerySelected;
	private boolean seleccion = false;
	
	/**
	 * 
	 */
	public PunVentaEmitidoCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			setEstado("TODOS_NO_ANULADOS");
			establecimientoFacade(establecimientoServicio, false);
			consultarUsuario();
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, 0);
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultarUsuario()throws DaoException{
		usuarioList = null;
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getIdestablecimiento());
	}
	
	public void consultar()throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
		seleccion = false;
		ventasQueryList = null;
		ventasQueryList = consultaVentaServicio.getFacturasEmitidas(
				populateEstadoList(),
				usuarioSelected!=null?usuarioSelected.getIdusuario():null, 
						criterioBusqueda, 
						desde, 
						hasta, 
						establecimientoMain.getIdestablecimiento(),
						GenTipoDocumentoEnum.FACTURA);
	}
	
	public List<String> populateEstadoList() {
		if (getEstado() == null ) return null;
		List<String> lista = new ArrayList<>();
		switch (getEstado()) {
		case "VALIDOS":
			lista.add("BORRADOR");
			lista.add("PENDIENTE");
			lista.add("RECIBIDO_SRI");
			lista.add("AUTORIZADO");
			
			break;
		case "ERROR" :
			lista.add("RECHAZADO_SRI");
			lista.add("ERROR_SRI");
			lista.add("ERROR");
			break;
		case "ANULADO" :
			lista.add("ANULADO");
			break;
		case "BORRADOR" :
			lista.add("BORRADOR");
			break;
		case "TODOS_NO_ANULADOS" :
			lista.add("BORRADOR");
			lista.add("PENDIENTE");
			lista.add("RECIBIDO_SRI");
			lista.add("AUTORIZADO");
			lista.add("RECHAZADO_SRI");
			lista.add("ERROR_SRI");
			lista.add("ERROR");
			break;
		default:
			lista = null;
			break;
		}
		return lista;
	}
	
	@Override
	public void buscar() {
		try {
			consultar();
			consultarUsuario();
			totalizar();
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
		
		if(ventasQueryList!=null) {
			totalesDto.setCantidad(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getCantidad().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setSubtotal(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setDescuento(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaldescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotalsinimpuestos(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalsinimpuestos().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIva(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getIva().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIce(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getIce().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotal(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setRetencion(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getValorretenido().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setApagar(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getValorapagar().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setPago(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalpago().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
		}
		
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(ventasQueryList==null || ventasQueryList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-FacEmitidas.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(usuarioSelected!=null?usuarioSelected.getNombrepantalla():"TODOS");
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			int fila = 10;
			for (VentasQuery v : ventasQueryList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getNombrepantalla());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getIdguiaremision()==null?"N":"S");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getEnvioemail()==0?"N":"S");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCantidad().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getSubtotal().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getIva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getIce().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotal().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getValorretenido().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalpago().doubleValue());
				
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-FacEmitidas-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
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
			
			if(ventasQueryList==null || ventasQueryList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-FacEmitidasDetalle.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(usuarioSelected!=null?usuarioSelected.getNombrepantalla():"TODOS");
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			List<Cabecera> cabeceraList = facturaServicio.getByIdList(ventasQueryList.stream().map(x->x.getIdcabecera()).collect(Collectors.toList()));
			
			int filaDetalle = 11;
			int filaPago = 11;
			int fila = 11;
			for (Cabecera v : cabeceraList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getDetalleList().stream().mapToDouble(x->x.getCantidad().doubleValue()).sum());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalsinimpuestos().add(v.getTotaldescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotaliva().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalice().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getTotalconimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getValorretenido().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getValorapagar().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getPagoList().stream().mapToDouble(x->x.getTotal().doubleValue()).sum());
				
				filaDetalle = fila;
				filaPago = fila;
				for(Detalle d : v.getDetalleList()) {
					
					col = 15;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoprincipal():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoauxiliar():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getDescripcion());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getCantidad().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getPreciounitario().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellValue(d.getPreciototalsinimpuesto().add(d.getDescuento()).setScale(2, RoundingMode.HALF_UP).doubleValue());
					
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
				
				for (Pago p : v.getPagoList()) {
					col = 26;
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
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-FacEmitidasDetalle-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return null;
	}
	
	@Override
	public void eliminar() {
		
	}
	
	public void enviarSri() {
		try {
			Cabecera c = cabeceraServicio.consultarByPk(ventasQuerySelected.getIdcabecera());
			c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(c);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void changeSeleccion() {
		try {
			
			if(ventasQueryList!=null) {
				for (VentasQuery v : ventasQueryList) {
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
			
			if(ventasQueryList==null || ventasQueryList.stream().filter(x->x.isSeleccion()).count()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe comprobantes seleccionados", 
						Message.ERROR);
				return;
			}
			
			for (VentasQuery ventasQuery : ventasQueryList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
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
			
			if(ventasQueryList==null || ventasQueryList.stream().filter(x->x.isSeleccion()).count()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen comprobantes seleccionados", 
						Message.ERROR);
				return;
			}
			
			for (VentasQuery ventasQuery : ventasQueryList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				if(ventasQuery.getEstado().equals(ComprobanteEstadoEnum.AUTORIZADO.toString())) {
					
					emailComprobanteServicio.enviarComprobanteFacade(null, null, null, ventasQuery.getIdcabecera(), null, null, null, null,true);
					
				}
				
			}
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					msg.getString("mensaje.emailenviados"), 
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
	 * @return the usuarioList
	 */
	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	/**
	 * @param usuarioList the usuarioList to set
	 */
	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	/**
	 * @return the usuarioSelected
	 */
	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}

	/**
	 * @param usuarioSelected the usuarioSelected to set
	 */
	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
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
	 * @return the ventasQueryList
	 */
	public List<VentasQuery> getVentasQueryList() {
		return ventasQueryList;
	}

	/**
	 * @param ventasQueryList the ventasQueryList to set
	 */
	public void setVentasQueryList(List<VentasQuery> ventasQueryList) {
		this.ventasQueryList = ventasQueryList;
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

	/**
	 * @return the ventasQuerySelected
	 */
	public VentasQuery getVentasQuerySelected() {
		return ventasQuerySelected;
	}

	/**
	 * @param ventasQuerySelected the ventasQuerySelected to set
	 */
	public void setVentasQuerySelected(VentasQuery ventasQuerySelected) {
		this.ventasQuerySelected = ventasQuerySelected;
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

}