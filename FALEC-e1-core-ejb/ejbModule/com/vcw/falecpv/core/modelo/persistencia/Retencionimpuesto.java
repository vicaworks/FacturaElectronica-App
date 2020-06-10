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
@Table(name = "retencionimpuesto")
public class Retencionimpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2800639879855361936L;

	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idretencionimpuesto", nullable = false, length = 40)
    private String idretencionimpuesto;
	
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
    
    @Size(max = 20)
    @Column(name = "codigo", length = 20)
    private String codigo;
    
    
	/**
	 * 
	 */
	public Retencionimpuesto() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idretencionimpuesto != null ? idretencionimpuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Retencionimpuesto)) {
            return false;
        }
        Retencionimpuesto other = (Retencionimpuesto) object;
        if ((this.idretencionimpuesto == null && other.idretencionimpuesto != null) || (this.idretencionimpuesto != null && !this.idretencionimpuesto.equals(other.idretencionimpuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",nombre , idretencionimpuesto);
    }
    
    public String toStringObject() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idretencionimpuesto
	 */
	public String getIdretencionimpuesto() {
		return idretencionimpuesto;
	}

	/**
	 * @param idretencionimpuesto the idretencionimpuesto to set
	 */
	public void setIdretencionimpuesto(String idretencionimpuesto) {
		this.idretencionimpuesto = idretencionimpuesto;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
