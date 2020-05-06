/**
 * 
 */
package com.vcw.falecpv.web.ctrl.facturacion;

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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;


/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class FactMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3096783449837611107L;

	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	private Cabecera cabeceraFac;
	private List<Detalle> detalleFacList;
	private Detalle detalleSelected;
	private List<Producto> productoList;
	private Producto productoSelected;
	private String precioOpcionSeleccion="PRECIO1";
	private String opcionCantidadPrecio="CANTIDAD";
	private boolean inicioCalculadora = false;
	private String separadorDecimal;
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	public FactMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			consultarProductos();
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
			productoSelected = null;
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		
		try {
			
//			if(criterioBusqueda==null || criterioBusqueda.trim().length()==0) {
//				AppJsfUtil.addErrorMessage("formMain:intCriterioBusqueda", "","REQUERIDO");
//				return;
//				
//			}
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()==0) {
				criterioBusqueda = null;
				
			}
			
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
	
	
	
	public void agregarProducto() {
		try {
			
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			
			detalleSelected = existeProductoLista();
			boolean existe = false;
			if(detalleSelected!=null) {
				detalleSelected.setCantidad(detalleSelected.getCantidad().add(BigDecimal.valueOf(1)));
				existe = true;
			}else {
				detalleSelected = new Detalle();
				detalleSelected.setCantidad(BigDecimal.valueOf(1));
				detalleSelected.setDescripcion(productoSelected.getNombregenerico());
				detalleSelected.setPreciounitario(productoSelected.getPreciounitario());
				precioOpcionSeleccion = "PRECIO1";
				detalleSelected.setPrecioOpcionSeleccion(precioOpcionSeleccion);
			}
			
			calcularItem(detalleSelected);
			
			if(!existe) {
				detalleFacList.add(detalleSelected);
			}
			
			totalizar();
			
			opcionCantidadPrecio = "CANTIDAD";
			inicioCalculadora = true;
			separadorDecimal = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void calcularItem(Detalle dFac) {
		
		dFac.setDescuento(productoSelected.getPorcentajedescuento().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciounitario()).multiply(dFac.getCantidad())
				.setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototalsinimpuesto(dFac.getCantidad().multiply(dFac.getPreciounitario()).add(dFac.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		dFac.setProducto(productoSelected);
		dFac.setValoriva(productoSelected.getIva().getValor().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciototalsinimpuesto().add(dFac.getDescuento().negate())).setScale(2, RoundingMode.HALF_UP));
		dFac.setValorice(productoSelected.getIce().getValor().divide(BigDecimal.valueOf(100)).multiply(dFac.getPreciototalsinimpuesto()).setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototal(dFac.getPreciototalsinimpuesto().add(dFac.getValoriva()).setScale(2, RoundingMode.HALF_UP));
		
		
	}
	
	private void totalizar() throws DaoException {
		if(cabeceraFac==null) nuevaFactura();
		
		
		cabeceraFac.setTotaliva(BigDecimal.ZERO);
		cabeceraFac.setTotalice(BigDecimal.ZERO);
		cabeceraFac.setTotaldescuento(BigDecimal.ZERO);
		cabeceraFac.setTotalsinimpuestos(BigDecimal.ZERO);
		cabeceraFac.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : detalleFacList) {
			System.out.println("=================================");
			System.out.println(d.toString());
			
			cabeceraFac.setTotalsinimpuestos(cabeceraFac.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			cabeceraFac.setTotaliva(cabeceraFac.getTotaliva().add(d.getValoriva()));
			cabeceraFac.setTotalice(cabeceraFac.getTotalice().add(d.getValorice()));
			cabeceraFac.setTotaldescuento(cabeceraFac.getTotaldescuento().add(d.getDescuento()));
			System.out.println("=================================");
			System.out.println(cabeceraFac.toString());
			
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		cabeceraFac.setTotaliva(cabeceraFac.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		cabeceraFac.setTotalice(cabeceraFac.getTotalice().setScale(2, RoundingMode.HALF_UP));
		cabeceraFac.setTotaldescuento(cabeceraFac.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		cabeceraFac.setTotalsinimpuestos(cabeceraFac.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		cabeceraFac.setTotalconimpuestos(cabeceraFac.getTotalsinimpuestos().add(cabeceraFac.getTotaliva()).add(cabeceraFac.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
		System.out.println("=================================");
		System.out.println(cabeceraFac.toString());
		
	}
	
	private Detalle existeProductoLista() {
		for (Detalle d : detalleFacList) {
			if(d.getProducto().getIdproducto().equals(productoSelected.getIdproducto())) {
				return d;
			}
		}
		
		return null;
	}
	
	public void nuevaFactura() throws DaoException {
		cabeceraFac = new Cabecera();
		cabeceraFac.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		cabeceraFac.setFechaemision(new Date());
		cabeceraFac.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		SimpleDateFormat sf = new SimpleDateFormat("MM-yyyy");
		cabeceraFac.setPeriodofiscal(sf.format(new Date()));
		cabeceraFac.setTipocomprobante(tipocomprobanteServicio.consultarByPk("1"));
		precioOpcionSeleccion = "PRECIO1";
		opcionCantidadPrecio = "CANTIDAD";
		inicioCalculadora = false;
		separadorDecimal = null;
	}

	public void cambiarPrecio(String precio) {
		try {
			
			if(detalleSelected==null || detalleFacList==null || detalleFacList.isEmpty()) {
				return;
			}
			
			precioOpcionSeleccion = precio;
			
			switch (precioOpcionSeleccion) {
			case "PRECIO1":
				detalleSelected.setPrecioOpcionSeleccion("PRECIO1");
				detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciounitario());
				break;
			case "PRECIO2":
				detalleSelected.setPrecioOpcionSeleccion("PRECIO2");
				detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciodos());
				break;	
			case "PRECIO3":
				detalleSelected.setPrecioOpcionSeleccion("PRECIO3");
				detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciotres());
				break;
			default:
				break;
			}
			
			calcularItem(detalleSelected);
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void seleccionOpcionCalculadora(String opcion) {
		try {
			
			if(detalleSelected==null || detalleFacList==null || detalleFacList.isEmpty()) {
				return;
			}

			switch (opcion) {
			case "+":
				
				if(opcionCantidadPrecio.equals("CANTIDAD")) {
					detalleSelected.setCantidad(detalleSelected.getCantidad().add(BigDecimal.valueOf(1)));
				}else {
					detalleSelected.setPreciounitario(detalleSelected.getPreciounitario().add(BigDecimal.valueOf(1)));
				}
				
				break;
			case "-":
				if(opcionCantidadPrecio.equals("CANTIDAD")) {
					detalleSelected.setCantidad(detalleSelected.getCantidad().add(BigDecimal.valueOf(1).negate()));
					if(detalleSelected.getCantidad().doubleValue()<0) {
						detalleSelected.setCantidad(BigDecimal.ZERO);
					}
				}else {
					detalleSelected.setPreciounitario(detalleSelected.getPreciounitario().add(BigDecimal.valueOf(1).negate()));
					if(detalleSelected.getPreciounitario().doubleValue()<0) {
						detalleSelected.setPreciounitario(BigDecimal.ZERO);
					}
				}
				break;
			case "back":
				
				Integer intPart = detalleSelected.getCantidad().intValue();
				Integer decimalpart = decimalPart(detalleSelected.getCantidad());
				BigDecimal valor = detalleSelected.getCantidad();
				
				if(opcionCantidadPrecio.equals("PRECIO")) {
					intPart = detalleSelected.getPreciounitario().intValue();
					decimalpart = decimalPart(detalleSelected.getPreciounitario());
					valor = detalleSelected.getPreciounitario();
							
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
				
				if(opcionCantidadPrecio.equals("CANTIDAD")) {
					detalleSelected.setCantidad(BigDecimal.valueOf(Double.parseDouble(valorCadena)));
				}else {
					detalleSelected.setPreciounitario(BigDecimal.valueOf(Double.parseDouble(valorCadena)));
				}
				break;	
			case "C":
				if(opcionCantidadPrecio.equals("CANTIDAD")) {
					detalleSelected.setCantidad(BigDecimal.ZERO);
				}else {
					detalleSelected.setPreciounitario(BigDecimal.ZERO);
				}
				inicioCalculadora = true;
				break;	
			default:
				break;
			}
			separadorDecimal = null;
			calcularItem(detalleSelected);
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private Integer decimalPart(BigDecimal valor) {
		if(!valor.toString().contains(".")) {
			return 0;
		}
		String val = valor.toString().substring(valor.toString().lastIndexOf(".")+1, valor.toString().length());
		return Integer.parseInt(val);
	}
	
	public void ejecutarCalculadora(String valorCalculadora) {
		try {
			
			if(detalleSelected==null || detalleFacList==null || detalleFacList.isEmpty()) {
				return;
			}
			
			if(valorCalculadora.equals(".")) {
				separadorDecimal = ".";
				return;
			}
			
			
			
			switch (opcionCantidadPrecio) {
			case "CANTIDAD":
				
				if(inicioCalculadora) {
					detalleSelected.setCantidad(BigDecimal.valueOf(Double.parseDouble(valorCalculadora)));
					inicioCalculadora = false;
					break;
				}
				
				detalleSelected.setCantidad(procesarCal(detalleSelected.getCantidad(),valorCalculadora));
				
				break;
			case "PRECIO":
				
				if(inicioCalculadora) {
					detalleSelected.setPreciounitario(BigDecimal.valueOf(Double.parseDouble(valorCalculadora)));
					inicioCalculadora = false;
					break;
				}
				
				detalleSelected.setPreciounitario(procesarCal(detalleSelected.getPreciounitario(), valorCalculadora));
				
				break;	
			default:
				break;
			}
			
			calcularItem(detalleSelected);
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
	
	public void seleccionarDetalle() {
		try {
			
			precioOpcionSeleccion = detalleSelected.getPrecioOpcionSeleccion();
			opcionCantidadPrecio = "CANTIDAD";
			inicioCalculadora = true;
			separadorDecimal = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @return the productoServicio
	 */
	public ProductoServicio getProductoServicio() {
		return productoServicio;
	}

	/**
	 * @param productoServicio the productoServicio to set
	 */
	public void setProductoServicio(ProductoServicio productoServicio) {
		this.productoServicio = productoServicio;
	}

	/**
	 * @return the cabeceraServicio
	 */
	public CabeceraServicio getCabeceraServicio() {
		return cabeceraServicio;
	}

	/**
	 * @param cabeceraServicio the cabeceraServicio to set
	 */
	public void setCabeceraServicio(CabeceraServicio cabeceraServicio) {
		this.cabeceraServicio = cabeceraServicio;
	}

	/**
	 * @return the cabeceraFac
	 */
	public Cabecera getCabeceraFac() {
		return cabeceraFac;
	}

	/**
	 * @param cabeceraFac the cabeceraFac to set
	 */
	public void setCabeceraFac(Cabecera cabeceraFac) {
		this.cabeceraFac = cabeceraFac;
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
	 * @return the precioOpcionSeleccion
	 */
	public String getPrecioOpcionSeleccion() {
		return precioOpcionSeleccion;
	}

	/**
	 * @param precioOpcionSeleccion the precioOpcionSeleccion to set
	 */
	public void setPrecioOpcionSeleccion(String precioOpcionSeleccion) {
		this.precioOpcionSeleccion = precioOpcionSeleccion;
	}

	/**
	 * @return the opcionCantidadPrecio
	 */
	public String getOpcionCantidadPrecio() {
		return opcionCantidadPrecio;
	}

	/**
	 * @param opcionCantidadPrecio the opcionCantidadPrecio to set
	 */
	public void setOpcionCantidadPrecio(String opcionCantidadPrecio) {
		this.opcionCantidadPrecio = opcionCantidadPrecio;
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
