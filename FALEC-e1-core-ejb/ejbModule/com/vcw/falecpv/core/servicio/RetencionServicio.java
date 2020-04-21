/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCRetencion;
import com.vcw.falecpv.core.dao.impl.RetencionDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Retencion;
import com.vcw.falecpv.core.modelo.persistencia.Retenciondetalle;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionServicio extends AppGenericService<Retencion, String> {

	@Inject
	private RetencionDao retencionDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private RetenciondetalleServicio retenciondetalleServicio;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	

	@Override
	public List<Retencion> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencion, String> getDao() {
		return retencionDao;
	}

	/**
	 * @return the retencionDao
	 */
	public RetencionDao getRetencionDao() {
		return retencionDao;
	}

	public Retencion guardarFacade(Retencion retencion,List<Retenciondetalle> retenciondetalleList,Adquisicion adquisicion)throws DaoException{
		try {
			
			if(retencion.getIdretencion()==null) {
				retencion.setIdretencion(contadorPkServicio.generarContadorTabla(TCRetencion.RETENCION, retencion.getEstablecimiento().getIdestablecimiento()));
				crear(retencion);
			}else {
				actualizar(retencion);
			}
			
			
			// detalle de la retencion
			for (Retenciondetalle rd : retenciondetalleList) {
				rd.setRetencion(retencion);
				if(rd.getIdretenciondetalle()==null || rd.getIdretenciondetalle().contains("MM")) {
					rd.setIdretenciondetalle(contadorPkServicio.generarContadorTabla(TCRetencion.RETENCIONDETALLE, retencion.getEstablecimiento().getIdestablecimiento()));
					retenciondetalleServicio.crear(rd);
				}else {
					retenciondetalleServicio.actualizar(rd);
				}
			}
			
			// actualizar los datos de la compra
			if(adquisicion!=null) {
				adquisicion = adquisicionServicio.consultarByPk(adquisicion.getIdadquisicion());
				adquisicion.setTotalretencion(retencion.getTotalretencion());
				adquisicion.setTotalpagar(adquisicion.getTotalfactura().add(retencion.getTotalretencion().negate()).setScale(2, RoundingMode.HALF_UP));
				adquisicion.setEstado("RETENCION");
				adquisicionServicio.actualizar(adquisicion);
			}
			
			return retencion;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idRetencion
	 * @return
	 * @throws DaoException
	 */
	public Retencion anularRetencion(String idRetencion)throws DaoException{
		try {
			
			Retencion r = consultarByPk(idRetencion);
			r.setEstado("ANULADO");
			if (r.getAdquisicion()!=null) {
				Adquisicion a = adquisicionServicio.consultarByPk(r.getAdquisicion().getIdadquisicion());
				a.setEstado("GEN");
				a.setTotalretencion(BigDecimal.ZERO);
				a.setTotalpagar(a.getTotalfactura());
				adquisicionServicio.actualizar(a);
			}
			r.setAdquisicion(null);
			
			actualizar(r);
			
			return r;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idAdquisicion
	 * @return
	 * @throws DaoException
	 */
	public Retencion getByAdquisicionEstado(String idAdquisicion)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(retencionDao.getEntityManager());
			return (Retencion) q.select("r")
					.from(Retencion.class,"r")
					.equals("r.adquisicion.idadquisicion",idAdquisicion)
					.notEquals("r.estado", "ANULADO").getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
