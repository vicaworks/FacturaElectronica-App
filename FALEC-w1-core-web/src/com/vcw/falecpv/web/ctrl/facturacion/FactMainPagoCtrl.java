/**
 * 
 */
package com.vcw.falecpv.web.ctrl.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class FactMainPagoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3828995775819666807L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	
	private Cabecera cabecerSelected;
	private String criterioCliente;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private String opcionSeleccionCalculadora = "VALOR";
	private boolean inicioCalculadora = false; 
	private String separadorDecimal = null;
	
	/**
	 * 
	 */
	public FactMainPagoCtrl() {
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
	
	public void encerar() {
		cabecerSelected = null;
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		opcionSeleccionCalculadora = "VALOR";
		inicioCalculadora = false; 
		separadorDecimal = null;
	}
	
	public void consultarCliente(String identificador) throws DaoException {
		cabecerSelected.setCliente(null);
		cabecerSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void initPago(Cabecera cabeceraFac) {
		cabecerSelected = cabeceraFac;
		criterioCliente = null;
	}
	
	private void totalizar() {
		
		totalPago = BigDecimal.ZERO;
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
	}
	
	public void agregarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		try {
			
			if(cabecerSelected == null) {
				return;
			}
			
			if (pagoList==null) {
				pagoList = new ArrayList<>();
			}
			Tipopago tp = tipopagoServicio.getByCodINterno(tipoPagoFormularioEnum,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			
			boolean flag = false;
			for (Pago p : pagoList) {
				if(p.getTipopago().equals(tp) && !tipoPagoFormularioEnum.isRepetir()) {
					pagoSelected = p;
					flag = true;
					break;
				}
			}
			
			totalizar();
			if(!flag) {
				pagoSelected = new Pago();
				pagoSelected.setCabecera(cabecerSelected);
				pagoSelected.setTipopago(tp);
				pagoSelected.setTotal(cabecerSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
				pagoList.add(pagoSelected);
				totalizar();
			}
			
			opcionSeleccionCalculadora = "VALOR";
			inicioCalculadora = false; 
			separadorDecimal = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public void ejecutarCalculadora(String valorCalculadora) {
		try {
			
			if(cabecerSelected==null || pagoList==null || pagoList.isEmpty()) {
				return;
			}
			
			switch (opcionSeleccionCalculadora) {
			case "VALOR":
				
				if(inicioCalculadora) {
					pagoSelected.setTotal(BigDecimal.valueOf(Double.parseDouble(valorCalculadora)));
					inicioCalculadora = false;
				}
				pagoSelected.setTotal(procesarCal(pagoSelected.getTotal(), valorCalculadora));
				break;
			case "CAMBIO":
				if(inicioCalculadora) {
					pagoSelected.setValorentrega(BigDecimal.valueOf(Double.parseDouble(valorCalculadora)));
					inicioCalculadora = false;
				}
				pagoSelected.setValorentrega(procesarCal(pagoSelected.getValorentrega(), valorCalculadora));
				calcularCambio();
				break;
			case "DOCUMENTO":
				if(inicioCalculadora) {
					pagoSelected.setNumerodocumento(valorCalculadora);
					inicioCalculadora = false;
				}
				pagoSelected.setNumerodocumento(pagoSelected.getNumerodocumento().concat(valorCalculadora));
				break;	
			default:
				break;
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private BigDecimal procesarCal(BigDecimal valor,String valorCalculadora) {
		
		Integer intPart = valor.intValue();
		Integer decimalpart = decimalPart(valor);
		
		if(decimalpart==0 && separadorDecimal==null) {
			
			if(intPart==0) {
				return BigDecimal.valueOf(Long.parseLong(valorCalculadora));
			}
			
			return BigDecimal.valueOf(Long.parseLong(intPart.toString().concat(valorCalculadora)));
					
		}
		
		if(separadorDecimal!=null) {
			
			if(decimalpart==0) {
				separadorDecimal = null;
				return BigDecimal.valueOf(Double.parseDouble(intPart.toString().concat(".").concat(valorCalculadora)));
			}
			
			separadorDecimal = null;
			return BigDecimal.valueOf(Double.parseDouble(
					intPart.toString().concat(".").concat(decimalpart.toString()).concat(valorCalculadora)));
		}
		
		return valor;
	}
	
	private Integer decimalPart(BigDecimal valor) {
		if(!valor.toString().contains(".")) {
			return 0;
		}
		String val = valor.toString().substring(valor.toString().lastIndexOf(".")+1, valor.toString().length());
		return Integer.parseInt(val);
	}
	
	public void seleccionOpcionCalculadora(String opcion) {
		try {
			
			if(cabecerSelected==null || pagoList==null || pagoList.isEmpty()) {
				return;
			}
			
			switch (opcion) {
			case "+1":				
				switch (opcionSeleccionCalculadora) {
				case "VALOR":
					pagoSelected.setTotal(pagoSelected.getTotal().add(BigDecimal.valueOf(1)));
					break;
				case "CAMBIO":
					pagoSelected.setCambio(pagoSelected.getCambio().add(BigDecimal.valueOf(1)));
					calcularCambio();
					break;

				default:
					break;
				}
				break;
			case "+5":
				switch (opcionSeleccionCalculadora) {
				case "VALOR":
					pagoSelected.setTotal(pagoSelected.getTotal().add(BigDecimal.valueOf(5)));
					break;
				case "CAMBIO":
					pagoSelected.setCambio(pagoSelected.getCambio().add(BigDecimal.valueOf(5)));
					calcularCambio();
					break;

				default:
					break;
				}
				break;
			case "+10":
				switch (opcionSeleccionCalculadora) {
				case "VALOR":
					pagoSelected.setTotal(pagoSelected.getTotal().add(BigDecimal.valueOf(10)));
					break;
				case "CAMBIO":
					pagoSelected.setCambio(pagoSelected.getCambio().add(BigDecimal.valueOf(10)));
					calcularCambio();
					break;

				default:
					break;
				}
				break;
			case "+20":
				switch (opcionSeleccionCalculadora) {
				case "VALOR":
					pagoSelected.setTotal(pagoSelected.getTotal().add(BigDecimal.valueOf(20)));
					break;
				case "CAMBIO":
					pagoSelected.setCambio(pagoSelected.getCambio().add(BigDecimal.valueOf(20)));
					calcularCambio();
					break;

				default:
					break;
				}
				break;
			case "back":
				if(opcionSeleccionCalculadora.equals("DOCUMENTO")) {
					
					if(pagoSelected.getNumerodocumento()!=null && pagoSelected.getNumerodocumento().length()>0) {
						pagoSelected.setNumerodocumento(pagoSelected.getNumerodocumento().substring(0, pagoSelected.getNumerodocumento().length()-1));
					}else {
						pagoSelected.setNumerodocumento("");
					}
					
				}else {
					
					Integer intPart = pagoSelected.getTotal().intValue();
					Integer decimalpart = decimalPart(pagoSelected.getTotal());
					BigDecimal valor = pagoSelected.getTotal();
					
					if(opcionSeleccionCalculadora.equals("CAMBIO")) {
						intPart = pagoSelected.getCambio().intValue();
						decimalpart = decimalPart(pagoSelected.getCambio());
						valor = pagoSelected.getCambio();
					}
					
					String valorCadena = valor.toString();
					
					if(decimalpart==0) {
						valorCadena = intPart.toString(); 
					}
					
					if(valorCadena.length()-1 == 0) {
						valorCadena = "0";
					}else {
						valorCadena = valorCadena.substring(0, valorCadena.length()-1);
						if(valorCadena.subSequence(valorCadena.length()-1, valorCadena.length()).equals(".")) {
							valorCadena = valorCadena.substring(0, valorCadena.length()-1);
						}
					}
					
					if(opcionSeleccionCalculadora.equals("VALOR")) {
						pagoSelected.setTotal(BigDecimal.valueOf(Double.parseDouble(valorCadena)));
					}else {
						pagoSelected.setCambio(BigDecimal.valueOf(Double.parseDouble(valorCadena)));
						calcularCambio();
					}
					
					
				}
				break;
			case "C":
				
				switch (opcionSeleccionCalculadora) {
				case "VALOR":
					pagoSelected.setTotal(BigDecimal.ZERO);
					if(pagoSelected.getValorentrega().doubleValue()>0) {
						calcularCambio();
					}
					break;
				case "CAMBIO":
					pagoSelected.setCambio(BigDecimal.ZERO);
					pagoSelected.setValorentrega(BigDecimal.ZERO);
					break;
				case "DOCUMENTO":
					pagoSelected.setNumerodocumento("");	
					break;
				default:
					break;
				}
				
				inicioCalculadora = true;
				break;
			default:
				
				break;
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	private void calcularCambio() {
		pagoSelected.setCambio(pagoSelected.getValorentrega().add(pagoSelected.getTotal().negate()).setScale(2, RoundingMode.HALF_UP));
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
	 * @return the opcionSeleccionCalculadora
	 */
	public String getOpcionSeleccionCalculadora() {
		return opcionSeleccionCalculadora;
	}

	/**
	 * @param opcionSeleccionCalculadora the opcionSeleccionCalculadora to set
	 */
	public void setOpcionSeleccionCalculadora(String opcionSeleccionCalculadora) {
		this.opcionSeleccionCalculadora = opcionSeleccionCalculadora;
	}

	/**
	 * @return the inicioCalculadora
	 */
	public boolean isInicioCalculadora() {
		return inicioCalculadora;
	}

	/**
	 * @param inicioCalculadora the inicioCalculadora to set
	 */
	public void setInicioCalculadora(boolean inicioCalculadora) {
		this.inicioCalculadora = inicioCalculadora;
	}

	/**
	 * @return the separadorDecimal
	 */
	public String getSeparadorDecimal() {
		return separadorDecimal;
	}

	/**
	 * @param separadorDecimal the separadorDecimal to set
	 */
	public void setSeparadorDecimal(String separadorDecimal) {
		this.separadorDecimal = separadorDecimal;
	}

}
