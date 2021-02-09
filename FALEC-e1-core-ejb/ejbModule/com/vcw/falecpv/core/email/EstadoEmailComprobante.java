/**
 * 
 */
package com.vcw.falecpv.core.email;

import javax.ejb.EJB;

import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;
import com.vcw.falecpv.core.servicio.CabeceraServicio;

/**
 * @author cristianvillarreal
 *
 */
public class EstadoEmailComprobante implements ActualizaNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4581787165364347389L;
	
	
	private String idCabecera;
	
	@EJB
	private CabeceraServicio cabeceraServicio;

	/**
	 * 
	 */
	public EstadoEmailComprobante() {
	}

	@Override
	public void cambiarEstado(EstadoEnvioEmailEnum estado) {
		try {
			
			if (estado.equals(EstadoEnvioEmailEnum.ENVIADO)) {
				cabeceraServicio.getCabeceraDao().setEstadoEmailComprobante(idCabecera, 1);
			}else {
				cabeceraServicio.getCabeceraDao().setEstadoEmailComprobante(idCabecera, 0);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the idCabecera
	 */
	public String getIdCabecera() {
		return idCabecera;
	}

	/**
	 * @param idCabecera the idCabecera to set
	 */
	public void setIdCabecera(String idCabecera) {
		this.idCabecera = idCabecera;
	}

}
