/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.FechaUtil;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGJobEnum;
import com.vcw.falecpv.core.dao.impl.VComprobantesresumenDao;
import com.vcw.falecpv.core.modelo.vista.VComprobantesresumen;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantesresumenServicio extends AppGenericService<VComprobantesresumen, String> {

	@Inject
	private VComprobantesresumenDao comprobantesresumenDao;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	/**
	 * 
	 */
	public VComprobantesresumenServicio() {
	}

	@Override
	public List<VComprobantesresumen> consultarActivos() {
		return null;
	}

	@Override
	public List<VComprobantesresumen> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<VComprobantesresumen, String> getDao() {
		return comprobantesresumenDao;
	}

	/**
	 * @return the comprobantesresumenDao
	 */
	public VComprobantesresumenDao getComprobantesresumenDao() {
		return comprobantesresumenDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<VComprobantesresumen> getComprobantesTransferencia()throws DaoException{
		try {
			
			int minutosFechaEmision = parametroGenericoServicio.consultarParametro(PGJobEnum.SRI_MINUTOS, TipoRetornoParametroGenerico.INTEGER);
			int minutosAutorizacion = parametroGenericoServicio.consultarParametro(PGJobEnum.SRI_MINUTOS_AUTORIZACION, TipoRetornoParametroGenerico.INTEGER);
			String estados = parametroGenericoServicio.consultarParametro(PGJobEnum.SRI_ESTADOS, TipoRetornoParametroGenerico.STRING);
			
			// 1. calcular la fecha minima de consulta
			Date fechaBaseConsulta = FechaUtil.agregarMinutos(new Date(), minutosFechaEmision * -1);
			fechaBaseConsulta = FechaUtil.truncarFecha(fechaBaseConsulta);
			Date fechaTopeConsulta = FechaUtil.agregarMinutos(new Date(), minutosAutorizacion * -1);
			List<String> estadoList = Arrays.asList(estados.split(","));
			
			QueryBuilder qb = new QueryBuilder(comprobantesresumenDao.getEntityManager());
			return qb.select("c")
						.from(VComprobantesresumen.class, "c")
						.greaterEqualsThan("c.fechaemision", fechaBaseConsulta)
						.lessEqualsThan("c.updated", fechaTopeConsulta)
						.in("c.estado", estadoList)
						.orderBy("c.fechaemision DESC").getResultList();
				
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
