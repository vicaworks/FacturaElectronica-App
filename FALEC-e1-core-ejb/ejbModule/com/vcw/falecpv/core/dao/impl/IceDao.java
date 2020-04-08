/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TCIce;
import com.vcw.falecpv.core.constante.contadores.TCProducto;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IceDao extends AppGenericDao<Ice, String> {

	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * @param type
	 */
	public IceDao() {
		super(Ice.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Ice> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			Query q = getEntityManager().createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa ORDER BY i.descripcion");
			q.setParameter("idempresa", idEmpresa);
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

	/**
	 * @author Isabel Lobato
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Ice> getAllIce(String idEmpresa)throws DaoException{
		try {
			List<Ice> lista = new ArrayList<>();
			Query q = getEntityManager()
					.createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa ORDER BY i.codigo");
			q.setParameter("idempresa", idEmpresa);

			lista = q.getResultList();

			return lista;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author Isabel Lobato
	 * @param codigo
	 * @param idice
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCodigo(String codigo, String idice)throws DaoException{
		try {
			
			Query q = null;
			
			if(idice!=null) {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.codigo=:codigo AND i.idice<>:idice");
				q.setParameter("codigo", codigo);
				q.setParameter("idice", idice);
				
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.codigo=:codigo ");
				q.setParameter("codigo", codigo);
			}
		
			if(q.getResultList().size()>0) {
				return true;
				}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author Isabel Lobato
	 * 
	 * @param lista
	 * @param empresa
	 * @param idusuario
	 * @throws DaoException
	 * @throws ParametroRequeridoException 
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Ice> importarProductoFacade(List<Ice> lista, Empresa empresa, String idusuario)throws DaoException, ParametroRequeridoException{
		
		continuar1:for (Ice p : lista) {
			
			// verifica si la fila tienen error
			if (p.isError())
				continue continuar1;

			// verifica si ya existe el ice
			if (existeCodigo(p.getCodigo(), p.getIdice())) {
				p.setError(true);
				p.setNovedad("ICE YA EXISTE.");
				continue continuar1;
			}

			Ice ice = new Ice();

			// datos iniciales
			ice.setCodigo(p.getCodigo());
			ice.setDescripcion(p.getDescripcion());
			ice.setEmpresa(empresa);
			ice.setTarifaadvalorem(p.getTarifaadvalorem()!=null?((new BigDecimal(p.getTarifaadvalorem())).multiply(new BigDecimal(100)))+"%":"");
			ice.setTarifaespecifica(p.getTarifaespecifica());
			ice.setValor(p.getTarifaadvalorem()!=null?new BigDecimal(p.getTarifaadvalorem()):BigDecimal.ZERO);
			ice.setIdusuario(idusuario);
			ice.setUpdated(new Date());

			// Genera contador PK
			ice.setIdice(contadorPkServicio.generarContadorTabla(TCIce.ICE, null));
			guardar(ice);
			p.setIdice(ice.getIdice());
						
		}
		
		return lista;
				
	}
	

}
