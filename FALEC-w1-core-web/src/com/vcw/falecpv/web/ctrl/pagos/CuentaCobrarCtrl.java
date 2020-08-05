/**
 * 
 */
package com.vcw.falecpv.web.ctrl.pagos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.vista.VComprobantescredito;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.VComprobantescreditoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CuentaCobrarCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515265342249527817L;

	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private VComprobantescreditoServicio vComprobantescreditoServicio;
	
	private List<VComprobantescredito> vComprobantescreditoList;
	private VComprobantescredito vComprobantescreditoSelected;
	private List<Tipocomprobante> tipocomprobanteList;
	private Tipocomprobante tipocomprobante;
	private BigDecimal totalCobrar;
	private BigDecimal totalVencido;
	private BigDecimal totalProximo;
	private String criterioBusqueda;
	
	public CuentaCobrarCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			consultarTipoComprobante();
			tipocomprobante = null;
			totalCobrar = BigDecimal.ZERO;
			totalVencido = BigDecimal.ZERO;
			totalProximo = BigDecimal.ZERO;
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		vComprobantescreditoList = null;
		vComprobantescreditoList = vComprobantescreditoServicio.getByCuentasCobrar(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), tipocomprobante,criterioBusqueda);
		totalizar();
	}
	
	public void consultarTipoComprobante()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:pvUnoDT");
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(TipoComprobanteEnum.CUENTAS_COBRAR));
	}

	@Override
	public void buscar() {
		try {
			consultarTipoComprobante();
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public Boolean isFechaVencida(Date fecha) {
		try {
			return (FechaUtil.comparaFechas(fecha, new Date())<0);
		} catch (ParseException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return false;
	}
	
	public void totalizar() {
		
		if(vComprobantescreditoList!=null) {
			
			totalCobrar = BigDecimal.valueOf(vComprobantescreditoList.stream().mapToDouble(x->x.getTotalpago().doubleValue()).sum() +
			vComprobantescreditoList.stream().mapToDouble(x->x.getAbono().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
			
			List<VComprobantescredito> vencimientoList =  vComprobantescreditoList.stream().filter(x->isFechaVencida(x.getFechapago())).collect(Collectors.toList());
			totalVencido = BigDecimal.ZERO;
			for (VComprobantescredito v : vencimientoList) {
				v.getPagoList().sort(Comparator.comparing(Pago::getFechapago));
				for (Pago p : v.getPagoList()) {
					if(p.getValorpago().doubleValue()<p.getTotal().doubleValue() && isFechaVencida(p.getFechapago())) {
						totalVencido = totalVencido.add(p.getTotal().add(p.getValorpago().negate())).setScale(2, RoundingMode.HALF_UP);
					}
				}
			}
			
		}
		
	}

	/**
	 * @return the vComprobantescreditoList
	 */
	public List<VComprobantescredito> getvComprobantescreditoList() {
		return vComprobantescreditoList;
	}

	/**
	 * @param vComprobantescreditoList the vComprobantescreditoList to set
	 */
	public void setvComprobantescreditoList(List<VComprobantescredito> vComprobantescreditoList) {
		this.vComprobantescreditoList = vComprobantescreditoList;
	}

	/**
	 * @return the vComprobantescreditoSelected
	 */
	public VComprobantescredito getvComprobantescreditoSelected() {
		return vComprobantescreditoSelected;
	}

	/**
	 * @param vComprobantescreditoSelected the vComprobantescreditoSelected to set
	 */
	public void setvComprobantescreditoSelected(VComprobantescredito vComprobantescreditoSelected) {
		this.vComprobantescreditoSelected = vComprobantescreditoSelected;
	}

	/**
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the tipocomprobanteList
	 */
	public List<Tipocomprobante> getTipocomprobanteList() {
		return tipocomprobanteList;
	}

	/**
	 * @param tipocomprobanteList the tipocomprobanteList to set
	 */
	public void setTipocomprobanteList(List<Tipocomprobante> tipocomprobanteList) {
		this.tipocomprobanteList = tipocomprobanteList;
	}

	/**
	 * @return the totalCobrar
	 */
	public BigDecimal getTotalCobrar() {
		return totalCobrar;
	}

	/**
	 * @param totalCobrar the totalCobrar to set
	 */
	public void setTotalCobrar(BigDecimal totalCobrar) {
		this.totalCobrar = totalCobrar;
	}

	/**
	 * @return the totalVencido
	 */
	public BigDecimal getTotalVencido() {
		return totalVencido;
	}

	/**
	 * @param totalVencido the totalVencido to set
	 */
	public void setTotalVencido(BigDecimal totalVencido) {
		this.totalVencido = totalVencido;
	}

	/**
	 * @return the totalProximo
	 */
	public BigDecimal getTotalProximo() {
		return totalProximo;
	}

	/**
	 * @param totalProximo the totalProximo to set
	 */
	public void setTotalProximo(BigDecimal totalProximo) {
		this.totalProximo = totalProximo;
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

}
