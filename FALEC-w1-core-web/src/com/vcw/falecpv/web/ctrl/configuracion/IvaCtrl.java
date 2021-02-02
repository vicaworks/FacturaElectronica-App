/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
/**
 * @author Isabel Lobato
 *
 */
@Named
@ViewScoped
public class IvaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -695740557750314887L;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;

	private List<Iva> ivaAllList;
	
	private Iva ivaSelected;
    public final static int defecto=1;
			
	
	@PostConstruct
	public void init() {
		try {
			consultarIva();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	/**
	 * 
	 */
	public IvaCtrl() {
	}

	/**
	 *
	 */
	@Override
	public void refrescar() {
		try {
			consultarIva();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			//verifica dependencias
			if(ivaServicio.tieneDependenciasIva(ivaSelected.getIdiva())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EXISTE REFERENCIAS");
				return;
			}
			
			ivaServicio.eliminar(ivaSelected);
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ELIMINADO CORRECTAMENTE.");
			consultarIva();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
	}

	@Override
	public void guardar() {
		
		try {
			Iva ivaUpdate= new Iva();
			
			//validar unico codigo iva
			if (ivaServicio.getIvaDao().existeCodigo(ivaSelected.getCodigo(), ivaSelected.getIdiva(),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmIva", "ERROR", "CODIGO DUPLICADO");
				ivaAllList = null;
				consultarIva();
				return;
			}
			//validar unico valor iva!
			if(!ivaSelected.getValor().equals(BigDecimal.ZERO)) {
					if (ivaServicio.getIvaDao().existeValor(ivaSelected.getValor().abs(), ivaSelected.getIdiva(),AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
						AppJsfUtil.addErrorMessage("frmIva", "ERROR", "VALOR DUPLICADO");
						ivaAllList = null;
						consultarIva();
						return;
					}						
			}
			//valida si el valor es x defecto 
			if (ivaSelected.getDefecto() == 1) {
				ivaUpdate = ivaServicio.getIvaDao().existeValorDefecto(defecto, ivaSelected.getIdiva(),
						AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
				if (ivaUpdate != null) {
					// actualizo objeto
					ivaUpdate.setDefecto(0);
					ivaServicio.actualizar(ivaUpdate);
				}
			}	
			
			//guarda-edita
			ivaSelected.setUpdated(new Date());
			ivaSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			ivaSelected = ivaServicio.guardar(ivaSelected);
			
			ivaAllList = null;
			consultarIva();
			AppJsfUtil.addInfoMessage("frmIva", "OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
										
		} catch (DaoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIva", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			}
		
	}

	@Override
	public void editar() {
		try {
			
			AppJsfUtil.showModalRender("dlgIva", "frmIva");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIva", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			
			nuevoIva();
			AppJsfUtil.showModalRender("dlgIva", "frmIva");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIva", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevoIva() {
		
		ivaSelected = new Iva();
		ivaSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
		ivaSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		ivaSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		ivaSelected.setCodigoIva("2");
		
	}
	
	public void nuevoForm() {
		try {
			nuevoIva();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmIva", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @throws DaoException
	 */
	private void consultarIva() throws DaoException {
		ivaAllList= new ArrayList<>();
		ivaAllList  = ivaServicio.getIvaDao().getAllIva(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	
	/**
	 * @return
	 */
	public StreamedContent getFileIva()  {
		try {
			
			if(ivaAllList==null || ivaAllList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-ivaList.xls");
			
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
			
			for (Iva e : ivaAllList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(e.getIdiva());
//				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(e.getCodigo());
				
				cell = row.createCell(2);
				cell.setCellValue(e.getEstado().equals("I")?"INACTIVO":"ACTIVO");
				
				cell = row.createCell(3);
				cell.setCellValue(e.getPorcentaje());
				
				cell = row.createCell(4);
				cell.setCellValue(e.getValor().doubleValue());
				
				cell = row.createCell(5);
				cell.setCellValue(e.getDefecto()==1?"SI":"NO");
				
				cell = row.createCell(6);
				cell.setCellValue(e.getLabelfactura());
				
				cell = row.createCell(7);
				cell.setCellValue(e.getOrdenfactura());
				
				cell = row.createCell(8);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(e.getIdusuario()).getNombre());
				
				cell = row.createCell(9);
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
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-ivaList" +  AppJsfUtil.getEstablecimiento().getEmpresa().getNombrecomercial() + ".xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}

	public List<Iva> getIvaAllList() {
		return ivaAllList;
	}

	public void setIvaAllList(List<Iva> ivaAllList) {
		this.ivaAllList = ivaAllList;
	}

	public Iva getIvaSelected() {
		return ivaSelected;
	}

	public void setIvaSelected(Iva ivaSelected) {
		this.ivaSelected = ivaSelected;
	}

	

}
