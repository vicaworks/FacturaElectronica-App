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
import com.vcw.falecpv.core.dao.impl.EtiquetaDao;
import com.vcw.falecpv.core.modelo.persistencia.Etiqueta;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EtiquetaServicio extends AppGenericService<Etiqueta, String> {

	@Inject
	private EtiquetaDao tareaetiquetaDao;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public EtiquetaServicio() {
	}

	@Override
	public List<Etiqueta> consultarActivos() {
		return null;
	}

	@Override
	public List<Etiqueta> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Etiqueta, String> getDao() {
		return tareaetiquetaDao;
	}

	/**
	 * @return the tareaetiquetaDao
	 */
	public EtiquetaDao getTareaetiquetaDao() {
		return tareaetiquetaDao;
	}

	@Override
	public Etiqueta crear(Etiqueta o) throws DaoException {
		try {
			
			o.setIdetiqueta(contadorPkServicio.generarContadorTabla(TCEmpresa.TAREA_ETIQUETA, o.getIdEstablecimiento(), true));
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
	public List<Etiqueta> getEtiquetas(String idEmpresa,String modulo)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(tareaetiquetaDao.getEntityManager());
			
			return	qb.select("e")
					.from(Etiqueta.class,"e")
					.equals("e.estado","A")
					.equals("e.empresa.idempresa",idEmpresa)
					.equals("e.modulo",modulo)
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
					.from(Etiqueta.class,"e")
					.equals("e.estado","A")
					.equals("e.empresa.idempresa",idEmpresa)
					.equals("e.etiqueta",etiqueta.toUpperCase())
					.notEquals("e.idetiqueta", idtareaetiqueta==null?"-1":idtareaetiqueta)
					.orderBy("e.etiqueta").getResultList().size()>0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
