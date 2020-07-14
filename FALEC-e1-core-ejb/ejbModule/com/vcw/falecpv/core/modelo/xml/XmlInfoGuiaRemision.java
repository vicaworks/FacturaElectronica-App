/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "infoGuiaRemision")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoGuiaRemision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823453856527470067L;
	
	@XmlElement
	private String dirEstablecimiento;
	@XmlElement
	private String dirPartida;
	@XmlElement
	private String razonSocialTransportista;
	@XmlElement
	private String tipoIdentificacionTransportista;
	@XmlElement
	private String rucTransportista;
	@XmlElement
	private String rise;
	@XmlElement
	private String obligadoContabilidad;
	@XmlElement
	private String contribuyenteEspecial;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaIniTransporte;
	@XmlElement
	private Date fechaFinTransporte;
	@XmlElement
	private String placa;
	
	/**
	 * 
	 */
	public XmlInfoGuiaRemision() {
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
	 * @return the dirPartida
	 */
	public String getDirPartida() {
		return dirPartida;
	}

	/**
	 * @param dirPartida the dirPartida to set
	 */
	public void setDirPartida(String dirPartida) {
		this.dirPartida = dirPartida;
	}

	/**
	 * @return the razonSocialTransportista
	 */
	public String getRazonSocialTransportista() {
		return razonSocialTransportista;
	}

	/**
	 * @param razonSocialTransportista the razonSocialTransportista to set
	 */
	public void setRazonSocialTransportista(String razonSocialTransportista) {
		this.razonSocialTransportista = razonSocialTransportista;
	}

	/**
	 * @return the tipoIdentificacionTransportista
	 */
	public String getTipoIdentificacionTransportista() {
		return tipoIdentificacionTransportista;
	}

	/**
	 * @param tipoIdentificacionTransportista the tipoIdentificacionTransportista to set
	 */
	public void setTipoIdentificacionTransportista(String tipoIdentificacionTransportista) {
		this.tipoIdentificacionTransportista = tipoIdentificacionTransportista;
	}

	/**
	 * @return the rucTransportista
	 */
	public String getRucTransportista() {
		return rucTransportista;
	}

	/**
	 * @param rucTransportista the rucTransportista to set
	 */
	public void setRucTransportista(String rucTransportista) {
		this.rucTransportista = rucTransportista;
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
	 * @return the fechaIniTransporte
	 */
	public Date getFechaIniTransporte() {
		return fechaIniTransporte;
	}

	/**
	 * @param fechaIniTransporte the fechaIniTransporte to set
	 */
	public void setFechaIniTransporte(Date fechaIniTransporte) {
		this.fechaIniTransporte = fechaIniTransporte;
	}

	/**
	 * @return the fechaFinTransporte
	 */
	public Date getFechaFinTransporte() {
		return fechaFinTransporte;
	}

	/**
	 * @param fechaFinTransporte the fechaFinTransporte to set
	 */
	public void setFechaFinTransporte(Date fechaFinTransporte) {
		this.fechaFinTransporte = fechaFinTransporte;
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

}
