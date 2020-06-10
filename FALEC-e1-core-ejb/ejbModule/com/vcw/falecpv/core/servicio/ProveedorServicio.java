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
import com.vcw.falecpv.core.constante.contadores.TCProveedor;
import com.vcw.falecpv.core.dao.impl.ProveedorDao;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProveedorServicio extends AppGenericService<Proveedor, String> {
	
	@Inject
	private ProveedorDao proveedorDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	
	/**
	 * 
	 */
	public ProveedorServicio() {
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<TipoIdentificacion> getByProveedor()throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(proveedorDao.getEntityManager());
			return q.select("t")
					.from(TipoIdentificacion.class,"t")
					.equals("t.proveedor","S")
					.orderBy("t.tipoidentificacion").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipo
	 * @param valor
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCampo(String id,String idEmpresa,String tipo,String valor)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(proveedorDao.getEntityManager());
			
					
			switch (tipo) {
			case "RS":
				return q.select("p")
					.from(Proveedor.class,"p")
					.equals("p.razonsocial",valor)
					.notEquals("p.idproveedor", id==null?"-":id).count()>0;
			case "NB":
				return q.select("p")
						.from(Proveedor.class,"p")
						.equals("p.nombrecomercial",valor)
						.notEquals("p.idproveedor", id==null?"-":id).count()>0;
			case "ID":
				
				return q.select("p")
						.from(Proveedor.class,"p")
						.equals("p.identificacion",valor)
						.notEquals("p.idproveedor", id==null?"-":id).count()>0;
			default:
				break;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Proveedor> consultarActivos() {
		return null;
	}

	@Override
	public List<Proveedor> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Proveedor, String> getDao() {
		return proveedorDao;
	}

	/**
	 * @return the proveedorDao
	 */
	public ProveedorDao getProveedorDao() {
		return proveedorDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param proveedor
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Proveedor guardar(Proveedor proveedor,String idEstablecimiento)throws DaoException{
		try {
			
			if(proveedor.getIdproveedor()==null) {
				proveedor.setIdproveedor(contadorPkServicio.generarContadorTabla(TCProveedor.PROVEEDOR, idEstablecimiento));
				crear(proveedor);
			}else {
				actualizar(proveedor);
			}
			
			return proveedor;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
