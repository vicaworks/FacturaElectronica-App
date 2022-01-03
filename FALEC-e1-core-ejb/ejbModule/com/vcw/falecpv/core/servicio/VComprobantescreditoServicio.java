/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.dao.impl.VComprobantescreditoDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.vista.VComprobantescredito;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantescreditoServicio extends AppGenericService<VComprobantescredito, String> {
	
	@Inject
	private VComprobantescreditoDao vComprobantescreditoDao;
	@Inject
	private PagoDao pagoDao;
	
	/**
	 * 
	 */
	public VComprobantescreditoServicio() {
	}

	@Override
	public List<VComprobantescredito> consultarActivos() {
		return null;
	}

	@Override
	public List<VComprobantescredito> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<VComprobantescredito, String> getDao() {
		return vComprobantescreditoDao;
	}

	/**
	 * @return the vComprobantescreditoDao
	 */
	public VComprobantescreditoDao getvComprobantescreditoDao() {
		return vComprobantescreditoDao;
	}

	/**
	 * @param vComprobantescreditoDao the vComprobantescreditoDao to set
	 */
	public void setvComprobantescreditoDao(VComprobantescreditoDao vComprobantescreditoDao) {
		this.vComprobantescreditoDao = vComprobantescreditoDao;
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param tipocomprobante
	 * @param criterio
	 * @return
	 * @throws DaoException
	 */
	public List<VComprobantescredito> getByCuentasCobrar(String idEstablecimiento,Tipocomprobante tipocomprobante,String criterio,String criterioCliente)throws DaoException{
		try {
			
			List<VComprobantescredito> cabeceralist = vComprobantescreditoDao.getByCuentasCobrar(idEstablecimiento, tipocomprobante,criterio,criterioCliente);
			
			if(cabeceralist.isEmpty()) {
				return cabeceralist;
			}
			
			List<String> idList = cabeceralist.stream().map(x->x.getIdcabecera()).collect(Collectors.toList());
			List<Pago> pagoList = pagoDao.getByIdCabecera(idList);
			
			cabeceralist.stream().forEach(x->{
				x.setPagoList(pagoList.stream().filter(p->(p.getTipopago().getIdtipopago().equals("6") && p.getCabecera().getIdcabecera().equals(x.getIdcabecera()))).collect(Collectors.toList()));
				x.setPagoOtrosList(pagoList.stream().filter(p->(!p.getTipopago().getIdtipopago().equals("6") && p.getCabecera().getIdcabecera().equals(x.getIdcabecera()))).collect(Collectors.toList()));
			});
			
			cabeceralist = cabeceralist.stream().sorted(Comparator.comparing(VComprobantescredito::getFechaemision).thenComparing(VComprobantescredito::getRazonsocial)).collect(Collectors.toList());
			
			return cabeceralist;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
