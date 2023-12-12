/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraadjuntoDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Cabeceraadjunto;

/**
 * 
 */
@Stateless
public class CabeceraadjuntoServicio extends AppGenericService<Cabeceraadjunto, String> {

	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	@Inject
	private CabeceraadjuntoDao cabeceraadjuntoDao;
	
	/**
	 * 
	 */
	public CabeceraadjuntoServicio() {
	}

	@Override
	public List<Cabeceraadjunto> consultarActivos() {
		return null;
	}

	@Override
	public List<Cabeceraadjunto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Cabeceraadjunto, String> getDao() {
		return cabeceraadjuntoDao;
	}

	/**
	 * @return the cabeceraadjuntoDao
	 */
	public CabeceraadjuntoDao getCabeceraadjuntoDao() {
		return cabeceraadjuntoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param cabeceraadjuntoList
	 * @throws DaoException
	 * @throws ParametroRequeridoException 
	 */
	public void guardarAdjuntos(String idCabecera,String idEstablecimiento ,List<Cabeceraadjunto> cabeceraadjuntoList)throws DaoException, ParametroRequeridoException{
		cabeceraadjuntoDao.eliminarByCabecera(idCabecera);
		Cabecera c = new Cabecera();
		c.setIdcabecera(idCabecera);
		for (Cabeceraadjunto cabeceraadjunto : cabeceraadjuntoList) {
			cabeceraadjunto.setCabecera(c);
			cabeceraadjunto.setIdcabeceraadjunto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.CABECERA_AJUNTO, idEstablecimiento));
			cabeceraadjuntoDao.guardar(cabeceraadjunto);
		}
	}

}
