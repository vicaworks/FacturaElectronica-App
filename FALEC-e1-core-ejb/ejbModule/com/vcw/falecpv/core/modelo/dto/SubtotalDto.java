/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cristianvillarreal
 *
 */
public class SubtotalDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1593349165551719933L;

	private String concepto;
	private BigDecimal valor = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public SubtotalDto() {
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
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

}
