/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.dao.impl.VComprobantespagarcreditoDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.vista.VComprobantespagarcredito;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantespagarcreditoServicio extends AppGenericService<VComprobantespagarcredito, String> {
	
	
	@Inject
	private VComprobantespagarcreditoDao vComprobantespagarcreditoDao;
	@Inject
	private PagoDao pagoDao;

	@Override
	public List<VComprobantespagarcredito> consultarActivos() {
		return null;
	}

	@Override
	public List<VComprobantespagarcredito> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<VComprobantespagarcredito, String> getDao() {
		return vComprobantespagarcreditoDao;
	}

	/**
	 * @return the vComprobantespagarcreditoDao
	 */
	public VComprobantespagarcreditoDao getvComprobantespagarcreditoDao() {
		return vComprobantespagarcreditoDao;
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
	public List<VComprobantespagarcredito> getByCuentasPagar(String idEstablecimiento,String tipocomprobante,String criterio)throws DaoException{
		try {
			
			List<VComprobantespagarcredito> cabeceralist = vComprobantespagarcreditoDao.getByCuentasCobrar(idEstablecimiento, tipocomprobante,criterio);
			
			if(cabeceralist.isEmpty()) {
				return cabeceralist;
			}
			
			List<String> idList = cabeceralist.stream().map(x->x.getIdcabecera()).collect(Collectors.toList());
			List<Pago> pagoList = pagoDao.getByIdCabecera(idList);
			pagoList.addAll(pagoDao.getByIdAdquisicion(idList));
			
			cabeceralist.stream().forEach(x->{
				if(x.getIdtipocomprobante().equals("C")) {
					x.setPagoList(pagoList.stream().filter(p->(p.getTipopago().getIdtipopago().equals("6") && p.getAdquisicion().getIdadquisicion().equals(x.getIdcabecera()))).collect(Collectors.toList()));
					x.setPagoOtrosList(pagoList.stream().filter(p->(!p.getTipopago().getIdtipopago().equals("6") && p.getAdquisicion().getIdadquisicion().equals(x.getIdcabecera()))).collect(Collectors.toList()));
				}else {
					x.setPagoList(pagoList.stream().filter(p->(p.getTipopago().getIdtipopago().equals("6") && p.getCabecera().getIdcabecera().equals(x.getIdcabecera()))).collect(Collectors.toList()));
					x.setPagoOtrosList(pagoList.stream().filter(p->(!p.getTipopago().getIdtipopago().equals("6") && p.getCabecera().getIdcabecera().equals(x.getIdcabecera()))).collect(Collectors.toList()));
				}
			});
			
			return cabeceralist;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
