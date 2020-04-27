/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "retencion")
public class Retencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1511695644727338761L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idretencion", nullable = false, length = 40)
    private String idretencion;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaemision", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaemision;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "anio", nullable = false, length = 2147483647)
    private String anio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "mes", nullable = false)
    private int mes;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numcomprobante", nullable = false, length = 50)
    private String numcomprobante;
    
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "numautorizacion", nullable = true, length = 150)
    private String numautorizacion;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numfactura", nullable = false, length = 50)
    private String numfactura;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @JoinColumn(name = "idadquisicion", referencedColumnName = "idadquisicion")
    @ManyToOne
    private Adquisicion adquisicion;
    
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento establecimiento;
    
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor", nullable = false)
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = false)
    @ManyToOne(optional = false)
    private Tipocomprobante tipocomprobante;
    

	/**
	 * 
	 */
	public Retencion() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idretencion != null ? idretencion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Retencion)) {
            return false;
        }
        Retencion other = (Retencion) object;
        if ((this.idretencion == null && other.idretencion != null) || (this.idretencion != null && !this.idretencion.equals(other.idretencion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idretencion
	 */
	public String getIdretencion() {
		return idretencion;
	}

	/**
	 * @param idretencion the idretencion to set
	 */
	public void setIdretencion(String idretencion) {
		this.idretencion = idretencion;
	}

	/**
	 * @return the fechaemision
	 */
	public Date getFechaemision() {
		return fechaemision;
	}

	/**
	 * @param fechaemision the fechaemision to set
	 */
	public void setFechaemision(Date fechaemision) {
		this.fechaemision = fechaemision;
	}

	/**
	 * @return the anio
	 */
	public String getAnio() {
		return anio;
	}

	/**
	 * @param anio the anio to set
	 */
	public void setAnio(String anio) {
		this.anio = anio;
	}

	/**
	 * @return the mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * @param mes the mes to set
	 */
	public void setMes(int mes) {
		this.mes = mes;
	}

	/**
	 * @return the numcomprobante
	 */
	public String getNumcomprobante() {
		return numcomprobante;
	}

	/**
	 * @param numcomprobante the numcomprobante to set
	 */
	public void setNumcomprobante(String numcomprobante) {
		this.numcomprobante = numcomprobante;
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

	/**
	 * @return the adquisicion
	 */
	public Adquisicion getAdquisicion() {
		return adquisicion;
	}

	/**
	 * @param adquisicion the adquisicion to set
	 */
	public void setAdquisicion(Adquisicion adquisicion) {
		this.adquisicion = adquisicion;
	}

	/**
	 * @return the establecimiento
	 */
	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	/**
	 * @param establecimiento the establecimiento to set
	 */
	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	
	public String getEstadoStyle() {
		
		return ComprobanteEstadoEnum.getStyleEstado(estado);
		
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the numfactura
	 */
	public String getNumfactura() {
		return numfactura;
	}

	/**
	 * @param numfactura the numfactura to set
	 */
	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
	}

	/**
	 * @return the numautorizacion
	 */
	public String getNumautorizacion() {
		return numautorizacion;
	}

	/**
	 * @param numautorizacion the numautorizacion to set
	 */
	public void setNumautorizacion(String numautorizacion) {
		this.numautorizacion = numautorizacion;
	}

}
