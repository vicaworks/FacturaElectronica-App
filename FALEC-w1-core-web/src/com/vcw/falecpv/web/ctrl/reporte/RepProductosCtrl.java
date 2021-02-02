/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RepProductosCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3057955400813916401L;

	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private List<Producto> productoList;
	private Producto productoSelected;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQuerieList;
	private BarChartModel barChartModel = new BarChartModel();
	
	/**
	 * 
	 */
	public RepProductosCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -180);
			consultarProducto();
			consultar();
			populateChart();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void buscar() {
		try {
			consultarProducto();
			consultar();
			populateChart();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultar()throws DaoException{
		ventasQuerieList = consultaVentaServicio.getVentasProductos(desde, 
				hasta, 
				productoSelected,establecimientoMain!=null?establecimientoMain.getIdestablecimiento():null,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	
	public void consultarProducto()throws DaoException{
		productoList = null;
		productoList = productoServicio.getProductoDao().getByQuery(EstadoRegistroEnum.ACTIVO, 
				establecimientoMain!=null?establecimientoMain.getIdestablecimiento():null,
						AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	private void populateChart() {
		barChartModel = new BarChartModel();
		ChartSeries productos = new ChartSeries();
		productos.setLabel("CANTIDAD");
		int cant = 0;
		for (VentasQuery v : ventasQuerieList) {
			if(cant<10) {
				productos.set(v.getNombregenerico().length()>20?v.getNombregenerico().substring(0, 20):v.getNombregenerico(),v.getCantidad());
			}
			cant++;
		}
		barChartModel.addSeries(productos);
		barChartModel.setTitle("PRODUCTOS");
		barChartModel.setZoom(true);
		barChartModel.setShowPointLabels(true);
		
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(ventasQuerieList==null || ventasQuerieList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-ClienteProducto.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(establecimientoMain==null?"TODOS":TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(),3,"0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(productoSelected!=null?productoSelected.getNombregenerico():"TODOS");
			
			int fila = 10;
			
			for (VentasQuery v : ventasQuerieList) {
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getCodigoprincipal());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(v.getNombregenerico());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getCantidad().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(v.getPreciototalsinimpuesto().doubleValue());
				
				fila++;
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-ClienteProducto-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
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
	 * @return the productoList
	 */
	public List<Producto> getProductoList() {
		return productoList;
	}

	/**
	 * @param productoList the productoList to set
	 */
	public void setProductoList(List<Producto> productoList) {
		this.productoList = productoList;
	}

	/**
	 * @return the productoSelected
	 */
	public Producto getProductoSelected() {
		return productoSelected;
	}

	/**
	 * @param productoSelected the productoSelected to set
	 */
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	/**
	 * @return the ventasQuerieList
	 */
	public List<VentasQuery> getVentasQuerieList() {
		return ventasQuerieList;
	}

	/**
	 * @param ventasQuerieList the ventasQuerieList to set
	 */
	public void setVentasQuerieList(List<VentasQuery> ventasQuerieList) {
		this.ventasQuerieList = ventasQuerieList;
	}

	/**
	 * @return the barChartModel
	 */
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	/**
	 * @param barChartModel the barChartModel to set
	 */
	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}

	public void limpiarFormulario(boolean consultarProductos) {
		try {
			
			ventasQuerieList = null;
			barChartModel = new BarChartModel();
			if(consultarProductos) {
				consultarProducto();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		 
	}
	
	public void limpiarBefore() {
		ventasQuerieList = null;
		barChartModel = new BarChartModel();
		AppJsfUtil.ajaxUpdate("formMain");
	}

}
