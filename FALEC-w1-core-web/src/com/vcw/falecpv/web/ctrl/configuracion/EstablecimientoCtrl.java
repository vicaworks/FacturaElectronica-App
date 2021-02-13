/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPlantillasEnum;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
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
public class EstablecimientoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private EmpresaServicio empresaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private List<Establecimiento> establecimientoAllList;
	private Establecimiento establecimientoSelected;
	private boolean flagEstablecimiento;
    private UploadedFile file;
    public final static String matriz="S";

	/**
	 * 
	 */
	public EstablecimientoCtrl() {
		
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarEstablecimiento();
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

	@Override
	public void refrescar() {
		try {
			establecimientoSelected  = null;
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
			//tiene referencias
			if(establecimientoServicio.tieneDependenciasEst(establecimientoSelected.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EXISTE REFERENCIAS");
				return;
			}
			//elimina establecimiento
			establecimientoServicio.eliminar(establecimientoSelected);
			
			//Eliminar parametros genericos
			eliminaParametros(establecimientoSelected);
			
			establecimientoSelected = null;
			establecimientoAllList = null;
			consultarEstablecimiento();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @param establecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean eliminaParametros(Establecimiento establecimiento) throws DaoException{
		
		List<ParametroGenericoEmpresa> paramEmpresaList = new ArrayList<>();
			// consulta parametros genericos empresa
			paramEmpresaList = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getParamEmpresa(establecimiento.getIdestablecimiento());
			// Elimina los parametros
			return parametroGenericoEmpresaServicio.deleteParamEstableciemto(paramEmpresaList);
	
	}
	
	@Override
	public void guardar() {
		try {
			Establecimiento establecimientoUpdate = new Establecimiento();
			// Validar el codigo establecimiento
			if (establecimientoServicio.getEstablecimientoDao().existeCodEstablecimiento(
					establecimientoSelected.getIdestablecimiento(),
					establecimientoSelected.getCodigoestablecimiento(),establecimientoSelected.getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmEstable", "ERROR", "CODIGO DUPLICADO");
				return;
			}
				
			// Validar si existe Matriz
			if (establecimientoServicio.getEstablecimientoDao().existeMatriz(null, matriz,establecimientoSelected.getEmpresa().getIdempresa())) {
				if (matriz.equals(establecimientoSelected.getMatriz())) {
					// consulto el id de la matriz
					establecimientoUpdate = establecimientoServicio.getEstablecimientoDao()
							.getEstablecimientobyMatriz(matriz,establecimientoSelected.getEmpresa().getIdempresa()).get(0);
					// actualizo el campo matriz del actual
					establecimientoUpdate.setMatriz("N");
					// actualiza establecimiento existente
					establecimientoServicio.guardar(establecimientoUpdate);
					// guarda establecimiento nuevo
					establecimientoSelected.setUpdated(new Date());
					establecimientoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
					
					
					
					establecimientoSelected = establecimientoServicio.guardar(establecimientoSelected);

				} else {
					// consulto el id de la matriz
					establecimientoUpdate = establecimientoServicio.getEstablecimientoDao()
							.getEstablecimientobyMatriz(matriz,establecimientoSelected.getEmpresa().getIdempresa()).get(0);
					// inserto normal
					establecimientoSelected.setUpdated(new Date());
					establecimientoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
					establecimientoSelected = establecimientoServicio.guardar(establecimientoSelected);
				}

			} else {
				
				// Asigno 1ra vez matriz
				establecimientoSelected.setMatriz("S");
				establecimientoSelected.setUpdated(new Date());
				establecimientoSelected.setIdusuario(AppJsfUtil.getRemoteUser());
				establecimientoSelected = establecimientoServicio.guardar(establecimientoSelected);

			}
			
			// registra la imagen
			
			if(establecimientoSelected.getLogo()!=null) {
				String nombreLogo = establecimientoSelected.getEmpresa().getRuc() + establecimientoSelected.getCodigoestablecimiento() + establecimientoSelected.getNombreimagen().substring(establecimientoSelected.getNombreimagen().lastIndexOf("."),establecimientoSelected.getNombreimagen().length());
				String pathLogo = parametroGenericoServicio.consultarParametro(PGPlantillasEnum.PATH_LOGO, TipoRetornoParametroGenerico.STRING);
				FileOutputStream output = new FileOutputStream(new File(pathLogo + nombreLogo));
				IOUtils.write(establecimientoSelected.getLogo(), output);
				establecimientoSelected.setNombreimagen(nombreLogo);
				establecimientoSelected = establecimientoServicio.actualizar(establecimientoSelected);
			}
			
			// parametros genericos
			empresaServicio.parametrosGenericosFacade(establecimientoMain.getEmpresa().getIdempresa());
			
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
			
			flagEstablecimiento=true;
			establecimientoSelected = new Establecimiento();
			establecimientoSelected.setAmbiente("1");
			establecimientoSelected.setMatriz("N");
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
		establecimientoAllList  = establecimientoServicio.getEstablecimientoDao().getByEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	/**
	 * Edita el establecimiento
	 */
	public void editar() {
		try {
			flagEstablecimiento=false;
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
		        bytes = IOUtils.toByteArray(file.getInputStream());
		        establecimientoSelected.setLogo(bytes);
		        AppJsfUtil.addInfoMessage("frmEstable", "OK", "IMAGEN CARGADA CORRECTAMENTE.");
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEstable", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public void handleUpload(FileUploadEvent event) throws IOException {
		
		UploadedFile uploadedFile = event.getFile();
		establecimientoSelected.setLogo(uploadedFile.getContent());
		establecimientoSelected.setNombreimagen(uploadedFile.getFileName());
		
	}
	
	public String getNombreImagen() {
		
		if(establecimientoSelected!=null) {
			return establecimientoSelected.getNombreimagen();
		}
		
		return "";
	}
	
	public StreamedContent getImageEstablecimiento() {
		try {
			
			if(establecimientoSelected!=null && establecimientoSelected.getLogo()==null) {
				return null;
			}
			
			return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(establecimientoSelected.getLogo())).build();
			
//			return new DefaultStreamedContent(new ByteArrayInputStream(establecimientoSelected.getLogo()));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public StreamedContent getImageToStream(byte[] img) {
		try {
			
			if(img==null) {
				return null;
			}
			
			return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(img)).build();
			
//			return  new DefaultStreamedContent(new ByteArrayInputStream(img));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void limpiarImagen() {
		try {
			
			establecimientoSelected.setLogo(null);
			establecimientoSelected.setNombreimagen(null);
			
		} catch (Exception e) {
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
			cell.setCellValue(AppJsfUtil.getUsuario().getEstablecimiento().getEmpresa().getNombrecomercial());
			
			// lista de Establecimientos
			int fila = 8;
			
			for (Establecimiento e : establecimientoAllList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(e.getCodigoestablecimiento());
				
				cell = row.createCell(1);
				cell.setCellValue(e.getNombrecomercial());
				
				cell = row.createCell(2);
				cell.setCellValue(e.getPuntoemision());
				
				cell = row.createCell(3);
				cell.setCellValue(e.getDireccionestablecimiento());
				
				cell = row.createCell(4);
				cell.setCellValue(e.getTelefono());
				
				cell = row.createCell(5);
				cell.setCellValue(e.getCorreo());
				
				cell = row.createCell(6);
				cell.setCellValue(e.getMatriz());
				
				cell = row.createCell(7);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(e.getIdusuario()).getIdusuario());
				
				cell = row.createCell(8);
				cell.setCellValue(e.getUpdated());
				
				
				fila++;
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-listaestablecimiento" + AppJsfUtil.getUsuario().getEstablecimiento().getEmpresa().getNombrecomercial() + ".xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
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
