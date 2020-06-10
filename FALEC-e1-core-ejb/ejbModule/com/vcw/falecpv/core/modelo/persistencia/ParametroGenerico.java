/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "parametrogenerico")
public class ParametroGenerico implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3182219447121839357L;
	
	
	@Id
	@Column(name = "idparametrogenerico")
	private String idparametrogenerico;
	
	@Column(name = "concepto")
	private String concepto;
	
	@Column(name = "valor")
	private String valor;	
	
	@Column(name = "descripcion")
	private String descripcion; 

	/**
	 * 
	 */
	public ParametroGenerico() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idparametrogenerico != null ? idparametrogenerico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParametroGenerico)) {
            return false;
        }
        ParametroGenerico other = (ParametroGenerico) object;
        if ((this.idparametrogenerico == null && other.idparametrogenerico != null) || (this.idparametrogenerico != null && !this.idparametrogenerico.equals(other.idparametrogenerico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idparametrogenerico
	 */
	public String getIdparametrogenerico() {
		return idparametrogenerico;
	}

	/**
	 * @param idparametrogenerico the idparametrogenerico to set
	 */
	public void setIdparametrogenerico(String idparametrogenerico) {
		this.idparametrogenerico = idparametrogenerico;
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
