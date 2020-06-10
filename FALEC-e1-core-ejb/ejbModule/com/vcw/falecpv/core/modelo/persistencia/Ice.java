/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "ice")
public class Ice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3072760813740867189L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idice", nullable = false, length = 40)
    private String idice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codigo", nullable = false, length = 4)
    private String codigo;
    @Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "codigoice", nullable = true, length = 1)
	private String codigoIce;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;
    @Size(max = 10)
    @Column(name = "tarifaadvalorem", length = 10)
    private String tarifaadvalorem;
    @Size(max = 100)
    @Column(name = "tarifaespecifica", length = 100)
    private String tarifaespecifica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;
	@Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 6, scale = 3)
    private BigDecimal valor;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "idempresa",referencedColumnName = "idempresa")
    private Empresa empresa;
	
	@Transient
	private boolean error = false;
	@Transient
	private String novedad;
	@Transient
	private int fila;

	/**
	 * 
	 */
	public Ice() {
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idice != null ? idice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ice)) {
            return false;
        }
        Ice other = (Ice) object;
        if ((this.idice == null && other.idice != null) || (this.idice != null && !this.idice.equals(other.idice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s, %s]", idice, descripcion);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idice
	 */
	public String getIdice() {
		return idice;
	}

	/**
	 * @param idice the idice to set
	 */
	public void setIdice(String idice) {
		this.idice = idice;
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
	 * @return the tarifaadvalorem
	 */
	public String getTarifaadvalorem() {
		return tarifaadvalorem;
	}

	/**
	 * @param tarifaadvalorem the tarifaadvalorem to set
	 */
	public void setTarifaadvalorem(String tarifaadvalorem) {
		this.tarifaadvalorem = tarifaadvalorem;
	}

	/**
	 * @return the tarifaespecifica
	 */
	public String getTarifaespecifica() {
		return tarifaespecifica;
	}

	/**
	 * @param tarifaespecifica the tarifaespecifica to set
	 */
	public void setTarifaespecifica(String tarifaespecifica) {
		this.tarifaespecifica = tarifaespecifica;
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
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getNovedad() {
		return novedad;
	}

	public void setNovedad(String novedad) {
		this.novedad = novedad;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public String getCodigoIce() {
		return codigoIce;
	}

	public void setCodigoIce(String codigoIce) {
		this.codigoIce = codigoIce;
	}	

}
