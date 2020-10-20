/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dom4j.DocumentException;

import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.PojoUtil;
import com.servitec.common.util.XmlCommonsUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "comprobanterecibido")
public class Comprobanterecibido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7808957690342901851L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idcomprobanterecibido", nullable = false, length = 40)
    private String idcomprobanterecibido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "comprobante", nullable = false, length = 60)
    private String comprobante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "serie_comprobante", nullable = false, length = 17)
    private String serieComprobante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "ruc_emisor", nullable = false, length = 13)
    private String rucEmisor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "razon_social_emisor", nullable = false, length = 300)
    private String razonSocialEmisor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_autorizacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_emision", nullable = false, length = 20)
    private String tipoEmision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificacion_receptor", nullable = false, length = 20)
    private String identificacionReceptor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 49)
    @Column(name = "clave_acceso", nullable = false, length = 49)
    private String claveAcceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 49)
    @Column(name = "numero_autorizacion", nullable = false, length = 49)
    private String numeroAutorizacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "importe_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal importeTotal;
    @Size(max = 2147483647)
    @Column(name = "valor_xml", length = 2147483647)
    private String valorXml;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalsinimpuestos", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalsinimpuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalconimpuestos", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalconimpuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaldescuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal totaldescuento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaliva", nullable = false, precision = 12, scale = 2)
    private BigDecimal totaliva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalice", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalbaseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalbaseimponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalretencion", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalretencion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalrenta", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalrenta;
    
    @Basic(optional = true)
    @Size(min = 1, max = 400)
    @Column(name = "motivo", nullable = true, length = 400)
    private String motivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = false)
    @ManyToOne(optional = false)
    private Tipocomprobante tipocomprobante;

	/**
	 * 
	 */
	public Comprobanterecibido() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcomprobanterecibido != null ? idcomprobanterecibido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comprobanterecibido)) {
            return false;
        }
        Comprobanterecibido other = (Comprobanterecibido) object;
        if ((this.idcomprobanterecibido == null && other.idcomprobanterecibido != null) || (this.idcomprobanterecibido != null && !this.idcomprobanterecibido.equals(other.idcomprobanterecibido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }
    
    
    public String getPlaca() throws DocumentException {
    	if(valorXml!=null) {
    		return XmlCommonsUtil.valorXpath(getValorXml(), "//placa");
    	}
    	
    	return "";
    }
    
    public String getDireccionPartida() throws DocumentException {
    	if(valorXml!=null) {
    		return XmlCommonsUtil.valorXpath(getValorXml(), "//dirPartida");
    	}
    	
    	return "";
    }
    
    public Date getFechaPartida() throws ParseException, DocumentException {
    	if(valorXml!=null) {
    		return FechaUtil.formatoFecha(XmlCommonsUtil.valorXpath(getValorXml(), "//fechaIniTransporte"));
    	}
    	return null;
    }
    
    public Date getFechaLlegada() throws ParseException, DocumentException {
    	if(valorXml!=null) {
    		return FechaUtil.formatoFecha(XmlCommonsUtil.valorXpath(getValorXml(), "//fechaFinTransporte"));
    	}
    	return null;
    }
    
    
    
    
	/**
	 * @return the idcomprobanterecibido
	 */
	public String getIdcomprobanterecibido() {
		return idcomprobanterecibido;
	}

	/**
	 * @param idcomprobanterecibido the idcomprobanterecibido to set
	 */
	public void setIdcomprobanterecibido(String idcomprobanterecibido) {
		this.idcomprobanterecibido = idcomprobanterecibido;
	}

	/**
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the serieComprobante
	 */
	public String getSerieComprobante() {
		return serieComprobante;
	}

	/**
	 * @param serieComprobante the serieComprobante to set
	 */
	public void setSerieComprobante(String serieComprobante) {
		this.serieComprobante = serieComprobante;
	}

	/**
	 * @return the rucEmisor
	 */
	public String getRucEmisor() {
		return rucEmisor;
	}

	/**
	 * @param rucEmisor the rucEmisor to set
	 */
	public void setRucEmisor(String rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	/**
	 * @return the razonSocialEmisor
	 */
	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}

	/**
	 * @param razonSocialEmisor the razonSocialEmisor to set
	 */
	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}

	/**
	 * @return the fechaEmision
	 */
	public Date getFechaEmision() {
		return fechaEmision;
	}

	/**
	 * @param fechaEmision the fechaEmision to set
	 */
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	/**
	 * @return the fechaAutorizacion
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the tipoEmision
	 */
	public String getTipoEmision() {
		return tipoEmision;
	}

	/**
	 * @param tipoEmision the tipoEmision to set
	 */
	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	/**
	 * @return the identificacionReceptor
	 */
	public String getIdentificacionReceptor() {
		return identificacionReceptor;
	}

	/**
	 * @param identificacionReceptor the identificacionReceptor to set
	 */
	public void setIdentificacionReceptor(String identificacionReceptor) {
		this.identificacionReceptor = identificacionReceptor;
	}

	/**
	 * @return the claveAcceso
	 */
	public String getClaveAcceso() {
		return claveAcceso;
	}

	/**
	 * @param claveAcceso the claveAcceso to set
	 */
	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return the importeTotal
	 */
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}

	/**
	 * @param importeTotal the importeTotal to set
	 */
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}

	/**
	 * @return the valorXml
	 */
	public String getValorXml() {
		return valorXml;
	}

	/**
	 * @param valorXml the valorXml to set
	 */
	public void setValorXml(String valorXml) {
		this.valorXml = valorXml;
	}

	/**
	 * @return the totalsinimpuestos
	 */
	public BigDecimal getTotalsinimpuestos() {
		return totalsinimpuestos;
	}

	/**
	 * @param totalsinimpuestos the totalsinimpuestos to set
	 */
	public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
		this.totalsinimpuestos = totalsinimpuestos;
	}

	/**
	 * @return the totalconimpuestos
	 */
	public BigDecimal getTotalconimpuestos() {
		return totalconimpuestos;
	}

	/**
	 * @param totalconimpuestos the totalconimpuestos to set
	 */
	public void setTotalconimpuestos(BigDecimal totalconimpuestos) {
		this.totalconimpuestos = totalconimpuestos;
	}

	/**
	 * @return the totaldescuento
	 */
	public BigDecimal getTotaldescuento() {
		return totaldescuento;
	}

	/**
	 * @param totaldescuento the totaldescuento to set
	 */
	public void setTotaldescuento(BigDecimal totaldescuento) {
		this.totaldescuento = totaldescuento;
	}

	/**
	 * @return the totaliva
	 */
	public BigDecimal getTotaliva() {
		return totaliva;
	}

	/**
	 * @param totaliva the totaliva to set
	 */
	public void setTotaliva(BigDecimal totaliva) {
		this.totaliva = totaliva;
	}

	/**
	 * @return the totalice
	 */
	public BigDecimal getTotalice() {
		return totalice;
	}

	/**
	 * @param totalice the totalice to set
	 */
	public void setTotalice(BigDecimal totalice) {
		this.totalice = totalice;
	}

	/**
	 * @return the totalbaseimponible
	 */
	public BigDecimal getTotalbaseimponible() {
		return totalbaseimponible;
	}

	/**
	 * @param totalbaseimponible the totalbaseimponible to set
	 */
	public void setTotalbaseimponible(BigDecimal totalbaseimponible) {
		this.totalbaseimponible = totalbaseimponible;
	}

	/**
	 * @return the totalretencion
	 */
	public BigDecimal getTotalretencion() {
		return totalretencion;
	}

	/**
	 * @param totalretencion the totalretencion to set
	 */
	public void setTotalretencion(BigDecimal totalretencion) {
		this.totalretencion = totalretencion;
	}

	/**
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the totalrenta
	 */
	public BigDecimal getTotalrenta() {
		return totalrenta;
	}

	/**
	 * @param totalrenta the totalrenta to set
	 */
	public void setTotalrenta(BigDecimal totalrenta) {
		this.totalrenta = totalrenta;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the idusuario
	 */
	public String getIdusuario() {
		return idusuario;
	}

	/**
	 * @param idusuario the idusuario to set
	 */
	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

}
