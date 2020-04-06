/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TCCategoria;
import com.vcw.falecpv.core.constante.contadores.TCFabricante;
import com.vcw.falecpv.core.constante.contadores.TCProducto;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.dao.impl.FabricanteDao;
import com.vcw.falecpv.core.dao.impl.IceDao;
import com.vcw.falecpv.core.dao.impl.IvaDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.dao.impl.TipoProductoDao;
import com.vcw.falecpv.core.dao.impl.UsuarioDao;
import com.vcw.falecpv.core.modelo.dto.ImportProductoDto;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;
import com.vcw.falecpv.core.util.EncoderUtil;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ImportarProductoServicio {

	@Inject
	private ProductoDao productoDao;
	
	@Inject
	private FabricanteDao fabricanteDao;
	
	@Inject
	private CategoriaDao categoriaDao;
	
	@Inject
	private TipoProductoDao tipoProductoDao;
	
	@Inject
	private EstablecimientoDao establecimientoDao;
	
	@Inject
	private IvaDao ivaDao;
	
	@Inject
	private IceDao iceDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param lista
	 * @throws DaoException
	 * @throws ParametroRequeridoException 
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ImportProductoDto> importarProductoFacade(List<ImportProductoDto> lista,String idEstablecimiento,String idUsuario)throws DaoException, ParametroRequeridoException{
		
		Establecimiento e = establecimientoDao.cargar(idEstablecimiento);
		establecimientoDao.getEntityManager().detach(e);
		
		continuar1:for (ImportProductoDto p : lista) {
			
			// verifica si la fila tienen error
			if(p.isError()) continue continuar1;
			
			// verifica si ya existe el producto
			if(existeProducto(idEstablecimiento, p.getNombre(), p.getNombreComercial())) {
				p.setError(true);
				p.setNovedad("PRODUCTO YA EXISTE.");
				continue continuar1;
			}
			
			Producto producto = new Producto();
			// datos iniciales
			producto.setEstablecimiento(e);
			producto.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			producto.setIdusuario(idUsuario);
			producto.setUpdated(new Date());
			
			// datos negocios 
			producto.setStock(BigDecimal.ZERO);
			producto.setCategoria(categoria(p,e,idUsuario));
			producto.setFabricante(fabricante(p,e,idUsuario));
			producto.setTipoProducto(tipoProducto(p));
			producto.setNombre(p.getNombre());
			producto.setNombregenerico(p.getNombreComercial());
			producto.setDescripcion(p.getDescripcion());
			producto.setPreciounitario(p.getPrecioUnitario());
			producto.setIva(iva(p,e));
			if(producto.getIva()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?" , NO EXISTE EL VALOR DEL IVA EN EL CATALOGO DE CONFIGURACION":"NO EXISTE EL VALOR DEL IVA EN EL CATALOGO DE CONFIGURACION");
				continue continuar1;
			}
			producto.setIce(ice(p,e));
			if(producto.getIce()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?" , NO EXISTE EL VALOR DEL ICE EN EL CATALOGO DE CONFIGURACION":"NO EXISTE EL VALOR DEL ICE EN EL CATALOGO DE CONFIGURACION");
				continue continuar1;
			}
			producto.setPreciouno(p.getPrecio1());
			producto.setPreciodos(p.getPrecio2());
			producto.setPreciotres(p.getPrecio3());
			producto.setPorcentajedescuento(p.getDescuento());
			producto.setUnidadesporcajaofrasco(p.getUnidadesCaja().intValue());
			producto.setUnidadesporpaquete(p.getUnidadesEmbase().intValue());
			producto.setMedida(p.getMedida());
			producto.setConversionmedida(p.getConversionMedida());
			producto.setFechafabricacion(p.getFechaFabricacion());
			producto.setNombreimagen(p.getNombreImagen());
			if(p.getImagen()!=null && p.getImagen().trim().length()>0) {
				try {
					producto.setImagen(EncoderUtil.decodeHexString(p.getImagen()));
				} catch (Exception e2) {
					p.setError(true);
					p.setNovedad(p.getNovedad()!=null?" , FORMATO DE LA IMGEN ERRONEO (HEX)":"FORMATO DE LA IMGEN ERRONEO (HEX)");
					continue continuar1;
				}
			}
			
			// crear el producto
			producto.setIdproducto(contadorPkServicio.generarContadorTabla(TCProducto.PRODUCTO, idEstablecimiento));
			producto.setCodigoprincipal(p.getCodigoPrincipal()==null?producto.getIdproducto():p.getCodigoPrincipal());
			productoDao.guardar(producto);
			p.setIdProducto(producto.getIdproducto());
			
		}
		
		return lista;
				
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param nombre
	 * @param nombreComercial
	 * @return
	 * @throws DaoException
	 */
	private boolean existeProducto(String idEstablecimiento,String nombre, String nombreComercial)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(productoDao.getEntityManager());
			
			if(q.select("p")
				.from(Producto.class,"p")
				.equals("p.establecimiento.idestablecimiento",idEstablecimiento)
				.equals("p.nombre", nombre.toUpperCase()).count()>0) return true;
			
			if(q.select("p")
					.from(Producto.class,"p")
					.equals("p.establecimiento.idestablecimiento",idEstablecimiento)
					.equals("p.nombregenerico", nombreComercial.toUpperCase()).count()>0) return true;
			
			return false;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param p
	 * @param establecimiento
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	private Categoria categoria(ImportProductoDto p,Establecimiento establecimiento,String idUsuario)throws DaoException {
		try {
			
			if(p.getIdCategoria()!=null) {
				Categoria c = categoriaDao.cargar(p.getIdCategoria());
				if(c!=null) {
					return c;
				}
			}
			// busca si existe por nombre
			
			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());
			
			List<Categoria> lista = q.select("c")
				.from(Categoria.class,"c")
				.equals("c.categoria", p.getCategoria().toUpperCase()).getResultList();
			if(lista.size()>0) {
				return lista.get(0);
			}
			
			
			// crear la categoria 
			Categoria categoria = new Categoria();
			categoria.setEstablecimiento(establecimiento);
			categoria.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			categoria.setIdusuario(idUsuario);
			categoria.setUpdated(new Date());
			categoria.setCategoria(p.getCategoria().toUpperCase());
			categoria.setDescripcion(p.getCategoria().toUpperCase());
			categoria.setIdcategoria(contadorPkServicio.generarContadorTabla(TCCategoria.CATEGORIA, establecimiento.getIdestablecimiento()));
			categoriaDao.guardar(categoria);
			
			return categoria;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param p
	 * @param establecimiento
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	private Fabricante fabricante(ImportProductoDto p,Establecimiento establecimiento,String idUsuario)throws DaoException{
		try {
			
			if(p.getIdFabricante()!=null) {
				Fabricante f = fabricanteDao.cargar(p.getIdFabricante());
				if (f!=null) return f;
			}
			
			// busca por nombre
			QueryBuilder q = new QueryBuilder(fabricanteDao.getEntityManager());
			List<Fabricante> lista = q.select("f")
				.from(Fabricante.class,"f")
				.equals("f.nombrecomercial", p.getFabricante().toUpperCase()).getResultList();
			if(lista.size()>0) {
				return lista.get(0);
			}
			
			// crear el fabricante
			Fabricante fabricante = new Fabricante();
			fabricante.setIdfabricante(contadorPkServicio.generarContadorTabla(TCFabricante.FABRICANTE, establecimiento.getIdestablecimiento()));
			fabricante.setEstablecimiento(establecimiento);
			fabricante.setIdusuario(idUsuario);
			fabricante.setUpdated(new Date());
			fabricante.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			fabricante.setNombrecomercial(p.getFabricante().toUpperCase());
			fabricanteDao.guardar(fabricante);
			
			return fabricante;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param p
	 * @param establecimiento
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	private TipoProducto tipoProducto(ImportProductoDto p)throws DaoException{
		
		try {
			
			QueryBuilder q = new QueryBuilder(tipoProductoDao.getEntityManager());
			
			List<TipoProducto> lista = q.select("t")
				.from(TipoProducto.class,"t")
				.equals("t.nombre",p.getIdTipoProducto().toUpperCase()).getResultList();
			if(lista.size()>0) return lista.get(0);
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param p
	 * @return
	 * @throws DaoException
	 */
	private Iva iva(ImportProductoDto p,Establecimiento establecimiento)throws DaoException{
		
		try {
			
			QueryBuilder q = new QueryBuilder(ivaDao.getEntityManager());
			return (Iva) q.select("i")
				.from(Iva.class,"i")
				.equals("i.empresa.idempresa", establecimiento.getEmpresa().getIdempresa())
				.equals("i.valor", p.getIva()).getSingleResult();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param p
	 * @return
	 * @throws DaoException
	 */
	private Ice ice(ImportProductoDto p,Establecimiento establecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(iceDao.getEntityManager());
			return (Ice) q.select("i")
				.from(Ice.class,"i")
				.equals("i.empresa.idempresa", establecimiento.getEmpresa().getIdempresa())
				.equals("i.valor", p.getIce()).getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
}
