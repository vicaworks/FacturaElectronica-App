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
import com.vcw.falecpv.core.constante.contadores.TCProducto;
import com.vcw.falecpv.core.dao.impl.KardexProductoDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProductoServicio extends AppGenericService<Producto, String> {

	@Inject
	private ProductoDao productoDao;
	
	@Inject
	private KardexProductoDao kardexProductoDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public ProductoServicio() {
	}

	@Override
	public List<Producto> consultarActivos() {
		return null;
	}

	@Override
	public List<Producto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Producto, String> getDao() {
		return productoDao;
	}

	/**
	 * @return the productoDao
	 */
	public ProductoDao getProductoDao() {
		return productoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param producto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Producto guardar(Producto producto)throws DaoException{
		try {
			
			if(producto.getIdproducto()==null) {
				
				producto.setIdproducto(contadorPkServicio.generarContadorTabla(TCProducto.PRODUCTO,
						producto.getEstablecimiento().getIdestablecimiento()));
				if(producto.getCodigoprincipal()==null || producto.getCodigoprincipal().trim().length()==0) {
					producto.setCodigoprincipal(producto.getIdproducto());
				}
				crear(producto);
				
			}else {
				
				actualizar(producto);
				
			}
			
			return producto;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneReferencias(String idProducto,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(kardexProductoDao.getEntityManager());
			if(q.select("k")
					.from(KardexProducto.class,"k")
					.equals("k.establecimiento.idestablecimiento", idEstablecimiento)
					.equals("k.producto.idproducto",idProducto).count()>0) {
				return true;
			}
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
