/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.TransaccionTipoEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccion;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionDao extends AppGenericDao<Transaccion, String> {

	/**
	 * @param type
	 */
	public TransaccionDao() {
		super(Transaccion.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param idTransaccionTipo
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Transaccion> getByfechas(String idEstablecimiento,String idTransaccionTipo,Date desde,Date hasta,Transaccionconcepto transaccionconcepto)throws DaoException{
		try {
			
			String sql = "SELECT t FROM Transaccion t WHERE t.establecimiento.idestablecimiento=:idEstablecimiento "
					+ " AND t.transacciontipo.idtransacciontipo =:idTransaccionTipo "
					+ (transaccionconcepto!=null?" AND t.transaccionconcepto.idtransaccionconcepto=:idtransaccionconcepto ":" ")
					+ " AND t.fechaemision BETWEEN :desde AND :hasta "
					+ " ORDER BY t.fechaemision, t.updated";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idTransaccionTipo", idTransaccionTipo);
			q.setParameter("desde", desde);
			q.setParameter("hasta", hasta);
			if(transaccionconcepto!=null) {
				q.setParameter("idtransaccionconcepto", transaccionconcepto.getIdtransaccionconcepto());
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param transaccionTipoEnum
	 * @return
	 * @throws DaoException
	 */
	public BigDecimal getSaldoActual(String idEstablecimiento,TransaccionTipoEnum transaccionTipoEnum)throws DaoException{
		try {
			
			String sql = "select " +
				"	(SUM(valoringreso)-SUM(valoregreso)) as saldo " +
				"	from 	 " +
				"		transaccion t " +
				"	where " +
				"		t.idtransacciontipo = '" + transaccionTipoEnum.getId() + "' " +
				"		and t.idestablecimiento = '" + idEstablecimiento + "' " ;
			
			Query q = getEntityManager().createNativeQuery(sql);
			BigDecimal saldo =  (BigDecimal) q.getSingleResult();
			
			if(saldo==null) return BigDecimal.ZERO;
			
			return saldo.setScale(2, RoundingMode.HALF_UP);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
