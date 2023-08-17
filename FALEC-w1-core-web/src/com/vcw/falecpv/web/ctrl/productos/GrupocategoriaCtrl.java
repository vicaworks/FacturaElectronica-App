/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Grupocategoria;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.GrupocategoriaServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
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
public class GrupocategoriaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@Inject
	private GrupocategoriaServicio grupocategoriaServicio;
	
	@Inject
	private CategoriaServicio categoriaServicio;
	
	@Inject
	private UsuarioServicio usuarioServicio;
	
	private List<Grupocategoria> grupocategoriaList;
	private Grupocategoria grupocategoriaSelected;
	private String estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
	private String moduloCall;
	private String updateView;
	private CategoriaCtrl categoriaCtrl;
	private ProductoCtrl productoCtrl;

	/**
	 * 
	 */
	public GrupocategoriaCtrl() {
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
//			grupocategoriaSelected = null;
//			consultar();
			getMessageCommonCtrl().crearMensaje("Importante", "TODO EL MENSJE EN ESTA SECCION", Message.WARNING);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	private void consultar() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable(":formMain:grupocategoiaDT");
		grupocategoriaList = null;
		grupocategoriaList = grupocategoriaServicio.getGrupocategoriaDao().getByEstado(
				EstadoRegistroEnum.getByInicial(estadoRegBusqueda),
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
		for (Grupocategoria grupocategoria : grupocategoriaList) {
			grupocategoria.setCategoriaList(
					categoriaServicio.getByGrupoCategoria(
							grupocategoria.getIdgrupocategoria()
							)
					);
		}
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(grupocategoriaSelected == null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// si tiene dependencias
			if (grupocategoriaServicio.tieneDependencias(grupocategoriaSelected.getIdgrupocategoria(),
					grupocategoriaSelected.getEmpresa().getIdempresa())) {
				
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINNAR TIENE DEPENDENCIAS.");
				return;
				
			}
			
			grupocategoriaServicio.eliminar(grupocategoriaSelected);
			grupocategoriaSelected = null;
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
			if(grupocategoriaServicio.getGrupocategoriaDao().existeGrupoCategoria(
					grupocategoriaSelected.getGrupo(), 
					grupocategoriaSelected.getIdgrupocategoria(),
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())){
				AppJsfUtil.addErrorMessage("frmCategoria", "ERROR","EL NOMBRE DEL GRUPO YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmGrupoCategoria:intCategoria","YA EXISTE.");
				return;
			}
			
			grupocategoriaSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			grupocategoriaSelected = grupocategoriaServicio.guardar(grupocategoriaSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			switch (moduloCall) {
			case "PRODUCTO":
				
				productoCtrl.consultarCategoria();
				//productoCtrl.getProductoSelected().setCategoria(categoriaSelected);
				//AppJsfUtil.ajaxUpdate("frmProducto:somFrmProductoCategoria");
				AppJsfUtil.ajaxUpdate(updateView);
				break;
			case "CATEGORIA":
				
				categoriaCtrl.buscar();
				//productoCtrl.getProductoSelected().setCategoria(categoriaSelected);
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
			
			nuevoCategoria();
			AppJsfUtil.showModalRender("dlgCategoria", "frmCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevoCategoria() {
		grupocategoriaSelected = new Grupocategoria();
		grupocategoriaSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		grupocategoriaSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
	}
	
	public void nuevoForm() {
		try {
			nuevoCategoria();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}	
	}
	
	public StreamedContent getFileCategoria() {
		try {
			
			if(grupocategoriaList==null || grupocategoriaList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listagrupocategoria.xls");
			
			// 2. inicializamos el excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
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
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			// lista de categoria
			int fila = 8;
			
			for (Grupocategoria c : grupocategoriaList) {
				
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(c.getIdgrupocategoria());
				
				cell = row.createCell(2);
				cell.setCellValue(c.getGrupo());
				
				
				cell = row.createCell(3);
				cell.setCellValue(
						c.getCategoriaList() == null || c.getCategoriaList().isEmpty() ? "" : 
							c.getCategoriaList().stream().map(x->x.getCategoria()).collect(Collectors.joining(" ,")));
				
				
				cell = row.createCell(4);
				cell.setCellValue(c.getEstado());
				
				
				cell = row.createCell(5);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(c.getIdusuario()).getNombre());
				
				
				cell = row.createCell(6);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFechaHora(c.getUpdated()));
				
				fila++;
				
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-listagrupocategoria.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}	

}
