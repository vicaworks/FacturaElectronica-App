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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "detalleimpuesto")
public class Detalleimpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6621346570323211173L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "iddetalleimpuesto", nullable = false, length = 40)
    private String iddetalleimpuesto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "baseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseimponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @Column(name = "tarifa", precision = 12, scale = 2)
    private BigDecimal tarifa;
	
    @JoinColumn(name = "iddetalle", referencedColumnName = "iddetalle", nullable = false)
    @ManyToOne(optional = false)
    private Detalle detalle;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idice", referencedColumnName = "idice",nullable = true)
    private Ice ice;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idiva", referencedColumnName = "idiva", nullable = true)
    private Iva iva;

	/**
	 * 
	 */
	public Detalleimpuesto() {
		
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetalleimpuesto != null ? iddetalleimpuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Detalleimpuesto)) {
            return false;
        }
        Detalleimpuesto other = (Detalleimpuesto) object;
        if ((this.iddetalleimpuesto == null && other.iddetalleimpuesto != null) || (this.iddetalleimpuesto != null && !this.iddetalleimpuesto.equals(other.iddetalleimpuesto))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the iddetalleimpuesto
	 */
	public String getIddetalleimpuesto() {
		return iddetalleimpuesto;
	}

	/**
	 * @param iddetalleimpuesto the iddetalleimpuesto to set
	 */
	public void setIddetalleimpuesto(String iddetalleimpuesto) {
		this.iddetalleimpuesto = iddetalleimpuesto;
	}

	/**
	 * @return the baseimponible
	 */
	public BigDecimal getBaseimponible() {
		return baseimponible;
	}

	/**
	 * @param baseimponible the baseimponible to set
	 */
	public void setBaseimponible(BigDecimal baseimponible) {
		this.baseimponible = baseimponible;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the tarifa
	 */
	public BigDecimal getTarifa() {
		return tarifa;
	}

	/**
	 * @param tarifa the tarifa to set
	 */
	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
	}

	/**
	 * @return the detalle
	 */
	public Detalle getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(Detalle detalle) {
		this.detalle = detalle;
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

}
