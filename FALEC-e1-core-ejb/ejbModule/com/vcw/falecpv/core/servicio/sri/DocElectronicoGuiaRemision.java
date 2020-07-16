/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.modelo.xml.XmlDestinatario;
import com.vcw.falecpv.core.modelo.xml.XmlDestinatarioDetalle;
import com.vcw.falecpv.core.modelo.xml.XmlGuiaRemision;
import com.vcw.falecpv.core.modelo.xml.XmlInfoGuiaRemision;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoGuiaRemision extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoGuiaRemision() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlGuiaRemision guiaRemision = new XmlGuiaRemision();
		
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		guiaRemision.setInfoTributaria(getXmlInfoTributaria(cabecera));
		// informacion
		guiaRemision.setInfoGuiaRemision(getInfoGuiaRemision(cabecera));
		// destinatarios
		List<String> ids = new ArrayList<>();
		ids.add(cabecera.getIdcabecera());
		List<Destinatario> destinatarioList = destinatarioServicio.getDestinatarioDao().getIdDestinatarioByCabeceraIdList(ids);
		List<Detalledestinatario> detalledestinatarioList = detalledestinatarioServicio.getDetalledestinatarioDao()
				.getByIdListDestinatario(
						destinatarioList.stream().map(x -> x.getIddestinatario()).collect(Collectors.toList()));
		destinatarioList.stream().forEach(x->{
			x.setDetalledestinatarioList(detalledestinatarioList.stream()
					.filter(y -> y.getDestinatario().getIddestinatario().equals(x.getIddestinatario()))
					.collect(Collectors.toList()));
		});
		guiaRemision.setDestinatarioList(getDestinatario(destinatarioList));
		
		// infoadicional
		guiaRemision.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		
		return XmlCommonsUtil.jaxbMarshall(guiaRemision, true, false);
	}

	protected XmlInfoGuiaRemision getInfoGuiaRemision(Cabecera cabecera) {
		XmlInfoGuiaRemision info = new XmlInfoGuiaRemision();
		
		info.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		info.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		info.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		info.setTipoIdentificacionTransportista(cabecera.getTransportista().getTipoIdentificacion().getCodigo());
		info.setRazonSocialTransportista(cabecera.getTransportista().getRazonsocial());
		info.setRucTransportista(cabecera.getTransportista().getIdentificacion());
		info.setDirPartida(cabecera.getDireccionpartida());
		info.setFechaIniTransporte(cabecera.getFechainiciotransporte());
		info.setFechaFinTransporte(cabecera.getFechafintransporte());
		info.setPlaca(cabecera.getPlaca());
		
		return info;
	}
	
	protected List<XmlDestinatario> getDestinatario(List<Destinatario> destinatarioList){
		
		List<XmlDestinatario> lista = new ArrayList<>();
		
		for (Destinatario d : destinatarioList) {
			XmlDestinatario dest = new XmlDestinatario();
			
			dest.setIdentificacionDestinatario(d.getIdentificaciondestinatario());
			dest.setRazonSocialDestinatario(d.getRazonsocialdestinatario());
			dest.setDirDestinatario(d.getDirdestinatario());
			dest.setMotivoTraslado(d.getMotivotraslado());
			dest.setDocAduaneroUnico(d.getDocaduanerounico());
			dest.setCodEstabDestino(d.getCodestabdestino());
			dest.setRuta(d.getRuta());
			dest.setCodDocSustento(d.getCoddocsustento());
			dest.setNumDocSustento(ComprobanteHelper.formatNumDocumento(d.getNumdocsustento()));
			dest.setNumAutDocSustento(d.getNumautdocsustento());
			dest.setFechaEmisionDocSustento(d.getFechaemisiondocsustento());
			dest.setDestinatarioDetallesList(new ArrayList<>());
			
			for (Detalledestinatario dd : d.getDetalledestinatarioList()) {
				XmlDestinatarioDetalle det = new XmlDestinatarioDetalle();
				
				det.setCodigoInterno(dd.getCodigointerno());
				det.setCodigoAdicional(dd.getCodigoadicional());
				det.setDescripcion(dd.getDescripcion());
				det.setCantidad(dd.getCantidad().intValue());
				
				dest.getDestinatarioDetallesList().add(det);
			}
			
			
			lista.add(dest);
		}
		
		return lista;
	}
	
}
