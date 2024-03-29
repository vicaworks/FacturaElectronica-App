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
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "identificacion", nullable = false, length = 20)
	private String identificacion;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 300)
	@Column(name = "razonsocial", nullable = false, length = 300)
	private String razonsocial;
	
	@Size(max = 300)
	@Column(name = "direccion", length = 300)
	private String direccion;
	
	@Size(max = 300)
	@Column(name = "correoelectronico", length = 300)
	private String correoelectronico;
	
	@Size(max = 20)
	@Column(name = "telefono", length = 20)
	private String telefono;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
	
	@Size(max = 300)
	@Column(name = "nombregarante1", length = 300)
	private String nombregarante1;
	
	@Size(max = 20)
	@Column(name = "cedulagarante1", length = 20)
	private String cedulagarante1;
	
	@Size(max = 300)
	@Column(name = "direcciongarante1", length = 300)
	private String direcciongarante1;
	
	@Size(max = 20)
	@Column(name = "telefonogarante1", length = 20)
	private String telefonogarante1;
	
	@Size(max = 50)
	@Column(name = "ocupaciongarante1", length = 50)
	private String ocupaciongarante1;
	
	@Size(max = 300)
	@Column(name = "nombregarante2", length = 300)
	private String nombregarante2;
	
	@Size(max = 20)
	@Column(name = "cedulagarante2", length = 20)
	private String cedulagarante2;
	
	@Size(max = 300)
	@Column(name = "direcciongarante2", length = 300)
	private String direcciongarante2;
	
	@Size(max = 20)
	@Column(name = "telefonogarante2", length = 20)
	private String telefonogarante2;
	
	@Size(max = 50)
	@Column(name = "ocupaciongarante2", length = 50)
	private String ocupaciongarante2;
	
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
	 * @return the nombregarante1
	 */
	public String getNombregarante1() {
		return nombregarante1;
	}

	/**
	 * @param nombregarante1 the nombregarante1 to set
	 */
	public void setNombregarante1(String nombregarante1) {
		this.nombregarante1 = nombregarante1;
	}

	/**
	 * @return the cedulagarante1
	 */
	public String getCedulagarante1() {
		return cedulagarante1;
	}

	/**
	 * @param cedulagarante1 the cedulagarante1 to set
	 */
	public void setCedulagarante1(String cedulagarante1) {
		this.cedulagarante1 = cedulagarante1;
	}

	/**
	 * @return the direcciongarante1
	 */
	public String getDirecciongarante1() {
		return direcciongarante1;
	}

	/**
	 * @param direcciongarante1 the direcciongarante1 to set
	 */
	public void setDirecciongarante1(String direcciongarante1) {
		this.direcciongarante1 = direcciongarante1;
	}

	/**
	 * @return the telefonogarante1
	 */
	public String getTelefonogarante1() {
		return telefonogarante1;
	}

	/**
	 * @param telefonogarante1 the telefonogarante1 to set
	 */
	public void setTelefonogarante1(String telefonogarante1) {
		this.telefonogarante1 = telefonogarante1;
	}

	/**
	 * @return the ocupaciongarante1
	 */
	public String getOcupaciongarante1() {
		return ocupaciongarante1;
	}

	/**
	 * @param ocupaciongarante1 the ocupaciongarante1 to set
	 */
	public void setOcupaciongarante1(String ocupaciongarante1) {
		this.ocupaciongarante1 = ocupaciongarante1;
	}

	/**
	 * @return the nombregarante2
	 */
	public String getNombregarante2() {
		return nombregarante2;
	}

	/**
	 * @param nombregarante2 the nombregarante2 to set
	 */
	public void setNombregarante2(String nombregarante2) {
		this.nombregarante2 = nombregarante2;
	}

	/**
	 * @return the cedulagarante2
	 */
	public String getCedulagarante2() {
		return cedulagarante2;
	}

	/**
	 * @param cedulagarante2 the cedulagarante2 to set
	 */
	public void setCedulagarante2(String cedulagarante2) {
		this.cedulagarante2 = cedulagarante2;
	}

	/**
	 * @return the direcciongarante2
	 */
	public String getDirecciongarante2() {
		return direcciongarante2;
	}

	/**
	 * @param direcciongarante2 the direcciongarante2 to set
	 */
	public void setDirecciongarante2(String direcciongarante2) {
		this.direcciongarante2 = direcciongarante2;
	}

	/**
	 * @return the telefonogarante2
	 */
	public String getTelefonogarante2() {
		return telefonogarante2;
	}

	/**
	 * @param telefonogarante2 the telefonogarante2 to set
	 */
	public void setTelefonogarante2(String telefonogarante2) {
		this.telefonogarante2 = telefonogarante2;
	}

	/**
	 * @return the ocupaciongarante2
	 */
	public String getOcupaciongarante2() {
		return ocupaciongarante2;
	}

	/**
	 * @param ocupaciongarante2 the ocupaciongarante2 to set
	 */
	public void setOcupaciongarante2(String ocupaciongarante2) {
		this.ocupaciongarante2 = ocupaciongarante2;
	}
}