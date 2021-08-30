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
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ConfiguracionServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.MotivoServicio;
import com.vcw.falecpv.core.servicio.NotaDebitoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.core.servicio.TotalimpuestoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.common.RideCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
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
	
	@EJB
	private InfoadicionalServicio infoadicionalServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ConfiguracionServicio configuracionServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	
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
	private String callModule;
	private RideCtrl rideCtrl;

	/**
	 * 
	 */
	public NotaDebitoFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			if(establecimientoMain!=null) {
				nuevaNotDebito();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarTipoComprobante() throws DaoException {
		tipocomprobanteList = null;
		tipocomprobanteList = tipocomprobanteServicio.getTipocomprobanteDao().getByEmpresaFormulario(TipoComprobanteEnum.NOTA_DEBITO);
	}
	
	public void nuevaNotDebito() throws DaoException, NumberFormatException, ParametroRequeridoException{
		
		consultarTipoComprobante();
		consultarIva();
		populateTipoPago();
		notDebitoSelected = new Cabecera();
		notDebitoSelected.setEstablecimiento(establecimientoMain);
		notDebitoSelected.setTotal(BigDecimal.ZERO);
		notDebitoSelected.setTotaldescuento(BigDecimal.ZERO);
		notDebitoSelected.setTotaliva(BigDecimal.ZERO);
		notDebitoSelected.setTotalice(BigDecimal.ZERO);
		notDebitoSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setTotalconimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setDetalleList(new ArrayList<>());
		notDebitoSelected.setFechaemision(new Date());
		notDebitoSelected.setFechaemisiondocasociado(new Date());
		infoadicionalList = null;
		// estado borrador
		notDebitoSelected.setBorrador(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.ESTADO_BORRADOR, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento()));
		inicializarSecuencia(notDebitoSelected);
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
		enableAccion = false;
		// infoadicional configuracion
		notDebitoSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.NOTA_DEBITO));
		notDebitoSelected.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		configuracionServicio.populateInformacionAdicional(notDebitoSelected);
		infoadicionalList = notDebitoSelected.getInfoadicionalList();
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
				establecimientoMain.getEmpresa().getIdempresa()));
	}
	
	public void buscarNumDocumento() {
		try {
			
			Cabecera c = cabeceraServicio.getCabeceraDao().getByTipoComprobante(establecimientoMain.getIdestablecimiento(), notDebitoSelected.getTipocomprobanteretencion() ,notDebitoSelected.getNumdocasociado());
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
	
	public void nuevoByFacturaEmitida(String idFactura)throws DaoException, NumberFormatException, ParametroRequeridoException{
		
		
		nuevaNotDebito();
		Cabecera cabecera = cabeceraServicio.consultarByPk(idFactura);
		notDebitoSelected.setCliente(cabecera.getCliente());
		notDebitoSelected.setFechaemision(cabecera.getFechaemision());
		notDebitoSelected.setTipocomprobanteretencion(cabecera.getTipocomprobante());
		notDebitoSelected.setFechaemisiondocasociado(cabecera.getFechaemision());
		notDebitoSelected.setNumdocasociado(cabecera.getNumdocumento());
		notDebitoSelected.setBorrador(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.ESTADO_BORRADOR, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento()));
		inicializarSecuencia(notDebitoSelected);
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
	
	private void totalizar() throws DaoException, NumberFormatException, ParametroRequeridoException {
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
		notDebitoSelected.setTotalpagar(notDebitoSelected.getTotalconimpuestos().add(notDebitoSelected.getValorretenido().negate()).setScale(2, RoundingMode.HALF_UP));
//		if(totalPago!=null && totalPago.doubleValue()>0) {
//			totalizarPago();
//		}
		pagoList = null;
		totalPago = BigDecimal.ZERO;
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
	
	public String editar(String idNotDebito) throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		nuevaNotDebito();
		notDebitoSelected = cabeceraServicio.consultarByPk(idNotDebito);
		
		if(notDebitoSelected==null) {
			return "NO EXISTE LA NOTA DEBITO";
		}
		
		motivoList = motivoServicio.getMotivoDao().getByIdCabecera(idNotDebito);
		totalimpuesto = totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idNotDebito).isEmpty()?null:totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idNotDebito).get(0);
		infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idNotDebito);
		totalizar();
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idNotDebito);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		});
		totalizarPago();
		habilitarCrud(notDebitoSelected.getEstado());
		if (notDebitoSelected.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
			notDebitoSelected.setBorrador(true);
		}
		return null;
	}
	
	@Override
	public void guardar() {
		try {
			
			// validar cliente
			if(notDebitoSelected.getCliente()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DATOS DEL CLIENTE");
				return;
			}
			
			// validar estado
			if(notDebitoSelected.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarEstadoComprobante(notDebitoSelected.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			if(motivoList==null || motivoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE LISTA DE MOTIVOS.");
				return;
			}
			
			// validar el valor
			if(totalPago.doubleValue() != notDebitoSelected.getTotalpagar().doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO DIFERENTE AL VALOR A PAGAR.");
				return;
			}
			
			notDebitoSelected.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.NOTA_DEBITO);
			populateNotaDebito(GenTipoDocumentoEnum.NOTA_DEBITO);
			notDebitoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			notDebitoSelected.setUpdated(new Date());
			notDebitoSelected.setPagoList(pagoList);	
			notDebitoSelected.setDetalleEliminarList(null);
			notDebitoSelected.setDetalleList(null);
			notDebitoSelected = cabeceraServicio.guardarComprobanteFacade(notDebitoSelected);
			infoadicionalList = notDebitoSelected.getInfoadicionalList();
			noEditarSecuencial(notDebitoSelected);
			notDebitoSelected.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(notDebitoSelected);
			NotaDebitoCtrl notaDebitoCtrl = (NotaDebitoCtrl) AppJsfUtil.getManagedBean("notaDebitoCtrl");
			notaDebitoCtrl.consultar();
			
			if(notDebitoSelected.isBorrador()) {
				messageCtrl.cargarMenssage("AVISO", "BORRADOR DE NOTA DE " +  msg.getString("label.debito") + "  GENERADA CORRECTAMENTE.", "WARNING");
			}else {
				messageCtrl.cargarMenssage("OK", "NOTA DE " +  msg.getString("label.debito") + " GENERADA CORRECTAMENTE.", "OK");
			}
			
			
		}  catch (ExisteNumDocumentoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", e.getMessage());
			notDebitoSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			notDebitoSelected.setSecuencial(null);
			notDebitoSelected.setNumdocumento(null);
			notDebitoSelected.setClaveacceso(null);
			notDebitoSelected.setNumeroautorizacion(null);
		}  catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			notDebitoSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			notDebitoSelected.setSecuencial(null);
			notDebitoSelected.setNumdocumento(null);
			notDebitoSelected.setClaveacceso(null);
			notDebitoSelected.setNumeroautorizacion(null);
		}
	}


	private void populateNotaDebito(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException{
		notDebitoSelected.setTipoemision("1");
		notDebitoSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		notDebitoSelected.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		notDebitoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		notDebitoSelected.setContribuyenteespecial("5368");
		notDebitoSelected.setMoneda("DOLAR");
		notDebitoSelected.setPropina(BigDecimal.ZERO);
		if(notDebitoSelected.getIdcabecera()==null || (notDebitoSelected.getIdcabecera()!=null && 
														(notDebitoSelected.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString()) ||
														 notDebitoSelected.getEstado().equals(ComprobanteEstadoEnum.RECHAZADO_SRI.toString())
														)
													  )
				){
			if(notDebitoSelected.isBorrador()) {
				notDebitoSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			}else {
				notDebitoSelected.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
			}
		}
		notDebitoSelected.setTipodocasociado(notDebitoSelected.getTipocomprobanteretencion().getIdentificador());
		notDebitoSelected.setValorapagar(notDebitoSelected.getTotalconimpuestos());
		
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
		notDebitoSelected.setInfoadicionalList(infoadicionalList);
	}
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		notDebitoSelected.setPeriodofiscal(sf.format(notDebitoSelected.getFechaemision()));
	}
	
	public void editarSecuencialAction() {
		try {
			
			editarSecuencial(notDebitoSelected, "formMain:intSecDocumento");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void noEditarSecuencialAction() {
		try {
			
			noEditarSecuencial(notDebitoSelected);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void imprimir() {
		// verifica si solo debe de imprimir
		if(notDebitoSelected.getIdcabecera()!=null) {
			if(cabeceraServicio.getCabeceraDao().isImprimir(notDebitoSelected.getIdcabecera())){
				showRide();
				return;
			}
		}
		// pone el nuevo estado
		boolean estadoTemp = notDebitoSelected.isBorrador();
		notDebitoSelected.setBorrador(false);
		guardar();
		AppJsfUtil.ajaxUpdate("formMain");
		if(AppJsfUtil.existErrors()) {
			notDebitoSelected.setBorrador(estadoTemp);
			return;
		}
		showRide();
	}
	
	private void showRide() {
		// despliega el comprobante
		rideCtrl.setIdCabecera(notDebitoSelected.getIdcabecera());
		rideCtrl.setInicialComprobante("FAC-");
		rideCtrl.setNumComprobante(ComprobanteHelper.formatNumDocumento(notDebitoSelected.getNumdocumento()));
		rideCtrl.showRide();
		
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
	 * @return the rideCtrl
	 */
	public RideCtrl getRideCtrl() {
		return rideCtrl;
	}

	/**
	 * @param rideCtrl the rideCtrl to set
	 */
	public void setRideCtrl(RideCtrl rideCtrl) {
		this.rideCtrl = rideCtrl;
	}

}
