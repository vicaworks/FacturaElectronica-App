/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.ValidarParametro;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
import com.vcw.falecpv.core.dao.impl.ParametroGenericoEmpresaDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoEmpresaServicio extends AppGenericService<ParametroGenericoEmpresa, String> {
	
	public enum TipoRetornoParametroGenerico {
		INTEGER, STRING, LONG, BIGDECIMAL, FLOAT,BOOLEAN;
	}
	
	@Inject
	private ParametroGenericoEmpresaDao parametroGenericoEmpresaDao;
	
	/**
	 * 
	 */
	public ParametroGenericoEmpresaServicio() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param <T>
	 * @param parametroId
	 * @param tipoRetornoParametroGenerico
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 */
	@SuppressWarnings("unchecked")
	public <T> T consultarParametroEmpresa(ParametroGenericoBaseEnum parametroId,
			TipoRetornoParametroGenerico tipoRetornoParametroGenerico,String idEmpresa)
			throws DaoException,NumberFormatException, ParametroRequeridoException {

		ValidarParametro.validar(tipoRetornoParametroGenerico,
				"tipoRetornoParametroGenerico");
		ValidarParametro.validar(parametroId, "parametroId");

		ParametroGenericoEmpresa parametrosGenericos = null;

		try {
			parametrosGenericos = parametroGenericoEmpresaDao.getByEmpresa(idEmpresa, parametroId);
		} catch (NoResultException e) {
			throw new DaoException("No existe el parametro generico : " + parametroId.toString() + " id: "
					+ parametroId.getId() + " de la empresa id: " + idEmpresa);
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param <T>
	 * @param parametroId
	 * @param tipoRetornoParametroGenerico
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 */
	@SuppressWarnings("unchecked")
	public <T> T consultarParametroEstablecimiento(ParametroGenericoBaseEnum parametroId,
			TipoRetornoParametroGenerico tipoRetornoParametroGenerico,String idEstablecimiento)
			throws DaoException,NumberFormatException, ParametroRequeridoException {

		ValidarParametro.validar(tipoRetornoParametroGenerico,
				"tipoRetornoParametroGenerico");
		ValidarParametro.validar(parametroId, "parametroId");

		ParametroGenericoEmpresa parametrosGenericos = null;

		try {
			parametrosGenericos = parametroGenericoEmpresaDao.getByEstablecimiento(idEstablecimiento, parametroId);
		} catch (NoResultException e) {
			throw new DaoException("No existe el parametro generico : " + parametroId.toString() + " id: "
					+ parametroId.getId() + " del establecimiento id: " + idEstablecimiento);
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