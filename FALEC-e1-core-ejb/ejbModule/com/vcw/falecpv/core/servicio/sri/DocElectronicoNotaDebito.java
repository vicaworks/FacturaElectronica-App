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
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlInfoNotaDebito;
import com.vcw.falecpv.core.modelo.xml.XmlMotivo;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoNotaDebito extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoNotaDebito() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlNotaDebito notDebito = new XmlNotaDebito();
		
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		notDebito.setInfoTributaria(getXmlInfoTributaria(cabecera));
		notDebito.setInfoNotaDebito(getInfoNotaCredito(cabecera, pagoServicio.getPagoDao().getByIdCabecera(idDocumento),
				totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idDocumento)));
		// info
		List<Motivo> motivoList = motivoServicio.getMotivoDao().getByIdCabecera(idDocumento);
		notDebito.setMotivoList(getMotivo(motivoList));
		// infoadicional
		notDebito.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		
		return XmlCommonsUtil.jaxbMarshall(notDebito, true, false);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param pagoList
	 * @param totalimpuestoList
	 * @return
	 */
	protected XmlInfoNotaDebito getInfoNotaCredito(Cabecera cabecera,List<Pago> pagoList,List<Totalimpuesto> totalimpuestoList) {
		
		XmlInfoNotaDebito info = new XmlInfoNotaDebito();
		info.setFechaEmision(cabecera.getFechaemision());
		info.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		info.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		info.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		info.setTipoIdentificacionComprador(cabecera.getCliente().getTipoIdentificacion().getCodigo());
		info.setRazonSocialComprador(cabecera.getCliente().getRazonsocial());
		info.setIdentificacionComprador(cabecera.getCliente().getIdentificacion());
		info.setCodDocModificado(cabecera.getTipodocasociado());
		info.setNumDocModificado(ComprobanteHelper.formatNumDocumento(cabecera.getNumdocasociado()));
		info.setFechaEmisionDocSustento(cabecera.getFechaemisiondocasociado());
		info.setTotalSinImpuestos(cabecera.getTotalsinimpuestos().doubleValue());
		info.setValorTotal(cabecera.getImportetotal().doubleValue());
		
		// impuestos
		
		info.setImpuestoList(new ArrayList<>());
		
		for (Totalimpuesto totalimpuesto : totalimpuestoList) {
			XmlImpuesto impuesto = new XmlImpuesto();
			impuesto.setCodigo(totalimpuesto.getIva().getCodigoIva());
			impuesto.setCodigoPorcentaje(totalimpuesto.getIva().getCodigoIva());
			impuesto.setTarifa(totalimpuesto.getIva().getValor().doubleValue());
			impuesto.setBaseImponible(totalimpuesto.getBaseimponible().doubleValue());
			impuesto.setValor(totalimpuesto.getValor().doubleValue());
			info.getImpuestoList().add(impuesto);
		}
		
		// pagos
		info.setPagoList(getPago(pagoList));
		
		return info;
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param motivoList
	 * @return
	 */
	protected List<XmlMotivo> getMotivo(List<Motivo> motivoList){
		
		List<XmlMotivo> lista = new ArrayList<>();
		
		for (Motivo m : motivoList) {
			XmlMotivo motivo = new XmlMotivo();
			motivo.setRazon(m.getRazon());
			motivo.setValor(m.getValor().doubleValue());
			lista.add(motivo);
		}
		
		return lista;
		
	}

}
