/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.CabeceraRetencionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ImpuestoretencionServicio;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
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
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private CabeceraRetencionServicio cabeceraRetencionServicio;
	
	@EJB
	private ImpuestoretencionServicio impuestoretencionServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	private String callModule;
	private String viewUpdate;
	private Adquisicion adquisicionSelected = new Adquisicion();
	private List<Proveedor> proveedorList;
	private List<Retencionimpuesto> retencionimpuestoList;
	private List<Retencionimpuestodet> retencionimpuestodetList;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera retencionSeleccion;
	private Retencionimpuesto retencionimpuesto;
	private List<Impuestoretencion> retenciondetalleList;
	private Impuestoretencion retenciondetalleSelected;
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
			retencionSeleccion = new Cabecera();
			retenciondetalleSelected = new Impuestoretencion();
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
				.getByEmpresaFormulario(TipoComprobanteEnum.RETENCION));
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
			Cabecera r = cabeceraRetencionServicio.getByAdquisicionEstado(adquisicionSelected.getIdadquisicion());
			
			if(r!=null) {
				editarRetencion(r.getIdcabecera());
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
		retencionSeleccion = new Cabecera();
		retencionSeleccion.setAdquisicion(null);
		retencionSeleccion.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		retencionSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		retencionSeleccion.setFechaemision(new Date());
		retencionSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		retencionSeleccion.setTotalbaseimponible(BigDecimal.ZERO);
		retencionSeleccion.setTotalretencion(BigDecimal.ZERO);
		inicializarSecuencia(retencionSeleccion);
		
		if(adquisicionSelected!=null) {
			retencionSeleccion.setFechaemision(adquisicionSelected.getFecha());
			retencionSeleccion.setNumfactura(adquisicionSelected.getNumfactura());
			retencionSeleccion.setProveedor(adquisicionSelected.getProveedor());
			retencionSeleccion.setTipocomprobanteretencion(adquisicionSelected.getTipocomprobante());
		}
		retencionSeleccion.setFechaemisiondocasociado(retencionSeleccion.getFechaemision());
		retenciondetalleList = null;
		nuevaRetencionDetalle();
		determinarPeriodoFiscal();
	}
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		retencionSeleccion.setPeriodofiscal(sf.format(retencionSeleccion.getFechaemisiondocasociado()));
	}
	
	private void nuevaRetencionDetalle() {
		retenciondetalleSelected = new Impuestoretencion();
		retenciondetalleSelected = new Impuestoretencion();
		retenciondetalleSelected.setBaseimponible(new BigDecimal(0));
		if(adquisicionSelected!=null && retencionimpuesto!=null) {
			if(retencionimpuesto.getIdretencionimpuesto().equals("1")) {
				retenciondetalleSelected.setBaseimponible(adquisicionSelected.getTotaliva());
			}else if(retencionimpuesto.getIdretencionimpuesto().equals("2")) {
				retenciondetalleSelected.setBaseimponible(adquisicionSelected.getSubtotal()
						.add(adquisicionSelected.getTotaldescuento().negate()).setScale(2, RoundingMode.HALF_UP));
			}
		}
		retenciondetalleSelected.setPorcentajeretener(new BigDecimal(0));
		retenciondetalleSelected.setValorretenido(new BigDecimal(0));
		
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
			retenciondetalleSelected.setPorcentajeretener(retenciondetalleSelected.getRetencionimpuestodet().getValor());
			retenciondetalleSelected.setValorretenido(retenciondetalleSelected.getPorcentajeretener().divide(BigDecimal.valueOf(100))
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
			
			// verifica numfactura proveedor establecimeinto
			if (cabeceraRetencionServicio.existeRetencionProveedor(retencionSeleccion.getIdcabecera(),
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
			
			if(retencionSeleccion.getIdcabecera()!=null) {
				String analisisEstado = cabeceraRetencionServicio.analizarEstado(retencionSeleccion.getIdcabecera(), retencionSeleccion.getEstablecimiento().getIdestablecimiento(), "GUARDAR");
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
				Cabecera r = cabeceraRetencionServicio.getByAdquisicionEstado(adquisicionSelected.getIdadquisicion());
				if(r!=null && retencionSeleccion.getIdcabecera()!=null) {
					if(!r.getIdcabecera().equals(retencionSeleccion.getIdcabecera())) {
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
			retencionSeleccion.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.RETENCION);
			retencionSeleccion.setSecuencial(null);
			populateretencion();
			// guarda los datos
			retencionSeleccion = cabeceraServicio.guardarComprobanteFacade(retencionSeleccion);
			noEditarSecuencial(retencionSeleccion);
			
			// Manage de session para actualizar las pantallas
			adquisicionFrmCtrl = (AdquisicionFrmCtrl) AppJsfUtil.getManagedBean("adquisicionFrmCtrl");
			adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			
			switch (callModule) {
			case "RETENCION":
				
				retencionMainCtrl.consultarRetenciones();
				adquisicionMainCtrl.consultarAdquisiciones();
				
				break;
			
			case "ADQUISICION":
				
				adquisicionFrmCtrl.editarAdquisicion(adquisicionSelected.getIdadquisicion());
				adquisicionMainCtrl.consultarAdquisiciones();
				retencionMainCtrl.consultarRetenciones();
				
				break;

			default:
				break;
			}
			
			AppJsfUtil.addInfoMessage("formMain", "OK","REGISTRO GUARDADO CORRECTAMENTE.");
			
		}  catch (ExisteNumDocumentoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void populateretencion() throws DaoException, ParametroRequeridoException {
		
		retencionSeleccion.setTipoemision("1");
		retencionSeleccion.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.RETENCION));
		retencionSeleccion.setEstablecimiento(establecimientoServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
		retencionSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		retencionSeleccion.setContribuyenteespecial("5368");
		retencionSeleccion.setMoneda("DOLAR");
		retencionSeleccion.setPropina(BigDecimal.ZERO);
		retencionSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		
		// detalle retencion
		retencionSeleccion.setImpuestoretencionList(retenciondetalleList);
		for (Impuestoretencion i : retencionSeleccion.getImpuestoretencionList()) {
			i.setCoddocsustento(retencionSeleccion.getTipocomprobanteretencion().getIdentificador());
			i.setNumdocsustento(retencionSeleccion.getNumfactura());
			i.setFechaemisiondocsustento(retencionSeleccion.getFechaemisiondocasociado());
		}
		
		// infromacion adicional 
		retencionSeleccion.setInfoadicionalList(ComprobanteHelper.determinarInfoAdicional(retencionSeleccion));
		
		retencionSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		retencionSeleccion.setUpdated(new Date());
		determinarPeriodoFiscal();
	}
	

	public void editarRetencion(String idRetencion)throws DaoException{
		
		retencionSeleccion = cabeceraServicio.consultarByPk(idRetencion);
		retenciondetalleList = cabeceraRetencionServicio.getDetalleById(idRetencion);
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
			for (Impuestoretencion rd : retenciondetalleList) {
				if(rd.getRetencionimpuestodet().getCodigo().equals(retenciondetalleSelected.getRetencionimpuestodet().getCodigo())) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", "YA EXISTE EL CODIGO DE RETENCION");
					return;
				}
			}
			
			retenciondetalleSelected.setCodigo(retencionimpuesto.getCodigo());
			retenciondetalleSelected.setCodigoretencion(retenciondetalleSelected.getCodigo());
			
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
		for (Impuestoretencion rt : retenciondetalleList) {
			if(rt.getIdimpuestoretencion()!=null && rt.getIdimpuestoretencion().contains("MM")) {
				rt.setIdimpuestoretencion("MM" + i);
			}
			retencionSeleccion.setTotalretencion(retencionSeleccion.getTotalretencion().add(rt.getValorretenido()));
			retencionSeleccion.setTotalbaseimponible(retencionSeleccion.getTotalbaseimponible().add(rt.getBaseimponible()));
			i++;
		}
		
		retencionSeleccion.getTotalbaseimponible().setScale(0, RoundingMode.HALF_UP);
		retencionSeleccion.getTotalretencion().setScale(0, RoundingMode.HALF_UP);
	}
	
	public void eliminarDetalle() {
		
		try {
			
			
			// verifica estado de la retencion
			
			if(retencionSeleccion.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraRetencionServicio.analizarEstado(retencionSeleccion.getIdcabecera(), retencionSeleccion.getEstablecimiento().getIdestablecimiento(), "ELIMINAR_DETALLE");
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
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
	
	public void editarSecuencialAction() {
		try {
			
			editarSecuencial(retencionSeleccion, "formMain:intSecDocumento");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void noEditarSecuencialAction() {
		try {
			
			noEditarSecuencial(retencionSeleccion);
			
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

	/**
	 * @return the retencionSeleccion
	 */
	public Cabecera getRetencionSeleccion() {
		return retencionSeleccion;
	}

	/**
	 * @param retencionSeleccion the retencionSeleccion to set
	 */
	public void setRetencionSeleccion(Cabecera retencionSeleccion) {
		this.retencionSeleccion = retencionSeleccion;
	}

	/**
	 * @return the retenciondetalleList
	 */
	public List<Impuestoretencion> getRetenciondetalleList() {
		return retenciondetalleList;
	}

	/**
	 * @param retenciondetalleList the retenciondetalleList to set
	 */
	public void setRetenciondetalleList(List<Impuestoretencion> retenciondetalleList) {
		this.retenciondetalleList = retenciondetalleList;
	}

	/**
	 * @return the retenciondetalleSelected
	 */
	public Impuestoretencion getRetenciondetalleSelected() {
		return retenciondetalleSelected;
	}

	/**
	 * @param retenciondetalleSelected the retenciondetalleSelected to set
	 */
	public void setRetenciondetalleSelected(Impuestoretencion retenciondetalleSelected) {
		this.retenciondetalleSelected = retenciondetalleSelected;
	}

}
