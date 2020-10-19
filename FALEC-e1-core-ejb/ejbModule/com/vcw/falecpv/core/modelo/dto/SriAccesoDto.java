/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
public class SriAccesoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148340335207536611L;
	
	private String wsdl;
	private String host;
	private String metodoPost;
	
	/**
	 * 
	 */
	public SriAccesoDto() {
	}
	
	/**
	 * @return the wsdl
	 */
	public String getWsdl() {
		return wsdl;
	}

	/**
	 * @param wsdl the wsdl to set
	 */
	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the metodoPost
	 */
	public String getMetodoPost() {
		return metodoPost;
	}

	/**
	 * @param metodoPost the metodoPost to set
	 */
	public void setMetodoPost(String metodoPost) {
		this.metodoPost = metodoPost;
	}



	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

}
