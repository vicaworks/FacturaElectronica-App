/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlCampoAdicional;
import com.vcw.falecpv.core.modelo.xml.XmlInfoTributaria;
import com.vcw.falecpv.core.modelo.xml.XmlPago;
import com.vcw.falecpv.core.modelo.xml.XmlTotalImpuesto;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.DestinatarioServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.DetalledestinatarioServicio;
import com.vcw.falecpv.core.servicio.DetalleimpuestoServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ImpuestoretencionServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.core.servicio.KardexProductoServicio;
import com.vcw.falecpv.core.servicio.MotivoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TotalimpuestoServicio;

/**
 * @author cristianvillarreal
 *
 */
public abstract class GenerarDocumentoElectronico {

	
	
	protected CabeceraServicio cabeceraServicio;
	protected DetalleServicio detalleServicio;
	protected DetalleimpuestoServicio detalleimpuestoServicio;
	protected PagoServicio pagoServicio;
	protected InfoadicionalServicio infoadicionalServicio;
	protected TotalimpuestoServicio totalimpuestoServicio;
	protected KardexProductoServicio kardexProductoServicio;
	protected ProductoServicio productoServicio;
	protected ImpuestoretencionServicio impuestoretencionServicio;
	protected AdquisicionServicio adquisicionServicio;
	protected DestinatarioServicio destinatarioServicio;
	protected DetalledestinatarioServicio detalledestinatarioServicio;
	protected MotivoServicio motivoServicio;
	protected EstablecimientoServicio establecimientoServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idDocumento
	 * @param idEstablecimeinto
	 * @param tipoDocumento
	 * @return
	 * @throws DaoException
	 */
	public abstract String generarFacade(String idDocumento,String idEstablecimeinto,GenTipoDocumentoEnum tipoDocumento)throws DaoException, JAXBException;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @return
	 */
	protected XmlInfoTributaria getXmlInfoTributaria(Cabecera cabecera) {
		XmlInfoTributaria infoTributaria = new XmlInfoTributaria();
		
		infoTributaria.setAmbiente(cabecera.getEstablecimiento().getAmbiente());
		infoTributaria.setTipoEmision(cabecera.getTipoemision());
		infoTributaria.setRazonSocial(cabecera.getEstablecimiento().getEmpresa().getRazonsocial());
		infoTributaria.setNombreComercial(cabecera.getEstablecimiento().getNombrecomercial());
		infoTributaria.setRuc(cabecera.getEstablecimiento().getEmpresa().getRuc());
		infoTributaria.setClaveAcceso(cabecera.getClaveacceso());
		infoTributaria.setCodDoc(cabecera.getTipocomprobante().getIdentificador());
		infoTributaria.setEstab(TextoUtil.leftPadTexto(cabecera.getEstablecimiento().getCodigoestablecimiento(),3,"0"));
		infoTributaria.setPtoEmi(ComprobanteHelper.getPuntoEmision(cabecera.getNumdocumento()));
		infoTributaria.setSecuencial(cabecera.getSecuencial());
		infoTributaria.setDirMatriz(cabecera.getEstablecimiento().getEmpresa().getDireccionmatriz());
		
		return infoTributaria;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param infoadicionalList
	 * @return
	 */
	protected List<XmlCampoAdicional> getInfoAdicinal(List<Infoadicional> infoadicionalList){
		List<XmlCampoAdicional> infoAdicinalList = new ArrayList<>();
		for (Infoadicional infoadicional : infoadicionalList) {
			XmlCampoAdicional xmlCampoAdicional = new XmlCampoAdicional();
			xmlCampoAdicional.setNombre(infoadicional.getNombre());
			xmlCampoAdicional.setValue(infoadicional.getValor());
			infoAdicinalList.add(xmlCampoAdicional);
		}
		return infoAdicinalList;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param totalimpuestoList
	 * @return
	 */
	protected List<XmlTotalImpuesto> getTotalImpuesto(List<Totalimpuesto> totalimpuestoList){
		List<XmlTotalImpuesto> xmlTotalImpuestoList = new ArrayList<>();
		for (Totalimpuesto totalimpuesto : totalimpuestoList) {
			XmlTotalImpuesto xmlTotalImpuesto = new XmlTotalImpuesto();
			if(totalimpuesto.getIva()!=null) {
				xmlTotalImpuesto.setCodigo(totalimpuesto.getIva().getCodigo());
				xmlTotalImpuesto.setTarifa(totalimpuesto.getIva().getValor().doubleValue());
			}
			if(totalimpuesto.getIce()!=null) {
				xmlTotalImpuesto.setCodigo(totalimpuesto.getIce().getCodigo());
				xmlTotalImpuesto.setTarifa(totalimpuesto.getIce().getValor().doubleValue());
			}
			
			xmlTotalImpuesto.setCodigoPorcentaje(xmlTotalImpuesto.getCodigo());
			xmlTotalImpuesto.setBaseImponible(totalimpuesto.getBaseimponible().doubleValue());
			xmlTotalImpuesto.setValor(totalimpuesto.getValor().doubleValue());
			xmlTotalImpuestoList.add(xmlTotalImpuesto);
		}
		return xmlTotalImpuestoList;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param pagoList
	 * @return
	 */
	protected List<XmlPago> getPago(List<Pago> pagoList){
		List<XmlPago> xmlPagoList = new ArrayList<>();
		for (Pago pago : pagoList) {
			XmlPago xmlPago = new XmlPago();
			xmlPago.setFormaPago(pago.getTipopago().getNombre());
			xmlPago.setTotal(pago.getTotal().doubleValue());
			xmlPago.setPlazo(pago.getPlazo().doubleValue());
			xmlPago.setUnidadTiempo(pago.getUnidadtiempo());
			xmlPagoList.add(xmlPago);
		}
		return xmlPagoList;
	}

	/**
	 * @return the cabeceraServicio
	 */
	public CabeceraServicio getCabeceraServicio() {
		return cabeceraServicio;
	}

	/**
	 * @param cabeceraServicio the cabeceraServicio to set
	 */
	public void setCabeceraServicio(CabeceraServicio cabeceraServicio) {
		this.cabeceraServicio = cabeceraServicio;
	}

	/**
	 * @return the detalleServicio
	 */
	public DetalleServicio getDetalleServicio() {
		return detalleServicio;
	}

	/**
	 * @param detalleServicio the detalleServicio to set
	 */
	public void setDetalleServicio(DetalleServicio detalleServicio) {
		this.detalleServicio = detalleServicio;
	}

	/**
	 * @return the detalleimpuestoServicio
	 */
	public DetalleimpuestoServicio getDetalleimpuestoServicio() {
		return detalleimpuestoServicio;
	}

	/**
	 * @param detalleimpuestoServicio the detalleimpuestoServicio to set
	 */
	public void setDetalleimpuestoServicio(DetalleimpuestoServicio detalleimpuestoServicio) {
		this.detalleimpuestoServicio = detalleimpuestoServicio;
	}

	/**
	 * @return the pagoServicio
	 */
	public PagoServicio getPagoServicio() {
		return pagoServicio;
	}

	/**
	 * @param pagoServicio the pagoServicio to set
	 */
	public void setPagoServicio(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	/**
	 * @return the infoadicionalServicio
	 */
	public InfoadicionalServicio getInfoadicionalServicio() {
		return infoadicionalServicio;
	}

	/**
	 * @param infoadicionalServicio the infoadicionalServicio to set
	 */
	public void setInfoadicionalServicio(InfoadicionalServicio infoadicionalServicio) {
		this.infoadicionalServicio = infoadicionalServicio;
	}

	/**
	 * @return the totalimpuestoServicio
	 */
	public TotalimpuestoServicio getTotalimpuestoServicio() {
		return totalimpuestoServicio;
	}

	/**
	 * @param totalimpuestoServicio the totalimpuestoServicio to set
	 */
	public void setTotalimpuestoServicio(TotalimpuestoServicio totalimpuestoServicio) {
		this.totalimpuestoServicio = totalimpuestoServicio;
	}

	/**
	 * @return the kardexProductoServicio
	 */
	public KardexProductoServicio getKardexProductoServicio() {
		return kardexProductoServicio;
	}

	/**
	 * @param kardexProductoServicio the kardexProductoServicio to set
	 */
	public void setKardexProductoServicio(KardexProductoServicio kardexProductoServicio) {
		this.kardexProductoServicio = kardexProductoServicio;
	}

	/**
	 * @return the productoServicio
	 */
	public ProductoServicio getProductoServicio() {
		return productoServicio;
	}

	/**
	 * @param productoServicio the productoServicio to set
	 */
	public void setProductoServicio(ProductoServicio productoServicio) {
		this.productoServicio = productoServicio;
	}

	/**
	 * @return the impuestoretencionServicio
	 */
	public ImpuestoretencionServicio getImpuestoretencionServicio() {
		return impuestoretencionServicio;
	}

	/**
	 * @param impuestoretencionServicio the impuestoretencionServicio to set
	 */
	public void setImpuestoretencionServicio(ImpuestoretencionServicio impuestoretencionServicio) {
		this.impuestoretencionServicio = impuestoretencionServicio;
	}

	/**
	 * @return the adquisicionServicio
	 */
	public AdquisicionServicio getAdquisicionServicio() {
		return adquisicionServicio;
	}

	/**
	 * @param adquisicionServicio the adquisicionServicio to set
	 */
	public void setAdquisicionServicio(AdquisicionServicio adquisicionServicio) {
		this.adquisicionServicio = adquisicionServicio;
	}

	/**
	 * @return the destinatarioServicio
	 */
	public DestinatarioServicio getDestinatarioServicio() {
		return destinatarioServicio;
	}

	/**
	 * @param destinatarioServicio the destinatarioServicio to set
	 */
	public void setDestinatarioServicio(DestinatarioServicio destinatarioServicio) {
		this.destinatarioServicio = destinatarioServicio;
	}

	/**
	 * @return the detalledestinatarioServicio
	 */
	public DetalledestinatarioServicio getDetalledestinatarioServicio() {
		return detalledestinatarioServicio;
	}

	/**
	 * @param detalledestinatarioServicio the detalledestinatarioServicio to set
	 */
	public void setDetalledestinatarioServicio(DetalledestinatarioServicio detalledestinatarioServicio) {
		this.detalledestinatarioServicio = detalledestinatarioServicio;
	}

	/**
	 * @return the motivoServicio
	 */
	public MotivoServicio getMotivoServicio() {
		return motivoServicio;
	}

	/**
	 * @param motivoServicio the motivoServicio to set
	 */
	public void setMotivoServicio(MotivoServicio motivoServicio) {
		this.motivoServicio = motivoServicio;
	}

	/**
	 * @return the establecimientoServicio
	 */
	public EstablecimientoServicio getEstablecimientoServicio() {
		return establecimientoServicio;
	}

	/**
	 * @param establecimientoServicio the establecimientoServicio to set
	 */
	public void setEstablecimientoServicio(EstablecimientoServicio establecimientoServicio) {
		this.establecimientoServicio = establecimientoServicio;
	}
	
}
