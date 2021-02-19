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

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "segtipoopcion")
public class Segtipoopcion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9140550720978254559L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegtipoopcion", nullable = false, length = 40)
    private String idsegtipoopcion;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    
    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

	/**
	 * 
	 */
	public Segtipoopcion() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegtipoopcion != null ? idsegtipoopcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segtipoopcion)) {
            return false;
        }
        Segtipoopcion other = (Segtipoopcion) object;
        if ((this.idsegtipoopcion == null && other.idsegtipoopcion != null) || (this.idsegtipoopcion != null && !this.idsegtipoopcion.equals(other.idsegtipoopcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegtipoopcion
	 */
	public String getIdsegtipoopcion() {
		return idsegtipoopcion;
	}

	/**
	 * @param idsegtipoopcion the idsegtipoopcion to set
	 */
	public void setIdsegtipoopcion(String idsegtipoopcion) {
		this.idsegtipoopcion = idsegtipoopcion;
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
