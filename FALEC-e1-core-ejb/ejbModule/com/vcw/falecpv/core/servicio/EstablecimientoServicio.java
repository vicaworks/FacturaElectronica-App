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
import com.vcw.falecpv.core.constante.contadores.TCEstablecimiento;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.xpert.persistence.query.QueryBuilder;


/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstablecimientoServicio extends AppGenericService<Establecimiento, String> {
	
	@Inject
	private EstablecimientoDao establecimientoDao;
	
	@Inject
	private CategoriaDao categoriaDao;

	public void setEstablecimientoDao(EstablecimientoDao establecimientoDao) {
		this.establecimientoDao = establecimientoDao;
	}

	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public EstablecimientoServicio() {
	}

	@Override
	public List<Establecimiento> consultarActivos() {
		return null;
	}

	@Override
	public List<Establecimiento> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Establecimiento, String> getDao() {
		return establecimientoDao;
	}

	/**
	 * @return the establecimientoDao
	 */
	public EstablecimientoDao getEstablecimientoDao() {
		return establecimientoDao;
	}
	
	/**
	 * @author Isabel Lobato
	 * 
	 * @param establecimiento
	 * @return
	 * @throws DaoException
	 */
	public Establecimiento guardar(Establecimiento establecimiento)throws DaoException{
		try {
			
			if (establecimiento.getIdestablecimiento()==null) {
				establecimiento.setIdestablecimiento(contadorPkServicio.generarContadorTabla(TCEstablecimiento.ESTABLECIMIENTO, null));
				
				crear(establecimiento);
			}else {
					
				actualizar(establecimiento);
			}
			return establecimiento;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author Isabel Lobato
	 * @param idestablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependenciasEst(String idestablecimiento)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());

			if (q.select("e").from(Categoria.class, "e")
					.equals("e.establecimiento.idestablecimiento", idestablecimiento).count() > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
