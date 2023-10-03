/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
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
public class RepClienteProductosCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3057955400813916401L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;

	
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQuerieList;
	
	/**
	 * 
	 */
	public RepClienteProductosCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -180);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void buscar() {
		try {
			consultar();
			//populateChart();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultar()throws DaoException{
		ventasQuerieList = consultaVentaServicio.getVentasProductoByNombreCliente(establecimientoMain!=null?establecimientoMain.getIdestablecimiento():null,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), 
				desde, 
				hasta, 
				criterioBusqueda);
	}
	
	public List<String> completeClienteCriterio(String query) {
		try {
			
			List<Cliente> lista = clienteServicio.getClienteDao().consultarClientes(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), query); 
			
			if(!lista.isEmpty()) {
				return lista.stream().map(x->x.getRazonsocial()).collect(Collectors.toList());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return new ArrayList<>();
    }
	
	public StreamedContent getFileResumen() {
		try {
			
			if(ventasQuerieList==null || ventasQuerieList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-VentaProductos.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(),3,"0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(criterioBusqueda);
			
			int fila = 10;
			
			for (VentasQuery v : ventasQuerieList) {
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCodigoprincipal());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getNombregenerico());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCantidad().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getPreciototalsinimpuesto().doubleValue());
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-VentaProductos-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
			
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
	 * @return the desde
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @param desde the desde to set
	 */
	public void setDesde(Date desde) {
		this.desde = desde;
	}

	/**
	 * @return the hasta
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * @param hasta the hasta to set
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the ventasQuerieList
	 */
	public List<VentasQuery> getVentasQuerieList() {
		return ventasQuerieList;
	}

	/**
	 * @param ventasQuerieList the ventasQuerieList to set
	 */
	public void setVentasQuerieList(List<VentasQuery> ventasQuerieList) {
		this.ventasQuerieList = ventasQuerieList;
	}		
}
