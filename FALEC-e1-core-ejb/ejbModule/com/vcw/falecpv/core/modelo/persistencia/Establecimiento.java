/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "establecimiento")
public class Establecimiento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9005830519416748954L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idEstablecimiento", nullable = false, length = 40)
    private String idEstablecimiento;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "codigoEstablecimiento", nullable = false, length = 3)
    private String codigoEstablecimiento;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "direccionEstablecimiento", nullable = false, length = 300)
    private String direccionEstablecimiento;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreComercial", nullable = false, length = 300)
    private String nombreComercial;
    
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "puntoEmision", nullable = false, length = 3)
    private String puntoEmision;
    
    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Size(max = 50)
    @Column(name = "correo", length = 50)
    private String correo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "matriz", nullable = false, length = 10)
    private String matriz;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialFactura", nullable = false)
    private int secuencialFactura;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialLiquidacionCompra", nullable = false)
    private int secuencialLiquidacionCompra;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialNotaCredito", nullable = false)
    private int secuencialNotaCredito;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialNotaDebito", nullable = false)
    private int secuencialNotaDebito;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialGuiaRemision", nullable = false)
    private int secuencialGuiaRemision;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialRetencion", nullable = false)
    private int secuencialRetencion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idUsuario", nullable = false, length = 40)
    private String idUsuario;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idEmpresa",referencedColumnName = "idEmpresa",insertable = false,updatable = false)
    private Empresa empresa;
    
    @OneToMany(mappedBy = "establecimiento",fetch = FetchType.LAZY)
    private List<Usuario> usuarioList;

	/**
	 * 
	 */
	public Establecimiento() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstablecimiento != null ? idEstablecimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Establecimiento)) {
            return false;
        }
        Establecimiento other = (Establecimiento) object;
        if ((this.idEstablecimiento == null && other.idEstablecimiento != null) || (this.idEstablecimiento != null && !this.idEstablecimiento.equals(other.idEstablecimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idEstablecimiento
	 */
	public String getIdEstablecimiento() {
		return idEstablecimiento;
	}

	/**
	 * @param idEstablecimiento the idEstablecimiento to set
	 */
	public void setIdEstablecimiento(String idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	/**
	 * @return the codigoEstablecimiento
	 */
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}

	/**
	 * @param codigoEstablecimiento the codigoEstablecimiento to set
	 */
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}

	/**
	 * @return the direccionEstablecimiento
	 */
	public String getDireccionEstablecimiento() {
		return direccionEstablecimiento;
	}

	/**
	 * @param direccionEstablecimiento the direccionEstablecimiento to set
	 */
	public void setDireccionEstablecimiento(String direccionEstablecimiento) {
		this.direccionEstablecimiento = direccionEstablecimiento;
	}

	/**
	 * @return the nombreComercial
	 */
	public String getNombreComercial() {
		return nombreComercial;
	}

	/**
	 * @param nombreComercial the nombreComercial to set
	 */
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	/**
	 * @return the logo
	 */
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	/**
	 * @return the puntoEmision
	 */
	public String getPuntoEmision() {
		return puntoEmision;
	}

	/**
	 * @param puntoEmision the puntoEmision to set
	 */
	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return the matriz
	 */
	public String getMatriz() {
		return matriz;
	}

	/**
	 * @param matriz the matriz to set
	 */
	public void setMatriz(String matriz) {
		this.matriz = matriz;
	}

	/**
	 * @return the secuencialFactura
	 */
	public int getSecuencialFactura() {
		return secuencialFactura;
	}

	/**
	 * @param secuencialFactura the secuencialFactura to set
	 */
	public void setSecuencialFactura(int secuencialFactura) {
		this.secuencialFactura = secuencialFactura;
	}

	/**
	 * @return the secuencialLiquidacionCompra
	 */
	public int getSecuencialLiquidacionCompra() {
		return secuencialLiquidacionCompra;
	}

	/**
	 * @param secuencialLiquidacionCompra the secuencialLiquidacionCompra to set
	 */
	public void setSecuencialLiquidacionCompra(int secuencialLiquidacionCompra) {
		this.secuencialLiquidacionCompra = secuencialLiquidacionCompra;
	}

	/**
	 * @return the secuencialNotaCredito
	 */
	public int getSecuencialNotaCredito() {
		return secuencialNotaCredito;
	}

	/**
	 * @param secuencialNotaCredito the secuencialNotaCredito to set
	 */
	public void setSecuencialNotaCredito(int secuencialNotaCredito) {
		this.secuencialNotaCredito = secuencialNotaCredito;
	}

	/**
	 * @return the secuencialNotaDebito
	 */
	public int getSecuencialNotaDebito() {
		return secuencialNotaDebito;
	}

	/**
	 * @param secuencialNotaDebito the secuencialNotaDebito to set
	 */
	public void setSecuencialNotaDebito(int secuencialNotaDebito) {
		this.secuencialNotaDebito = secuencialNotaDebito;
	}

	/**
	 * @return the secuencialGuiaRemision
	 */
	public int getSecuencialGuiaRemision() {
		return secuencialGuiaRemision;
	}

	/**
	 * @param secuencialGuiaRemision the secuencialGuiaRemision to set
	 */
	public void setSecuencialGuiaRemision(int secuencialGuiaRemision) {
		this.secuencialGuiaRemision = secuencialGuiaRemision;
	}

	/**
	 * @return the secuencialRetencion
	 */
	public int getSecuencialRetencion() {
		return secuencialRetencion;
	}

	/**
	 * @param secuencialRetencion the secuencialRetencion to set
	 */
	public void setSecuencialRetencion(int secuencialRetencion) {
		this.secuencialRetencion = secuencialRetencion;
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
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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
	 * @return the usuarioList
	 */
	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	/**
	 * @param usuarioList the usuarioList to set
	 */
	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

}
