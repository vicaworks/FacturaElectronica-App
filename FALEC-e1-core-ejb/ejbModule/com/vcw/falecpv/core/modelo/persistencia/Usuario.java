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
@Table(name = "usuario")
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5134342897943843178L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre", nullable = false, length = 300)
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "login", nullable = false, length = 50)
    private String login;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "clave", nullable = false, length = 20)
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "puntoemision", nullable = false, length = 3)
    private String puntoemision;
    
    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "administrador", nullable = false, length = 1)
    private String administrador;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Column(name = "nombrepantalla", nullable = false, length = 20)
    private String nombrepantalla;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    private Establecimiento establecimiento;
    
    @Transient
    private Empresa empresa;

    @Transient
    private String clave2;
    
    @Transient
    private boolean actualizarCredenciales = false;
    
	/**
	 * 
	 */
	public Usuario() {
	}
	
	
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuario != null ? idusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idusuario == null && other.idusuario != null) || (this.idusuario != null && !this.idusuario.equals(other.idusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }



	/**
	 * @return the idusuario
	 */
	public String getIdusuario() {
		return idusuario;
	}



	/**
	 * @param idusuario the idusuario to set
	 */
	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
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
	 * @return the identificacion
	 */
	public String getIdentificacion() {
		return identificacion;
	}



	/**
	 * @param identificacion the identificacion to set
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}



	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}



	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}



	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}



	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}



	/**
	 * @return the puntoemision
	 */
	public String getPuntoemision() {
		return puntoemision;
	}



	/**
	 * @param puntoemision the puntoemision to set
	 */
	public void setPuntoemision(String puntoemision) {
		this.puntoemision = puntoemision;
	}



	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}



	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	/**
	 * @return the administrador
	 */
	public String getAdministrador() {
		return administrador;
	}



	/**
	 * @param administrador the administrador to set
	 */
	public void setAdministrador(String administrador) {
		this.administrador = administrador;
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
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}



	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}



	/**
	 * @return the establecimiento
	 */
	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}



	/**
	 * @param establecimiento the establecimiento to set
	 */
	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}



	/**
	 * @return the nombrepantalla
	 */
	public String getNombrepantalla() {
		return nombrepantalla;
	}



	/**
	 * @param nombrepantalla the nombrepantalla to set
	 */
	public void setNombrepantalla(String nombrepantalla) {
		this.nombrepantalla = nombrepantalla;
	}



	/**
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}



	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}



	/**
	 * @return the clave2
	 */
	public String getClave2() {
		return clave2;
	}



	/**
	 * @param clave2 the clave2 to set
	 */
	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}



	/**
	 * @return the actualizarCredenciales
	 */
	public boolean isActualizarCredenciales() {
		return actualizarCredenciales;
	}



	/**
	 * @param actualizarCredenciales the actualizarCredenciales to set
	 */
	public void setActualizarCredenciales(boolean actualizarCredenciales) {
		this.actualizarCredenciales = actualizarCredenciales;
	}



	

}
