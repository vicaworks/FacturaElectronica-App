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
@Table(name = "ice")
public class Ice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3072760813740867189L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idIce", nullable = false, length = 40)
    private String idIce;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codigo", nullable = false, length = 4)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;
    @Size(max = 10)
    @Column(name = "tarifaAdValorem", length = 10)
    private String tarifaAdValorem;
    @Size(max = 100)
    @Column(name = "tarifaEspecifica", length = 100)
    private String tarifaEspecifica;

	/**
	 * 
	 */
	public Ice() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idIce != null ? idIce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ice)) {
            return false;
        }
        Ice other = (Ice) object;
        if ((this.idIce == null && other.idIce != null) || (this.idIce != null && !this.idIce.equals(other.idIce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idIce
	 */
	public String getIdIce() {
		return idIce;
	}

	/**
	 * @param idIce the idIce to set
	 */
	public void setIdIce(String idIce) {
		this.idIce = idIce;
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
	 * @return the tarifaAdValorem
	 */
	public String getTarifaAdValorem() {
		return tarifaAdValorem;
	}

	/**
	 * @param tarifaAdValorem the tarifaAdValorem to set
	 */
	public void setTarifaAdValorem(String tarifaAdValorem) {
		this.tarifaAdValorem = tarifaAdValorem;
	}

	/**
	 * @return the tarifaEspecifica
	 */
	public String getTarifaEspecifica() {
		return tarifaEspecifica;
	}

	/**
	 * @param tarifaEspecifica the tarifaEspecifica to set
	 */
	public void setTarifaEspecifica(String tarifaEspecifica) {
		this.tarifaEspecifica = tarifaEspecifica;
	}

}
