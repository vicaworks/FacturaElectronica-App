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
@Table(name = "catagenteretencion")
public class CatAgenteretencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6304729623048799806L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idcatagenteretencion", nullable = false, length = 40)
    private String idcatagenteretencion;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ruc", nullable = false, length = 20)
    private String ruc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipocontribuyente", nullable = false, length = 100)
    private String tipocontribuyente;
    
    @Size(max = 100)
    @Column(name = "zona", length = 100)
    private String zona;
    
    @Size(max = 150)
    @Column(name = "provincia", length = 150)
    private String provincia;
    
    @Size(max = 150)
    @Column(name = "resolucion", length = 150)
    private String resolucion;

	/**
	 * 
	 */
	public CatAgenteretencion() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcatagenteretencion != null ? idcatagenteretencion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CatAgenteretencion)) {
            return false;
        }
        CatAgenteretencion other = (CatAgenteretencion) object;
        if ((this.idcatagenteretencion == null && other.idcatagenteretencion != null) || (this.idcatagenteretencion != null && !this.idcatagenteretencion.equals(other.idcatagenteretencion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idcatagenteretencion
	 */
	public String getIdcatagenteretencion() {
		return idcatagenteretencion;
	}

	/**
	 * @param idcatagenteretencion the idcatagenteretencion to set
	 */
	public void setIdcatagenteretencion(String idcatagenteretencion) {
		this.idcatagenteretencion = idcatagenteretencion;
	}

	/**
	 * @return the ruc
	 */
	public String getRuc() {
		return ruc;
	}

	/**
	 * @param ruc the ruc to set
	 */
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	/**
	 * @return the tipocontribuyente
	 */
	public String getTipocontribuyente() {
		return tipocontribuyente;
	}

	/**
	 * @param tipocontribuyente the tipocontribuyente to set
	 */
	public void setTipocontribuyente(String tipocontribuyente) {
		this.tipocontribuyente = tipocontribuyente;
	}

	/**
	 * @return the zona
	 */
	public String getZona() {
		return zona;
	}

	/**
	 * @param zona the zona to set
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}

	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @return the resolucion
	 */
	public String getResolucion() {
		return resolucion;
	}

	/**
	 * @param resolucion the resolucion to set
	 */
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

}
