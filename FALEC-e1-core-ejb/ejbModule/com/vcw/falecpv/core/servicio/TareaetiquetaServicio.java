/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;
import com.vcw.falecpv.core.dao.impl.TareaetiquetaDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareaetiqueta;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareaetiquetaServicio extends AppGenericService<Tareaetiqueta, String> {

	@Inject
	private TareaetiquetaDao tareaetiquetaDao;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public TareaetiquetaServicio() {
	}

	@Override
	public List<Tareaetiqueta> consultarActivos() {
		return null;
	}

	@Override
	public List<Tareaetiqueta> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tareaetiqueta, String> getDao() {
		return tareaetiquetaDao;
	}

	/**
	 * @return the tareaetiquetaDao
	 */
	public TareaetiquetaDao getTareaetiquetaDao() {
		return tareaetiquetaDao;
	}

	@Override
	public Tareaetiqueta crear(Tareaetiqueta o) throws DaoException {
		try {
			
			o.setIdtareaetiqueta(contadorPkServicio.generarContadorTabla(TCEmpresa.TAREA_ETIQUETA, o.getIdEstablecimiento(), true));
			return super.crear(o);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<Tareaetiqueta> getEtiquetas(String idEmpresa)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(tareaetiquetaDao.getEntityManager());
			
			return	qb.select("e")
					.from(Tareaetiqueta.class,"e")
					.equals("e.estado","A")
					.equals("e.empresa.idempresa",idEmpresa)
					.orderBy("e.etiqueta").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idtareaetiqueta
	 * @return
	 * @throws DaoException
	 */
	public boolean existeEtiquetas(String idEmpresa,String idtareaetiqueta,String etiqueta)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(tareaetiquetaDao.getEntityManager());
			
			return	qb.select("e")
					.from(Tareaetiqueta.class,"e")
					.equals("e.estado","A")
					.equals("e.empresa.idempresa",idEmpresa)
					.equals("e.etiqueta",etiqueta.toUpperCase())
					.notEquals("e.idtareaetiqueta", idtareaetiqueta==null?"-1":idtareaetiqueta)
					.orderBy("e.etiqueta").getResultList().size()>0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
