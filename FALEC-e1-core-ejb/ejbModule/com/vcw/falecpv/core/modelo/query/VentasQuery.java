/**
 * 
 */
package com.vcw.falecpv.core.modelo.query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;

/**
 * @author cristianvillarreal
 *
 */
public class VentasQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7547423286215345479L;

	private String id;
	private String idcabecera;
	private String secuencial;
	private Date fechaemision;
	private String idcliente;
	private String razonsocial;
	private String nombrepantalla;
	private BigDecimal cantidad = BigDecimal.ZERO;
	private BigDecimal subtotal = BigDecimal.ZERO;
	private BigDecimal iva = BigDecimal.ZERO;
	private BigDecimal ice = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	private String estado;
	private String estadoautorizacion;
	private BigDecimal licitado = BigDecimal.ZERO;
	private BigDecimal cambio = BigDecimal.ZERO;
	private BigDecimal totaldescuento = BigDecimal.ZERO;
	private BigDecimal totalpago = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public VentasQuery() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the idcabecera
	 */
	public String getIdcabecera() {
		return idcabecera;
	}

	/**
	 * @param idcabecera the idcabecera to set
	 */
	public void setIdcabecera(String idcabecera) {
		this.idcabecera = idcabecera;
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
	 * @return the fechaemision
	 */
	public Date getFechaemision() {
		return fechaemision;
	}

	/**
	 * @param fechaemision the fechaemision to set
	 */
	public void setFechaemision(Date fechaemision) {
		this.fechaemision = fechaemision;
	}

	/**
	 * @return the idcliente
	 */
	public String getIdcliente() {
		return idcliente;
	}

	/**
	 * @param idcliente the idcliente to set
	 */
	public void setIdcliente(String idcliente) {
		this.idcliente = idcliente;
	}

	/**
	 * @return the razonsocial
	 */
	public String getRazonsocial() {
		return razonsocial;
	}

	/**
	 * @param razonsocial the razonsocial to set
	 */
	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}

	/**
	 * @return the nombrepantalla
	 */
	public String getNombrepantalla() {
		return nombrepantalla;
	}

	/**
	 * @param nombrepantalla the nombrepantalla to set
	 */
	public void setNombrepantalla(String nombrepantalla) {
		this.nombrepantalla = nombrepantalla;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the estadoautorizacion
	 */
	public String getEstadoautorizacion() {
		return estadoautorizacion;
	}

	/**
	 * @param estadoautorizacion the estadoautorizacion to set
	 */
	public void setEstadoautorizacion(String estadoautorizacion) {
		this.estadoautorizacion = estadoautorizacion;
	}

	/**
	 * @return the licitado
	 */
	public BigDecimal getLicitado() {
		return licitado;
	}

	/**
	 * @param licitado the licitado to set
	 */
	public void setLicitado(BigDecimal licitado) {
		this.licitado = licitado;
	}

	/**
	 * @return the cambio
	 */
	public BigDecimal getCambio() {
		return cambio;
	}

	/**
	 * @param cambio the cambio to set
	 */
	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	/**
	 * @return the totaldescuento
	 */
	public BigDecimal getTotaldescuento() {
		return totaldescuento;
	}

	/**
	 * @param totaldescuento the totaldescuento to set
	 */
	public void setTotaldescuento(BigDecimal totaldescuento) {
		this.totaldescuento = totaldescuento;
	}
	
	public String getEstadoStyle() {
		return ComprobanteEstadoEnum.getStyleEstado(estado);
	}

	/**
	 * @return the totalpago
	 */
	public BigDecimal getTotalpago() {
		return totalpago;
	}

	/**
	 * @param totalpago the totalpago to set
	 */
	public void setTotalpago(BigDecimal totalpago) {
		this.totalpago = totalpago;
	}

}
