/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
@Table(name = "destinatario")
public class Destinatario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 51818742743194997L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "iddestinatario", nullable = false, length = 40)
    private String iddestinatario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificaciondestinatario", nullable = false, length = 20)
    private String identificaciondestinatario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "razonsocialdestinatario", nullable = false, length = 300)
    private String razonsocialdestinatario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dirdestinatario", nullable = false, length = 300)
    private String dirdestinatario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "motivotraslado", nullable = false, length = 300)
    private String motivotraslado;
    @Size(max = 20)
    @Column(name = "docaduanerounico", length = 20)
    private String docaduanerounico;
    @Size(max = 3)
    @Column(name = "codestabdestino", length = 3)
    private String codestabdestino;
    @Size(max = 300)
    @Column(name = "ruta", length = 300)
    private String ruta;
    @Size(max = 2)
    @Column(name = "coddocsustento", length = 2)
    private String coddocsustento;
    @Size(max = 17)
    @Column(name = "numdocsustento", length = 17)
    private String numdocsustento;
    @Size(max = 49)
    @Column(name = "numautdocsustento", length = 49)
    private String numautdocsustento;
    @Column(name = "fechaemisiondocsustento")
    @Temporal(TemporalType.DATE)
    private Date fechaemisiondocsustento;
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera", nullable = false)
    @ManyToOne(optional = false)
    private Cabecera cabecera;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idcliente", referencedColumnName = "idcliente", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = true)
    private Tipocomprobante tipocomprobante;
    
    @Transient
    private List<Detalledestinatario> detalledestinatarioList;
    
    @Transient
    private BigDecimal total = BigDecimal.ZERO;
    
    @Transient
    private String idVenta;
    
    @Transient
    private boolean consultarDetalleFactura = false;
    
    @Transient
    private boolean seleccion = false;
    
    @Transient
    private String filterDestinatario;
    
    @Transient
    private String filterTransportista;

	/**
	 * 
	 */
	public Destinatario() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iddestinatario != null ? iddestinatario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Destinatario)) {
            return false;
        }
        Destinatario other = (Destinatario) object;
        if ((this.iddestinatario == null && other.iddestinatario != null) || (this.iddestinatario != null && !this.iddestinatario.equals(other.iddestinatario))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the iddestinatario
	 */
	public String getIddestinatario() {
		return iddestinatario;
	}

	/**
	 * @param iddestinatario the iddestinatario to set
	 */
	public void setIddestinatario(String iddestinatario) {
		this.iddestinatario = iddestinatario;
	}

	/**
	 * @return the identificaciondestinatario
	 */
	public String getIdentificaciondestinatario() {
		return identificaciondestinatario;
	}

	/**
	 * @param identificaciondestinatario the identificaciondestinatario to set
	 */
	public void setIdentificaciondestinatario(String identificaciondestinatario) {
		this.identificaciondestinatario = identificaciondestinatario;
	}

	/**
	 * @return the razonsocialdestinatario
	 */
	public String getRazonsocialdestinatario() {
		return razonsocialdestinatario;
	}

	/**
	 * @param razonsocialdestinatario the razonsocialdestinatario to set
	 */
	public void setRazonsocialdestinatario(String razonsocialdestinatario) {
		this.razonsocialdestinatario = razonsocialdestinatario;
	}

	/**
	 * @return the dirdestinatario
	 */
	public String getDirdestinatario() {
		return dirdestinatario;
	}

	/**
	 * @param dirdestinatario the dirdestinatario to set
	 */
	public void setDirdestinatario(String dirdestinatario) {
		this.dirdestinatario = dirdestinatario;
	}

	/**
	 * @return the motivotraslado
	 */
	public String getMotivotraslado() {
		return motivotraslado;
	}

	/**
	 * @param motivotraslado the motivotraslado to set
	 */
	public void setMotivotraslado(String motivotraslado) {
		this.motivotraslado = motivotraslado;
	}

	/**
	 * @return the docaduanerounico
	 */
	public String getDocaduanerounico() {
		return docaduanerounico;
	}

	/**
	 * @param docaduanerounico the docaduanerounico to set
	 */
	public void setDocaduanerounico(String docaduanerounico) {
		this.docaduanerounico = docaduanerounico;
	}

	/**
	 * @return the codestabdestino
	 */
	public String getCodestabdestino() {
		return codestabdestino;
	}

	/**
	 * @param codestabdestino the codestabdestino to set
	 */
	public void setCodestabdestino(String codestabdestino) {
		this.codestabdestino = codestabdestino;
	}

	/**
	 * @return the ruta
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 * @return the coddocsustento
	 */
	public String getCoddocsustento() {
		return coddocsustento;
	}

	/**
	 * @param coddocsustento the coddocsustento to set
	 */
	public void setCoddocsustento(String coddocsustento) {
		this.coddocsustento = coddocsustento;
	}

	/**
	 * @return the numdocsustento
	 */
	public String getNumdocsustento() {
		return numdocsustento;
	}

	/**
	 * @param numdocsustento the numdocsustento to set
	 */
	public void setNumdocsustento(String numdocsustento) {
		this.numdocsustento = numdocsustento;
	}

	/**
	 * @return the numautdocsustento
	 */
	public String getNumautdocsustento() {
		return numautdocsustento;
	}

	/**
	 * @param numautdocsustento the numautdocsustento to set
	 */
	public void setNumautdocsustento(String numautdocsustento) {
		this.numautdocsustento = numautdocsustento;
	}

	/**
	 * @return the fechaemisiondocsustento
	 */
	public Date getFechaemisiondocsustento() {
		return fechaemisiondocsustento;
	}

	/**
	 * @param fechaemisiondocsustento the fechaemisiondocsustento to set
	 */
	public void setFechaemisiondocsustento(Date fechaemisiondocsustento) {
		this.fechaemisiondocsustento = fechaemisiondocsustento;
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
	 * @return the detalledestinatarioList
	 */
	public List<Detalledestinatario> getDetalledestinatarioList() {
		return detalledestinatarioList;
	}

	/**
	 * @param detalledestinatarioList the detalledestinatarioList to set
	 */
	public void setDetalledestinatarioList(List<Detalledestinatario> detalledestinatarioList) {
		this.detalledestinatarioList = detalledestinatarioList;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the idVenta
	 */
	public String getIdVenta() {
		return idVenta;
	}

	/**
	 * @param idVenta the idVenta to set
	 */
	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}

	/**
	 * @return the consultarDetalleFactura
	 */
	public boolean isConsultarDetalleFactura() {
		return consultarDetalleFactura;
	}

	/**
	 * @param consultarDetalleFactura the consultarDetalleFactura to set
	 */
	public void setConsultarDetalleFactura(boolean consultarDetalleFactura) {
		this.consultarDetalleFactura = consultarDetalleFactura;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the filterDestinatario
	 */
	public String getFilterDestinatario() {
		filterDestinatario = "";
		if(getRazonsocialdestinatario()!=null) {
			filterDestinatario += getRazonsocialdestinatario() + " ";
		}
		if(getRazonsocialdestinatario()!=null) {
			filterDestinatario += getRazonsocialdestinatario() + " ";
		}
		if(getIdentificaciondestinatario()!=null) {
			filterDestinatario += getIdentificaciondestinatario() + " ";
		}
		if(getRuta()!=null) {
			filterDestinatario += getRuta() + " ";
		}	
		
		return filterDestinatario;
	}

	/**
	 * @param filterDestinatario the filterDestinatario to set
	 */
	public void setFilterDestinatario(String filterDestinatario) {
		this.filterDestinatario = filterDestinatario;
	}

	/**
	 * @return the filterTransportista
	 */
	public String getFilterTransportista() {
		filterTransportista = "";
		if(getCabecera()!=null && getCabecera().getTransportista()!=null && getCabecera().getTransportista().getRazonsocial()!=null) {
			filterTransportista += getCabecera().getTransportista().getRazonsocial() + " ";
		}
		if(getCabecera()!=null && getCabecera().getTransportista()!=null && getCabecera().getTransportista().getIdentificacion()!=null) {
			filterTransportista += getCabecera().getTransportista().getIdentificacion() + " ";
		}
		if(getCabecera()!=null && getCabecera().getFechainiciotransporte()!=null) {
			filterTransportista += (new SimpleDateFormat("dd/MM/yyyy")).format(getCabecera().getFechainiciotransporte()) + " ";
		}
		if(getCabecera()!=null && getCabecera().getFechafintransporte()!=null) {
			filterTransportista += (new SimpleDateFormat("dd/MM/yyyy")).format(getCabecera().getFechafintransporte()) + " ";
		}
		return filterTransportista;
	}

	/**
	 * @param filterTransportista the filterTransportista to set
	 */
	public void setFilterTransportista(String filterTransportista) {
		this.filterTransportista = filterTransportista;
	}

}
