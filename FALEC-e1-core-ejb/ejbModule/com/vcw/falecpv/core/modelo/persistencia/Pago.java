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
@Table(name = "pago")
public class Pago implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4661781844746631777L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idpago", nullable = false, length = 40)
    private String idpago;
	 // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazo", nullable = false, precision = 12, scale = 2)
    private BigDecimal plazo;
    @Size(max = 10)
    @Column(name = "unidadtiempo", length = 10)
    private String unidadtiempo;
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
    @JoinColumn(name = "idtipopago", referencedColumnName = "idtipopago", nullable = false)
    @ManyToOne(optional = false)
    private Tipopago tipopago;

	public Pago() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idpago != null ? idpago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idpago == null && other.idpago != null) || (this.idpago != null && !this.idpago.equals(other.idpago))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idpago
	 */
	public String getIdpago() {
		return idpago;
	}

	/**
	 * @param idpago the idpago to set
	 */
	public void setIdpago(String idpago) {
		this.idpago = idpago;
	}

	/**
	 * @return the unidadtiempo
	 */
	public String getUnidadtiempo() {
		return unidadtiempo;
	}

	/**
	 * @param unidadtiempo the unidadtiempo to set
	 */
	public void setUnidadtiempo(String unidadtiempo) {
		this.unidadtiempo = unidadtiempo;
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
	 * @return the tipopago
	 */
	public Tipopago getTipopago() {
		return tipopago;
	}

	/**
	 * @param tipopago the tipopago to set
	 */
	public void setTipopago(Tipopago tipopago) {
		this.tipopago = tipopago;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the plazo
	 */
	public BigDecimal getPlazo() {
		return plazo;
	}

	/**
	 * @param plazo the plazo to set
	 */
	public void setPlazo(BigDecimal plazo) {
		this.plazo = plazo;
	}

}
