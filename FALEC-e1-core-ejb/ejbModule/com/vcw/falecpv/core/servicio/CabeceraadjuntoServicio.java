/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public Map<String, byte[]> getByCabecera(String idCabecera)throws DaoException{
		try {
			Map<String, byte[]> adjuntos = new HashMap<>();
			List<Cabeceraadjunto> cabeceraadjuntoList = cabeceraadjuntoDao.getByCabecera(idCabecera);
			for (Cabeceraadjunto cabeceraadjunto : cabeceraadjuntoList) {
				adjuntos.put(cabeceraadjunto.getNombreadjunto(), cabeceraadjunto.getStream());
			}
			return adjuntos;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param idEstablecimiento
	 * @param adjuntos
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	public void guardarAdjuntos(String idCabecera,String idEstablecimiento ,Map<String, byte[]> adjuntos)throws DaoException, ParametroRequeridoException{
		try {
			List<Cabeceraadjunto> cabeceraadjuntoList = new ArrayList<>();
			for (String key : adjuntos.keySet()) {
				Cabeceraadjunto cabeceraadjunto = new Cabeceraadjunto();
				cabeceraadjunto.setNombreadjunto(key);
				cabeceraadjunto.setStream(adjuntos.get(key));
				cabeceraadjuntoList.add(cabeceraadjunto);
			}
			guardarAdjuntos(idCabecera, idEstablecimiento, cabeceraadjuntoList);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
