/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.servicio.IceServicio;
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

	private List<Ice> iceAllList;
	private List<Ice> iceAllFilterList;
	
	private Ice iceSelected;

	/**
	 * 
	 */
	public IceCtrl() {
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
			if (iceServicio.getIceDao().existeCodigo(iceSelected.getCodigo(), iceSelected.getIdice())) {
				AppJsfUtil.addErrorMessage("frmIce", "ERROR", "CODIGO DUPLICADO");
				iceAllList = null;
				consultarIce();
				return;
			}
			
			//guarda-edita
			iceSelected.setUpdated(new Date());
			iceSelected.setIdusuario(AppJsfUtil.getRemoteUser());
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
			iceSelected = new Ice();
			iceSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			iceSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
	
			AppJsfUtil.showModalRender("dlgIce", "frmIce");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIce", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @throws DaoException
	 */
	private void consultarIce() throws DaoException {
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
			cell.setCellValue(AppJsfUtil.getUsuario().getEstablecimiento().getNombrecomercial());
			
			// lista de IVA
			int fila = 9;
			
			for (Ice e : iceAllList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(e.getIdice());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(e.getCodigo());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(2);
				cell.setCellValue(e.getDescripcion());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(e.getTarifaadvalorem());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(e.getTarifaespecifica());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(5);
				cell.setCellValue(e.getValor().doubleValue());
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
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-iceList.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
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
	

}
