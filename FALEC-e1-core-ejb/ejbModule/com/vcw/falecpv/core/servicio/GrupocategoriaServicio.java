/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCCategoria;
import com.vcw.falecpv.core.dao.impl.GrupocategoriaDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Grupocategoria;
import com.xpert.persistence.query.QueryBuilder;

/**
 * 
 */
@Stateless
public class GrupocategoriaServicio extends AppGenericService<Grupocategoria, String> {

	@Inject
	private GrupocategoriaDao grupocategoriaDao;
	@Inject
	private ContadorPkServicio contadorPkServicio;

	/**
	 * @return the grupocategoriaDao
	 */
	public GrupocategoriaDao getGrupocategoriaDao() {
		return grupocategoriaDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idgrupocategoria
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependencias(String idgrupocategoria,String idEmpresa)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(grupocategoriaDao.getEntityManager());
			
			if(q.select("c")
					.from(Categoria.class,"c")
					.equals("c.empresa.idempresa", idEmpresa)
					.equals("c.grupocategoria.idgrupocategoria",idgrupocategoria).count()>0) {
				
				return true;
				
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param grupocategoria
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Grupocategoria guardar(Grupocategoria grupocategoria,String idEstablecimiento)throws DaoException{
		try {
			grupocategoria.setUpdated(new Date());
			if(grupocategoria.getIdgrupocategoria() == null) {
				grupocategoria.setIdgrupocategoria(contadorPkServicio.generarContadorTabla(TCCategoria.CATEGORIA, idEstablecimiento));
				crear(grupocategoria);
			}else {
				actualizar(grupocategoria);
			}
			
			return grupocategoria;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Grupocategoria> consultarActivos() {
		return null;
	}

	@Override
	public List<Grupocategoria> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Grupocategoria, String> getDao() {
		return grupocategoriaDao;
	}

}
