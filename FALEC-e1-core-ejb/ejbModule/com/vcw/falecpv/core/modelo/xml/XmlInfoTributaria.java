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
@XmlRootElement(name = "infoTributaria")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoTributaria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5304231134127250603L;

	@XmlElement
	private String ambiente;
	@XmlElement
	private String tipoEmision;
	@XmlElement
	private String razonSocialrazonSocial;
	@XmlElement
	private String nombreComercialnombreComercial;
	@XmlElement
	private String ruc;
	@XmlElement
	private String claveAcceso;
	@XmlElement
	private String codDoc;
	@XmlElement
	private String estab;
	@XmlElement
	private String ptoEmi;
	@XmlElement
	private String secuencial;
	@XmlElement
	private String dirMatrizdirMatriz;
	
	/**
	 * 
	 */
	public XmlInfoTributaria() {
		
	}

	/**
	 * @return the ambiente
	 */
	public String getAmbiente() {
		return ambiente;
	}

	/**
	 * @param ambiente the ambiente to set
	 */
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * @return the tipoEmision
	 */
	public String getTipoEmision() {
		return tipoEmision;
	}

	/**
	 * @param tipoEmision the tipoEmision to set
	 */
	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	/**
	 * @return the razonSocialrazonSocial
	 */
	public String getRazonSocialrazonSocial() {
		return razonSocialrazonSocial;
	}

	/**
	 * @param razonSocialrazonSocial the razonSocialrazonSocial to set
	 */
	public void setRazonSocialrazonSocial(String razonSocialrazonSocial) {
		this.razonSocialrazonSocial = razonSocialrazonSocial;
	}

	/**
	 * @return the nombreComercialnombreComercial
	 */
	public String getNombreComercialnombreComercial() {
		return nombreComercialnombreComercial;
	}

	/**
	 * @param nombreComercialnombreComercial the nombreComercialnombreComercial to set
	 */
	public void setNombreComercialnombreComercial(String nombreComercialnombreComercial) {
		this.nombreComercialnombreComercial = nombreComercialnombreComercial;
	}

	/**
	 * @return the ruc
	 */
	public String getRuc() {
		return ruc;
	}

	/**
	 * @param ruc the ruc to set
	 */
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	/**
	 * @return the claveAcceso
	 */
	public String getClaveAcceso() {
		return claveAcceso;
	}

	/**
	 * @param claveAcceso the claveAcceso to set
	 */
	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

	/**
	 * @return the codDoc
	 */
	public String getCodDoc() {
		return codDoc;
	}

	/**
	 * @param codDoc the codDoc to set
	 */
	public void setCodDoc(String codDoc) {
		this.codDoc = codDoc;
	}

	/**
	 * @return the estab
	 */
	public String getEstab() {
		return estab;
	}

	/**
	 * @param estab the estab to set
	 */
	public void setEstab(String estab) {
		this.estab = estab;
	}

	/**
	 * @return the ptoEmi
	 */
	public String getPtoEmi() {
		return ptoEmi;
	}

	/**
	 * @param ptoEmi the ptoEmi to set
	 */
	public void setPtoEmi(String ptoEmi) {
		this.ptoEmi = ptoEmi;
	}

	/**
	 * @return the secuencial
	 */
	public String getSecuencial() {
		return secuencial;
	}

	/**
	 * @param secuencial the secuencial to set
	 */
	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

	/**
	 * @return the dirMatrizdirMatriz
	 */
	public String getDirMatrizdirMatriz() {
		return dirMatrizdirMatriz;
	}

	/**
	 * @param dirMatrizdirMatriz the dirMatrizdirMatriz to set
	 */
	public void setDirMatrizdirMatriz(String dirMatrizdirMatriz) {
		this.dirMatrizdirMatriz = dirMatrizdirMatriz;
	}

}
