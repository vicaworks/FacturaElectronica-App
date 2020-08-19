/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "transacciontipo")
public class Transacciontipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3973161931683904746L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(nullable = false, length = 40)
    private String idtransacciontipo;
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

	/**
	 * 
	 */
	public Transacciontipo() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransacciontipo != null ? idtransacciontipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transacciontipo)) {
            return false;
        }
        Transacciontipo other = (Transacciontipo) object;
        if ((this.idtransacciontipo == null && other.idtransacciontipo != null) || (this.idtransacciontipo != null && !this.idtransacciontipo.equals(other.idtransacciontipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s, %s]", idtransacciontipo, nombre);
    }

	/**
	 * @return the idtransacciontipo
	 */
	public String getIdtransacciontipo() {
		return idtransacciontipo;
	}

	/**
	 * @param idtransacciontipo the idtransacciontipo to set
	 */
	public void setIdtransacciontipo(String idtransacciontipo) {
		this.idtransacciontipo = idtransacciontipo;
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

}
