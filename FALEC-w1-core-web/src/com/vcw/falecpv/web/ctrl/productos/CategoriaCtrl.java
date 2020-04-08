/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
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
public class CategoriaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private CategoriaServicio categoriaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private List<Categoria> categoriaList;
	private Categoria categoriaSelected;
	private String estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
	private String moduloCall;
	private String updateView;
	private ProductoCtrl productoCtrl;

	/**
	 * 
	 */
	public CategoriaCtrl() {
	}

	@Override
	public void limpiar() {
		super.limpiar();
	}
	
	@PostConstruct
	private void init() {
		try {
			estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void refrescar() {
		try {
			categoriaSelected = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	private void consultar() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable(":formMain:categoiaDT");
		categoriaList = null;
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.getByInicial(estadoRegBusqueda),AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(categoriaSelected == null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// si tiene dependencias
			if (categoriaServicio.tieneDependencias(categoriaSelected.getIdcategoria(),
					categoriaSelected.getEstablecimiento().getIdestablecimiento())) {
				
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINNAR TIENE DEPENDENCIAS.");
				return;
				
			}
			
			categoriaServicio.eliminar(categoriaSelected);
			categoriaSelected = null;
			consultar();
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ELIMINADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void guardar() {
		try {
			
			// validar si existe el nombre de la categoria
			if(categoriaServicio.getCategoriaDao().existeCategoria(categoriaSelected.getCategoria(), categoriaSelected.getIdcategoria(),AppJsfUtil.getEstablecimiento().getIdestablecimiento())){
				AppJsfUtil.addErrorMessage("frmCategoria", "ERROR","EL NOMBRE DE LA CATEGORIA YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmCategoria:intCategoria","YA EXISTE.");
				return;
			}
			
			categoriaSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			categoriaSelected.setUpdated(new Date());
			categoriaSelected = categoriaServicio.guardar(categoriaSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			switch (moduloCall) {
			case "PRODUCTO":
				
				productoCtrl.consultarCategoria();
				productoCtrl.getProductoSelected().setCategoria(categoriaSelected);
				//AppJsfUtil.ajaxUpdate("frmProducto:somFrmProductoCategoria");
				AppJsfUtil.ajaxUpdate(updateView);
				break;

			default:
				consultar();
				break;
			}
			
			AppJsfUtil.addInfoMessage("frmCategoria","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmCategoria", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void editar() {
		try {
			
			AppJsfUtil.showModalRender("dlgCategoria", "frmCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void nuevo() {
		try {
			
			categoriaSelected = new Categoria();
			categoriaSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			categoriaSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
			AppJsfUtil.showModalRender("dlgCategoria", "frmCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileCategoria() {
		try {
			
			if(categoriaList==null || categoriaList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listacategoria.xls");
			
			// 2. inicializamos el excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llenaa hoja 1 del archivo
			HSSFSheet sheet=wb.getSheetAt(0);
			
			// datos de la cabecera
			HSSFRow row = sheet.getRow(3);
			HSSFCell cell = row.getCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(new Date()));
			
			row = sheet.getRow(4);
			cell = row.getCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			row = sheet.getRow(5);
			cell = row.getCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getEstablecimiento().getNombrecomercial());
			
			// lista de categoria
			int fila = 8;
			
			for (Categoria c : categoriaList) {
				
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(c.getIdcategoria());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(c.getEstablecimiento().getNombrecomercial());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(2);
				cell.setCellValue(c.getCategoria());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(c.getDescripcion());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(c.getEstado());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(5);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(c.getIdusuario()).getNombre());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(6);				
				cell.setCellValue(c.getUpdated());
				UtilExcel.setHSSBordeCell(cell,"dd/mm/yyyy HH:mm");
				
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-listacategoria.xls");
			
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
	 * @return the estadoRegBusqueda
	 */
	public String getEstadoRegBusqueda() {
		return estadoRegBusqueda;
	}

	/**
	 * @param estadoRegBusqueda the estadoRegBusqueda to set
	 */
	public void setEstadoRegBusqueda(String estadoRegBusqueda) {
		this.estadoRegBusqueda = estadoRegBusqueda;
	}

	/**
	 * @return the moduloCall
	 */
	public String getModuloCall() {
		return moduloCall;
	}

	/**
	 * @param moduloCall the moduloCall to set
	 */
	public void setModuloCall(String moduloCall) {
		this.moduloCall = moduloCall;
	}

	/**
	 * @return the updateView
	 */
	public String getUpdateView() {
		return updateView;
	}

	/**
	 * @param updateView the updateView to set
	 */
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}

	/**
	 * @return the productoCtrl
	 */
	public ProductoCtrl getProductoCtrl() {
		return productoCtrl;
	}

	/**
	 * @param productoCtrl the productoCtrl to set
	 */
	public void setProductoCtrl(ProductoCtrl productoCtrl) {
		this.productoCtrl = productoCtrl;
	}	

}
