/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenerico;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EstablecimientoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;
	
	private List<Establecimiento> establecimientoAllList;
	private Establecimiento establecimientoSelected;
	private boolean flagEstablecimiento;
    private UploadedFile file;

	/**
	 * 
	 */
	public EstablecimientoCtrl() {
		
	}
	
	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public void buscar() {
		super.buscar();
	}

	@Override
	public void refrescar() {
		try {
			consultarEstablecimiento();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			if(establecimientoSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// si es matriz
			if("S".equals(establecimientoSelected.getMatriz())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EL ESTABLECIMIENTO MATRIZ");
				return;
			}
			
			establecimientoServicio.eliminar(establecimientoSelected);
			establecimientoSelected = null;
			establecimientoAllList = null;
			consultarEstablecimiento();
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ELIMINADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void guardar() {
		try {
        
			List<ParametroGenericoEmpresa> paramempresaList=new ArrayList<>();
			List<ParametroGenerico> paramGenericoList= new ArrayList<>();
			ParametroGenerico paramGeneric= new  ParametroGenerico();
			
			// validar si es Matriz
			if ("S".equals(establecimientoSelected.getMatriz())) {
				if (establecimientoServicio.getEstablecimientoDao().existeMatriz(
						establecimientoSelected.getIdestablecimiento(), establecimientoSelected.getMatriz())) {
					AppJsfUtil.addErrorMessage("frmEstable", "ERROR", "MATRIZ YA EXISTE.");
					return;
				}
			}

			establecimientoSelected.setUpdated(new Date());
			establecimientoSelected.setIdusuario(AppJsfUtil.getRemoteUser());
			establecimientoSelected = establecimientoServicio.guardar(establecimientoSelected);
			//guarda parametros genericos
			if ("S".equals(establecimientoSelected.getMatriz())) {
				// consulta parametro empresa
				paramempresaList = consultaParamEmpresa(establecimientoSelected.getEmpresa().getIdempresa());
				
				// seteo parametros
				for (ParametroGenericoEmpresa x : paramempresaList) {
					paramGeneric= new ParametroGenerico();
					paramGeneric.setConcepto(x.getConcepto());
					paramGeneric.setDescripcion(x.getDescripcion());
					paramGeneric.setValor(x.getValor());
					paramGeneric.setIdparametrogenerico(x.getIdparametroempresa());
					paramGenericoList.add(paramGeneric);
				}
				
				// inserta la lista de los parametros
				paramGenericoList.forEach((y) ->{
					 try {
						parametroGenericoServicio.insertListParamEstableciemto(y);
					} catch (DaoException e) {
						e.printStackTrace();
						AppJsfUtil.addErrorMessage("frmEstable", "ERROR AL INSERTAR PARAMETROS GENERICOS",
								TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
					}
					});
			
				AppJsfUtil.addInfoMessage("frmEstable", "OK", "REGISTRO,PARAMETROS ALMACENADOS CORRECTAMENTE.");
			}
			
			
			establecimientoAllList = null;
			consultarEstablecimiento();
			AppJsfUtil.addInfoMessage("frmEstable", "OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			file = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmEstable", "ERROR",
					TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}

	}
	
	@Override
	public void nuevo() {
		try {
			
			establecimientoSelected = new Establecimiento();
			flagEstablecimiento=false;
			
			establecimientoSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
	
			AppJsfUtil.showModalRender("dlgEstable", "frmEstable");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @throws DaoException
	 */
	private void consultarEstablecimiento() throws DaoException {
		establecimientoAllList= new ArrayList<>();
		establecimientoAllList  = establecimientoServicio.getEstablecimientoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}
	
	public void editar() {
		try {
			flagEstablecimiento=true;
			AppJsfUtil.showModalRender("dlgEstable", "frmEstable");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	
	/**
	 * 
	 */
	public void upload(FileUploadEvent event)
	{
		file = event.getFile();
		try {
			if(file != null)
            {
				byte[] bytes;
		        bytes = IOUtils.toByteArray(file.getInputstream());
		        establecimientoSelected.setLogo(bytes);
		        AppJsfUtil.addInfoMessage("frmEstable", "OK", "IMAGEN CARGADA CORRECTAMENTE.");
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstable", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @return
	 */
	public StreamedContent getFileEstablecimientos()  {
		try {
			
			
			if(establecimientoAllList==null || establecimientoAllList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listaestablecimiento.xls");
			
			// 2. inicializamos el excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llena hoja 1 del archivo
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
			
			for (Establecimiento e : establecimientoAllList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(e.getCodigoestablecimiento());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(e.getNombrecomercial());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(2);
				cell.setCellValue(e.getPuntoemision());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(e.getDireccionestablecimiento());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(e.getTelefono());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(5);
				cell.setCellValue(e.getCorreo());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(6);
				cell.setCellValue(e.getMatriz());
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
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-listaestablecimiento.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	
	/**
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<ParametroGenericoEmpresa> consultaParamEmpresa(String idEmpresa) throws DaoException {
		List<ParametroGenericoEmpresa> listaParamEmpresa = new ArrayList<>();

		listaParamEmpresa = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao()
				.getParamEmpresa(idEmpresa);

		return listaParamEmpresa;

	}
	

	public List<Establecimiento> getEstablecimientoAllList() {
		return establecimientoAllList;
	}

	public void setEstablecimientoAllList(List<Establecimiento> establecimientoAllList) {
		this.establecimientoAllList = establecimientoAllList;
	}

	public Establecimiento getEstablecimientoSelected() {
		return establecimientoSelected;
	}

	public void setEstablecimientoSelected(Establecimiento establecimientoSelected) {
		this.establecimientoSelected = establecimientoSelected;
	}

	public boolean isFlagEstablecimiento() {
		return flagEstablecimiento;
	}

	public void setFlagEstablecimiento(boolean flagEstablecimiento) {
		this.flagEstablecimiento = flagEstablecimiento;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
    
   
	
	
	
	
	

}
