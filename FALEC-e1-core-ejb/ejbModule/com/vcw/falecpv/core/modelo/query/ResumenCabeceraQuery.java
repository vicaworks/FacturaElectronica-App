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
public class ResumenCabeceraQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1989264187299808606L;
	
	private Date fechaemision;
	private String idcabecera;
	private String idtipocomprobante;
	private String indentificadorcomprobante;
	private String comprobante;
	private String secuencial;
	private String numdocumento;
	private String numfactura;
	private String idcliente;
	private String clienteidentificacion;
	private String cliente;
	private String estado;	
	private String claveacceso;
	private String estadoautorizacion;
	private String numeroautorizacion;
	private BigDecimal totalbaseimponible = BigDecimal.ZERO;
	private BigDecimal totalsinimpuestos = BigDecimal.ZERO;
	private BigDecimal totalice = BigDecimal.ZERO;
	private BigDecimal totaliva = BigDecimal.ZERO;
	private BigDecimal totalconimpuestos = BigDecimal.ZERO;
	private String idguiaremision;
	

	/**
	 * 
	 */
	public ResumenCabeceraQuery() {
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	/**
	 * @return
	 */
	public String getEstadoStyle() {
		
		return ComprobanteEstadoEnum.getStyleEstado(estado);
		
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
	 * @return the indentificadorcomprobante
	 */
	public String getIndentificadorcomprobante() {
		return indentificadorcomprobante;
	}

	/**
	 * @param indentificadorcomprobante the indentificadorcomprobante to set
	 */
	public void setIndentificadorcomprobante(String indentificadorcomprobante) {
		this.indentificadorcomprobante = indentificadorcomprobante;
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
	 * @return the numfactura
	 */
	public String getNumfactura() {
		return numfactura;
	}

	/**
	 * @param numfactura the numfactura to set
	 */
	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
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
	 * @return the clienteidentificacion
	 */
	public String getClienteidentificacion() {
		return clienteidentificacion;
	}

	/**
	 * @param clienteidentificacion the clienteidentificacion to set
	 */
	public void setClienteidentificacion(String clienteidentificacion) {
		this.clienteidentificacion = clienteidentificacion;
	}

	/**
	 * @return the cliente
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
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
	 * @return the claveacceso
	 */
	public String getClaveacceso() {
		return claveacceso;
	}

	/**
	 * @param claveacceso the claveacceso to set
	 */
	public void setClaveacceso(String claveacceso) {
		this.claveacceso = claveacceso;
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
	 * @return the numeroautorizacion
	 */
	public String getNumeroautorizacion() {
		return numeroautorizacion;
	}

	/**
	 * @param numeroautorizacion the numeroautorizacion to set
	 */
	public void setNumeroautorizacion(String numeroautorizacion) {
		this.numeroautorizacion = numeroautorizacion;
	}

	/**
	 * @return the totalbaseimponible
	 */
	public BigDecimal getTotalbaseimponible() {
		return totalbaseimponible;
	}

	/**
	 * @param totalbaseimponible the totalbaseimponible to set
	 */
	public void setTotalbaseimponible(BigDecimal totalbaseimponible) {
		this.totalbaseimponible = totalbaseimponible;
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
	 * @return the totalice
	 */
	public BigDecimal getTotalice() {
		return totalice;
	}

	/**
	 * @param totalice the totalice to set
	 */
	public void setTotalice(BigDecimal totalice) {
		this.totalice = totalice;
	}

	/**
	 * @return the totaliva
	 */
	public BigDecimal getTotaliva() {
		return totaliva;
	}

	/**
	 * @param totaliva the totaliva to set
	 */
	public void setTotaliva(BigDecimal totaliva) {
		this.totaliva = totaliva;
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
	 * @return the idguiaremision
	 */
	public String getIdguiaremision() {
		return idguiaremision;
	}

	/**
	 * @param idguiaremision the idguiaremision to set
	 */
	public void setIdguiaremision(String idguiaremision) {
		this.idguiaremision = idguiaremision;
	}

}
