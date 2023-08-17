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
@Table(name = "grupocategoria")
public class Grupocategoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3968724866704313023L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "idgrupocategoria", nullable = false, length = 40)
	private String idgrupocategoria;
	
	@Size(max = 150)
	@Column(name = "grupo", length = 150)
	private String grupo;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "estado", nullable = false, length = 1)
	private String estado;
	
	@ManyToOne
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    private Empresa empresa;
	

	/**
	 * 
	 */
	public Grupocategoria() {

	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idgrupocategoria != null ? idgrupocategoria.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Grupocategoria)) {
			return false;
		}
		Grupocategoria other = (Grupocategoria) object;
		if ((this.idgrupocategoria == null && other.idgrupocategoria != null)
				|| (this.idgrupocategoria != null && !this.idgrupocategoria.equals(other.idgrupocategoria))) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
    	return String.format("[%s, %s]", idgrupocategoria, grupo);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idgrupocategoria
	 */
	public String getIdgrupocategoria() {
		return idgrupocategoria;
	}

	/**
	 * @param idgrupocategoria the idgrupocategoria to set
	 */
	public void setIdgrupocategoria(String idgrupocategoria) {
		this.idgrupocategoria = idgrupocategoria;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
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
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
    
    
}
