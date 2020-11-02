/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlTotalComprobante;
import com.vcw.falecpv.core.modelo.xml.XmlTotalImpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanteUtilServicio {

	@Inject
	private IvaServicio ivaServicio;
	
	@Inject
	private RetencionimpuestoServicio retencionimpuestoServicio;
	
	@Inject
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param factura
	 * @return
	 * @throws DaoException
	 */
	public List<XmlTotalComprobante> populateTotalesComprobanteFactura(XmlFactura factura,String idEmpresa,boolean propina)throws DaoException{
		List<XmlTotalComprobante> totales = new ArrayList<>();
		
		try {
			XmlTotalComprobante total = new XmlTotalComprobante();
			// consultar los valores del iva
			
			List<Iva> ivaList = ivaServicio.getIvaDao().getLabelComprobante(idEmpresa);
			for (Iva iva : ivaList) {
				total = new XmlTotalComprobante();
				total.setCodigoSri("2");
				total.setIva(iva);
				total.setLabel(iva.getLabelfactura());
				
				XmlTotalImpuesto xmlTotalImpuesto =  factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals(iva.getCodigoIva()) && x.getCodigoPorcentaje().equals(iva.getCodigo())).findFirst().orElse(null);
				if(xmlTotalImpuesto!=null) {
					total.setValor(BigDecimal.valueOf(xmlTotalImpuesto.getBaseImponible()));
				}
				totales.add(total);
			}
			
			// total sin dispuesto
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL SIN IMPUESTOS");
			total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getTotalSinImpuestos()));
			totales.add(total);
			
			// total descuento
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL DESCUENTO");
			total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getTotalDescuento()));
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("ICE");
			total.setValor(BigDecimal.ZERO);
			if(factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).count()>0) {
				total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).mapToDouble(x->x.getValor()).sum()));
			}
			totales.add(total);
			
			// determinar el IVA
			total = new XmlTotalComprobante();
			if(factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).count()>0) {
				// consultar el codigo
				XmlTotalImpuesto xmlTotalImpuesto =  factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).findFirst().orElse(null);
				Iva iva = ivaServicio.getIvaDao().getIva(idEmpresa, xmlTotalImpuesto.getCodigoPorcentaje());
				
				total.setLabel("IVA " + iva.getValor().intValue() + "%");
				total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2")).mapToDouble(x->x.getValor()).sum()));
				
			}else {
				total.setLabel("IVA");
				total.setValor(BigDecimal.ZERO);
			}
			totales.add(total);
			
			if(propina) {
				total = new XmlTotalComprobante();
				total.setLabel("PROPINA");
				total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getPropina()));
				totales.add(total);
			}
			
			total = new XmlTotalComprobante();
			total.setLabel("VALOR TOTAL");
			total.setValor(BigDecimal.valueOf(factura.getInfoFactura().getImporteTotal()));
			totales.add(total);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return totales;
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlImpuestoRetencion
	 * @throws DaoException
	 */
	public void populateImpuestoRetencion(XmlImpuestoRetencion xmlImpuestoRetencion)throws DaoException{
		
		Retencionimpuesto ri = retencionimpuestoServicio.getRetencionimpuestoDao().getByCodigoSri(xmlImpuestoRetencion.getCodigo());
		xmlImpuestoRetencion.setDesCodigoRetencion(ri!=null?ri.getDescripcion():"");
		
		Tipocomprobante tc = tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.getEnumByIdentificador(xmlImpuestoRetencion.getCodDocSustento()));
		xmlImpuestoRetencion.setDesCodDocSustento(tc!=null?tc.getComprobante():"");
		
	}
	
}
