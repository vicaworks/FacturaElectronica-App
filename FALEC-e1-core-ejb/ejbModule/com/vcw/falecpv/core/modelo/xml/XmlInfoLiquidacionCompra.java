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
@XmlRootElement(name = "infoLiquidacionCompra")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoLiquidacionCompra implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2633840624700345912L;

	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmision;
	@XmlElement
    private String dirEstablecimiento;
	@XmlElement
    private String contribuyenteEspecial;
	@XmlElement
    private String obligadoContabilidad;
	@XmlElement
    private String tipoIdentificacionProveedor;
	@XmlElement
    private String razonSocialProveedor;
	@XmlElement
    private String identificacionProveedor;
	@XmlElement
	private String direccionProveedor;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalSinImpuestos;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double totalDescuento;
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
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double importeTotal;
	
	@XmlElement
    private String moneda;
	
	@XmlElementRef
	@XmlElementWrapper(name = "pagos")
	private List<XmlPago> pagoList;
	
	/**
	 * 
	 */
	public XmlInfoLiquidacionCompra() {
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
	 * @return the tipoIdentificacionProveedor
	 */
	public String getTipoIdentificacionProveedor() {
		return tipoIdentificacionProveedor;
	}

	/**
	 * @param tipoIdentificacionProveedor the tipoIdentificacionProveedor to set
	 */
	public void setTipoIdentificacionProveedor(String tipoIdentificacionProveedor) {
		this.tipoIdentificacionProveedor = tipoIdentificacionProveedor;
	}

	/**
	 * @return the razonSocialProveedor
	 */
	public String getRazonSocialProveedor() {
		return razonSocialProveedor;
	}

	/**
	 * @param razonSocialProveedor the razonSocialProveedor to set
	 */
	public void setRazonSocialProveedor(String razonSocialProveedor) {
		this.razonSocialProveedor = razonSocialProveedor;
	}

	/**
	 * @return the identificacionProveedor
	 */
	public String getIdentificacionProveedor() {
		return identificacionProveedor;
	}

	/**
	 * @param identificacionProveedor the identificacionProveedor to set
	 */
	public void setIdentificacionProveedor(String identificacionProveedor) {
		this.identificacionProveedor = identificacionProveedor;
	}

	/**
	 * @return the direccionProveedor
	 */
	public String getDireccionProveedor() {
		return direccionProveedor;
	}

	/**
	 * @param direccionProveedor the direccionProveedor to set
	 */
	public void setDireccionProveedor(String direccionProveedor) {
		this.direccionProveedor = direccionProveedor;
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

}
