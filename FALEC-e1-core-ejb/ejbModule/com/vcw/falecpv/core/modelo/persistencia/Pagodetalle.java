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
@Table(name = "pagodetalle")
public class Pagodetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3831605085198079392L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idpagodetalle", nullable = false, length = 40)
    private String idpagodetalle;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tiporegistro", nullable = false, length = 1)
    private String tiporegistro;
    
    @Size(max = 150)
    @Column(name = "numerodocumento", length = 150)
    private String numerodocumento;
    
    @Size(max = 50)
    @Column(name = "numerocuenta", length = 50)
    private String numerocuenta;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    
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
	
	@Basic(optional = true)
	@Size(min = 1, max = 100)
	@Column(name = "nombrebanco", nullable = true, length = 100)
	private String nombrebanco;
    
    @JoinColumn(name = "idbanco", referencedColumnName = "idbanco")
    @ManyToOne
    private Banco banco;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idadquisicion", referencedColumnName = "idadquisicion", nullable = false)
    private Adquisicion adquisicion;
    
    @JoinColumn(name = "idtipopago", referencedColumnName = "idtipopago", nullable = false)
    @ManyToOne(optional = false)
    private Tipopago tipopago;
	
	
	/**
	 * 
	 */
	public Pagodetalle() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idpagodetalle != null ? idpagodetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pagodetalle)) {
            return false;
        }
        Pagodetalle other = (Pagodetalle) object;
        if ((this.idpagodetalle == null && other.idpagodetalle != null) || (this.idpagodetalle != null && !this.idpagodetalle.equals(other.idpagodetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idpagodetalle
	 */
	public String getIdpagodetalle() {
		return idpagodetalle;
	}

	/**
	 * @param idpagodetalle the idpagodetalle to set
	 */
	public void setIdpagodetalle(String idpagodetalle) {
		this.idpagodetalle = idpagodetalle;
	}

	/**
	 * @return the tiporegistro
	 */
	public String getTiporegistro() {
		return tiporegistro;
	}

	/**
	 * @param tiporegistro the tiporegistro to set
	 */
	public void setTiporegistro(String tiporegistro) {
		this.tiporegistro = tiporegistro;
	}

	/**
	 * @return the numerodocumento
	 */
	public String getNumerodocumento() {
		return numerodocumento;
	}

	/**
	 * @param numerodocumento the numerodocumento to set
	 */
	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	/**
	 * @return the numerocuenta
	 */
	public String getNumerocuenta() {
		return numerocuenta;
	}

	/**
	 * @param numerocuenta the numerocuenta to set
	 */
	public void setNumerocuenta(String numerocuenta) {
		this.numerocuenta = numerocuenta;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	 * @return the banco
	 */
	public Banco getBanco() {
		return banco;
	}

	/**
	 * @param banco the banco to set
	 */
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	/**
	 * @return the adquisicion
	 */
	public Adquisicion getAdquisicion() {
		return adquisicion;
	}

	/**
	 * @param adquisicion the adquisicion to set
	 */
	public void setAdquisicion(Adquisicion adquisicion) {
		this.adquisicion = adquisicion;
	}

	/**
	 * @return the tipopago
	 */
	public Tipopago getTipopago() {
		return tipopago;
	}

	/**
	 * @param tipopago the tipopago to set
	 */
	public void setTipopago(Tipopago tipopago) {
		this.tipopago = tipopago;
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
	 * @return the nombrebanco
	 */
	public String getNombrebanco() {
		return nombrebanco;
	}

	/**
	 * @param nombrebanco the nombrebanco to set
	 */
	public void setNombrebanco(String nombrebanco) {
		this.nombrebanco = nombrebanco;
	}
	
}
