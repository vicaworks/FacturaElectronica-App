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
@Table(name = "transaccion")
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
    private Establecimiento establecimiento;
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor")
    @ManyToOne
    private Proveedor proveedor;
    @JoinColumn(name = "idtransaccionconcepto", referencedColumnName = "idtransaccionconcepto")
    @ManyToOne
    private Transaccionconcepto transaccionconcepto;
    @JoinColumn(name = "idtransacciontipo", referencedColumnName = "idtransacciontipo", nullable = false)
    @ManyToOne(optional = false)
    private Transacciontipo transacciontipo;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuario;
    
    @Transient
    private String tipoTransaccion;

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
	 * @return the transacciontipo
	 */
	public Transacciontipo getTransacciontipo() {
		return transacciontipo;
	}

	/**
	 * @param transacciontipo the transacciontipo to set
	 */
	public void setTransacciontipo(Transacciontipo transacciontipo) {
		this.transacciontipo = transacciontipo;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the transaccionconcepto
	 */
	public Transaccionconcepto getTransaccionconcepto() {
		return transaccionconcepto;
	}

	/**
	 * @param transaccionconcepto the transaccionconcepto to set
	 */
	public void setTransaccionconcepto(Transaccionconcepto transaccionconcepto) {
		this.transaccionconcepto = transaccionconcepto;
	}

	/**
	 * @return the tipoTransaccion
	 */
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	/**
	 * @param tipoTransaccion the tipoTransaccion to set
	 */
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}
