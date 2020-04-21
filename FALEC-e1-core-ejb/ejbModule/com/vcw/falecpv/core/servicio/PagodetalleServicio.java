/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCAdquisicion;
import com.vcw.falecpv.core.dao.impl.PagodetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Pagodetalle;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagodetalleServicio extends AppGenericService<Pagodetalle, String> {

	
	@Inject
	private PagodetalleDao pagodetalleDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@Override
	public List<Pagodetalle> consultarActivos() {
		return null;
	}

	@Override
	public List<Pagodetalle> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Pagodetalle, String> getDao() {
		return pagodetalleDao;
	}

	/**
	 * @return the pagodetalleDao
	 */
	public PagodetalleDao getPagodetalleDao() {
		return pagodetalleDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idAdquisicion
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByAdquisicion(String idAdquisicion)throws DaoException{
		try {
			
			String sql = "DELETE FROM pagodetalle WHERE idadquisicion='" + idAdquisicion + "'";
			
			return execute(sql);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idadquisicion
	 * @return
	 * @throws DaoException
	 */
	public List<Pagodetalle> getByAdquisicion(String idadquisicion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(pagodetalleDao.getEntityManager());
			
			return q.select("p")
				.from(Pagodetalle.class,"p")
				.equals("p.adquisicion.idadquisicion", idadquisicion)
				.orderBy("p.fecha").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param pagodetalle
	 * @return
	 * @throws DaoException
	 */
	public Pagodetalle guardarFacade(String idEstablecimiento,Pagodetalle pagodetalle)throws DaoException{
		try {
			
			if(pagodetalle.getIdpagodetalle()==null || pagodetalle.getIdpagodetalle().contains("MM")) {
				
				pagodetalle.setIdpagodetalle(
						contadorPkServicio.generarContadorTabla(TCAdquisicion.PAGODETALLE, idEstablecimiento));
				crear(pagodetalle);
				
			}else {
				actualizar(pagodetalle);
			}
			
			return pagodetalle;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
