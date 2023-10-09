package com.vcw.falecpv.core.modelo.vista;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

@Entity
@Table(name = "v_comprobantespagarcredito")
public class VComprobantespagarcredito implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4189923306899283042L;
	
	@Id
	@Size(max = 40)
    @Column(name = "idcabecera", length = 40)
    private String idcabecera;
    @Size(max = 40)
    @Column(name = "idestablecimiento", length = 40)
    private String idestablecimiento;
    @Size(max = 2147483647)
    @Column(name = "idtipocomprobante", length = 2147483647)
    private String idtipocomprobante;
    @Size(max = 2147483647)
    @Column(name = "comprobante", length = 2147483647)
    private String comprobante;
    @Column(name = "fechaemision")
    @Temporal(TemporalType.DATE)
    private Date fechaemision;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 40)
    @Column(name = "idusuario", length = 40)
    private String idusuario;
    @Size(max = 2147483647)
    @Column(name = "numdocumento", length = 2147483647)
    private String numdocumento;
    @Size(max = 40)
    @Column(name = "idcliente", length = 40)
    private String idcliente;
    @Size(max = 20)
    @Column(name = "identificacion", length = 20)
    private String identificacion;
    @Size(max = 200)
    @Column(name = "razonsocial", length = 200)
    private String razonsocial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalsinimpuestos", precision = 12, scale = 2)
    private BigDecimal totalsinimpuestos;
    @Column(name = "totaldescuento", precision = 12, scale = 2)
    private BigDecimal totaldescuento;
    @Column(name = "totalice", precision = 12, scale = 2)
    private BigDecimal totalice;
    @Column(name = "totaliva", precision = 12, scale = 2)
    private BigDecimal totaliva;
    @Column(name = "totalconimpuestos", precision = 12, scale = 2)
    private BigDecimal totalconimpuestos;
    @Column(name = "totalretencion", precision = 12, scale = 2)
    private BigDecimal totalretencion;
    @Column(name = "valorapagar", precision = 12, scale = 2)
    private BigDecimal valorapagar;
    @Column(name = "totalpago")
    private BigDecimal totalpago;
    @Column(name = "fechapago")
    @Temporal(TemporalType.DATE)
    private Date fechapago;
    @Column(name = "abono")
    private BigDecimal abono;
    
    @Transient
    private List<Pago> pagoList;
    
    @Transient
    private List<Pago> pagoOtrosList;
    
    @Transient
    private String filtroComprobante;
    
    @Transient
    private String filtroProveedor;
    
    @Transient
    private BigDecimal totalOtrosPagos;
    
    public VComprobantespagarcredito() {
    }
    
    @Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcabecera != null ? idcabecera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cabecera)) {
            return false;
        }
        VComprobantespagarcredito other = (VComprobantespagarcredito) object;
        if ((this.idcabecera == null && other.idcabecera != null) || (this.idcabecera != null && !this.idcabecera.equals(other.idcabecera))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the idcabecera
	 */
	public String getIdcabecera() {
		return idcabecera;
	}

	/**
	 * @param idcabecera the idcabecera to set
	 */
	public void setIdcabecera(String idcabecera) {
		this.idcabecera = idcabecera;
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
	 * @return the idtipocomprobante
	 */
	public String getIdtipocomprobante() {
		return idtipocomprobante;
	}

	/**
	 * @param idtipocomprobante the idtipocomprobante to set
	 */
	public void setIdtipocomprobante(String idtipocomprobante) {
		this.idtipocomprobante = idtipocomprobante;
	}

	/**
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the fechaemision
	 */
	public Date getFechaemision() {
		return fechaemision;
	}

	/**
	 * @param fechaemision the fechaemision to set
	 */
	public void setFechaemision(Date fechaemision) {
		this.fechaemision = fechaemision;
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
	 * @return the numdocumento
	 */
	public String getNumdocumento() {
		return numdocumento;
	}

	/**
	 * @param numdocumento the numdocumento to set
	 */
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}


	/**
	 * @return the identificacion
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * @param identificacion the identificacion to set
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	/**
	 * @return the razonsocial
	 */
	public String getRazonsocial() {
		return razonsocial;
	}

	/**
	 * @param razonsocial the razonsocial to set
	 */
	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}

	/**
	 * @return the totalsinimpuestos
	 */
	public BigDecimal getTotalsinimpuestos() {
		return totalsinimpuestos;
	}

	/**
	 * @param totalsinimpuestos the totalsinimpuestos to set
	 */
	public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
		this.totalsinimpuestos = totalsinimpuestos;
	}

	/**
	 * @return the totaldescuento
	 */
	public BigDecimal getTotaldescuento() {
		return totaldescuento;
	}

	/**
	 * @param totaldescuento the totaldescuento to set
	 */
	public void setTotaldescuento(BigDecimal totaldescuento) {
		this.totaldescuento = totaldescuento;
	}

	/**
	 * @return the totalice
	 */
	public BigDecimal getTotalice() {
		return totalice;
	}

	/**
	 * @param totalice the totalice to set
	 */
	public void setTotalice(BigDecimal totalice) {
		this.totalice = totalice;
	}

	/**
	 * @return the totaliva
	 */
	public BigDecimal getTotaliva() {
		return totaliva;
	}

	/**
	 * @param totaliva the totaliva to set
	 */
	public void setTotaliva(BigDecimal totaliva) {
		this.totaliva = totaliva;
	}

	/**
	 * @return the totalconimpuestos
	 */
	public BigDecimal getTotalconimpuestos() {
		return totalconimpuestos;
	}

	/**
	 * @param totalconimpuestos the totalconimpuestos to set
	 */
	public void setTotalconimpuestos(BigDecimal totalconimpuestos) {
		this.totalconimpuestos = totalconimpuestos;
	}

	/**
	 * @return the totalretencion
	 */
	public BigDecimal getTotalretencion() {
		return totalretencion;
	}

	/**
	 * @param totalretencion the totalretencion to set
	 */
	public void setTotalretencion(BigDecimal totalretencion) {
		this.totalretencion = totalretencion;
	}

	/**
	 * @return the valorapagar
	 */
	public BigDecimal getValorapagar() {
		return valorapagar;
	}

	/**
	 * @param valorapagar the valorapagar to set
	 */
	public void setValorapagar(BigDecimal valorapagar) {
		this.valorapagar = valorapagar;
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
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the pagoOtrosList
	 */
	public List<Pago> getPagoOtrosList() {
		return pagoOtrosList;
	}

	/**
	 * @param pagoOtrosList the pagoOtrosList to set
	 */
	public void setPagoOtrosList(List<Pago> pagoOtrosList) {
		this.pagoOtrosList = pagoOtrosList;
	}

	/**
	 * @return the totalpago
	 */
	public BigDecimal getTotalpago() {
		return totalpago;
	}

	/**
	 * @param totalpago the totalpago to set
	 */
	public void setTotalpago(BigDecimal totalpago) {
		this.totalpago = totalpago;
	}

	/**
	 * @return the abono
	 */
	public BigDecimal getAbono() {
		return abono;
	}

	/**
	 * @param abono the abono to set
	 */
	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	/**
	 * @return the idcliente
	 */
	public String getIdcliente() {
		return idcliente;
	}

	/**
	 * @param idcliente the idcliente to set
	 */
	public void setIdcliente(String idcliente) {
		this.idcliente = idcliente;
	}

	/**
	 * @return the filtroComprobante
	 */
	public String getFiltroComprobante() {
		filtroComprobante = "";
		if(numdocumento != null) {
			filtroComprobante += numdocumento;
		}
		if(comprobante != null) {
			filtroComprobante += comprobante;
		}
		if(fechaemision != null) {
			filtroComprobante += FechaUtil.formatoFecha(fechaemision);
		}
		return filtroComprobante;
	}

	/**
	 * @param filtroComprobante the filtroComprobante to set
	 */
	public void setFiltroComprobante(String filtroComprobante) {
		this.filtroComprobante = filtroComprobante;
	}

	/**
	 * @return the filtroProveedor
	 */
	public String getFiltroProveedor() {
		filtroProveedor = "";
		if(identificacion != null) {
			filtroProveedor += identificacion;
		}
		if(razonsocial != null) {
			filtroProveedor += razonsocial;
		}
		return filtroProveedor;
	}

	/**
	 * @param filtroProveedor the filtroProveedor to set
	 */
	public void setFiltroProveedor(String filtroProveedor) {
		this.filtroProveedor = filtroProveedor;
	}

	/**
	 * @return the totalOtrosPagos
	 */
	public BigDecimal getTotalOtrosPagos() {
		totalOtrosPagos = (getPagoOtrosList() != null && !getPagoOtrosList().isEmpty()) ?
				BigDecimal.valueOf(getPagoOtrosList().stream().mapToDouble(x -> x.getTotal().doubleValue()).sum()) :
				BigDecimal.ZERO;
		return totalOtrosPagos;
	}

	/**
	 * @param totalOtrosPagos the totalOtrosPagos to set
	 */
	public void setTotalOtrosPagos(BigDecimal totalOtrosPagos) {
		this.totalOtrosPagos = totalOtrosPagos;
	}

}
