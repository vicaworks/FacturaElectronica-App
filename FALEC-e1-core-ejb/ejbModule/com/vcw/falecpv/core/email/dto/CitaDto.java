package com.vcw.falecpv.core.email.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Para definir los campos correspondientes solo a Citas
 */
public class CitaDto implements Serializable {

	private static final long serialVersionUID = -6578874658114166380L;

	protected Date fechaHoraInicio;
	protected Date fechaHoraFin;
	protected String lugar;

	/**
	 * @return the fechaHoraInicio
	 */
	public Date getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	/**
	 * @param fechaHoraInicio
	 *            the fechaHoraInicio to set
	 */
	public void setFechaHoraInicio(Date fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	/**
	 * @return the fechaHoraFin
	 */
	public Date getFechaHoraFin() {
		return fechaHoraFin;
	}

	/**
	 * @param fechaHoraFin
	 *            the fechaHoraFin to set
	 */
	public void setFechaHoraFin(Date fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	/**
	 * @return the lugar
	 */
	public String getLugar() {
		return lugar;
	}

	/**
	 * @param lugar
	 *            the lugar to set
	 */
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

}
