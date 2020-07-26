package com.vcw.falecpv.core.servicio;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

@Stateless
public class FacturaServicio {

	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private DetalleServicio detalleServicio;
	
	@Inject
	private PagoServicio pagoServicio;
	
	@Inject
	private InfoadicionalServicio infoadicionalServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabeceraList
	 * @return
	 * @throws DaoException
	 */
	public List<Cabecera> getByIdList(List<String> idCabeceraList)throws DaoException{
		try {
			
			List<Cabecera> lista = cabeceraServicio.getCabeceraDao().getByIds(idCabeceraList);
			
			List<Detalle> detalleList = detalleServicio.getDetalleDao().getByIdCabecera(idCabeceraList);
			
			List<Pago> pagoList = pagoServicio.getPagoDao().getByIdCabecera(idCabeceraList);
			
			List<Infoadicional> infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idCabeceraList);
			
			lista.stream().forEach(c->{
				if(!detalleList.isEmpty()) {
					c.setDetalleList(detalleList.stream().filter(d->d.getCabecera().getIdcabecera().equals(c.getIdcabecera())).collect(Collectors.toList()));
				}
				if(!pagoList.isEmpty()) {
					c.setPagoList(pagoList.stream().filter(p->p.getCabecera().getIdcabecera().equals(c.getIdcabecera())).collect(Collectors.toList()));
				}
				if(!infoadicionalList.isEmpty()) {
					c.setInfoadicionalList(infoadicionalList.stream().filter(i->i.getCabecera().getIdcabecera().equals(c.getIdcabecera())).collect(Collectors.toList()));
					
				}
			});
			
			return lista;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
