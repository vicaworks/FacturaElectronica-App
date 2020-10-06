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
@Table(name = "logtransferenciasri")
public class Logtransferenciasri implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8448046133574997767L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idlogtransferenciasri", nullable = false, length = 40)
    private String idlogtransferenciasri;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 32)
    @Column(name = "etiqueta", length = 32)
    private String etiqueta;
    @Size(max = 128)
    @Column(name = "descripcion", length = 128)
    private String descripcion;
    @Size(max = 512)
    @Column(name = "motivo", length = 512)
    private String motivo;
    @Column(name = "idusuario", nullable = false)
    private String idusuario;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    private Cabecera cabecera;
    @ManyToOne(optional = false)
    @JoinColumn(name = "idestadosri", referencedColumnName = "idestadosri", nullable = false)
    private Estadosri estadosri;

	/**
	 * 
	 */
	public Logtransferenciasri() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idlogtransferenciasri != null ? idlogtransferenciasri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Logtransferenciasri)) {
            return false;
        }
        Logtransferenciasri other = (Logtransferenciasri) object;
        if ((this.idlogtransferenciasri == null && other.idlogtransferenciasri != null) || (this.idlogtransferenciasri != null && !this.idlogtransferenciasri.equals(other.idlogtransferenciasri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idlogtransferenciasri
	 */
	public String getIdlogtransferenciasri() {
		return idlogtransferenciasri;
	}

	/**
	 * @param idlogtransferenciasri the idlogtransferenciasri to set
	 */
	public void setIdlogtransferenciasri(String idlogtransferenciasri) {
		this.idlogtransferenciasri = idlogtransferenciasri;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
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
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
	 * @return the estadosri
	 */
	public Estadosri getEstadosri() {
		return estadosri;
	}

	/**
	 * @param estadosri the estadosri to set
	 */
	public void setEstadosri(Estadosri estadosri) {
		this.estadosri = estadosri;
	}

}
