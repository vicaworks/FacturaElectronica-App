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
	@Column(name = "idFabricante", nullable = false, length = 40)
	private String idFabricante;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nombreComercial", nullable = false, length = 100)
	private String nombreComercial;
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
	@Column(name = "idUsuario", nullable = false, length = 40)
	private String idUsuario;

	/**
	 * 
	 */
	public Fabricante() {
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idFabricante != null ? idFabricante.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Fabricante)) {
			return false;
		}
		Fabricante other = (Fabricante) object;
		if ((this.idFabricante == null && other.idFabricante != null)
				|| (this.idFabricante != null && !this.idFabricante.equals(other.idFabricante))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	/**
	 * @return the idFabricante
	 */
	public String getIdFabricante() {
		return idFabricante;
	}

	/**
	 * @param idFabricante the idFabricante to set
	 */
	public void setIdFabricante(String idFabricante) {
		this.idFabricante = idFabricante;
	}

	/**
	 * @return the nombreComercial
	 */
	public String getNombreComercial() {
		return nombreComercial;
	}

	/**
	 * @param nombreComercial the nombreComercial to set
	 */
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
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
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

}
