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
import com.vcw.falecpv.core.constante.contadores.TCIva;
import com.vcw.falecpv.core.dao.impl.IvaDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IvaServicio extends AppGenericService<Iva, String> {

	

	@Inject
	private IvaDao ivaDao;
	
	@Inject
	private ProductoDao productoDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	public static final String codigoIva="2";
	/**
	 * 
	 */
	public IvaServicio() {
	}

	@Override
	public List<Iva> consultarActivos() {
		return null;
	}

	@Override
	public List<Iva> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Iva, String> getDao() {
		return ivaDao;
	}

	/**
	 * @return the ivaDao
	 */
	public IvaDao getIvaDao() {
		return ivaDao;
	}
	
	public void setIvaDao(IvaDao ivaDao) {
		this.ivaDao = ivaDao;
	}

	/**
	 * @author Isabel Lobato
	 * @param idiva
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependenciasIva(String idiva)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());

			if (q.select("p").from(Producto.class, "p")
					.equals("p.iva.idiva", idiva).count() > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * @author Isabel Lobato
	 * @param establecimiento
	 * @return
	 * @throws DaoException
	 */
	public Iva guardar(Iva iva)throws DaoException{
		try {
			iva.setCodigoIva(codigoIva);
			if (iva.getIdiva()==null) {
				iva.setIdiva(contadorPkServicio.generarContadorTabla(TCIva.IVA, null));
				
				crear(iva);
			}else {
					
				actualizar(iva);
			}
			return iva;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	
}
