/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.guiarem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.GuiaRemisionServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class GuiaRemCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5481608094276437109L;

	@EJB
	private GuiaRemisionServicio guiaRemisionServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> guiaRemisionList;
	private Cabecera guiaRemisionSelected;
	private List<Destinatario> destinatarioList;
	private Destinatario destinatarioSelected;
	private List<Detalledestinatario> detalledestinatarioList;
	private GuiaRemFormCtrl guiaRemFormCtrl;
	private boolean seleccion = false;
	
	/**
	 * 
	 */
	public GuiaRemCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -60);
			criterioBusqueda = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		seleccion = false;
		AppJsfUtil.limpiarFiltrosDataTable("formMain:guiaRemDT");
		guiaRemisionList = null;
		destinatarioList = null;
		destinatarioList = guiaRemisionServicio.getGRByDateCriteria(establecimientoMain.getIdestablecimiento(), desde, hasta, criterioBusqueda,estado);
	}

	@Override
	public void buscar() {
		try {
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(guiaRemisionSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			String analisis = cabeceraServicio.analizarEstadoComprobante(guiaRemisionSelected.getIdcabecera(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraServicio.anularGuiaRemisionFacade(guiaRemisionSelected.getIdcabecera());
			consultar();
			guiaRemisionSelected = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaGuiaRemision(){
		try {
			
			guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
			guiaRemFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			guiaRemFormCtrl.nuevaGuiaRemision();
			
			return "./guiaRemForm.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public String editarGuiaRem(String idGuiaRem) {
		try {
			
			GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
			guiaRemFormCtrl.setEstablecimientoMain(this.establecimientoMain);
			String editar = guiaRemFormCtrl.editar(idGuiaRem);
			
			if(editar==null) {
				return "./guiaRemForm.jsf?faces-redirect=true";
			}
			
			AppJsfUtil.addErrorMessage("formMain","ERROR",editar);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public StreamedContent getFileGR() {
		try {
			
			if(destinatarioList==null || destinatarioList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-GuiaRemision.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			
			//datos de la cabecera
			Row row = sheet.getRow(3);
			Cell cell = row.createCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			row = sheet.getRow(4);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(desde));
			
			row = sheet.getRow(5);
			cell = row.createCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(hasta));
			
			row = sheet.getRow(6);
			cell = row.createCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			
			int fila = 10;
			int filaDestinatario = 10;
			int filaDt = 10;
			
			guiaRemisionList = guiaRemisionServicio.getByDateCriteria(establecimientoMain.getIdestablecimiento(), desde, hasta, criterioBusqueda,estado);
			
			for (Cabecera gr : guiaRemisionList) {
				
				row = sheet.createRow(fila);
				int col = 0;
				
				cell = row.createCell(col++);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(gr.getNumdocumento()));
				
				cell = row.createCell(col++);
				cell.setCellValue(gr.getEstado());
				
				cell = row.createCell(col++);
				cell.setCellValue(gr.getTransportista().getIdentificacion());
				
				cell = row.createCell(col++);
				cell.setCellValue(gr.getTransportista().getRazonsocial());
				
				cell = row.createCell(col++);
				cell.setCellValue(gr.getDireccionpartida());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(gr.getFechainiciotransporte()));
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(gr.getFechafintransporte()));
				
				filaDestinatario = (fila+1);
				
				for (Destinatario	de : gr.getDestinatarioList()) {
					
					col = 7;
					
					row = sheet.getRow(filaDestinatario);
					if(row==null) {
						row = sheet.createRow(filaDestinatario);
					}
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getIdentificaciondestinatario());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getRazonsocialdestinatario());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getDirdestinatario());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getMotivotraslado());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getCodestabdestino());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getTipocomprobante()!=null?de.getTipocomprobante().getComprobante():"-");
					
					cell = row.createCell(col++);
					cell.setCellValue(ComprobanteHelper.formatNumDocumento(de.getNumdocsustento()));
					
					cell = row.createCell(col++);
					cell.setCellValue(FechaUtil.formatoFecha(gr.getFechaemision()));
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getRuta());
					
					cell = row.createCell(col++);
					cell.setCellValue(de.getDocaduanerounico());
					
					filaDt =  filaDestinatario;
					for (Detalledestinatario dt : de.getDetalledestinatarioList()) {
						
						col = 17;
						
						row = sheet.getRow(filaDt);
						if(row==null) {
							row = sheet.createRow(filaDt);
						}
						
						cell = row.createCell(col++);
						cell.setCellValue(dt.getCodigointerno());
						
						cell = row.createCell(col++);
						cell.setCellValue(dt.getCodigoadicional());
						
						cell = row.createCell(col++);
						cell.setCellValue(dt.getDescripcion());
						
						cell = row.createCell(col++);
						cell.setCellValue(dt.getCantidad().doubleValue());
						
						filaDt++;
						
					}
					
					filaDestinatario = filaDt;
					
				}
				
				if(filaDt>filaDestinatario) {
					fila = filaDt;
				}else {
					fila = filaDestinatario;
				}
				
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-GuiaRemision_" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public void changeSeleccion() {
		try {
			
			if(destinatarioList!=null) {
				for (Destinatario v : destinatarioList) {
					if(!v.getCabecera().getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && !v.getCabecera().getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
						v.setSeleccion(this.seleccion);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmarEnviar() {
		try {
			
			if(destinatarioList==null ||  destinatarioList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			
			for(String id : destinatarioList.stream().filter(x->x.isSeleccion()).map(x->x.getCabecera().getIdcabecera()).distinct().collect(Collectors.toList())) {
				Cabecera c = new Cabecera();
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				c.setIdcabecera(id);
				c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
				sriDispacher.queue_comprobanteSriDispacher(c);
				
				
			}
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FIRMAR Y ENVIAR COMPROBANTE SRI", msg.getString("mensaje.enviarComprobante"));
	        PrimeFaces.current().dialog().showMessageDynamic(message,true);
			
	        consultar();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void enviarEmail() {
		try {
			
			if(destinatarioList==null || destinatarioList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			for (Destinatario destinatario : destinatarioList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				if(destinatario.getCabecera().getEstado().equals(ComprobanteEstadoEnum.AUTORIZADO.toString())) {
					emailComprobanteServicio.enviarComprobanteFacade(null, null, null, destinatario.getCabecera().getIdcabecera(), null, null, null, null,true);
				}
			}
			AppJsfUtil.addInfoMessage("formMain", "OK", "LOS CORREOS HAN SIDO ENVIADOS CORRECTAMENTE, SOLO LOS COMPROBANTES QUE ESTAN EN ESTADO AUTORIZADO.");
	        consultar();
	        
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the guiaRemisionList
	 */
	public List<Cabecera> getGuiaRemisionList() {
		return guiaRemisionList;
	}

	/**
	 * @param guiaRemisionList the guiaRemisionList to set
	 */
	public void setGuiaRemisionList(List<Cabecera> guiaRemisionList) {
		this.guiaRemisionList = guiaRemisionList;
	}

	/**
	 * @return the guiaRemisionSelected
	 */
	public Cabecera getGuiaRemisionSelected() {
		return guiaRemisionSelected;
	}

	/**
	 * @param guiaRemisionSelected the guiaRemisionSelected to set
	 */
	public void setGuiaRemisionSelected(Cabecera guiaRemisionSelected) {
		this.guiaRemisionSelected = guiaRemisionSelected;
	}

	/**
	 * @return the guiaRemFormCtrl
	 */
	public GuiaRemFormCtrl getGuiaRemFormCtrl() {
		return guiaRemFormCtrl;
	}

	/**
	 * @param guiaRemFormCtrl the guiaRemFormCtrl to set
	 */
	public void setGuiaRemFormCtrl(GuiaRemFormCtrl guiaRemFormCtrl) {
		this.guiaRemFormCtrl = guiaRemFormCtrl;
	}

	/**
	 * @return the detalledestinatarioList
	 */
	public List<Detalledestinatario> getDetalledestinatarioList() {
		return detalledestinatarioList;
	}

	/**
	 * @param detalledestinatarioList the detalledestinatarioList to set
	 */
	public void setDetalledestinatarioList(List<Detalledestinatario> detalledestinatarioList) {
		this.detalledestinatarioList = detalledestinatarioList;
	}

	/**
	 * @return the destinatarioSelected
	 */
	public Destinatario getDestinatarioSelected() {
		return destinatarioSelected;
	}

	/**
	 * @param destinatarioSelected the destinatarioSelected to set
	 */
	public void setDestinatarioSelected(Destinatario destinatarioSelected) {
		this.destinatarioSelected = destinatarioSelected;
	}

	/**
	 * @return the destinatarioList
	 */
	public List<Destinatario> getDestinatarioList() {
		return destinatarioList;
	}

	/**
	 * @param destinatarioList the destinatarioList to set
	 */
	public void setDestinatarioList(List<Destinatario> destinatarioList) {
		this.destinatarioList = destinatarioList;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

}
