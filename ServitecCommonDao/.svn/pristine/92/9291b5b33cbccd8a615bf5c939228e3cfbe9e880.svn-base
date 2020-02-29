/**
 * 
 */
package com.servitec.common.service;

import java.io.Serializable;

import javax.persistence.NoResultException;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;

/**
 * @author cvillarreal
 * 
 * Implementaci&oacute;n abstracta del CRUD service
 * 
 * @version 1.0
 *
 */
public abstract class GenericServiceImpl<T extends Serializable,PK extends Serializable> implements GenericService<T , PK> {

	public abstract DaoGenerico<T, PK> getDao();
	

	@Override
	public T consultarByPk(PK pk) throws DaoException {
		
		try {
			return getDao().cargar(pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public T crear(T o) throws DaoException {
		getDao().guardar(o);
		return o;
	}

	@Override
	public T actualizar(T o) throws DaoException {
		getDao().actualizar(o);
		return o;
	}

	@Override
	public void eliminar(T o) throws DaoException {
		getDao().eliminacionFisica(o);
	}


	
	
}
