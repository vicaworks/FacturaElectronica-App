/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.ValidarParametro;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
import com.vcw.falecpv.core.dao.impl.ParametroGenericoDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoServicio extends AppGenericService<ParametroGenerico, String> {
	
	public enum TipoRetornoParametroGenerico {
		INTEGER, STRING, LONG, BIGDECIMAL, FLOAT,BOOLEAN;
	}

	@Inject
	private ParametroGenericoDao parametroGenericoDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	
	/**
	 * 
	 */
	public ParametroGenericoServicio() {
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param <T>
	 * @param parametroId
	 * @param tipoRetornoParametroGenerico
	 * @return
	 * @throws DaoException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 */
	@SuppressWarnings("unchecked")
	public <T> T consultarParametro(ParametroGenericoBaseEnum parametroId,
			TipoRetornoParametroGenerico tipoRetornoParametroGenerico)
			throws DaoException,NumberFormatException, ParametroRequeridoException {

		ValidarParametro.validar(tipoRetornoParametroGenerico,
				"tipoRetornoParametroGenerico");
		ValidarParametro.validar(parametroId, "parametroId");

		ParametroGenerico parametrosGenericos = null;

		try {
			parametrosGenericos = parametroGenericoDao.cargar(parametroId.getId());
		} catch (NoResultException e) {
			throw new DaoException("No existe el parametro generico : " + parametroId.toString() + " id: "
					+ parametroId.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}

		switch (tipoRetornoParametroGenerico) {
		case STRING:
			
			return (T)parametrosGenericos.getValor();
			
		case BIGDECIMAL:
			
			return (T)BigDecimal.valueOf(Double.parseDouble(parametrosGenericos.getValor()));
			
		case FLOAT:
			Float num = Float.parseFloat(parametrosGenericos.getValor()); 
			return (T)num;
		case INTEGER:
			Integer num_i = Integer.parseInt(parametrosGenericos.getValor());
			
			return (T) num_i;

		case LONG:
			
			Long num_l = Long.parseLong(parametrosGenericos.getValor());
			
			return (T)num_l;
		case BOOLEAN:
			
			if (parametrosGenericos.getValor().toUpperCase().contains("S")){
				return (T)Boolean.valueOf(true);
			}
			
			return (T)Boolean.valueOf(false);
		}

		return null;
		
	}
	
	

	@Override
	public List<ParametroGenerico> consultarActivos() {
		return null;
	}

	@Override
	public List<ParametroGenerico> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<ParametroGenerico, String> getDao() {
		return parametroGenericoDao;
	}

}
