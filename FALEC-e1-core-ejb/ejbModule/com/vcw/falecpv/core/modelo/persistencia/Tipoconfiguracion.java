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
@Table(name = "tipoconfiguracion")
public class Tipoconfiguracion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6438574236274411593L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtipoconfiguracion", nullable = false, length = 40)
    private String idtipoconfiguracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Size(max = 3000)
    @Column(name = "descripcion", length = 3000)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
	
	/**
	 * 
	 */
	public Tipoconfiguracion() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoconfiguracion != null ? idtipoconfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tipoconfiguracion)) {
            return false;
        }
        Tipoconfiguracion other = (Tipoconfiguracion) object;
        if ((this.idtipoconfiguracion == null && other.idtipoconfiguracion != null) || (this.idtipoconfiguracion != null && !this.idtipoconfiguracion.equals(other.idtipoconfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtipoconfiguracion
	 */
	public String getIdtipoconfiguracion() {
		return idtipoconfiguracion;
	}

	/**
	 * @param idtipoconfiguracion the idtipoconfiguracion to set
	 */
	public void setIdtipoconfiguracion(String idtipoconfiguracion) {
		this.idtipoconfiguracion = idtipoconfiguracion;
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
