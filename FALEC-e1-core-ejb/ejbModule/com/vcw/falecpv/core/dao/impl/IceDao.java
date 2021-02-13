/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

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
	
	public static final String codigoIce="3";
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
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa AND i.estado=:estado ORDER BY i.codigo");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa ORDER BY i.codigo");
			}
			
			
			
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
	public boolean existeCodigo(String codigo, String idice,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idice!=null) {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.empresa.idempresa=:id and i.codigo=:codigo AND i.idice<>:idice");
				q.setParameter("codigo", codigo);
				q.setParameter("idice", idice);
				
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.empresa.idempresa=:id and i.codigo=:codigo ");
				q.setParameter("codigo", codigo);
			}
			
			q.setParameter("id", idEmpresa); 
			
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
		
		List<Ice> listaaux=new ArrayList<>();
		
		for(Ice p : lista) {
			Ice aux= new Ice();
			if(p.getTarifaadvalorem()!=null && !p.isError()) {
				aux.setCodigo(p.getCodigo());
				aux.setCodigoIce(p.getCodigoIce());
				aux.setDescripcion(p.getDescripcion());
				aux.setIdice(p.getIdice());
				aux.setTarifaadvalorem(p.getTarifaadvalorem());
				aux.setValor(p.getValor());
				aux.setIdice(contadorPkServicio.generarContadorTabla(TCIce.ICE, null));
				listaaux.add(aux);
			}
		}
		
		continuar1:for (Ice p : listaaux) {
			
			// verifica si la fila tienen error
			if (p.isError())
				continue continuar1;

			Ice ice = new Ice();

			// datos iniciales
			ice.setCodigo(p.getCodigo());
			ice.setDescripcion(p.getDescripcion());
			ice.setEmpresa(empresa);
			ice.setTarifaadvalorem(p.getTarifaadvalorem());
			ice.setIdusuario(idusuario);
			ice.setCodigoIce(codigoIce);
			ice.setUpdated(new Date());
			ice.setValor(p.getValor());
			ice.setTarifaadvalorem(p.getTarifaadvalorem());
			ice.setIdice(p.getIdice());
			guardar(ice);
			p.setIdice(ice.getIdice());
	

			
						
		}
		
		return lista;
				
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param codigoImpuesto
	 * @return
	 */
	public Ice getIce(String idEmpresa,String codigoImpuesto) {
		
		Query q = getEntityManager().createQuery("SELECT i FROM Ice i WHERE i.codigoIce='3' AND i.empresa.idempresa=:idEmpresa AND i.codigo=:codigo");
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("codigo", codigoImpuesto);
		
		return (Ice) q.getResultList().get(0);
		
	}

}
