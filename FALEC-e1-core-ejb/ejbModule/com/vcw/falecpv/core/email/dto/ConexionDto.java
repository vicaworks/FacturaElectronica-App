package com.vcw.falecpv.core.email.dto;

import java.io.Serializable;

/**
 * Para definir los campos correspondientes a la conexion con el servidor de
 * correos
 */
public class ConexionDto implements Serializable {

	private static final long serialVersionUID = -391987894465564215L;

	protected String host;
	protected String port;
	protected String mailFrom;
	protected String mailFromName;
	protected String mailFromReplyTo;
	protected String mailFromNameReplyTo;
	protected String mailOwner;
	protected String mailOwnerPasw;
	protected String ssl;
	protected String starttls;
	protected String auth;

	/**
	 * 
	 */
	public ConexionDto() {
		super();
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @param mailFrom
	 *            the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return the mailFromName
	 */
	public String getMailFromName() {
		return mailFromName;
	}

	/**
	 * @param mailFromName
	 *            the mailFromName to set
	 */
	public void setMailFromName(String mailFromName) {
		this.mailFromName = mailFromName;
	}

	/**
	 * @return the mailOwner
	 */
	public String getMailOwner() {
		return mailOwner;
	}

	/**
	 * @param mailOwner
	 *            the mailOwner to set
	 */
	public void setMailOwner(String mailOwner) {
		this.mailOwner = mailOwner;
	}

	/**
	 * @return the mailOwnerPasw
	 */
	public String getMailOwnerPasw() {
		return mailOwnerPasw;
	}

	/**
	 * @param mailOwnerPasw
	 *            the mailOwnerPasw to set
	 */
	public void setMailOwnerPasw(String mailOwnerPasw) {
		this.mailOwnerPasw = mailOwnerPasw;
	}

	/**
	 * @return the ssl
	 */
	public String getSsl() {
		return ssl;
	}

	/**
	 * @param ssl
	 *            the ssl to set
	 */
	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the starttls
	 */
	public String getStarttls() {
		return starttls;
	}

	/**
	 * @param starttls
	 *            the starttls to set
	 */
	public void setStarttls(String starttls) {
		this.starttls = starttls;
	}

	/**
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * @param auth
	 *            the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getMailFromReplyTo() {
		return mailFromReplyTo;
	}

	public void setMailFromReplyTo(String mailFromReplyTo) {
		this.mailFromReplyTo = mailFromReplyTo;
	}

	public String getMailFromNameReplyTo() {
		return mailFromNameReplyTo;
	}

	public void setMailFromNameReplyTo(String mailFromNameReplyTo) {
		this.mailFromNameReplyTo = mailFromNameReplyTo;
	}

}
