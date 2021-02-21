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

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "segopcion")
public class Segopcion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455143943977884258L;
	
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idsegopcion", nullable = false, length = 40)
    private String idsegopcion;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "identificador", nullable = false, length = 200)
    private String identificador;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    
    @Size(max = 300)
    @Column(name = "url", length = 300)
    private String url;
    
    @Column(name = "nivel")
    private Integer nivel;
    
    @Column(name = "orden")
    private Integer orden;
    
    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    
    @Size(max = 150)
    @Column(name = "icono", length = 150)
    private String icono;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Column(name = "columna")
    private Integer columna;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idsegopcionpadre", referencedColumnName = "idsegopcion", nullable = true)
    private Segopcion segopcionpadre;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idsegsistema", referencedColumnName = "idsegsistema", nullable = false)
    private Segsistema segsistema;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idsegtipoopcion", referencedColumnName = "idsegtipoopcion", nullable = false)
    private Segtipoopcion segtipoopcion;

    @Transient
    private String iniciales;
    
    @Transient
    private String opcion;
    
	/**
	 * 
	 */
	public Segopcion() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idsegopcion != null ? idsegopcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Segopcion)) {
            return false;
        }
        Segopcion other = (Segopcion) object;
        if ((this.idsegopcion == null && other.idsegopcion != null) || (this.idsegopcion != null && !this.idsegopcion.equals(other.idsegopcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idsegopcion
	 */
	public String getIdsegopcion() {
		return idsegopcion;
	}

	/**
	 * @param idsegopcion the idsegopcion to set
	 */
	public void setIdsegopcion(String idsegopcion) {
		this.idsegopcion = idsegopcion;
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
	 * @return the nivel
	 */
	public Integer getNivel() {
		return nivel;
	}

	/**
	 * @param nivel the nivel to set
	 */
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	/**
	 * @return the orden
	 */
	public Integer getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
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
	 * @return the icono
	 */
	public String getIcono() {
		return icono;
	}

	/**
	 * @param icono the icono to set
	 */
	public void setIcono(String icono) {
		this.icono = icono;
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
	 * @return the columna
	 */
	public Integer getColumna() {
		return columna;
	}

	/**
	 * @param columna the columna to set
	 */
	public void setColumna(Integer columna) {
		this.columna = columna;
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

	/**
	 * @return the segtipoopcion
	 */
	public Segtipoopcion getSegtipoopcion() {
		return segtipoopcion;
	}

	/**
	 * @param segtipoopcion the segtipoopcion to set
	 */
	public void setSegtipoopcion(Segtipoopcion segtipoopcion) {
		this.segtipoopcion = segtipoopcion;
	}

	/**
	 * @return the segopcionpadre
	 */
	public Segopcion getSegopcionpadre() {
		return segopcionpadre;
	}

	/**
	 * @param segopcionpadre the segopcionpadre to set
	 */
	public void setSegopcionpadre(Segopcion segopcionpadre) {
		this.segopcionpadre = segopcionpadre;
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
	 * @return the opcion
	 */
	public String getOpcion() {
		return opcion;
	}

	/**
	 * @param opcion the opcion to set
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

}
