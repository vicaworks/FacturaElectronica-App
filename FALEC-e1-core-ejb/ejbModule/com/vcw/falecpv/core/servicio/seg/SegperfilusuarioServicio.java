/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.SegperfilusuarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilusuario;
import com.vcw.falecpv.core.servicio.AppGenericService;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilusuarioServicio extends AppGenericService<Segperfilusuario, String> {

	@Inject
	private SegperfilusuarioDao segperfilusuarioDao;
	
	
	/**
	 * 
	 */
	public SegperfilusuarioServicio() {
	}

	@Override
	public List<Segperfilusuario> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilusuario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilusuario, String> getDao() {
		return segperfilusuarioDao;
	}

	/**
	 * @return the segperfilusuarioDao
	 */
	public SegperfilusuarioDao getSegperfilusuarioDao() {
		return segperfilusuarioDao;
	}
	
	public List<Segperfil> getPerfilByUsuario(String idUsuario)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(segperfilusuarioDao.getEntityManager());
			
			return qb.select("p.segperfil")
						.from(Segperfilusuario.class,"p")
						.equals("p.usuario.idusuario", idUsuario)
						.equals("p.estado","A")
						.equals("p.segperfil.estado","A").getResultList();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
