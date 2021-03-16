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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "etiqueta")
public class Etiqueta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1545491402447342325L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtareaetiqueta", nullable = false, length = 40)
    private String idtareaetiqueta;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "etiqueta", nullable = false, length = 200)
    private String etiqueta;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "modulo", nullable = false, length = 1)
    private String modulo;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @Transient
    private String idEstablecimiento;

	/**
	 * 
	 */
	public Etiqueta() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtareaetiqueta != null ? idtareaetiqueta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Etiqueta)) {
            return false;
        }
        Etiqueta other = (Etiqueta) object;
        if ((this.idtareaetiqueta == null && other.idtareaetiqueta != null) || (this.idtareaetiqueta != null && !this.idtareaetiqueta.equals(other.idtareaetiqueta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s, %s]", idtareaetiqueta, etiqueta);
    }

	/**
	 * @return the idtareaetiqueta
	 */
	public String getIdtareaetiqueta() {
		return idtareaetiqueta;
	}

	/**
	 * @param idtareaetiqueta the idtareaetiqueta to set
	 */
	public void setIdtareaetiqueta(String idtareaetiqueta) {
		this.idtareaetiqueta = idtareaetiqueta;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
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

	/**
	 * @return the idEstablecimiento
	 */
	public String getIdEstablecimiento() {
		return idEstablecimiento;
	}

	/**
	 * @param idEstablecimiento the idEstablecimiento to set
	 */
	public void setIdEstablecimiento(String idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	/**
	 * @return the modulo
	 */
	public String getModulo() {
		return modulo;
	}

	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

}
