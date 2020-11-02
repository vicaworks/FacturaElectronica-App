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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;
import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "infoNotaDebito")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoNotaDebito implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7635332171153256915L;

	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmision;
	@XmlElement
    private String dirEstablecimiento;
	@XmlElement
    private String tipoIdentificacionComprador;
	@XmlElement
    private String razonSocialComprador;
	@XmlElement
    private String identificacionComprador;
	@XmlElement
    private String contribuyenteEspecial;
	@XmlElement
    private String obligadoContabilidad;
	@XmlElement
    private String rise;
	@XmlElement
    private String codDocModificado;
	@XmlElement
    private String numDocModificado;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
    private Date fechaEmisionDocSustento;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalSinImpuestos;
	
	@XmlElementRef
	@XmlElementWrapper(name = "impuestos")
	private List<XmlImpuesto> impuestoList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "compensaciones")
	private List<XmlCompensacion> compensacionList;
	
	@XmlElement
	private Double valorTotal;
	
	@XmlElementRef
	@XmlElementWrapper(name = "pagos")
	private List<XmlPago> pagoList;
	
	@XmlTransient
	private String comprobanteModificado;
	
	/**
	 * 
	 */
	public XmlInfoNotaDebito() {
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
	 * @return the rise
	 */
	public String getRise() {
		return rise;
	}



	/**
	 * @param rise the rise to set
	 */
	public void setRise(String rise) {
		this.rise = rise;
	}



	/**
	 * @return the codDocModificado
	 */
	public String getCodDocModificado() {
		return codDocModificado;
	}



	/**
	 * @param codDocModificado the codDocModificado to set
	 */
	public void setCodDocModificado(String codDocModificado) {
		this.codDocModificado = codDocModificado;
	}



	/**
	 * @return the numDocModificado
	 */
	public String getNumDocModificado() {
		return numDocModificado;
	}



	/**
	 * @param numDocModificado the numDocModificado to set
	 */
	public void setNumDocModificado(String numDocModificado) {
		this.numDocModificado = numDocModificado;
	}



	/**
	 * @return the fechaEmisionDocSustento
	 */
	public Date getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}



	/**
	 * @param fechaEmisionDocSustento the fechaEmisionDocSustento to set
	 */
	public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
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
	 * @return the impuestoList
	 */
	public List<XmlImpuesto> getImpuestoList() {
		return impuestoList;
	}



	/**
	 * @param impuestoList the impuestoList to set
	 */
	public void setImpuestoList(List<XmlImpuesto> impuestoList) {
		this.impuestoList = impuestoList;
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
	 * @return the valorTotal
	 */
	public Double getValorTotal() {
		return valorTotal;
	}



	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
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
	 * @return the comprobanteModificado
	 */
	public String getComprobanteModificado() {
		return comprobanteModificado;
	}

	/**
	 * @param comprobanteModificado the comprobanteModificado to set
	 */
	public void setComprobanteModificado(String comprobanteModificado) {
		this.comprobanteModificado = comprobanteModificado;
	}

}
