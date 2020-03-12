/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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
@Table(name = "empresa")
public class Empresa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -214869066431730178L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idEmpresa", nullable = false, length = 40)
    private String idEmpresa;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "ruc", nullable = false, length = 13)
    private String ruc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "razonSocial", nullable = false, length = 300)
    private String razonSocial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreComercial", nullable = false, length = 300)
    private String nombreComercial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "direccionMatriz", nullable = false, length = 300)
    private String direccionMatriz;
    
    @Size(max = 2)
    @Column(name = "obligadoContablidad", length = 2)
    private String obligadoContablidad;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ambiente", nullable = false, length = 1)
    private String ambiente;
    
    @Size(max = 20)
    @Column(name = "claveFirmaElectronica", length = 20)
    private String claveFirmaElectronica;
    
    @Lob
    @Column(name = "archivoFirmaElectronica")    
    private byte[] archivoFirmaElectronica;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idUsuario", nullable = false, length = 40)
    private String idUsuario;
    
    @OneToMany(mappedBy = "empresa",fetch = FetchType.LAZY)
    private List<Establecimiento> establecimientoList;
    
    public Empresa() {
    	
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idEmpresa
	 */
	public String getIdEmpresa() {
		return idEmpresa;
	}

	/**
	 * @param idEmpresa the idEmpresa to set
	 */
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the nombreComercial
	 */
	public String getNombreComercial() {
		return nombreComercial;
	}

	/**
	 * @param nombreComercial the nombreComercial to set
	 */
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	/**
	 * @return the direccionMatriz
	 */
	public String getDireccionMatriz() {
		return direccionMatriz;
	}

	/**
	 * @param direccionMatriz the direccionMatriz to set
	 */
	public void setDireccionMatriz(String direccionMatriz) {
		this.direccionMatriz = direccionMatriz;
	}

	/**
	 * @return the obligadoContablidad
	 */
	public String getObligadoContablidad() {
		return obligadoContablidad;
	}

	/**
	 * @param obligadoContablidad the obligadoContablidad to set
	 */
	public void setObligadoContablidad(String obligadoContablidad) {
		this.obligadoContablidad = obligadoContablidad;
	}

	/**
	 * @return the ambiente
	 */
	public String getAmbiente() {
		return ambiente;
	}

	/**
	 * @param ambiente the ambiente to set
	 */
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * @return the claveFirmaElectronica
	 */
	public String getClaveFirmaElectronica() {
		return claveFirmaElectronica;
	}

	/**
	 * @param claveFirmaElectronica the claveFirmaElectronica to set
	 */
	public void setClaveFirmaElectronica(String claveFirmaElectronica) {
		this.claveFirmaElectronica = claveFirmaElectronica;
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
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the establecimientoList
	 */
	public List<Establecimiento> getEstablecimientoList() {
		return establecimientoList;
	}

	/**
	 * @param establecimientoList the establecimientoList to set
	 */
	public void setEstablecimientoList(List<Establecimiento> establecimientoList) {
		this.establecimientoList = establecimientoList;
	}

	/**
	 * @return the archivoFirmaElectronica
	 */
	public byte[] getArchivoFirmaElectronica() {
		return archivoFirmaElectronica;
	}

	/**
	 * @param archivoFirmaElectronica the archivoFirmaElectronica to set
	 */
	public void setArchivoFirmaElectronica(byte[] archivoFirmaElectronica) {
		this.archivoFirmaElectronica = archivoFirmaElectronica;
	}

}
