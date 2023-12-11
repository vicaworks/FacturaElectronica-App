/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * 
 */
@Entity
@Table(name = "cabeceraadjunto")
public class Cabeceraadjunto implements Serializable {

	private static final long serialVersionUID = -7973842568173159825L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idcabeceraadjunto", nullable = false, length = 40)
    private String idcabeceraadjunto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombreadjunto", nullable = false, length = 500)
    private String nombreadjunto;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "stream", nullable = false)
    private byte[] stream;
    
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
	
	/**
	 * 
	 */
	public Cabeceraadjunto() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcabeceraadjunto != null ? idcabeceraadjunto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cabeceraadjunto)) {
            return false;
        }
        Cabeceraadjunto other = (Cabeceraadjunto) object;
        if ((this.idcabeceraadjunto == null && other.idcabeceraadjunto != null) || (this.idcabeceraadjunto != null && !this.idcabeceraadjunto.equals(other.idcabeceraadjunto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idcabeceraadjunto
	 */
	public String getIdcabeceraadjunto() {
		return idcabeceraadjunto;
	}

	/**
	 * @param idcabeceraadjunto the idcabeceraadjunto to set
	 */
	public void setIdcabeceraadjunto(String idcabeceraadjunto) {
		this.idcabeceraadjunto = idcabeceraadjunto;
	}

	/**
	 * @return the nombreadjunto
	 */
	public String getNombreadjunto() {
		return nombreadjunto;
	}

	/**
	 * @param nombreadjunto the nombreadjunto to set
	 */
	public void setNombreadjunto(String nombreadjunto) {
		this.nombreadjunto = nombreadjunto;
	}

	/**
	 * @return the stream
	 */
	public byte[] getStream() {
		return stream;
	}

	/**
	 * @param stream the stream to set
	 */
	public void setStream(byte[] stream) {
		this.stream = stream;
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

}
