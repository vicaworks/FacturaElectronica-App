/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "tipopago")
public class Tipopago implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919157765122914626L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idtipopago", nullable = false, length = 40)
    private String idtipopago;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "subdetalle", nullable = false, length = 1)
    private String subdetalle;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "formulario", nullable = false, length = 100)
    private String formulario;
    
    
    @Size(max = 4)
    @Column(name = "codinterno", length = 4)
    private String codinterno;
    
    @Size(max = 2)
    @Column(name = "codigo", length = 2)
    private String codigo;
    
    @Column(name = "orden")
    private Integer orden;
    
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    
	/**
	 * 
	 */
	public Tipopago() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipopago != null ? idtipopago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tipopago)) {
            return false;
        }
        Tipopago other = (Tipopago) object;
        if ((this.idtipopago == null && other.idtipopago != null) || (this.idtipopago != null && !this.idtipopago.equals(other.idtipopago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",nombre , idtipopago);
    }

	/**
	 * @return the idtipopago
	 */
	public String getIdtipopago() {
		return idtipopago;
	}

	/**
	 * @param idtipopago the idtipopago to set
	 */
	public void setIdtipopago(String idtipopago) {
		this.idtipopago = idtipopago;
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
	 * @return the subdetalle
	 */
	public String getSubdetalle() {
		return subdetalle;
	}

	/**
	 * @param subdetalle the subdetalle to set
	 */
	public void setSubdetalle(String subdetalle) {
		this.subdetalle = subdetalle;
	}

	/**
	 * @return the formulario
	 */
	public String getFormulario() {
		return formulario;
	}

	/**
	 * @param formulario the formulario to set
	 */
	public void setFormulario(String formulario) {
		this.formulario = formulario;
	}

	/**
	 * @return the codinterno
	 */
	public String getCodinterno() {
		return codinterno;
	}

	/**
	 * @param codinterno the codinterno to set
	 */
	public void setCodinterno(String codinterno) {
		this.codinterno = codinterno;
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
	 * @return the orden
	 */
	public Integer getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
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

}
