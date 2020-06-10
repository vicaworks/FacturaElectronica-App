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
@Table(name = "transportista")
public class Transportista implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3547748835937243113L;

	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtransportista", nullable = false, length = 40)
    private String idtransportista;
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
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idtipoidentificacion", referencedColumnName = "idtipoidentificacion", nullable = false)
    private TipoIdentificacion tipoIdentificacion;
    
    @ManyToOne
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa", nullable = false)
    private Empresa empresa;
	
	
	public Transportista() {
		super();
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransportista != null ? idtransportista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transportista)) {
            return false;
        }
        Transportista other = (Transportista) object;
        if ((this.idtransportista == null && other.idtransportista != null) || (this.idtransportista != null && !this.idtransportista.equals(other.idtransportista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",razonsocial , idtransportista);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idtransportista
	 */
	public String getIdtransportista() {
		return idtransportista;
	}

	/**
	 * @param idtransportista the idtransportista to set
	 */
	public void setIdtransportista(String idtransportista) {
		this.idtransportista = idtransportista;
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
	
}
