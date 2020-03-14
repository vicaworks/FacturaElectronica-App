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
    @Column(name = "idice", nullable = false, length = 40)
    private String idice;
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
    @Column(name = "tarifaadvalorem", length = 10)
    private String tarifaadvalorem;
    @Size(max = 100)
    @Column(name = "tarifaespecifica", length = 100)
    private String tarifaespecifica;

	/**
	 * 
	 */
	public Ice() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idice != null ? idice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ice)) {
            return false;
        }
        Ice other = (Ice) object;
        if ((this.idice == null && other.idice != null) || (this.idice != null && !this.idice.equals(other.idice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idice
	 */
	public String getIdice() {
		return idice;
	}

	/**
	 * @param idice the idice to set
	 */
	public void setIdice(String idice) {
		this.idice = idice;
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
	 * @return the tarifaadvalorem
	 */
	public String getTarifaadvalorem() {
		return tarifaadvalorem;
	}

	/**
	 * @param tarifaadvalorem the tarifaadvalorem to set
	 */
	public void setTarifaadvalorem(String tarifaadvalorem) {
		this.tarifaadvalorem = tarifaadvalorem;
	}

	/**
	 * @return the tarifaespecifica
	 */
	public String getTarifaespecifica() {
		return tarifaespecifica;
	}

	/**
	 * @param tarifaespecifica the tarifaespecifica to set
	 */
	public void setTarifaespecifica(String tarifaespecifica) {
		this.tarifaespecifica = tarifaespecifica;
	}	

}
