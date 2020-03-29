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
@Table(name = "tipoproducto")
public class TipoProducto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3460674941256034867L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtipoproducto", nullable = false, length = 40)
    private String idtipoproducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre", nullable = false, length = 30)
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
	public TipoProducto() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoproducto != null ? idtipoproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoProducto)) {
            return false;
        }
        TipoProducto other = (TipoProducto) object;
        if ((this.idtipoproducto == null && other.idtipoproducto != null) || (this.idtipoproducto != null && !this.idtipoproducto.equals(other.idtipoproducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s,%s]", idtipoproducto,nombre);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtipoproducto
	 */
	public String getIdtipoproducto() {
		return idtipoproducto;
	}

	/**
	 * @param idtipoproducto the idtipoproducto to set
	 */
	public void setIdtipoproducto(String idtipoproducto) {
		this.idtipoproducto = idtipoproducto;
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
