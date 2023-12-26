/**
 * 
 */
package com.vcw.falecpv.web.ctrl.seg;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ConfiguracionGeneralEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.core.servicio.seg.SegperfilServicio;
import com.vcw.falecpv.core.servicio.seg.SegperfilpredefinidoServicio;
import com.vcw.falecpv.core.servicio.seg.SegperfilusuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PerfilUsuarioCtrl extends BaseCtrl{

	@EJB
	private SegperfilpredefinidoServicio segperfilpredefinidoServicio;
	
	@EJB
	private SegperfilServicio segperfilServicio;
	
	@EJB
	private SegperfilusuarioServicio segperfilusuarioServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5139602474752165395L;
	
	private List<Segperfilpredefinido> segperfilpredefinidoList;
	private List<Segperfil> segperfilList;
	private Segperfilpredefinido segperfilpredefinidoSeleccion;
	private String callForm;
	private Usuario usuarioSelected;
	private boolean disablePerfiles;

	/**
	 * 
	 */
	public PerfilUsuarioCtrl() {
		
	}
	
	private void consultarPerfilPredefinido() {
		segperfilpredefinidoList = null;
		segperfilpredefinidoList = segperfilpredefinidoServicio.getSegperfilpredefinidoDao().getByEstado(EstadoRegistroEnum.ACTIVO,ConfiguracionGeneralEnum.SISTEMA_ID.getId());
	}
	
	private void consultarPerfiles() {
		segperfilList = null;
		segperfilList = segperfilServicio.getSegperfilDao().getByEstado(EstadoRegistroEnum.ACTIVO,ConfiguracionGeneralEnum.SISTEMA_ID.getId());
	}
	
	private List<Segperfil> consultarPerfilesUsuario() throws DaoException {
		return segperfilusuarioServicio.getPerfilByUsuario(usuarioSelected.getIdusuario(),ConfiguracionGeneralEnum.SISTEMA_ID.getId());
	}

	public void cargarFormulario() {
		try {
			disablePerfiles = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.PERFIL_HABILITAR_SELECCION, TipoRetornoParametroGenerico.BOOLEAN, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()); 
			consultarPerfilPredefinido();
			consultarPerfiles();
			segperfilpredefinidoSeleccion = usuarioSelected.getSegperfilpredefinido();
			selecccionarPerfilPredefinido();
			seleccionarPerfiles(consultarPerfilesUsuario());
			segperfilpredefinidoSeleccion = null;
			if(usuarioSelected.getSegperfilpredefinido()!=null) {
				segperfilpredefinidoSeleccion = segperfilpredefinidoList.stream().filter(x->x.getIdsegperfilpredefinido().equals(usuarioSelected.getSegperfilpredefinido().getIdsegperfilpredefinido())).findFirst().orElse(null);
				selecccionarPerfilPredefinido();
			}
			
			AppJsfUtil.showModalRender("dlgPerfilUsuario", "frmPerfilUsuario");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void refrescar() {
		try {
			
			consultarFacade();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultarFacade() throws DaoException {
		consultarPerfilPredefinido();
		consultarPerfiles();
		selecccionarPerfilPredefinido();
		seleccionarPerfiles(consultarPerfilesUsuario());
		segperfilpredefinidoSeleccion = null;
		if(usuarioSelected.getSegperfilpredefinido()!=null) {
			segperfilpredefinidoSeleccion = segperfilpredefinidoList.stream().filter(x->x.getIdsegperfilpredefinido().equals(usuarioSelected.getSegperfilpredefinido().getIdsegperfilpredefinido())).findFirst().orElse(null);
			selecccionarPerfilPredefinido();
		}
	}
	
	public void cambioPerfilPredefinido(Segperfilpredefinido segperfilpredefinido) {
		try {
			
			segperfilpredefinidoSeleccion = segperfilpredefinido;
			
			// 1. desactivar los perfiles predefinidos no seleccionados
			selecccionarPerfilPredefinido();
			
			// 2. consultar todos los perfiles
			consultarPerfiles();
			
			// 3. consultar los perfiles del perfilpredefinido
			List<Segperfil> pList = segperfilpredefinidoServicio.getByPerfilDefinido(segperfilpredefinidoSeleccion.getIdsegperfilpredefinido());
			
			// 4. seleccionar los perfiles a partir 
			seleccionarPerfiles(pList);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void selecccionarPerfilPredefinido() {
		if(segperfilpredefinidoSeleccion!=null) {
			for (Segperfilpredefinido pp : segperfilpredefinidoList) {
				if(pp.getIdsegperfilpredefinido().equals(segperfilpredefinidoSeleccion.getIdsegperfilpredefinido())) {
					pp.setSeleccion(true);
				}
				if(!pp.getIdsegperfilpredefinido().equals(segperfilpredefinidoSeleccion.getIdsegperfilpredefinido())) {
					pp.setSeleccion(false);
				}
			}
		}
	}
	
	private void seleccionarPerfiles(List<Segperfil> pList) {
		
		for (Segperfil pf : segperfilList) {
			if(pList.stream().filter(x->x.getIdsegperfil().equals(pf.getIdsegperfil())).findFirst().orElse(null)!=null) {
				pf.setSeleccion(true);
			}
		}		
	}
	
	@Override
	public void guardar() {
		try {
			
			if(segperfilList.stream().filter(x->x.isSeleccion()).count()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe perfil de acceso seleccionado", 
						Message.ERROR);
				return;
			}
			
			// asigna los perfiles
			segperfilusuarioServicio.asignarPerfiles(ConfiguracionGeneralEnum.SISTEMA_ID.getId(), usuarioSelected, segperfilList, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			// actualzia los datos del usuario
			usuarioSelected.setSegperfilpredefinido(segperfilpredefinidoSeleccion);
			usuarioServicio.actualizar(usuarioSelected);
			
			// refresca todo nuevamente
			consultarFacade();
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Perfil de acceso asignado", 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	/**
	 * @return the segperfilpredefinidoServicio
	 */
	public SegperfilpredefinidoServicio getSegperfilpredefinidoServicio() {
		return segperfilpredefinidoServicio;
	}

	/**
	 * @param segperfilpredefinidoServicio the segperfilpredefinidoServicio to set
	 */
	public void setSegperfilpredefinidoServicio(SegperfilpredefinidoServicio segperfilpredefinidoServicio) {
		this.segperfilpredefinidoServicio = segperfilpredefinidoServicio;
	}

	/**
	 * @return the segperfilpredefinidoList
	 */
	public List<Segperfilpredefinido> getSegperfilpredefinidoList() {
		return segperfilpredefinidoList;
	}

	/**
	 * @param segperfilpredefinidoList the segperfilpredefinidoList to set
	 */
	public void setSegperfilpredefinidoList(List<Segperfilpredefinido> segperfilpredefinidoList) {
		this.segperfilpredefinidoList = segperfilpredefinidoList;
	}

	/**
	 * @return the segperfilpredefinidoSeleccion
	 */
	public Segperfilpredefinido getSegperfilpredefinidoSeleccion() {
		return segperfilpredefinidoSeleccion;
	}

	/**
	 * @param segperfilpredefinidoSeleccion the segperfilpredefinidoSeleccion to set
	 */
	public void setSegperfilpredefinidoSeleccion(Segperfilpredefinido segperfilpredefinidoSeleccion) {
		this.segperfilpredefinidoSeleccion = segperfilpredefinidoSeleccion;
	}

	/**
	 * @return the callForm
	 */
	public String getCallForm() {
		return callForm;
	}

	/**
	 * @param callForm the callForm to set
	 */
	public void setCallForm(String callForm) {
		this.callForm = callForm;
	}

	/**
	 * @return the segperfilList
	 */
	public List<Segperfil> getSegperfilList() {
		return segperfilList;
	}

	/**
	 * @param segperfilList the segperfilList to set
	 */
	public void setSegperfilList(List<Segperfil> segperfilList) {
		this.segperfilList = segperfilList;
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
	 * @return the disablePerfiles
	 */
	public boolean isDisablePerfiles() {
		return disablePerfiles;
	}

	/**
	 * @param disablePerfiles the disablePerfiles to set
	 */
	public void setDisablePerfiles(boolean disablePerfiles) {
		this.disablePerfiles = disablePerfiles;
	}
	
	

}
