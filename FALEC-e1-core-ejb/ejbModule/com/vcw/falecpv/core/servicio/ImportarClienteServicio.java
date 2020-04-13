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
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TCCategoria;
import com.vcw.falecpv.core.constante.contadores.TCCliente;
import com.vcw.falecpv.core.constante.contadores.TCFabricante;
import com.vcw.falecpv.core.constante.contadores.TCProducto;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.dao.impl.ClienteDao;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.dao.impl.FabricanteDao;
import com.vcw.falecpv.core.dao.impl.IceDao;
import com.vcw.falecpv.core.dao.impl.IvaDao;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.dao.impl.TipoIdentificacionDao;
import com.vcw.falecpv.core.dao.impl.TipoProductoDao;
import com.vcw.falecpv.core.dao.impl.UsuarioDao;
import com.vcw.falecpv.core.modelo.dto.ImportClienteDto;
import com.vcw.falecpv.core.modelo.dto.ImportProductoDto;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;
import com.vcw.falecpv.core.util.EncoderUtil;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author Jorge Toaza
 *
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ImportarClienteServicio {

	@Inject
	private ClienteDao clienteDao;
	
	@Inject
	private TipoIdentificacionDao tipoIdentificacionDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * @author Jorge Toaza
	 * @param lista
	 * @param empresa
	 * @param idUsuario
	 * @throws DaoException
	 * @throws ParametroRequeridoException 
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ImportClienteDto> importarClienteFacade(List<ImportClienteDto> lista, Empresa empresa, String idUsuario)throws DaoException, ParametroRequeridoException {
		
		continuar1:for (ImportClienteDto c : lista) {
			
			// verifica si la fila tiene error
			if(c.isError()) continue continuar1;
			
			// verifica si ya existe la identificación
			if(existeIdentificacion(empresa, c.getIdentificacion())) {
				c.setError(true);
				c.setNovedad("IDENTIFICACÓN YA EXISTE.");
				continue continuar1;
			}
			
			Cliente cliente = new Cliente();
			// datos iniciales
			cliente.setEmpresa(empresa);
			cliente.setIdusuario(idUsuario);
			cliente.setUpdated(new Date());
			cliente.setEstado("A");
			
			// datos negocios
			TipoIdentificacion t = tipoIdentificacionDao.cargar(c.getIdTipoIdentificacion());
			cliente.setTipoIdentificacion(t);
			cliente.setIdentificacion(c.getIdentificacion());
			cliente.setRazonsocial(c.getRazonSocial());
			cliente.setDireccion(c.getDireccion());
			cliente.setCorreoelectronico(c.getCorreoElectronico());
			cliente.setTelefono(c.getTelefono());
			cliente.setNombregarante1(c.getNombreGarante1());
			cliente.setCedulagarante1(c.getCedulaGarante1());
			cliente.setDirecciongarante1(c.getDireccionGarante1());
			cliente.setTelefonogarante1(c.getTelefonoGarante1());
			cliente.setOcupaciongarante1(c.getOcupacionGarante1());
			cliente.setNombregarante2(c.getNombreGarante2());
			cliente.setCedulagarante2(c.getCedulaGarante2());
			cliente.setDirecciongarante2(c.getDireccionGarante2());
			cliente.setTelefonogarante2(c.getTelefonoGarante2());
			cliente.setOcupaciongarante2(c.getOcupacionGarante2());
			
			// crear el producto
			cliente.setIdcliente(contadorPkServicio.generarContadorTabla(TCCliente.CLIENTE, null));
			clienteDao.guardar(cliente);
			c.setIdCliente(cliente.getIdcliente());
			
		}
		
		return lista;
				
	}
	
	/**
	 * @author Jorge Toaza
	 * @param empresa
	 * @param identificacion
	 * @return
	 * @throws DaoException
	 */
	private boolean existeIdentificacion(Empresa empresa, String identificacion) throws DaoException {
		try {
			QueryBuilder q = new QueryBuilder(clienteDao.getEntityManager());
			
			if(q.select("c")
				.from(Cliente.class,"c")
				.equals("c.empresa.idempresa", empresa.getIdempresa())
				.equals("c.identificacion", identificacion).count()>0) return true;

			return false;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}