/**
 * 
 */
package com.vcw.falecpv.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cristianvillarreal
 *
 */
public class VentasDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 707443473821972965L;

	private String id;
	private String cliente;
	private Date hora;
	private Double valor;
	
	/**
	 * 
	 */
	public VentasDto() {
	}

	/**
	 * @param id
	 * @param cliente
	 * @param hora
	 * @param valor
	 */
	public VentasDto(String id, String cliente, Date hora, Double valor) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.hora = hora;
		this.valor = valor;
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
	 * @return the hora
	 */
	public Date getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(Date hora) {
		this.hora = hora;
	}

	/**
	 * @return the valor
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	

}
