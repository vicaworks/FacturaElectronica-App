/**
 * 
 */
package com.vcw.falecpv.web.ctrl.kdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.KardexProductoServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.productos.InventarioCtrl;
import com.vcw.falecpv.web.ctrl.productos.ProductoCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;
import com.xpert.faces.utils.FacesUtils;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class KardexCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7747240311615335417L;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private KardexProductoServicio kardexProductoServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private List<KardexProducto> kardexProductoList;
	private List<Producto> productoList;
	private Producto productoSelected;
	private KardexProducto kardexProductoSelected;
	private String tipoRegistro;
	private String callMOdule;
	private String updateView;
	private String codProducto;
	private String formView;
	private Date fechaInicial;
	private Date fechaFinal;
	private ProductoCtrl productoCtrl;
	private InventarioCtrl inventarioCtrl;

	/**
	 * 
	 */
	public KardexCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarProductoForm();
			fechaFinal = new Date();
			fechaInicial = FechaUtil.agregarDias(fechaFinal, -90);
			
			Producto p = (Producto) FacesUtils.getFromSession("producto");
			if(p!=null) {
				// carga el producto desde pantalla de productos
				valoresRedirectProducto(p);
				// remueve la variable de session
				FacesUtils.removeFromSession("producto");
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	private void valoresRedirectProducto(Producto p) {
		try {
			
			Date fechaUltimoKardex = kardexProductoServicio.getKardexProductoDao()
					.getMaxFechaRegistro(p.getIdproducto(), p.getEstablecimiento().getIdestablecimiento());
			
			if (fechaUltimoKardex!=null) {
				
				fechaFinal = fechaUltimoKardex;
				fechaInicial = FechaUtil.agregarDias(fechaFinal, -30);
				
			}
			establecimientoMain = p.getEstablecimiento();
			codProducto = p.getCodigoprincipal();
			consultarProductoByCodBarra();
			consultarKardex();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void consultarProductoForm()throws DaoException{
		productoList = null;
		productoList = productoServicio.getProductoDao().getByQuery("PRODUCTO",EstadoRegistroEnum.ACTIVO, establecimientoMain.getIdestablecimiento());
	}
	
	public void cambioProducto() {
		try {
			codProducto = productoSelected.getCodigoprincipal();
			AppJsfUtil.updateComponente("formMain:intCodProducto");
			AppJsfUtil.updateComponente("formMain:pngKardexOpciones");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarKardex() throws DaoException{
		kardexProductoList = kardexProductoServicio.getKardexProductoDao().consultar(productoSelected.getIdproducto(),
				establecimientoMain.getIdestablecimiento(), fechaInicial, fechaFinal);
	}
	
	
	private void consultarProductoByCodBarra() throws DaoException{
		if(codProducto!=null) {
			productoSelected = kardexProductoServicio.getProducto(null, establecimientoMain.getIdestablecimiento(), codProducto);
		}else if(productoSelected!=null) {
			productoSelected = kardexProductoServicio.getProducto(productoSelected.getIdproducto(), establecimientoMain.getIdestablecimiento(), null);
		}
		if(productoSelected==null) {
			AppJsfUtil.addErrorMessage("formMain:somFrmListaProducto", "REQUERIDO");
			AppJsfUtil.addErrorMessage("formMain", "ERROR",codProducto!=null?("NO EXISTE PRODUCTO o CODIGO SELECCIONADO : " + codProducto):"NO EXISTE COD PRODUCTO.");
			return;
		}
	}
	
	@Override
	public void refrescar() {
		try {
			
			consultarProductoForm();
			consultarProductoByCodBarra();
			if(AppJsfUtil.existErrors()) return;
			consultarKardex();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void limpiar() {
		try {
			consultarProductoForm();
			codProducto=null;
			kardexProductoList = null;
			productoSelected = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}



	@Override
	public void guardar() {
		try {
			
			kardexProductoSelected.setUpdated(new Date());
			productoSelected = productoServicio.getProductoDao().cargar(kardexProductoSelected.getProducto().getIdproducto());
			
			// valida si existe saldo
			if(productoSelected.getStock().doubleValue()<kardexProductoSelected.getCantidad().doubleValue() && kardexProductoSelected.getTiporegistro().equals("S")) {
				AppJsfUtil.addErrorMessage(formView, "ERROR", "NO EXISTE SALDO PARA EL EGRESO, SALDO ACTUAL : " + productoSelected.getStock());
				return;
			}
			
			kardexProductoSelected = kardexProductoServicio.registrarKardexFacade(kardexProductoSelected);
			
			
			switch (callMOdule) {
			case "KARDEX":
				consultarKardex();
				
				break;
			case "PRODUCTO" :
				
				productoCtrl.consultarProducto();
				break;
			case "INVENTARIO":
				inventarioCtrl.buscarDispacher();
				break;
			default:
				break;
			}
			
			switch (tipoRegistro) {
				case "E":
				
				AppJsfUtil.hideModal("dlgEntradaInventario");
				break;

			default:
				AppJsfUtil.hideModal("dlgSalidaInventario");
				break;
			}
			
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(formView, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}




	@Override
	public void nuevo() {
		try {
			
			kardexProductoSelected = new KardexProducto();
			kardexProductoSelected.setTiporegistro(tipoRegistro);
			kardexProductoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			kardexProductoSelected.setFecharegistro(new Date());
			kardexProductoSelected.setFechacompra(new Date());
			kardexProductoSelected.setCantidad(BigDecimal.ZERO);
			kardexProductoSelected.setCostounitario(BigDecimal.ZERO);
			kardexProductoSelected.setCostototal(BigDecimal.ZERO);
			kardexProductoSelected.setEstablecimiento(establecimientoMain);
			kardexProductoSelected.setProducto(productoSelected);
			kardexProductoSelected.setSaldo(BigDecimal.ZERO);
			kardexProductoSelected.setCostounitario(productoSelected.getPreciounitario());
			
			switch (callMOdule) {
			case "KARDEX":
				
				consultarProductoByCodBarra();
				kardexProductoSelected.setProducto(productoSelected);
				
				break;
			case "PRODUCTO":
				
				kardexProductoSelected.setProducto(kardexProductoServicio.getProducto(productoSelected.getIdproducto(), null, null));
				
				break;
			case "INVENTARIO":
				
				kardexProductoSelected.setProducto(kardexProductoServicio.getProducto(productoSelected.getIdproducto(), null, null));
				
				break;
			default:
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE MODULE CALL");
				return;
			}
			
			switch (kardexProductoSelected.getTiporegistro()) {
			case "E":
				
				AppJsfUtil.showModalRender("dlgEntradaInventario", "frmKardex");
				break;

			default:
				AppJsfUtil.showModalRender("dlgSalidaInventario", "frmKardexSalida");
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileKardex() {
		try {
			
			if(kardexProductoList==null || kardexProductoList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-kardexProducto.xls");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llenaa hoja 1 del archivo
			HSSFSheet sheet=wb.getSheetAt(0);
			
			// datos de la cabecera
			HSSFRow row = sheet.getRow(3);
			HSSFCell cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(fechaInicial));
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(fechaFinal));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			row = sheet.getRow(6);
			cell = row.createCell(1);
			cell.setCellValue(productoSelected.getNombregenerico());
			
			row = sheet.getRow(7);
			cell = row.createCell(1);
			cell.setCellValue(productoSelected.getCodigoprincipal());
			
			row = sheet.getRow(8);
			cell = row.createCell(1);
			cell.setCellValue(productoSelected.getStock().doubleValue());
			
			int fila = 11;
			
			for (KardexProducto k : kardexProductoList) {
				
				row = sheet.createRow(fila);
				
				int col =0;
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getIdkardexproducto());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFechaHora(k.getUpdated()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(k.getFecharegistro()));
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getObservacion());
				
				
				cell = row.createCell(col++);
				if(k.getFechafabricacion()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechafabricacion()));
				}
				
				cell = row.createCell(col++);
				if(k.getFechacompra()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechacompra()));
				}
				
				cell = row.createCell(col++);
				if(k.getFechavencimiento()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechavencimiento()));
				}
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getTiporegistro().equals("E")?"ENTRADA":"SALIDA");
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCantidad().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCostounitario().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCostototal().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciouno().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciodos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciotres().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getSaldo().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(usuarioServicio.consultarByPk(k.getIdusuario()).getNombre());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(k.getUpdated()));
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-kardexProducto.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	
	public StreamedContent getFileKardexAll() {
		try {
			
			// consulta todos los productos en un rango de fechas
			List<KardexProducto> kardexProductoTodosList = kardexProductoServicio.getKardexProductoDao().consultar(establecimientoMain.getIdestablecimiento(), fechaInicial, fechaFinal);
			List<Usuario> usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.TODOS);
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-kardexProductoTodos.xls");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llenaa hoja 1 del archivo
			HSSFSheet sheet=wb.getSheetAt(0);
			
			// datos de la cabecera
			HSSFRow row = sheet.getRow(3);
			HSSFCell cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(fechaInicial));
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(fechaFinal));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			int fila = 8;
			
			for (KardexProducto k : kardexProductoTodosList) {
				
				row = sheet.createRow(fila);
				
				int col =0;
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getIdkardexproducto());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFechaHora(k.getUpdated()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(k.getFecharegistro()));
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getObservacion());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getNombregenerico());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getCodigoprincipal());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getStock().doubleValue());
				
				cell = row.createCell(col++);
				if(k.getFechafabricacion()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechafabricacion()));
				}
				
				cell = row.createCell(col++);
				if(k.getFechacompra()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechacompra()));
				}
				
				cell = row.createCell(col++);
				if(k.getFechavencimiento()!=null) {
					cell.setCellValue(FechaUtil.formatoFecha(k.getFechavencimiento()));
				}
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getTiporegistro().equals("E")?"ENTRADA":"SALIDA");
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCantidad().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCostounitario().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getCostototal().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciouno().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciodos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getProducto().getPreciotres().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(k.getSaldo().doubleValue());
				
				cell = row.createCell(col++);
				Usuario u = usuarioList.stream().filter(x->x.getIdusuario().equals(k.getIdusuario())).collect(Collectors.toList()).get(0);
				cell.setCellValue(u.getNombre());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(k.getUpdated()));
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-kardexProductoTodos.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public boolean getCompararValoresDouble(Double v1,Double v2) {
		if(v1<=v2) return true;
		
		return false;
	}
	
	/**
	 * @return the kardexProductoSelected
	 */
	public KardexProducto getKardexProductoSelected() {
		return kardexProductoSelected;
	}

	/**
	 * @param kardexProductoSelected the kardexProductoSelected to set
	 */
	public void setKardexProductoSelected(KardexProducto kardexProductoSelected) {
		this.kardexProductoSelected = kardexProductoSelected;
	}

	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 * @return the callMOdule
	 */
	public String getCallMOdule() {
		return callMOdule;
	}

	/**
	 * @param callMOdule the callMOdule to set
	 */
	public void setCallMOdule(String callMOdule) {
		this.callMOdule = callMOdule;
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
	 * @return the kardexProductoList
	 */
	public List<KardexProducto> getKardexProductoList() {
		return kardexProductoList;
	}

	/**
	 * @param kardexProductoList the kardexProductoList to set
	 */
	public void setKardexProductoList(List<KardexProducto> kardexProductoList) {
		this.kardexProductoList = kardexProductoList;
	}

	/**
	 * @return the productoList
	 */
	public List<Producto> getProductoList() {
		return productoList;
	}

	/**
	 * @param productoList the productoList to set
	 */
	public void setProductoList(List<Producto> productoList) {
		this.productoList = productoList;
	}

	/**
	 * @return the productoSelected
	 */
	public Producto getProductoSelected() {
		return productoSelected;
	}

	/**
	 * @param productoSelected the productoSelected to set
	 */
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	/**
	 * @return the codProducto
	 */
	public String getCodProducto() {
		return codProducto;
	}

	/**
	 * @param codProducto the codProducto to set
	 */
	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	/**
	 * @return the formView
	 */
	public String getFormView() {
		return formView;
	}

	/**
	 * @param formView the formView to set
	 */
	public void setFormView(String formView) {
		this.formView = formView;
	}

	/**
	 * @return the fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
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
	 * @return the inventarioCtrl
	 */
	public InventarioCtrl getInventarioCtrl() {
		return inventarioCtrl;
	}

	/**
	 * @param inventarioCtrl the inventarioCtrl to set
	 */
	public void setInventarioCtrl(InventarioCtrl inventarioCtrl) {
		this.inventarioCtrl = inventarioCtrl;
	}
	

}
