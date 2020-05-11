package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;

@Stateless
public class CabeceraServicio extends AppGenericService<Cabecera, String> {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DetalleServicio detalleServicio;
	
	@Inject
	private DetalleimpuestoServicio detalleimpuestoServicio;
	
	@Inject
	private PagoServicio pagoServicio;
	
	@Inject
	private InfoadicionalServicio infoadicionalServicio;
	
	@Inject
	private TotalimpuestoServicio totalimpuestoServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	
	
	public CabeceraServicio() {
	}

	@Override
	public List<Cabecera> consultarActivos() {
		return null;
	}

	@Override
	public List<Cabecera> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Cabecera, String> getDao() {
		return cabeceraDao;
	}

	/**
	 * @return the cabeceraDao
	 */
	public CabeceraDao getCabeceraDao() {
		return cabeceraDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @return
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	public Cabecera guardarComprobanteFacade(Cabecera cabecera)throws DaoException, ParametroRequeridoException{
		
		// 1. crear la cabecera
		if(cabecera.getIdcabecera()==null) {
			cabecera.setIdcabecera(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.CABECERA, cabecera.getEstablecimiento().getIdestablecimiento()));
			crear(cabecera);
		}else {
			actualizar(cabecera);
		}
		
		// 2. total impuesto
		totalimpuestoServicio.getTotalimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Totalimpuesto	ti : cabecera.getTotalimpuestoList()) {
			ti.setCabecera(cabecera);
			if(ti.getIdtotalmpuesto()==null || ti.getIdtotalmpuesto().contains("M")) {
				ti.setIdtotalmpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.TOTALIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			totalimpuestoServicio.crear(ti);
		}
		
		
		// 3. guardar detalle
		detalleServicio.getDetalleDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Detalle d : cabecera.getDetalleList()) {
			if(d.getIddetalle()==null || d.getIddetalle().contains("M")) {
				d.setIddetalle(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLE, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			d.setCabecera(cabecera);
			detalleServicio.crear(d);
		}
		
		// 4. detalle impuesto
		detalleimpuestoServicio.getDetalleimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Detalle d : cabecera.getDetalleList()) {
			for (Detalleimpuesto di : d.getDetalleimpuestoList()) {
				di.setDetalle(d);
				if(di.getIddetalleimpuesto()==null || di.getIddetalleimpuesto().contains("M")) {
					di.setIddetalleimpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLEIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				detalleimpuestoServicio.crear(di);
			}
		}
		
		// 5. pago
		pagoServicio.getPagoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Pago	p : cabecera.getPagoList()) {
			if(p.getIdpago()==null || p.getIdpago().contains("M")) {
				p.setIdpago(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.PAGO, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			p.setCabecera(cabecera);
			pagoServicio.crear(p);
		}
		
		// 6. infoadicional
		infoadicionalServicio.getInfoadicionalDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Infoadicional	ia : cabecera.getInfoadicionalList()) {
			if(ia.getIdinfoadicional()==null || ia.getIdinfoadicional().contains("M")) {
				ia.setIdinfoadicional(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.INFOADICIONAL, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			ia.setCabecera(cabecera);
			infoadicionalServicio.crear(ia);
		}
		
		return cabecera;
	}

}
