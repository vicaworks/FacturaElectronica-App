/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "detalledestinatario")
public class Detalledestinatario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4497822048765056117L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "iddetalledestinatario", nullable = false, length = 40)
    private String iddetalledestinatario;
    @Size(max = 25)
    @Column(name = "codigointerno", length = 25)
    private String codigointerno;
    @Size(max = 25)
    @Column(name = "codigoadicional", length = 25)
    private String codigoadicional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;
    @JoinColumn(name = "iddestinatario", referencedColumnName = "iddestinatario", nullable = false)
    @ManyToOne(optional = false)
    private Destinatario destinatario;
    
    @Transient
    private Producto producto;

	/**
	 * 
	 */
	public Detalledestinatario() {
		
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetalledestinatario != null ? iddetalledestinatario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Detalledestinatario)) {
            return false;
        }
        Detalledestinatario other = (Detalledestinatario) object;
        if ((this.iddetalledestinatario == null && other.iddetalledestinatario != null) || (this.iddetalledestinatario != null && !this.iddetalledestinatario.equals(other.iddetalledestinatario))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the iddetalledestinatario
	 */
	public String getIddetalledestinatario() {
		return iddetalledestinatario;
	}

	/**
	 * @param iddetalledestinatario the iddetalledestinatario to set
	 */
	public void setIddetalledestinatario(String iddetalledestinatario) {
		this.iddetalledestinatario = iddetalledestinatario;
	}

	/**
	 * @return the codigointerno
	 */
	public String getCodigointerno() {
		return codigointerno;
	}

	/**
	 * @param codigointerno the codigointerno to set
	 */
	public void setCodigointerno(String codigointerno) {
		this.codigointerno = codigointerno;
	}

	/**
	 * @return the codigoadicional
	 */
	public String getCodigoadicional() {
		return codigoadicional;
	}

	/**
	 * @param codigoadicional the codigoadicional to set
	 */
	public void setCodigoadicional(String codigoadicional) {
		this.codigoadicional = codigoadicional;
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
	 * @return the destinatario
	 */
	public Destinatario getDestinatario() {
		return destinatario;
	}

	/**
	 * @param destinatario the destinatario to set
	 */
	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
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
