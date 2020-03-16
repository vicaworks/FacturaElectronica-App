/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CategoriaServicio extends AppGenericService<Categoria, String> {

	@Inject
	private CategoriaDao categoriaDao;
	
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

}
