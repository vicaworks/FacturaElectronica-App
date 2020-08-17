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
@Table(catalog = "falecpv")
public class Transaccion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7682683313580166686L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(nullable = false, length = 40)
    private String idtransaccion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaemision;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valoringreso;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valoregreso;
    @Size(max = 500)
    @Column(length = 500)
    private String nota;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 20)
    @Column(length = 20)
    private String numdocumento;
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento idestablecimiento;
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor")
    @ManyToOne
    private Proveedor idproveedor;
    @JoinColumn(name = "idtransaccionconcepto", referencedColumnName = "idtransaccionconcepto")
    @ManyToOne
    private Transaccionconcepto idtransaccionconcepto;
    @JoinColumn(name = "idtransacciontipo", referencedColumnName = "idtransacciontipo", nullable = false)
    @ManyToOne(optional = false)
    private Transacciontipo idtransacciontipo;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario idusuario;

	/**
	 * 
	 */
	public Transaccion() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccion != null ? idtransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idtransaccion == null && other.idtransaccion != null) || (this.idtransaccion != null && !this.idtransaccion.equals(other.idtransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtransaccion
	 */
	public String getIdtransaccion() {
		return idtransaccion;
	}

	/**
	 * @param idtransaccion the idtransaccion to set
	 */
	public void setIdtransaccion(String idtransaccion) {
		this.idtransaccion = idtransaccion;
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
	 * @return the valoringreso
	 */
	public BigDecimal getValoringreso() {
		return valoringreso;
	}

	/**
	 * @param valoringreso the valoringreso to set
	 */
	public void setValoringreso(BigDecimal valoringreso) {
		this.valoringreso = valoringreso;
	}

	/**
	 * @return the valoregreso
	 */
	public BigDecimal getValoregreso() {
		return valoregreso;
	}

	/**
	 * @param valoregreso the valoregreso to set
	 */
	public void setValoregreso(BigDecimal valoregreso) {
		this.valoregreso = valoregreso;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
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
	 * @return the numdocumento
	 */
	public String getNumdocumento() {
		return numdocumento;
	}

	/**
	 * @param numdocumento the numdocumento to set
	 */
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}

	/**
	 * @return the idestablecimiento
	 */
	public Establecimiento getIdestablecimiento() {
		return idestablecimiento;
	}

	/**
	 * @param idestablecimiento the idestablecimiento to set
	 */
	public void setIdestablecimiento(Establecimiento idestablecimiento) {
		this.idestablecimiento = idestablecimiento;
	}

	/**
	 * @return the idproveedor
	 */
	public Proveedor getIdproveedor() {
		return idproveedor;
	}

	/**
	 * @param idproveedor the idproveedor to set
	 */
	public void setIdproveedor(Proveedor idproveedor) {
		this.idproveedor = idproveedor;
	}

	/**
	 * @return the idtransaccionconcepto
	 */
	public Transaccionconcepto getIdtransaccionconcepto() {
		return idtransaccionconcepto;
	}

	/**
	 * @param idtransaccionconcepto the idtransaccionconcepto to set
	 */
	public void setIdtransaccionconcepto(Transaccionconcepto idtransaccionconcepto) {
		this.idtransaccionconcepto = idtransaccionconcepto;
	}

	/**
	 * @return the idtransacciontipo
	 */
	public Transacciontipo getIdtransacciontipo() {
		return idtransacciontipo;
	}

	/**
	 * @param idtransacciontipo the idtransacciontipo to set
	 */
	public void setIdtransacciontipo(Transacciontipo idtransacciontipo) {
		this.idtransacciontipo = idtransacciontipo;
	}

	/**
	 * @return the idusuario
	 */
	public Usuario getIdusuario() {
		return idusuario;
	}

	/**
	 * @param idusuario the idusuario to set
	 */
	public void setIdusuario(Usuario idusuario) {
		this.idusuario = idusuario;
	}

}
