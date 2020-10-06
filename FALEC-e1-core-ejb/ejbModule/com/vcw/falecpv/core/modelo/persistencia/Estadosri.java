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
@Table(name = "estadosri")
public class Estadosri implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4870288093297848363L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idestadosri", nullable = false, length = 40)
    private String idestadosri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "estadosri", nullable = false, length = 32)
    private String estadosri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "descripcion", nullable = false, length = 128)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eliminar", nullable = false)
    private int eliminar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actualizar", nullable = false)
    private int actualizar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cambiarclaveacceso", nullable = false)
    private int cambiarclaveacceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "estado", nullable = false, length = 32)
    private String estado;

	/**
	 * 
	 */
	public Estadosri() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idestadosri != null ? idestadosri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Estadosri)) {
            return false;
        }
        Estadosri other = (Estadosri) object;
        if ((this.idestadosri == null && other.idestadosri != null) || (this.idestadosri != null && !this.idestadosri.equals(other.idestadosri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idestadosri
	 */
	public String getIdestadosri() {
		return idestadosri;
	}

	/**
	 * @param idestadosri the idestadosri to set
	 */
	public void setIdestadosri(String idestadosri) {
		this.idestadosri = idestadosri;
	}

	/**
	 * @return the estadosri
	 */
	public String getEstadosri() {
		return estadosri;
	}

	/**
	 * @param estadosri the estadosri to set
	 */
	public void setEstadosri(String estadosri) {
		this.estadosri = estadosri;
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
	 * @return the eliminar
	 */
	public int getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(int eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the actualizar
	 */
	public int getActualizar() {
		return actualizar;
	}

	/**
	 * @param actualizar the actualizar to set
	 */
	public void setActualizar(int actualizar) {
		this.actualizar = actualizar;
	}

	/**
	 * @return the cambiarclaveacceso
	 */
	public int getCambiarclaveacceso() {
		return cambiarclaveacceso;
	}

	/**
	 * @param cambiarclaveacceso the cambiarclaveacceso to set
	 */
	public void setCambiarclaveacceso(int cambiarclaveacceso) {
		this.cambiarclaveacceso = cambiarclaveacceso;
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
