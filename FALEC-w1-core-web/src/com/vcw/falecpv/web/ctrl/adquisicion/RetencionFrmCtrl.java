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
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.Retencion;
import com.vcw.falecpv.core.modelo.persistencia.Retenciondetalle;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
import com.vcw.falecpv.core.servicio.RetencionServicio;
import com.vcw.falecpv.core.servicio.RetenciondetalleServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestoServicio;
import com.vcw.falecpv.core.servicio.RetencionimpuestodetServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class RetencionFrmCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820370076797199304L;

	@EJB
	private RetencionimpuestoServicio retencionimpuestoServicio;
	
	@EJB
	private RetencionimpuestodetServicio retencionimpuestodetServicio;
	
	@EJB
	private ProveedorServicio proveedorServicio;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private RetencionServicio retencionServicio;
	
	@EJB
	private RetenciondetalleServicio retenciondetalleServicio;
	
	
	private String callModule;
	private String viewUpdate;
	private Adquisicion adquisicionSelected = new Adquisicion();
	private List<Proveedor> proveedorList;
	private List<Retencionimpuesto> retencionimpuestoList;
	private List<Retencionimpuestodet> retencionimpuestodetList;
	private List<Tipocomprobante> tipocomprobanteList;
	private Retencion retencionSeleccion;
	private Retencionimpuesto retencionimpuesto;
	private List<Retenciondetalle> retenciondetalleList;
	private Retenciondetalle retenciondetalleSelected;
	private RetencionMainCtrl retencionMainCtrl;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private AdquisicionMainCtrl adquisicionMainCtrl;
	
	
	/**
	 * 
	 */
	public RetencionFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			callModule = "RETENCION";
			retencionSeleccion = new Retencion();
			retenciondetalleSelected = new Retenciondetalle();
			consultarProveedor();
			consultarTipoComprobante();
			consultarRetencionImpuesto();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarProveedor()throws DaoException {
		
		setProveedorList(proveedorServicio.getProveedorDao().getByConsulta(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), EstadoRegistroEnum.ACTIVO, null));
		
	}
	
	public void consultarTipoComprobante()throws DaoException{
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
						TipoComprobanteEnum.RETENCION));
	}
	
	public void consultarRetencionImpuesto()throws DaoException {
		retencionimpuestoList = null;
		retencionimpuestoList = retencionimpuestoServicio.getRetencionimpuestoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}
	
	public void consultarRetencionDetalleImpuesto() {
		try {
			
			retencionimpuestodetList = null;
			if(retencionimpuesto!=null) {
				retencionimpuestodetList = retencionimpuestodetServicio.getRetencionimpuestodetDao()
						.getByRetencionEstado(retencionimpuesto.getIdretencionimpuesto(), EstadoRegistroEnum.ACTIVO);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevaRetencionDispacher() throws DaoException {
		if(adquisicionSelected!=null) {
			Retencion r = retencionServicio.getByAdquisicionEstado(adquisicionSelected.getIdadquisicion());
			
			if(r!=null) {
				editarRetencion(r.getIdretencion());
			}else {
				nuevaRetencion();
			}
		}else {
			nuevaRetencion();
		}
			
	}
	
	private void nuevaRetencion()throws DaoException {
		
		consultarProveedor();
		consultarTipoComprobante();
		consultarRetencionImpuesto();
		
		retencionimpuesto = null;
		retencionSeleccion = new Retencion();
		retencionSeleccion.setAdquisicion(null);
		retencionSeleccion.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		retencionSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		retencionSeleccion.setFechaemision(new Date());
		retencionSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		retencionSeleccion.setTotalbaseimponible(BigDecimal.ZERO);
		retencionSeleccion.setTotalretencion(BigDecimal.ZERO);
		
		if(adquisicionSelected!=null) {
			retencionSeleccion.setFechaemision(adquisicionSelected.getFecha());
			retencionSeleccion.setNumfactura(adquisicionSelected.getNumfactura());
			retencionSeleccion.setProveedor(adquisicionSelected.getProveedor());
			retencionSeleccion.setTipocomprobante(adquisicionSelected.getTipocomprobante());
		}
		retenciondetalleList = null;
		nuevaRetencionDetalle();
		determinarPeriodoFiscal();
	}
	
	private void determinarPeriodoFiscal() {
		retencionSeleccion.setAnio(FechaUtil.getAnio(retencionSeleccion.getFechaemision()).toString());
		retencionSeleccion.setMes(FechaUtil.getMes(retencionSeleccion.getFechaemision()));
	}
	
	private void nuevaRetencionDetalle() {
		retenciondetalleSelected = new Retenciondetalle();
		retenciondetalleSelected = new Retenciondetalle();
		retenciondetalleSelected.setBaseimponible(new BigDecimal(0));
		if(adquisicionSelected!=null && retencionimpuesto!=null) {
			if(retencionimpuesto.getIdretencionimpuesto().equals("1")) {
				retenciondetalleSelected.setBaseimponible(adquisicionSelected.getTotaliva());
			}else if(retencionimpuesto.getIdretencionimpuesto().equals("2")) {
				retenciondetalleSelected.setBaseimponible(adquisicionSelected.getSubtotal()
						.add(adquisicionSelected.getTotaldescuento().negate()).setScale(2, RoundingMode.HALF_UP));
			}
		}
		retenciondetalleSelected.setPorcentaje(new BigDecimal(0));
		retenciondetalleSelected.setValor(new BigDecimal(0));
		retenciondetalleSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		
	}
	
	public void cambioImpuesto() {
		try {
			nuevaRetencionDetalle();
			retenciondetalleSelected.setRetencionimpuestodet(null);
			consultarRetencionDetalleImpuesto();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cambioFechaEmision() {
		try {
			determinarPeriodoFiscal();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void calcularImpuesto() {
		if(retenciondetalleSelected.getRetencionimpuestodet()!=null) {
			retenciondetalleSelected.setPorcentaje(retenciondetalleSelected.getRetencionimpuestodet().getValor());
			retenciondetalleSelected.setValor(retenciondetalleSelected.getPorcentaje().divide(BigDecimal.valueOf(100))
					.multiply(retenciondetalleSelected.getBaseimponible()).setScale(2, RoundingMode.HALF_UP));
		}
	}
	
	public void cambioCalcularImpuesto() {
		try {
			
			calcularImpuesto();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	@Override
	public void guardar() {
		try {
			
			// TODO datos 
			retencionSeleccion.setNumautorizacion("111111111111111111");
			//retencionSeleccion.setNumcomprobante("222222222222222222");
			
			// verifica numfactura proveedor establecimeinto
			if (retencionServicio.existeRetencionProveedor(retencionSeleccion.getIdretencion(),
					retencionSeleccion.getEstablecimiento().getIdestablecimiento(),
					retencionSeleccion.getProveedor().getIdproveedor(), retencionSeleccion.getNumfactura())) {
				
				AppJsfUtil.addErrorMessage("formMain", "ERROR",
						"YA EXISTE UNA RETENCION DE LA FACTURA : " + retencionSeleccion.getNumfactura()
								+ " DEL PROVEEDOR : " + retencionSeleccion.getProveedor().getNombrecomercial());
				return;
				
			}
			
			// verifica si existe la adquisicion
			if(adquisicionSelected==null) {
				adquisicionSelected = adquisicionServicio.getByFactura(
						retencionSeleccion.getProveedor().getIdproveedor(), retencionSeleccion.getNumfactura(),
						retencionSeleccion.getEstablecimiento().getIdestablecimiento());
			}
			
			if(retencionSeleccion.getIdretencion()!=null) {
				String analisisEstado = retencionServicio.analizarEstado(retencionSeleccion.getIdretencion(), retencionSeleccion.getEstablecimiento().getIdestablecimiento(), "GUARDAR");
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
			}
			
			if(adquisicionSelected!=null) {
				
				// validar estado de la adquisicion
				retencionSeleccion.setAdquisicion(adquisicionSelected);
				
				String analisisEstado = adquisicionServicio.analizarEstado(adquisicionSelected.getIdadquisicion(), adquisicionSelected.getEstablecimiento().getIdestablecimiento(), "RETENCION");
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
				// verificar si ya existe un aretencion a la adquisicion
				Retencion r = retencionServicio.getByAdquisicionEstado(adquisicionSelected.getIdadquisicion());
				if(r!=null && retencionSeleccion.getIdretencion()!=null) {
					if(!r.getIdretencion().equals(retencionSeleccion.getIdretencion())) {
						AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE GUARDAR, YA EXISTE UNA RETENCION DE LA COMPRA CONE DOCUMENTO : " + retencionSeleccion.getNumfactura());
						return;
					}
				}
				
			}else {
				
				retencionSeleccion.setAdquisicion(null);
				
			}
			
			// si no existe detalle
			if(retenciondetalleList==null || retenciondetalleList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE GUARDAR, NO EXISTE DETALLE DE RETENCIONCIONES");
				return;
			}
			
			retencionSeleccion.setUpdated(new Date());
			
			for (Retenciondetalle rd : retenciondetalleList) {
				rd.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
				rd.setUpdated(new Date());
			}
			
			// guarda los datos
			retencionSeleccion = retencionServicio.guardarFacade(retencionSeleccion, retenciondetalleList, adquisicionSelected);
			
			switch (callModule) {
			case "RETENCION":
				
				retencionMainCtrl.consultarRetenciones();
				
				break;
			
			case "ADQUISICION":
				
				adquisicionFrmCtrl = (AdquisicionFrmCtrl) AppJsfUtil.getManagedBean("adquisicionFrmCtrl");
				adquisicionFrmCtrl.editarAdquisicion(adquisicionSelected.getIdadquisicion());
				adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
				adquisicionMainCtrl.consultarAdquisiciones();
				retencionMainCtrl.consultarRetenciones();
				
				break;

			default:
				break;
			}
			
			AppJsfUtil.addInfoMessage("formMain", "OK","REGISTRO GUARDADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void editarRetencion(String idRetencion)throws DaoException{
		
		retencionSeleccion = retencionServicio.consultarByPk(idRetencion);
		retenciondetalleList = retenciondetalleServicio.getByRetencion(idRetencion);
		adquisicionSelected = null;
		if(retencionSeleccion.getAdquisicion()!=null) {
			adquisicionSelected = adquisicionServicio.consultarByPk(retencionSeleccion.getAdquisicion().getIdadquisicion());
		}
		nuevaRetencionDetalle();
		consultarProveedor();
		consultarTipoComprobante();
		consultarRetencionImpuesto();
		totalizar();
	}
	
	@Override
	public void nuevo() {
		try {
			nuevaRetencion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	
	public void agregarDetalle() {
		try {
			
			if(retencionimpuesto==null) {
				AppJsfUtil.addErrorMessage("formMain:somRtnImpuesto", "", "REQUERIDO");
				return;
			}
			
			if(retenciondetalleSelected.getRetencionimpuestodet()==null) {
				AppJsfUtil.addErrorMessage("formMain:somRtnImpuestoDet", "", "REQUERIDO");
				return;
			}
			
			if(retenciondetalleList==null) {
				retenciondetalleList = new ArrayList<>();
			}
			
			// validar si ya existe el impuesto
			for (Retenciondetalle rd : retenciondetalleList) {
				if(rd.getRetencionimpuestodet().getCodigo().equals(retenciondetalleSelected.getRetencionimpuestodet().getCodigo())) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "YA EXISTE EL CODIGO DE RETENCION");
					return;
				}
			}
			
			retenciondetalleList.add(retenciondetalleSelected);
			
			retencionimpuesto = null;
			nuevaRetencionDetalle();
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() {
		retencionSeleccion.setTotalretencion(BigDecimal.ZERO);
		retencionSeleccion.setTotalbaseimponible(BigDecimal.ZERO);
		int i = 0;
		for (Retenciondetalle rt : retenciondetalleList) {
			if(rt.getIdretenciondetalle()!=null && rt.getIdretenciondetalle().contains("MM")) {
				rt.setIdretenciondetalle("MM" + i);
			}
			retencionSeleccion.setTotalretencion(retencionSeleccion.getTotalretencion().add(rt.getValor()));
			retencionSeleccion.setTotalbaseimponible(retencionSeleccion.getTotalbaseimponible().add(rt.getBaseimponible()));
			i++;
		}
		
		retencionSeleccion.getTotalbaseimponible().setScale(0, RoundingMode.HALF_UP);
		retencionSeleccion.getTotalretencion().setScale(0, RoundingMode.HALF_UP);
	}
	
	public void eliminarDetalle() {
		
		try {
			
			
			// verifica estado de la retencion
			
			if(retencionSeleccion.getIdretencion()!=null) {
				
				String analisisEstado = retencionServicio.analizarEstado(retencionSeleccion.getIdretencion(), retencionSeleccion.getEstablecimiento().getIdestablecimiento(), "ELIMINAR_DETALLE");
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			if(retenciondetalleSelected.getIdretenciondetalle()!=null && !retenciondetalleSelected.getIdretenciondetalle().contains("MM")) {
				retenciondetalleServicio.eliminar(retenciondetalleSelected);
			}
			
			retenciondetalleList.remove(retenciondetalleSelected);
			retencionimpuesto = null;
			nuevaRetencionDetalle();
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}

	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
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
	 * @return the retencionimpuestoList
	 */
	public List<Retencionimpuesto> getRetencionimpuestoList() {
		return retencionimpuestoList;
	}

	/**
	 * @param retencionimpuestoList the retencionimpuestoList to set
	 */
	public void setRetencionimpuestoList(List<Retencionimpuesto> retencionimpuestoList) {
		this.retencionimpuestoList = retencionimpuestoList;
	}

	/**
	 * @return the retencionimpuestodetList
	 */
	public List<Retencionimpuestodet> getRetencionimpuestodetList() {
		return retencionimpuestodetList;
	}

	/**
	 * @param retencionimpuestodetList the retencionimpuestodetList to set
	 */
	public void setRetencionimpuestodetList(List<Retencionimpuestodet> retencionimpuestodetList) {
		this.retencionimpuestodetList = retencionimpuestodetList;
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
	 * @return the retencionSeleccion
	 */
	public Retencion getRetencionSeleccion() {
		return retencionSeleccion;
	}

	/**
	 * @param retencionSeleccion the retencionSeleccion to set
	 */
	public void setRetencionSeleccion(Retencion retencionSeleccion) {
		this.retencionSeleccion = retencionSeleccion;
	}

	/**
	 * @return the retencionimpuesto
	 */
	public Retencionimpuesto getRetencionimpuesto() {
		return retencionimpuesto;
	}

	/**
	 * @param retencionimpuesto the retencionimpuesto to set
	 */
	public void setRetencionimpuesto(Retencionimpuesto retencionimpuesto) {
		this.retencionimpuesto = retencionimpuesto;
	}

	/**
	 * @return the retenciondetalleList
	 */
	public List<Retenciondetalle> getRetenciondetalleList() {
		return retenciondetalleList;
	}

	/**
	 * @param retenciondetalleList the retenciondetalleList to set
	 */
	public void setRetenciondetalleList(List<Retenciondetalle> retenciondetalleList) {
		this.retenciondetalleList = retenciondetalleList;
	}

	/**
	 * @return the retenciondetalleSelected
	 */
	public Retenciondetalle getRetenciondetalleSelected() {
		return retenciondetalleSelected;
	}

	/**
	 * @param retenciondetalleSelected the retenciondetalleSelected to set
	 */
	public void setRetenciondetalleSelected(Retenciondetalle retenciondetalleSelected) {
		this.retenciondetalleSelected = retenciondetalleSelected;
	}

	/**
	 * @return the retencionMainCtrl
	 */
	public RetencionMainCtrl getRetencionMainCtrl() {
		return retencionMainCtrl;
	}

	/**
	 * @param retencionMainCtrl the retencionMainCtrl to set
	 */
	public void setRetencionMainCtrl(RetencionMainCtrl retencionMainCtrl) {
		this.retencionMainCtrl = retencionMainCtrl;
	}

	/**
	 * @return the adquisicionFrmCtrl
	 */
	public AdquisicionFrmCtrl getAdquisicionFrmCtrl() {
		return adquisicionFrmCtrl;
	}

	/**
	 * @param adquisicionFrmCtrl the adquisicionFrmCtrl to set
	 */
	public void setAdquisicionFrmCtrl(AdquisicionFrmCtrl adquisicionFrmCtrl) {
		this.adquisicionFrmCtrl = adquisicionFrmCtrl;
	}

	/**
	 * @return the adquisicionMainCtrl
	 */
	public AdquisicionMainCtrl getAdquisicionMainCtrl() {
		return adquisicionMainCtrl;
	}

	/**
	 * @param adquisicionMainCtrl the adquisicionMainCtrl to set
	 */
	public void setAdquisicionMainCtrl(AdquisicionMainCtrl adquisicionMainCtrl) {
		this.adquisicionMainCtrl = adquisicionMainCtrl;
	}

}
