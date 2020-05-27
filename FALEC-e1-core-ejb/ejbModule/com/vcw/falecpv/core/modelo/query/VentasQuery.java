/**
 * 
 */
package com.vcw.falecpv.core.modelo.query;

import java.io.Serializable;

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
	
	/**
	 * 
	 */
	public VentasQuery() {
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

}
