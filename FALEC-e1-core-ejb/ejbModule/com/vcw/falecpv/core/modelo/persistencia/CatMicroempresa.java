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
@Table(name = "catmicroempresa")
public class CatMicroempresa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6402224460978685620L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idcatmicroempresa", nullable = false, length = 40)
    private String idcatmicroempresa;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ruc", nullable = false, length = 20)
    private String ruc;
    
    @Size(max = 100)
    @Column(name = "zona", length = 100)
    private String zona;
    
    @Size(max = 150)
    @Column(name = "provincia", length = 150)
    private String provincia;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio", nullable = false)
    private int anio;

	/**
	 * 
	 */
	public CatMicroempresa() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcatmicroempresa != null ? idcatmicroempresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CatMicroempresa)) {
            return false;
        }
        CatMicroempresa other = (CatMicroempresa) object;
        if ((this.idcatmicroempresa == null && other.idcatmicroempresa != null) || (this.idcatmicroempresa != null && !this.idcatmicroempresa.equals(other.idcatmicroempresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idcatmicroempresa
	 */
	public String getIdcatmicroempresa() {
		return idcatmicroempresa;
	}

	/**
	 * @param idcatmicroempresa the idcatmicroempresa to set
	 */
	public void setIdcatmicroempresa(String idcatmicroempresa) {
		this.idcatmicroempresa = idcatmicroempresa;
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
	 * @return the anio
	 */
	public int getAnio() {
		return anio;
	}

	/**
	 * @param anio the anio to set
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}

}
