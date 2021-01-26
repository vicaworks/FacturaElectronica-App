/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
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
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RepRecibAnuCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5541322574433690245L;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private List<Usuario> usuarioList;
	private Usuario usuarioSelected;
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQueryList;
	private TotalesDto totalesDto;
	private RepMainCtrl repMainCtrl;
	
	/**
	 * 
	 */
	public RepRecibAnuCtrl() {
	}
	
	@PostConstruct
	public void init() {
		// el init se paso a initEvent para inicialiuzar la sucursal
		hasta = new Date();
		desde = FechaUtil.agregarMeses(hasta, -1);
	}
	
	public void initEvent(RepMainCtrl repMainCtrl) {
		try {
			this.repMainCtrl = repMainCtrl;
			consultarUsuario();
			
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain5", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarUsuario()throws DaoException{
		usuarioList = null;
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(
					EstadoRegistroEnum.ACTIVO, 
					establecimientoMain!=null?establecimientoMain.getIdestablecimiento():null,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	private void consultar()throws DaoException {
		ventasQueryList = null;
		ventasQueryList = consultaVentaServicio.getFacturasEmitidas(usuarioSelected,
				ComprobanteEstadoEnum.ANULADO, 
				criterioBusqueda, 
				desde, 
				hasta, 
				establecimientoMain!=null?establecimientoMain.getIdestablecimiento():null,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
				GenTipoDocumentoEnum.RECIBO);
	}
	
	@Override
	public void buscar() {
		try {
			consultar();
			totalizar();
			consultarUsuario();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain5", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() {
		totalesDto = new TotalesDto();
		
		if(ventasQueryList!=null) {
			totalesDto.setCantidad(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getCantidad().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setSubtotal(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getSubtotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setDescuento(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getTotaldescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotal(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setPago(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getTotalpago().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
		}
	}
	
	public StreamedContent getFile() {
		try {
			if(ventasQueryList==null || ventasQueryList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-RecAnulados.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(establecimientoMain.getEmpresa().getRazonsocial());
			
			rowCliente = sheet.getRow(3);
			rowCliente.createCell(6).setCellValue(establecimientoMain!=null?establecimientoMain.getNombrecomercial():"TODOS");
			
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
				cell.setCellValue(TextoUtil.leftPadTexto(v.getCodigoestablecimiento(), 3, "0"));
				
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
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-RecAnulados-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain5", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the repMainCtrl
	 */
	public RepMainCtrl getRepMainCtrl() {
		return repMainCtrl;
	}

	/**
	 * @param repMainCtrl the repMainCtrl to set
	 */
	public void setRepMainCtrl(RepMainCtrl repMainCtrl) {
		this.repMainCtrl = repMainCtrl;
	}

}
