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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "iva")
public class Iva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1510963512187186133L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idiva", nullable = false, length = 40)
	private String idiva;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "codigo", nullable = false, length = 1)
	private String codigo;
	
	@Basic(optional = true)
	@Size(min = 1, max = 10)
	@Column(name = "codigoiva", nullable = true, length = 10)
	private String codigoIva;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "porcentaje", nullable = false, length = 32)
	private String porcentaje;
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
	@Column(name = "defecto", nullable = false)
	private Integer defecto;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ordenfactura", nullable = false)
	private Integer ordenfactura;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "labelfactura", nullable = false, length = 150)
	private String labelfactura;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "estado", nullable = false, length = 1)
	private String estado;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 6, scale = 3)
    private BigDecimal valor;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "idempresa",referencedColumnName = "idempresa")
    private Empresa empresa;

	/**
	 * 
	 */
	public Iva() {

	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idiva != null ? idiva.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Iva)) {
			return false;
		}
		Iva other = (Iva) object;
		if ((this.idiva == null && other.idiva != null) || (this.idiva != null && !this.idiva.equals(other.idiva))) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
    	return String.format("[%s, %s]", idiva, porcentaje);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idiva
	 */
	public String getIdiva() {
		return idiva;
	}

	/**
	 * @param idiva the idiva to set
	 */
	public void setIdiva(String idiva) {
		this.idiva = idiva;
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
	 * @return the porcentaje
	 */
	public String getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
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
	 * @return the defecto
	 */
	public Integer getDefecto() {
		return defecto;
	}

	/**
	 * @param defecto the defecto to set
	 */
	public void setDefecto(Integer defecto) {
		this.defecto = defecto;
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

	public String getCodigoIva() {
		return codigoIva;
	}

	public void setCodigoIva(String codigoIva) {
		this.codigoIva = codigoIva;
	}

	/**
	 * @return the ordenfactura
	 */
	public Integer getOrdenfactura() {
		return ordenfactura;
	}

	/**
	 * @param ordenfactura the ordenfactura to set
	 */
	public void setOrdenfactura(Integer ordenfactura) {
		this.ordenfactura = ordenfactura;
	}

	/**
	 * @return the labelfactura
	 */
	public String getLabelfactura() {
		return labelfactura;
	}

	/**
	 * @param labelfactura the labelfactura to set
	 */
	public void setLabelfactura(String labelfactura) {
		this.labelfactura = labelfactura;
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

	
	

}
