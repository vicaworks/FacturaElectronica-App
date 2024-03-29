/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlInfoFactura;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoFactura extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoFactura() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlFactura factura = new XmlFactura();
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		factura.setInfoTributaria(getXmlInfoTributaria(cabecera));
		factura.setInfoFactura(getInfoFactura(cabecera,pagoServicio.getPagoDao().getByIdCabecera(idDocumento),totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idDocumento)));
		List<Detalle> detalleList = detalleServicio.getDetalleDao().getByIdCabecera(idDocumento);
		// detalle impuesto
		List<Detalleimpuesto> detalleimpuestoList = detalleimpuestoServicio.getDetalleimpuestoDao().getByIdCabecera(idDocumento);
		detalleList.stream().forEach(x->{
			x.setDetalleimpuestoList(detalleimpuestoList.stream().filter(y->y.getDetalle().getIddetalle().equals(x.getIddetalle())).collect(Collectors.toList()));
		});
		factura.setDetalleList(getDetalleList(cabecera, detalleList));
		// infoadicional
		factura.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		String xml = new String(XmlCommonsUtil.jaxbMarshall(factura, true, false,encode).getBytes(), StandardCharsets.UTF_8);
		System.out.println("---------------------------------------------------------");
		System.out.println(factura.getInfoTributaria().getContribuyenteRimpe());
		System.out.println(xml);
		return xml;
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @return
	 */
	protected XmlInfoFactura getInfoFactura(Cabecera cabecera,List<Pago> pagoList,List<Totalimpuesto> totalimpuestoList) {
		XmlInfoFactura xmlInfoFactura = new XmlInfoFactura();
		
		xmlInfoFactura.setFechaEmision(cabecera.getFechaemision());
		xmlInfoFactura.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		xmlInfoFactura.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		xmlInfoFactura.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		xmlInfoFactura.setTipoIdentificacionComprador(cabecera.getCliente().getTipoIdentificacion().getCodigo());
		xmlInfoFactura.setRazonSocialComprador(cabecera.getCliente().getRazonsocial());
		xmlInfoFactura.setIdentificacionComprador(cabecera.getCliente().getIdentificacion());
		xmlInfoFactura.setDireccionComprador(cabecera.getCliente().getDireccion());
		xmlInfoFactura.setTotalSinImpuestos(cabecera.getTotalsinimpuestos().doubleValue());
		xmlInfoFactura.setTotalDescuento(cabecera.getTotaldescuento().doubleValue());
		xmlInfoFactura.setImporteTotal(cabecera.getTotalconimpuestos().doubleValue());
		xmlInfoFactura.setMoneda(cabecera.getMoneda());
		xmlInfoFactura.setPropina(0.00d);
		if(cabecera.getValorretenido().doubleValue()>0d) {
			xmlInfoFactura.setValorRetIva(cabecera.getValorretenidoiva().doubleValue());
			xmlInfoFactura.setValorRetRenta(cabecera.getValorretenidorenta().doubleValue());
		}
		// total impuesto
		xmlInfoFactura.setTotalImpuestoList(getTotalImpuesto(totalimpuestoList,true));
		// pagos
		xmlInfoFactura.setPagoList(getPago(pagoList));
		
		return xmlInfoFactura;
	}

}
