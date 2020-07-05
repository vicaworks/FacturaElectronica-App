/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nd;

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

import org.omnifaces.util.Ajax;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.constante.contadores.TCAleatorio;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.MotivoServicio;
import com.vcw.falecpv.core.servicio.NotaDebitoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.core.servicio.TotalimpuestoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class NotaDebitoFrmCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5271954796795645087L;
	
	@EJB
	private NotaDebitoServicio notaDebitoServicio;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private MotivoServicio motivoServicio;
	
	@EJB
	private TotalimpuestoServicio totalimpuestoServicio;
	
	
	private List<Iva> ivaList;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private String criterioCliente;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera notDebitoSelected;
	private List<Motivo> motivoList;
	private Motivo motivoSelected;
	private Totalimpuesto totalimpuesto;

	/**
	 * 
	 */
	public NotaDebitoFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			nuevaNotDebito();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarTipoComprobante() throws DaoException {
		tipocomprobanteList = null;
		tipocomprobanteList = tipocomprobanteServicio.getTipocomprobanteDao().getByEmpresaFormulario(TipoComprobanteEnum.NOTA_DEBITO);
	}
	
	public void nuevaNotDebito() throws DaoException{
		
		consultarTipoComprobante();
		consultarIva();
		
		notDebitoSelected = new Cabecera();
		notDebitoSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		notDebitoSelected.setTotal(BigDecimal.ZERO);
		notDebitoSelected.setTotaldescuento(BigDecimal.ZERO);
		notDebitoSelected.setTotaliva(BigDecimal.ZERO);
		notDebitoSelected.setTotalice(BigDecimal.ZERO);
		notDebitoSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setTotalconimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setDetalleList(new ArrayList<>());
		notDebitoSelected.setFechaemision(new Date());
		notDebitoSelected.setCliente(new Cliente());
		totalimpuesto = new Totalimpuesto();
		totalimpuesto.setBaseimponible(BigDecimal.ZERO);
		totalimpuesto.setDescuentoadicional(BigDecimal.ZERO);
		totalimpuesto.setValor(BigDecimal.ZERO);
		totalimpuesto.setIva(ivaList.stream().filter(x->x.getValor().intValue() == 0 ).findFirst().orElse(null));
		motivoList = new ArrayList<>();;
		criterioCliente = null;
		motivoSelected = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		
	}
	
	public void buscarCliente() {
		try {
			
			if(notDebitoSelected==null) return;
			
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
	
	public void consultarCliente(String identificador) throws DaoException {
		notDebitoSelected.setCliente(null);
		notDebitoSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void buscarNumDocumento() {
		try {
			
			Cabecera c = cabeceraServicio.getCabeceraDao().getByTipoComprobante(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), notDebitoSelected.getTipocomprobanteretencion() ,notDebitoSelected.getNumdocasociado());
			if(c!=null) {
				nuevoByFacturaEmitida(c.getIdcabecera());
			}else {
				AppJsfUtil.addErrorMessage("formMain:inpNcNumFac", "ERROR", "NO EXISTE");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevoByFacturaEmitida(String idFactura)throws DaoException{
		
		
		nuevaNotDebito();
		Cabecera cabecera = cabeceraServicio.consultarByPk(idFactura);
		notDebitoSelected.setCliente(cabecera.getCliente());
		notDebitoSelected.setFechaemision(cabecera.getFechaemision());
		notDebitoSelected.setTipocomprobanteretencion(cabecera.getTipocomprobante());
		notDebitoSelected.setFechaemisiondocasociado(cabecera.getFechaemision());
		notDebitoSelected.setNumdocasociado(cabecera.getNumdocumento());
		
		totalizar();
		
	}
	
	@Override
	public void nuevo() {
		try {
			
			nuevaNotDebito();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarItem() {
		try {
			
			if(motivoList==null) {
				motivoList = new ArrayList<>();
			}
			
			consultarIva();
			
			motivoSelected = new Motivo();
			motivoSelected.setValor(BigDecimal.ZERO);
			
			motivoList.add(motivoSelected);
			
			calcularItem(motivoSelected);
			totalizar();
			Ajax.oncomplete("PrimeFaces.focus('formMain:notMotivoDT:" + (motivoList.size()-1) + ":intNdMotivo');");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularItemAction(Motivo det) {
		try {
			motivoSelected = det;
			calcularItem(motivoSelected);
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void calcularItem(Motivo dFac) {
		
		dFac.setValor(dFac.getValor());
		
	}
	
	private void totalizar() throws DaoException {
		if(notDebitoSelected==null) nuevaNotDebito();		
		
		notDebitoSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Motivo d : motivoList) {
			
			notDebitoSelected.setTotalsinimpuestos(notDebitoSelected.getTotalsinimpuestos().add(d.getValor()));
			
			if(d.getIdmotivo()==null || d.getIdmotivo().contains("MM")) {
				d.setIdmotivo("MM" + i);
			}
			
			i++;
		}
		
		notDebitoSelected.setTotalsinimpuestos(notDebitoSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		totalimpuesto.setBaseimponible(notDebitoSelected.getTotalsinimpuestos());
		if(totalimpuesto.getIva()!=null) {
			notDebitoSelected.setTotaliva(totalimpuesto.getIva().getValor().divide(BigDecimal.valueOf(100)).multiply(notDebitoSelected.getTotalsinimpuestos()).setScale(2, RoundingMode.HALF_UP));
			totalimpuesto.setValor(notDebitoSelected.getTotaliva());
		}else {
			notDebitoSelected.setTotaliva(BigDecimal.ZERO);
			totalimpuesto.setValor(BigDecimal.ZERO);
		}
		
		notDebitoSelected.setTotalconimpuestos(notDebitoSelected.getTotalsinimpuestos().add(notDebitoSelected.getTotaliva()).setScale(2, RoundingMode.HALF_UP));
		
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		
	}
	
	public void calcularIvaAction() {
		try {
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
			
			for (Motivo p : motivoList) {
				if(motivoSelected.getIdmotivo().equals(p.getIdmotivo())) {
					break;
				}
			}
			
			motivoList.remove(motivoSelected);
			if(motivoList.isEmpty()) {
				motivoSelected=null;
			}else {
				motivoSelected = motivoList.get(motivoList.size()-1);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		try {
			
			aplicarPago(tipoPagoFormularioEnum);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void aplicarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) throws DaoException {
		if(notDebitoSelected == null) {
			return;
		}
		
		if (notDebitoSelected.getTotalsinimpuestos().doubleValue()<=0) {
			return;
		}
		
		if (pagoList==null) {
			pagoList = new ArrayList<>();
		}
		
		Tipopago tp = tipopagoServicio.getByCodINterno(tipoPagoFormularioEnum);
		
		boolean flag = false;
		for (Pago p : pagoList) {
			if(p.getTipopago().equals(tp) && !tipoPagoFormularioEnum.isRepetir()) {
				pagoSelected = p;
				flag = true;
				break;
			}
		}
		
		totalizarPago();
		if(!flag) {
			pagoSelected = new Pago();
			pagoSelected.setTipoPagoFormularioEnum(tipoPagoFormularioEnum);
			pagoSelected.setCabecera(notDebitoSelected);
			pagoSelected.setTipopago(tp);
			pagoSelected.setTotal(notDebitoSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
			pagoSelected.setPlazo(BigDecimal.ZERO);
			pagoSelected.setUnidadtiempo("DIAS");
			pagoList.add(pagoSelected);
			totalizarPago();
		}
		
		switch (tipoPagoFormularioEnum) {
		case EFECTIVO:
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagValorEntrega_input');");
			break;
		case CREDITO:
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagPlazo_input');");
			break;	
		default:
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagValor_input');");
			break;
		}
	}
	
	private void totalizarPago() {
		
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		if(pagoList==null) {
			return;
		}
		int cont = 1;
		for (Pago p : pagoList) {
			if(p.getIdpago()==null || p.getIdpago().contains("MM")) {
				p.setIdpago("MM" + cont);
			}
			totalPago = totalPago.add(p.getTotal());
			cont++;
		}
		
		totalPago = totalPago.setScale(2, RoundingMode.HALF_UP);
		
		totalSaldo = notDebitoSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
		if(totalSaldo.doubleValue()<0) {
			totalSaldo = BigDecimal.ZERO;
		}
		
	}
	
	public void calcularCambioAction(Pago p) {
		try {
			pagoSelected = p;
			if(pagoSelected.getValorentrega().doubleValue()==0) {
				pagoSelected.setCambio(BigDecimal.ZERO);
			}else {
				pagoSelected.setCambio(pagoSelected.getValorentrega().add(pagoSelected.getTotal().negate()).setScale(2, RoundingMode.HALF_UP));
				if(pagoSelected.getCambio().doubleValue()<0) {
					pagoSelected.setCambio(BigDecimal.ZERO);
				}
			}
			totalizarPago();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void totalizarPagoaction(Pago p) {
		try {
			pagoSelected = p;
			if(pagoSelected.getTipoPagoFormularioEnum().equals(TipoPagoFormularioEnum.EFECTIVO)) {
				calcularCambioAction(p);
			}
			totalizarPago();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDetallePago() {
		
		try {
			
			for (Pago p : pagoList) {
				if(pagoSelected.getIdpago().equals(p.getIdpago())) {
					break;
				}
			}
			
			pagoList.remove(pagoSelected);
			if(pagoList.isEmpty()) {
				pagoSelected=null;
			}else {
				pagoSelected = pagoList.get(pagoList.size()-1);
			}
			
			totalizarPago();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public String editar(String idNotDebito) throws DaoException {
		
		nuevaNotDebito();
		notDebitoSelected = cabeceraServicio.consultarByPk(idNotDebito);
		
		if(notDebitoSelected==null) {
			return "NO EXISTE LA NOTA DEBITO";
		}
		
		motivoList = motivoServicio.getMotivoDao().getByIdCabecera(idNotDebito);
		totalimpuesto = totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idNotDebito).isEmpty()?null:totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idNotDebito).get(0);
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idNotDebito);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		});
		totalizar();
		totalizarPago();
		
		return null;
	}
	
	@Override
	public void guardar() {
		try {
			
			// validar estado
			if(notDebitoSelected.getIdcabecera()!=null) {
				
				String analisisEstado = notaDebitoServicio.analizarEstado(notDebitoSelected.getIdcabecera(),notDebitoSelected.getEstablecimiento().getIdestablecimiento(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			if(motivoList==null || motivoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE LISTA DE MOTIVOS.");
				return;
			}
			
			
			populateNotaDebito(GenTipoDocumentoEnum.NOTA_DEBITO);
			notDebitoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			notDebitoSelected.setUpdated(new Date());
			notDebitoSelected.setPagoList(pagoList);	
			notDebitoSelected.setDetalleEliminarList(null);
			notDebitoSelected.setDetalleList(null);
			notDebitoSelected = cabeceraServicio.guardarComprobanteFacade(notDebitoSelected);
			
			NotaDebitoCtrl notaDebitoCtrl = (NotaDebitoCtrl) AppJsfUtil.getManagedBean("notaDebitoCtrl");
			notaDebitoCtrl.consultar();
			
			AppJsfUtil.addInfoMessage("formMain", "OK", "GUARDADO CORRECTAMENTE");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}


	private void populateNotaDebito(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException{
		notDebitoSelected.setTipoemision("1");
		notDebitoSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		notDebitoSelected.setEstablecimiento(establecimientoServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
		notDebitoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		notDebitoSelected.setContribuyenteespecial("5368");
		notDebitoSelected.setMoneda("DOLAR");
		if(notDebitoSelected.getSecuencial()==null) {
			notDebitoSelected.setSecuencial(contadorPkServicio.generarNumeroDocumento(genTipoDocumentoEnum,
					AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
			// clave de acceso
			notDebitoSelected.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(notDebitoSelected,
					contadorPkServicio.generarContadorTabla(TCAleatorio.ALEATORIONOTADEBITO,
							notDebitoSelected.getEstablecimiento().getIdestablecimiento(), new Object[] { false })));
			notDebitoSelected.setNumdocumento(TextoUtil.leftPadTexto(notDebitoSelected.getEstablecimiento().getCodigoestablecimiento(),3, "0").concat("001").concat(notDebitoSelected.getSecuencial()));
			notDebitoSelected.setNumfactura(notDebitoSelected.getNumdocumento());
		}
		
		notDebitoSelected.setPropina(BigDecimal.ZERO);
		notDebitoSelected.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		
		if(notDebitoSelected.getTipocomprobanteretencion()!=null) {
			notDebitoSelected.setTipodocasociado(notDebitoSelected.getTipocomprobanteretencion().getIdentificador());
		}
		notDebitoSelected.setImportetotal(notDebitoSelected.getTotalconimpuestos());
		totalimpuesto.setBaseimponible(totalimpuesto.getIva().getValor());
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.add(totalimpuesto);
		notDebitoSelected.setTotalimpuestoList(totalimpuestoList);
		notDebitoSelected.setMotivoList(motivoList);
		// infromacion adicional 
		notDebitoSelected.setInfoadicionalList(ComprobanteHelper.determinarInfoAdicional(notDebitoSelected));
	}
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		notDebitoSelected.setPeriodofiscal(sf.format(notDebitoSelected.getFechaemision()));
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
	 * @return the motivoList
	 */
	public List<Motivo> getMotivoList() {
		return motivoList;
	}

	/**
	 * @param motivoList the motivoList to set
	 */
	public void setMotivoList(List<Motivo> motivoList) {
		this.motivoList = motivoList;
	}

	/**
	 * @return the motivoSelected
	 */
	public Motivo getMotivoSelected() {
		return motivoSelected;
	}

	/**
	 * @param motivoSelected the motivoSelected to set
	 */
	public void setMotivoSelected(Motivo motivoSelected) {
		this.motivoSelected = motivoSelected;
	}

	/**
	 * @return the totalimpuesto
	 */
	public Totalimpuesto getTotalimpuesto() {
		return totalimpuesto;
	}

	/**
	 * @param totalimpuesto the totalimpuesto to set
	 */
	public void setTotalimpuesto(Totalimpuesto totalimpuesto) {
		this.totalimpuesto = totalimpuesto;
	}

}