/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.math.BigDecimal;

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Iva;

/**
 * @author cristianvillarreal
 *
 */
public class XmlTotalComprobante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8157561438547946411L;

	private String codigoSri;
	private String label;
	private BigDecimal valor = BigDecimal.ZERO;
	private Iva iva;
	
	/**
	 * 
	 */
	public XmlTotalComprobante() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the codigoSri
	 */
	public String getCodigoSri() {
		return codigoSri;
	}

	/**
	 * @param codigoSri the codigoSri to set
	 */
	public void setCodigoSri(String codigoSri) {
		this.codigoSri = codigoSri;
	}

	/**
	 * @return the iva
	 */
	public Iva getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(Iva iva) {
		this.iva = iva;
	}

}
