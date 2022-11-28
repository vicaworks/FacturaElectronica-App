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
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.impl.ComprobanterecibidoDao;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlDocSustento;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlRetencionV2;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanterecibidoServicio extends AppGenericService<Comprobanterecibido, String> {
	
	@Inject
	private ComprobanterecibidoDao  comprobanterecibidoDao;

	@Override
	public List<Comprobanterecibido> consultarActivos() {
		return null;
	}

	@Override
	public List<Comprobanterecibido> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Comprobanterecibido, String> getDao() {
		return comprobanterecibidoDao;
	}

	/**
	 * @return the comprobanterecibidoDao
	 */
	public ComprobanterecibidoDao getComprobanterecibidoDao() {
		return comprobanterecibidoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipoComprobante
	 * @param claveAcceso
	 * @param numComprobante
	 * @return
	 * @throws DaoException
	 */
	public Comprobanterecibido getByComprobanteEmpresa(String idEmpresa,GenTipoDocumentoEnum tipoComprobante,String claveAcceso,String numComprobante)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(comprobanterecibidoDao.getEntityManager());
			
			List<Comprobanterecibido> lista =	qb.select("c")
					.from(Comprobanterecibido.class, "c")
					.equals("c.empresa.idempresa", idEmpresa)
					.equals("c.serieComprobante", numComprobante)
					.equals("c.tipocomprobante.identificador", tipoComprobante.getIdentificador())
					.equals("c.claveAcceso",claveAcceso).getResultList();
			
			if(lista.isEmpty()) {
				return null;
			}
			
			return lista.get(0);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param retencion
	 * @throws DaoException
	 */
	public void conerterV2ToV1(XmlComprobanteRetencion retencion)throws DaoException {
		try {
			
			XmlDocSustento  xmlDoccSustento = retencion.getDocSustentoList().get(0);
			
			retencion.setImpuestoretencionList(new ArrayList<>());
			
			for (XmlRetencionV2 r : xmlDoccSustento.getRetencionList()) {
				XmlImpuestoRetencion xmlImpuestoRetencion = new XmlImpuestoRetencion();
				
				xmlImpuestoRetencion.setCodigo(r.getCodigo());
				xmlImpuestoRetencion.setCodigoRetencion(r.getCodigoRetencion());
				xmlImpuestoRetencion.setBaseImponible(r.getBaseImponible());
				xmlImpuestoRetencion.setPorcentajeRetener(r.getPorcentajeRetener());
				xmlImpuestoRetencion.setValorRetenido(r.getValorRetenido());
				xmlImpuestoRetencion.setCodDocSustento(xmlDoccSustento.getCodDocSustento());
				xmlImpuestoRetencion.setNumDocSustento(xmlDoccSustento.getNumAutDocSustento());
				xmlImpuestoRetencion.setFechaEmisionDocSustento(xmlDoccSustento.getFechaEmisionDocSustento());
				
				retencion.getImpuestoretencionList().add(xmlImpuestoRetencion);
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
