/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	private BigDecimal retencion = BigDecimal.ZERO;
	private BigDecimal apagar = BigDecimal.ZERO;
	private BigDecimal totalRetenido = BigDecimal.ZERO;
	private List<SubtotalDto> subtotalDtoList = new ArrayList<>();
	
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

	/**
	 * @return the retencion
	 */
	public BigDecimal getRetencion() {
		return retencion;
	}

	/**
	 * @param retencion the retencion to set
	 */
	public void setRetencion(BigDecimal retencion) {
		this.retencion = retencion;
	}

	/**
	 * @return the apagar
	 */
	public BigDecimal getApagar() {
		return apagar;
	}

	/**
	 * @param apagar the apagar to set
	 */
	public void setApagar(BigDecimal apagar) {
		this.apagar = apagar;
	}

	/**
	 * @return the totalRetenido
	 */
	public BigDecimal getTotalRetenido() {
		return totalRetenido;
	}

	/**
	 * @param totalRetenido the totalRetenido to set
	 */
	public void setTotalRetenido(BigDecimal totalRetenido) {
		this.totalRetenido = totalRetenido;
	}

	/**
	 * @return the subtotalDtoList
	 */
	public List<SubtotalDto> getSubtotalDtoList() {
		return subtotalDtoList;
	}

	/**
	 * @param subtotalDtoList the subtotalDtoList to set
	 */
	public void setSubtotalDtoList(List<SubtotalDto> subtotalDtoList) {
		this.subtotalDtoList = subtotalDtoList;
	}

}
