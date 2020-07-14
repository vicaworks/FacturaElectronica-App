/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

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
	public abstract String generarFacade(String idDocumento,String idEstablecimeinto,GenTipoDocumentoEnum tipoDocumento)throws DaoException;

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
