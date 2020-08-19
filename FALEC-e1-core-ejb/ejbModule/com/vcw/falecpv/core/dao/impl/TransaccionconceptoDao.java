/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionconceptoDao extends AppGenericDao<Transaccionconcepto, String> {

	/**
	 * @param type
	 */
	public TransaccionconceptoDao() {
		super(Transaccionconcepto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idTransacciontipo
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Transaccionconcepto> getByEmpresa(String idEmpresa,String idTransacciontipo,EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			
			String sql = "SELECT t FROM Transaccionconcepto t WHERE t.empresa.idempresa=:idempresa AND t.transacciontipo.idtransacciontipo=:transacciontipo ";
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				sql += " AND t.estado=:estado ";
			}
			sql += " ORDER BY t.nombre";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("transacciontipo", idTransacciontipo);
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param concepto
	 * @param idtransaccionconcepto
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public boolean existeConcepto(String concepto,String idtransaccionconcepto,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idtransaccionconcepto!=null) {
				
				q = getEntityManager().createQuery("SELECT c FROM Transaccionconcepto c WHERE c.empresa.idempresa=:idempresa AND upper(c.nombre)=:nombre AND c.idtransaccionconcepto<>:idtransaccionconcepto");
				q.setParameter("idtransaccionconcepto", idtransaccionconcepto);
				
			}else {
				
				q = getEntityManager().createQuery("SELECT c FROM Transaccionconcepto c WHERE c.empresa.idempresa=:idempresa AND upper(c.nombre)=:nombre");
				
			}
			
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("nombre", concepto);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
