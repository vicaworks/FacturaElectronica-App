/**
 * 
 */
package com.vcw.falecpv.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.ws.rs.core.Link;

import org.omnifaces.util.Ajax;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.FileUtil;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;

/**
 * @author cvillarreal
 * 
 *         Clase utilitaria de JSF hereda {@link FacesUtil}
 *
 */
public class AppJsfUtil extends FacesUtil {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7631549445102421594L;

	/**
	 * 
	 */
	public AppJsfUtil() {
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Descarga un archivo para primefaces
	 * 
	 * @param is
	 *            {@link Link InputStream} del archivo
	 * @param nombreFile
	 *            nombre del archivo con la extenci&oacute;n
	 * 
	 * @return {@link DefaultStreamedContent}
	 */
	public static StreamedContent downloadFile(InputStream is, String nombreFile) throws Exception {

		return new DefaultStreamedContent(is, FileUtil.getMimeType(nombreFile), nombreFile);

	}

	/**
	 * @author cvillarreal
	 * 
	 *         Descarga files para primefaces
	 * 
	 * @param file
	 *            archivo de descarga
	 * @param nombreFile
	 *            nombre del archivo
	 * @return {@link DefaultStreamedContent}
	 * @throws Exception
	 */
	public static StreamedContent downloadFile(File file, String nombreFile) throws Exception {

		InputStream is = new FileInputStream(file);
		return new DefaultStreamedContent(is, FileUtil.getMimeType(nombreFile), nombreFile);

	}

	/**
	 * @param idDataTable
	 */
	public static void limpiarFiltrosDataTable(String idDataTable) {
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(idDataTable);
		if (dataTable!=null) {
			dataTable.resetValue();
			dataTable.reset();
			dataTable.clearInitialState();
			dataTable.setFilteredValue(null);
            dataTable.setFilters(null);
            dataTable.setSortBy(null);
            PrimeFaces.current().ajax().update(idDataTable);
            
            // tomar en consideracion para otros cambios
//            dataTable.setMultiViewState(false);
//            RequestContext requestContext = RequestContext.getCurrentInstance();
//            requestContext.update(idDataTable);
//            dataTable.setMultiViewState(true);
		}
	}
	
	/**
	 * @author phidalgo
	 * 
	 *         Ejecuta directamente c&oacute;digo JavaScript
	 * 
	 * @param codigoJavaScript
	 */
	public static void executeJavaScript(String codigoJavaScript) {
		PrimeFaces.current().executeScript(codigoJavaScript);
//		RequestContext rct = RequestContext.getCurrentInstance();
//		rct.execute(codigoJavaScript);
	}

	/**
	 * @author phidalgo
	 * 
	 *         Actualiza el componente seleccionado
	 * 
	 * @param componente
	 */
	public static void updateComponente(String componente) {
		PrimeFaces.current().ajax().update(componente);
//		RequestContext rct = RequestContext.getCurrentInstance();
//		rct.update(componente);
	}

	/**
	 * @author phidalgo
	 * 
	 *         Actualiza varios componentes
	 * 
	 * @param listaComponentes
	 *            listado de componentes
	 */
	public static void updateListaComponente(List<String> listaComponentes) {
		PrimeFaces.current().ajax().update(listaComponentes);
//		RequestContext rct = RequestContext.getCurrentInstance();
//		rct.update(listaComponentes);
	}

	/**
	 * @author phidalgo
	 * 
	 *         Muestra el modal
	 * 
	 * 
	 * @param modal
	 *            widgetVar del modal
	 */
	public static void showModal(String modal) {
		executeJavaScript("PF('" + modal + "').show()");
	}

	/**
	 * @author phidalgo
	 * 
	 * 
	 *         oculta el modal
	 * 
	 * @param modal
	 *            widgetVar del modal
	 */
	public static void hideModal(String modal) {
		executeJavaScript("PF('" + modal + "').hide()");
	}

	/**
	 * @author phidalgo
	 * 
	 *         Actualiza un componente seg&uacute;n el id del componente
	 * 
	 * @param idComponent
	 *            ID del componente
	 */
	public static void ajaxUpdate(String idComponent) {
		Ajax.update(idComponent);
	}

	/**
	 * @author phidalgo
	 * 
	 *         Muestra un modal y actualiza un componete
	 * 
	 * @param modal
	 *            widgetVar del componente
	 * @param idRender
	 *            ID del componente
	 */
	public static void showModalRender(String modal, String idRender) {
		showModal(modal);
		ajaxUpdate(idRender);
	}
	
	/**
	 * @author cvillarreal
	 * 
	 * Remueve los mensajes del contecto
	 * 
	 */
	public static void eliminarMessageFacesContext(){
		
		if (FacesContext.getCurrentInstance().getMessages()!=null){
			try {
				FacesContext.getCurrentInstance().getMessages().remove();
			} catch (Exception e) {
				
			}
		}
		
	}
	
	
	/**
	 * Coloca los archivos de un directorio en un componente TreeNode
	 * @author wayala
	 * @param file
	 * @param node
	 * @param expanded si se desea o no el TreeNode expandido
	 * @return
	 */
	public static TreeNode recorrerDirectorio(File file, TreeNode node, boolean expanded) {
		if (file.isDirectory()) {
			TreeNode treeNode = new DefaultTreeNode(file, node);
			Arrays.sort(file.listFiles());
			for (File hijo : file.listFiles()) {
				recorrerDirectorio(hijo, treeNode, expanded);
			}
		} else {
			node.getChildren().add(new DefaultTreeNode(file));
			node.setExpanded(expanded);
		}

		return node;
	}

	
	/**
	 * @author cvillarreal
	 * 
	 * @param file
	 * @param node
	 * @param expanded
	 * @return
	 */
	public static TreeNode recorrerDirectorioOrder(File file, TreeNode node, boolean expanded) {
		
		if (file.isDirectory()) {
			TreeNode treeNode = new DefaultTreeNode(file, node);
			Arrays.sort(file.listFiles());
			for (File hijo : file.listFiles()) {
				recorrerDirectorio(hijo, treeNode, expanded);
			}
		} else {
			node.getChildren().add(new DefaultTreeNode(file));
			node.setExpanded(expanded);
		}
		
		Collections.sort(node.getChildren(),new TreeNodeComparator());
		return node;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public static String getLoginUser() {
		return FacesUtil.getRemoteUser();
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public Establecimiento getEstablecimiento() {
		if(FacesUtil.getHttpSession(false).getAttribute("establecimiento")!=null) {
			return (Establecimiento) FacesUtil.getHttpSession(false).getAttribute("establecimiento");
		}
		return null;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public Usuario getUsuario() {
		if(FacesUtil.getHttpSession(false).getAttribute("usuario")!=null) {
			return (Usuario) FacesUtil.getHttpSession(false).getAttribute("usuario");
		}
		return null;
	}

}
