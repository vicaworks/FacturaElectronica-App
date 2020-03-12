/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.EmpresaDao;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EmpresaServicio extends AppGenericService<Empresa, String> {
		
	@Inject
	private EmpresaDao empresaDao;
	
	/**
	 * 
	 */
	public EmpresaServicio() {
	}

	@Override
	public List<Empresa> consultarActivos() {		
		return null;
	}

	@Override
	public List<Empresa> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Empresa, String> getDao() {
		return empresaDao;
	}


}
