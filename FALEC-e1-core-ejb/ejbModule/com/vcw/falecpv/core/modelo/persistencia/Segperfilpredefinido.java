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
@Table(name = "segperfilpredefinido")
public class Segperfilpredefinido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7904809111334063638L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegperfilpredefinido", nullable = false, length = 40)
    private String idsegperfilpredefinido;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden", nullable = false)
    private int orden;

	/**
	 * 
	 */
	public Segperfilpredefinido() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegperfilpredefinido != null ? idsegperfilpredefinido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segperfilpredefinido)) {
            return false;
        }
        Segperfilpredefinido other = (Segperfilpredefinido) object;
        if ((this.idsegperfilpredefinido == null && other.idsegperfilpredefinido != null) || (this.idsegperfilpredefinido != null && !this.idsegperfilpredefinido.equals(other.idsegperfilpredefinido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegperfilpredefinido
	 */
	public String getIdsegperfilpredefinido() {
		return idsegperfilpredefinido;
	}

	/**
	 * @param idsegperfilpredefinido the idsegperfilpredefinido to set
	 */
	public void setIdsegperfilpredefinido(String idsegperfilpredefinido) {
		this.idsegperfilpredefinido = idsegperfilpredefinido;
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

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

}
