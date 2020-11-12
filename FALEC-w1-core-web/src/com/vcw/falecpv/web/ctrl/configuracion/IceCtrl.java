/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.servicio.IceServicio;
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
public class IceCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3122436716767785684L;
	
	@EJB
	private IceServicio iceServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;

	private List<Ice> iceAllList;
	private List<Ice> iceAllFilterList;
	
	private Ice iceSelected;
	private File fileIces;
	private String nombreFileIce;
	private boolean existeNovedadesIce;
	private boolean renderResultadoImportIce;

	/**
	 * 
	 */
	public IceCtrl() {
	}

	@PostConstruct
	public void init() {
		try {
			consultarIce();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	@Override
	public void refrescar() {
		try {
			consultarIce();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			//verifica dependencias
			if(iceServicio.tieneDependenciasIce(iceSelected.getIdice())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EXISTE REFERENCIAS");
				return;
			}
			
			iceServicio.eliminar(iceSelected);
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ELIMINADO CORRECTAMENTE.");
			consultarIce();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
	}

	@Override
	public void guardar() {
		try {
			
			//validar unico codigo ice
			if (iceServicio.getIceDao().existeCodigo(iceSelected.getCodigo(), iceSelected.getIdice(),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmIce", "ERROR", "CODIGO DUPLICADO");
				iceAllList = null;
				consultarIce();
				return;
			}
			
			//guarda-edita
			iceSelected.setUpdated(new Date());
			iceSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			iceSelected.setTarifaadvalorem(iceSelected.getTarifaadvalorem().contains("%")?iceSelected.getTarifaadvalorem():iceSelected.getTarifaadvalorem()+"%");
			iceSelected = iceServicio.guardar(iceSelected);
				
				iceAllList = null;
				consultarIce();
				AppJsfUtil.addInfoMessage("frmIce", "OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
										
		} catch (DaoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
	}

	@Override
	public void editar() {
		try {
			AppJsfUtil.showModalRender("dlgIce", "frmIce");
			} catch (Exception e) {
				e.printStackTrace();
				AppJsfUtil.addErrorMessage("frmIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
	}

	@Override
	public void nuevo() {
		try {
			nuevoIce();
			AppJsfUtil.showModalRender("dlgIce", "frmIce");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevoIce() {
		iceSelected = new Ice();
		iceSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
		iceSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		iceSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		iceSelected.setCodigoIce("3");
	}
	
	
	public void nuevoForm() {
		try {
			nuevoIce();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @throws DaoException
	 */
	private void consultarIce() throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:iceDT");
		iceAllList= new ArrayList<>();
		iceAllList  = iceServicio.getIceDao().getAllIce(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}

	/**
	 * @return
	 */
	public StreamedContent getFileIce()  {
		try {
						
			if(iceAllList==null || iceAllList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-iceList.xls");
			
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
			
			// lista de IVA
			int fila = 9;
			
			for (Ice e : iceAllList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(e.getIdice());
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(e.getCodigo());
//				UtilExcel.setHSSBordeCell(cell);
				
				
				cell = row.createCell(2);
				cell.setCellValue(e.getEstado().equals("I")?"INACTIVO":"ACTIVO");
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(e.getDescripcion());
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(e.getTarifaadvalorem());
//				UtilExcel.setHSSBordeCell(cell);
				
				
				cell = row.createCell(5);
				cell.setCellValue(e.getValor().doubleValue());
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(6);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(e.getIdusuario()).getNombre());
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(7);
				cell.setCellValue(FechaUtil.formatoFechaHora(e.getUpdated()));
//				UtilExcel.setHSSBordeCell(cell);
				
				
				fila++;
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-iceList" +  AppJsfUtil.getEstablecimiento().getEmpresa().getNombrecomercial() + ".xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}

	
	public StreamedContent getPlantillaImportarIce() {
		try {
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-importarIce.xls");
			
			return AppJsfUtil.downloadFile(new File(path), "FALECPV-importarIce.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getPlantillaImportarIceNovedades() {
		try {
			
						
			return AppJsfUtil.downloadFile(fileIces, "FALECPV-importarIce.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void handleUpload_Ice(FileUploadEvent event) throws IOException {
			
			try {
				existeNovedadesIce = false;
				renderResultadoImportIce = false;
				
				UploadedFile uploadedFile = event.getFile();
				nombreFileIce = uploadedFile.getFileName();
				File parent = new File("uploads");
				parent.mkdirs();
				fileIces = new File(parent, nombreFileIce);
				FileUtils.writeByteArrayToFile(fileIces, uploadedFile.getContent());
				
			} catch (Exception e) {
				e.printStackTrace();
				AppJsfUtil.addErrorMessage("frmImportIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
			
	}
	
	public void importarIce() {
		
		try {
			
			existeNovedadesIce = false;
			renderResultadoImportIce = false;
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileIces));
			HSSFSheet sheet=wb.getSheetAt(0);
			
			List<Ice> importIceList = new ArrayList<>();
			int fila=3;
			conti1:while(true) {
				HSSFRow row = sheet.getRow(fila);
				if(row==null) break;
				
				Ice ice = new Ice();
					
					int col = 0;
					
					ice.setFila(fila);
					
					HSSFCell cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							ice.setCodigo(cell.getStringCellValue());
						} catch (Exception e) {
							ice.setError(true);
							ice.setNovedad("FORMATO CODIGO ERROR");
							importIceList.add(ice);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							ice.setDescripcion(cell.getStringCellValue());
						} catch (Exception e) {
							ice.setError(true);
							ice.setNovedad("FORMATO DESCCRIPCION ERROR");
							importIceList.add(ice);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							ice.setTarifaadvalorem(cell.getStringCellValue());
							
						} catch (Exception e) {
							ice.setError(true);
							ice.setNovedad("FORMATO TARIFA AD VALOREM ERROR");
							importIceList.add(ice);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							ice.setValor(BigDecimal.valueOf(cell.getNumericCellValue()));
							
						} catch (Exception e) {
							ice.setError(true);
							ice.setNovedad("FORMATO VALOR ERROR");
							importIceList.add(ice);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					importIceList.add(ice);
				fila++;
				
			}
			
			// validacion formato
			validarFormatoImportIce(importIceList);
			
			// cargar los datos
			importIceList = iceServicio.getIceDao().importarProductoFacade(importIceList,AppJsfUtil.getEstablecimiento().getEmpresa(), AppJsfUtil.getUsuario().getIdusuario());
			
			
			// colocar los errores en el archivo
			for (Ice p : importIceList) {
				if(p.isError()) {
					
					// color
					HSSFCellStyle myStyle = wb.createCellStyle();
					myStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
					myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					
					// coloca el error
					HSSFRow row = sheet.getRow(p.getFila());
					HSSFCell cell = row.createCell(4);
					
					cell.setCellValue(p.getNovedad());
					cell.setCellStyle(myStyle);
					
					// pinta de rojo la 1era celda
					cell = row.getCell(0);
					if(cell==null) cell = row.createCell(0);
				
					
				}
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(fileIces);
			wb.write(out);
			out.close();
			
			// existe novedades 
			existeNovedadesIce = importIceList.stream().filter(x->x.isError()==true).count()>0?true:false;
			renderResultadoImportIce = true;
			if(existeNovedadesIce) {
				byte[] file = FileUtils.readFileToByteArray(fileIces);
				fileIces = File.createTempFile("FALECPV-importarIce", ".xls");
				FileUtils.writeByteArrayToFile(fileIces, file);
			}
			
			consultarIce();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	
		
		
	}
	
	
	private void validarFormatoImportIce(List<Ice> lista) throws DaoException {
		
	
		conti1:for (Ice ice : lista) {
			 
			if(ice.isError()) continue conti1;
			
			// datos obligatorios
			if(ice.getCodigo()==null || ice.getCodigo().trim().length()==0) {
				ice.setError(true);
				ice.setNovedad(ice.getNovedad()!=null?ice.getNovedad().concat(", CAMPO CODIGO OBLIGATORIO"):"CAMPO CODIGO OBLIGATORIO");
			}
			if(ice.getDescripcion()==null || ice.getDescripcion().trim().length()==0) {
				ice.setError(true);
				ice.setNovedad(ice.getNovedad()!=null?ice.getNovedad().concat(", CAMPO DESCRIPCION OBLIGATORIO"):"CAMPO DESCRIPCION OBLIGATORIO");
			}
			if((ice.getTarifaadvalorem()==null || ice.getTarifaadvalorem().trim().length()==0)) {
				ice.setError(true);
				ice.setNovedad(ice.getNovedad() != null
						? ice.getNovedad()
								.concat(", UNO DE LOS CAMPOS TARIFA AD VALOREM Y TARIFA ESPECIFICA SON OBLIGATORIOS")
						: "UNO DE LOS CAMPOS TARIFA AD VALOREM Y TARIFA ESPECIFICA SON OBLIGATORIOS");
			}
			
			if(ice.getTarifaadvalorem()==null) {
				ice.setError(true);
				ice.setNovedad(ice.getNovedad()!=null?ice.getNovedad().concat(", CAMPO TARIFA VALOREM OBLIGATORIO"):"CAMPO TARIFA VALOREM OBLIGATORIO");
			}
			
			if(ice.getValor()==null) {
				ice.setError(true);
				ice.setNovedad(ice.getNovedad() != null
						? ice.getNovedad()
								.concat(", EL CAMPO VALOR ES OBLIGATORIO")
						: "EL CAMPO VALOR ES OBLIGATORIO");
			}
			
			if(ice.getCodigo()!=null) {
				
				if(iceServicio.getIceDao().existeCodigo(ice.getCodigo(), ice.getIdice(),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
					ice.setError(true);
					ice.setNovedad(ice.getNovedad()!=null?ice.getNovedad().concat(", CAMPO CODIGO  YA EXISTE"):"CAMPO CODIGO  YA EXISTE");
				}
				
			}
			
		}
	}
	
	@SuppressWarnings("unused")
	public boolean validaTarEspecif(String valor) 
	{
		//valida tarifa valorem
		boolean flag=false;
		int size= valor.length();
		
		try {
			BigDecimal tarifa=new BigDecimal(valor);
			
			flag= true;
		} catch (NumberFormatException e) {
			
		}
		return flag;
		
	}
	
	public List<Ice> getIceAllList() {
		return iceAllList;
	}

	public void setIceAllList(List<Ice> iceAllList) {
		this.iceAllList = iceAllList;
	}

	public Ice getIceSelected() {
		return iceSelected;
	}

	public void setIceSelected(Ice iceSelected) {
		this.iceSelected = iceSelected;
	}

	public List<Ice> getIceAllFilterList() {
		return iceAllFilterList;
	}

	public void setIceAllFilterList(List<Ice> iceAllFilterList) {
		this.iceAllFilterList = iceAllFilterList;
	}

	public String getNombreFileIce() {
		return nombreFileIce;
	}

	public void setNombreFileIce(String nombreFileIce) {
		this.nombreFileIce = nombreFileIce;
	}

	public boolean isExisteNovedadesIce() {
		return existeNovedadesIce;
	}

	public void setExisteNovedadesIce(boolean existeNovedadesIce) {
		this.existeNovedadesIce = existeNovedadesIce;
	}



	public boolean isRenderResultadoImportIce() {
		return renderResultadoImportIce;
	}

	public void setRenderResultadoImportIce(boolean renderResultadoImportIce) {
		this.renderResultadoImportIce = renderResultadoImportIce;
	}

	public File getFileIces() {
		return fileIces;
	}

	public void setFileIces(File fileIces) {
		this.fileIces = fileIces;
	}
	

}
