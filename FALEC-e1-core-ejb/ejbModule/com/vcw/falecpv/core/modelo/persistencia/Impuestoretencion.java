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
@Table(name = "impuestoretencion")
public class Impuestoretencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4407977212957062273L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idimpuestoretencion", nullable = false, length = 40)
    private String idimpuestoretencion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "codigo", nullable = false, length = 1)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigoretencion", nullable = false, length = 5)
    private String codigoretencion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "baseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseimponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajeretener", nullable = false, precision = 12, scale = 2)
    private BigDecimal porcentajeretener;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorretenido", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorretenido;
    @Size(max = 2)
    @Column(name = "coddocsustento", length = 2)
    private String coddocsustento;
    @Size(max = 15)
    @Column(name = "numdocsustento", length = 15)
    private String numdocsustento;
    @Column(name = "fechaemisiondocsustento")
    @Temporal(TemporalType.DATE)
    private Date fechaemisiondocsustento;
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idretencionimpuestodet", referencedColumnName = "idretencionimpuestodet", nullable = false)
    private Retencionimpuestodet retencionimpuestodet;
	
	/**
	 * 
	 */
	public Impuestoretencion() {
		
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idimpuestoretencion != null ? idimpuestoretencion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Impuestoretencion)) {
            return false;
        }
        Impuestoretencion other = (Impuestoretencion) object;
        if ((this.idimpuestoretencion == null && other.idimpuestoretencion != null) || (this.idimpuestoretencion != null && !this.idimpuestoretencion.equals(other.idimpuestoretencion))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idimpuestoretencion
	 */
	public String getIdimpuestoretencion() {
		return idimpuestoretencion;
	}

	/**
	 * @param idimpuestoretencion the idimpuestoretencion to set
	 */
	public void setIdimpuestoretencion(String idimpuestoretencion) {
		this.idimpuestoretencion = idimpuestoretencion;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoretencion
	 */
	public String getCodigoretencion() {
		return codigoretencion;
	}

	/**
	 * @param codigoretencion the codigoretencion to set
	 */
	public void setCodigoretencion(String codigoretencion) {
		this.codigoretencion = codigoretencion;
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
	 * @return the porcentajeretener
	 */
	public BigDecimal getPorcentajeretener() {
		return porcentajeretener;
	}

	/**
	 * @param porcentajeretener the porcentajeretener to set
	 */
	public void setPorcentajeretener(BigDecimal porcentajeretener) {
		this.porcentajeretener = porcentajeretener;
	}

	/**
	 * @return the valorretenido
	 */
	public BigDecimal getValorretenido() {
		return valorretenido;
	}

	/**
	 * @param valorretenido the valorretenido to set
	 */
	public void setValorretenido(BigDecimal valorretenido) {
		this.valorretenido = valorretenido;
	}

	/**
	 * @return the coddocsustento
	 */
	public String getCoddocsustento() {
		return coddocsustento;
	}

	/**
	 * @param coddocsustento the coddocsustento to set
	 */
	public void setCoddocsustento(String coddocsustento) {
		this.coddocsustento = coddocsustento;
	}

	/**
	 * @return the numdocsustento
	 */
	public String getNumdocsustento() {
		return numdocsustento;
	}

	/**
	 * @param numdocsustento the numdocsustento to set
	 */
	public void setNumdocsustento(String numdocsustento) {
		this.numdocsustento = numdocsustento;
	}

	/**
	 * @return the fechaemisiondocsustento
	 */
	public Date getFechaemisiondocsustento() {
		return fechaemisiondocsustento;
	}

	/**
	 * @param fechaemisiondocsustento the fechaemisiondocsustento to set
	 */
	public void setFechaemisiondocsustento(Date fechaemisiondocsustento) {
		this.fechaemisiondocsustento = fechaemisiondocsustento;
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
