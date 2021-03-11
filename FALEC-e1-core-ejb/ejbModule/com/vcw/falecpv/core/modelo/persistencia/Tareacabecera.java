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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "tareacabecera")
public class Tareacabecera implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -106693468527813626L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtareacabecera", nullable = false, length = 40)
    private String idtareacabecera;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechalimite", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechalimite;
    
    @Size(max = 1000)
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "prioridadvalor", nullable = false)
    private int prioridadvalor;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "prioridad", nullable = false, length = 100)
    private String prioridad;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "estado", nullable = false, length = 100)
    private String estado;
    
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
    
    @JoinColumn(name = "idtareaetiqueta", referencedColumnName = "idtareaetiqueta", nullable = false)
    @ManyToOne(optional = false)
    private Tareaetiqueta tareaetiqueta;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    
    @Transient
    private String idEstablecimiento;

	/**
	 * 
	 */
	public Tareacabecera() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtareacabecera != null ? idtareacabecera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tareacabecera)) {
            return false;
        }
        Tareacabecera other = (Tareacabecera) object;
        if ((this.idtareacabecera == null && other.idtareacabecera != null) || (this.idtareacabecera != null && !this.idtareacabecera.equals(other.idtareacabecera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtareacabecera
	 */
	public String getIdtareacabecera() {
		return idtareacabecera;
	}

	/**
	 * @param idtareacabecera the idtareacabecera to set
	 */
	public void setIdtareacabecera(String idtareacabecera) {
		this.idtareacabecera = idtareacabecera;
	}

	/**
	 * @return the fechalimite
	 */
	public Date getFechalimite() {
		return fechalimite;
	}

	/**
	 * @param fechalimite the fechalimite to set
	 */
	public void setFechalimite(Date fechalimite) {
		this.fechalimite = fechalimite;
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
	 * @return the prioridadvalor
	 */
	public int getPrioridadvalor() {
		return prioridadvalor;
	}

	/**
	 * @param prioridadvalor the prioridadvalor to set
	 */
	public void setPrioridadvalor(int prioridadvalor) {
		this.prioridadvalor = prioridadvalor;
	}

	/**
	 * @return the prioridad
	 */
	public String getPrioridad() {
		return prioridad;
	}

	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
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
	 * @return the cabecera
	 */
	public Cabecera getCabecera() {
		return cabecera;
	}

	/**
	 * @param cabecera the cabecera to set
	 */
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}

	/**
	 * @return the tareaetiqueta
	 */
	public Tareaetiqueta getTareaetiqueta() {
		return tareaetiqueta;
	}

	/**
	 * @param tareaetiqueta the tareaetiqueta to set
	 */
	public void setTareaetiqueta(Tareaetiqueta tareaetiqueta) {
		this.tareaetiqueta = tareaetiqueta;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the idEstablecimiento
	 */
	public String getIdEstablecimiento() {
		return idEstablecimiento;
	}

	/**
	 * @param idEstablecimiento the idEstablecimiento to set
	 */
	public void setIdEstablecimiento(String idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

}
