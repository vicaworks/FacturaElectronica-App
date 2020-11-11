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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "configuracion")
public class Configuracion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7921670212181927184L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idconfiguracion", nullable = false, length = 40)
    private String idconfiguracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "concepto", nullable = false, length = 200)
    private String concepto;
    @Size(max = 3000)
    @Column(name = "valor", length = 3000)
    private String valor;
    @Basic(optional = true)
    @Size(min = 1, max = 100)
    @Column(name = "etiqueta", nullable = true, length = 100)
    private String etiqueta;
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento establecimiento;
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante")
    @ManyToOne
    private Tipocomprobante tipocomprobante;
    @JoinColumn(name = "idtipoconfiguracion", referencedColumnName = "idtipoconfiguracion", nullable = false)
    @ManyToOne(optional = false)
    private Tipoconfiguracion tipoconfiguracion;

	public Configuracion() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idconfiguracion != null ? idconfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Configuracion)) {
            return false;
        }
        Configuracion other = (Configuracion) object;
        if ((this.idconfiguracion == null && other.idconfiguracion != null) || (this.idconfiguracion != null && !this.idconfiguracion.equals(other.idconfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idconfiguracion
	 */
	public String getIdconfiguracion() {
		return idconfiguracion;
	}

	/**
	 * @param idconfiguracion the idconfiguracion to set
	 */
	public void setIdconfiguracion(String idconfiguracion) {
		this.idconfiguracion = idconfiguracion;
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
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
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the tipoconfiguracion
	 */
	public Tipoconfiguracion getTipoconfiguracion() {
		return tipoconfiguracion;
	}

	/**
	 * @param tipoconfiguracion the tipoconfiguracion to set
	 */
	public void setTipoconfiguracion(Tipoconfiguracion tipoconfiguracion) {
		this.tipoconfiguracion = tipoconfiguracion;
	}

}
