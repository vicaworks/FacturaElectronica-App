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
import com.vcw.falecpv.core.constante.contadores.TCUsuario;
import com.vcw.falecpv.core.dao.impl.EmpresaDao;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;

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
			if (empresa.getIdempresa()==null) {
				empresa.setIdempresa(contadorPkServicio.generarContadorTabla(TCEmpresa.EMPRESA, ""));
				crear(empresa);
			}
			else {
//			empresa.setClave(UtilMd5.hash(empresa.getClave()));
				actualizar(empresa);
			}
			return empresa;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
