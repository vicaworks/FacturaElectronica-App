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
@Table(name = "tipocomprobante")
public class Tipocomprobante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 116854799186002477L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtipocomprobante", nullable = false, length = 40)
    private String idtipocomprobante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "comprobante", nullable = false, length = 200)
    private String comprobante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "formularios", nullable = false, length = 100)
    private String formularios;
    @Size(max = 100)
    @Column(name = "identificador", length = 100)
    private String identificador;
    @Size(max = 40)
    @Column(name = "idusuario", length = 40)
    private String idusuario;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
//    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
//    @ManyToOne(optional = false)
//    private Empresa empresa;

	/**
	 * 
	 */
	public Tipocomprobante() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipocomprobante != null ? idtipocomprobante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tipocomprobante)) {
            return false;
        }
        Tipocomprobante other = (Tipocomprobante) object;
        if ((this.idtipocomprobante == null && other.idtipocomprobante != null) || (this.idtipocomprobante != null && !this.idtipocomprobante.equals(other.idtipocomprobante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",comprobante , idtipocomprobante);
    }
    
    public String toStringObject() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idtipocomprobante
	 */
	public String getIdtipocomprobante() {
		return idtipocomprobante;
	}

	/**
	 * @param idtipocomprobante the idtipocomprobante to set
	 */
	public void setIdtipocomprobante(String idtipocomprobante) {
		this.idtipocomprobante = idtipocomprobante;
	}

	/**
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
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
	 * @return the formularios
	 */
	public String getFormularios() {
		return formularios;
	}

	/**
	 * @param formularios the formularios to set
	 */
	public void setFormularios(String formularios) {
		this.formularios = formularios;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
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

}
