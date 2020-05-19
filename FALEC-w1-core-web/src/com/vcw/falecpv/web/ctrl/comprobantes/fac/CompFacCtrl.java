/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.fac;

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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
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
public class CompFacCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9045892564546644805L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private IceServicio iceServicio;

	
	private Cabecera cabecerSelected;
	private String criterioCliente;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private List<Detalle> detalleFacList;
	private Detalle detalleSelected;
	private List<Producto> productoList;
	private Producto productoSelected;
	private String criterioBusqueda;
	private List<Ice> iceList;
	private List<Iva> ivaList;
 	
	/**
	 * 
	 */
	public CompFacCtrl() {
		
	}
	
	@PostConstruct
	public void init() {
		try {
			
			productoSelected = null;
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void encerar() {
		cabecerSelected = null;
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
	}
	
	public void buscarConsumidorFinal() {
		try {
			if(cabecerSelected==null) return;
			
			consultarCliente("9999999999999");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarCliente(String identificador) throws DaoException {
		cabecerSelected.setCliente(null);
		cabecerSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void buscarCliente() {
		try {
			
			if(cabecerSelected==null) return;
			
			if(criterioCliente==null || criterioCliente.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioCliente", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarCliente(criterioCliente);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void nuevo() {
		try {
			
			productoSelected = null;
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarProductos()throws DaoException{
		productoList = null;
		productoList = productoServicio.getProductoDao().consultarAllImageEager(AppJsfUtil.getEstablecimiento().getIdestablecimiento(),criterioBusqueda);
	}
	
	public void nuevaFactura() throws DaoException {
		cabecerSelected = new Cabecera();
		cabecerSelected.setFechaemision(new Date());
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
	}
	
	public void agregarProducto() {
		try {
			
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			
			detalleSelected = existeProductoLista();
			boolean existe = false;
			if(detalleSelected!=null) {
				detalleSelected.setCantidad(
						productoSelected.getCantidad() != null ? BigDecimal.valueOf(productoSelected.getCantidad())
								: BigDecimal.valueOf(1));
				existe = true;
			}else {
				detalleSelected = new Detalle();
				detalleSelected.setCantidad(
						productoSelected.getCantidad() != null ? BigDecimal.valueOf(productoSelected.getCantidad())
								: BigDecimal.valueOf(1));
				detalleSelected.setDescripcion(productoSelected.getNombregenerico());
				detalleSelected.setPreciounitario(productoSelected.getPreciounitario());
				detalleSelected.setProducto(productoSelected);
				detalleSelected.setIva(productoSelected.getIva());
				detalleSelected.setIce(productoSelected.getIce());
			}
			
			calcularItem(detalleSelected,true);
			
			if(!existe) {
				detalleFacList.add(detalleSelected);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public void calcularItemAction(boolean calcDescuento) {
		try {
			calcularItem(detalleSelected,calcDescuento);
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	private void calcularItem(Detalle dFac,boolean calcDescuento) {
		if(calcDescuento) {
			dFac.setDescuento(productoSelected.getPorcentajedescuento().divide(BigDecimal.valueOf(100))
					.multiply(dFac.getPreciounitario()).multiply(dFac.getCantidad())
					.setScale(2, RoundingMode.HALF_UP));
		}
		dFac.setPreciototalsinimpuesto(dFac.getCantidad().multiply(dFac.getPreciounitario()).add(dFac.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValorice(dFac.getIce().getValor().divide(BigDecimal.valueOf(100)).multiply(dFac.getPreciototalsinimpuesto()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValoriva(dFac.getIva().getValor().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciototalsinimpuesto().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototal(dFac.getPreciototalsinimpuesto().add(dFac.getValoriva().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		
	}
	
	private void totalizar() throws DaoException {
		if(cabecerSelected==null) nuevaFactura();
		
		
		cabecerSelected.setTotaliva(BigDecimal.ZERO);
		cabecerSelected.setTotalice(BigDecimal.ZERO);
		cabecerSelected.setTotaldescuento(BigDecimal.ZERO);
		cabecerSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		cabecerSelected.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : detalleFacList) {
			
			cabecerSelected.setTotalsinimpuestos(cabecerSelected.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			cabecerSelected.setTotaliva(cabecerSelected.getTotaliva().add(d.getValoriva()));
			cabecerSelected.setTotalice(cabecerSelected.getTotalice().add(d.getValorice()));
			cabecerSelected.setTotaldescuento(cabecerSelected.getTotaldescuento().add(d.getDescuento()));
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		cabecerSelected.setTotaliva(cabecerSelected.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalice(cabecerSelected.getTotalice().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotaldescuento(cabecerSelected.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalsinimpuestos(cabecerSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalconimpuestos(cabecerSelected.getTotalsinimpuestos().add(cabecerSelected.getTotaliva()).add(cabecerSelected.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
	}
	
	private Detalle existeProductoLista() {
		for (Detalle d : detalleFacList) {
			if(d.getProducto()!=null && d.getProducto().getIdproducto().equals(productoSelected.getIdproducto())) {
				return d;
			}
		}
		
		return null;
	}
	
	public void agregarItem() {
		try {
			
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			
			consultarIce();
			consultarIva();
			
			detalleSelected = new Detalle();
			detalleSelected.setCantidad(BigDecimal.valueOf(1));
			detalleSelected.setDescuento(BigDecimal.ZERO);
			detalleSelected.setDescripcion("");
			detalleSelected.setPreciounitario(BigDecimal.ZERO);
			detalleSelected.setProducto(null);
			detalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			detalleSelected.setIce(iceList.stream().filter(x->x.getValor().doubleValue()==0d).findFirst().get());
			detalleFacList.add(detalleSelected);
			
			calcularItem(detalleSelected, false);
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDetalle() {
		try {
			
			// eliminar datos de la base de datos
			
			// eliminar
			
			for (Detalle p : detalleFacList) {
				if(detalleSelected.getIddetalle().equals(p.getIddetalle())) {
					break;
				}
			}
			
			if(cabecerSelected.getDetalleEliminarList()==null) {
				cabecerSelected.setDetalleEliminarList(new ArrayList<>());
			}
			detalleSelected.setIdUsuarioEliminacion(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.getDetalleEliminarList().add(detalleSelected);
			detalleFacList.remove(detalleSelected);
			if(detalleFacList.isEmpty()) {
				detalleSelected=null;
			}else {
				detalleSelected = detalleFacList.get(detalleFacList.size()-1);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the clienteServicio
	 */
	public ClienteServicio getClienteServicio() {
		return clienteServicio;
	}

	/**
	 * @param clienteServicio the clienteServicio to set
	 */
	public void setClienteServicio(ClienteServicio clienteServicio) {
		this.clienteServicio = clienteServicio;
	}

	/**
	 * @return the cabecerSelected
	 */
	public Cabecera getCabecerSelected() {
		return cabecerSelected;
	}

	/**
	 * @param cabecerSelected the cabecerSelected to set
	 */
	public void setCabecerSelected(Cabecera cabecerSelected) {
		this.cabecerSelected = cabecerSelected;
	}

	/**
	 * @return the criterioCliente
	 */
	public String getCriterioCliente() {
		return criterioCliente;
	}

	/**
	 * @param criterioCliente the criterioCliente to set
	 */
	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
	}

	/**
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the pagoSelected
	 */
	public Pago getPagoSelected() {
		return pagoSelected;
	}

	/**
	 * @param pagoSelected the pagoSelected to set
	 */
	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	/**
	 * @return the totalPago
	 */
	public BigDecimal getTotalPago() {
		return totalPago;
	}

	/**
	 * @param totalPago the totalPago to set
	 */
	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	/**
	 * @return the detalleFacList
	 */
	public List<Detalle> getDetalleFacList() {
		return detalleFacList;
	}

	/**
	 * @param detalleFacList the detalleFacList to set
	 */
	public void setDetalleFacList(List<Detalle> detalleFacList) {
		this.detalleFacList = detalleFacList;
	}

	/**
	 * @return the detalleSelected
	 */
	public Detalle getDetalleSelected() {
		return detalleSelected;
	}

	/**
	 * @param detalleSelected the detalleSelected to set
	 */
	public void setDetalleSelected(Detalle detalleSelected) {
		this.detalleSelected = detalleSelected;
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
	 * @return the iceList
	 */
	public List<Ice> getIceList() {
		return iceList;
	}

	/**
	 * @param iceList the iceList to set
	 */
	public void setIceList(List<Ice> iceList) {
		this.iceList = iceList;
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

}
