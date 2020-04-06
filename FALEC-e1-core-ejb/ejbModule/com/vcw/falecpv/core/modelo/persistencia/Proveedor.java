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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "proveedor")
public class Proveedor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4527200820344627020L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idproveedor", nullable = false, length = 40)
    private String idproveedor;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "razonsocial", nullable = false, length = 200)
    private String razonsocial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombrecomercial", nullable = false, length = 200)
    private String nombrecomercial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;
    
    @Size(max = 50)
    @Column(name = "telefono", length = 50)
    private String telefono;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "email", length = 60)
    private String email;
    
    @Size(max = 200)
    @Column(name = "contactonombre", length = 200)
    private String contactonombre;
    
    @Size(max = 60)
    @Column(name = "contactotelefono", length = 60)
    private String contactotelefono;
    
    @Size(max = 60)
    @Column(name = "contactoemail", length = 60)
    private String contactoemail;
    
    @Size(max = 100)
    @Column(name = "provincia", length = 100)
    private String provincia;
    
    @Size(max = 100)
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Size(max = 200)
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Size(max = 40)
    @Column(name = "codigopostal", length = 40)
    private String codigopostal;
    
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
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idtipoidentificacion", referencedColumnName = "idtipoidentificacion", nullable = false)
    @ManyToOne(optional = false)
    private TipoIdentificacion tipoidentificacion;

	/**
	 * 
	 */
	public Proveedor() {
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
	public String toStringObject() {
		return PojoUtil.toString(this);
	}
	
	 @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproveedor != null ? idproveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.idproveedor == null && other.idproveedor != null) || (this.idproveedor != null && !this.idproveedor.equals(other.idproveedor))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idproveedor
	 */
	public String getIdproveedor() {
		return idproveedor;
	}

	/**
	 * @param idproveedor the idproveedor to set
	 */
	public void setIdproveedor(String idproveedor) {
		this.idproveedor = idproveedor;
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
	 * @return the nombrecomercial
	 */
	public String getNombrecomercial() {
		return nombrecomercial;
	}

	/**
	 * @param nombrecomercial the nombrecomercial to set
	 */
	public void setNombrecomercial(String nombrecomercial) {
		this.nombrecomercial = nombrecomercial;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the contactonombre
	 */
	public String getContactonombre() {
		return contactonombre;
	}

	/**
	 * @param contactonombre the contactonombre to set
	 */
	public void setContactonombre(String contactonombre) {
		this.contactonombre = contactonombre;
	}

	/**
	 * @return the contactotelefono
	 */
	public String getContactotelefono() {
		return contactotelefono;
	}

	/**
	 * @param contactotelefono the contactotelefono to set
	 */
	public void setContactotelefono(String contactotelefono) {
		this.contactotelefono = contactotelefono;
	}

	/**
	 * @return the contactoemail
	 */
	public String getContactoemail() {
		return contactoemail;
	}

	/**
	 * @param contactoemail the contactoemail to set
	 */
	public void setContactoemail(String contactoemail) {
		this.contactoemail = contactoemail;
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
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
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
	 * @return the codigopostal
	 */
	public String getCodigopostal() {
		return codigopostal;
	}

	/**
	 * @param codigopostal the codigopostal to set
	 */
	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
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
	 * @return the tipoidentificacion
	 */
	public TipoIdentificacion getTipoidentificacion() {
		return tipoidentificacion;
	}

	/**
	 * @param tipoidentificacion the tipoidentificacion to set
	 */
	public void setTipoidentificacion(TipoIdentificacion tipoidentificacion) {
		this.tipoidentificacion = tipoidentificacion;
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

}
