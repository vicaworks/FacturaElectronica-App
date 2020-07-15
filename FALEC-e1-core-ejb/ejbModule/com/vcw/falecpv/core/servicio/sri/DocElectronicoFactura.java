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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlDetalle;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
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
		
		return XmlCommonsUtil.jaxbMarshall(factura, true, false);
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
		// total impuesto
		xmlInfoFactura.setTotalImpuestoList(getTotalImpuesto(totalimpuestoList));
		// pagos
		xmlInfoFactura.setPagoList(getPago(pagoList));
		
		return xmlInfoFactura;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param detalleList
	 * @return
	 */
	protected List<XmlDetalle> getDetalleList(Cabecera cabecera,List<Detalle> detalleList){
		
		List<XmlDetalle> lista = new ArrayList<>();
		
		for (Detalle d : detalleList) {
			XmlDetalle det = new XmlDetalle();
			if(d.getProducto()!=null) {
				det.setCodigoPrincipal(d.getProducto().getCodigoprincipal());
				det.setCodigoAuxiliar(d.getProducto().getCodigoauxiliar());
			}else {
				det.setCodigoPrincipal(d.getIddetalle());
				det.setCodigoAuxiliar(d.getIddetalle());
			}
			det.setDescripcion(d.getDescripcion());
			det.setCantidad(d.getCantidad().doubleValue());
			det.setPrecioUnitario(d.getPreciounitario().doubleValue());
			det.setDescuento(d.getDescuento().doubleValue());
			det.setPrecioTotalSinImpuesto(d.getPreciototalsinimpuesto().doubleValue());
			det.setImpuestoList(new ArrayList<>());
			// detalle del impuesto
			for (Detalleimpuesto i : d.getDetalleimpuestoList()) {
				XmlImpuesto impuesto = new XmlImpuesto();
				if(i.getIva()!=null) {
					impuesto.setCodigo(i.getIva().getCodigo());
				}
				if(i.getIce()!=null) {
					impuesto.setCodigo(i.getIce().getCodigo());
				}
				impuesto.setCodigoPorcentaje(impuesto.getCodigo());
				impuesto.setTarifa(i.getTarifa().doubleValue());
				impuesto.setBaseImponible(i.getBaseimponible().doubleValue());
				impuesto.setValor(i.getValor().doubleValue());
				det.getImpuestoList().add(impuesto);
			}
			lista.add(det);
		}
		
		return lista;
		
	}
	
	

}
