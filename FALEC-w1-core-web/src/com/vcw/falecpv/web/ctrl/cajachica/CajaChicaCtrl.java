/**
 * 
 */
package com.vcw.falecpv.web.ctrl.cajachica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

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
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.TransaccionTipoEnum;
import com.vcw.falecpv.core.constante.contadores.EstadoComprobanteEnum;
import com.vcw.falecpv.core.modelo.persistencia.Transaccion;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.TransaccionServicio;
import com.vcw.falecpv.core.servicio.TransaccionconceptoServicio;
import com.vcw.falecpv.core.servicio.TransacciontipoServicio;
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
public class CajaChicaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6920391538405286693L;
	
	@EJB
	private TransaccionconceptoServicio transaccionconceptoServicio;
	
	@EJB
	private TransaccionServicio transaccionServicio;
	
	@EJB
	private TransacciontipoServicio transacciontipoServicio;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private Transaccionconcepto transaccionconcepto;
	private List<Transaccionconcepto> transaccionconceptoList;
	private List<Transaccion> transaccionList;
	private Transaccion transaccionSelected;
	private Date desde;
	private Date hasta;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private BigDecimal totalIngreso = BigDecimal.ZERO;
	private BigDecimal totalEgreso = BigDecimal.ZERO;
	private BigDecimal saldoActual = BigDecimal.ZERO;
	private String criterioProveedor;
	private boolean flagGuardar = true;  
	private Transaccionconcepto transaccionconceptoForm;
	
	/**
	 * 
	 */
	public CajaChicaCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -7);
			consultarTransaccionConcepto();
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultarTransaccionConcepto() throws DaoException {
		transaccionconceptoList = null;
		transaccionconceptoList = transaccionconceptoServicio.getTransaccionconceptoDao().getByEmpresa(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), TransaccionTipoEnum.CAJA_CHICA.getId(),
				EstadoRegistroEnum.ACTIVO);
	}
	
	public void consultar() throws DaoException{
		transaccionList = null;
		transaccionList = transaccionServicio.getTransaccionDao().getByfechas(establecimientoMain.getIdestablecimiento(), TransaccionTipoEnum.CAJA_CHICA.getId(), desde, hasta,transaccionconcepto);
	}

	@Override
	public void buscar() {
		try {
			consultarTransaccionConcepto();
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if (transaccionSelected==null) {
				getMessageCommonCtrl().crearMensaje("Error","No existe un registro seleccionado", 
						Message.ERROR);
				return;
			}
			
			if (transaccionSelected.getValoregreso().doubleValue() > 0d) {
				transaccionSelected.setValoringreso(transaccionSelected.getValoregreso());
				transaccionSelected.setValoregreso(BigDecimal.ZERO);
				transaccionSelected.setNota("ANULAR EGRESO DOC REFERENCIA : " 
						+ (transaccionSelected.getNumdocumento() != null ? transaccionSelected.getNumdocumento() : " ")
						+ " / " + transaccionSelected.getNota());
			} else {
				if (transaccionSelected.getValoringreso().doubleValue() > 0d) {
					saldoActual = transaccionServicio.getTransaccionDao().getSaldoActual(
							establecimientoMain.getIdestablecimiento(), TransaccionTipoEnum.CAJA_CHICA);

					if (transaccionSelected.getValoringreso().doubleValue() >= saldoActual.doubleValue()) {
						transaccionSelected.setValoregreso(saldoActual);
					} else {
						transaccionSelected.setValoregreso(transaccionSelected.getValoringreso());
					}
					transaccionSelected.setNota("ANULAR INGRESO / " + transaccionSelected.getNota());
					transaccionSelected.setValoringreso(BigDecimal.ZERO);
				}
			}
			
			transaccionSelected.setUsuario(AppJsfUtil.getUsuario());
			String idAnular = transaccionSelected.getIdtransaccion();
			transaccionSelected.setIdtransaccion(null);
			transaccionSelected.setUpdated(new Date());
			transaccionSelected.setAjuste(1);
			transaccionServicio.guardarFacade(transaccionSelected,idAnular);
			transaccionSelected = null;
			
			consultar();
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void guardar() {
		try {
			
			transaccionSelected.setUpdated(new Date());
			saldoActual = transaccionServicio.getTransaccionDao().getSaldoActual(establecimientoMain.getIdestablecimiento(), TransaccionTipoEnum.CAJA_CHICA);
			if(transaccionSelected.getTipoTransaccion().equals("INGRESO")) {
				transaccionSelected.setValoregreso(BigDecimal.ZERO);
			}else {
				transaccionSelected.setValoringreso(BigDecimal.ZERO);
			}
			
			// validar saldo si es egreso
			if(transaccionSelected.getTipoTransaccion().equals("EGRESO")) {
				if(transaccionSelected.getValoregreso().doubleValue()>saldoActual.doubleValue()) {
					getMessageCommonCtrl().crearMensaje("Error","No existe saldo suficiente", 
							Message.ERROR);
					return;
				}
			}
			
			if(transaccionSelected.getTipoTransaccion().equals("EGRESO")) {
				if(transaccionSelected.getValoregreso().doubleValue()==0d) {
					AppJsfUtil.addErrorMessage("formCC:ipsCCValor", "ERROR", "Mayor a 0");
					return;
				}
			}else{
				if(transaccionSelected.getValoringreso().doubleValue()==0d) {
					AppJsfUtil.addErrorMessage("formCC:ipsCCValor", "ERROR", "Mayor a 0");
					return;
				}
			}
			
			transaccionServicio.guardarFacade(transaccionSelected);
			consultar();
			totalizar();
			flagGuardar = false;
			AppJsfUtil.addInfoMessage("formCC", "OK", "Registro guardado correctamente.");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void nuevo() {
		try {
			consultarTransaccionConcepto();
			nuevaTransaccion();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void nuevoForm() {
		try {
			nuevaTransaccion();
			AppJsfUtil.showModalRender("dlgcajaChica", "formCC");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	
	
	private void nuevaTransaccion() throws DaoException {
		flagGuardar = true;
		criterioProveedor = null;
		transaccionconceptoForm = null;
		transaccionSelected = new Transaccion();
		transaccionSelected.setTransacciontipo(transacciontipoServicio.consultarByPk(TransaccionTipoEnum.CAJA_CHICA.getId()));
		transaccionSelected.setValoringreso(BigDecimal.ZERO);
		transaccionSelected.setValoregreso(BigDecimal.ZERO);
		transaccionSelected.setUsuario(AppJsfUtil.getUsuario());
		transaccionSelected.setEstablecimiento(establecimientoMain);
		transaccionSelected.setFechaemision(new Date());
		transaccionSelected.setEstado(EstadoComprobanteEnum.REGISTRADO.toString());
	}
	
	public void totalizar() throws DaoException {
		totalEgreso = BigDecimal.ZERO;
		totalIngreso = BigDecimal.ZERO;
		saldoActual = BigDecimal.ZERO;
		if(transaccionList==null || transaccionList.isEmpty()) {
			return;
		}
		
		totalEgreso = BigDecimal.valueOf(transaccionList.stream().filter(x->!x.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString())).mapToDouble(x->x.getValoregreso().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		totalIngreso = BigDecimal.valueOf(transaccionList.stream().filter(x->!x.getEstado().equals(ComprobanteEstadoEnum.ANULADO.toString()) && x.getAjuste()==0).mapToDouble(x->x.getValoringreso().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		saldoActual = transaccionServicio.getTransaccionDao().getSaldoActual(establecimientoMain.getIdestablecimiento(), TransaccionTipoEnum.CAJA_CHICA);
	}
	
	public StreamedContent getFileResumen() {
		try {
			
			if(transaccionList==null || transaccionList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error","No existen datos", 
						Message.ERROR);
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-CajaChica.xlsx");
			
			// icialización
			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// datos cabecera
			Row rowCliente = sheet.getRow(3);
			rowCliente.createCell(1).setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			rowCliente = sheet.getRow(4);
			rowCliente.createCell(1).setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			rowCliente = sheet.getRow(5);
			rowCliente.createCell(1).setCellValue(saldoActual.doubleValue());
			
			rowCliente = sheet.getRow(6);
			rowCliente.createCell(1).setCellValue(totalIngreso.doubleValue());
			
			rowCliente = sheet.getRow(7);
			rowCliente.createCell(1).setCellValue(totalEgreso.doubleValue());
			
			int fila = 10;
			for (Transaccion t : transaccionList) {
				
				int col = 0;
				rowCliente = sheet.createRow(fila);
				
				Cell cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(t.getFechaemision()));
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getTransaccionconcepto().getNombre());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getCliente()!=null?t.getCliente().getIdentificacion():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getCliente()!=null?t.getCliente().getRazonsocial():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getNumdocumento()!=null?t.getNumdocumento():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getEstado());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getNota()!=null?t.getNota():"");
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getValoringreso().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getValoregreso().doubleValue());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(t.getUsuario().getNombre());
				
				cell = rowCliente.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFechaHora(t.getUpdated()));
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls, "MAKOPV-CajaChica-" + establecimientoMain.getCodigoestablecimiento() + "_" + establecimientoMain.getNombrecomercial() + ".xlsx");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return null;
	}
	
	public void buscarProveedor() {
		try {
			
			if(transaccionSelected==null) return;
			
			if(criterioProveedor==null || criterioProveedor.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formCC:inpCriterioProveedor", "Error", "Requerido");
				return;
			}
			
			consultarProveedor(criterioProveedor);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultarProveedor(String identificador) throws DaoException {
		transaccionSelected.setCliente(null);
		transaccionSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	/**
	 * @return the transaccionconcepto
	 */
	public Transaccionconcepto getTransaccionconcepto() {
		return transaccionconcepto;
	}

	/**
	 * @param transaccionconcepto the transaccionconcepto to set
	 */
	public void setTransaccionconcepto(Transaccionconcepto transaccionconcepto) {
		this.transaccionconcepto = transaccionconcepto;
	}

	/**
	 * @return the transaccionconceptoList
	 */
	public List<Transaccionconcepto> getTransaccionconceptoList() {
		return transaccionconceptoList;
	}

	/**
	 * @param transaccionconceptoList the transaccionconceptoList to set
	 */
	public void setTransaccionconceptoList(List<Transaccionconcepto> transaccionconceptoList) {
		this.transaccionconceptoList = transaccionconceptoList;
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
	 * @return the totalSaldo
	 */
	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	/**
	 * @param totalSaldo the totalSaldo to set
	 */
	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	/**
	 * @return the totalIngreso
	 */
	public BigDecimal getTotalIngreso() {
		return totalIngreso;
	}

	/**
	 * @param totalIngreso the totalIngreso to set
	 */
	public void setTotalIngreso(BigDecimal totalIngreso) {
		this.totalIngreso = totalIngreso;
	}

	/**
	 * @return the totalEgreso
	 */
	public BigDecimal getTotalEgreso() {
		return totalEgreso;
	}

	/**
	 * @param totalEgreso the totalEgreso to set
	 */
	public void setTotalEgreso(BigDecimal totalEgreso) {
		this.totalEgreso = totalEgreso;
	}

	/**
	 * @return the saldoActual
	 */
	public BigDecimal getSaldoActual() {
		return saldoActual;
	}

	/**
	 * @param saldoActual the saldoActual to set
	 */
	public void setSaldoActual(BigDecimal saldoActual) {
		this.saldoActual = saldoActual;
	}

	/**
	 * @return the transaccionList
	 */
	public List<Transaccion> getTransaccionList() {
		return transaccionList;
	}

	/**
	 * @param transaccionList the transaccionList to set
	 */
	public void setTransaccionList(List<Transaccion> transaccionList) {
		this.transaccionList = transaccionList;
	}

	/**
	 * @return the transaccionSelected
	 */
	public Transaccion getTransaccionSelected() {
		return transaccionSelected;
	}

	/**
	 * @param transaccionSelected the transaccionSelected to set
	 */
	public void setTransaccionSelected(Transaccion transaccionSelected) {
		this.transaccionSelected = transaccionSelected;
	}

	/**
	 * @return the criterioProveedor
	 */
	public String getCriterioProveedor() {
		return criterioProveedor;
	}

	/**
	 * @param criterioProveedor the criterioProveedor to set
	 */
	public void setCriterioProveedor(String criterioProveedor) {
		this.criterioProveedor = criterioProveedor;
	}

	/**
	 * @return the flagGuardar
	 */
	public boolean isFlagGuardar() {
		return flagGuardar;
	}

	/**
	 * @param flagGuardar the flagGuardar to set
	 */
	public void setFlagGuardar(boolean flagGuardar) {
		this.flagGuardar = flagGuardar;
	}

	/**
	 * @return the transaccionconceptoForm
	 */
	public Transaccionconcepto getTransaccionconceptoForm() {
		return transaccionconceptoForm;
	}

	/**
	 * @param transaccionconceptoForm the transaccionconceptoForm to set
	 */
	public void setTransaccionconceptoForm(Transaccionconcepto transaccionconceptoForm) {
		this.transaccionconceptoForm = transaccionconceptoForm;
	}

}
