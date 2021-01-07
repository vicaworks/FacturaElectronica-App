/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.util.HashMap;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.contadores.TCSri;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.modelo.persistencia.Logtransferenciasri;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.LogtransferenciasriServicio;

/**
 * @author cristianvillarreal
 *
 */
public class EnviarComprobanteSRIDecorador implements EnviarComprobanteSRI {

	private EnviarComprobanteSRI enviarComprobanteSRI;
	protected LogtransferenciasriServicio logtransferenciasriServicio;
	protected ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public EnviarComprobanteSRIDecorador(EnviarComprobanteSRI enviarComprobanteSRI) {
		this.enviarComprobanteSRI=enviarComprobanteSRI;
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		enviarComprobanteSRI.enviarComprobante(null);
		return null;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param logtransferenciasri
	 * @param idEstablecimiento
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	protected void registrarlog(Logtransferenciasri logtransferenciasri,String idEstablecimiento) throws DaoException, ParametroRequeridoException {
		logtransferenciasri.setIdlogtransferenciasri(contadorPkServicio.generarContadorTabla(TCSri.LOGTRANSFERENCIASRI, idEstablecimiento));
		logtransferenciasriServicio.crear(logtransferenciasri);
	}

}
