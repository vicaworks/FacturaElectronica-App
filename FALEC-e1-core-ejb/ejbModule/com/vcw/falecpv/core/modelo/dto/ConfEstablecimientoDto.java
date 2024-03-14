/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;

/**
 * @author cristianvillarreal
 *
 */
public class ConfEstablecimientoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5655087901792765638L;
	
	private boolean permisoModificacion;
	
	private boolean emailTesting;
	private String emailsEnvioTest;
	
	private boolean plantillaEmailEstablecimiento;
	private String plantillaEmail;
	
	private String plantillaFactura;
	private String plantillaFacturaPuntoVenta;
	private String plantillaRetencion;
	private String plantillaNotaCredito;
	private String plantillaNotaDebito;
	private String plantillaGuiaRemision;
	private String plantillaLiqCompra;
	

	/**
	 * 
	 */
	public ConfEstablecimientoDto() {
		
	}


	/**
	 * @return the permisoModificacion
	 */
	public boolean isPermisoModificacion() {
		return permisoModificacion;
	}


	/**
	 * @param permisoModificacion the permisoModificacion to set
	 */
	public void setPermisoModificacion(boolean permisoModificacion) {
		this.permisoModificacion = permisoModificacion;
	}


	/**
	 * @return the emailTesting
	 */
	public boolean isEmailTesting() {
		return emailTesting;
	}


	/**
	 * @param emailTesting the emailTesting to set
	 */
	public void setEmailTesting(boolean emailTesting) {
		this.emailTesting = emailTesting;
	}


	/**
	 * @return the emailsEnvioTest
	 */
	public String getEmailsEnvioTest() {
		return emailsEnvioTest;
	}


	/**
	 * @param emailsEnvioTest the emailsEnvioTest to set
	 */
	public void setEmailsEnvioTest(String emailsEnvioTest) {
		this.emailsEnvioTest = emailsEnvioTest;
	}


	/**
	 * @return the plantillaEmailEstablecimiento
	 */
	public boolean isPlantillaEmailEstablecimiento() {
		return plantillaEmailEstablecimiento;
	}


	/**
	 * @param plantillaEmailEstablecimiento the plantillaEmailEstablecimiento to set
	 */
	public void setPlantillaEmailEstablecimiento(boolean plantillaEmailEstablecimiento) {
		this.plantillaEmailEstablecimiento = plantillaEmailEstablecimiento;
	}


	/**
	 * @return the plantillaEmail
	 */
	public String getPlantillaEmail() {
		return plantillaEmail;
	}


	/**
	 * @param plantillaEmail the plantillaEmail to set
	 */
	public void setPlantillaEmail(String plantillaEmail) {
		this.plantillaEmail = plantillaEmail;
	}


	/**
	 * @return the plantillaFactura
	 */
	public String getPlantillaFactura() {
		return plantillaFactura;
	}


	/**
	 * @param plantillaFactura the plantillaFactura to set
	 */
	public void setPlantillaFactura(String plantillaFactura) {
		this.plantillaFactura = plantillaFactura;
	}


	/**
	 * @return the plantillaRetencion
	 */
	public String getPlantillaRetencion() {
		return plantillaRetencion;
	}


	/**
	 * @param plantillaRetencion the plantillaRetencion to set
	 */
	public void setPlantillaRetencion(String plantillaRetencion) {
		this.plantillaRetencion = plantillaRetencion;
	}


	/**
	 * @return the plantillaNotaCredito
	 */
	public String getPlantillaNotaCredito() {
		return plantillaNotaCredito;
	}


	/**
	 * @param plantillaNotaCredito the plantillaNotaCredito to set
	 */
	public void setPlantillaNotaCredito(String plantillaNotaCredito) {
		this.plantillaNotaCredito = plantillaNotaCredito;
	}


	/**
	 * @return the plantillaNotaDebito
	 */
	public String getPlantillaNotaDebito() {
		return plantillaNotaDebito;
	}


	/**
	 * @param plantillaNotaDebito the plantillaNotaDebito to set
	 */
	public void setPlantillaNotaDebito(String plantillaNotaDebito) {
		this.plantillaNotaDebito = plantillaNotaDebito;
	}


	/**
	 * @return the plantillaGuiaRemision
	 */
	public String getPlantillaGuiaRemision() {
		return plantillaGuiaRemision;
	}


	/**
	 * @param plantillaGuiaRemision the plantillaGuiaRemision to set
	 */
	public void setPlantillaGuiaRemision(String plantillaGuiaRemision) {
		this.plantillaGuiaRemision = plantillaGuiaRemision;
	}


	/**
	 * @return the plantillaLiqCompra
	 */
	public String getPlantillaLiqCompra() {
		return plantillaLiqCompra;
	}


	/**
	 * @param plantillaLiqCompra the plantillaLiqCompra to set
	 */
	public void setPlantillaLiqCompra(String plantillaLiqCompra) {
		this.plantillaLiqCompra = plantillaLiqCompra;
	}


	/**
	 * @return the plantillaFacturaPuntoVenta
	 */
	public String getPlantillaFacturaPuntoVenta() {
		return plantillaFacturaPuntoVenta;
	}


	/**
	 * @param plantillaFacturaPuntoVenta the plantillaFacturaPuntoVenta to set
	 */
	public void setPlantillaFacturaPuntoVenta(String plantillaFacturaPuntoVenta) {
		this.plantillaFacturaPuntoVenta = plantillaFacturaPuntoVenta;
	}
	
}
