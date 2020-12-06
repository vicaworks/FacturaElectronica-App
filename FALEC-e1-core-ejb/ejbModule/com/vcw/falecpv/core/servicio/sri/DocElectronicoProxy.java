/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.DestinatarioServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.DetalledestinatarioServicio;
import com.vcw.falecpv.core.servicio.DetalleimpuestoServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ImpuestoretencionServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.core.servicio.MotivoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TotalimpuestoServicio;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DocElectronicoProxy {

	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private DetalleServicio detalleServicio;
	
	@Inject
	private DetalleimpuestoServicio detalleimpuestoServicio;
	
	@Inject
	private PagoServicio pagoServicio;
	
	@Inject
	private InfoadicionalServicio infoadicionalServicio;
	
	@Inject
	private TotalimpuestoServicio totalimpuestoServicio;
	
	@Inject
	private ProductoServicio productoServicio;
	
	@Inject
	private ImpuestoretencionServicio impuestoretencionServicio;
	
	@Inject
	private AdquisicionServicio adquisicionServicio;
	
	@Inject
	private DestinatarioServicio destinatarioServicio;
	
	@Inject
	private DetalledestinatarioServicio detalledestinatarioServicio;
	
	@Inject
	private MotivoServicio motivoServicio;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param idEstablecimiento
	 * @param tipoDocumento
	 * @return
	 * @throws DaoException
	 * @throws JAXBException 
	 */
	public String getDocElectronicoFacade(String idCabecera,String idEstablecimiento,GenTipoDocumentoEnum tipoDocumento)throws DaoException, JAXBException{
		
		GenerarDocumentoElectronico generarDocumentoElectronico = null;
		
		switch (tipoDocumento) {
		case FACTURA:
		case RECIBO:	
			generarDocumentoElectronico = new DocElectronicoFactura();
			break;
		
		case RETENCION:
			generarDocumentoElectronico = new DocElectronicoRetencion();
			break;
		
		case LIQUIDACION_COMPRA:
			generarDocumentoElectronico = new DocElectronicoLiqCompra();
			break;
		
		case NOTA_CREDITO:
			generarDocumentoElectronico = new DocElectronicoNotaCredito();
			break;
		case NOTA_DEBITO:
			generarDocumentoElectronico = new DocElectronicoNotaDebito();
			break;
		case GUIA_REMISION:
			generarDocumentoElectronico = new DocElectronicoGuiaRemision();
			break;
			
		default:
			break;
		}
		
		setServicios(generarDocumentoElectronico);
		return generarDocumentoElectronico.generarFacade(idCabecera, idEstablecimiento, tipoDocumento);
		
	}
	
	
	/**
	 * @param generarDocumentoElectronico
	 */
	private void setServicios(GenerarDocumentoElectronico generarDocumentoElectronico) {
		generarDocumentoElectronico.setCabeceraServicio(cabeceraServicio);
		generarDocumentoElectronico.setDetalleServicio(detalleServicio);
		generarDocumentoElectronico.setDetalleimpuestoServicio(detalleimpuestoServicio);
		generarDocumentoElectronico.setPagoServicio(pagoServicio);
		generarDocumentoElectronico.setInfoadicionalServicio(infoadicionalServicio);
		generarDocumentoElectronico.setTotalimpuestoServicio(totalimpuestoServicio);
		generarDocumentoElectronico.setProductoServicio(productoServicio);
		generarDocumentoElectronico.setImpuestoretencionServicio(impuestoretencionServicio);
		generarDocumentoElectronico.setAdquisicionServicio(adquisicionServicio);
		generarDocumentoElectronico.setDestinatarioServicio(destinatarioServicio);
		generarDocumentoElectronico.setDetalledestinatarioServicio(detalledestinatarioServicio);
		generarDocumentoElectronico.setMotivoServicio(motivoServicio);
		generarDocumentoElectronico.setEstablecimientoServicio(establecimientoServicio);
	}

}
