/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

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
import com.vcw.falecpv.core.modelo.xml.XmlInfoLiquidacionCompra;
import com.vcw.falecpv.core.modelo.xml.XmlLiquidacionCompra;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoLiqCompra extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoLiqCompra() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlLiquidacionCompra liqCompra = new XmlLiquidacionCompra();
		
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		liqCompra.setInfoTributaria(getXmlInfoTributaria(cabecera));
		liqCompra.setInfoLiquidacionCompra(
				getInfoLiquidacionCompra(cabecera, pagoServicio.getPagoDao().getByIdCabecera(idDocumento),
						totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(idDocumento)));
		List<Detalle> detalleList = detalleServicio.getDetalleDao().getByIdCabecera(idDocumento);
		// detalle impuesto
		List<Detalleimpuesto> detalleimpuestoList = detalleimpuestoServicio.getDetalleimpuestoDao().getByIdCabecera(idDocumento);
		detalleList.stream().forEach(x->{
			x.setDetalleimpuestoList(detalleimpuestoList.stream().filter(y->y.getDetalle().getIddetalle().equals(x.getIddetalle())).collect(Collectors.toList()));
		});
		liqCompra.setDetalleList(getDetalleList(cabecera, detalleList));
		
		// infoadicional
		liqCompra.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		
		return XmlCommonsUtil.jaxbMarshall(liqCompra, true, false,encode);
	}
	
	protected XmlInfoLiquidacionCompra getInfoLiquidacionCompra(Cabecera cabecera,List<Pago> pagoList,List<Totalimpuesto> totalimpuestoList) {
		XmlInfoLiquidacionCompra info = new XmlInfoLiquidacionCompra();
		
		info.setFechaEmision(cabecera.getFechaemision());
		info.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		info.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		info.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		info.setTipoIdentificacionProveedor(cabecera.getProveedor().getTipoidentificacion().getCodigo());
		info.setRazonSocialProveedor(cabecera.getProveedor().getRazonsocial());
		info.setIdentificacionProveedor(cabecera.getProveedor().getIdentificacion());
		info.setDireccionProveedor(cabecera.getProveedor().getDireccion());
		info.setTotalSinImpuestos(cabecera.getTotalsinimpuestos().doubleValue());
		info.setTotalDescuento(cabecera.getTotaldescuento().doubleValue());
		info.setImporteTotal(cabecera.getTotalconimpuestos().doubleValue());
		info.setMoneda(cabecera.getMoneda());
		
		// total impuesto
		info.setTotalImpuestoList(getTotalImpuesto(totalimpuestoList,true));
		// pagos
		info.setPagoList(getPago(pagoList));
		
		return info;
	}

}
