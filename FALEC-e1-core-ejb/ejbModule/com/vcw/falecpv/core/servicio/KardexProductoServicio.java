/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.contadores.TCKardexproducto;
import com.vcw.falecpv.core.dao.impl.KardexProductoDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Singleton
@Startup
@Lock(LockType.READ)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class KardexProductoServicio extends AppGenericService<KardexProducto, String> {
	
	@Inject
	private KardexProductoDao kardexProductoDao;
	
	@Inject
	private ProductoDao productoDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;

	/**
	 * 
	 */
	public KardexProductoServicio() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param kardexProducto
	 * @return
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public KardexProducto registrarKardexFacade(KardexProducto kardexProducto)throws DaoException, ParametroRequeridoException{
		
		// consultar el producto
		Producto producto = productoDao.cargar(kardexProducto.getProducto().getIdproducto());
		
		kardexProducto.setIdkardexproducto(contadorPkServicio.generarContadorTabla(TCKardexproducto.KARDEXPRODUCTO,
				kardexProducto.getEstablecimiento().getIdestablecimiento()));
		
		if(producto==null) {
			throw new DaoException("PRODUCTO : " + kardexProducto.getProducto().getNombregenerico() + " NO EXISTE");
		}
		
		switch (kardexProducto.getTiporegistro()) {
		case "E":
			// agregar 
			kardexProducto.setSaldo(producto.getStock().add(kardexProducto.getCantidad()));
			break;
		case "S":
			kardexProducto.setSaldo(producto.getStock().add(kardexProducto.getCantidad().negate()));
			if(kardexProducto.getSaldo().doubleValue()<0.0d) {
				kardexProducto.setSaldo(BigDecimal.ZERO);
			}
			break;	
		default:
			throw new DaoException("NO EXISTE TIPO DE KARDEX [E] enrada, [S] salida.");
		}
		kardexProducto.setCostototal(kardexProducto.getCantidad().multiply(kardexProducto.getCostounitario()));
		producto.setStock(kardexProducto.getSaldo());
		kardexProducto.setOrden(getOrden(kardexProducto.getProducto().getIdproducto(), kardexProducto.getEstablecimiento().getIdestablecimiento())+1);
		kardexProductoDao.guardar(kardexProducto);
		return kardexProducto;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProducto
	 * @param idEstablecimeinto
	 * @return
	 * @throws DaoException
	 */
	private Integer getOrden(String idProducto,String idEstablecimeinto)throws DaoException{
		try {
			String sql = "SELECT coalesce (max(k.orden ),0) as orden FROM kardexproducto k WHERE k.idproducto='" + idProducto +"' AND k.idestablecimiento='" + idEstablecimeinto + "'";
			Map<String, Object> resultado = singleResultMap(sql);
			return (Integer)resultado.get("orden");
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProducto
	 * @param idEStablecimiento
	 * @param codigoPrincipal
	 * @return
	 * @throws DaoException
	 */
	@Lock(LockType.READ)
	public Producto getProducto(String idProducto,String idEStablecimiento,String codigoPrincipal)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			if(idProducto!=null) {
				
				return (Producto) q.select("p")
							.from(Producto.class,"p")
							.equals("p.idproducto",idProducto).getSingleResult();
				
			}
			
			if(codigoPrincipal!=null) {
				
				return (Producto) q.select("p")
						.from(Producto.class,"p")
						.equals("p.establecimiento.idestablecimiento",idEStablecimiento)
						.equals("p.codigoprincipal",codigoPrincipal).getSingleResult();
				
			}
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

	@Override
	public List<KardexProducto> consultarActivos() {
		return null;
	}

	@Override
	public List<KardexProducto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<KardexProducto, String> getDao() {
		return kardexProductoDao;
	}

	/**
	 * @return the kardexProductoDao
	 */
	public KardexProductoDao getKardexProductoDao() {
		return kardexProductoDao;
	}

}
