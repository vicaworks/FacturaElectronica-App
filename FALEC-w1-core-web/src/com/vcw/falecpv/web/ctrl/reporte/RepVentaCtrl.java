/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TipoPagoEnum;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RepVentaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5541322574433690245L;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private FabricanteServicio fabricanteServicio;
	
	@EJB
	private CategoriaServicio categoriaServicio;

	private List<Usuario> usuarioList;
	private Usuario usuarioSelected;
	private List<Fabricante> fabricanteList;
	private Fabricante fabricanteSelected;
	private List<Categoria> categoriaList;
	private Categoria categoriaSelected;
	private List<Tipopago> tipopagoList;
	private Tipopago tipopagoSelected;
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQueryList;
	private TotalesDto totalesDto = new TotalesDto();
	
	/**
	 * 
	 */
	public RepVentaCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			consultarUsuario();
			consultarTipoPago();
			consultarFabricante();
			consultarCategoria();
			hasta = new Date();
			desde = FechaUtil.agregarMeses(hasta, -1);
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarUsuario()throws DaoException{
		usuarioList = null;
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	private void consultarTipoPago()throws DaoException{
		tipopagoList = null;
		tipopagoList = tipopagoServicio.getTipopagoDao().getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), TipoPagoEnum.FACTURACION);
	}
	
	private void consultarFabricante()throws DaoException{
		fabricanteList = null;
		fabricanteList = fabricanteServicio.getFabricanteDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	private void consultarCategoria()throws DaoException{
		categoriaList = null;
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	private void consultar()throws DaoException {
		ventasQueryList = null;
		ventasQueryList = consultaVentaServicio.getVentasDetalleCriterio(usuarioSelected, tipopagoSelected,
				fabricanteSelected, categoriaSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento(),
				criterioBusqueda, desde, hasta);
	}
	
	@Override
	public void buscar() {
		try {
			consultar();
			consultarUsuario();
			consultarTipoPago();
			consultarFabricante();
			consultarCategoria();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() {
		totalesDto = new TotalesDto();
		
		if(ventasQueryList!=null) {
			totalesDto.setCantidad(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getCantidad().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setSubtotal(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getPreciototalsinimpuesto().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setDescuento(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getDescuento().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIva(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getValoriva().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setIce(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getValorice().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
			totalesDto.setTotal(BigDecimal.valueOf(ventasQueryList.stream().mapToDouble(x->x.getPreciototal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
		}
	}
	
	public StreamedContent getFile() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the desde
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @param desde the desde to set
	 */
	public void setDesde(Date desde) {
		this.desde = desde;
	}

	/**
	 * @return the hasta
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * @param hasta the hasta to set
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	/**
	 * @return the ventasQueryList
	 */
	public List<VentasQuery> getVentasQueryList() {
		return ventasQueryList;
	}

	/**
	 * @param ventasQueryList the ventasQueryList to set
	 */
	public void setVentasQueryList(List<VentasQuery> ventasQueryList) {
		this.ventasQueryList = ventasQueryList;
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
	 * @return the usuarioSelected
	 */
	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}

	/**
	 * @param usuarioSelected the usuarioSelected to set
	 */
	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}

	/**
	 * @return the fabricanteList
	 */
	public List<Fabricante> getFabricanteList() {
		return fabricanteList;
	}

	/**
	 * @param fabricanteList the fabricanteList to set
	 */
	public void setFabricanteList(List<Fabricante> fabricanteList) {
		this.fabricanteList = fabricanteList;
	}

	/**
	 * @return the fabricanteSelected
	 */
	public Fabricante getFabricanteSelected() {
		return fabricanteSelected;
	}

	/**
	 * @param fabricanteSelected the fabricanteSelected to set
	 */
	public void setFabricanteSelected(Fabricante fabricanteSelected) {
		this.fabricanteSelected = fabricanteSelected;
	}

	/**
	 * @return the categoriaList
	 */
	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}

	/**
	 * @param categoriaList the categoriaList to set
	 */
	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}

	/**
	 * @return the categoriaSelected
	 */
	public Categoria getCategoriaSelected() {
		return categoriaSelected;
	}

	/**
	 * @param categoriaSelected the categoriaSelected to set
	 */
	public void setCategoriaSelected(Categoria categoriaSelected) {
		this.categoriaSelected = categoriaSelected;
	}

	/**
	 * @return the tipopagoList
	 */
	public List<Tipopago> getTipopagoList() {
		return tipopagoList;
	}

	/**
	 * @param tipopagoList the tipopagoList to set
	 */
	public void setTipopagoList(List<Tipopago> tipopagoList) {
		this.tipopagoList = tipopagoList;
	}

	/**
	 * @return the tipopagoSelected
	 */
	public Tipopago getTipopagoSelected() {
		return tipopagoSelected;
	}

	/**
	 * @param tipopagoSelected the tipopagoSelected to set
	 */
	public void setTipopagoSelected(Tipopago tipopagoSelected) {
		this.tipopagoSelected = tipopagoSelected;
	}

	/**
	 * @return the totalesDto
	 */
	public TotalesDto getTotalesDto() {
		return totalesDto;
	}

	/**
	 * @param totalesDto the totalesDto to set
	 */
	public void setTotalesDto(TotalesDto totalesDto) {
		this.totalesDto = totalesDto;
	}

}
