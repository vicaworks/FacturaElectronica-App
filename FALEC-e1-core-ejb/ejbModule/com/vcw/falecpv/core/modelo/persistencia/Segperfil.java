/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "segperfil")
public class Segperfil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5045947763291791082L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegperfil", nullable = false, length = 40)
    private String idsegperfil;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombre", nullable = false, length = 2147483647)
    private String nombre;
    
    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Size(max = 40)
    @Column(name = "idreferencia", length = 40)
    private String idreferencia;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idsegsistema", referencedColumnName = "idsegsistema", nullable = false)
    private Segsistema segsistema;
    
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "segperfil")
    private List<Segperfilopcion> segperfilopcionList;
    
    @Transient
    private boolean seleccion;

	/**
	 * 
	 */
	public Segperfil() {
		
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegperfil != null ? idsegperfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segperfil)) {
            return false;
        }
        Segperfil other = (Segperfil) object;
        if ((this.idsegperfil == null && other.idsegperfil != null) || (this.idsegperfil != null && !this.idsegperfil.equals(other.idsegperfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegperfil
	 */
	public String getIdsegperfil() {
		return idsegperfil;
	}

	/**
	 * @param idsegperfil the idsegperfil to set
	 */
	public void setIdsegperfil(String idsegperfil) {
		this.idsegperfil = idsegperfil;
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
	 * @return the idreferencia
	 */
	public String getIdreferencia() {
		return idreferencia;
	}

	/**
	 * @param idreferencia the idreferencia to set
	 */
	public void setIdreferencia(String idreferencia) {
		this.idreferencia = idreferencia;
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
	 * @return the segperfilopcionList
	 */
	public List<Segperfilopcion> getSegperfilopcionList() {
		return segperfilopcionList;
	}

	/**
	 * @param segperfilopcionList the segperfilopcionList to set
	 */
	public void setSegperfilopcionList(List<Segperfilopcion> segperfilopcionList) {
		this.segperfilopcionList = segperfilopcionList;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the segsistema
	 */
	public Segsistema getSegsistema() {
		return segsistema;
	}

	/**
	 * @param segsistema the segsistema to set
	 */
	public void setSegsistema(Segsistema segsistema) {
		this.segsistema = segsistema;
	}
	
}
