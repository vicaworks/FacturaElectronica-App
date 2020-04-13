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
	private String estado;
	private String nombreGarante1;
	private String cedulaGarante1;
	private String direccionGarante1;
	private String telefonoGarante1;
	private String ocupacionGarante1;
	private String nombreGarante2;
	private String cedulaGarante2;
	private String direccionGarante2;
	private String telefonoGarante2;
	private String ocupacionGarante2;
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
	 * @return the nombreGarante1
	 */
	public String getNombreGarante1() {
		return nombreGarante1;
	}

	/**
	 * @param nombreGarante1 the nombreGarante1 to set
	 */
	public void setNombreGarante1(String nombreGarante1) {
		this.nombreGarante1 = nombreGarante1;
	}

	/**
	 * @return the cedulaGarante1
	 */
	public String getCedulaGarante1() {
		return cedulaGarante1;
	}

	/**
	 * @param cedulaGarante1 the cedulaGarante1 to set
	 */
	public void setCedulaGarante1(String cedulaGarante1) {
		this.cedulaGarante1 = cedulaGarante1;
	}

	/**
	 * @return the direccionGarante1
	 */
	public String getDireccionGarante1() {
		return direccionGarante1;
	}

	/**
	 * @param direccionGarante1 the direccionGarante1 to set
	 */
	public void setDireccionGarante1(String direccionGarante1) {
		this.direccionGarante1 = direccionGarante1;
	}

	/**
	 * @return the telefonoGarante1
	 */
	public String getTelefonoGarante1() {
		return telefonoGarante1;
	}

	/**
	 * @param telefonoGarante1 the telefonoGarante1 to set
	 */
	public void setTelefonoGarante1(String telefonoGarante1) {
		this.telefonoGarante1 = telefonoGarante1;
	}

	/**
	 * @return the ocupacionGarante1
	 */
	public String getOcupacionGarante1() {
		return ocupacionGarante1;
	}

	/**
	 * @param ocupacionGarante1 the ocupacionGarante1 to set
	 */
	public void setOcupacionGarante1(String ocupacionGarante1) {
		this.ocupacionGarante1 = ocupacionGarante1;
	}

	/**
	 * @return the nombreGarante2
	 */
	public String getNombreGarante2() {
		return nombreGarante2;
	}

	/**
	 * @param nombreGarante2 the nombreGarante2 to set
	 */
	public void setNombreGarante2(String nombreGarante2) {
		this.nombreGarante2 = nombreGarante2;
	}

	/**
	 * @return the cedulaGarante2
	 */
	public String getCedulaGarante2() {
		return cedulaGarante2;
	}

	/**
	 * @param cedulaGarante2 the cedulaGarante2 to set
	 */
	public void setCedulaGarante2(String cedulaGarante2) {
		this.cedulaGarante2 = cedulaGarante2;
	}

	/**
	 * @return the direccionGarante2
	 */
	public String getDireccionGarante2() {
		return direccionGarante2;
	}

	/**
	 * @param direccionGarante2 the direccionGarante2 to set
	 */
	public void setDireccionGarante2(String direccionGarante2) {
		this.direccionGarante2 = direccionGarante2;
	}

	/**
	 * @return the telefonoGarante2
	 */
	public String getTelefonoGarante2() {
		return telefonoGarante2;
	}

	/**
	 * @param telefonoGarante2 the telefonoGarante2 to set
	 */
	public void setTelefonoGarante2(String telefonoGarante2) {
		this.telefonoGarante2 = telefonoGarante2;
	}

	/**
	 * @return the ocupacionGarante2
	 */
	public String getOcupacionGarante2() {
		return ocupacionGarante2;
	}

	/**
	 * @param ocupacionGarante2 the ocupacionGarante2 to set
	 */
	public void setOcupacionGarante2(String ocupacionGarante2) {
		this.ocupacionGarante2 = ocupacionGarante2;
	}
}