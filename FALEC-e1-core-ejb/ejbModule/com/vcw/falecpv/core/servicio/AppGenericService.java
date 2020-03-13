/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.io.Serializable;

import javax.persistence.NoResultException;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.service.GenericService;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.util.MessageCoreUtil;

/**
 * @author cristianvillarreal
 *
 */
public abstract class AppGenericService<T extends Serializable, PK extends Serializable> extends DBUtilGenericoApp
		implements GenericService<T, PK> {

	public abstract DaoGenerico<T, PK> getDao();
	protected MessageCoreUtil messageCoreUtil = new MessageCoreUtil();

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
