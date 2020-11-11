/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Iva;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IvaDao extends AppGenericDao<Iva, String> {

	/**
	 * @param type
	 */
	public IvaDao() {
		super(Iva.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Iva> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa AND i.estado=:estado ORDER BY i.porcentaje");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa ORDER BY i.porcentaje");
			}
			
			q.setParameter("idempresa", idEmpresa);
			return q.getResultList();
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
	@SuppressWarnings("unchecked")
	public Iva getDefecto(String idEmpresa)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa AND i.defecto=1 ORDER BY i.porcentaje");
			q.setParameter("idempresa", idEmpresa);
			
			List<Iva> lista = q.getResultList();
			
			if(lista.size()>0) return lista.get(0);
			
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param valor
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public Iva getByValor(String idEmpresa,BigDecimal valor)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa AND i.valor=:valor ORDER BY i.porcentaje");
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("valor", valor);
			
			List<Iva> lista = q.getResultList();
			
			if(lista.size()>0) return lista.get(0);
			
			return null;
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
	public List<Iva> getAllIva(String idEmpresa)throws DaoException{
		try {
			List<Iva> lista = new ArrayList<>();
			Query q = getEntityManager()
					.createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa ORDER BY i.porcentaje");
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
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCodigo(String codigo, String idiva,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idiva!=null) {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:id and i.codigo=:codigo AND i.idiva<>:idiva");
				q.setParameter("codigo", codigo);
				q.setParameter("idiva", idiva);
				
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:id and i.codigo=:codigo ");
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
	 * @param valor
	 * @return
	 * @throws DaoException
	 */
	public boolean existeValor(BigDecimal valor, String idiva,String idEmpresa)throws DaoException{
		try {

			Query q = null;

			if (idiva != null) {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:id and i.valor=:valor AND i.idiva<>:idiva");
				q.setParameter("valor", valor);
				q.setParameter("idiva", idiva);
			} else {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:id and i.valor=:valor ");
				q.setParameter("valor", valor);
			}
			q.setParameter("id", idEmpresa);
			if (q.getResultList().size() > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author Isabel Lobato
	 * @param codigo
	 * @return
	 * @throws DaoException
	 */
	public Iva existeValorDefecto(int defecto, String idiva,String idEmpresa)throws DaoException{
		try {
			Query q = null;
			
			if(idiva!=null) {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:idEmpresa and i.defecto=:defecto AND i.idiva<>:idiva");
				q.setParameter("defecto", defecto);
				q.setParameter("idiva", idiva);
				
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Iva i  WHERE i.empresa.idempresa=:idEmpresa and i.defecto=:defecto ");
				q.setParameter("defecto", defecto);
			}
			
			q.setParameter("idEmpresa", idEmpresa);
			
			if(q.getResultList().size()>0) {
				return (Iva) q.getSingleResult();
				}
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Iva> getLabelComprobante(String idEmpresa){
		
		Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.estado='A' AND i.empresa.idempresa=:idEmpresa ORDER BY i.ordenfactura");
		q.setParameter("idEmpresa", idEmpresa);
		return q.getResultList();
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param codigoImpuesto
	 * @return
	 */
	public Iva getIva(String idEmpresa,String codigoImpuesto) {
		
		Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.codigoIva='2' AND i.empresa.idempresa=:idEmpresa AND i.codigo=:codigo ORDER BY i.ordenfactura");
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("codigo", codigoImpuesto);
		
		return (Iva) q.getResultList().get(0);
		
	}
	
}
