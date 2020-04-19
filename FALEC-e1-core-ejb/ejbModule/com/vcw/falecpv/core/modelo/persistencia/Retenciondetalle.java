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
@Table(name = "retenciondetalle")
public class Retenciondetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1627897961752013597L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idretenciondetalle", nullable = false, length = 40)
    private String idretenciondetalle;
	
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "baseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseimponible;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje", nullable = false, precision = 12, scale = 2)
    private BigDecimal porcentaje;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    
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
    
    @JoinColumn(name = "idretencion", referencedColumnName = "idretencion", nullable = false)
    @ManyToOne(optional = false)
    private Retencion retencion;
    
    @JoinColumn(name = "idretencionimpuestodet", referencedColumnName = "idretencionimpuestodet", nullable = false)
    @ManyToOne(optional = false)
    private Retencionimpuestodet retencionimpuestodet;

	/**
	 * 
	 */
	public Retenciondetalle() {
	}
	
	 @Override
    public int hashCode() {
        int hash = 0;
        hash += (idretenciondetalle != null ? idretenciondetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Retenciondetalle)) {
            return false;
        }
        Retenciondetalle other = (Retenciondetalle) object;
        if ((this.idretenciondetalle == null && other.idretenciondetalle != null) || (this.idretenciondetalle != null && !this.idretenciondetalle.equals(other.idretenciondetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idretenciondetalle
	 */
	public String getIdretenciondetalle() {
		return idretenciondetalle;
	}

	/**
	 * @param idretenciondetalle the idretenciondetalle to set
	 */
	public void setIdretenciondetalle(String idretenciondetalle) {
		this.idretenciondetalle = idretenciondetalle;
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
	 * @return the porcentaje
	 */
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
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
	 * @return the retencion
	 */
	public Retencion getRetencion() {
		return retencion;
	}

	/**
	 * @param retencion the retencion to set
	 */
	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	/**
	 * @return the retencionimpuestodet
	 */
	public Retencionimpuestodet getRetencionimpuestodet() {
		return retencionimpuestodet;
	}

	/**
	 * @param retencionimpuestodet the retencionimpuestodet to set
	 */
	public void setRetencionimpuestodet(Retencionimpuestodet retencionimpuestodet) {
		this.retencionimpuestodet = retencionimpuestodet;
	}

}
