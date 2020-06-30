/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "retencionimpuestodet")
public class Retencionimpuestodet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2263675842100523294L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idretencionimpuestodet", nullable = false, length = 40)
    private String idretencionimpuestodet;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre", nullable = false, length = 500)
    private String nombre;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;
    
    @JoinColumn(name = "idretencionimpuesto", referencedColumnName = "idretencionimpuesto", nullable = false)
    @ManyToOne(optional = false)
    private Retencionimpuesto retencionimpuesto;
    
    @Transient
    private String display;
    
	/**
	 * 
	 */
	public Retencionimpuestodet() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idretencionimpuestodet != null ? idretencionimpuestodet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Retencionimpuestodet)) {
            return false;
        }
        Retencionimpuestodet other = (Retencionimpuestodet) object;
        if ((this.idretencionimpuestodet == null && other.idretencionimpuestodet != null) || (this.idretencionimpuestodet != null && !this.idretencionimpuestodet.equals(other.idretencionimpuestodet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]", getDisplay() , idretencionimpuestodet);
    }
    
    public String toStringObject() {
    	return PojoUtil.toString(this);
    }

	/**
	 * @return the idretencionimpuestodet
	 */
	public String getIdretencionimpuestodet() {
		return idretencionimpuestodet;
	}

	/**
	 * @param idretencionimpuestodet the idretencionimpuestodet to set
	 */
	public void setIdretencionimpuestodet(String idretencionimpuestodet) {
		this.idretencionimpuestodet = idretencionimpuestodet;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the retencionimpuesto
	 */
	public Retencionimpuesto getRetencionimpuesto() {
		return retencionimpuesto;
	}

	/**
	 * @param retencionimpuesto the retencionimpuesto to set
	 */
	public void setRetencionimpuesto(Retencionimpuesto retencionimpuesto) {
		this.retencionimpuesto = retencionimpuesto;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		display = "";
		if(nombre!=null) {
			display = nombre.length()>130?nombre.substring(0,130):nombre;
		}
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

}
