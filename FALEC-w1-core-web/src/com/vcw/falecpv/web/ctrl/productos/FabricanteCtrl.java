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
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
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
public class FabricanteCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@Inject
	private FabricanteServicio fabricanteServicio;
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	@Inject
	private UsuarioServicio usuarioServicio;
	
	private List<Fabricante> fabricanteList;
	private Fabricante fabricanteSelected;
	private String estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
	private String moduloCall;
	private String updateView;
	private ProductoCtrl productoCtrl;

	/**
	 * 
	 */
	public FabricanteCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public void buscar() {
		super.buscar();
	}

	private void consultar() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable(":formMain:fabricanteDT");
		fabricanteList = null;
		fabricanteList = fabricanteServicio.getFabricanteDao().getByEstado(EstadoRegistroEnum.getByInicial(estadoRegBusqueda),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	@Override
	public void refrescar() {
		try {
			fabricanteSelected = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(fabricanteSelected == null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						msg.getString("error.registros.noexiste"), 
						Message.ERROR);
				return;
			}
			
			// si tiene dependencias
			if(fabricanteServicio.tieneDependencias(fabricanteSelected.getIdfabricante(),fabricanteSelected.getEmpresa().getIdempresa())) {
				getMessageCommonCtrl().crearMensaje("Error", 
						msg.getString("error.tienedependencias"), 
						Message.ERROR);
				return;
			}
			
			fabricanteServicio.eliminar(fabricanteSelected);
			fabricanteSelected = null;
			consultar();
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					msg.getString("mensaje.eliminado.ok"), 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}
	}

	@Override
	public void guardar() {
		try {
			
			// validar si existe el nombre
			if(fabricanteServicio.getFabricanteDao().existeNombre(fabricanteSelected.getNombrecomercial(), fabricanteSelected.getIdfabricante(),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmFabricante", "ERROR","El nombre del Fabricante ya existe.");
				AppJsfUtil.addErrorMessage("frmFabricante:intNombre","Ya existe.");
				return;
			}
			
			fabricanteSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			fabricanteSelected.setUpdated(new Date());
			fabricanteSelected = fabricanteServicio.guardar(fabricanteSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			switch (moduloCall) {
			
				case "PRODUCTO":
					
					productoCtrl.consultarFabrica();
					productoCtrl.getProductoSelected().setFabricante(fabricanteSelected);
					//AppJsfUtil.ajaxUpdate("frmProducto:somFrmProductoEstablecimiento");
					AppJsfUtil.ajaxUpdate(updateView);
					break;
				default:
					
					consultar();
					break;
					
			}
			
			AppJsfUtil.addInfoMessage("frmFabricante","OK", msg.getString("mensaje.guardado.correctamente"));
			
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
			AppJsfUtil.showModalRender("dlgFabricante", "frmFabricante");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}
	}
	
	@Override
	public void nuevo() {
		try {
			nuevoFabricante();			
			AppJsfUtil.showModalRender("dlgFabricante", "frmFabricante");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}	
	}
	
	private void nuevoFabricante() {
		fabricanteSelected = new Fabricante();
		fabricanteSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		fabricanteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
	}
	
	public void nuevoForm() {
		try {
			nuevoFabricante();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}	
	}
	

	public StreamedContent getFileFabricante() {
		try {
			
			if(fabricanteList==null || fabricanteList.size()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listafabricante.xls");
			
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
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			// lista de categoria
			int fila = 8;
			int col = 0;
			for (Fabricante f : fabricanteList) {
				
				row = sheet.createRow(fila);
				col = 0;
				
				cell = row.createCell(col++);
				cell.setCellValue(f.getIdfabricante());
				
				cell = row.createCell(col++);
				cell.setCellValue(f.getNombrecomercial());
				
				
				cell = row.createCell(col++);
				cell.setCellValue(f.getEstado());
				
				
				cell = row.createCell(col++);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(f.getIdusuario()).getNombre());
				
				
				cell = row.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFechaHora(f.getUpdated()));
				
				fila++;
				
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-listafabricante.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e), 
					Message.ERROR);
		}
		return null;
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
