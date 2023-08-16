/**
 * 
 */
package com.vcw.falecpv.web.ctrl.home;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.TransaccionTipoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPagos;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.TransaccionServicio;
import com.vcw.falecpv.web.common.AppSessionCtrl;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class MainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288465203727050008L;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private TransaccionServicio transaccionServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;
	
	private VentasQuery totalVentas;
	private BigDecimal promedioConsumo;
	private Date desde;
	private Date hasta;
	private AppSessionCtrl appSessionCtrl;
	private Integer contadorClientes;
	private List<VentasQuery> productosMasVendidos;
	private List<VentasQuery> resumenVentas;
	private Date fechaActual = new Date();
	private BigDecimal totalCajaChica = BigDecimal.ZERO;
	private BigDecimal pagoEfectivo = BigDecimal.ZERO;
	private BigDecimal pagoTransferencia = BigDecimal.ZERO;
	private BigDecimal pagoTarjetas = BigDecimal.ZERO;
	private BigDecimal pagoCredito = BigDecimal.ZERO;

	/**
	 * 
	 */
	public MainCtrl() {
	}
	
	@PostConstruct
    public void init() {
		try {
			// establecer establecimiento main
			appSessionCtrl = (AppSessionCtrl) AppJsfUtil.getManagedBean("appSessionCtrl");
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			consultarFacade();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarFacade() throws DaoException, NumberFormatException, ParametroRequeridoException {
		consultarTotalventas();
		consultarPromedioConsumo();
		consultarContadorCliente();
		consultarProductosMasVendidos();
		consultarResumenVentas();
		consultarCajaChicaSaldo();
		consultarTipoPago();
	}
	
	@Override
	public void buscar() {
		try {
			consultarFacade();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarTotalventas()throws DaoException{
		List<VentasQuery> totales = consultaVentaServicio.getTotalByFecha(establecimientoMain.getIdestablecimiento(), FechaUtil.agregarDias(new Date(), 0));
		totalVentas = null;
		if(totales!=null && !totales.isEmpty()) {
			VentasQuery vf = totales.stream().filter(x->x.getIdentificador().equals(GenTipoDocumentoEnum.FACTURA.getIdentificador())).findFirst().orElse(null);
			VentasQuery vr = totales.stream().filter(x->x.getIdentificador().equals(GenTipoDocumentoEnum.RECIBO.getIdentificador())).findFirst().orElse(null);
			totalVentas = new VentasQuery();
			if(vf!=null) {
				totalVentas.setTotalconimpuestos(totalVentas.getTotalconimpuestos().add(vf.getTotalconimpuestos()));
				totalVentas.setImpuestos(totalVentas.getImpuestos().add(vf.getImpuestos()));
				totalVentas.setTotalsinimpuestos(totalVentas.getTotalsinimpuestos().add(vf.getTotalsinimpuestos()));
				totalVentas.setContador(vf.getContador() + totalVentas.getContador());
			}
			if(vr!=null) {
				totalVentas.setTotalconimpuestos(totalVentas.getTotalconimpuestos().add(vr.getTotalconimpuestos()));
				totalVentas.setTotalrecibos(totalVentas.getTotalrecibos().add(vr.getTotalsinimpuestos()));
				totalVentas.setContador(vr.getContador() + totalVentas.getContador());
			}
		}
	}
	
	private void consultarPromedioConsumo()throws DaoException{
		promedioConsumo = null;
		promedioConsumo = consultaVentaServicio.getConsumoPromedio(establecimientoMain.getIdestablecimiento(), desde, hasta);
		if(promedioConsumo!=null && promedioConsumo.doubleValue()==0d) {
			promedioConsumo = null;
		}
	}
	
	private void consultarContadorCliente()throws DaoException{
		contadorClientes = null;
		contadorClientes = consultaVentaServicio.getClientesContador(establecimientoMain.getIdestablecimiento(), desde, hasta);
		if(contadorClientes!=null && contadorClientes==0) {
			contadorClientes = null;
		}
	}
	
	private void consultarProductosMasVendidos()throws DaoException{
		productosMasVendidos = null;
		productosMasVendidos = consultaVentaServicio.getVentasProductos(establecimientoMain.getIdestablecimiento(), desde, hasta, null);
		if(productosMasVendidos!=null && productosMasVendidos.size()>20) {
			
			for(int i=0;i<(productosMasVendidos.size()-20);i++) {
				productosMasVendidos.remove(productosMasVendidos.size()-1);
			}
		}
	}
	
	private void consultarTipoPago()throws DaoException, NumberFormatException, ParametroRequeridoException{		
		
		pagoEfectivo = BigDecimal.ZERO;
		pagoTransferencia = BigDecimal.ZERO;
		pagoTarjetas = BigDecimal.ZERO;
		pagoCredito = BigDecimal.ZERO;
		
		pagoEfectivo = pagoServicio.getTotalPago(new Date(), 
				Arrays.asList(new String[] {"ANULADO"}), 
				Arrays.asList(new String[] {"1"}), 
				parametroGenericoServicio.consultarParametro(PGPagos.PAGOS_EFECTIVO, TipoRetornoParametroGenerico.STRING));
		
		pagoTransferencia = pagoServicio.getTotalPago(new Date(), 
				Arrays.asList(new String[] {"ANULADO"}), 
				Arrays.asList(new String[] {"1"}), 
				parametroGenericoServicio.consultarParametro(PGPagos.PAGOS_TRANSFERENCIA, TipoRetornoParametroGenerico.STRING));
		
		pagoTarjetas = pagoServicio.getTotalPago(new Date(), 
				Arrays.asList(new String[] {"ANULADO"}), 
				Arrays.asList(new String[] {"1"}), 
				parametroGenericoServicio.consultarParametro(PGPagos.PAGOS_TARJETA, TipoRetornoParametroGenerico.STRING));
		
		pagoCredito = pagoServicio.getTotalPago(new Date(), 
				Arrays.asList(new String[] {"ANULADO"}), 
				Arrays.asList(new String[] {"1"}), 
				parametroGenericoServicio.consultarParametro(PGPagos.PAGOS_CREDITO, TipoRetornoParametroGenerico.STRING));
		
	}
	
	private void consultarResumenVentas()throws DaoException{
		resumenVentas = null;
		resumenVentas = consultaVentaServicio.getVentasResumenByFecha(establecimientoMain.getIdestablecimiento(), new Date());
	}
	
	private void consultarCajaChicaSaldo() throws DaoException {
		totalCajaChica = transaccionServicio.getTransaccionDao().getSaldoActual(
				establecimientoMain.getIdestablecimiento(), TransaccionTipoEnum.CAJA_CHICA);
	}
	
	public String getFormatoNumDocumento(String numDoc) {
		return ComprobanteHelper.formatNumDocumento(numDoc);
	}

	/**
	 * @return the totalVentas
	 */
	public VentasQuery getTotalVentas() {
		return totalVentas;
	}

	/**
	 * @param totalVentas the totalVentas to set
	 */
	public void setTotalVentas(VentasQuery totalVentas) {
		this.totalVentas = totalVentas;
	}

	/**
	 * @return the promedioConsumo
	 */
	public BigDecimal getPromedioConsumo() {
		return promedioConsumo;
	}

	/**
	 * @param promedioConsumo the promedioConsumo to set
	 */
	public void setPromedioConsumo(BigDecimal promedioConsumo) {
		this.promedioConsumo = promedioConsumo;
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
	 * @return the appSessionCtrl
	 */
	public AppSessionCtrl getAppSessionCtrl() {
		return appSessionCtrl;
	}

	/**
	 * @param appSessionCtrl the appSessionCtrl to set
	 */
	public void setAppSessionCtrl(AppSessionCtrl appSessionCtrl) {
		this.appSessionCtrl = appSessionCtrl;
	}

	/**
	 * @return the contadorClientes
	 */
	public Integer getContadorClientes() {
		return contadorClientes;
	}

	/**
	 * @param contadorClientes the contadorClientes to set
	 */
	public void setContadorClientes(Integer contadorClientes) {
		this.contadorClientes = contadorClientes;
	}

	/**
	 * @return the productosMasVendidos
	 */
	public List<VentasQuery> getProductosMasVendidos() {
		return productosMasVendidos;
	}

	/**
	 * @param productosMasVendidos the productosMasVendidos to set
	 */
	public void setProductosMasVendidos(List<VentasQuery> productosMasVendidos) {
		this.productosMasVendidos = productosMasVendidos;
	}

	/**
	 * @return the resumenVentas
	 */
	public List<VentasQuery> getResumenVentas() {
		return resumenVentas;
	}

	/**
	 * @param resumenVentas the resumenVentas to set
	 */
	public void setResumenVentas(List<VentasQuery> resumenVentas) {
		this.resumenVentas = resumenVentas;
	}

	/**
	 * @return the fechaActual
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual the fechaActual to set
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * @return the totalCajaChica
	 */
	public BigDecimal getTotalCajaChica() {
		return totalCajaChica;
	}

	/**
	 * @param totalCajaChica the totalCajaChica to set
	 */
	public void setTotalCajaChica(BigDecimal totalCajaChica) {
		this.totalCajaChica = totalCajaChica;
	}

	/**
	 * @return the pagoEfectivo
	 */
	public BigDecimal getPagoEfectivo() {
		return pagoEfectivo;
	}

	/**
	 * @param pagoEfectivo the pagoEfectivo to set
	 */
	public void setPagoEfectivo(BigDecimal pagoEfectivo) {
		this.pagoEfectivo = pagoEfectivo;
	}

	/**
	 * @return the pagoTarjetas
	 */
	public BigDecimal getPagoTarjetas() {
		return pagoTarjetas;
	}

	/**
	 * @param pagoTarjetas the pagoTarjetas to set
	 */
	public void setPagoTarjetas(BigDecimal pagoTarjetas) {
		this.pagoTarjetas = pagoTarjetas;
	}

	/**
	 * @return the pagoCredito
	 */
	public BigDecimal getPagoCredito() {
		return pagoCredito;
	}

	/**
	 * @param pagoCredito the pagoCredito to set
	 */
	public void setPagoCredito(BigDecimal pagoCredito) {
		this.pagoCredito = pagoCredito;
	}

	/**
	 * @return the pagoTransferencia
	 */
	public BigDecimal getPagoTransferencia() {
		return pagoTransferencia;
	}

	/**
	 * @param pagoTransferencia the pagoTransferencia to set
	 */
	public void setPagoTransferencia(BigDecimal pagoTransferencia) {
		this.pagoTransferencia = pagoTransferencia;
	}

}
