/**
 * 
 */
package com.vcw.falecpv.web.ctrl.usuario;

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
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
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
@ViewScoped
public class UsuarioCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8543804720463095574L;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	
	private List<Usuario> usuarioList;
	private String estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
	private Usuario usuarioSelected;
	private List<Establecimiento> establecimientoList;
	
	/**
	 * 
	 */
	public UsuarioCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			usuarioList = null;
			consultarUsuario();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void limpiar() {
		usuarioList = null;
		estadoRegBusqueda = "A";
	}



	@Override
	public void refrescar() {
		try {
			usuarioList = null;
			consultarUsuario();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	private void consultarUsuario() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:usuariosDT");
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.getByInicial(estadoRegBusqueda));
	}
	
	private void consultarEstablecimientos() throws DaoException {
		establecimientoList = null;
		establecimientoList = establecimientoServicio.getEstablecimientoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}

	@Override
	public void eliminar() {
		try {
			if(usuarioSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// si el usuario es el mismo del de sesion
			if (usuarioSelected.getIdusuario().equals(AppJsfUtil.getUsuario().getIdusuario())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINNAR EL USUARIO DE SESSION.");
				return;
			}
			
			// si tiene dependencias
			if(usuarioServicio.dependencias(AppJsfUtil.getUsuario().getIdusuario())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINNAR EL USUARIO TIENE REGISTROS EN EL SISTEMA REALIZADOS.");
				return;
			}
			
			usuarioServicio.eliminar(usuarioSelected);
			usuarioSelected = null;
			usuarioList = null;
			consultarUsuario();
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ELIMINADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void guardar() {
		try {
			
			if(usuarioSelected.isActualizarCredenciales()) {
				if(!usuarioSelected.getClave().equals(usuarioSelected.getClave2())) {
					AppJsfUtil.addErrorMessage("frmUsuario", "ERROR","LAS CREDENCIALES NO COINCIDEN.");
					AppJsfUtil.addErrorMessage("frmUsuario:intClave1","ERROR CONFIRMACION (LAS CREDENCIALES NO COINCIDEN).");
					return;
				}
			}
			
			// validar si existe el login
			if (usuarioServicio.getUsuarioDao().existeLogin(usuarioSelected.getIdusuario(), usuarioSelected.getLogin())) {
				AppJsfUtil.addErrorMessage("frmUsuario", "ERROR","EL LOGIN YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmUsuario:intLogin","YA EXISTE.");
				return;
			}
			
			usuarioSelected.setUpdated(new Date());			
			usuarioSelected = usuarioServicio.guardar(usuarioSelected);
			usuarioList = null;
			consultarUsuario();
			AppJsfUtil.addInfoMessage("frmUsuario","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmUsuario", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void nuevo() {
		try {
			usuarioSelected = new Usuario();
			usuarioSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			usuarioSelected.setAdministrador("N");
			usuarioSelected.setActualizarCredenciales(true);
			usuarioSelected.setEstado("A");
			consultarEstablecimientos();
			AppJsfUtil.showModalRender("dlgUsuario", "frmUsuario");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editar() {
		try {
			usuarioSelected.setEmpresa(usuarioSelected.getEstablecimiento().getEmpresa());
			usuarioSelected.setActualizarCredenciales(false);
			consultarEstablecimientos();
			AppJsfUtil.showModalRender("dlgUsuario", "frmUsuario");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public StreamedContent getFileUsuarios()  {
		try {
			
			
			if(usuarioList==null || usuarioList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listausuarios.xls");
			
			// 2. inicializamos el excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
//			usuarioList = null;
//			usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.getByInicial(EstadoRegistroEnum.TODOS.getInicial()));
			
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
			
			// lista de usuarios
			int fila = 8;
			
			for (Usuario u : usuarioList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(u.getIdusuario());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(u.getNombre());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(2);
				cell.setCellValue(u.getEstablecimiento().getNombrecomercial());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(u.getPuntoemision());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(u.getTelefono());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(5);
				cell.setCellValue(u.getNombrepantalla());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(6);
				cell.setCellValue(u.getAdministrador());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(7);
				cell.setCellValue(u.getEstado());
				UtilExcel.setHSSBordeCell(cell);
				
				fila++;
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-listausuarios.xls");
			
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
	 * @return the establecimientoList
	 */
	public List<Establecimiento> getEstablecimientoList() {
		return establecimientoList;
	}


	/**
	 * @param establecimientoList the establecimientoList to set
	 */
	public void setEstablecimientoList(List<Establecimiento> establecimientoList) {
		this.establecimientoList = establecimientoList;
	}
	

}
