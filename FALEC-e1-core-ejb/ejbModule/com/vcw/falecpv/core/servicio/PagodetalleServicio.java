/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.inject.Inject;

import javax.ejb.Stateless;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.PagodetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Pagodetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagodetalleServicio extends AppGenericService<Pagodetalle, String> {

	
	@Inject
	private PagodetalleDao pagodetalleDao;
	
	@Override
	public List<Pagodetalle> consultarActivos() {
		return null;
	}

	@Override
	public List<Pagodetalle> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Pagodetalle, String> getDao() {
		return null;
	}

	/**
	 * @return the pagodetalleDao
	 */
	public PagodetalleDao getPagodetalleDao() {
		return pagodetalleDao;
	}

}
