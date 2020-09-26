/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "destino")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDestino implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7598064262096132322L;
	
	@XmlElement
	private String motivoTraslado;
	@XmlElement
    private String docAduaneroUnico;
	@XmlElement
    private String codEstabDestino;
	@XmlElement
    private String ruta;
	
	/**
	 * 
	 */
	public XmlDestino() {
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

}
