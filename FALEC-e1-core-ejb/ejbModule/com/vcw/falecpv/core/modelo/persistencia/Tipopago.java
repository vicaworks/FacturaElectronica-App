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
@Table(name = "tipopago")
public class Tipopago implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919157765122914626L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtipopago", nullable = false, length = 40)
    private String idtipopago;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "subdetalle", nullable = false, length = 1)
    private String subdetalle;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "formulario", nullable = false, length = 100)
    private String formulario;
    
    @Size(max = 40)
    @Column(name = "idusuario", length = 40)
    private String idusuario;
    
    @Size(max = 4)
    @Column(name = "codinterno", length = 4)
    private String codinterno;
    
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
//    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
//    @ManyToOne(optional = false)
//    private Empresa empresa;

	/**
	 * 
	 */
	public Tipopago() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipopago != null ? idtipopago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tipopago)) {
            return false;
        }
        Tipopago other = (Tipopago) object;
        if ((this.idtipopago == null && other.idtipopago != null) || (this.idtipopago != null && !this.idtipopago.equals(other.idtipopago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",nombre , idtipopago);
    }

	/**
	 * @return the idtipopago
	 */
	public String getIdtipopago() {
		return idtipopago;
	}

	/**
	 * @param idtipopago the idtipopago to set
	 */
	public void setIdtipopago(String idtipopago) {
		this.idtipopago = idtipopago;
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
	 * @return the subdetalle
	 */
	public String getSubdetalle() {
		return subdetalle;
	}

	/**
	 * @param subdetalle the subdetalle to set
	 */
	public void setSubdetalle(String subdetalle) {
		this.subdetalle = subdetalle;
	}

	/**
	 * @return the formulario
	 */
	public String getFormulario() {
		return formulario;
	}

	/**
	 * @param formulario the formulario to set
	 */
	public void setFormulario(String formulario) {
		this.formulario = formulario;
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
	 * @return the codinterno
	 */
	public String getCodinterno() {
		return codinterno;
	}

	/**
	 * @param codinterno the codinterno to set
	 */
	public void setCodinterno(String codinterno) {
		this.codinterno = codinterno;
	}

}
