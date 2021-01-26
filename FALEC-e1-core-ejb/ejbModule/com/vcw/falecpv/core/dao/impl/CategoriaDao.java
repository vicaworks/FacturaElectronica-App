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
import com.vcw.falecpv.core.modelo.persistencia.Categoria;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CategoriaDao extends AppGenericDao<Categoria, String> {

	/**
	 * @param type
	 */
	public CategoriaDao() {
		super(Categoria.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Categoria> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.empresa.idempresa=:idempresa AND c.estado=:estado ORDER BY c.categoria");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.empresa.idempresa=:idempresa ORDER BY c.categoria");
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
	 * @param categoria
	 * @param idcategoria
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCategoria(String categoria,String idcategoria,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idcategoria!=null) {
				
				q = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.empresa.idempresa=:idempresa AND upper(c.categoria)=:categoria AND c.idcategoria<>:idcategoria");
				q.setParameter("idcategoria", idcategoria);
				
			}else {
				
				q = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.empresa.idempresa=:idempresa AND upper(c.categoria)=:categoria");
				
			}
			
			q.setParameter("categoria", categoria.toUpperCase());
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
