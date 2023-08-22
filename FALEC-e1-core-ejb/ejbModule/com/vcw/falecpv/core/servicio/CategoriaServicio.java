/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCCategoria;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CategoriaServicio extends AppGenericService<Categoria, String> {

	@Inject
	private CategoriaDao categoriaDao;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public CategoriaServicio() {
	}

	@Override
	public List<Categoria> consultarActivos() {
		return null;
	}

	@Override
	public List<Categoria> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Categoria, String> getDao() {
		return categoriaDao;
	}

	/**
	 * @return the categoriaDao
	 */
	public CategoriaDao getCategoriaDao() {
		return categoriaDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idcategoria
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependencias(String idcategoria,String idEmpresa)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());
			
			if(q.select("p")
					.from(Producto.class,"p")
					.equals("p.establecimiento.empresa.idempresa", idEmpresa)
					.equals("p.categoria.idcategoria",idcategoria).count()>0) {
				
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
	 * @param categoria
	 * @return
	 * @throws DaoException
	 */
	public Categoria guardar(Categoria categoria,String idEstablecimiento)throws DaoException{
		try {
			
			if(categoria.getIdcategoria()==null) {
				categoria.setIdcategoria(contadorPkServicio.generarContadorTabla(TCCategoria.CATEGORIA, idEstablecimiento));
				crear(categoria);
			}else {
				actualizar(categoria);
			}
			
			return categoria;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idgrupocategoria
	 * @return
	 * @throws DaoException
	 */
	public List<Categoria> getByGrupoCategoria(String idgrupocategoria)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());
			
			return q.select("c")
				.from(Categoria.class,"c")
				.equals("c.grupocategoria.idgrupocategoria",idgrupocategoria).getResultList();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
