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
@XmlRootElement(name = "infoCompRetencion")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInfoCompRetencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8993949189911136440L;
	
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
	private String tipoIdentificacionSujetoRetenido;
	@XmlElement
	private String razonSocialSujetoRetenido;
	@XmlElement
	private String identificacionSujetoRetenido;
	@XmlElement
	private String periodoFiscal;

	/**
	 * 
	 */
	public XmlInfoCompRetencion() {
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
	 * @return the tipoIdentificacionSujetoRetenido
	 */
	public String getTipoIdentificacionSujetoRetenido() {
		return tipoIdentificacionSujetoRetenido;
	}

	/**
	 * @param tipoIdentificacionSujetoRetenido the tipoIdentificacionSujetoRetenido to set
	 */
	public void setTipoIdentificacionSujetoRetenido(String tipoIdentificacionSujetoRetenido) {
		this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido;
	}

	/**
	 * @return the razonSocialSujetoRetenido
	 */
	public String getRazonSocialSujetoRetenido() {
		return razonSocialSujetoRetenido;
	}

	/**
	 * @param razonSocialSujetoRetenido the razonSocialSujetoRetenido to set
	 */
	public void setRazonSocialSujetoRetenido(String razonSocialSujetoRetenido) {
		this.razonSocialSujetoRetenido = razonSocialSujetoRetenido;
	}

	/**
	 * @return the identificacionSujetoRetenido
	 */
	public String getIdentificacionSujetoRetenido() {
		return identificacionSujetoRetenido;
	}

	/**
	 * @param identificacionSujetoRetenido the identificacionSujetoRetenido to set
	 */
	public void setIdentificacionSujetoRetenido(String identificacionSujetoRetenido) {
		this.identificacionSujetoRetenido = identificacionSujetoRetenido;
	}

	/**
	 * @return the periodoFiscal
	 */
	public String getPeriodoFiscal() {
		return periodoFiscal;
	}

	/**
	 * @param periodoFiscal the periodoFiscal to set
	 */
	public void setPeriodoFiscal(String periodoFiscal) {
		this.periodoFiscal = periodoFiscal;
	}

}
