/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
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

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(catalog = "falecpv")
public class Transaccionconcepto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7354429880649179214L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(nullable = false, length = 40)
    private String idtransaccionconcepto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(nullable = false, length = 1)
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(nullable = false, length = 40)
    private String idusuario;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    @ManyToOne(optional = false)
    private Empresa idempresa;
    @JoinColumn(name = "idtransacciontipo", referencedColumnName = "idtransacciontipo", nullable = false)
    @ManyToOne(optional = false)
    private Transacciontipo idtransacciontipo;

	/**
	 * 
	 */
	public Transaccionconcepto() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccionconcepto != null ? idtransaccionconcepto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transaccionconcepto)) {
            return false;
        }
        Transaccionconcepto other = (Transaccionconcepto) object;
        if ((this.idtransaccionconcepto == null && other.idtransaccionconcepto != null) || (this.idtransaccionconcepto != null && !this.idtransaccionconcepto.equals(other.idtransaccionconcepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s, %s]", idtransaccionconcepto, nombre);
    }

	/**
	 * @return the idtransaccionconcepto
	 */
	public String getIdtransaccionconcepto() {
		return idtransaccionconcepto;
	}

	/**
	 * @param idtransaccionconcepto the idtransaccionconcepto to set
	 */
	public void setIdtransaccionconcepto(String idtransaccionconcepto) {
		this.idtransaccionconcepto = idtransaccionconcepto;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	 * @return the idempresa
	 */
	public Empresa getIdempresa() {
		return idempresa;
	}

	/**
	 * @param idempresa the idempresa to set
	 */
	public void setIdempresa(Empresa idempresa) {
		this.idempresa = idempresa;
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

}
