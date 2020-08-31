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
import com.vcw.falecpv.core.constante.contadores.TCCliente;
import com.vcw.falecpv.core.dao.impl.ClienteDao;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author Jorge Toaza
 *
 */
@Stateless
public class ClienteServicio extends AppGenericService<Cliente, String> {

	@Inject
	private ClienteDao clienteDao;

	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public ClienteServicio() {
	}

	@Override
	public List<Cliente> consultarActivos() {
		return null;
	}
	
	@Override
	public List<Cliente> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Cliente, String> getDao() {
		return clienteDao;
	}

	/**
	 * @return the clienteDao
	 */
	public ClienteDao getClienteDao() {
		return clienteDao;
	}
	
	/**
	 * @author Jorge Toaza
	 * @param cliente
	 * @return
	 * @throws DaoException
	 */
	public Cliente guardar(Cliente cliente)throws DaoException{
		try {
			
			if(cliente.getIdcliente()==null) {
				
				cliente.setIdcliente(contadorPkServicio.generarContadorTabla(TCCliente.CLIENTE, null));
				crear(cliente);
				
			} else {
				
				actualizar(cliente);
				
			}
			
			return cliente;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<TipoIdentificacion> getByProveedor()throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(clienteDao.getEntityManager());
			return q.select("t")
					.from(TipoIdentificacion.class,"t")
					.equals("t.proveedor","S")
					.orderBy("t.tipoidentificacion").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
