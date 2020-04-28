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
@Table(name = "totalimpuesto")
public class Totalimpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -292199319280308010L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtotalmpuesto", nullable = false, length = 40)
    private String idtotalmpuesto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "descuentoadicional", precision = 12, scale = 2)
    private BigDecimal descuentoadicional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "baseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseimponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
    @JoinColumn(name = "idice", referencedColumnName = "idice")
    @ManyToOne
    private Ice ice;
    @JoinColumn(name = "idiva", referencedColumnName = "idiva")
    @ManyToOne
    private Iva iva;

	/**
	 * 
	 */
	public Totalimpuesto() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtotalmpuesto != null ? idtotalmpuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Totalimpuesto)) {
            return false;
        }
        Totalimpuesto other = (Totalimpuesto) object;
        if ((this.idtotalmpuesto == null && other.idtotalmpuesto != null) || (this.idtotalmpuesto != null && !this.idtotalmpuesto.equals(other.idtotalmpuesto))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idtotalmpuesto
	 */
	public String getIdtotalmpuesto() {
		return idtotalmpuesto;
	}

	/**
	 * @param idtotalmpuesto the idtotalmpuesto to set
	 */
	public void setIdtotalmpuesto(String idtotalmpuesto) {
		this.idtotalmpuesto = idtotalmpuesto;
	}

	/**
	 * @return the descuentoadicional
	 */
	public BigDecimal getDescuentoadicional() {
		return descuentoadicional;
	}

	/**
	 * @param descuentoadicional the descuentoadicional to set
	 */
	public void setDescuentoadicional(BigDecimal descuentoadicional) {
		this.descuentoadicional = descuentoadicional;
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
	 * @return the cabecera
	 */
	public Cabecera getCabecera() {
		return cabecera;
	}

	/**
	 * @param cabecera the cabecera to set
	 */
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
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
