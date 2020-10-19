/**
 * 
 */
package com.vcw.falecpv.core.helper;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGSRIAccesoEnum;
import com.vcw.falecpv.core.dao.impl.EmpresaDao;
import com.vcw.falecpv.core.modelo.dto.SriAccesoDto;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SriAccesoHelper {

	
	@Inject
	private EmpresaDao empresaDao;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public boolean cosultarEstado(String idEmpresa)throws DaoException{
		Empresa e = empresaDao.cargar(idEmpresa);
		if(e.getAmbiente().equals("0")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param sriWs
	 * @param produccion
	 * @return
	 * @throws DaoException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 */
	public SriAccesoDto consultarDatosAcceso(String sriWs,boolean produccion)throws DaoException, NumberFormatException, ParametroRequeridoException{
		
		SriAccesoDto sriAccesoDto = new SriAccesoDto();
		
		if(produccion && sriWs.equals("APROBACION")) {
			sriAccesoDto.setWsdl(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_WSDL_APROBACION_PRODUCCION, TipoRetornoParametroGenerico.STRING));
			sriAccesoDto.setHost(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_HOST_PRODUCCION, TipoRetornoParametroGenerico.STRING));
			return sriAccesoDto;
		}
		
		if(!produccion && sriWs.equals("APROBACION")) {
			sriAccesoDto.setWsdl(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_WSDL_APROBACION_PRUEBAS, TipoRetornoParametroGenerico.STRING));
			sriAccesoDto.setHost(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_HOST_PRUEBAS, TipoRetornoParametroGenerico.STRING));
			return sriAccesoDto;
		}
		
		if(produccion && sriWs.equals("RECEPCION")) {
			sriAccesoDto.setWsdl(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_WSDL_RECEPCION_PRODUCCION, TipoRetornoParametroGenerico.STRING));
			sriAccesoDto.setHost(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_HOST_PRODUCCION, TipoRetornoParametroGenerico.STRING));
			return sriAccesoDto;
		}
		
		if(!produccion && sriWs.equals("APROBACION")) {
			sriAccesoDto.setWsdl(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_WSDL_RECEPCION_PRUEBAS, TipoRetornoParametroGenerico.STRING));
			sriAccesoDto.setHost(parametroGenericoServicio.consultarParametro(PGSRIAccesoEnum.SRI_HOST_PRUEBAS, TipoRetornoParametroGenerico.STRING));
			return sriAccesoDto;
		}
		
		return null;
	}
	
	
}
