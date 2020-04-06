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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.servitec.common.util.PojoUtil;

/**
 * @author Jorge Toaza
 *
 */
@Entity
@Table(name = "cliente")

public class Cliente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7654129876541239857L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idcliente", nullable = false, length = 40)
	private String idcliente;
	
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "identificacion", nullable = false, length = 20)
	private String identificacion;
	
	@NotNull
	@Size(min = 1, max = 300)
	@Column(name = "razonsocial", nullable = false, length = 300)
	private String razonsocial;
	
	@Size(max = 300)
	@Column(name = "direccion", length = 300)
	private String direccion;
	
	@Size(max = 50)
	@Column(name = "correoelectronico", length = 50)
	private String correoelectronico;
	
	@Size(max = 20)
	@Column(name = "telefono", length = 20)
	private String telefono;
	
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;
	
	@NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
	
	@ManyToOne
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    private Empresa empresa;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "idtipoidentificacion", referencedColumnName = "idtipoidentificacion", nullable = false)
    private TipoIdentificacion tipoIdentificacion;
	
	/**
	 * 
	 */
	public Cliente() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcliente != null ? idcliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.idcliente == null && other.idcliente != null) || (this.idcliente != null && !this.idcliente.equals(other.idcliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",idcliente , razonsocial);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idcliente
	 */
	public String getIdcliente() {
		return idcliente;
	}

	/**
	 * @param idcliente the idcliente to set
	 */
	public void setIdcliente(String idcliente) {
		this.idcliente = idcliente;
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
	 * @return the razonsocial
	 */
	public String getRazonsocial() {
		return razonsocial;
	}

	/**
	 * @param razonsocial the razonsocial to set
	 */
	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the correoelectronico
	 */
	public String getCorreoelectronico() {
		return correoelectronico;
	}

	/**
	 * @param correoelectronico the correoelectronico to set
	 */
	public void setCorreoelectronico(String correoelectronico) {
		this.correoelectronico = correoelectronico;
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
	 * @return the tipoIdentificacion
	 */
	public TipoIdentificacion getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
}