/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.constante.contadores.TipoPagoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pagodetalle;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.AdquisiciondetalleServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.PagodetalleServicio;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class AdquisicionFrmCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1134597750495584673L;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	@EJB
	private TipopagoServicio tipopagoServicio;
	@EJB
	private ProveedorServicio proveedorServicio;
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	
	@EJB
	private AdquisiciondetalleServicio adquisiciondetalleServicio;
	
	@EJB
	private PagodetalleServicio pagodetalleServicio;
	
	private Adquisicion adquisicionSelected = new Adquisicion();
	private List<Proveedor> proveedorList;
	private List<Tipocomprobante> tipocomprobanteList;
	private List<Tipopago> tipopagoList;
	private List<Adquisiciondetalle> adquisiciondetalleList;
	private Adquisiciondetalle adquisiciondetalleSelected;
	private List<Iva> ivaList;
	private List<Pagodetalle> pagodetalleList;
	private Pagodetalle pagodetalleSelected;
	private List<Tipopago> tipopagoFormList;
	private BigDecimal valorTotalPago;
	
	/**
	 * 
	 */
	public AdquisicionFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			adquisicionSelected = new Adquisicion();
			adquisicionSelected.setSubtotal(BigDecimal.ZERO);
			adquisicionSelected.setTotaliva(BigDecimal.ZERO);
			adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
			adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
			adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
			adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void refrescar() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			nuevaAdquisicion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			// verifica si la adquisicion estado GEN
			if(adquisicionSelected.getIdadquisicion()!=null) {
				Adquisicion ad = adquisicionServicio.consultarByPk(adquisicionSelected.getIdadquisicion());
				if(ad!=null && !ad.getEstado().equals("GEN")) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE GUARDAR, YA QUE SE ENCUENTRA EN ESTADO :" + ad.getEstado());
					return;
				}
			}
			
			// si ya existe la factura del mismo proveedor
			
			if (adquisicionServicio.getAdquisicionDao().existeFacturaProveedor(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), adquisicionSelected.getProveedor().getIdproveedor(), adquisicionSelected.getIdadquisicion(), adquisicionSelected.getNumfactura())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "YA EXISTE LA FACTURA :" + adquisicionSelected.getNumfactura() + " DEL PROVEEDOR : " + adquisicionSelected.getProveedor().getNombrecomercial());
				return;
			}
			
			// sino existe detale
			if(adquisiciondetalleList==null || adquisiciondetalleList.size()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE GUARDAR, NO EXISTE DETALLE DE COMPRA");
				return;
			}
			
			adquisicionSelected.setUpdated(new Date());
			for (Adquisiciondetalle d : adquisiciondetalleList) {
				d.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
				d.setUpdated(new Date());
			}
			
			adquisicionSelected = adquisicionServicio.guadarFacade(adquisicionSelected, adquisiciondetalleList, pagodetalleList);
			
			// actualiza lista de compras
			AdquisicionMainCtrl adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			adquisicionMainCtrl.consultarAdquisiciones();
			
			AppJsfUtil.addInfoMessage("formMain", "OK","REGISTRO GUARDADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editarAdquisicion(String idAdquisicion)throws DaoException {
		
		adquisicionSelected = adquisicionServicio.consultarByPk(idAdquisicion);
		adquisiciondetalleList = adquisicionServicio.getByAdquisicion(idAdquisicion);
		pagodetalleList = pagodetalleServicio.getByAdquisicion(adquisicionSelected.getIdadquisicion());
		totalizarPagoDetalle();
		
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
						TipoComprobanteEnum.ADQUICIION));
		
		setTipopagoList(tipopagoServicio.getTipopagoDao().getByEmpresaFormulario(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), TipoPagoEnum.ADQUISICION));
		
		consultarProveedor();
		consultarIva();
	}
	
	public void nuevaAdquisicion() throws DaoException {
		
		adquisiciondetalleList = new ArrayList<>();
		adquisicionSelected = new Adquisicion();
		adquisicionSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		adquisicionSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		adquisicionSelected.setFecha(new Date());
		adquisicionSelected.setSubtotal(BigDecimal.ZERO);
		adquisicionSelected.setTotaliva(BigDecimal.ZERO);
		adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
		adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
		adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
		adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
		adquisicionSelected.setEstado("GEN");
		adquisicionSelected.setTipopago(tipopagoServicio.getTipopagoDao().getByNombre(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), "EFECTIVO"));
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
						TipoComprobanteEnum.ADQUICIION));
		
		setTipopagoList(tipopagoServicio.getTipopagoDao().getByEmpresaFormulario(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), TipoPagoEnum.ADQUISICION));
		
		consultarProveedor();
		consultarIva();
	}
	
	public void consultarIva()throws DaoException{
		ivaList = null;
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarProveedor()throws DaoException {
		
		setProveedorList(proveedorServicio.getProveedorDao().getByConsulta(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), EstadoRegistroEnum.ACTIVO, null));
		
	}
	
	public void agregarProducto(Producto p) {
		
		if(adquisiciondetalleList==null) {
			adquisiciondetalleList = new ArrayList<>();
		}
		
		Adquisiciondetalle ad = new Adquisiciondetalle();
		ad.setCantidad(p.getCantidad());
		ad.setDescripcion(p.getNombregenerico());
		ad.setDescuento(BigDecimal.ZERO);
		ad.setPorcentajeDescuento(p.getPorcentajedescuento());
		ad.setPrecioUntarioCalculado(BigDecimal.ZERO);
		ad.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		ad.setIva(p.getIva());
		ad.setPreciounitario(p.getPreciounitario());
		ad.setProducto(p);
		calcularAdquicisioDetalleProducto(ad);
		adquisiciondetalleList.add(ad);
		totalizarCompra();
	}
	
	private void calcularAdquicisioDetalleProducto(Adquisiciondetalle a) {
		a.setPorcentajeDescuento(a.getPorcentajeDescuento().divide(BigDecimal.valueOf(100)));
		a.setPrecioUntarioCalculado(a.getPreciounitario());
		if(a.getPorcentajeDescuento().doubleValue()>0.0d) {
			a.setPrecioUntarioCalculado(
					a.getPreciounitario().add(a.getPreciounitario().multiply(a.getPorcentajeDescuento()).negate())
							.setScale(2, RoundingMode.HALF_UP));
		}
		a.setPreciototal(BigDecimal.valueOf(a.getCantidad()).multiply(a.getPrecioUntarioCalculado()));
		a.setDescuento(BigDecimal.valueOf(a.getCantidad()).multiply(a.getPreciounitario()).add(a.getPreciototal().negate()));
		// iva
		a.setValorIva(a.getPreciototal().multiply(a.getIva().getValor().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
		
		
	}
	
	private void calcularAdquicisioDetalle(Adquisiciondetalle a) {
		a.setPreciototal(BigDecimal.valueOf(a.getCantidad()).multiply(a.getPreciounitario()).add(a.getDescuento().negate()));
		// iva
		if(a.getIva()!=null) {
			a.setValorIva(a.getPreciototal().multiply(a.getIva().getValor().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
		}else{
			a.setValorIva(BigDecimal.ZERO);
		}
	}
	
	public void changeAdquicisionDetalle(Adquisiciondetalle a) {
		try {
			calcularAdquicisioDetalle(a);
			totalizarCompra();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizarCompra() {
		
		adquisicionSelected.setSubtotal(BigDecimal.ZERO);
		adquisicionSelected.setTotaliva(BigDecimal.ZERO);
		adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
		adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
		//adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
		adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
		int fil = 1;
		for (Adquisiciondetalle a : adquisiciondetalleList) {
			// totales
			adquisicionSelected.setSubtotal(adquisicionSelected.getSubtotal().add(a.getPreciototal()));
			adquisicionSelected.setTotaliva(adquisicionSelected.getTotaliva().add(a.getValorIva()));
			adquisicionSelected.setTotaldescuento(adquisicionSelected.getTotaldescuento().add(a.getDescuento()));
			if(a.getIdadquisiciondetalle()==null || a.getIdadquisiciondetalle().contains("MM")) {
				a.setIdadquisiciondetalle("MM" + fil);
			}
			fil++;
			
		}
		adquisicionSelected.setTotalfactura(adquisicionSelected.getSubtotal().add(adquisicionSelected.getTotaliva()).setScale(2, RoundingMode.HALF_UP));
		// Valor de la retención
		adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
		adquisicionSelected.setTotalpagar(adquisicionSelected.getTotalfactura().add(adquisicionSelected.getTotalretencion().negate()).setScale(2, RoundingMode.HALF_UP));
	}
	
	public void agregarDetalle() {
		
		try {
			
			if(adquisiciondetalleList==null) {
				adquisiciondetalleList = new ArrayList<>();
			}
			
			Adquisiciondetalle ad = new Adquisiciondetalle();
			ad.setCantidad(1);
			ad.setDescripcion("-");
			ad.setDescuento(BigDecimal.ZERO);
			ad.setPorcentajeDescuento(BigDecimal.ZERO);
			ad.setPrecioUntarioCalculado(BigDecimal.ZERO);
			ad.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			ad.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			ad.setPreciounitario(BigDecimal.valueOf(1));
			ad.setProducto(null);
			calcularAdquicisioDetalle(ad);
			adquisiciondetalleList.add(0, ad);
			totalizarCompra();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void eliminarDetalle() {
		try {
			
			if(adquisicionSelected!=null && adquisicionSelected.getIdadquisicion()!=null) {
				Adquisicion a = adquisicionServicio.consultarByPk(adquisicionSelected.getIdadquisicion());
				if(a.getEstado().equals("ANU")) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR LA COMPRA, ESTA EN ESTADO ANULADO.");
					return;
				}
				
				if(a.getEstado().equals("ENV") || a.getEstado().equals("RETENCION")) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR LA COMPRA, TIENE RETENCION.");
					return;
				}
			}
			
			Adquisiciondetalle ad = adquisiciondetalleServicio.consultarByPk(adquisiciondetalleSelected.getIdadquisiciondetalle());
			if(ad!=null) {
				adquisicionServicio.anularDetalle(adquisiciondetalleSelected.getIdadquisiciondetalle(), true, AppJsfUtil.getUsuario().getIdusuario());
			}
			
			adquisiciondetalleList.remove(adquisiciondetalleSelected);
			totalizarCompra();
			// pantalla principal
			AdquisicionMainCtrl adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			adquisicionMainCtrl.consultarAdquisiciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void showDetallePago() {
		try {
			
			if(pagodetalleList==null) {
				pagodetalleList = new ArrayList<>();
			}
			setTipopagoFormList(tipopagoServicio.getTipopagoDao().getByEmpresaFormulario(
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), TipoPagoEnum.ADQUISICION));
			totalizarPagoDetalle();
			
			AppJsfUtil.showModalRender("dlgAdqDetallePago", "frmAdqPago");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void totalizarPagoDetalle() {
		valorTotalPago = BigDecimal.ZERO;
		int i = 0;
		for (Pagodetalle pg : pagodetalleList) {
			valorTotalPago = valorTotalPago.add(pg.getValor());
			pg.setIdpagodetalle("MM"+(i++));
		}
		valorTotalPago.setScale(2, RoundingMode.HALF_UP);
	}
	
	public void agregarPagoDetalle() {
		try {
			
			nuevoPagoDetalle();
			totalizarPagoDetalle();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmAdqPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarPagoDetalle() {
		try {
			
			if(adquisicionSelected!=null && adquisicionSelected.getIdadquisicion()!=null) {
				Adquisicion a = adquisicionServicio.consultarByPk(adquisicionSelected.getIdadquisicion());
				if(a.getEstado().equals("ANU")) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR LA COMPRA, ESTA EN ESTADO ANULADO.");
					return;
				}
				
				if(a.getEstado().equals("ENV") || a.getEstado().equals("RETENCION")) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR LA COMPRA, TIENE RETENCION.");
					return;
				}
			}
			pagodetalleSelected.setUpdated(new Date());
			pagodetalleServicio.eliminar(pagodetalleSelected);
			pagodetalleList.remove(pagodetalleSelected);
			totalizarPagoDetalle();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmAdqPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	private void nuevoPagoDetalle()throws DaoException {
		Pagodetalle pd = new Pagodetalle();
		pd.setAdquisicion(adquisicionSelected);
		pd.setFecha(new Date());
		pd.setTiporegistro("C");
		pd.setValor(BigDecimal.ZERO);
		pd.setTipopago(adquisicionSelected.getTipopago());
		pd.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		pagodetalleList.add(0,pd);
	}
	
	public void validarPagoDetalle() {
		
		try {
			
			if(valorTotalPago.doubleValue()!=adquisicionSelected.getTotalpagar().doubleValue()) {
				AppJsfUtil.addErrorMessage("frmAdqPago", "ERROR", "TOTAL DE PAGO, ES DIFERENTE A LOS VALORES REGISTRADOS.");
				return;
			}
			
			AppJsfUtil.hideModal("dlgAdqDetallePago");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmAdqPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	/**
	 * @return the proveedorList
	 */
	public List<Proveedor> getProveedorList() {
		return proveedorList;
	}

	/**
	 * @param proveedorList the proveedorList to set
	 */
	public void setProveedorList(List<Proveedor> proveedorList) {
		this.proveedorList = proveedorList;
	}

	/**
	 * @return the adquisicionSelected
	 */
	public Adquisicion getAdquisicionSelected() {
		return adquisicionSelected;
	}

	/**
	 * @param adquisicionSelected the adquisicionSelected to set
	 */
	public void setAdquisicionSelected(Adquisicion adquisicionSelected) {
		this.adquisicionSelected = adquisicionSelected;
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
	 * @return the tipopagoList
	 */
	public List<Tipopago> getTipopagoList() {
		return tipopagoList;
	}

	/**
	 * @param tipopagoList the tipopagoList to set
	 */
	public void setTipopagoList(List<Tipopago> tipopagoList) {
		this.tipopagoList = tipopagoList;
	}

	/**
	 * @return the adquisiciondetalleList
	 */
	public List<Adquisiciondetalle> getAdquisiciondetalleList() {
		return adquisiciondetalleList;
	}

	/**
	 * @param adquisiciondetalleList the adquisiciondetalleList to set
	 */
	public void setAdquisiciondetalleList(List<Adquisiciondetalle> adquisiciondetalleList) {
		this.adquisiciondetalleList = adquisiciondetalleList;
	}

	/**
	 * @return the ivaList
	 */
	public List<Iva> getIvaList() {
		return ivaList;
	}

	/**
	 * @param ivaList the ivaList to set
	 */
	public void setIvaList(List<Iva> ivaList) {
		this.ivaList = ivaList;
	}

	/**
	 * @return the adquisiciondetalleSelected
	 */
	public Adquisiciondetalle getAdquisiciondetalleSelected() {
		return adquisiciondetalleSelected;
	}

	/**
	 * @param adquisiciondetalleSelected the adquisiciondetalleSelected to set
	 */
	public void setAdquisiciondetalleSelected(Adquisiciondetalle adquisiciondetalleSelected) {
		this.adquisiciondetalleSelected = adquisiciondetalleSelected;
	}

	/**
	 * @return the pagodetalleList
	 */
	public List<Pagodetalle> getPagodetalleList() {
		return pagodetalleList;
	}

	/**
	 * @param pagodetalleList the pagodetalleList to set
	 */
	public void setPagodetalleList(List<Pagodetalle> pagodetalleList) {
		this.pagodetalleList = pagodetalleList;
	}

	/**
	 * @return the pagodetalleSelected
	 */
	public Pagodetalle getPagodetalleSelected() {
		return pagodetalleSelected;
	}

	/**
	 * @param pagodetalleSelected the pagodetalleSelected to set
	 */
	public void setPagodetalleSelected(Pagodetalle pagodetalleSelected) {
		this.pagodetalleSelected = pagodetalleSelected;
	}

	/**
	 * @return the tipopagoFormList
	 */
	public List<Tipopago> getTipopagoFormList() {
		return tipopagoFormList;
	}

	/**
	 * @param tipopagoFormList the tipopagoFormList to set
	 */
	public void setTipopagoFormList(List<Tipopago> tipopagoFormList) {
		this.tipopagoFormList = tipopagoFormList;
	}

	/**
	 * @return the valorTotalPago
	 */
	public BigDecimal getValorTotalPago() {
		return valorTotalPago;
	}

	/**
	 * @param valorTotalPago the valorTotalPago to set
	 */
	public void setValorTotalPago(BigDecimal valorTotalPago) {
		this.valorTotalPago = valorTotalPago;
	}


}
