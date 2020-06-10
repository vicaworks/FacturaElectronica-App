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
@Table(name = "parametroempresa")
public class ParametroGenericoEmpresa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4968008098828734957L;
	
	@Id
	@Column(name = "id")
	private String id;
	

	
	@Column(name = "idparametroempresa")
	private String idparametroempresa;
	
	@Column(name = "idempresa")
	private String idempresa;
	
	@Column(name = "idestablecimiento")
	private String idestablecimiento;
	
	@Column(name = "concepto")
	private String concepto;
	
	@Column(name = "valor")
	private String valor;	
	
	@Column(name = "descripcion")
	private String descripcion; 

	/**
	 * 
	 */
	public ParametroGenericoEmpresa() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idparametroempresa != null ? idparametroempresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParametroGenericoEmpresa)) {
            return false;
        }
        ParametroGenericoEmpresa other = (ParametroGenericoEmpresa) object;
        if ((this.idparametroempresa == null && other.idparametroempresa != null) || (this.idparametroempresa != null && !this.idparametroempresa.equals(other.idparametroempresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idparametroempresa
	 */
	public String getIdparametroempresa() {
		return idparametroempresa;
	}

	/**
	 * @param idparametroempresa the idparametroempresa to set
	 */
	public void setIdparametroempresa(String idparametroempresa) {
		this.idparametroempresa = idparametroempresa;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}
