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

import org.hibernate.annotations.Type;

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
    @Column(name = "idempresa", nullable = false, length = 40)
    private String idempresa;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "ruc", nullable = false, length = 13)
    private String ruc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "razonsocial", nullable = false, length = 300)
    private String razonsocial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombrecomercial", nullable = false, length = 300)
    private String nombrecomercial;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "direccionmatriz", nullable = false, length = 300)
    private String direccionmatriz;
    
    @Size(max = 2)
    @Column(name = "obligadocontablidad", length = 2)
    private String obligadocontablidad;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ambiente", nullable = false, length = 1)
    private String ambiente;
    
    @Size(max = 128)
    @Column(name = "clavefirmaelectronica", length = 128)
    private String clavefirmaelectronica;
    
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "archivofirmaelectronica")
    private byte[] archivofirmaelectronica;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombrearchivo", nullable = true, length = 300)
    private String nombrearchivo;
    
    @Basic(optional = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "fechavigencia")
    private Date  fechavigencia;
    
    @OneToMany(mappedBy = "empresa",fetch = FetchType.LAZY)
    private List<Establecimiento> establecimientoList;
    
    public Empresa() {
    	
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idempresa != null ? idempresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idempresa == null && other.idempresa != null) || (this.idempresa != null && !this.idempresa.equals(other.idempresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idempresa
	 */
	public String getIdempresa() {
		return idempresa;
	}

	/**
	 * @param idempresa the idempresa to set
	 */
	public void setIdempresa(String idempresa) {
		this.idempresa = idempresa;
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
	 * @return the direccionmatriz
	 */
	public String getDireccionmatriz() {
		return direccionmatriz;
	}

	/**
	 * @param direccionmatriz the direccionmatriz to set
	 */
	public void setDireccionmatriz(String direccionmatriz) {
		this.direccionmatriz = direccionmatriz;
	}

	/**
	 * @return the obligadocontablidad
	 */
	public String getObligadocontablidad() {
		return obligadocontablidad;
	}

	/**
	 * @param obligadocontablidad the obligadocontablidad to set
	 */
	public void setObligadocontablidad(String obligadocontablidad) {
		this.obligadocontablidad = obligadocontablidad;
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
	 * @return the clavefirmaelectronica
	 */
	public String getClavefirmaelectronica() {
		return clavefirmaelectronica;
	}

	/**
	 * @param clavefirmaelectronica the clavefirmaelectronica to set
	 */
	public void setClavefirmaelectronica(String clavefirmaelectronica) {
		this.clavefirmaelectronica = clavefirmaelectronica;
	}

	/**
	 * @return the archivofirmaelectronica
	 */
	public byte[] getArchivofirmaelectronica() {
		return archivofirmaelectronica;
	}

	/**
	 * @param archivofirmaelectronica the archivofirmaelectronica to set
	 */
	public void setArchivofirmaelectronica(byte[] archivofirmaelectronica) {
		this.archivofirmaelectronica = archivofirmaelectronica;
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
	 * @return the fechavigencia
	 */
	public Date getFechavigencia() {
		return fechavigencia;
	}

	/**
	 * @param fechavigencia the fechavigencia to set
	 */
	public void setFechavigencia(Date fechavigencia) {
		this.fechavigencia = fechavigencia;
	}

	/**
	 * @return the nombrearchivo
	 */
	public String getNombrearchivo() {
		return nombrearchivo;
	}

	/**
	 * @param nombrearchivo the nombrearchivo to set
	 */
	public void setNombrearchivo(String nombrearchivo) {
		this.nombrearchivo = nombrearchivo;
	}

	

}
