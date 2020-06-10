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
@Table(name = "infoadicional")
public class Infoadicional implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -537326915318629939L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idinfoadicional", nullable = false, length = 40)
    private String idinfoadicional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre", nullable = false, length = 300)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "valor", nullable = false, length = 300)
    private String valor;
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;

	/**
	 * 
	 */
	public Infoadicional() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idinfoadicional != null ? idinfoadicional.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Infoadicional)) {
            return false;
        }
        Infoadicional other = (Infoadicional) object;
        if ((this.idinfoadicional == null && other.idinfoadicional != null) || (this.idinfoadicional != null && !this.idinfoadicional.equals(other.idinfoadicional))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idinfoadicional
	 */
	public String getIdinfoadicional() {
		return idinfoadicional;
	}

	/**
	 * @param idinfoadicional the idinfoadicional to set
	 */
	public void setIdinfoadicional(String idinfoadicional) {
		this.idinfoadicional = idinfoadicional;
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
