package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.servitec.common.util.PojoUtil;

/**
 * @author Jorge Toaza
 *
 */
public class ImportClienteDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7319671072016809168L;
	
	private int fila;
	private String idTipoIdentificacion;
	private String identificacion;
	private String razonSocial;
	private String direccion;
	private String correoElectronico;
	private String telefono;
	private boolean error = false;
	private String novedad;
	private String idCliente;
	
	/**
	 * 
	 */
	public ImportClienteDto() {
	}

	/**
	 * @return the fila
	 */
	public int getFila() {
		return fila;
	}

	/**
	 * @param fila the fila to set
	 */
	public void setFila(int fila) {
		this.fila = fila;
	}

	/**
	 * @return the idTipoIdentificacion
	 */
	public String getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}

	/**
	 * @param idTipoIdentificacion the idTipoIdentificacion to set
	 */
	public void setIdTipoIdentificacion(String idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
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
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the correoElectronico
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	/**
	 * @param correoElectronico the correoElectronico to set
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the novedad
	 */
	public String getNovedad() {
		return novedad;
	}

	/**
	 * @param novedad the novedad to set
	 */
	public void setNovedad(String novedad) {
		this.novedad = novedad;
	}

	/**
	 * @return the idCliente
	 */
	public String getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
}