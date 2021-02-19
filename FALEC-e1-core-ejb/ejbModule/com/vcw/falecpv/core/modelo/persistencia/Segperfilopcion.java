/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "segperfilopcion")
public class Segperfilopcion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418729117805922301L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegperfilopcion", nullable = false, length = 40)
    private String idsegperfilopcion;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @JoinColumn(name = "idsegopcion", referencedColumnName = "idsegopcion", nullable = false)
    @ManyToOne(optional = false)
    private Segopcion segopcion;
    
    @JoinColumn(name = "idsegperfil", referencedColumnName = "idsegperfil", nullable = false)
    @ManyToOne(optional = false)
    private Segperfil segperfil;

	/**
	 * 
	 */
	public Segperfilopcion() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegperfilopcion != null ? idsegperfilopcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segperfilopcion)) {
            return false;
        }
        Segperfilopcion other = (Segperfilopcion) object;
        if ((this.idsegperfilopcion == null && other.idsegperfilopcion != null) || (this.idsegperfilopcion != null && !this.idsegperfilopcion.equals(other.idsegperfilopcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegperfilopcion
	 */
	public String getIdsegperfilopcion() {
		return idsegperfilopcion;
	}

	/**
	 * @param idsegperfilopcion the idsegperfilopcion to set
	 */
	public void setIdsegperfilopcion(String idsegperfilopcion) {
		this.idsegperfilopcion = idsegperfilopcion;
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
	 * @return the segopcion
	 */
	public Segopcion getSegopcion() {
		return segopcion;
	}

	/**
	 * @param segopcion the segopcion to set
	 */
	public void setSegopcion(Segopcion segopcion) {
		this.segopcion = segopcion;
	}

	/**
	 * @return the segperfil
	 */
	public Segperfil getSegperfil() {
		return segperfil;
	}

	/**
	 * @param segperfil the segperfil to set
	 */
	public void setSegperfil(Segperfil segperfil) {
		this.segperfil = segperfil;
	}

}
