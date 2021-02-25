/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.SegperfilpredefinidoDao;
import com.vcw.falecpv.core.dao.impl.SegperfilpredefinidoperfilDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinidoperfil;
import com.vcw.falecpv.core.servicio.AppGenericService;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilpredefinidoServicio extends AppGenericService<Segperfilpredefinido, String> {

	@Inject
	private SegperfilpredefinidoDao segperfilpredefinidoDao;
	
	@Inject
	private SegperfilpredefinidoperfilDao segperfilpredefinidoperfilDao;
	
	/**
	 * 
	 */
	public SegperfilpredefinidoServicio() {
	}

	@Override
	public List<Segperfilpredefinido> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilpredefinido> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilpredefinido, String> getDao() {
		return segperfilpredefinidoDao;
	}

	/**
	 * @return the segperfilpredefinidoDao
	 */
	public SegperfilpredefinidoDao getSegperfilpredefinidoDao() {
		return segperfilpredefinidoDao;
	}

	/**
	 * @return the segperfilpredefinidoperfilDao
	 */
	public SegperfilpredefinidoperfilDao getSegperfilpredefinidoperfilDao() {
		return segperfilpredefinidoperfilDao;
	}
	
	/**
	 * @param idSegPerfilPredefinido
	 * @return
	 * @throws DaoException
	 */
	public List<Segperfil> getByPerfilDefinido(String idSegPerfilPredefinido)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(segperfilpredefinidoDao.getEntityManager());
			
			return qb.select("DISTINCT p.segperfil")
					.from(Segperfilpredefinidoperfil.class,"p")
					.equals("p.segperfilpredefinido.idsegperfilpredefinido", idSegPerfilPredefinido)
					.equals("p.estado","A")
					.equals("p.segperfil.estado","A")
					.equals("p.segperfilpredefinido.estado", "A").getResultList();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
