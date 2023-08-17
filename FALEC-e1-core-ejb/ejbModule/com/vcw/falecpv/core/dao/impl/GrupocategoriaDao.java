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
import com.vcw.falecpv.core.modelo.persistencia.Grupocategoria;

/**
 * 
 */
@Stateless
public class GrupocategoriaDao extends AppGenericDao<Grupocategoria, String> {

	/**
	 * @param type
	 */
	public GrupocategoriaDao() {
		super(Grupocategoria.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * @param estadoRegistroEnum
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Grupocategoria> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT c FROM Grupocategoria c WHERE c.empresa.idempresa=:idempresa AND c.estado=:estado ORDER BY c.grupo");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Grupocategoria c WHERE c.empresa.idempresa=:idempresa ORDER BY c.grupo");
			}
			q.setParameter("idempresa", idEmpresa);
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * @param grupoCategoria
	 * @param idgrupocategoria
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public boolean existeGrupoCategoria(String grupoCategoria,String idgrupocategoria,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idgrupocategoria!=null) {
				
				q = getEntityManager().createQuery("SELECT c FROM Grupocategoria c WHERE c.empresa.idempresa=:idempresa AND upper(c.grupo)=:grupo AND c.idgrupocategoria<>:idgrupocategoria");
				q.setParameter("idgrupocategoria", idgrupocategoria);
				
			}else {
				
				q = getEntityManager().createQuery("SELECT c FROM Grupocategoria c WHERE c.empresa.idempresa=:idempresa AND upper(c.grupo)=:grupo");
				
			}
			
			q.setParameter("grupo", grupoCategoria.toUpperCase());
			q.setParameter("idempresa", idEmpresa);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
