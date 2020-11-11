/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ConfiguracionEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.impl.ConfiguracionDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Configuracion;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ConfiguracionServicio extends AppGenericService<Configuracion, String> {
	
	@Inject
	private ConfiguracionDao configuracionDao;

	@Override
	public List<Configuracion> consultarActivos() {
		return null;
	}

	@Override
	public List<Configuracion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Configuracion, String> getDao() {
		return configuracionDao;
	}

	/**
	 * @return the configuracionDao
	 */
	public ConfiguracionDao getConfiguracionDao() {
		return configuracionDao;
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param idusuario
	 * @throws DaoException
	 */
	public void populateInformacionAdicional(Cabecera cabecera)throws DaoException{
		try {
			
			List<Configuracion> confList = getByTipoConfiguracion(cabecera.getEstablecimiento().getIdestablecimiento(),
					ConfiguracionEnum.INFORMACION_ADICIONAL,
					GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador()));
			
			if(cabecera.getInfoadicionalList()==null) {
				cabecera.setInfoadicionalList(new ArrayList<>());
			}
			
			for (Configuracion conf : confList) {
				switch (conf.getValor()) {
				case "TOTAL_RETENCION":
					setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getTotalretencion().doubleValue()+"");
					break;
				case "DIRECCION":
					if(cabecera.getTipocomprobante().getIdentificador().equals(GenTipoDocumentoEnum.GUIA_REMISION.getIdentificador())) {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getTransportista().getDireccion()==null?"N/A":cabecera.getTransportista().getDireccion());
					}else {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getCliente().getDireccion()==null?"N/A":cabecera.getCliente().getDireccion());
					}
					break;
				case "TELEFONO":
					if(cabecera.getTipocomprobante().getIdentificador().equals(GenTipoDocumentoEnum.GUIA_REMISION.getIdentificador())) {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getTransportista().getTelefono()==null?"N/A":cabecera.getTransportista().getTelefono());
					}else {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getCliente().getTelefono()==null?"N/A":cabecera.getCliente().getTelefono());
					}
					break;
				case "CORREO":
					if(cabecera.getTipocomprobante().getIdentificador().equals(GenTipoDocumentoEnum.GUIA_REMISION.getIdentificador())) {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getTransportista().getEmail()==null?"N/A":cabecera.getTransportista().getEmail());
					}else {
						setValorInfoAdicional(cabecera.getInfoadicionalList(),conf.getEtiqueta(),cabecera.getCliente().getCorreoelectronico()==null?"N/A":cabecera.getCliente().getCorreoelectronico());
					}
					break;

				default:
					break;
				}
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param infoadicionalList
	 * @param etiqueta
	 * @param valor
	 */
	private void setValorInfoAdicional(List<Infoadicional> infoadicionalList,String etiqueta,String valor) {
		Infoadicional i = infoadicionalList.stream().filter(x->x.getNombre().toUpperCase().equals(etiqueta.toUpperCase())).findFirst().orElse(null);
		if(i!=null) {
			i.setValor(valor);
		}else {
			i = new Infoadicional();
			i.setNombre(etiqueta);
			i.setValor(valor);
			infoadicionalList.add(i);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param configuracionEnum
	 * @return
	 * @throws DaoException
	 */
	public List<Configuracion> getByTipoConfiguracion(String idEstablecimiento,ConfiguracionEnum configuracionEnum,GenTipoDocumentoEnum tipoDocumentoEnum)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(configuracionDao.getEntityManager());
			
			return qb.select("c")
					.from(Configuracion.class,"c")
					.equals("c.establecimiento.idestablecimiento",idEstablecimiento)
					.equals("c.tipoconfiguracion.idtipoconfiguracion",configuracionEnum.getId())
					.equals("c.tipocomprobante.identificador",tipoDocumentoEnum.getIdentificador()).getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
