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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
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
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.liqcompra.LiqCompraFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ProveedorCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5057524440057042402L;
	
	@EJB
	private ProveedorServicio proveedorServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	
	private List<Proveedor> proveedorList;
	private Proveedor proveedorSelected;
	private String criteriBusqueda;
	private List<TipoIdentificacion> tipoIdentificacionList;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private RetencionFrmCtrl retencionFrmCtrl;

	/**
	 * 
	 */
	public ProveedorCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			estado = EstadoRegistroEnum.ACTIVO.getInicial();
			consultarProveedores();
			consultarTipoIdentificacion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarTipoIdentificacion()throws DaoException{
		tipoIdentificacionList = null;
		tipoIdentificacionList = proveedorServicio.getByProveedor();
	}
	
	public void consultarProveedores()throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:adquisicionDT");
		proveedorList = null;
		if(AppJsfUtil.getEstablecimiento()!=null) {
			proveedorList = proveedorServicio.getProveedorDao().getByConsulta(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), EstadoRegistroEnum.getByInicial(estado), criteriBusqueda);
		}
	}

	@Override
	public void refrescar() {
		try {
			AppJsfUtil.limpiarFiltrosDataTable("formMain:proveedorDT");
			proveedorSelected = null;
			consultarProveedores();
			consultarTipoIdentificacion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(proveedorSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "NO EXISTE REGISTRO SELECIONADO");
				return;
			}
			
			proveedorServicio.eliminar(proveedorSelected);
			proveedorSelected = null;
			consultarProveedores();
			AppJsfUtil.addInfoMessage("formMain", "REGISTRO ELIMINADO CORRECTAMENTE");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void guardar() {
		try {
			
			// validaciones
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "RS", proveedorSelected.getRazonsocial())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON LA MISMA RAZON SOCIAL");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorRazonSocial","YA EXISTE");
				return;
			}
			
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "NB", proveedorSelected.getNombrecomercial())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON EL MISMO NOMBRE COMERCIAL");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorNombreComercial","YA EXISTE");
				return;
			}
			
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "ID", proveedorSelected.getIdentificacion())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON LA MISMA IDENTIFICACION");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorNombreComercial","YA EXISTE");
				return;
			}
			
			proveedorSelected.setUpdated(new Date());
			proveedorSelected = proveedorServicio.guardar(proveedorSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			boolean flag = false;
			
			switch (callModule) {
			case "PROVEEDOR":
				consultarProveedores();
				flag = true;
				break;
			case "ADQUISICION":
				adquisicionFrmCtrl.consultarProveedor();
				adquisicionFrmCtrl.getAdquisicionSelected().setProveedor(proveedorSelected);
				flag = true;
				break;
			case "RETENCION":
				retencionFrmCtrl.getRetencionSeleccion().setProveedor(proveedorSelected);
				flag = false;
				break;
			case "LIQCOMPRA":
				LiqCompraFormCtrl liqCompraFormCtrl = (LiqCompraFormCtrl)AppJsfUtil.getManagedBean("liqCompraFormCtrl");
				liqCompraFormCtrl.getLiqCompraSelected().setProveedor(proveedorSelected);
				break;
			default:
				break;
			}
			if(flag) {
				AppJsfUtil.addInfoMessage("frmProveedor","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			}else {
				AppJsfUtil.hideModal("dlgProveedor");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProveedor", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void editar() {
		try {
			
			if(proveedorSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "NO EXISTE REGISTRO SELECIONADO");
				return;
			}
			
			AppJsfUtil.showModalRender("dlgProveedor", "frmProveedor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			
			nuevoProveedor();
			
			AppJsfUtil.showModalRender("dlgProveedor", "frmProveedor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevoEditar(String id) {
		try {
			
			proveedorSelected = null;
			
			if(id!=null) {
				proveedorSelected = proveedorServicio.consultarByPk(id);
			}
			
			if(proveedorSelected==null) {
				nuevoProveedor();
			}
			
			AppJsfUtil.showModalRender("dlgProveedor", "frmProveedor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevoProveedor() {
		proveedorSelected = new Proveedor();
		proveedorSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
		proveedorSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		proveedorSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
	}
	
	public void nuevoForm() {
		try {
			
			nuevoProveedor();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileProveedor() {
		try {
			
			if(proveedorList==null || proveedorList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listaproveedor.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			//datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(new Date()));
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			
			int fila = 8;
			
			
			for (Proveedor p : proveedorList) {
				
				int col = 0;
				
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getIdproveedor());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getTipoidentificacion().getTipoidentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getNombrecomercial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getTelefono()!=null?p.getTelefono():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getEmail()!=null?p.getEmail():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getProvincia()!=null?p.getProvincia():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getCiudad()!=null?p.getCiudad():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getDireccion()!=null?p.getDireccion():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getCodigopostal()!=null?p.getCodigopostal():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getContactonombre()!=null?p.getContactonombre():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getContactotelefono()!=null?p.getContactotelefono():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(p.getContactoemail()!=null?p.getContactoemail():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(usuarioServicio.consultarByPk(p.getIdusuario()).getNombre());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(FechaUtil.formatoFecha(p.getUpdated()));
				
				fila++;
				
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-listaproveedor-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}

	/**
	 * @return the proveedorList
	 */
	public List<Proveedor> getProveedorList() {
		return proveedorList;
	}

	/**
	 * @param proveedorList the proveedorList to set
	 */
	public void setProveedorList(List<Proveedor> proveedorList) {
		this.proveedorList = proveedorList;
	}

	/**
	 * @return the proveedorSelected
	 */
	public Proveedor getProveedorSelected() {
		return proveedorSelected;
	}

	/**
	 * @param proveedorSelected the proveedorSelected to set
	 */
	public void setProveedorSelected(Proveedor proveedorSelected) {
		this.proveedorSelected = proveedorSelected;
	}

	/**
	 * @return the tipoIdentificacionList
	 */
	public List<TipoIdentificacion> getTipoIdentificacionList() {
		return tipoIdentificacionList;
	}

	/**
	 * @param tipoIdentificacionList the tipoIdentificacionList to set
	 */
	public void setTipoIdentificacionList(List<TipoIdentificacion> tipoIdentificacionList) {
		this.tipoIdentificacionList = tipoIdentificacionList;
	}

	/**
	 * @return the criteriBusqueda
	 */
	public String getCriteriBusqueda() {
		return criteriBusqueda;
	}

	/**
	 * @param criteriBusqueda the criteriBusqueda to set
	 */
	public void setCriteriBusqueda(String criteriBusqueda) {
		this.criteriBusqueda = criteriBusqueda;
	}

	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the formModule
	 */
	public String getFormModule() {
		return formModule;
	}

	/**
	 * @param formModule the formModule to set
	 */
	public void setFormModule(String formModule) {
		this.formModule = formModule;
	}

	/**
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
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

	/**
	 * @return the retencionFrmCtrl
	 */
	public RetencionFrmCtrl getRetencionFrmCtrl() {
		return retencionFrmCtrl;
	}

	/**
	 * @param retencionFrmCtrl the retencionFrmCtrl to set
	 */
	public void setRetencionFrmCtrl(RetencionFrmCtrl retencionFrmCtrl) {
		this.retencionFrmCtrl = retencionFrmCtrl;
	}
	
	

}
