/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;
import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "infoFactura")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoFactura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5847867518866765667L;
	
	// dd/MM/yyyy
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmision;
	@XmlElement
    private String dirEstablecimiento;
	@XmlElement
    private String contribuyenteEspecial;
    // SI NO
	@XmlElement
    private String obligadoContabilidad;
	@XmlElement
    private String comercioExterior;
	@XmlElement
    private String incoTermFactura;
	@XmlElement
    private String lugarIncoTerm;
	@XmlElement
    private String paisOrigen;
	@XmlElement
    private String puertoEmbarque;
	@XmlElement
    private String puertoDestino;
	@XmlElement
    private String paisDestino;
	@XmlElement
    private String paisAdquisicion;
	@XmlElement
    private String tipoIdentificacionComprador;
	@XmlElement
    private String guiaRemision;
	@XmlElement
    private String razonSocialComprador;
	@XmlElement
    private String identificacionComprador;
	@XmlElement
    private String direccionComprador;
    // #.00
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalSinImpuestos = 0d;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalSubsidio = 0d;
    
    // FOB
	@XmlElement
    private String incoTermTotalSinImpuestos;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalDescuento = 0d;
	@XmlElement
    private String codDocReembolso;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalComprobantesReembolso;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalBaseImponibleReembolso;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalImpuestoReembolso;
	
	@XmlElementRef
	@XmlElementWrapper(name = "totalConImpuestos")
	private List<XmlTotalImpuesto> totalImpuestoList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "compensaciones")
	private List<XmlCompensacion> compensacionList;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double propina = 0.00d;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double fleteInternacional;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double seguroInternacional;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double gastosAduaneros;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double gastosTransporteOtros;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double importeTotal;
    @XmlElement
    private String moneda;
    @XmlElement
    private String placa;
    
    @XmlElementRef
    @XmlElementWrapper(name = "pagos")
    private List<XmlPago> pagoList;
    
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valorRetIva;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valorRetRenta;

	/**
	 * 
	 */
	public XmlInfoFactura() {
	}

	/**
	 * @return the fechaEmision
	 */
	public Date getFechaEmision() {
		return fechaEmision;
	}

	/**
	 * @param fechaEmision the fechaEmision to set
	 */
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	/**
	 * @return the dirEstablecimiento
	 */
	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}

	/**
	 * @param dirEstablecimiento the dirEstablecimiento to set
	 */
	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}

	/**
	 * @return the contribuyenteEspecial
	 */
	public String getContribuyenteEspecial() {
		return contribuyenteEspecial;
	}

	/**
	 * @param contribuyenteEspecial the contribuyenteEspecial to set
	 */
	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
	}

	/**
	 * @return the obligadoContabilidad
	 */
	public String getObligadoContabilidad() {
		return obligadoContabilidad;
	}

	/**
	 * @param obligadoContabilidad the obligadoContabilidad to set
	 */
	public void setObligadoContabilidad(String obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}

	/**
	 * @return the comercioExterior
	 */
	public String getComercioExterior() {
		return comercioExterior;
	}

	/**
	 * @param comercioExterior the comercioExterior to set
	 */
	public void setComercioExterior(String comercioExterior) {
		this.comercioExterior = comercioExterior;
	}

	/**
	 * @return the incoTermFactura
	 */
	public String getIncoTermFactura() {
		return incoTermFactura;
	}

	/**
	 * @param incoTermFactura the incoTermFactura to set
	 */
	public void setIncoTermFactura(String incoTermFactura) {
		this.incoTermFactura = incoTermFactura;
	}

	/**
	 * @return the lugarIncoTerm
	 */
	public String getLugarIncoTerm() {
		return lugarIncoTerm;
	}

	/**
	 * @param lugarIncoTerm the lugarIncoTerm to set
	 */
	public void setLugarIncoTerm(String lugarIncoTerm) {
		this.lugarIncoTerm = lugarIncoTerm;
	}

	/**
	 * @return the paisOrigen
	 */
	public String getPaisOrigen() {
		return paisOrigen;
	}

	/**
	 * @param paisOrigen the paisOrigen to set
	 */
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	/**
	 * @return the puertoEmbarque
	 */
	public String getPuertoEmbarque() {
		return puertoEmbarque;
	}

	/**
	 * @param puertoEmbarque the puertoEmbarque to set
	 */
	public void setPuertoEmbarque(String puertoEmbarque) {
		this.puertoEmbarque = puertoEmbarque;
	}

	/**
	 * @return the puertoDestino
	 */
	public String getPuertoDestino() {
		return puertoDestino;
	}

	/**
	 * @param puertoDestino the puertoDestino to set
	 */
	public void setPuertoDestino(String puertoDestino) {
		this.puertoDestino = puertoDestino;
	}

	/**
	 * @return the paisDestino
	 */
	public String getPaisDestino() {
		return paisDestino;
	}

	/**
	 * @param paisDestino the paisDestino to set
	 */
	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}

	/**
	 * @return the paisAdquisicion
	 */
	public String getPaisAdquisicion() {
		return paisAdquisicion;
	}

	/**
	 * @param paisAdquisicion the paisAdquisicion to set
	 */
	public void setPaisAdquisicion(String paisAdquisicion) {
		this.paisAdquisicion = paisAdquisicion;
	}

	/**
	 * @return the tipoIdentificacionComprador
	 */
	public String getTipoIdentificacionComprador() {
		return tipoIdentificacionComprador;
	}

	/**
	 * @param tipoIdentificacionComprador the tipoIdentificacionComprador to set
	 */
	public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
		this.tipoIdentificacionComprador = tipoIdentificacionComprador;
	}

	/**
	 * @return the guiaRemision
	 */
	public String getGuiaRemision() {
		return guiaRemision;
	}

	/**
	 * @param guiaRemision the guiaRemision to set
	 */
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	/**
	 * @return the razonSocialComprador
	 */
	public String getRazonSocialComprador() {
		return razonSocialComprador;
	}

	/**
	 * @param razonSocialComprador the razonSocialComprador to set
	 */
	public void setRazonSocialComprador(String razonSocialComprador) {
		this.razonSocialComprador = razonSocialComprador;
	}

	/**
	 * @return the identificacionComprador
	 */
	public String getIdentificacionComprador() {
		return identificacionComprador;
	}

	/**
	 * @param identificacionComprador the identificacionComprador to set
	 */
	public void setIdentificacionComprador(String identificacionComprador) {
		this.identificacionComprador = identificacionComprador;
	}

	/**
	 * @return the direccionComprador
	 */
	public String getDireccionComprador() {
		return direccionComprador;
	}

	/**
	 * @param direccionComprador the direccionComprador to set
	 */
	public void setDireccionComprador(String direccionComprador) {
		this.direccionComprador = direccionComprador;
	}

	/**
	 * @return the totalSinImpuestos
	 */
	public Double getTotalSinImpuestos() {
		return totalSinImpuestos;
	}

	/**
	 * @param totalSinImpuestos the totalSinImpuestos to set
	 */
	public void setTotalSinImpuestos(Double totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}

	/**
	 * @return the totalSubsidio
	 */
	public Double getTotalSubsidio() {
		return totalSubsidio;
	}

	/**
	 * @param totalSubsidio the totalSubsidio to set
	 */
	public void setTotalSubsidio(Double totalSubsidio) {
		this.totalSubsidio = totalSubsidio;
	}

	/**
	 * @return the incoTermTotalSinImpuestos
	 */
	public String getIncoTermTotalSinImpuestos() {
		return incoTermTotalSinImpuestos;
	}

	/**
	 * @param incoTermTotalSinImpuestos the incoTermTotalSinImpuestos to set
	 */
	public void setIncoTermTotalSinImpuestos(String incoTermTotalSinImpuestos) {
		this.incoTermTotalSinImpuestos = incoTermTotalSinImpuestos;
	}

	/**
	 * @return the totalDescuento
	 */
	public Double getTotalDescuento() {
		return totalDescuento;
	}

	/**
	 * @param totalDescuento the totalDescuento to set
	 */
	public void setTotalDescuento(Double totalDescuento) {
		this.totalDescuento = totalDescuento;
	}

	/**
	 * @return the codDocReembolso
	 */
	public String getCodDocReembolso() {
		return codDocReembolso;
	}

	/**
	 * @param codDocReembolso the codDocReembolso to set
	 */
	public void setCodDocReembolso(String codDocReembolso) {
		this.codDocReembolso = codDocReembolso;
	}

	/**
	 * @return the totalComprobantesReembolso
	 */
	public Double getTotalComprobantesReembolso() {
		return totalComprobantesReembolso;
	}

	/**
	 * @param totalComprobantesReembolso the totalComprobantesReembolso to set
	 */
	public void setTotalComprobantesReembolso(Double totalComprobantesReembolso) {
		this.totalComprobantesReembolso = totalComprobantesReembolso;
	}

	/**
	 * @return the totalBaseImponibleReembolso
	 */
	public Double getTotalBaseImponibleReembolso() {
		return totalBaseImponibleReembolso;
	}

	/**
	 * @param totalBaseImponibleReembolso the totalBaseImponibleReembolso to set
	 */
	public void setTotalBaseImponibleReembolso(Double totalBaseImponibleReembolso) {
		this.totalBaseImponibleReembolso = totalBaseImponibleReembolso;
	}

	/**
	 * @return the totalImpuestoReembolso
	 */
	public Double getTotalImpuestoReembolso() {
		return totalImpuestoReembolso;
	}

	/**
	 * @param totalImpuestoReembolso the totalImpuestoReembolso to set
	 */
	public void setTotalImpuestoReembolso(Double totalImpuestoReembolso) {
		this.totalImpuestoReembolso = totalImpuestoReembolso;
	}

	/**
	 * @return the totalImpuestoList
	 */
	public List<XmlTotalImpuesto> getTotalImpuestoList() {
		return totalImpuestoList;
	}

	/**
	 * @param totalImpuestoList the totalImpuestoList to set
	 */
	public void setTotalImpuestoList(List<XmlTotalImpuesto> totalImpuestoList) {
		this.totalImpuestoList = totalImpuestoList;
	}

	/**
	 * @return the compensacionList
	 */
	public List<XmlCompensacion> getCompensacionList() {
		return compensacionList;
	}

	/**
	 * @param compensacionList the compensacionList to set
	 */
	public void setCompensacionList(List<XmlCompensacion> compensacionList) {
		this.compensacionList = compensacionList;
	}

	/**
	 * @return the propina
	 */
	public Double getPropina() {
		return propina;
	}

	/**
	 * @param propina the propina to set
	 */
	public void setPropina(Double propina) {
		this.propina = propina;
	}

	/**
	 * @return the fleteInternacional
	 */
	public Double getFleteInternacional() {
		return fleteInternacional;
	}

	/**
	 * @param fleteInternacional the fleteInternacional to set
	 */
	public void setFleteInternacional(Double fleteInternacional) {
		this.fleteInternacional = fleteInternacional;
	}

	/**
	 * @return the seguroInternacional
	 */
	public Double getSeguroInternacional() {
		return seguroInternacional;
	}

	/**
	 * @param seguroInternacional the seguroInternacional to set
	 */
	public void setSeguroInternacional(Double seguroInternacional) {
		this.seguroInternacional = seguroInternacional;
	}

	/**
	 * @return the gastosAduaneros
	 */
	public Double getGastosAduaneros() {
		return gastosAduaneros;
	}

	/**
	 * @param gastosAduaneros the gastosAduaneros to set
	 */
	public void setGastosAduaneros(Double gastosAduaneros) {
		this.gastosAduaneros = gastosAduaneros;
	}

	/**
	 * @return the gastosTransporteOtros
	 */
	public Double getGastosTransporteOtros() {
		return gastosTransporteOtros;
	}

	/**
	 * @param gastosTransporteOtros the gastosTransporteOtros to set
	 */
	public void setGastosTransporteOtros(Double gastosTransporteOtros) {
		this.gastosTransporteOtros = gastosTransporteOtros;
	}

	/**
	 * @return the importeTotal
	 */
	public Double getImporteTotal() {
		return importeTotal;
	}

	/**
	 * @param importeTotal the importeTotal to set
	 */
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}

	/**
	 * @param moneda the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the placa
	 */
	public String getPlaca() {
		return placa;
	}

	/**
	 * @param placa the placa to set
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	/**
	 * @return the pagoList
	 */
	public List<XmlPago> getPagoList() {
		return pagoList;
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<XmlPago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the valorRetIva
	 */
	public Double getValorRetIva() {
		return valorRetIva;
	}

	/**
	 * @param valorRetIva the valorRetIva to set
	 */
	public void setValorRetIva(Double valorRetIva) {
		this.valorRetIva = valorRetIva;
	}

	/**
	 * @return the valorRetRenta
	 */
	public Double getValorRetRenta() {
		return valorRetRenta;
	}

	/**
	 * @param valorRetRenta the valorRetRenta to set
	 */
	public void setValorRetRenta(Double valorRetRenta) {
		this.valorRetRenta = valorRetRenta;
	}

}
