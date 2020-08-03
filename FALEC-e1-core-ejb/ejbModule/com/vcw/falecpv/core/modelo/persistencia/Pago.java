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
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "pago")
public class Pago implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4661781844746631777L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idpago", nullable = false, length = 40)
    private String idpago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total =  BigDecimal.ZERO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazo", nullable = false, precision = 12, scale = 2)
    private BigDecimal plazo =  BigDecimal.ZERO;
    @Size(max = 10)
    @Column(name = "unidadtiempo", length = 10)
    private String unidadtiempo;
    @Size(max = 150)
    @Column(name = "numerodocumento", length = 150)
    private String numerodocumento;
    @Size(max = 100)
    @Column(name = "nombrebanco", length = 100)
    private String nombrebanco;
    @Size(max = 50)
    @Column(name = "numerocuenta", length = 50)
    private String numerocuenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cambio", nullable = false, precision = 12, scale = 2)
    private BigDecimal cambio =  BigDecimal.ZERO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorentrega", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorentrega =  BigDecimal.ZERO;
    
    @Basic(optional = true)
    @Column(name = "fechapago", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechapago;
    
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = true)
    @ManyToOne(optional = true)
    private Cabecera cabecera;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idadquisicion", referencedColumnName = "idadquisicion", nullable = true)
    private Adquisicion adquisicion;
    
    @JoinColumn(name = "idtipopago", referencedColumnName = "idtipopago", nullable = false)
    @ManyToOne(optional = false)
    private Tipopago tipopago;
    
    @Transient
    private TipoPagoFormularioEnum tipoPagoFormularioEnum;

	public Pago() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idpago != null ? idpago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idpago == null && other.idpago != null) || (this.idpago != null && !this.idpago.equals(other.idpago))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idpago
	 */
	public String getIdpago() {
		return idpago;
	}

	/**
	 * @param idpago the idpago to set
	 */
	public void setIdpago(String idpago) {
		this.idpago = idpago;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the plazo
	 */
	public BigDecimal getPlazo() {
		return plazo;
	}

	/**
	 * @param plazo the plazo to set
	 */
	public void setPlazo(BigDecimal plazo) {
		this.plazo = plazo;
	}

	/**
	 * @return the unidadtiempo
	 */
	public String getUnidadtiempo() {
		return unidadtiempo;
	}

	/**
	 * @param unidadtiempo the unidadtiempo to set
	 */
	public void setUnidadtiempo(String unidadtiempo) {
		this.unidadtiempo = unidadtiempo;
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
	 * @return the cambio
	 */
	public BigDecimal getCambio() {
		return cambio;
	}

	/**
	 * @param cambio the cambio to set
	 */
	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	/**
	 * @return the valorentrega
	 */
	public BigDecimal getValorentrega() {
		return valorentrega;
	}

	/**
	 * @param valorentrega the valorentrega to set
	 */
	public void setValorentrega(BigDecimal valorentrega) {
		this.valorentrega = valorentrega;
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
	 * @return the tipoPagoFormularioEnum
	 */
	public TipoPagoFormularioEnum getTipoPagoFormularioEnum() {
		return tipoPagoFormularioEnum;
	}

	/**
	 * @param tipoPagoFormularioEnum the tipoPagoFormularioEnum to set
	 */
	public void setTipoPagoFormularioEnum(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		this.tipoPagoFormularioEnum = tipoPagoFormularioEnum;
	}

	/**
	 * @return the fechapago
	 */
	public Date getFechapago() {
		return fechapago;
	}

	/**
	 * @param fechapago the fechapago to set
	 */
	public void setFechapago(Date fechapago) {
		this.fechapago = fechapago;
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

}
