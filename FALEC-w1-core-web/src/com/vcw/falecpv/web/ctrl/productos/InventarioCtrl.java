/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

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
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.InventarioServicio;
import com.vcw.falecpv.core.servicio.KardexProductoServicio;
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
public class InventarioCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2989264243074360358L;

	@EJB
	private CategoriaServicio categoriaServicio;
	
	@EJB
	private FabricanteServicio fabricanteServicio;
	
	@EJB
	private KardexProductoServicio kardexProductoServicio;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private InventarioServicio inventarioServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	
	private List<Categoria> categoriaList;
	private Categoria categoriaSelected;
	private List<Fabricante> fabricanteList;
	private Fabricante fabricanteSelected;
	private List<Producto> productoFormList;
	private List<Producto> productoList;
	private Producto productoFormSelected;
	private String opcionBusqueda;
	private String codProducto;
	private BigDecimal stock;
	private Date fechaCaducidad;
	
	/**
	 * 
	 */
	public InventarioCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			stock = BigDecimal.ZERO;
			fechaCaducidad = new Date();
			opcionBusqueda = "INVENTARIO";
			consultarCategoriasList();
			consultarProductoList();
			consultarFabricanteList();
			buscarDispacher();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarCategoriasList()throws DaoException{
		categoriaList = null;
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarFabricanteList()throws DaoException{
		fabricanteList = null;
		fabricanteList = fabricanteServicio.getFabricanteDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarProductoList()throws DaoException{
		productoFormList = null;
		productoFormList = productoServicio.getProductoDao().getByEstado("PRODUCTO",EstadoRegistroEnum.ACTIVO, establecimientoMain.getIdestablecimiento());
	}

	@Override
	public void refrescar() {
		
		try {
			
			buscarDispacher();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	@Override
	public void limpiar() {
		
		try {
			
			opcionBusqueda = "INVENTARIO";
			consultarCategoriasList();
			consultarProductoList();
			consultarFabricanteList();
			buscarDispacher();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	
	
	public void buscarDispacher() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:inventarioDT");
		productoList = null;
		
		switch (opcionBusqueda) {
		case "INVENTARIO":
			productoList = inventarioServicio.getInventario("PRODUCTO",establecimientoMain.getIdestablecimiento());
			break;
		case "STOCKMAYORZERO":
			productoList = inventarioServicio.getStokMayorZero(establecimientoMain.getIdestablecimiento());
			break;
		case "STOCKMENORIGUAL":
			if(stock!=null) {
				productoList = inventarioServicio.getStokLessEqualsThan(stock, establecimientoMain.getIdestablecimiento());
			}
			break;
		case "FECHACADUCIDAD":
			if(fechaCaducidad!=null) {
				productoList = inventarioServicio.getFechaCaducidadLessEqualsThan(fechaCaducidad, establecimientoMain.getIdestablecimiento());
			}
			break;
		case "PRODUCTO":
			if(productoFormSelected!=null) {
				productoList = inventarioServicio.getByIdProducto(productoFormSelected.getIdproducto(), establecimientoMain.getIdestablecimiento());
			}
			break;
		case "CODPRODUCTO":
			if(codProducto!=null) {
				productoList = inventarioServicio.getByCodigoPrincipal(codProducto, establecimientoMain.getIdestablecimiento());
			}
			break;
		case "CATEGORIA":
			if(categoriaSelected!=null) {
				productoList = inventarioServicio.getByCategoria(categoriaSelected.getIdcategoria(), establecimientoMain.getIdestablecimiento());
			}
			break;
		case "FABRICANTE":
			if(fabricanteSelected!=null) {
				productoList = inventarioServicio.getByFabricante(fabricanteSelected.getIdfabricante(), establecimientoMain.getIdestablecimiento());
			}
			break;
		default:
			return;
		}
	}
	
	public StreamedContent getFileInventario(){
		try {
			
			if(productoList==null || productoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-Inventario.xlsx");
			
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
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(new Date()));
			
			
			int fila = 8;
			sheet.shiftRows(fila, (fila+(productoList.size()<4?4:productoList.size())), (productoList.size()<4?4:productoList.size()));
			
			for (Producto p : productoList) {
				
				int col = 0;
				
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(p.getCategoria().getCategoria());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(p.getFabricante().getNombrecomercial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(p.getCodigoprincipal());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(p.getNombregenerico());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getIva().getValor().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getStock().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getPreciounitario().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getPreciouno().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getPreciodos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(p.getPreciotres().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				if(p.getFechavencimiento()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(p.getFechavencimiento()));
				}
				
				
				fila++;
			}
			
			BigDecimal totalStock = BigDecimal.ZERO;
			BigDecimal totalCosto = BigDecimal.ZERO;
			
			if(!productoList.isEmpty()) {
				totalStock = BigDecimal.valueOf(productoList.stream().mapToDouble(x->x.getStock().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
				totalCosto = BigDecimal.valueOf(productoList.stream().mapToDouble(x -> x.getStock()
						.multiply(x.getPreciounitario()).setScale(2, RoundingMode.HALF_UP).doubleValue()).sum());
			}
			
			for (int i = fila; i < fila+100; i++) {
				fila = i;
				rowCliente = sheet.getRow(fila);
				if(rowCliente!=null && rowCliente.getCell(0).getStringCellValue().contains("STOCK")) {
					break;
				}
				
			}
			
			// totales
//			fila+=3;
			rowCliente = sheet.getRow(fila);
			if(rowCliente!=null) {
				rowCliente.createCell(1).setCellValue(totalStock.doubleValue());
			}
			
			fila++;
			rowCliente = sheet.getRow(fila);
			if(rowCliente!=null) {
				rowCliente.createCell(1).setCellValue(totalCosto.doubleValue());
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-Inventario-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	/**
	 * @return the categoriaList
	 */
	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}

	/**
	 * @param categoriaList the categoriaList to set
	 */
	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}

	/**
	 * @return the categoriaSelected
	 */
	public Categoria getCategoriaSelected() {
		return categoriaSelected;
	}

	/**
	 * @param categoriaSelected the categoriaSelected to set
	 */
	public void setCategoriaSelected(Categoria categoriaSelected) {
		this.categoriaSelected = categoriaSelected;
	}

	/**
	 * @return the fabricanteList
	 */
	public List<Fabricante> getFabricanteList() {
		return fabricanteList;
	}

	/**
	 * @param fabricanteList the fabricanteList to set
	 */
	public void setFabricanteList(List<Fabricante> fabricanteList) {
		this.fabricanteList = fabricanteList;
	}

	/**
	 * @return the fabricanteSelected
	 */
	public Fabricante getFabricanteSelected() {
		return fabricanteSelected;
	}

	/**
	 * @param fabricanteSelected the fabricanteSelected to set
	 */
	public void setFabricanteSelected(Fabricante fabricanteSelected) {
		this.fabricanteSelected = fabricanteSelected;
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
	 * @return the opcionBusqueda
	 */
	public String getOpcionBusqueda() {
		return opcionBusqueda;
	}

	/**
	 * @param opcionBusqueda the opcionBusqueda to set
	 */
	public void setOpcionBusqueda(String opcionBusqueda) {
		this.opcionBusqueda = opcionBusqueda;
	}

	/**
	 * @return the codProducto
	 */
	public String getCodProducto() {
		return codProducto;
	}

	/**
	 * @param codProducto the codProducto to set
	 */
	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	/**
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * @return the fechaCaducidad
	 */
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	/**
	 * @param fechaCaducidad the fechaCaducidad to set
	 */
	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	/**
	 * @return the productoFormList
	 */
	public List<Producto> getProductoFormList() {
		return productoFormList;
	}

	/**
	 * @param productoFormList the productoFormList to set
	 */
	public void setProductoFormList(List<Producto> productoFormList) {
		this.productoFormList = productoFormList;
	}

	/**
	 * @return the productoFormSelected
	 */
	public Producto getProductoFormSelected() {
		return productoFormSelected;
	}

	/**
	 * @param productoFormSelected the productoFormSelected to set
	 */
	public void setProductoFormSelected(Producto productoFormSelected) {
		this.productoFormSelected = productoFormSelected;
	}

}
