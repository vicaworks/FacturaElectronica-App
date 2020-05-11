/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.UtilMd5;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;
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
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
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

	/**
	 * @return the empresaDao
	 */
	public EmpresaDao getEmpresaDao() {
		return empresaDao;
	}

	public Empresa guardar(Empresa empresa)throws DaoException{
		try {
			if (empresa.getIdempresa()==null) { // si no existe la empresa
				empresa.setIdempresa(contadorPkServicio.generarContadorTabla(TCEmpresa.EMPRESA, null));
				empresa.setClavefirmaelectronica(UtilMd5.hash(empresa.getClavefirmaelectronica()));
				crear(empresa);
			}
			else { // si ya existe la empresa
				empresa.setClavefirmaelectronica(UtilMd5.hash(empresa.getClavefirmaelectronica()));
				actualizar(empresa);
			}
			return empresa;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
