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
public class TotalesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7236546037265079066L;
	
	private BigDecimal cantidad = BigDecimal.ZERO;
	private BigDecimal subtotal = BigDecimal.ZERO;
	private BigDecimal descuento = BigDecimal.ZERO;
	private BigDecimal iva = BigDecimal.ZERO;
	private BigDecimal ice = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal pago = BigDecimal.ZERO;
	private BigDecimal totalsinimpuestos = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public TotalesDto() {
	}

	/**
	 * @return the cantidad
	 */
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return the descuento
	 */
	public BigDecimal getDescuento() {
		return descuento;
	}

	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	/**
	 * @return the iva
	 */
	public BigDecimal getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	/**
	 * @return the ice
	 */
	public BigDecimal getIce() {
		return ice;
	}

	/**
	 * @param ice the ice to set
	 */
	public void setIce(BigDecimal ice) {
		this.ice = ice;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the pago
	 */
	public BigDecimal getPago() {
		return pago;
	}

	/**
	 * @param pago the pago to set
	 */
	public void setPago(BigDecimal pago) {
		this.pago = pago;
	}

	/**
	 * @return the totalsinimpuestos
	 */
	public BigDecimal getTotalsinimpuestos() {
		return totalsinimpuestos;
	}

	/**
	 * @param totalsinimpuestos the totalsinimpuestos to set
	 */
	public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
		this.totalsinimpuestos = totalsinimpuestos;
	}

}
