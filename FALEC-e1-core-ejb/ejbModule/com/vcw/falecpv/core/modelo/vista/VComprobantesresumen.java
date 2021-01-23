/**
 * 
 */
package com.vcw.falecpv.core.modelo.vista;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "v_comprobantesresumen")
@XmlRootElement
public class VComprobantesresumen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4229473724685836258L;
	
	@Id
	@Size(max = 40)
    @Column(name = "idcabecera", length = 40)
    private String idcabecera;
    @Size(max = 40)
    @Column(name = "idempresa", length = 40)
    private String idempresa;
    @Size(max = 300)
    @Column(name = "razonsocial", length = 300)
    private String razonsocial;
    @Size(max = 40)
    @Column(name = "idestablecimiento", length = 40)
    private String idestablecimiento;
    @Size(max = 300)
    @Column(name = "nombrecomercial", length = 300)
    private String nombrecomercial;
    @Size(max = 1)
    @Column(name = "ambiente", length = 1)
    private String ambiente;
    @Size(max = 40)
    @Column(name = "idtipocomprobante", length = 40)
    private String idtipocomprobante;
    @Size(max = 100)
    @Column(name = "identificador", length = 100)
    private String identificador;
    @Size(max = 200)
    @Column(name = "comprobante", length = 200)
    private String comprobante;
    @Column(name = "fechaemision")
    @Temporal(TemporalType.DATE)
    private Date fechaemision;
    @Column(name = "fechaautorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaautorizacion;
    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado;
    @Size(max = 2)
    @Column(name = "estadoautorizacion", length = 2)
    private String estadoautorizacion;
    @Size(max = 40)
    @Column(name = "idusuario", length = 40)
    private String idusuario;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    public VComprobantesresumen() {
    	
    }
    
    @Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcabecera != null ? idcabecera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VComprobantesresumen)) {
            return false;
        }
        VComprobantesresumen other = (VComprobantesresumen) object;
        if ((this.idcabecera == null && other.idcabecera != null) || (this.idcabecera != null && !this.idcabecera.equals(other.idcabecera))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idcabecera
	 */
	public String getIdcabecera() {
		return idcabecera;
	}

	/**
	 * @param idcabecera the idcabecera to set
	 */
	public void setIdcabecera(String idcabecera) {
		this.idcabecera = idcabecera;
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
	 * @return the idestablecimiento
	 */
	public String getIdestablecimiento() {
		return idestablecimiento;
	}

	/**
	 * @param idestablecimiento the idestablecimiento to set
	 */
	public void setIdestablecimiento(String idestablecimiento) {
		this.idestablecimiento = idestablecimiento;
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
	 * @return the idtipocomprobante
	 */
	public String getIdtipocomprobante() {
		return idtipocomprobante;
	}

	/**
	 * @param idtipocomprobante the idtipocomprobante to set
	 */
	public void setIdtipocomprobante(String idtipocomprobante) {
		this.idtipocomprobante = idtipocomprobante;
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
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the fechaemision
	 */
	public Date getFechaemision() {
		return fechaemision;
	}

	/**
	 * @param fechaemision the fechaemision to set
	 */
	public void setFechaemision(Date fechaemision) {
		this.fechaemision = fechaemision;
	}

	/**
	 * @return the fechaautorizacion
	 */
	public Date getFechaautorizacion() {
		return fechaautorizacion;
	}

	/**
	 * @param fechaautorizacion the fechaautorizacion to set
	 */
	public void setFechaautorizacion(Date fechaautorizacion) {
		this.fechaautorizacion = fechaautorizacion;
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
	 * @return the estadoautorizacion
	 */
	public String getEstadoautorizacion() {
		return estadoautorizacion;
	}

	/**
	 * @param estadoautorizacion the estadoautorizacion to set
	 */
	public void setEstadoautorizacion(String estadoautorizacion) {
		this.estadoautorizacion = estadoautorizacion;
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
    
    

}
