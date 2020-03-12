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
@Table(name = "iva")
public class Iva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1510963512187186133L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idIva", nullable = false, length = 40)
	private String idIva;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "codigo", nullable = false, length = 1)
	private String codigo;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "porcentaje", nullable = false, length = 32)
	private String porcentaje;

	/**
	 * 
	 */
	public Iva() {

	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idIva != null ? idIva.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Iva)) {
			return false;
		}
		Iva other = (Iva) object;
		if ((this.idIva == null && other.idIva != null) || (this.idIva != null && !this.idIva.equals(other.idIva))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	/**
	 * @return the idIva
	 */
	public String getIdIva() {
		return idIva;
	}

	/**
	 * @param idIva the idIva to set
	 */
	public void setIdIva(String idIva) {
		this.idIva = idIva;
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

	/**
	 * @return the porcentaje
	 */
	public String getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	

}
