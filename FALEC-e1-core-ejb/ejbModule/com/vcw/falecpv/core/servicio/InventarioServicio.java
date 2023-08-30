/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TemporalType;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
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
public class InventarioServicio {
	
	@Inject
	private KardexProductoDao kardexProductoDao;
	
	@Inject
	private ProductoDao productoDao;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getInventario(String idEstablecimiento)throws DaoException{
		return productoDao.getByEstado(EstadoRegistroEnum.ACTIVO, idEstablecimiento);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param tipoProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getInventario(String tipoProducto,String idEstablecimiento)throws DaoException{
		return productoDao.getByEstado(tipoProducto,EstadoRegistroEnum.ACTIVO, idEstablecimiento);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getStokMayorZero(String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento",idEstablecimiento)
				.equals("p.estado","A")
				.equals("p.tipoProducto.nombre","PRODUCTO")
				.greaterThan("p.stock", BigDecimal.ZERO)
				.orderBy("p.nombregenerico").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param valor
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getStokLessEqualsThan(BigDecimal valor,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento",idEstablecimiento)
				.equals("p.estado","A")
				.equals("p.tipoProducto.nombre","PRODUCTO")
				.lessEqualsThan("p.stock", valor)
				.orderBy("p.nombregenerico").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fecha
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getFechaCaducidadLessEqualsThan(Date fecha,String idEstablecimiento)throws DaoException{

		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			List<Producto> lista = q.select("p")
					.from(Producto.class,"p")
					.equals("p.establecimiento.idestablecimiento",idEstablecimiento)
					.equals("p.estado","A")
					.equals("p.tipoProducto.nombre","PRODUCTO")
					.greaterEqualsThan("p.stock", BigDecimal.ZERO)
					.in("p.idproducto", getProductoKardexFechaVencimientoLessEqualsThan(fecha, idEstablecimiento))
					.orderBy("p.nombregenerico").getResultList();
			
			lista.stream().forEach(x->{
				
				QueryBuilder q2 = new QueryBuilder(kardexProductoDao.getEntityManager());
				
				x.setFechavencimiento(
					(Date) q2.select("MAX(k.fechavencimiento)")
						.from(KardexProducto.class,"k")
						.equals("k.establecimiento.idestablecimiento", idEstablecimiento)
						.equals("k.producto.idproducto", x.getIdproducto())
						.lessEqualsThan("k.fechavencimiento", fecha, TemporalType.DATE).getSingleResult());
				
			});
			
			return lista;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fecha
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	private List<String> getProductoKardexFechaVencimientoLessEqualsThan(Date fecha,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(kardexProductoDao.getEntityManager());
			List<Producto> lista = q.select("k.producto")
									.from(KardexProducto.class,"k")
									.equals("k.establecimiento.idestablecimiento", idEstablecimiento)
									.lessEqualsThan("k.fechavencimiento", fecha, TemporalType.DATE)
									.orderBy("k.producto.nombregenerico").getResultList();
			
			if(lista.isEmpty()) {
				Producto p = new Producto();
				p.setIdproducto("-x");
				lista.add(p);
			}
			
			return lista.stream().map(x->x.getIdproducto()).collect(Collectors.toList());
			
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
	public List<Producto> getByIdProducto(String idProducto,String idEstablecimiento)throws DaoException{
		
		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.idproducto", idProducto).getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getByCodigoPrincipal(String codProducto,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.codigoprincipal", codProducto)
				.equals("p.estado","A")
				.orderBy("p.nombregenerico").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCategoria
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getByCategoria(String idCategoria,String idEstablecimiento)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.categoria.idcategoria", idCategoria)
				.equals("p.estado","A")
				.equals("p.tipoProducto.nombre","PRODUCTO")
				.orderBy("p.nombregenerico").getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idGrupoCategoria
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getByGrupoCategoria(String idGrupoCategoria,String idEstablecimiento)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			return q.select("p")
				.from(Producto.class,"p")	
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.categoria.grupocategoria.idgrupocategoria", idGrupoCategoria)
				.equals("p.estado","A")
				.equals("p.tipoProducto.nombre","PRODUCTO")
				.orderBy("p.nombregenerico").getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idFabricante
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<Producto> getByFabricante(String idFabricante,String idEstablecimiento)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			return q.select("p")
				.from(Producto.class,"p")
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.fabricante.idfabricante", idFabricante)
				.equals("p.estado","A")
				.equals("p.tipoProducto.nombre","PRODUCTO")
				.orderBy("p.nombregenerico").getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

}
