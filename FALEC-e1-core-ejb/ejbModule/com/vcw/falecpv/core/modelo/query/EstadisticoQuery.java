/**
 * 
 */
package com.vcw.falecpv.core.modelo.query;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cristianvillarreal
 *
 */
public class EstadisticoQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3611987748430077908L;
	
	
	private String label1;
	private String label2;
	private BigDecimal valor1 = BigDecimal.ZERO;
	private BigDecimal valor2 = BigDecimal.ZERO;
	private BigDecimal valor3 = BigDecimal.ZERO;
	private BigDecimal valor4 = BigDecimal.ZERO;
	private BigDecimal valor5 = BigDecimal.ZERO;
	
	
	/**
	 * 
	 */
	public EstadisticoQuery() {
	}


	/**
	 * @return the label1
	 */
	public String getLabel1() {
		return label1;
	}


	/**
	 * @param label1 the label1 to set
	 */
	public void setLabel1(String label1) {
		this.label1 = label1;
	}


	/**
	 * @return the label2
	 */
	public String getLabel2() {
		return label2;
	}


	/**
	 * @param label2 the label2 to set
	 */
	public void setLabel2(String label2) {
		this.label2 = label2;
	}


	/**
	 * @return the valor1
	 */
	public BigDecimal getValor1() {
		return valor1;
	}


	/**
	 * @param valor1 the valor1 to set
	 */
	public void setValor1(BigDecimal valor1) {
		this.valor1 = valor1;
	}


	/**
	 * @return the valor2
	 */
	public BigDecimal getValor2() {
		return valor2;
	}


	/**
	 * @param valor2 the valor2 to set
	 */
	public void setValor2(BigDecimal valor2) {
		this.valor2 = valor2;
	}


	/**
	 * @return the valor3
	 */
	public BigDecimal getValor3() {
		return valor3;
	}


	/**
	 * @param valor3 the valor3 to set
	 */
	public void setValor3(BigDecimal valor3) {
		this.valor3 = valor3;
	}


	/**
	 * @return the valor4
	 */
	public BigDecimal getValor4() {
		return valor4;
	}


	/**
	 * @param valor4 the valor4 to set
	 */
	public void setValor4(BigDecimal valor4) {
		this.valor4 = valor4;
	}


	/**
	 * @return the valor5
	 */
	public BigDecimal getValor5() {
		return valor5;
	}


	/**
	 * @param valor5 the valor5 to set
	 */
	public void setValor5(BigDecimal valor5) {
		this.valor5 = valor5;
	}

	


}
