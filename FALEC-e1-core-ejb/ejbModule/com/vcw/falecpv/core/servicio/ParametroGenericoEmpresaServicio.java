/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ParametroGenericoEmpresaDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoEmpresaServicio extends AppGenericService<ParametroGenericoEmpresa, String> {
	
	@Inject
	private ParametroGenericoEmpresaDao parametroGenericoEmpresaDao;
	
	/**
	 * 
	 */
	public ParametroGenericoEmpresaServicio() {
	}

	@Override
	public List<ParametroGenericoEmpresa> consultarActivos() {
		return null;
	}

	@Override
	public List<ParametroGenericoEmpresa> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<ParametroGenericoEmpresa, String> getDao() {
		return parametroGenericoEmpresaDao;
	}

}
