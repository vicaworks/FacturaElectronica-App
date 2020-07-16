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
@XmlRootElement(name = "destinatario")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDestinatario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6869094834415024820L;

	@XmlElement
	private String identificacionDestinatario;
	@XmlElement
	private String razonSocialDestinatario;
	@XmlElement
	private String dirDestinatario;
	@XmlElement
	private String motivoTraslado;
	@XmlElement
	private String docAduaneroUnico;
	@XmlElement
	private String codEstabDestino;
	@XmlElement
	private String ruta;
	@XmlElement
	private String codDocSustento;
	@XmlElement
	private String numDocSustento;
	@XmlElement
	private String numAutDocSustento;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmisionDocSustento;
	
	@XmlElementRef
	@XmlElementWrapper(name = "detalles")
	private List<XmlDestinatarioDetalle> destinatarioDetallesList;
	
	/**
	 * 
	 */
	public XmlDestinatario() {
	}

	/**
	 * @return the identificacionDestinatario
	 */
	public String getIdentificacionDestinatario() {
		return identificacionDestinatario;
	}

	/**
	 * @param identificacionDestinatario the identificacionDestinatario to set
	 */
	public void setIdentificacionDestinatario(String identificacionDestinatario) {
		this.identificacionDestinatario = identificacionDestinatario;
	}

	/**
	 * @return the razonSocialDestinatario
	 */
	public String getRazonSocialDestinatario() {
		return razonSocialDestinatario;
	}

	/**
	 * @param razonSocialDestinatario the razonSocialDestinatario to set
	 */
	public void setRazonSocialDestinatario(String razonSocialDestinatario) {
		this.razonSocialDestinatario = razonSocialDestinatario;
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
	 * @return the motivoTraslado
	 */
	public String getMotivoTraslado() {
		return motivoTraslado;
	}

	/**
	 * @param motivoTraslado the motivoTraslado to set
	 */
	public void setMotivoTraslado(String motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}

	/**
	 * @return the docAduaneroUnico
	 */
	public String getDocAduaneroUnico() {
		return docAduaneroUnico;
	}

	/**
	 * @param docAduaneroUnico the docAduaneroUnico to set
	 */
	public void setDocAduaneroUnico(String docAduaneroUnico) {
		this.docAduaneroUnico = docAduaneroUnico;
	}

	/**
	 * @return the codEstabDestino
	 */
	public String getCodEstabDestino() {
		return codEstabDestino;
	}

	/**
	 * @param codEstabDestino the codEstabDestino to set
	 */
	public void setCodEstabDestino(String codEstabDestino) {
		this.codEstabDestino = codEstabDestino;
	}

	/**
	 * @return the ruta
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 * @return the codDocSustento
	 */
	public String getCodDocSustento() {
		return codDocSustento;
	}

	/**
	 * @param codDocSustento the codDocSustento to set
	 */
	public void setCodDocSustento(String codDocSustento) {
		this.codDocSustento = codDocSustento;
	}

	/**
	 * @return the numDocSustento
	 */
	public String getNumDocSustento() {
		return numDocSustento;
	}

	/**
	 * @param numDocSustento the numDocSustento to set
	 */
	public void setNumDocSustento(String numDocSustento) {
		this.numDocSustento = numDocSustento;
	}

	/**
	 * @return the numAutDocSustento
	 */
	public String getNumAutDocSustento() {
		return numAutDocSustento;
	}

	/**
	 * @param numAutDocSustento the numAutDocSustento to set
	 */
	public void setNumAutDocSustento(String numAutDocSustento) {
		this.numAutDocSustento = numAutDocSustento;
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
	 * @return the destinatarioDetallesList
	 */
	public List<XmlDestinatarioDetalle> getDestinatarioDetallesList() {
		return destinatarioDetallesList;
	}

	/**
	 * @param destinatarioDetallesList the destinatarioDetallesList to set
	 */
	public void setDestinatarioDetallesList(List<XmlDestinatarioDetalle> destinatarioDetallesList) {
		this.destinatarioDetallesList = destinatarioDetallesList;
	}

}
