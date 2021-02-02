/**
 * 
 */
package com.vcw.falecpv.web.ctrl.facturacion;

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
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.FacturaServicio;
import com.vcw.falecpv.core.servicio.ReciboServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.fac.CompFacCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class RecEmitidoCtrl extends BaseCtrl {

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
	private ReciboServicio reciboServicio;
	
	@EJB
	private FacturaServicio facturaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;

	private List<Usuario> usuarioList;
	private Usuario usuarioSelected;	
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQueryList;
	private VentasQuery ventasQuerySelected;
	private TotalesDto totalesDto = new TotalesDto();
	
	/**
	 * 
	 */
	public RecEmitidoCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarUsuario();
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarUsuario()throws DaoException{
		usuarioList = null;
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getIdestablecimiento());
	}
	
	private void consultar()throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
		ventasQueryList = null;
		ventasQueryList = consultaVentaServicio.getFacturasEmitidas(usuarioSelected!=null?usuarioSelected.getIdusuario():null, criterioBusqueda, desde, hasta, establecimientoMain.getIdestablecimiento(),GenTipoDocumentoEnum.RECIBO);
	}
	
	@Override
	public void buscar() {
		try {
			consultar();
			consultarUsuario();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() {
		totalesDto = new TotalesDto();
		
		if(ventasQueryList!=null) {
			totalesDto.setCantidad(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getCantidad().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setSubtotal(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getSubtotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setDescuento(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotaldescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotal(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setPago(BigDecimal.valueOf(ventasQueryList.stream().filter(x->!x.getEstado().equals("ANULADO")).mapToDouble(x->x.getTotalpago().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
		}
		
	}
	
	public StreamedContent getFile() {
		try {
			
			if(ventasQueryList==null || ventasQueryList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RecEmitidas.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
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
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getNombrepantalla());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getCantidad().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getSubtotal().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotal().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotalpago().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getLicitado().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getCambio().doubleValue());
				
				fila++;
			}
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-RecEmitidas-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileDetalle() {
		try {
			
			if(ventasQueryList==null || ventasQueryList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RecEmitidasDet.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
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
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getDetalleList().stream().mapToDouble(x->x.getCantidad().doubleValue()).sum());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotaldescuento().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotaliva().add(v.getTotalice()).doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getTotalconimpuestos().doubleValue());
				
				filaDetalle = fila;
				filaPago = fila;
				for(Detalle d : v.getDetalleList()) {
					
					col = 10;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoprincipal():"");
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(d.getProducto()!=null?d.getProducto().getCodigoauxiliar():"");
					
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
					cell.setCellValue(d.getValoriva().add(d.getValorice()).doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getPreciototal().doubleValue());
					
					filaDetalle++;
					
				}
				
				for (Pago p : v.getPagoList()) {
					col = 19;
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-RecEmitidasDet-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(ventasQuerySelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE RECIBO SELECCIONADO.");
				return;
			}
			
			String analizarEstado = reciboServicio.analizarEstado(ventasQuerySelected.getIdcabecera(), establecimientoMain.getIdestablecimiento(), "ANULAR");
			if(analizarEstado!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analizarEstado);
				return;
			}
			
			reciboServicio.anularRecibo(ventasQuerySelected.getIdcabecera(),AppJsfUtil.getUsuario().getIdusuario());
			
			consultar();
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String editarRecibo(String idFactura) {
		try {
			
			CompFacCtrl compFacCtrl = (CompFacCtrl) AppJsfUtil.getManagedBean("compFacCtrl");
			compFacCtrl.setEstablecimientoMain(this.establecimientoMain);
			String editar = compFacCtrl.editar(ventasQuerySelected.getIdcabecera());
			
			if(editar==null) {
				return "./factura.jsf?faces-redirect=true";
			}
			
			AppJsfUtil.addErrorMessage("formMain","ERROR",editar);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
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
}
