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
@Table(name = "errorsri")
public class Errorsri implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4335929710504332901L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "iderrorsri", nullable = false, length = 40)
    private String iderrorsri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "coderror", nullable = false, length = 2)
    private String coderror;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "etiqueta", nullable = false, length = 32)
    private String etiqueta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "descripcion", nullable = false, length = 128)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "motivo", nullable = false, length = 512)
    private String motivo;

	/**
	 * 
	 */
	public Errorsri() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iderrorsri != null ? iderrorsri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Errorsri)) {
            return false;
        }
        Errorsri other = (Errorsri) object;
        if ((this.iderrorsri == null && other.iderrorsri != null) || (this.iderrorsri != null && !this.iderrorsri.equals(other.iderrorsri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the iderrorsri
	 */
	public String getIderrorsri() {
		return iderrorsri;
	}

	/**
	 * @param iderrorsri the iderrorsri to set
	 */
	public void setIderrorsri(String iderrorsri) {
		this.iderrorsri = iderrorsri;
	}

	/**
	 * @return the coderror
	 */
	public String getCoderror() {
		return coderror;
	}

	/**
	 * @param coderror the coderror to set
	 */
	public void setCoderror(String coderror) {
		this.coderror = coderror;
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
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
