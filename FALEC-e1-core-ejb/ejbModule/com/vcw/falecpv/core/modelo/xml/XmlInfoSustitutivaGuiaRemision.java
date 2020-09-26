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

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "infoSustitutivaGuiaRemision")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoSustitutivaGuiaRemision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1464993284088729886L;

	@XmlElement
	private String dirPartida;
	@XmlElement
    private String dirDestinatario;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
    private Date fechaIniTransporte;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
    private Date fechaFinTransporte;
	@XmlElement
    private String razonSocialTransportista;
	@XmlElement
    private String tipoIdentificacionTransportista;
	@XmlElement
    private String rucTransportista;
	@XmlElement
    private String placa;
	@XmlElementRef
	@XmlElementWrapper(name = "destinos")
	private List<XmlDestino> destinoList;
	
	/**
	 * 
	 */
	public XmlInfoSustitutivaGuiaRemision() {
		
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
	 * @return the dirDestinatario
	 */
	public String getDirDestinatario() {
		return dirDestinatario;
	}

	/**
	 * @param dirDestinatario the dirDestinatario to set
	 */
	public void setDirDestinatario(String dirDestinatario) {
		this.dirDestinatario = dirDestinatario;
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
	 * @return the destinoList
	 */
	public List<XmlDestino> getDestinoList() {
		return destinoList;
	}

	/**
	 * @param destinoList the destinoList to set
	 */
	public void setDestinoList(List<XmlDestino> destinoList) {
		this.destinoList = destinoList;
	}

}
