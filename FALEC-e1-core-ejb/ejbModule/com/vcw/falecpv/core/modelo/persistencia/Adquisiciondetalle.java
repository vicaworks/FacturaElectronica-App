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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "adquisiciondetalle")
public class Adquisiciondetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5905930118455543641L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idadquisiciondetalle", nullable = false, length = 40)
    private String idadquisiciondetalle;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false)
    private BigDecimal cantidad;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciounitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciounitario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototal", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciototal;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal descuento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valoriva", nullable = false, precision = 12, scale = 2)
    private BigDecimal valoriva;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorice", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorice;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototalsinimpuesto", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciototalsinimpuesto;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idadquisicion", referencedColumnName = "idadquisicion", nullable = false)
    private Adquisicion adquisicion;
    
    @JoinColumn(name = "idiva", referencedColumnName = "idiva", nullable = false)
    @ManyToOne(optional = false)
    private Iva iva;
    
    @JoinColumn(name = "idice", referencedColumnName = "idice", nullable = true)
    @ManyToOne(optional = true)
    private Ice ice;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idproducto", referencedColumnName = "idproducto", nullable = true)
    private Producto producto;
    
    
    @Transient
    private BigDecimal porcentajeDescuento;
    
    @Transient
    private BigDecimal precioUntarioCalculado;
    
	/**
	 * 
	 */
	public Adquisiciondetalle() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idadquisiciondetalle != null ? idadquisiciondetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Adquisiciondetalle)) {
            return false;
        }
        Adquisiciondetalle other = (Adquisiciondetalle) object;
        if ((this.idadquisiciondetalle == null && other.idadquisiciondetalle != null) || (this.idadquisiciondetalle != null && !this.idadquisiciondetalle.equals(other.idadquisiciondetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idadquisiciondetalle
	 */
	public String getIdadquisiciondetalle() {
		return idadquisiciondetalle;
	}

	/**
	 * @param idadquisiciondetalle the idadquisiciondetalle to set
	 */
	public void setIdadquisiciondetalle(String idadquisiciondetalle) {
		this.idadquisiciondetalle = idadquisiciondetalle;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the preciounitario
	 */
	public BigDecimal getPreciounitario() {
		return preciounitario;
	}

	/**
	 * @param preciounitario the preciounitario to set
	 */
	public void setPreciounitario(BigDecimal preciounitario) {
		this.preciounitario = preciounitario;
	}

	/**
	 * @return the preciototal
	 */
	public BigDecimal getPreciototal() {
		return preciototal;
	}

	/**
	 * @param preciototal the preciototal to set
	 */
	public void setPreciototal(BigDecimal preciototal) {
		this.preciototal = preciototal;
	}

	/**
	 * @return the descuento
	 */
	public BigDecimal getDescuento() {
		return descuento;
	}

	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
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
	 * @return the iva
	 */
	public Iva getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(Iva iva) {
		this.iva = iva;
	}

	/**
	 * @return the producto
	 */
	public Producto getProducto() {
		return producto;
	}

	/**
	 * @param producto the producto to set
	 */
	public void setProducto(Producto producto) {
		this.producto = producto;
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
	 * @return the porcentajeDescuento
	 */
	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the precioUntarioCalculado
	 */
	public BigDecimal getPrecioUntarioCalculado() {
		return precioUntarioCalculado;
	}

	/**
	 * @param precioUntarioCalculado the precioUntarioCalculado to set
	 */
	public void setPrecioUntarioCalculado(BigDecimal precioUntarioCalculado) {
		this.precioUntarioCalculado = precioUntarioCalculado;
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
	 * @return the valoriva
	 */
	public BigDecimal getValoriva() {
		return valoriva;
	}

	/**
	 * @param valoriva the valoriva to set
	 */
	public void setValoriva(BigDecimal valoriva) {
		this.valoriva = valoriva;
	}

	/**
	 * @return the valorice
	 */
	public BigDecimal getValorice() {
		return valorice;
	}

	/**
	 * @param valorice the valorice to set
	 */
	public void setValorice(BigDecimal valorice) {
		this.valorice = valorice;
	}

	/**
	 * @return the ice
	 */
	public Ice getIce() {
		return ice;
	}

	/**
	 * @param ice the ice to set
	 */
	public void setIce(Ice ice) {
		this.ice = ice;
	}

	/**
	 * @return the cantidad
	 */
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the preciototalsinimpuesto
	 */
	public BigDecimal getPreciototalsinimpuesto() {
		return preciototalsinimpuesto;
	}

	/**
	 * @param preciototalsinimpuesto the preciototalsinimpuesto to set
	 */
	public void setPreciototalsinimpuesto(BigDecimal preciototalsinimpuesto) {
		this.preciototalsinimpuesto = preciototalsinimpuesto;
	}

}
