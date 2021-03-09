package com.vcw.falecpv.core.email.dto;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.vcw.falecpv.core.email.ActualizaNegocio;
import com.vcw.falecpv.core.modelo.dto.FileDto;


/**
 * Agrupa todos los datos respecto a correos en general, citas y la forma de
 * conexion
 */
public class EmailDto implements Serializable {

	private static final long serialVersionUID = -6578874658114166380L;

	protected List<String> correosTo;
	protected List<String> correosCC;
	protected List<String> correosCCo;
	protected String asunto;
	protected boolean correoHtml;
	protected String contenido;
	protected List<File> adjuntos;
	protected List<FileDto> fileDtos;

	protected boolean cita;
	protected boolean cancelacionCita;

	protected ConexionDto datosConexion;
	protected CitaDto datosCita;
	protected String logPath;

	protected boolean cambiarNombre = false;
	protected String nombreAdjunto;
	
	protected ActualizaNegocio actualizaNegocio;
	protected String toString;
	protected String ccString;
	protected boolean seleccion = false;
	

	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * @param asunto
	 *            the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * @return the correoHtml
	 */
	public boolean isCorreoHtml() {
		return correoHtml;
	}

	/**
	 * @param correoHtml
	 *            the correoHtml to set
	 */
	public void setCorreoHtml(boolean correoHtml) {
		this.correoHtml = correoHtml;
	}

	/**
	 * @return the contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido
	 *            the contenido to set
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * @return the cita
	 */
	public boolean isCita() {
		return cita;
	}

	/**
	 * @param cita
	 *            the cita to set
	 */
	public void setCita(boolean cita) {
		this.cita = cita;
	}

	/**
	 * @return the cancelacionCita
	 */
	public boolean isCancelacionCita() {
		return cancelacionCita;
	}

	/**
	 * @param cancelacionCita
	 *            the cancelacionCita to set
	 */
	public void setCancelacionCita(boolean cancelacionCita) {
		this.cancelacionCita = cancelacionCita;
	}

	/**
	 * @return the datosConexion
	 */
	public ConexionDto getDatosConexion() {
		return datosConexion;
	}

	/**
	 * @param datosConexion
	 *            the datosConexion to set
	 */
	public void setDatosConexion(ConexionDto datosConexion) {
		this.datosConexion = datosConexion;
	}

	/**
	 * @return the datosCita
	 */
	public CitaDto getDatosCita() {
		return datosCita;
	}

	/**
	 * @param datosCita
	 *            the datosCita to set
	 */
	public void setDatosCita(CitaDto datosCita) {
		this.datosCita = datosCita;
	}

	/**
	 * @return the correosTo
	 */
	public List<String> getCorreosTo() {
		return correosTo;
	}

	/**
	 * @param correosTo
	 *            the correosTo to set
	 */
	public void setCorreosTo(List<String> correosTo) {
		this.correosTo = correosTo;
	}

	/**
	 * @return the correosCC
	 */
	public List<String> getCorreosCC() {
		return correosCC;
	}

	/**
	 * @param correosCC
	 *            the correosCC to set
	 */
	public void setCorreosCC(List<String> correosCC) {
		this.correosCC = correosCC;
	}

	/**
	 * @return the adjuntos
	 */
	public List<File> getAdjuntos() {
		return adjuntos;
	}

	/**
	 * @param adjuntos
	 *            the adjuntos to set
	 */
	public void setAdjuntos(List<File> adjuntos) {
		this.adjuntos = adjuntos;
	}

	/**
	 * @return the logPath
	 */
	public String getLogPath() {
		return logPath;
	}

	/**
	 * @param logPath
	 *            the logPath to set
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	/**
	 * @return the actualizaNegocio
	 */
	public ActualizaNegocio getActualizaNegocio() {
		return actualizaNegocio;
	}

	/**
	 * @param actualizaNegocio
	 *            the actualizaNegocio to set
	 */
	public void setActualizaNegocio(ActualizaNegocio actualizaNegocio) {
		this.actualizaNegocio = actualizaNegocio;
	}

	public List<String> getCorreosCCo() {
		return correosCCo;
	}

	public void setCorreosCCo(List<String> correosCCo) {
		this.correosCCo = correosCCo;
	}

	/**
	 * @return the cambiarNombre
	 */
	public boolean isCambiarNombre() {
		return cambiarNombre;
	}

	/**
	 * @param cambiarNombre the cambiarNombre to set
	 */
	public void setCambiarNombre(boolean cambiarNombre) {
		this.cambiarNombre = cambiarNombre;
	}

	/**
	 * @return the nombreAdjunto
	 */
	public String getNombreAdjunto() {
		return nombreAdjunto;
	}

	/**
	 * @param nombreAdjunto the nombreAdjunto to set
	 */
	public void setNombreAdjunto(String nombreAdjunto) {
		this.nombreAdjunto = nombreAdjunto;
	}

	/**
	 * @return the toString
	 */
	public String getToString() {
		return toString;
	}

	/**
	 * @param toString the toString to set
	 */
	public void setToString(String toString) {
		this.toString = toString;
	}

	/**
	 * @return the ccString
	 */
	public String getCcString() {
		return ccString;
	}

	/**
	 * @param ccString the ccString to set
	 */
	public void setCcString(String ccString) {
		this.ccString = ccString;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the fileDtos
	 */
	public List<FileDto> getFileDtos() {
		return fileDtos;
	}

	/**
	 * @param fileDtos the fileDtos to set
	 */
	public void setFileDtos(List<FileDto> fileDtos) {
		this.fileDtos = fileDtos;
	}

	

}
