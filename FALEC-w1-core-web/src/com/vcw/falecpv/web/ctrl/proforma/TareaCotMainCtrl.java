/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.CotizacionServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.TareacabeceraServicio;
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
@SessionScoped
public class TareaCotMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8652749472314923747L;
	
	@EJB
	private CotizacionServicio cotizacionServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private TareacabeceraServicio tareacabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	private List<Tareacabecera> tareacabeceraList;
	private Tareacabecera tareacabeceraSelected;
	private String estadoSeleccion = "PENDIENTE-VENCIDO";
	private String prioridadSeleccion;
	private List<Usuario> usuarioList;
	private Usuario usuarioSeleccion;
	private List<Cliente> clienteList;
	private Cliente clienteSeleccion;
	private String numCotizacion;
	private Integer totalCerrado;
	private Integer totalPendiente;
	private Integer totalVencido;
	private boolean disableUsuario=false;
	

	/**
	 * 
	 */
	public TareaCotMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarUsuario();
			usuarioSeleccion = AppJsfUtil.getUsuario();
			consultarCliente();
			consultar();
			disableUsuario = parametroGenericoEmpresaServicio.consultarParametroEmpresa(
					PGEmpresaEnum.COTIZACION_VENDEDOR_VISUALIZACION, TipoRetornoParametroGenerico.BOOLEAN,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			if(cotizacionServicio.tienePerfilAdministrador(AppJsfUtil.getUsuario().getIdusuario())) {
				disableUsuario = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	public void consultarUsuario() throws DaoException{
		usuarioList = usuarioServicio.getUsuarioDao().getByEmpresaEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarCliente() throws DaoException{
		clienteList = cabeceraServicio.getClienteByTipoComprobante(establecimientoMain.getEmpresa().getIdempresa(), GenTipoDocumentoEnum.COTIZACION);
	}
	
	@Override
	public void buscar() {
		try {
			
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public Establecimiento getEstablecimiento() {
		return ((CotizacionCtrl)AppJsfUtil.getManagedBean("cotizacionCtrl")).getEstablecimientoMain();
	}
	
	public void consultar() throws DaoException {
		
		tareacabeceraServicio.getTareacabeceraDao().actualizarEstadoVencido(getEstablecimiento().getEmpresa().getIdempresa());
		
		AppJsfUtil.limpiarFiltrosDataTable("fomrMain:tarea:pvDosDT");
		
		List<String> estadoList = null;
		
		if(estadoSeleccion!=null) {
			estadoList = new ArrayList<>();
			switch (estadoSeleccion) {
			case "PENDIENTE-VENCIDO":
				estadoList.add("PENDIENTE");
				estadoList.add("VENCIDO");
				break;

			default:
				estadoList.add(estadoSeleccion);
				break;
			}
		}
		
		List<Integer> prioridadList = null;
		
		if(prioridadSeleccion!=null) {
			prioridadList = new ArrayList<>();
			switch (prioridadSeleccion) {
			case "IMPORTANTE":
				prioridadList.add(1);
				break;
			case "IMPORTANTE-ALTA":
				prioridadList.add(1);
				prioridadList.add(2);
				break;
			case "ALTA":
				prioridadList.add(2);
				break;
			case "MEDIA":
				prioridadList.add(3);
				break;
			case "BAJA":
				prioridadList.add(4);
				break;
			default:
				break;
			}
		}
		
		
		List<String> idClienteList = null;
		if(clienteSeleccion!=null) {
			idClienteList = new ArrayList<>();
			idClienteList.add(clienteSeleccion.getIdcliente());
		}else {
			idClienteList = cabeceraServicio
					.getClienteByTipoComprobante(getEstablecimiento().getEmpresa().getIdempresa(),
							GenTipoDocumentoEnum.COTIZACION)
					.stream().map(x -> x.getIdcliente()).collect(Collectors.toList());
		}
		
		
		tareacabeceraList =	tareacabeceraServicio.getTareacabeceraDao().consultarByOpciones(getEstablecimiento().getEmpresa().getIdempresa(),
								establecimientoMain==null?null:getEstablecimiento().getIdestablecimiento(), estadoList, prioridadList,
								usuarioSeleccion == null ? null : usuarioSeleccion.getIdusuario(), idClienteList, numCotizacion,
								GenTipoDocumentoEnum.COTIZACION);
		
		totalizar();
		consultarCliente();
		consultarUsuario();
	}
	
	private void totalizar() {
		if(tareacabeceraList!=null) {
			
			totalCerrado = (int)tareacabeceraList.stream().filter(x->x.getEstado().equals("CERRADO")).count();
			totalPendiente = (int)tareacabeceraList.stream().filter(x->x.getEstado().equals("PENDIENTE")).count();
			totalVencido = (int)tareacabeceraList.stream().filter(x->x.getEstado().equals("VENCIDO")).count();
			
		}
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(tareacabeceraList==null || tareacabeceraList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existen datos.", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CotTarea.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			int fila = 7;
			for (Tareacabecera v : tareacabeceraList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(v.getFechalimite()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getPrioridad());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getUsuario().getNombrepantalla());
				
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getEtiqueta().getEtiqueta());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getDescripcion());
				
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(TextoUtil.leftPadTexto(v.getCabecera().getEstablecimiento().getCodigoestablecimiento(),3,"0"));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(v.getCabecera().getNumdocumento()));
				
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(v.getCabecera().getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCabecera().getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCabecera().getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(v.getCabecera().getCliente().getRazonsocial());
				
				fila++;
			}
			fila++;
			// totales
			rowCliente = sheet.createRow(fila);
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-CotTarea-" +  establecimientoMain.getNombrecomercial() + ".xlsx");
			
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
	 * @return the tareacabeceraList
	 */
	public List<Tareacabecera> getTareacabeceraList() {
		return tareacabeceraList;
	}

	/**
	 * @param tareacabeceraList the tareacabeceraList to set
	 */
	public void setTareacabeceraList(List<Tareacabecera> tareacabeceraList) {
		this.tareacabeceraList = tareacabeceraList;
	}

	/**
	 * @return the tareacabeceraSelected
	 */
	public Tareacabecera getTareacabeceraSelected() {
		return tareacabeceraSelected;
	}

	/**
	 * @param tareacabeceraSelected the tareacabeceraSelected to set
	 */
	public void setTareacabeceraSelected(Tareacabecera tareacabeceraSelected) {
		this.tareacabeceraSelected = tareacabeceraSelected;
	}

	/**
	 * @return the estadoSeleccion
	 */
	public String getEstadoSeleccion() {
		return estadoSeleccion;
	}

	/**
	 * @param estadoSeleccion the estadoSeleccion to set
	 */
	public void setEstadoSeleccion(String estadoSeleccion) {
		this.estadoSeleccion = estadoSeleccion;
	}

	/**
	 * @return the prioridadSeleccion
	 */
	public String getPrioridadSeleccion() {
		return prioridadSeleccion;
	}

	/**
	 * @param prioridadSeleccion the prioridadSeleccion to set
	 */
	public void setPrioridadSeleccion(String prioridadSeleccion) {
		this.prioridadSeleccion = prioridadSeleccion;
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
	 * @return the usuarioSeleccion
	 */
	public Usuario getUsuarioSeleccion() {
		return usuarioSeleccion;
	}

	/**
	 * @param usuarioSeleccion the usuarioSeleccion to set
	 */
	public void setUsuarioSeleccion(Usuario usuarioSeleccion) {
		this.usuarioSeleccion = usuarioSeleccion;
	}

	/**
	 * @return the clienteList
	 */
	public List<Cliente> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList the clienteList to set
	 */
	public void setClienteList(List<Cliente> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * @return the clienteSeleccion
	 */
	public Cliente getClienteSeleccion() {
		return clienteSeleccion;
	}

	/**
	 * @param clienteSeleccion the clienteSeleccion to set
	 */
	public void setClienteSeleccion(Cliente clienteSeleccion) {
		this.clienteSeleccion = clienteSeleccion;
	}

	/**
	 * @return the numCotizacion
	 */
	public String getNumCotizacion() {
		return numCotizacion;
	}

	/**
	 * @param numCotizacion the numCotizacion to set
	 */
	public void setNumCotizacion(String numCotizacion) {
		this.numCotizacion = numCotizacion;
	}

	/**
	 * @return the totalCerrado
	 */
	public Integer getTotalCerrado() {
		return totalCerrado;
	}

	/**
	 * @param totalCerrado the totalCerrado to set
	 */
	public void setTotalCerrado(Integer totalCerrado) {
		this.totalCerrado = totalCerrado;
	}

	/**
	 * @return the totalPendiente
	 */
	public Integer getTotalPendiente() {
		return totalPendiente;
	}

	/**
	 * @param totalPendiente the totalPendiente to set
	 */
	public void setTotalPendiente(Integer totalPendiente) {
		this.totalPendiente = totalPendiente;
	}

	/**
	 * @return the totalVencido
	 */
	public Integer getTotalVencido() {
		return totalVencido;
	}

	/**
	 * @param totalVencido the totalVencido to set
	 */
	public void setTotalVencido(Integer totalVencido) {
		this.totalVencido = totalVencido;
	}

	/**
	 * @return the disableUsuario
	 */
	public boolean isDisableUsuario() {
		return disableUsuario;
	}

	/**
	 * @param disableUsuario the disableUsuario to set
	 */
	public void setDisableUsuario(boolean disableUsuario) {
		this.disableUsuario = disableUsuario;
	}

}
