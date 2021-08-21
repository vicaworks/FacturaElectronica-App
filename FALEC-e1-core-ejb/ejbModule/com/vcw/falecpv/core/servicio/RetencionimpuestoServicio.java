/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.RetencionimpuestoDao;
import com.vcw.falecpv.core.modelo.dto.SubtotalDto;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestoServicio extends AppGenericService<Retencionimpuesto, String> {
	
	@Inject
	private RetencionimpuestoDao retencionimpuestoDao;

	@Override
	public List<Retencionimpuesto> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencionimpuesto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencionimpuesto, String> getDao() {
		return retencionimpuestoDao;
	}

	/**
	 * @return the retencionimpuestoDao
	 */
	public RetencionimpuestoDao getRetencionimpuestoDao() {
		return retencionimpuestoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigo
	 * @return
	 * @throws DaoException
	 */
	public Retencionimpuesto getByCodSri(String codigo)throws DaoException{
		try {
				
			QueryBuilder qb = new QueryBuilder(retencionimpuestoDao.getEntityManager());
			
			return (Retencionimpuesto)qb.select("r")
					.from(Retencionimpuesto.class,"r")
					.equals("r.codigo",codigo).getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * Resumen de retencion impuestos
	 * @param idCabeceraList
	 * @return
	 * @throws DaoException
	 */
	public List<SubtotalDto> getResumentRetencionImpuestoByIdCabecera(String idCabeceraList)throws DaoException{
		try {
			
			idCabeceraList = "'" + idCabeceraList + "'"; 
			
			String sql = "select " + 
			"		r.nombre as concepto, " +	
			"		SUM(i.valorretenido) as valor " + 
			"	from  " +
			"		impuestoretencion i inner join retencionimpuestodet id on i.idretencionimpuestodet = id.idretencionimpuestodet " + 
			"		inner join retencionimpuesto r on r.idretencionimpuesto = id.idretencionimpuesto  " +
			"	where  " +
			"		i.idcabecera in (" + idCabeceraList + ") " +
			"	group by " +
			"		r.nombre " +
			"	order by  " +
			"		r.nombre";
			return resultList(sql, SubtotalDto.class, false);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
