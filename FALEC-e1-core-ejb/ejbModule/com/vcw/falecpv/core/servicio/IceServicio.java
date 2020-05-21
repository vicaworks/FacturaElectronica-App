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
import com.vcw.falecpv.core.constante.contadores.TCIce;
import com.vcw.falecpv.core.dao.impl.IceDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IceServicio extends AppGenericService<Ice, String> {

	@Inject
	private IceDao iceDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@Inject
	private ProductoDao productoDao;
	
	public static final String codigoIce="3";
	
	/**
	 * 
	 */
	public IceServicio() {
	}

	@Override
	public List<Ice> consultarActivos() {
		return null;
	}

	@Override
	public List<Ice> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Ice, String> getDao() {
		return iceDao;
	}

	/**
	 * @return the iceDao
	 */
	public IceDao getIceDao() {
		return iceDao;
	}

	public void setIceDao(IceDao iceDao) {
		this.iceDao = iceDao;
	}
	

	/**
	 * @author Isabel Lobato
	 * @param establecimiento
	 * @return
	 * @throws DaoException
	 */
	public Ice guardar(Ice ice)throws DaoException{
		try {
			ice.setCodigoIce(codigoIce);
			if (ice.getIdice()==null) {
				ice.setIdice(contadorPkServicio.generarContadorTabla(TCIce.ICE, null));
				
				crear(ice);
			}else {
					
				actualizar(ice);
			}
			return ice;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * @author Isabel Lobato
	 * @param idice
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependenciasIce(String idice)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());

			if (q.select("p").from(Producto.class, "p")
					.equals("p.ice.idice", idice).count() > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	
}
