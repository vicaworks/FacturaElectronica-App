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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlDestinatario;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlGuiaRemision;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlLiquidacionCompra;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCredito;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;
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
	
	@Inject
	private TotalimpuestoServicio totalimpuestoServicio;
	
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
				// el primer sibtotal
				totales.get(0).setLabel(totales.get(0).getLabel().replace("%", "") + iva.getValor().intValue() + "%");
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
	 * @param nc
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<XmlTotalComprobante> populateTotalesComprobantenotaCredito(XmlNotaCredito nc,String idEmpresa)throws DaoException{
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
				
				XmlTotalImpuesto xmlTotalImpuesto =  nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals(iva.getCodigoIva()) && x.getCodigoPorcentaje().equals(iva.getCodigo())).findFirst().orElse(null);
				if(xmlTotalImpuesto!=null) {
					total.setValor(BigDecimal.valueOf(xmlTotalImpuesto.getBaseImponible()));
				}
				totales.add(total);
			}
			
			// total sin dispuesto
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL SIN IMPUESTOS");
			total.setValor(BigDecimal.valueOf(nc.getInfoNotaCredito().getTotalSinImpuestos()));
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("ICE");
			total.setValor(BigDecimal.ZERO);
			if(nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).count()>0) {
				total.setValor(BigDecimal.valueOf(nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).mapToDouble(x->x.getValor()).sum()));
			}
			totales.add(total);
			
			// determinar el IVA
			total = new XmlTotalComprobante();
			if(nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).count()>0) {
				// consultar el codigo
				XmlTotalImpuesto xmlTotalImpuesto =  nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).findFirst().orElse(null);
				Iva iva = ivaServicio.getIvaDao().getIva(idEmpresa, xmlTotalImpuesto.getCodigoPorcentaje());
				
				total.setLabel("IVA " + iva.getValor().intValue() + "%");
				total.setValor(BigDecimal.valueOf(nc.getInfoNotaCredito().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2")).mapToDouble(x->x.getValor()).sum()));
				
			}else {
				total.setLabel("IVA");
				total.setValor(BigDecimal.ZERO);
			}
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("VALOR TOTAL");
			total.setValor(BigDecimal.valueOf(nc.getInfoNotaCredito().getValorModificacion()));
			totales.add(total);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return totales;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param notaDebito
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<XmlTotalComprobante> populateTotalesComprobanteNotaDebito(XmlNotaDebito notaDebito,String idEmpresa)throws DaoException{
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
				
				XmlImpuesto xmlTotalImpuesto =  notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals(iva.getCodigoIva()) && x.getCodigoPorcentaje().equals(iva.getCodigo())).findFirst().orElse(null);
				if(xmlTotalImpuesto!=null) {
					total.setValor(BigDecimal.valueOf(xmlTotalImpuesto.getBaseImponible()));
				}
				totales.add(total);
			}
			
			// total sin dispuesto
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL SIN IMPUESTOS");
			total.setValor(BigDecimal.valueOf(notaDebito.getInfoNotaDebito().getTotalSinImpuestos()));
			totales.add(total);
			
			
			total = new XmlTotalComprobante();
			total.setLabel("ICE");
			total.setValor(BigDecimal.ZERO);
			if(notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals("3")).count()>0) {
				total.setValor(BigDecimal.valueOf(notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals("3")).mapToDouble(x->x.getValor()).sum()));
			}
			totales.add(total);
			
			// determinar el IVA
			total = new XmlTotalComprobante();
			if(notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).count()>0) {
				// consultar el codigo
				XmlImpuesto xmlTotalImpuesto =  notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).findFirst().orElse(null);
				Iva iva = ivaServicio.getIvaDao().getIva(idEmpresa, xmlTotalImpuesto.getCodigoPorcentaje());
				
				total.setLabel("IVA " + iva.getValor().intValue() + "%");
				total.setValor(BigDecimal.valueOf(notaDebito.getInfoNotaDebito().getImpuestoList().stream().filter(x->x.getCodigo().equals("2")).mapToDouble(x->x.getValor()).sum()));
				
			}else {
				total.setLabel("IVA");
				total.setValor(BigDecimal.ZERO);
			}
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("VALOR TOTAL");
			total.setValor(BigDecimal.valueOf(notaDebito.getInfoNotaDebito().getValorTotal()));
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
		
		System.out.println("=== " + xmlImpuestoRetencion.getCodDocSustento());
		xmlImpuestoRetencion.setDesCodDocSustento("NA");
		if(GenTipoDocumentoEnum.getEnumByIdentificador(xmlImpuestoRetencion.getCodDocSustento())!=null) {
			Tipocomprobante tc = tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.getEnumByIdentificador(xmlImpuestoRetencion.getCodDocSustento()));
			xmlImpuestoRetencion.setDesCodDocSustento(tc!=null?tc.getComprobante():"");
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlGuiaRemision
	 * @throws DaoException
	 */
	public void populateGuiaRemision(XmlGuiaRemision xmlGuiaRemision)throws DaoException{
		if(xmlGuiaRemision.getDestinatarioList()!=null) {
			for (XmlDestinatario d : xmlGuiaRemision.getDestinatarioList()) {
				d.setTipoDocSustento("");
				if(GenTipoDocumentoEnum.getEnumByIdentificador(d.getCodDocSustento())!=null) {
					d.setTipoDocSustento(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.getEnumByIdentificador(d.getCodDocSustento())).getComprobante());
				}
			}
		}
		
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param factura
	 * @param idEmpresa
	 * @param propina
	 * @return
	 * @throws DaoException
	 */
	public List<XmlTotalComprobante> populateTotalesComprobanteLiqCompra(XmlLiquidacionCompra factura,String idEmpresa)throws DaoException{
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
				
				XmlTotalImpuesto xmlTotalImpuesto =  factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals(iva.getCodigoIva()) && x.getCodigoPorcentaje().equals(iva.getCodigo())).findFirst().orElse(null);
				if(xmlTotalImpuesto!=null) {
					total.setValor(BigDecimal.valueOf(xmlTotalImpuesto.getBaseImponible()));
				}
				totales.add(total);
			}
			
			// total sin dispuesto
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL SIN IMPUESTOS");
			total.setValor(BigDecimal.valueOf(factura.getInfoLiquidacionCompra().getTotalSinImpuestos()));
			totales.add(total);
			
			// total descuento
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL DESCUENTO");
			total.setValor(BigDecimal.valueOf(factura.getInfoLiquidacionCompra().getTotalDescuento()));
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("ICE");
			total.setValor(BigDecimal.ZERO);
			if(factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).count()>0) {
				total.setValor(BigDecimal.valueOf(factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("3")).mapToDouble(x->x.getValor()).sum()));
			}
			totales.add(total);
			
			// determinar el IVA
			total = new XmlTotalComprobante();
			if(factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).count()>0) {
				// consultar el codigo
				XmlTotalImpuesto xmlTotalImpuesto =  factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2") && x.getValor()>0d).findFirst().orElse(null);
				Iva iva = ivaServicio.getIvaDao().getIva(idEmpresa, xmlTotalImpuesto.getCodigoPorcentaje());
				
				total.setLabel("IVA " + iva.getValor().intValue() + "%");
				total.setValor(BigDecimal.valueOf(factura.getInfoLiquidacionCompra().getTotalImpuestoList().stream().filter(x->x.getCodigo().equals("2")).mapToDouble(x->x.getValor()).sum()));
				// el primer sibtotal
				totales.get(0).setLabel(totales.get(0).getLabel().replace("%", "") + iva.getValor().intValue() + "%");
			}else {
				total.setLabel("IVA");
				total.setValor(BigDecimal.ZERO);
			}
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("VALOR TOTAL");
			total.setValor(BigDecimal.valueOf(factura.getInfoLiquidacionCompra().getImporteTotal()));
			totales.add(total);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return totales;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param factura
	 * @param idEmpresa
	 * @param propina
	 * @return
	 * @throws DaoException
	 */
	public List<XmlTotalComprobante> populateTotalesCotizacion(Cabecera factura,String idEmpresa,boolean propina)throws DaoException{
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
				
				
				List<Totalimpuesto> totalimpuestoList = totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(factura.getIdcabecera());
				Totalimpuesto totalimpuesto = totalimpuestoList.stream().filter(x->x.getIva()!=null && x.getIva().getCodigo().equals(iva.getCodigo())).findFirst().orElse(null);
				
				if(totalimpuesto!=null) {
					total.setValor(totalimpuesto.getBaseimponible());
				}
				totales.add(total);
			}
			
			// total sin dispuesto
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL SIN IMPUESTOS");
			total.setValor(factura.getTotalsinimpuestos());
			totales.add(total);
			
			// total descuento
			total = new XmlTotalComprobante();
			total.setLabel("TOTAL DESCUENTO");
			total.setValor(factura.getTotaldescuento());
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("ICE");
			total.setValor(BigDecimal.ZERO);
			
			List<Totalimpuesto> totalimpuestoList = totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(factura.getIdcabecera());
			
			if(totalimpuestoList.stream().filter(x->x.getIce()!=null && x.getIce().getCodigoIce().equals("3")).count()>0) {
				total.setValor(BigDecimal.valueOf(totalimpuestoList.stream().filter(x->x.getIce()!=null && x.getIce().getCodigoIce().equals("3")).mapToDouble(x->x.getValor().doubleValue()).sum()));
			}
			totales.add(total);
			
			// determinar el IVA
			total = new XmlTotalComprobante();
			if(totalimpuestoList.stream().filter(x->x.getIva().getCodigoIva().equals("2") && x.getValor().doubleValue()>0d).count()>0) {
				// consultar el codigo
				Totalimpuesto totalimpuesto =  totalimpuestoList.stream().filter(x->x.getIva()!=null && x.getIva().getCodigoIva().equals("2") && x.getValor().doubleValue()>0d).findFirst().orElse(null);
				Iva iva = totalimpuesto.getIva();
				
				total.setLabel("IVA " + iva.getValor().intValue() + "%");
				total.setValor(BigDecimal.valueOf(totalimpuestoList.stream().filter(x->x.getIva()!=null && x.getIva().getCodigoIva().equals("2")).mapToDouble(x->x.getValor().doubleValue()).sum()));
				// el primer sibtotal
				totales.get(0).setLabel(totales.get(0).getLabel().replace("%", "") + iva.getValor().intValue() + "%");
			}else {
				total.setLabel("IVA");
				total.setValor(BigDecimal.ZERO);
			}
			totales.add(total);
			
			total = new XmlTotalComprobante();
			total.setLabel("VALOR TOTAL");
			total.setValor(factura.getTotalconimpuestos());
			totales.add(total);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return totales;
	}
	
}
