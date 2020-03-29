/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipoProductoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ProductoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private ProductoServicio productoServicio;
	@EJB
	private CategoriaServicio categoriaServicio;
	@EJB
	private FabricanteServicio fabricanteServicio;
	@EJB
	private IceServicio iceServicio;
	@EJB
	private IvaServicio ivaServicio;
	@EJB
	private TipoProductoServicio tipoProductoServicio;
	
	
	
	private List<Producto> productoList;
	private Producto productoSelected;
	private Producto productoFormSelected;
	private String criterioBusqueda;
	private List<Fabricante> fabricanteList;
	private List<TipoProducto> tipoProductoList;
	private List<Categoria> categoriaList; 
	private List<Ice> iceList;
	private List<Iva> ivaList;
	private List<Producto> productoFormList;
	private File imageProdutoFile;
	private String fileName;
	

	/**
	 * 
	 */
	public ProductoCtrl() {
		
	}

	@Override
	public void refrescar() {
		try {
			productoSelected = null;
			consultarProducto();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(productoSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// existe refrencias
			if(productoServicio.tieneReferencias(productoSelected.getIdproducto(), productoSelected.getEstablecimiento().getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EXISTE REFERENCIAS.");
				return;
			}
			
			productoServicio.eliminar(productoSelected);
			
			productoSelected = null;
			consultarProducto();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			imageProdutoFile = null;
			fileName = null;
			
			productoFormSelected = new Producto();
			productoSelected = new Producto();
			productoSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
			productoSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			productoSelected.setStock(Integer.valueOf(0));
			productoSelected.setUnidadesporpaquete(Integer.valueOf(0));
			productoSelected.setUnidadesporcajaofrasco(Integer.valueOf(0));
			productoSelected.setPreciouno(BigDecimal.ZERO);
			productoSelected.setPreciodos(BigDecimal.ZERO);
			productoSelected.setPreciotres(BigDecimal.ZERO);
			productoSelected.setPreciounitario(BigDecimal.ZERO);
			productoSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			productoSelected.setConversionmedida(BigDecimal.ZERO);
			productoSelected.setPorcentajedescuento(BigDecimal.ZERO);
			consultarFabrica();
			consultarCategoria();
			consultarIce();
			consultarIva();
			consultarProductoForm();
			consultarTipoProducto();
			AppJsfUtil.showModalRender("dlgProducto", "frmProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarTipoProducto()throws DaoException{
		tipoProductoList = null;
		tipoProductoList = tipoProductoServicio.getTipoProductoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}
	
	public void consultarCategoria()throws DaoException{
		categoriaList = null;
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	public void consultarFabrica()throws DaoException{
		fabricanteList = null;
		fabricanteList = fabricanteServicio.getFabricanteDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	public void consultarIva()throws DaoException{
		ivaList = null;
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarIce()throws DaoException{
		iceList = null;
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarProductoForm()throws DaoException{
		productoFormList = null;
		productoFormList = productoServicio.getProductoDao().getByQuery(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
	}
	
	public void consultarProducto()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:productoDT");
		productoList = null;
		productoList = productoServicio.getProductoDao().consultarAllImageEager(AppJsfUtil.getEstablecimiento().getIdestablecimiento(),criterioBusqueda);
	}
	
	public void handleUpload(FileUploadEvent event) throws IOException {
		
		UploadedFile uploadedFile = event.getFile();
		fileName = uploadedFile.getFileName();
		String path = FilenameUtils.getBaseName(fileName) + " " + UUID.randomUUID().toString() + " "
				+ FilenameUtils.getExtension(fileName);
		System.out.println(path);
		File parent = new File("uploads");
		parent.mkdirs();
		imageProdutoFile = new File(parent, fileName);
		FileUtils.writeByteArrayToFile(imageProdutoFile, uploadedFile.getContents());
		System.out.println(imageProdutoFile.getAbsolutePath());
	}
	
	public String getNombreImagen() {
		if(imageProdutoFile!=null) {
			return fileName;
		}
		return "";
	}
	
	public StreamedContent getImageProducto() {
		try {
			
			if(imageProdutoFile==null) {
				return null;
			}
			return  new DefaultStreamedContent(new FileInputStream(imageProdutoFile));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public StreamedContent getImageToStream(byte[] img) {
		try {
			
			if(img==null) {
				return null;
			}
			return  new DefaultStreamedContent(new ByteArrayInputStream(img));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void guardar() {
		
		try {
			// validaciones 
			
			//1. nombre comercial
			if(productoServicio.getProductoDao().existeNombreGenerico(productoSelected.getNombregenerico(), productoSelected.getIdproducto(), AppJsfUtil.getEstablecimiento().getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","EL NOMBRE COMERCIAL YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmProducto:intNombreProductoComercial","YA EXISTE.");
				return;
			}
			//2. nombre
			if(productoServicio.getProductoDao().existeNombre(productoSelected.getNombre(), productoSelected.getIdproducto(), AppJsfUtil.getEstablecimiento().getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","EL NOMBRE YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmProducto:intNombreProducto","YA EXISTE.");
				return;
			}
			
			//3. codigo no se repita
			if(productoSelected.getCodigoprincipal()!=null && productoSelected.getCodigoprincipal().trim().length()>0) {
				if(productoServicio.getProductoDao().existeCodigoProducto(productoSelected.getCodigoprincipal(), productoSelected.getIdproducto(), AppJsfUtil.getEstablecimiento().getIdestablecimiento())) {
					AppJsfUtil.addErrorMessage("frmProducto", "ERROR",msg.getString("label.codigo") +   " DEL PRODUCTO YA EXISTE.");
					AppJsfUtil.addErrorMessage("frmProducto:intCodigoProducto","YA EXISTE.");
					return;
				}
			}
			
			//4. si existe medida de conversion
			if(productoSelected.getMedida()!=null && productoSelected.getConversionmedida().floatValue()<0.001f) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","AL EXISTIR MEDIDA DE " + msg.getString("label.conversion") +   " DEBE EXISTIR UN VALOR > 0.");
				AppJsfUtil.addErrorMessage("frmProducto:intUniConversionProducto","MAYOR A 0.");
				return;
			}
			
			productoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			productoSelected.setUpdated(new Date());
			productoSelected.setImagen(null);
			if(imageProdutoFile!=null) {
				productoSelected.setImagen(FileUtils.readFileToByteArray(imageProdutoFile));
			}
			productoSelected.setNombreimagen(getNombreImagen());
			productoServicio.guardar(productoSelected);
			
			// lista del combo popup
			consultarProductoForm();
			// lista principal
			consultarProducto();
			
			AppJsfUtil.addInfoMessage("frmProducto","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void editarProducto(String id) {
		try {
			
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgProducto", "frmProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editarProductoForm() {
		try {
			
			if(productoFormSelected==null) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(productoFormSelected.getIdproducto());
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void editarFacade(String id) throws DaoException, IOException {
		
		productoSelected = productoServicio.getProductoDao().cargar(id);
		// imagen
		imageProdutoFile = null;
		if(productoSelected.getImagen()!=null) {
			imageProdutoFile = new File(FileUtils.getTempDirectory(),productoSelected.getNombreimagen()); 
			FileUtils.writeByteArrayToFile(imageProdutoFile, productoSelected.getImagen());;
		}
		fileName = productoSelected.getNombreimagen();
		consultarFabrica();
		consultarCategoria();
		consultarIce();
		consultarIva();
		consultarProductoForm();
		consultarTipoProducto();
	}
	
	public void limpiarImagen() {
		try {
			
			imageProdutoFile = null;
			fileName = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the productoList
	 */
	public List<Producto> getProductoList() {
		return productoList;
	}

	/**
	 * @param productoList the productoList to set
	 */
	public void setProductoList(List<Producto> productoList) {
		this.productoList = productoList;
	}

	/**
	 * @return the productoSelected
	 */
	public Producto getProductoSelected() {
		return productoSelected;
	}

	/**
	 * @param productoSelected the productoSelected to set
	 */
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
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
	 * @return the iceList
	 */
	public List<Ice> getIceList() {
		return iceList;
	}



	/**
	 * @param iceList the iceList to set
	 */
	public void setIceList(List<Ice> iceList) {
		this.iceList = iceList;
	}



	/**
	 * @return the ivaList
	 */
	public List<Iva> getIvaList() {
		return ivaList;
	}



	/**
	 * @param ivaList the ivaList to set
	 */
	public void setIvaList(List<Iva> ivaList) {
		this.ivaList = ivaList;
	}



	/**
	 * @return the productoFormList
	 */
	public List<Producto> getProductoFormList() {
		return productoFormList;
	}



	/**
	 * @param productoFormList the productoFormList to set
	 */
	public void setProductoFormList(List<Producto> productoFormList) {
		this.productoFormList = productoFormList;
	}

	/**
	 * @return the productoFormSelected
	 */
	public Producto getProductoFormSelected() {
		return productoFormSelected;
	}



	/**
	 * @param productoFormSelected the productoFormSelected to set
	 */
	public void setProductoFormSelected(Producto productoFormSelected) {
		this.productoFormSelected = productoFormSelected;
	}



	/**
	 * @return the tipoProductoList
	 */
	public List<TipoProducto> getTipoProductoList() {
		return tipoProductoList;
	}



	/**
	 * @param tipoProductoList the tipoProductoList to set
	 */
	public void setTipoProductoList(List<TipoProducto> tipoProductoList) {
		this.tipoProductoList = tipoProductoList;
	}



	/**
	 * @return the imageProdutoFile
	 */
	public File getImageProdutoFile() {
		return imageProdutoFile;
	}



	/**
	 * @param imageProdutoFile the imageProdutoFile to set
	 */
	public void setImageProdutoFile(File imageProdutoFile) {
		this.imageProdutoFile = imageProdutoFile;
	}


}
