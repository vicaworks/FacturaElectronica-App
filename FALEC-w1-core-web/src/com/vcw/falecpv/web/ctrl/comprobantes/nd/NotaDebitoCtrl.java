/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nd;

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
import org.apache.poi.ss.usermodel.CellType;
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
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.NotaDebitoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class NotaDebitoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1641640201539263798L;
	
	@EJB
	private NotaDebitoServicio notaDebitoServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> notDebitoList;
	private Cabecera notDebitoSelected;
	private boolean seleccion = false;
	
	/**
	 * 
	 */
	public NotaDebitoCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
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
		AppJsfUtil.limpiarFiltrosDataTable("formMain:notDebitoDT");
		notDebitoList = null;
		notDebitoList =  notaDebitoServicio.getByCriteria(desde, hasta, criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento(), estado);
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

			if(notDebitoSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			String analisis = cabeceraServicio.analizarEstadoComprobante(notDebitoSelected.getIdcabecera(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraServicio.anularGuiaRemisionFacade(notDebitoSelected.getIdcabecera());
			consultar();
			notDebitoSelected = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevanotaDebito(){
		try {
			
			NotaDebitoFrmCtrl notaDebitoFrmCtrl = (NotaDebitoFrmCtrl) AppJsfUtil.getManagedBean("notaDebitoFrmCtrl");
			notaDebitoFrmCtrl.nuevaNotDebito();
			
			return "./notaDebitoForm.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileNDDetalle() {
		try {
			
			if(notDebitoList==null || notDebitoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotDebitoDet.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 10;
			int filaDetalle = 10;
			int filaPago = 10;
			for (Cabecera lc : notDebitoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());

				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());
				
				filaDetalle = fila;
				filaPago = fila;
				for (Motivo d : lc.getMotivoList()) {
					
					col=11;
					rowCliente = sheet.getRow(filaDetalle);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaDetalle);
					}

					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(d.getRazon());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(d.getValor().doubleValue());

					filaDetalle++;
				}
				
				for (Pago p : lc.getPagoList()) {
					col = 13;
					rowCliente = sheet.getRow(filaPago);
					if(rowCliente==null) {
						rowCliente = sheet.createRow(filaPago);
					}
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getTipopago().getNombre());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getTotal().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getValorentrega().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(p.getCambio().doubleValue());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getNumerodocumento());
					
					cell = rowCliente.createCell(col++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(p.getNombrebanco());
					
					filaPago++;
					
				}

				if(filaDetalle>filaPago) {
					fila = filaDetalle;
				}else {
					fila = filaPago;
				}
				fila++;
				
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotDebitoDet-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public StreamedContent getFileND() {
		try {
			
			if(notDebitoList==null || notDebitoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS.");
				return null;
			}
			
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-NotDebito.xlsx");
			
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getEstablecimiento().getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(desde));
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(FechaUtil.formatoFecha(hasta));
			
			
			int fila = 9;
			for (Cabecera lc : notDebitoList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				// datos de la cabecera
				Cell cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocumento()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getIdentificacion());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getCliente().getRazonsocial());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(lc.getTipocomprobanteretencion().getComprobante());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(ComprobanteHelper.formatNumDocumento(lc.getNumdocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(FechaUtil.formatoFecha(lc.getFechaemisiondocasociado()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalsinimpuestos().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotaliva().doubleValue());

				cell = rowCliente.createCell(col++);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(lc.getTotalconimpuestos().doubleValue());
				
				fila++;
				
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "FALECPV-NotDebito-" +  AppJsfUtil.getEstablecimiento().getNombrecomercial() + ".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	public String editarNd(String idNotaDebito) {
		try {
			
			NotaDebitoFrmCtrl notaDebitoFrmCtrl = (NotaDebitoFrmCtrl) AppJsfUtil.getManagedBean("notaDebitoFrmCtrl");
 			String editar =	notaDebitoFrmCtrl.editar(idNotaDebito);
 			
 			if(editar==null) {
 				return "./notaDebitoForm.jsf?faces-redirect=true";
 			}
			
 			AppJsfUtil.addErrorMessage("formMain","ERROR",editar);
 			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void changeSeleccion() {
		try {
			
			if(notDebitoList!=null) {
				for (Cabecera v : notDebitoList) {
					if(!v.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && !v.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
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
			
			if(notDebitoList==null || notDebitoList.stream().filter(x->x.isSeleccion()).count()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN COMPROBANTES SELECCIONADOS.");
				return;
			}
			
			for (Cabecera ventasQuery : notDebitoList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				Cabecera c = new Cabecera();
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				c.setIdcabecera(ventasQuery.getIdcabecera());
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
	 * @return the notDebitoList
	 */
	public List<Cabecera> getNotDebitoList() {
		return notDebitoList;
	}

	/**
	 * @param notDebitoList the notDebitoList to set
	 */
	public void setNotDebitoList(List<Cabecera> notDebitoList) {
		this.notDebitoList = notDebitoList;
	}

	/**
	 * @return the notDebitoSelected
	 */
	public Cabecera getNotDebitoSelected() {
		return notDebitoSelected;
	}

	/**
	 * @param notDebitoSelected the notDebitoSelected to set
	 */
	public void setNotDebitoSelected(Cabecera notDebitoSelected) {
		this.notDebitoSelected = notDebitoSelected;
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
