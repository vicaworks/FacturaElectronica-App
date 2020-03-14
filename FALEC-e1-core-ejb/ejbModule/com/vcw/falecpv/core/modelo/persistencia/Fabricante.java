/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "fabricante")
public class Fabricante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 400951433810552133L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idfabricante", nullable = false, length = 40)
	private String idfabricante;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nombrecomercial", nullable = false, length = 100)
	private String nombrecomercial;
	@Size(max = 1)
	@Column(name = "estado", length = 1)
	private String estado;
	@Basic(optional = false)
	@NotNull
	@Column(name = "updated", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date updated;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;

	/**
	 * 
	 */
	public Fabricante() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idfabricante != null ? idfabricante.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Fabricante)) {
			return false;
		}
		Fabricante other = (Fabricante) object;
		if ((this.idfabricante == null && other.idfabricante != null)
				|| (this.idfabricante != null && !this.idfabricante.equals(other.idfabricante))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	/**
	 * @return the idfabricante
	 */
	public String getIdfabricante() {
		return idfabricante;
	}

	/**
	 * @param idfabricante the idfabricante to set
	 */
	public void setIdfabricante(String idfabricante) {
		this.idfabricante = idfabricante;
	}

	/**
	 * @return the nombrecomercial
	 */
	public String getNombrecomercial() {
		return nombrecomercial;
	}

	/**
	 * @param nombrecomercial the nombrecomercial to set
	 */
	public void setNombrecomercial(String nombrecomercial) {
		this.nombrecomercial = nombrecomercial;
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
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the idusuario
	 */
	public String getIdusuario() {
		return idusuario;
	}

	/**
	 * @param idusuario the idusuario to set
	 */
	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}	

}
