/**
 * 
 */
package com.vcw.falecpv.core.modelo.query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cristianvillarreal
 *
 */
public class PagosQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979300288221515126L;
	
	private String idpago;
	private String idestablecimiento;
	private Date updated;
	private Date fechaemision;
	private String idtipocomprobante;
	private String comprobante;
	private String idcabecera;
	private String numdocumento;
	private String idtipopago;
	private String descripcion;
	private String nombre;
	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal valorentrega = BigDecimal.ZERO;
	private BigDecimal cambio = BigDecimal.ZERO;
	private String numerodocumento;
	private String nombrebanco;
	private String idcliente;
	private String razonsocial;
	private String identificacion;
	private BigDecimal totalconimpuestos = BigDecimal.ZERO;
	private BigDecimal valorretenido = BigDecimal.ZERO;
	private BigDecimal valorapagar = BigDecimal.ZERO;

	/**
	 * 
	 */
	public PagosQuery() {
	}

	/**
	 * @return the idpago
	 */
	public String getIdpago() {
		return idpago;
	}

	/**
	 * @param idpago the idpago to set
	 */
	public void setIdpago(String idpago) {
		this.idpago = idpago;
	}

	/**
	 * @return the idestablecimiento
	 */
	public String getIdestablecimiento() {
		return idestablecimiento;
	}

	/**
	 * @param idestablecimiento the idestablecimiento to set
	 */
	public void setIdestablecimiento(String idestablecimiento) {
		this.idestablecimiento = idestablecimiento;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
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
	 * @return the idtipocomprobante
	 */
	public String getIdtipocomprobante() {
		return idtipocomprobante;
	}

	/**
	 * @param idtipocomprobante the idtipocomprobante to set
	 */
	public void setIdtipocomprobante(String idtipocomprobante) {
		this.idtipocomprobante = idtipocomprobante;
	}

	/**
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
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
	 * @return the numdocumento
	 */
	public String getNumdocumento() {
		return numdocumento;
	}

	/**
	 * @param numdocumento the numdocumento to set
	 */
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}

	/**
	 * @return the idtipopago
	 */
	public String getIdtipopago() {
		return idtipopago;
	}

	/**
	 * @param idtipopago the idtipopago to set
	 */
	public void setIdtipopago(String idtipopago) {
		this.idtipopago = idtipopago;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the valorentrega
	 */
	public BigDecimal getValorentrega() {
		return valorentrega;
	}

	/**
	 * @param valorentrega the valorentrega to set
	 */
	public void setValorentrega(BigDecimal valorentrega) {
		this.valorentrega = valorentrega;
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
	 * @return the numerodocumento
	 */
	public String getNumerodocumento() {
		return numerodocumento;
	}

	/**
	 * @param numerodocumento the numerodocumento to set
	 */
	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	/**
	 * @return the nombrebanco
	 */
	public String getNombrebanco() {
		return nombrebanco;
	}

	/**
	 * @param nombrebanco the nombrebanco to set
	 */
	public void setNombrebanco(String nombrebanco) {
		this.nombrebanco = nombrebanco;
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
	 * @return the identificacion
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * @param identificacion the identificacion to set
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	/**
	 * @return the totalconimpuestos
	 */
	public BigDecimal getTotalconimpuestos() {
		return totalconimpuestos;
	}

	/**
	 * @param totalconimpuestos the totalconimpuestos to set
	 */
	public void setTotalconimpuestos(BigDecimal totalconimpuestos) {
		this.totalconimpuestos = totalconimpuestos;
	}

	/**
	 * @return the valorretenido
	 */
	public BigDecimal getValorretenido() {
		return valorretenido;
	}

	/**
	 * @param valorretenido the valorretenido to set
	 */
	public void setValorretenido(BigDecimal valorretenido) {
		this.valorretenido = valorretenido;
	}

	/**
	 * @return the valorapagar
	 */
	public BigDecimal getValorapagar() {
		return valorapagar;
	}

	/**
	 * @param valorapagar the valorapagar to set
	 */
	public void setValorapagar(BigDecimal valorapagar) {
		this.valorapagar = valorapagar;
	}


}
