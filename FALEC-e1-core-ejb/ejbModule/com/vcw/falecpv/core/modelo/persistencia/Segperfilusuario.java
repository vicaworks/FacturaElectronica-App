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
@Table(name = "segperfilusuario")
public class Segperfilusuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8242535680226136544L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegperfilusuario", nullable = false, length = 40)
    private String idsegperfilusuario;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @JoinColumn(name = "idsegperfil", referencedColumnName = "idsegperfil", nullable = false)
    @ManyToOne(optional = false)
    private Segperfil segperfil;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

	/**
	 * 
	 */
	public Segperfilusuario() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegperfilusuario != null ? idsegperfilusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segperfilusuario)) {
            return false;
        }
        Segperfilusuario other = (Segperfilusuario) object;
        if ((this.idsegperfilusuario == null && other.idsegperfilusuario != null) || (this.idsegperfilusuario != null && !this.idsegperfilusuario.equals(other.idsegperfilusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegperfilusuario
	 */
	public String getIdsegperfilusuario() {
		return idsegperfilusuario;
	}

	/**
	 * @param idsegperfilusuario the idsegperfilusuario to set
	 */
	public void setIdsegperfilusuario(String idsegperfilusuario) {
		this.idsegperfilusuario = idsegperfilusuario;
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
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
