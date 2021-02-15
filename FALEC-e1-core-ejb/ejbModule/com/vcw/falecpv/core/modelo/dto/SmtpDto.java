/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;

/**
 * @author cristianvillarreal
 *
 */
public class SmtpDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1247433814719554946L;
	
	private String servidor;
	private String puerto;
	private String usuario;
	private String clave;
	private String nombreRemitente;
	private String emailRemitente;
	private boolean ssl = false;
	private boolean auth = false;
	private boolean tls = false;
	private boolean smtpPropio;
	
	
	
	/**
	 * 
	 */
	public SmtpDto() {
	}



	public SmtpDto(String servidor, String puerto, String usuario, String clave, String nombreRemitente,
			String emailRemitente, boolean ssl, boolean auth, boolean tls) {
		super();
		this.servidor = servidor;
		this.puerto = puerto;
		this.usuario = usuario;
		this.clave = clave;
		this.nombreRemitente = nombreRemitente;
		this.emailRemitente = emailRemitente;
		this.ssl = ssl;
		this.auth = auth;
		this.tls = tls;
	}



	/**
	 * @return the servidor
	 */
	public String getServidor() {
		return servidor;
	}



	/**
	 * @param servidor the servidor to set
	 */
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}



	/**
	 * @return the puerto
	 */
	public String getPuerto() {
		return puerto;
	}



	/**
	 * @param puerto the puerto to set
	 */
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}



	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}



	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}



	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}



	/**
	 * @return the nombreRemitente
	 */
	public String getNombreRemitente() {
		return nombreRemitente;
	}



	/**
	 * @param nombreRemitente the nombreRemitente to set
	 */
	public void setNombreRemitente(String nombreRemitente) {
		this.nombreRemitente = nombreRemitente;
	}



	/**
	 * @return the emailRemitente
	 */
	public String getEmailRemitente() {
		return emailRemitente;
	}



	/**
	 * @param emailRemitente the emailRemitente to set
	 */
	public void setEmailRemitente(String emailRemitente) {
		this.emailRemitente = emailRemitente;
	}



	/**
	 * @return the ssl
	 */
	public boolean isSsl() {
		return ssl;
	}



	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}



	/**
	 * @return the auth
	 */
	public boolean isAuth() {
		return auth;
	}



	/**
	 * @param auth the auth to set
	 */
	public void setAuth(boolean auth) {
		this.auth = auth;
	}



	/**
	 * @return the tls
	 */
	public boolean isTls() {
		return tls;
	}



	/**
	 * @param tls the tls to set
	 */
	public void setTls(boolean tls) {
		this.tls = tls;
	}



	/**
	 * @return the smtpPropio
	 */
	public boolean isSmtpPropio() {
		return smtpPropio;
	}



	/**
	 * @param smtpPropio the smtpPropio to set
	 */
	public void setSmtpPropio(boolean smtpPropio) {
		this.smtpPropio = smtpPropio;
	}

}
