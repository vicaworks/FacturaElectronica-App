/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.dto.ImportProductoDto;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.FabricanteServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.ImportarProductoServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.KardexProductoServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipoProductoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.ctrl.proforma.CotizacionFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;
import com.xpert.faces.utils.FacesUtils;

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
	@EJB
	private ImportarProductoServicio importarProductoServicio;
	@EJB
	private KardexProductoServicio kardexProductoServicio;
	@EJB
	private UsuarioServicio usuarioServicio;
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	
	
	private List<Producto> productoList;
	private Producto productoSelected=new Producto();
	private Producto productoFormSelected=new Producto();
	private String criterioBusqueda;
	private List<Fabricante> fabricanteList;
	private List<TipoProducto> tipoProductoList;
	private List<Categoria> categoriaList; 
	private List<Ice> iceList;
	private List<Iva> ivaList;
	private List<Producto> productoFormList;
	private File fileProductos;
	private String nombreFileProducto;
	private boolean existeNovedades;
	private boolean renderResultadoImportProducto;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private InventarioCtrl inventarioCtrl;
	private ListaProductoCtrl listaProductoCtrl;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;

	/**
	 * 
	 */
	public ProductoCtrl() {
		
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarProducto();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
			nuevoProducto();
			
			switch (callModule) {
			case "LISTA_PRODUCTO":
//				AppJsfUtil.hideModal("dlgListaProducto");
				break;
			default:
				break;
			}
			AppJsfUtil.showModalRender("dlgProducto", "frmProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevoProducto() throws DaoException {
		productoFormSelected = new Producto();
		productoSelected = new Producto();
		productoSelected.setEstablecimiento(establecimientoMain);
		productoSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		productoSelected.setStock(BigDecimal.ZERO);
		productoSelected.setUnidadesporpaquete(Integer.valueOf(0));
		productoSelected.setUnidadesporcajaofrasco(Integer.valueOf(0));
		productoSelected.setPreciouno(BigDecimal.ZERO);
		productoSelected.setPreciodos(BigDecimal.ZERO);
		productoSelected.setPreciotres(BigDecimal.ZERO);
		productoSelected.setPreciounitario(BigDecimal.ZERO);
		productoSelected.setStockmaximo(BigDecimal.ZERO);
		productoSelected.setStockminimo(BigDecimal.ZERO);
		productoSelected.setTipoventa(1);
		productoSelected.setIva(ivaServicio.getIvaDao().getDefecto(establecimientoMain.getEmpresa().getIdempresa()));
		// valida
		if(productoSelected.getIva()==null) {
			AppJsfUtil.addErrorMessage("frmProducto","ERROR","NO EXISTE IVA POR DEFECTO, CONFIGURACION / IVA : SELECCIONAR POR DEFECTO");
			return;
		}
		productoSelected.setConversionmedida(BigDecimal.ZERO);
		productoSelected.setPorcentajedescuento(BigDecimal.ZERO);
		consultarFabrica();
		consultarCategoria();
		consultarIce();
		consultarIva();
		consultarProductoForm();
		consultarTipoProducto();
	}
	
	public void nuevoForm() {
		try {
			nuevoProducto();
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
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.ACTIVO,establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarFabrica()throws DaoException{
		fabricanteList = null;
		fabricanteList = fabricanteServicio.getFabricanteDao().getByEstado(EstadoRegistroEnum.ACTIVO,establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException{
		ivaList = null;
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO,establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarIce()throws DaoException{
		iceList = null;
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO,establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarProductoForm()throws DaoException{
		productoFormList = null;
		productoFormList = productoServicio.getProductoDao().getByQuery(EstadoRegistroEnum.ACTIVO, establecimientoMain.getIdestablecimiento());
	}
	
	public void consultarProducto()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:productoDT");
		productoList = null;
		productoList = productoServicio.getProductoDao().consultarAllImageEager(establecimientoMain.getIdestablecimiento(),criterioBusqueda);
	}
	
	public void handleUpload(FileUploadEvent event) throws IOException {
		
		UploadedFile uploadedFile = event.getFile();
		productoSelected.setImagen(uploadedFile.getContent());
		productoSelected.setNombreimagen(uploadedFile.getFileName());
		
	}
	
	public String getNombreImagen() {
		
		if(productoSelected!=null) {
			return productoSelected.getNombreimagen();
		}
		
		return "";
	}
	
	public StreamedContent getImageProducto() {
		try {
			
			if(productoSelected!=null && productoSelected.getImagen()==null) {
				return null;
			}
			
			return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(productoSelected.getImagen())).build();
			
//			return new  DefaultStreamedContent(new ByteArrayInputStream(productoSelected.getImagen()));
			
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
			
			return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(img)).build();
			
//			DefaultStreamedContent.builder().contentType("").name("").stream(() -> new ByteArrayInputStream(img)).build() 
//			return  new DefaultStreamedContent(new ByteArrayInputStream(img));
			
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
			if(productoServicio.getProductoDao().existeNombreGenerico(productoSelected.getNombregenerico(), productoSelected.getIdproducto(), establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","EL NOMBRE COMERCIAL YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmProducto:intNombreProductoComercial","YA EXISTE.");
				return;
			}
			//2. nombre
			if(productoServicio.getProductoDao().existeNombre(productoSelected.getNombre(), productoSelected.getIdproducto(), establecimientoMain.getIdestablecimiento())) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","EL NOMBRE YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmProducto:intNombreProducto","YA EXISTE.");
				return;
			}
			
			//3. codigo no se repita
			if(productoSelected.getCodigoprincipal()!=null && productoSelected.getCodigoprincipal().trim().length()>0) {
				if(productoServicio.getProductoDao().existeCodigoProducto(productoSelected.getCodigoprincipal(), productoSelected.getIdproducto(), establecimientoMain.getIdestablecimiento())) {
					AppJsfUtil.addErrorMessage("frmProducto", "ERROR",msg.getString("label.codigo") +   " DEL PRODUCTO YA EXISTE.");
					AppJsfUtil.addErrorMessage("frmProducto:intCodigoProducto","YA EXISTE.");
					return;
				}
			}
			
			// valida si el medida tiene espacios en blanco 
			if(productoSelected.getMedida()!=null && productoSelected.getMedida().trim().length()==0) {
				productoSelected.setMedida(null);
				productoSelected.setConversionmedida(BigDecimal.ZERO);
			}
			
			//4. si existe medida de conversion
			if(productoSelected.getMedida()!=null && productoSelected.getConversionmedida().floatValue()<0.001f) {
				AppJsfUtil.addErrorMessage("frmProducto", "ERROR","AL EXISTIR MEDIDA DE " + msg.getString("label.conversion") +   " DEBE EXISTIR UN VALOR > 0.");
				AppJsfUtil.addErrorMessage("frmProducto:intUniConversionProducto","MAYOR A 0.");
				return;
			}
			if(productoSelected.getNombregenerico()==null || productoSelected.getNombregenerico().trim().length()==0) {
				productoSelected.setNombregenerico(productoSelected.getNombre());				
			}
			
			productoSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			productoSelected.setUpdated(new Date());	
			productoSelected.setNombregenerico(productoSelected.getNombre());
			productoSelected.setCodigoauxiliar(productoSelected.getCodigoprincipal());
			productoServicio.guardar(productoSelected);
			
			// lista del combo popup
			consultarProductoForm();
			String form="frmProducto";
			switch (callModule) {
			case "PRODUCTO":
				// lista principal
				consultarProducto();
				break;
			case "INVENTARIO":
				inventarioCtrl.buscarDispacher();
				break;
			case "ADQUISICION":
				productoSelected.setCantidad(1);
				adquisicionFrmCtrl.agregarProducto(productoSelected);
				break;
			case "LISTA_PRODUCTO":
				listaProductoCtrl.setCriterioBusqueda(productoSelected.getNombregenerico());
				listaProductoCtrl.refrescar();
				break;
			case "PROFORMA":
				CotizacionFormCtrl cotizacionFormCtrl = (CotizacionFormCtrl)AppJsfUtil.getManagedBean("cotizacionFormCtrl");
				cotizacionFormCtrl.getDetalleSelected().setProducto(productoSelected);
				form="frmProductoDesc";
				break;
			default:
				break;
			}
			
			AppJsfUtil.addInfoMessage(form,"OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
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
	
	public void editarDescripcionProducto(String id) {
		try {
			
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgProductoDes", "frmProductoDesc");
			
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
		consultarFabrica();
		consultarCategoria();
		consultarIce();
		consultarIva();
		consultarProductoForm();
		consultarTipoProducto();
	}
	
	public void limpiarImagen() {
		try {
			
			productoSelected.setImagen(null);
			productoSelected.setNombreimagen(null);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getPlantillaImportarProducto() {
		try {
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-importarProductos.xls");
			
			return AppJsfUtil.downloadFile(new File(path), "MAKOPV-importarProductos.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void handleUpload_Productos(FileUploadEvent event) throws IOException {
		
		try {
			existeNovedades = false;
			renderResultadoImportProducto = false;
			
			UploadedFile uploadedFile = event.getFile();
			nombreFileProducto = uploadedFile.getFileName();
			File parent = new File("uploads");
			parent.mkdirs();
			fileProductos = new File(parent, nombreFileProducto);
			FileUtils.writeByteArrayToFile(fileProductos, uploadedFile.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void importarProductos() {
		
		try {
			
			existeNovedades = false;
			renderResultadoImportProducto = false;
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileProductos));
			HSSFSheet sheet=wb.getSheetAt(0);
			
			List<ImportProductoDto> importProductoDtoList = new ArrayList<>();
			int fila=2;
			conti1:while(true) {
				HSSFRow row = sheet.getRow(fila);
				if(row==null) break;
				
				ImportProductoDto p = new ImportProductoDto();
					
					int col = 0;
					
					p.setFila(fila);
					
					HSSFCell cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setIdFabricante(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO ID FABRICANTE ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setFabricante(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO FABRICANTE ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setIdCategoria(cell.getStringCellValue());
							
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO ID CATEGORIA ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setCategoria(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO CATEGORIA ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							
							cell.setCellType(CellType.STRING);
							p.setIdTipoProducto(cell.getStringCellValue());
							
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO TIPO PRODUCTO ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setNombre(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO NOMBRE ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setNombreComercial(cell.getStringCellValue());
							
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO NOMBRE COMERCIAL ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setCodigoPrincipal(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO COD BARRA ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setDescripcion(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO DESCRIPCION ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					try {
						p.setStock(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO STOCK ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setPrecioUnitario(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO PRECIO UNITARIO ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						
						p.setIva(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO IVA ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setIce(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO ICE ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setPrecio1(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO PRECIO 1 ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setPrecio2(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO PRECIO 2 ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setPrecio3(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO PRECIO 3 ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setDescuento(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO DESCUENTO ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setUnidadesCaja(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO UNIDADES POR CAJA ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setUnidadesEmbase(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO UNIDADES POR EMBASE ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setMedida(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO MEDIDA ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					try {
						p.setConversionMedida(cell!=null?BigDecimal.valueOf(cell.getNumericCellValue()):BigDecimal.ZERO);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO CONVERSION MEDIDA ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					try {
						p.setFechaFabricacion(cell!=null?cell.getDateCellValue():null);
					} catch (Exception e) {
						p.setError(true);
						p.setNovedad("FORMATO FECHA FABRICACION ERROR");
						importProductoDtoList.add(p);
						e.printStackTrace();
							fila++;
							continue conti1;
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setNombreImagen(cell.getStringCellValue());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO NOMBRE IMAGEN ERROR");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					cell = row.getCell(col++);
					if(cell!=null) {
						try {
							cell.setCellType(CellType.STRING);
							p.setImagen(cell.getStringCellValue());
//							Hex.decodeHex(p.getImagen().toCharArray());
						} catch (Exception e) {
							p.setError(true);
							p.setNovedad("FORMATO IMAGEN ERROR DEBE ESTAR EN (HEX)");
							importProductoDtoList.add(p);
							e.printStackTrace();
							fila++;
							continue conti1;
						}
					}
					
					
				importProductoDtoList.add(p);
				fila++;
				
			}
			
			// validacion formato
			validarFormatoImportProducto(importProductoDtoList);
			
			// cargar los datos
			importProductoDtoList = importarProductoServicio.importarProductoFacade(importProductoDtoList,
					establecimientoMain.getIdestablecimiento(), AppJsfUtil.getUsuario().getIdusuario());
			
			// crear kardex
			
			for (ImportProductoDto p : importProductoDtoList) {
				if(p.getStock().doubleValue()>0d) {
					// si el stock > 0 se registra la entrada
					Producto producto = productoServicio.getProductoDao().cargar(p.getIdProducto());
					if(producto!=null && producto.getTipoProducto().getNombre().equals("PRODUCTO")) {
						KardexProducto kardexProducto = new KardexProducto();
						kardexProducto.setProducto(producto);
						kardexProducto.setEstablecimiento(producto.getEstablecimiento());
						kardexProducto.setCantidad(p.getStock());
						kardexProducto.setCostounitario(producto.getPreciounitario());
						kardexProducto.setFechacompra(producto.getFechacompra());
						kardexProducto.setFechafabricacion(producto.getFechafabricacion());
						kardexProducto.setFecharegistro(new Date());
						kardexProducto.setFechavencimiento(producto.getFechavencimiento());
						kardexProducto.setIdusuario(producto.getIdusuario());
						kardexProducto.setSaldo(BigDecimal.ZERO);
						kardexProducto.setTiporegistro("E");
						kardexProducto.setUpdated(new Date());
						kardexProducto.setObservacion("CARGA PRODUCTO EXCEL / USUARIO : " + usuarioServicio.getUsuarioDao().cargar(kardexProducto.getIdusuario()).getNombre() + " FECHA : " + FechaUtil.formatoFecha(new Date()));
						kardexProductoServicio.registrarKardexFacade(kardexProducto);
					}
				}
			}
			
			// colocar los errores en el archivo
			for (ImportProductoDto p : importProductoDtoList) {
				if(p.isError()) {
					
					// color
					HSSFCellStyle myStyle = wb.createCellStyle();
					myStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
					myStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					
					// coloca el error
					HSSFRow row = sheet.getRow(p.getFila());
					HSSFCell cell = row.createCell(23);
					
					cell.setCellValue(p.getNovedad());
					cell.setCellStyle(myStyle);
					
					// pinta de rojo la 1era celda
					cell = row.getCell(0);
					if(cell==null) cell = row.createCell(0);
					//cell.setCellStyle(myStyle);					
					
					
				}
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(fileProductos);
			wb.write(out);
			out.close();
			
			// existe novedades 
			existeNovedades = importProductoDtoList.stream().filter(x->x.isError()==true).count()>0?true:false;
			renderResultadoImportProducto = true;
			if(existeNovedades) {
				byte[] file = FileUtils.readFileToByteArray(fileProductos);
				fileProductos = File.createTempFile("FALECPV-importarProductos", ".xls");
				FileUtils.writeByteArrayToFile(fileProductos, file);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	private void validarFormatoImportProducto(List<ImportProductoDto> lista) throws DaoException {
		conti1:for (ImportProductoDto p : lista) {
			
			if(p.isError()) continue conti1;
			
			// datos obligatorios
			if(p.getCategoria()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO CATEGORIA OBLIGATORIO"):"CAMPO CATEGORIA OBLIGATORIO");
			}
			if(p.getFabricante()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO FABRICANTE OBLIGATORIO"):"CAMPO FABRICANTE OBLIGATORIO");
			}
			if(p.getIdTipoProducto()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO TIPO PRODUCTO OBLIGATORIO"):"CAMPO TIPO PRODUCTO OBLIGATORIO");
			}else if(!p.getIdTipoProducto().toUpperCase().equals("PRODUCTO") && !p.getIdTipoProducto().toUpperCase().equals("SERVICIO")){
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", TIPO PRODUCTO PUEDE PRODUCTO, SERVICIO"):"TIPO PRODUCTO PUEDE PRODUCTO, SERVICIO");
			}
			if(p.getNombre()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO NOMBRE OBLIGATORIO"):"CAMPO NOMBRE OBLIGATORIO");
			}
			if(p.getNombreComercial()==null) {
				p.setError(true);
				p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO NOMBRE COMERCIAL OBLIGATORIO"):"CAMPO NOMBRE COMERCIAL OBLIGATORIO");
			}
			if(p.getCodigoPrincipal()!=null) {
				
				if(productoServicio.getProductoDao().existeCodigoProducto(p.getCodigoPrincipal(), null, establecimientoMain.getIdestablecimiento())) {
					p.setError(true);
					p.setNovedad(p.getNovedad()!=null?p.getNovedad().concat(", CAMPO CODIGO BARRA YA EXISTE"):"CAMPO CODIGO BARRA YA EXISTE");
				}
				
			}
			
		}
	}
	
	public StreamedContent getPlantillaImportarProductoNovedades() {
		try {
			
						
			return AppJsfUtil.downloadFile(fileProductos, "MAKOPV-importarProductos.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public String redirectKardex2(Producto p) {
		
		System.out.println(p.toStringObject());
		
		return "kdx/kardexProducto.jsf?idProd=111&faces-redirect=true";
	}

	public void redirectKardex(Producto p) {
		try {
			ExternalContext e = FacesUtil.getExternalContext();
			FacesUtils.addToSession("producto", p);
			e.redirect("kdx/kardexProducto.jsf");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public StreamedContent getFileProducto() {
		
		try {
			
			if(productoList==null  || productoList.isEmpty()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-listaproducto.xls");
			
			// 2. inicializamos el excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llenaa hoja 1 del archivo
			HSSFSheet sheet=wb.getSheetAt(0);
			
			// datos de la cabecera
			HSSFRow row = sheet.getRow(3);
			HSSFCell cell = row.getCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(new Date()));
			
			row = sheet.getRow(4);
			cell = row.getCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre());
			
			row = sheet.getRow(5);
			cell = row.getCell(1);
			cell.setCellValue(TextoUtil.leftPadTexto(establecimientoMain.getCodigoestablecimiento(), 3, "0") + " " + establecimientoMain.getNombrecomercial());
			
			// lista de categoria
			int fila = 8;
			int col = 0;
			
			for (Producto p : productoList) {
				
				row = sheet.createRow(fila);
				col = 0;
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getIdproducto());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getCodigoprincipal());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getFabricante().getNombrecomercial());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getCategoria().getCategoria());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getNombregenerico());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getStock().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getPreciounitario().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getPorcentajedescuento().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getIva().getPorcentaje());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getIce().getTarifaadvalorem());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getPreciouno().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getPreciodos().doubleValue());
				
				cell = row.createCell(col++);
				cell.setCellValue(p.getPreciotres().doubleValue());
				
				cell = row.createCell(col++);
				Usuario u = usuarioServicio.consultarByPk(p.getIdusuario());
				cell.setCellValue(u.getNombre());
				
				cell = row.createCell(col++);
				cell.setCellValue(FechaUtil.formatoFecha(p.getUpdated()));
				
				fila++;
				
			}
			
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));			

			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-listaproducto.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
		
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
	 * @return the fileProductos
	 */
	public File getFileProductos() {
		return fileProductos;
	}

	/**
	 * @param fileProductos the fileProductos to set
	 */
	public void setFileProductos(File fileProductos) {
		this.fileProductos = fileProductos;
	}

	/**
	 * @return the nombreFileProducto
	 */
	public String getNombreFileProducto() {
		return nombreFileProducto;
	}

	/**
	 * @param nombreFileProducto the nombreFileProducto to set
	 */
	public void setNombreFileProducto(String nombreFileProducto) {
		this.nombreFileProducto = nombreFileProducto;
	}

	/**
	 * @return the existeNovedades
	 */
	public boolean isExisteNovedades() {
		return existeNovedades;
	}

	/**
	 * @param existeNovedades the existeNovedades to set
	 */
	public void setExisteNovedades(boolean existeNovedades) {
		this.existeNovedades = existeNovedades;
	}

	/**
	 * @return the renderResultadoImportProducto
	 */
	public boolean isRenderResultadoImportProducto() {
		return renderResultadoImportProducto;
	}

	/**
	 * @param renderResultadoImportProducto the renderResultadoImportProducto to set
	 */
	public void setRenderResultadoImportProducto(boolean renderResultadoImportProducto) {
		this.renderResultadoImportProducto = renderResultadoImportProducto;
	}

	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the formModule
	 */
	public String getFormModule() {
		return formModule;
	}

	/**
	 * @param formModule the formModule to set
	 */
	public void setFormModule(String formModule) {
		this.formModule = formModule;
	}

	/**
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
	}

	/**
	 * @return the inventarioCtrl
	 */
	public InventarioCtrl getInventarioCtrl() {
		return inventarioCtrl;
	}

	/**
	 * @param inventarioCtrl the inventarioCtrl to set
	 */
	public void setInventarioCtrl(InventarioCtrl inventarioCtrl) {
		this.inventarioCtrl = inventarioCtrl;
	}

	/**
	 * @return the adquisicionFrmCtrl
	 */
	public AdquisicionFrmCtrl getAdquisicionFrmCtrl() {
		return adquisicionFrmCtrl;
	}

	/**
	 * @param adquisicionFrmCtrl the adquisicionFrmCtrl to set
	 */
	public void setAdquisicionFrmCtrl(AdquisicionFrmCtrl adquisicionFrmCtrl) {
		this.adquisicionFrmCtrl = adquisicionFrmCtrl;
	}

	/**
	 * @return the listaProductoCtrl
	 */
	public ListaProductoCtrl getListaProductoCtrl() {
		return listaProductoCtrl;
	}

	/**
	 * @param listaProductoCtrl the listaProductoCtrl to set
	 */
	public void setListaProductoCtrl(ListaProductoCtrl listaProductoCtrl) {
		this.listaProductoCtrl = listaProductoCtrl;
	}

}
