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
@Table(name = "segperfilpredefinidoperfil")
public class Segperfilpredefinidoperfil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1810706351942361401L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegperfildefinidoperfil", nullable = false, length = 40)
    private String idsegperfildefinidoperfil;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @JoinColumn(name = "idsegperfil", referencedColumnName = "idsegperfil", nullable = false)
    @ManyToOne(optional = false)
    private Segperfil segperfil;
    
    @JoinColumn(name = "idsegperfilpredefinido", referencedColumnName = "idsegperfilpredefinido", nullable = false)
    @ManyToOne(optional = false)
    private Segperfilpredefinido segperfilpredefinido;

	/**
	 * 
	 */
	public Segperfilpredefinidoperfil() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegperfildefinidoperfil != null ? idsegperfildefinidoperfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segperfilpredefinidoperfil)) {
            return false;
        }
        Segperfilpredefinidoperfil other = (Segperfilpredefinidoperfil) object;
        if ((this.idsegperfildefinidoperfil == null && other.idsegperfildefinidoperfil != null) || (this.idsegperfildefinidoperfil != null && !this.idsegperfildefinidoperfil.equals(other.idsegperfildefinidoperfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegperfildefinidoperfil
	 */
	public String getIdsegperfildefinidoperfil() {
		return idsegperfildefinidoperfil;
	}

	/**
	 * @param idsegperfildefinidoperfil the idsegperfildefinidoperfil to set
	 */
	public void setIdsegperfildefinidoperfil(String idsegperfildefinidoperfil) {
		this.idsegperfildefinidoperfil = idsegperfildefinidoperfil;
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

	/**
	 * @return the segperfilpredefinido
	 */
	public Segperfilpredefinido getSegperfilpredefinido() {
		return segperfilpredefinido;
	}

	/**
	 * @param segperfilpredefinido the segperfilpredefinido to set
	 */
	public void setSegperfilpredefinido(Segperfilpredefinido segperfilpredefinido) {
		this.segperfilpredefinido = segperfilpredefinido;
	}

}
