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

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "kardexproducto")
public class KardexProducto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8099918957366864303L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idkardexproducto", nullable = false, length = 40)
    private String idkardexproducto;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tiporegistro", nullable = false, length = 1)
    private String tiporegistro;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecharegistro", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecharegistro;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "costounitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costounitario;
    
    @Column(name = "fechavencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechavencimiento;
    
    @Column(name = "fechafabricacion")
    @Temporal(TemporalType.DATE)
    private Date fechafabricacion;
    
    @Column(name = "fechacompra")
    @Temporal(TemporalType.DATE)
    private Date fechacompra;
    
    @Size(max = 800)
    @Column(name = "observacion", length = 800)
    private String observacion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "costototal", nullable = false, precision = 12, scale = 2)
    private BigDecimal costototal;
    
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
    
    @JoinColumn(name = "idproducto", referencedColumnName = "idproducto", nullable = false,insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

	/**
	 * 
	 */
	public KardexProducto() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idkardexproducto != null ? idkardexproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KardexProducto)) {
            return false;
        }
        KardexProducto other = (KardexProducto) object;
        if ((this.idkardexproducto == null && other.idkardexproducto != null) || (this.idkardexproducto != null && !this.idkardexproducto.equals(other.idkardexproducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idkardexproducto
	 */
	public String getIdkardexproducto() {
		return idkardexproducto;
	}

	/**
	 * @param idkardexproducto the idkardexproducto to set
	 */
	public void setIdkardexproducto(String idkardexproducto) {
		this.idkardexproducto = idkardexproducto;
	}

	/**
	 * @return the tiporegistro
	 */
	public String getTiporegistro() {
		return tiporegistro;
	}

	/**
	 * @param tiporegistro the tiporegistro to set
	 */
	public void setTiporegistro(String tiporegistro) {
		this.tiporegistro = tiporegistro;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the fecharegistro
	 */
	public Date getFecharegistro() {
		return fecharegistro;
	}

	/**
	 * @param fecharegistro the fecharegistro to set
	 */
	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}

	/**
	 * @return the costounitario
	 */
	public BigDecimal getCostounitario() {
		return costounitario;
	}

	/**
	 * @param costounitario the costounitario to set
	 */
	public void setCostounitario(BigDecimal costounitario) {
		this.costounitario = costounitario;
	}

	/**
	 * @return the fechavencimiento
	 */
	public Date getFechavencimiento() {
		return fechavencimiento;
	}

	/**
	 * @param fechavencimiento the fechavencimiento to set
	 */
	public void setFechavencimiento(Date fechavencimiento) {
		this.fechavencimiento = fechavencimiento;
	}

	/**
	 * @return the fechafabricacion
	 */
	public Date getFechafabricacion() {
		return fechafabricacion;
	}

	/**
	 * @param fechafabricacion the fechafabricacion to set
	 */
	public void setFechafabricacion(Date fechafabricacion) {
		this.fechafabricacion = fechafabricacion;
	}

	/**
	 * @return the fechacompra
	 */
	public Date getFechacompra() {
		return fechacompra;
	}

	/**
	 * @param fechacompra the fechacompra to set
	 */
	public void setFechacompra(Date fechacompra) {
		this.fechacompra = fechacompra;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return the costototal
	 */
	public BigDecimal getCostototal() {
		return costototal;
	}

	/**
	 * @param costototal the costototal to set
	 */
	public void setCostototal(BigDecimal costototal) {
		this.costototal = costototal;
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

		

}
