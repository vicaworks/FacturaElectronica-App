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

import org.hibernate.annotations.Type;

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
    @Column(name = "idestablecimiento", nullable = false, length = 40)
    private String idestablecimiento;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "codigoestablecimiento", nullable = false, length = 3)
    private String codigoestablecimiento;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "direccionestablecimiento", nullable = false, length = 300)
    private String direccionestablecimiento;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombrecomercial", nullable = false, length = 300)
    private String nombrecomercial;
    
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "logo")
    private byte[] logo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "puntoemision", nullable = false, length = 3)
    private String puntoemision;
    
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
    @Column(name = "secuencialfactura", nullable = false)
    private int secuencialfactura;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialrecibo", nullable = false)
    private int secuencialrecibo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialliquidacioncompra", nullable = false)
    private int secuencialliquidacioncompra;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialNotaCredito", nullable = false)
    private int secuencialNotaCredito;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialnotadebito", nullable = false)
    private int secuencialnotadebito;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialguiaremision", nullable = false)
    private int secuencialguiaremision;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialretencion", nullable = false)
    private int secuencialretencion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencialcotizacion", nullable = false)
    private int secuencialcotizacion;
    
    @Column(name = "nombreimagen",length = 200)
    private String nombreimagen;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ambiente", nullable = false, length = 1)
    private String ambiente;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idempresa",referencedColumnName = "idempresa")
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
        hash += (idestablecimiento != null ? idestablecimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Establecimiento)) {
            return false;
        }
        Establecimiento other = (Establecimiento) object;
        if ((this.idestablecimiento == null && other.idestablecimiento != null) || (this.idestablecimiento != null && !this.idestablecimiento.equals(other.idestablecimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("[%s, %s]", idestablecimiento, nombrecomercial);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
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
	 * @return the codigoestablecimiento
	 */
	public String getCodigoestablecimiento() {
		return codigoestablecimiento;
	}

	/**
	 * @param codigoestablecimiento the codigoestablecimiento to set
	 */
	public void setCodigoestablecimiento(String codigoestablecimiento) {
		this.codigoestablecimiento = codigoestablecimiento;
	}

	/**
	 * @return the direccionestablecimiento
	 */
	public String getDireccionestablecimiento() {
		return direccionestablecimiento;
	}

	/**
	 * @param direccionestablecimiento the direccionestablecimiento to set
	 */
	public void setDireccionestablecimiento(String direccionestablecimiento) {
		this.direccionestablecimiento = direccionestablecimiento;
	}

	/**
	 * @return the nombrecomercial
	 */
	public String getNombrecomercial() {
		return nombrecomercial;
	}

	/**
	 * @param nombrecomercial the nombrecomercial to set
	 */
	public void setNombrecomercial(String nombrecomercial) {
		this.nombrecomercial = nombrecomercial;
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
	 * @return the puntoemision
	 */
	public String getPuntoemision() {
		return puntoemision;
	}

	/**
	 * @param puntoemision the puntoemision to set
	 */
	public void setPuntoemision(String puntoemision) {
		this.puntoemision = puntoemision;
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
	 * @return the secuencialfactura
	 */
	public int getSecuencialfactura() {
		return secuencialfactura;
	}

	/**
	 * @param secuencialfactura the secuencialfactura to set
	 */
	public void setSecuencialfactura(int secuencialfactura) {
		this.secuencialfactura = secuencialfactura;
	}

	/**
	 * @return the secuencialliquidacioncompra
	 */
	public int getSecuencialliquidacioncompra() {
		return secuencialliquidacioncompra;
	}

	/**
	 * @param secuencialliquidacioncompra the secuencialliquidacioncompra to set
	 */
	public void setSecuencialliquidacioncompra(int secuencialliquidacioncompra) {
		this.secuencialliquidacioncompra = secuencialliquidacioncompra;
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
	 * @return the secuencialnotadebito
	 */
	public int getSecuencialnotadebito() {
		return secuencialnotadebito;
	}

	/**
	 * @param secuencialnotadebito the secuencialnotadebito to set
	 */
	public void setSecuencialnotadebito(int secuencialnotadebito) {
		this.secuencialnotadebito = secuencialnotadebito;
	}

	/**
	 * @return the secuencialguiaremision
	 */
	public int getSecuencialguiaremision() {
		return secuencialguiaremision;
	}

	/**
	 * @param secuencialguiaremision the secuencialguiaremision to set
	 */
	public void setSecuencialguiaremision(int secuencialguiaremision) {
		this.secuencialguiaremision = secuencialguiaremision;
	}

	/**
	 * @return the secuencialretencion
	 */
	public int getSecuencialretencion() {
		return secuencialretencion;
	}

	/**
	 * @param secuencialretencion the secuencialretencion to set
	 */
	public void setSecuencialretencion(int secuencialretencion) {
		this.secuencialretencion = secuencialretencion;
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

	/**
	 * @return
	 */
	public String getNombreimagen() {
		return nombreimagen;
	}

	/**
	 * @param nombreimagen
	 */
	public void setNombreimagen(String nombreimagen) {
		this.nombreimagen = nombreimagen;
	}

	/**
	 * @return the secuencialrecibo
	 */
	public int getSecuencialrecibo() {
		return secuencialrecibo;
	}

	/**
	 * @param secuencialrecibo the secuencialrecibo to set
	 */
	public void setSecuencialrecibo(int secuencialrecibo) {
		this.secuencialrecibo = secuencialrecibo;
	}

	/**
	 * @return the ambiente
	 */
	public String getAmbiente() {
		return ambiente;
	}

	/**
	 * @param ambiente the ambiente to set
	 */
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * @return the secuencialcotizacion
	 */
	public int getSecuencialcotizacion() {
		return secuencialcotizacion;
	}

	/**
	 * @param secuencialcotizacion the secuencialcotizacion to set
	 */
	public void setSecuencialcotizacion(int secuencialcotizacion) {
		this.secuencialcotizacion = secuencialcotizacion;
	}		
	
	

}
