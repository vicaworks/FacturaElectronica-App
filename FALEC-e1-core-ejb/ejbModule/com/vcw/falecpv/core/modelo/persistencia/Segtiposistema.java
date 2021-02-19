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
@Table(name = "segtiposistema")
public class Segtiposistema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3782111554053040773L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegtiposistema", nullable = false, length = 40)
    private String idsegtiposistema;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre", nullable = false, length = 250)
    private String nombre;
    
    @Size(max = 250)
    @Column(name = "descripcion", length = 250)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

	/**
	 * 
	 */
	public Segtiposistema() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegtiposistema != null ? idsegtiposistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segtiposistema)) {
            return false;
        }
        Segtiposistema other = (Segtiposistema) object;
        if ((this.idsegtiposistema == null && other.idsegtiposistema != null) || (this.idsegtiposistema != null && !this.idsegtiposistema.equals(other.idsegtiposistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegtiposistema
	 */
	public String getIdsegtiposistema() {
		return idsegtiposistema;
	}

	/**
	 * @param idsegtiposistema the idsegtiposistema to set
	 */
	public void setIdsegtiposistema(String idsegtiposistema) {
		this.idsegtiposistema = idsegtiposistema;
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
