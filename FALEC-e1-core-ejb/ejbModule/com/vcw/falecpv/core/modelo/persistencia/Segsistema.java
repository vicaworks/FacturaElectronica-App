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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "segsistema")
public class Segsistema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -181473888285794140L;
	
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegsistema", nullable = false, length = 40)
    private String idsegsistema;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre", nullable = false, length = 250)
    private String nombre;
    
    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "iniciales", nullable = false, length = 10)
    private String iniciales;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Size(max = 250)
    @Column(name = "url", length = 250)
    private String url;
    
    @JoinColumn(name = "idsegtiposistema", referencedColumnName = "idsegtiposistema", nullable = false)
    @ManyToOne(optional = false)
    private Segtiposistema segtiposistema;

	/**
	 * 
	 */
	public Segsistema() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegsistema != null ? idsegsistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segsistema)) {
            return false;
        }
        Segsistema other = (Segsistema) object;
        if ((this.idsegsistema == null && other.idsegsistema != null) || (this.idsegsistema != null && !this.idsegsistema.equals(other.idsegsistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegsistema
	 */
	public String getIdsegsistema() {
		return idsegsistema;
	}

	/**
	 * @param idsegsistema the idsegsistema to set
	 */
	public void setIdsegsistema(String idsegsistema) {
		this.idsegsistema = idsegsistema;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the iniciales
	 */
	public String getIniciales() {
		return iniciales;
	}

	/**
	 * @param iniciales the iniciales to set
	 */
	public void setIniciales(String iniciales) {
		this.iniciales = iniciales;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the segtiposistema
	 */
	public Segtiposistema getSegtiposistema() {
		return segtiposistema;
	}

	/**
	 * @param segtiposistema the segtiposistema to set
	 */
	public void setSegtiposistema(Segtiposistema segtiposistema) {
		this.segtiposistema = segtiposistema;
	}

}
