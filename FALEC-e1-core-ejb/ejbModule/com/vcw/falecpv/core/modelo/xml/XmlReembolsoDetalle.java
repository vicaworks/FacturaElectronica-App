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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "reembolsoDetalle")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlReembolsoDetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2682815970570096171L;
	
	@XmlElement
	private String tipoIdentificacionProveedorReembolso;
	@XmlElement
    private String identificacionProveedorReembolso;
	@XmlElement
    private String codPaisPagoProveedorReembolso;
	@XmlElement
    private String tipoProveedorReembolso;
	@XmlElement
    private String codDocReembolso;
	@XmlElement
    private String estabDocReembolso;
	@XmlElement
    private String ptoEmiDocReembolso;
	@XmlElement
    private String secuencialDocReembolso;
    @XmlElement
    @XmlJavaTypeAdapter(XmlAdapterSriDate.class)
    private Date fechaEmisionDocReembolso;
    @XmlElement
    private String numeroautorizacionDocReemb;
    
    @XmlElementRef(name = "detalleImpuestos")
    private List<XmlDetalleImpuesto> detalleImpuestoList;
	
    @XmlElementRef(name = "compensacionesReembolso")
    private List<XmlCompensacionReembolso> compensacionReembolsoList;
	/**
	 * 
	 */
	public XmlReembolsoDetalle() {
	}

	/**
	 * @return the tipoIdentificacionProveedorReembolso
	 */
	public String getTipoIdentificacionProveedorReembolso() {
		return tipoIdentificacionProveedorReembolso;
	}

	/**
	 * @param tipoIdentificacionProveedorReembolso the tipoIdentificacionProveedorReembolso to set
	 */
	public void setTipoIdentificacionProveedorReembolso(String tipoIdentificacionProveedorReembolso) {
		this.tipoIdentificacionProveedorReembolso = tipoIdentificacionProveedorReembolso;
	}

	/**
	 * @return the identificacionProveedorReembolso
	 */
	public String getIdentificacionProveedorReembolso() {
		return identificacionProveedorReembolso;
	}

	/**
	 * @param identificacionProveedorReembolso the identificacionProveedorReembolso to set
	 */
	public void setIdentificacionProveedorReembolso(String identificacionProveedorReembolso) {
		this.identificacionProveedorReembolso = identificacionProveedorReembolso;
	}

	/**
	 * @return the codPaisPagoProveedorReembolso
	 */
	public String getCodPaisPagoProveedorReembolso() {
		return codPaisPagoProveedorReembolso;
	}

	/**
	 * @param codPaisPagoProveedorReembolso the codPaisPagoProveedorReembolso to set
	 */
	public void setCodPaisPagoProveedorReembolso(String codPaisPagoProveedorReembolso) {
		this.codPaisPagoProveedorReembolso = codPaisPagoProveedorReembolso;
	}

	/**
	 * @return the tipoProveedorReembolso
	 */
	public String getTipoProveedorReembolso() {
		return tipoProveedorReembolso;
	}

	/**
	 * @param tipoProveedorReembolso the tipoProveedorReembolso to set
	 */
	public void setTipoProveedorReembolso(String tipoProveedorReembolso) {
		this.tipoProveedorReembolso = tipoProveedorReembolso;
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
	 * @return the estabDocReembolso
	 */
	public String getEstabDocReembolso() {
		return estabDocReembolso;
	}

	/**
	 * @param estabDocReembolso the estabDocReembolso to set
	 */
	public void setEstabDocReembolso(String estabDocReembolso) {
		this.estabDocReembolso = estabDocReembolso;
	}

	/**
	 * @return the ptoEmiDocReembolso
	 */
	public String getPtoEmiDocReembolso() {
		return ptoEmiDocReembolso;
	}

	/**
	 * @param ptoEmiDocReembolso the ptoEmiDocReembolso to set
	 */
	public void setPtoEmiDocReembolso(String ptoEmiDocReembolso) {
		this.ptoEmiDocReembolso = ptoEmiDocReembolso;
	}

	/**
	 * @return the secuencialDocReembolso
	 */
	public String getSecuencialDocReembolso() {
		return secuencialDocReembolso;
	}

	/**
	 * @param secuencialDocReembolso the secuencialDocReembolso to set
	 */
	public void setSecuencialDocReembolso(String secuencialDocReembolso) {
		this.secuencialDocReembolso = secuencialDocReembolso;
	}

	/**
	 * @return the fechaEmisionDocReembolso
	 */
	public Date getFechaEmisionDocReembolso() {
		return fechaEmisionDocReembolso;
	}

	/**
	 * @param fechaEmisionDocReembolso the fechaEmisionDocReembolso to set
	 */
	public void setFechaEmisionDocReembolso(Date fechaEmisionDocReembolso) {
		this.fechaEmisionDocReembolso = fechaEmisionDocReembolso;
	}

	/**
	 * @return the numeroautorizacionDocReemb
	 */
	public String getNumeroautorizacionDocReemb() {
		return numeroautorizacionDocReemb;
	}

	/**
	 * @param numeroautorizacionDocReemb the numeroautorizacionDocReemb to set
	 */
	public void setNumeroautorizacionDocReemb(String numeroautorizacionDocReemb) {
		this.numeroautorizacionDocReemb = numeroautorizacionDocReemb;
	}

	/**
	 * @return the detalleImpuestoList
	 */
	public List<XmlDetalleImpuesto> getDetalleImpuestoList() {
		return detalleImpuestoList;
	}

	/**
	 * @param detalleImpuestoList the detalleImpuestoList to set
	 */
	public void setDetalleImpuestoList(List<XmlDetalleImpuesto> detalleImpuestoList) {
		this.detalleImpuestoList = detalleImpuestoList;
	}

	/**
	 * @return the compensacionReembolsoList
	 */
	public List<XmlCompensacionReembolso> getCompensacionReembolsoList() {
		return compensacionReembolsoList;
	}

	/**
	 * @param compensacionReembolsoList the compensacionReembolsoList to set
	 */
	public void setCompensacionReembolsoList(List<XmlCompensacionReembolso> compensacionReembolsoList) {
		this.compensacionReembolsoList = compensacionReembolsoList;
	}

}
