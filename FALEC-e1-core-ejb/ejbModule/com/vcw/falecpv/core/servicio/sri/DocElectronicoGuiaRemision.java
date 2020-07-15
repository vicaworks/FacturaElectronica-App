/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoGuiaRemision extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoGuiaRemision() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		return null;
	}

}
