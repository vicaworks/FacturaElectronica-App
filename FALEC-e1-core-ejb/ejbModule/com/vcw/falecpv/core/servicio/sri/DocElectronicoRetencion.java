/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlInfoCompRetencion;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoRetencion extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoRetencion() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlComprobanteRetencion retencion = new XmlComprobanteRetencion();
		
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		retencion.setInfoTributaria(getXmlInfoTributaria(cabecera));
		retencion.setInfoCompRetencion(getXmlInfoCompRetencion(cabecera));
		
		// impuestos
		List<Impuestoretencion> impuestoretencionList = impuestoretencionServicio.getImpuestoretencionDao().getByCabecera(cabecera.getIdcabecera());
		retencion.setImpuestoretencionList(getXmlImpuestoRetencion(impuestoretencionList));
		
		// infoadicional
		retencion.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		
		return XmlCommonsUtil.jaxbMarshall(retencion, true, false,encode);
	}
	
	protected XmlInfoCompRetencion getXmlInfoCompRetencion(Cabecera cabecera) {
		XmlInfoCompRetencion info = new XmlInfoCompRetencion();
		
		info.setFechaEmision(cabecera.getFechaemision());
		info.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		info.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		info.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		info.setTipoIdentificacionSujetoRetenido(cabecera.getCliente().getTipoIdentificacion().getCodigo());
		info.setRazonSocialSujetoRetenido(cabecera.getCliente().getRazonsocial());
		info.setIdentificacionSujetoRetenido(cabecera.getCliente().getIdentificacion());
		info.setPeriodoFiscal(cabecera.getPeriodofiscal());
		return info;
	}
	
	protected List<XmlImpuestoRetencion> getXmlImpuestoRetencion(List<Impuestoretencion> impuestoretencionList){
		List<XmlImpuestoRetencion> lista = new ArrayList<>();
		
		for (Impuestoretencion i : impuestoretencionList) {
			XmlImpuestoRetencion imp = new XmlImpuestoRetencion();
			imp.setCodigo(i.getCodigo());
			imp.setCodigoRetencion(i.getCodigoretencion());
			imp.setBaseImponible(i.getBaseimponible().doubleValue());
			imp.setPorcentajeRetener(i.getPorcentajeretener().doubleValue());
			imp.setValorRetenido(i.getValorretenido().doubleValue());
			imp.setCodDocSustento(i.getCoddocsustento());
			imp.setNumDocSustento(i.getNumdocsustento());
			imp.setFechaEmisionDocSustento(i.getFechaemisiondocsustento());
			lista.add(imp);
			
		}
		
		return lista;
	}
	
	
}
