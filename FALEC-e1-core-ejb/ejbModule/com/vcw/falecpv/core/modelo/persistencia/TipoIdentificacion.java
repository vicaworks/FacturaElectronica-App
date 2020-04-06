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
 * @author Jorge Toaza
 *
 */
@Entity
@Table(name = "tipoidentificacion")

public class TipoIdentificacion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719845214540321467L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idtipoidentificacion", nullable = false, length = 40)
	private String idtipoidentificacion;
	
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "tipoidentificacion", nullable = false, length = 30)
	private String tipoidentificacion;
	
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "codigo", nullable = false, length = 2)
	private String codigo;
	
	/**
	 * 
	 */
	public TipoIdentificacion() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoidentificacion != null ? idtipoidentificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoIdentificacion)) {
            return false;
        }
        TipoIdentificacion other = (TipoIdentificacion) object;
        if ((this.idtipoidentificacion == null && other.idtipoidentificacion != null) || (this.idtipoidentificacion != null && !this.idtipoidentificacion.equals(other.idtipoidentificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",idtipoidentificacion , tipoidentificacion);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtipoidentificacion
	 */
	public String getIdtipoidentificacion() {
		return idtipoidentificacion;
	}

	/**
	 * @param idtipoidentificacion the idtipoidentificacion to set
	 */
	public void setIdtipoidentificacion(String idtipoidentificacion) {
		this.idtipoidentificacion = idtipoidentificacion;
	}

	/**
	 * @return the tipoidentificacion
	 */
	public String getTipoidentificacion() {
		return tipoidentificacion;
	}

	/**
	 * @param tipoidentificacion the tipoidentificacion to set
	 */
	public void setTipoidentificacion(String tipoidentificacion) {
		this.tipoidentificacion = tipoidentificacion;
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
}