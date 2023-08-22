/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
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
	
	private List<Categoria> categoriaList;
	private List<Categoria> categoriaSelectedList;
	private Categoria categoriaSelected;

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
		}
	}

	@Override
	public void refrescar() {
		try {
			grupocategoriaSelected = null;
			consultar();		
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen registros seleccionados.", 
						Message.ERROR);
				return;
			}
			
			// si tiene dependencias
			if (grupocategoriaServicio.tieneDependencias(grupocategoriaSelected.getIdgrupocategoria(),
					grupocategoriaSelected.getEmpresa().getIdempresa())) {
				
				getMessageCommonCtrl().crearMensaje("Error", 
						"No se puede eliminar, tiene referencias.", 
						Message.ERROR);
				return;
				
			}
			
			grupocategoriaServicio.eliminar(grupocategoriaSelected);
			grupocategoriaSelected = null;
			consultar();
			getMessageCommonCtrl().crearMensaje("Ok", 
					msg.getString("mensaje.eliminado.ok"), 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
				AppJsfUtil.addErrorMessage("frmGrupoCategoria", "Error","Error en el formulario");				
				AppJsfUtil.addErrorMessage("frmGrupoCategoria:intGrupoCategoria","Ya existe.");
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
			case "GRUPO_CATEGORIA":
				
				consultar();
				AppJsfUtil.ajaxUpdate(updateView);
				break;
			default:
				consultar();
				break;
			}
			
			AppJsfUtil.addInfoMessage("frmGrupoCategoria","OK", "Registro guardado correctamente.");
			
		} catch (Exception e) {
			e.printStackTrace();			
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}
	}
	
	@Override
	public void editar() {
		try {
			
			AppJsfUtil.showModalRender("dlgGrupoCategoria", "frmGrupoCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	@Override
	public void nuevo() {
		try {
			
			nuevoCategoria();
			AppJsfUtil.showModalRender("dlgGrupoCategoria", "frmGrupoCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}	
	}
	
	public void load_asignarCategorias() {
		try {
			
			categoriaList = null;
			categoriaSelectedList = null;
			categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.getByInicial(estadoRegBusqueda),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			AppJsfUtil.limpiarFiltrosDataTable(":frmListaCategoria:listacategoriaDT");
			AppJsfUtil.showModalRender("dlgListaCategoria", "frmListaCategoria");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void asignarCategorias() {
		try {
			
			if(categoriaSelectedList == null || categoriaSelectedList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen registros seleccionados.", 
						Message.ERROR);
				return;
			}
			
			List<String> idGrupoCategiaChange = new ArrayList<>();			
			idGrupoCategiaChange.add(grupocategoriaSelected.getIdgrupocategoria());
			for (Categoria categoria : categoriaSelectedList) {
				if(categoria.getGrupocategoria() != null && !idGrupoCategiaChange.contains(categoria.getGrupocategoria().getIdgrupocategoria())) {
					idGrupoCategiaChange.add(categoria.getGrupocategoria().getIdgrupocategoria());
				}
				categoria.setGrupocategoria(grupocategoriaSelected);
				categoriaServicio.actualizar(categoria);
			}
			
			for (String idGrupoCategoria : idGrupoCategiaChange) {
				Grupocategoria g = grupocategoriaList.stream().filter(x->x.getIdgrupocategoria().equals(idGrupoCategoria)).findFirst().orElse(null);
				if(g != null) {					
					g.setCategoriaList(
						categoriaServicio.getByGrupoCategoria(idGrupoCategoria)
						);
				}
			}			
			
			AppJsfUtil.hideModal("dlgListaCategoria");
			getMessageCommonCtrl().crearMensaje("Ok", 
					msg.getString("mensaje.categoria.asignadas"), 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void desasignarGrupocategoria() {
		try {
			
			categoriaSelected.setGrupocategoria(null);
			categoriaServicio.actualizar(categoriaSelected);
			
			grupocategoriaSelected.setCategoriaList(
					categoriaServicio.getByGrupoCategoria(grupocategoriaSelected.getIdgrupocategoria())
					);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public StreamedContent getFileCategoria() {
		try {
			
			if(grupocategoriaList==null || grupocategoriaList.size()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos", 
						Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
		return null;
	}

	/**
	 * @return the grupocategoriaList
	 */
	public List<Grupocategoria> getGrupocategoriaList() {
		return grupocategoriaList;
	}

	/**
	 * @param grupocategoriaList the grupocategoriaList to set
	 */
	public void setGrupocategoriaList(List<Grupocategoria> grupocategoriaList) {
		this.grupocategoriaList = grupocategoriaList;
	}

	/**
	 * @return the grupocategoriaSelected
	 */
	public Grupocategoria getGrupocategoriaSelected() {
		return grupocategoriaSelected;
	}

	/**
	 * @param grupocategoriaSelected the grupocategoriaSelected to set
	 */
	public void setGrupocategoriaSelected(Grupocategoria grupocategoriaSelected) {
		this.grupocategoriaSelected = grupocategoriaSelected;
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
	 * @return the categoriaCtrl
	 */
	public CategoriaCtrl getCategoriaCtrl() {
		return categoriaCtrl;
	}

	/**
	 * @param categoriaCtrl the categoriaCtrl to set
	 */
	public void setCategoriaCtrl(CategoriaCtrl categoriaCtrl) {
		this.categoriaCtrl = categoriaCtrl;
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
	 * @return the categoriaSelectedList
	 */
	public List<Categoria> getCategoriaSelectedList() {
		return categoriaSelectedList;
	}

	/**
	 * @param categoriaSelectedList the categoriaSelectedList to set
	 */
	public void setCategoriaSelectedList(List<Categoria> categoriaSelectedList) {
		this.categoriaSelectedList = categoriaSelectedList;
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

}
