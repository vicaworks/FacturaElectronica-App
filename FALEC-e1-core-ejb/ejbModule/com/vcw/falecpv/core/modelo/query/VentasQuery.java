/**
 * 
 */
package com.vcw.falecpv.core.modelo.query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;

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
	private String iddetalle;
	private String idtipocomprobante;
	private String identificador; 
	private String idproducto;
	private String descripcion;
	private BigDecimal preciounitario = BigDecimal.ZERO;
	private BigDecimal descuento = BigDecimal.ZERO;
	private BigDecimal preciototalsinimpuesto = BigDecimal.ZERO;
	private String idiva;
	private BigDecimal porcentajeiva = BigDecimal.ZERO;
	private BigDecimal valoriva = BigDecimal.ZERO;
	private String idice;
	private BigDecimal porcentajeice = BigDecimal.ZERO;
	private BigDecimal valorice = BigDecimal.ZERO;
	private BigDecimal preciototal = BigDecimal.ZERO;
	private String fabricante;
	private String categoria;
	private String codigoprincipal;
	
	/**
	 * 
	 */
	public VentasQuery() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	public String getTipoComprobante() {
		return GenTipoDocumentoEnum.getByIdentificador(identificador);
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

	/**
	 * @return the iddetalle
	 */
	public String getIddetalle() {
		return iddetalle;
	}

	/**
	 * @param iddetalle the iddetalle to set
	 */
	public void setIddetalle(String iddetalle) {
		this.iddetalle = iddetalle;
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
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the idproducto
	 */
	public String getIdproducto() {
		return idproducto;
	}

	/**
	 * @param idproducto the idproducto to set
	 */
	public void setIdproducto(String idproducto) {
		this.idproducto = idproducto;
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
	 * @return the preciounitario
	 */
	public BigDecimal getPreciounitario() {
		return preciounitario;
	}

	/**
	 * @param preciounitario the preciounitario to set
	 */
	public void setPreciounitario(BigDecimal preciounitario) {
		this.preciounitario = preciounitario;
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
	 * @return the preciototalsinimpuesto
	 */
	public BigDecimal getPreciototalsinimpuesto() {
		return preciototalsinimpuesto;
	}

	/**
	 * @param preciototalsinimpuesto the preciototalsinimpuesto to set
	 */
	public void setPreciototalsinimpuesto(BigDecimal preciototalsinimpuesto) {
		this.preciototalsinimpuesto = preciototalsinimpuesto;
	}

	/**
	 * @return the idiva
	 */
	public String getIdiva() {
		return idiva;
	}

	/**
	 * @param idiva the idiva to set
	 */
	public void setIdiva(String idiva) {
		this.idiva = idiva;
	}

	/**
	 * @return the porcentajeiva
	 */
	public BigDecimal getPorcentajeiva() {
		return porcentajeiva;
	}

	/**
	 * @param porcentajeiva the porcentajeiva to set
	 */
	public void setPorcentajeiva(BigDecimal porcentajeiva) {
		this.porcentajeiva = porcentajeiva;
	}

	/**
	 * @return the valoriva
	 */
	public BigDecimal getValoriva() {
		return valoriva;
	}

	/**
	 * @param valoriva the valoriva to set
	 */
	public void setValoriva(BigDecimal valoriva) {
		this.valoriva = valoriva;
	}

	/**
	 * @return the idice
	 */
	public String getIdice() {
		return idice;
	}

	/**
	 * @param idice the idice to set
	 */
	public void setIdice(String idice) {
		this.idice = idice;
	}

	/**
	 * @return the porcentajeice
	 */
	public BigDecimal getPorcentajeice() {
		return porcentajeice;
	}

	/**
	 * @param porcentajeice the porcentajeice to set
	 */
	public void setPorcentajeice(BigDecimal porcentajeice) {
		this.porcentajeice = porcentajeice;
	}

	/**
	 * @return the valorice
	 */
	public BigDecimal getValorice() {
		return valorice;
	}

	/**
	 * @param valorice the valorice to set
	 */
	public void setValorice(BigDecimal valorice) {
		this.valorice = valorice;
	}

	/**
	 * @return the preciototal
	 */
	public BigDecimal getPreciototal() {
		return preciototal;
	}

	/**
	 * @param preciototal the preciototal to set
	 */
	public void setPreciototal(BigDecimal preciototal) {
		this.preciototal = preciototal;
	}

	/**
	 * @return the fabricante
	 */
	public String getFabricante() {
		return fabricante;
	}

	/**
	 * @param fabricante the fabricante to set
	 */
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the codigoprincipal
	 */
	public String getCodigoprincipal() {
		return codigoprincipal;
	}

	/**
	 * @param codigoprincipal the codigoprincipal to set
	 */
	public void setCodigoprincipal(String codigoprincipal) {
		this.codigoprincipal = codigoprincipal;
	}

}
