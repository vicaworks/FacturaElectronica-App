/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoFactura extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoFactura() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException {
		return null;
	}

}
