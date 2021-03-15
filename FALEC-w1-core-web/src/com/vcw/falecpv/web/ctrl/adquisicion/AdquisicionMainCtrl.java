/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

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
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.AdquisiciondetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
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
public class AdquisicionMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5980287108670536653L;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private AdquisiciondetalleServicio adquisiciondetalleServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Adquisicion> adquisicionList;
	
	private AdquisicionFrmCtrl adquisicionFrmCtrl;

	public AdquisicionMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -90);
			criterioBusqueda = null;
			adquisicionFrmCtrl = (AdquisicionFrmCtrl) AppJsfUtil.getManagedBean("adquisicionFrmCtrl");
			consultarAdquisiciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void consultarAdquisiciones()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:adquisicionDT");
		adquisicionList = null;
		adquisicionList = adquisicionServicio.getByDateCriteria(establecimientoMain.getIdestablecimiento(), desde, hasta, criterioBusqueda,estado);
	}
	
	@Override
	public void refrescar() {
		try {
			consultarAdquisiciones();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public String nuevaCompra() {
		try {
			
			adquisicionFrmCtrl.nuevaAdquisicion();
			
			return "./adquisicion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			return null;
		}
		
	}
	
	public String editarCompra(String idadquisicion) {
		try {
			
			adquisicionFrmCtrl.editarAdquisicion(idadquisicion);
			
			return "./adquisicion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			return null;
		}
		
	}
	
	public void anular(String idadquisicion) {
		try {
			
			Adquisicion a = adquisicionServicio.consultarByPk(idadquisicion);
			if(a==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE LA FACTURA.");
				consultarAdquisiciones();
				return;
			}
			
			String analisisEstado = adquisicionServicio.analizarEstado(a.getIdadquisicion(), a.getEstablecimiento().getIdestablecimiento(), "ANULAR"); 
			
			if(analisisEstado!=null) {
				AppJsfUtil.addErrorMessage("formMain", analisisEstado);
				consultarAdquisiciones();
				return;
				
			}
			
			adquisicionServicio.anularCompra(idadquisicion, AppJsfUtil.getUsuario().getIdusuario(), false);
			consultarAdquisiciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileComprasDet() {
		try {
			
			if(adquisicionList==null || adquisicionList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-ComprasDet.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos de la cabecera
			Row row = sheet.getRow(3);
			Cell cell = row.createCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
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
			int filaPago = 10;
			int filaDt = 10;
			
			for (Adquisicion adq : adquisicionList) {
				
				row = sheet.createRow(fila);
				
				int col =0;
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(adq.getFecha()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getEstado());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getTipocomprobante().getComprobante());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(adq.getNumfactura()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getCliente().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getCliente().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getEsGastoBol()?"S":"N");
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getSubtotal().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotaldescuento().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalice().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalfactura().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalretencion().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalpagar().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalPagoSum().doubleValue());
				
				// detalle de pago
				filaDt = fila + 1;
				filaPago = fila + 1;
				for (Adquisiciondetalle ad : adq.getAdquisiciondetalleList()) {
					
					col = 15;
					
					row = sheet.getRow(filaDt);
					if(row==null) {
						row = sheet.createRow(filaDt);
					}
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(ad.getProducto()!=null?ad.getProducto().getCodigoprincipal():"");
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(ad.getDescripcion());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getCantidad().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getPreciounitario().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getDescuento().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getValorice().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getValoriva().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ad.getPreciototal().doubleValue());
					
					filaDt++;
					
				}
				
				for (Pago p : adq.getPagoList()) {
					col = 23;
					row = sheet.getRow(filaPago);
					if(row==null) {
						row = sheet.createRow(filaPago);
					}
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getTipopago().getNombre());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getTotal().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getValorentrega().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getCambio().doubleValue());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(FechaUtil.formatoFecha(p.getFechapago()));
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getNumerodocumento());
					
					cell = row.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getNombrebanco());
					
					filaPago++;
					
				}
				
				if(filaDt >= filaPago) {
					fila += (filaDt - fila);
				}else {
					fila += (filaPago - fila);
				}
				
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-CompraDet_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial()+".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileCompras() {
		try {
			
			if(adquisicionList==null || adquisicionList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-Compras.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos de la cabecera
			Row row = sheet.getRow(3);
			Cell cell = row.createCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
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
			
			for (Adquisicion adq : adquisicionList) {
				
				row = sheet.createRow(fila);
				
				int col =0;
				
				// datos de la cabecera
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(adq.getFecha()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getEstado());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getTipocomprobante().getComprobante());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(adq.getNumfactura()));
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getCliente().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getCliente().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(adq.getEsGastoBol()?"S":"N");
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getSubtotal().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotaldescuento().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalice().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotaliva().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalfactura().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalretencion().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalpagar().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(adq.getTotalPagoSum().doubleValue());
				
				fila++;
				
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-Compra_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial()+".xlsx");
			
			
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
	 * @return the adquisicionList
	 */
	public List<Adquisicion> getAdquisicionList() {
		return adquisicionList;
	}

	/**
	 * @param adquisicionList the adquisicionList to set
	 */
	public void setAdquisicionList(List<Adquisicion> adquisicionList) {
		this.adquisicionList = adquisicionList;
	}

	/**
	 * @return the adquisicionFrmCtrl
	 */
	public AdquisicionFrmCtrl getAdquisicionFrmCtrl() {
		return adquisicionFrmCtrl;
	}

	/**
	 * @param adquisicionFrmCtrl the adquisicionFrmCtrl to set
	 */
	public void setAdquisicionFrmCtrl(AdquisicionFrmCtrl adquisicionFrmCtrl) {
		this.adquisicionFrmCtrl = adquisicionFrmCtrl;
	}

}
