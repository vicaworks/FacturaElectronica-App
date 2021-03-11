/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;
import com.vcw.falecpv.core.dao.impl.TareacabeceraDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareacabeceraServicio extends AppGenericService<Tareacabecera, String> {
	
	@Inject
	private TareacabeceraDao tareacabeceraDao;
	
	@Inject 
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public TareacabeceraServicio() {
	}

	@Override
	public List<Tareacabecera> consultarActivos() {
		return null;
	}

	@Override
	public List<Tareacabecera> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tareacabecera, String> getDao() {
		return tareacabeceraDao;
	}

	/**
	 * @return the tareacabeceraDao
	 */
	public TareacabeceraDao getTareacabeceraDao() {
		return tareacabeceraDao;
	}

	@Override
	public Tareacabecera crear(Tareacabecera o) throws DaoException {
		try {
			
			o.setIdtareacabecera(contadorPkServicio.generarContadorTabla(TCEmpresa.TAREA_CABECERA, o.getIdEstablecimiento(), true));
			
			return super.crear(o);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	

}
