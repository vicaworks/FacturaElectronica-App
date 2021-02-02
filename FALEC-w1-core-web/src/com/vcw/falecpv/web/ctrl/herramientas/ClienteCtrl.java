package com.vcw.falecpv.web.ctrl.herramientas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.ExpresionRegularValidador;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ValidarExpresionException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.dto.ImportClienteDto;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ImportarClienteServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.RetencionFrmCtrl;
import com.vcw.falecpv.web.ctrl.cajachica.CajaChicaCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.fac.CompFacCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.liqcompra.LiqCompraFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nc.NotaCreditoCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nd.NotaDebitoFrmCtrl;
import com.vcw.falecpv.web.ctrl.facturacion.FactMainPagoCtrl;
import com.vcw.falecpv.web.ctrl.proforma.CotizacionFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author Jorge Toaza
 *
 */
@Named
@ViewScoped
public class ClienteCtrl extends BaseCtrl {
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private ClienteServicio clienteServicio;
	@EJB
	private ImportarClienteServicio importarClienteServicio;
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private String criterioBusqueda;
	private File fileClientes;
	private String nombreFileCliente;
	private boolean existenNovedades;
	private boolean renderResultadoImportCliente;
	private List<Cliente> clienteList;
	private Cliente clienteSelected;
	private String callModule;
	private String updateView;	
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private RetencionFrmCtrl retencionFrmCtrl;
	private CajaChicaCtrl cajaChicaCtrl;
	private LiqCompraFormCtrl liqCompraFormCtrl;
	
	
	public ClienteCtrl() {
	}

	@PostConstruct
	private void init() {
		clienteList = new ArrayList<>();
		try {
			consultarClientes();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void refrescar() {
		try {
			clienteSelected = null;
			consultarClientes();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		
		try {
			
			// 1. Identificacion
			if(clienteServicio.getClienteDao().existeIdentificacion(clienteSelected.getIdcliente(),clienteSelected.getIdentificacion(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmCliente", "ERROR", msg.getString("mensaje.identificacionexiste"));
				AppJsfUtil.addErrorMessage("frmCliente:intIdentificacion","YA EXISTE.");
				return;
			}
			clienteSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			clienteSelected.setUpdated(new Date());
			clienteServicio.guardar(clienteSelected);
			
			switch (callModule) {
			case "CLIENTE":
				consultarClientes();
				AppJsfUtil.addInfoMessage("frmCliente","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
				break;
				
			case "LIQCOMPRA":
				liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
				liqCompraFormCtrl.getLiqCompraSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "RETENCION":
				retencionFrmCtrl = (RetencionFrmCtrl)AppJsfUtil.getManagedBean("retencionFrmCtrl");
				retencionFrmCtrl.getRetencionSeleccion().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "CAJACHICA": case "cajaChicaCtrl" :
				cajaChicaCtrl.getTransaccionSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "ADQUISICION":
				adquisicionFrmCtrl.getAdquisicionSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "FactMainPagoCtrl":
				FactMainPagoCtrl fp = (FactMainPagoCtrl)AppJsfUtil.getManagedBean("factMainPagoCtrl");
				fp.getCabecerSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
			
			case "CompFacCtrl":
				CompFacCtrl cf = (CompFacCtrl)AppJsfUtil.getManagedBean("compFacCtrl");
				cf.getCabecerSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "NotaCreditoCtrl":
				NotaCreditoCtrl notaCreditoCtrl = (NotaCreditoCtrl)AppJsfUtil.getManagedBean("notaCreditoCtrl");
				notaCreditoCtrl.getNotaCreditoSeleccion().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "guiaRemFormCtrl":
				GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				guiaRemFormCtrl.getDestinatarioSelected().setCliente(clienteSelected);
				guiaRemFormCtrl.getDestinatarioSelected().setIdentificaciondestinatario(clienteSelected.getIdentificacion());
				guiaRemFormCtrl.getDestinatarioSelected().setRazonsocialdestinatario(clienteSelected.getRazonsocial());
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "NotaDebitoFrmCtrl":
				NotaDebitoFrmCtrl notaDebitoFrmCtrl = (NotaDebitoFrmCtrl) AppJsfUtil.getManagedBean("notaDebitoFrmCtrl");
				notaDebitoFrmCtrl.getNotDebitoSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			case "cotizacionFormCtrl":
				CotizacionFormCtrl cotizacionFormCtrl = (CotizacionFormCtrl)AppJsfUtil.getManagedBean("cotizacionFormCtrl");
				cotizacionFormCtrl.getCabecerSelected().setCliente(clienteSelected);
				AppJsfUtil.hideModal("dlgCliente");
				break;
				
			default:
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void nuevoEditar(String id) {
		try {
			if(id!=null) {
				editarFacade(id);
			}
			
			if(clienteSelected==null) {
				clienteSelected = new Cliente();
				clienteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
				clienteSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			}
			
			AppJsfUtil.showModalRender("dlgCliente", "frmCliente");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevoModal() {
		try {
			clienteSelected = new Cliente();
			clienteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			clienteSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	@Override
	public void nuevo() {
		try {
			clienteSelected = new Cliente();
			clienteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			clienteSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			AppJsfUtil.showModalRender("dlgCliente", "frmCliente");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(clienteSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			clienteServicio.eliminar(clienteSelected);
			
			clienteSelected = null;
			consultarClientes();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarClientes()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:clienteDT");
		clienteList = null;
		clienteList = clienteServicio.getClienteDao().consultarClientes(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), criterioBusqueda);
	}
	
	public void verCliente(String id) {
		try {
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgVerCliente", "frmVerCliente");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editarCliente(String id) {
		try {
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgCliente", "frmCliente");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void editarFacade(String id) throws DaoException, IOException {
		
		clienteSelected = clienteServicio.getClienteDao().cargar(id);

	}
	
	public StreamedContent getPlantillaImportarCliente() {
		try {
			
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.reporte") + "FALECPV-importarClientes.xls");
			
			return AppJsfUtil.downloadFile(new File(path), "MAKOCPV-importarClientes.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public void handleUpload_Clientes(FileUploadEvent event) throws IOException {
		
		try {
			existenNovedades = false;
			renderResultadoImportCliente = false;
			
			UploadedFile uploadedFile = event.getFile();
			nombreFileCliente = uploadedFile.getFileName();
			File parent = new File("uploads");
			parent.mkdirs();
			fileClientes = new File(parent, nombreFileCliente);
			FileUtils.writeByteArrayToFile(fileClientes, uploadedFile.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void importarClientes() {
		
		try {
			
			existenNovedades = false;
			renderResultadoImportCliente = false;
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileClientes));
			HSSFSheet sheet=wb.getSheetAt(0);
			
			List<ImportClienteDto> importClienteDtoList = new ArrayList<>();
			int fila = 2;
			conti1:while(true) {
				HSSFRow row = sheet.getRow(fila);
				if(row==null) break;
				
				ImportClienteDto c = new ImportClienteDto();
					
				int col = 0;
				
				c.setFila(fila);
				
				HSSFCell cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setIdTipoIdentificacion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO ID TIPO IDENTIFICACION ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setIdentificacion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO IDENTIFICACION ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setRazonSocial(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO RAZON SOCIAL ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setDireccion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCION ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						
						c.setCorreoElectronico(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CORREO ELECTRONICO ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setTelefono(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELOFONO ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setNombreGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO NOMBRE GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setCedulaGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CODULA GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setDireccionGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCION GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setTelefonoGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELOFONO GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setOcupacionGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO OCUPACION GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setNombreGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO NOMBRE GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setCedulaGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CODULA GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setDireccionGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCION GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setTelefonoGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELOFONO GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(CellType.STRING);
						c.setOcupacionGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO OCUPACION GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
					
				importClienteDtoList.add(c);
				fila++;
				
			}
			
			// validaciOn formato
			validarFormatoImportCliente(importClienteDtoList);
			
			// cargar los datos
			importClienteDtoList = importarClienteServicio.importarClienteFacade(importClienteDtoList,
					AppJsfUtil.getEstablecimiento().getEmpresa(), AppJsfUtil.getUsuario().getIdusuario());
			
			// colocar los errores en el archivo
			for (ImportClienteDto c : importClienteDtoList) {
				if(c.isError()) {
					
					// color
					HSSFCellStyle myStyle = wb.createCellStyle();
					myStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
					myStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					
					// coloca el error
					HSSFRow row = sheet.getRow(c.getFila());
					HSSFCell cell = row.createCell(16);
					
					cell.setCellValue(c.getNovedad());
					cell.setCellStyle(myStyle);
					
					// pinta de rojo la 1era celda
					cell = row.getCell(0);
					if(cell==null) cell = row.createCell(0);		
					
				}
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A3", sheet)));
			
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(fileClientes);
			wb.write(out);
			out.close();
			
			// existen novedades 
			existenNovedades = importClienteDtoList.stream().filter(x->x.isError()==true).count()>0?true:false;
			renderResultadoImportCliente = true;
			if(existenNovedades) {
				byte[] file = FileUtils.readFileToByteArray(fileClientes);
				fileClientes = File.createTempFile("FALECPV-importarClientes", ".xls");
				FileUtils.writeByteArrayToFile(fileClientes, file);
			}
			
			refrescar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	private void validarFormatoImportCliente(List<ImportClienteDto> lista) throws DaoException {
		conti1:for (ImportClienteDto c : lista) {
			
			if(c.isError()) continue conti1;
			
			// datos obligatorios
			if(c.getIdTipoIdentificacion()==null || c.getIdTipoIdentificacion().compareTo("") == 0) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO ID TIPO IDENTIFICACION OBLIGATORIO"):"CAMPO ID TIPO IDENTIFICACION OBLIGATORIO");
			}
			if(c.getIdentificacion()==null || c.getIdentificacion().compareTo("") == 0) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION OBLIGATORIO"):"CAMPO IDENTIFICACION OBLIGATORIO");
			} else {
				if (clienteServicio.getClienteDao().existeIdentificacion(c.getIdCliente(),c.getIdentificacion(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION YA EXISTE"):"CAMPO IDENTIFICACION YA EXISTE");
				}
				if (!isNumeric(c.getIdentificacion())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION FORMATO INCORRECTO"):"CAMPO IDENTIFICACION FORMATO INCORRECTO");
				}
			}
			if(c.getRazonSocial()==null || c.getRazonSocial().compareTo("") == 0) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO RAZON SOCIAL OBLIGATORIO"):"CAMPO RAZON SOCIAL OBLIGATORIO");
			}
			
			if(c.getTelefono()!=null) {
				if(c.getTelefono().compareTo("") != 0) {
					if (!isNumeric(c.getTelefono())) {
						c.setError(true);
						c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO FORMATO INCORRECTO"):"CAMPO TELEFONO FORMATO INCORRECTO");
					}
				}
			}
			
			if(c.getCedulaGarante1()!=null) {
				if(c.getCedulaGarante1().compareTo("") != 0) {
					if (!isNumeric(c.getCedulaGarante1())) {
						c.setError(true);
						c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO CEDULA GARANTE 1 FORMATO INCORRECTO"):"CAMPO CEDULA GARANTE 1 FORMATO INCORRECTO");
					}
				}
			}
			
			if(c.getTelefonoGarante1()!=null) {
				if(c.getTelefonoGarante1().compareTo("") != 0) {
					if (!isNumeric(c.getTelefonoGarante1())) {
						c.setError(true);
						c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO GARANTE 1 FORMATO INCORRECTO"):"CAMPO TELEFONO GARANTE 1 FORMATO INCORRECTO");
					}
				}
			}
			
			if(c.getCedulaGarante2()!=null) {
				if(c.getCedulaGarante2().compareTo("") != 0) {
					if (!isNumeric(c.getCedulaGarante2())) {
						c.setError(true);
						c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO CEDULA GARANTE 2 FORMATO INCORRECTO"):"CAMPO CEDULA GARANTE 2 FORMATO INCORRECTO");
					}
				}
			}
			
			if(c.getTelefonoGarante2()!=null) {
				if(c.getTelefonoGarante2().compareTo("") != 0) {
					if (!isNumeric(c.getTelefonoGarante2())) {
						c.setError(true);
						c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO GARANTE 2 FORMATO INCORRECTO"):"CAMPO TELEFONO GARANTE 2 FORMATO INCORRECTO");
					}
				}
			}
			
			try {
				ExpresionRegularValidador.validarEmail(c.getCorreoElectronico());
			} catch (ValidarExpresionException e) {
				c.setError(true);
				c.setNovedad("CAMPO CORREO ELECTRONICO, FORMATO INCORRECTO "+e.getMessage());
			}
			
		}
	}
	
	private static boolean isNumeric(String cadena) {
		try {
			Long.parseLong(cadena);
			return true; // la cadena solo tiene numeros
		} catch (NumberFormatException nfe){
			return false; // la cadena tiene letras
		}
	}

	/**
	 * @return
	 */
	public StreamedContent getFileCliente()  {
		
		try {
			
			if(clienteList == null || clienteList.size() == 0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTEN DATOS");
				return null;
			}
			String path = FacesUtil.getServletContext().getRealPath(AppConfiguracion.getString("dir.base.reporte") + "FALECPV-clienteList.xls");
			// inicializamos el Excel
			File tempXls = File.createTempFile("plantillaExcel", ".xls");
			File template = new File(path);
			FileUtils.copyFile(template, tempXls);
			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXls));
			// llena hoja 1 del archivo
			HSSFSheet sheet = wb.getSheetAt(0);
			// datos de la cabecera
			HSSFRow row = sheet.getRow(2);
			HSSFCell cell = row.getCell(1);
			cell.setCellValue(FechaUtil.formatoFecha(new Date())); // fecha
			
			row = sheet.getRow(3);
			cell = row.getCell(1);
			cell.setCellValue(AppJsfUtil.getUsuario().getNombre()); // usuario
			
			row = sheet.getRow(4);
			cell = row.getCell(1);
			cell.setCellValue(AppJsfUtil.getEstablecimiento().getEmpresa().getNombrecomercial()); // empresa
			
			// lista de clientes
			int fila = 7;
			for(Cliente c: clienteList) {
				row = sheet.createRow(fila);
				
				cell = row.createCell(0);
				cell.setCellValue(c.getIdcliente());
				
				cell = row.createCell(1);
				cell.setCellValue(c.getIdentificacion());
				
				cell = row.createCell(2);
				cell.setCellValue(c.getRazonsocial());
				
				cell = row.createCell(3);
				cell.setCellValue(c.getDireccion());
				
				cell = row.createCell(4);
				cell.setCellValue(c.getCorreoelectronico());
				
				cell = row.createCell(5);
				cell.setCellValue(c.getTelefono());
				
				cell = row.createCell(6);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(c.getIdusuario()).getNombre());
				
				cell = row.createCell(7);
				cell.setCellValue(c.getUpdated());
				
				cell = row.createCell(8);
				if(c.getEstado().compareTo("A") == 0) {
					cell.setCellValue("ACTIVO");
				}
				else {
					cell.setCellValue("INACTIVO");
				}
				
				cell = row.createCell(9);
				cell.setCellValue(c.getNombregarante1());
				
				cell = row.createCell(10);
				cell.setCellValue(c.getCedulagarante1());
				
				cell = row.createCell(11);
				cell.setCellValue(c.getDirecciongarante1());
				
				cell = row.createCell(12);
				cell.setCellValue(c.getTelefonogarante1());
				
				cell = row.createCell(13);
				cell.setCellValue(c.getOcupaciongarante1());
				
				cell = row.createCell(14);
				cell.setCellValue(c.getNombregarante2());
				
				cell = row.createCell(15);
				cell.setCellValue(c.getCedulagarante2());
				
				cell = row.createCell(16);
				cell.setCellValue(c.getDirecciongarante2());
				
				cell = row.createCell(17);
				cell.setCellValue(c.getTelefonogarante2());
				
				cell = row.createCell(18);
				cell.setCellValue(c.getOcupaciongarante2());
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"MAKOPV-clienteList.xls");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		
		}
		
		return null;
	}
	
	public StreamedContent getPlantillaImportarClienteNovedades() {
		try {
				
			return AppJsfUtil.downloadFile(fileClientes, "MAKOPV-importarClientes.xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the fileClientes
	 */
	public File getFileClientes() {
		return fileClientes;
	}

	/**
	 * @param fileClientes the fileClientes to set
	 */
	public void setFileClientes(File fileClientes) {
		this.fileClientes = fileClientes;
	}

	/**
	 * @return the nombreFileCliente
	 */
	public String getNombreFileCliente() {
		return nombreFileCliente;
	}

	/**
	 * @param nombreFileCliente the nombreFileCliente to set
	 */
	public void setNombreFileCliente(String nombreFileCliente) {
		this.nombreFileCliente = nombreFileCliente;
	}

	/**
	 * @return the existenNovedades
	 */
	public boolean isExistenNovedades() {
		return existenNovedades;
	}

	/**
	 * @param existenNovedades the existenNovedades to set
	 */
	public void setExistenNovedades(boolean existenNovedades) {
		this.existenNovedades = existenNovedades;
	}

	/**
	 * @return the renderResultadoImportCliente
	 */
	public boolean isRenderResultadoImportCliente() {
		return renderResultadoImportCliente;
	}

	/**
	 * @param renderResultadoImportCliente the renderResultadoImportCliente to set
	 */
	public void setRenderResultadoImportCliente(boolean renderResultadoImportCliente) {
		this.renderResultadoImportCliente = renderResultadoImportCliente;
	}

	/**
	 * @return the clienteList
	 */
	public List<Cliente> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList the clienteList to set
	 */
	public void setClienteList(List<Cliente> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * @return the clienteSelected
	 */
	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	/**
	 * @param clienteSelected the clienteSelected to set
	 */
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
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
	 * @return the updateView
	 */
	public String getUpdateView() {
		return updateView;
	}

	/**
	 * @param updateView the updateView to set
	 */
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
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
	 * @return the retencionFrmCtrl
	 */
	public RetencionFrmCtrl getRetencionFrmCtrl() {
		return retencionFrmCtrl;
	}

	/**
	 * @param retencionFrmCtrl the retencionFrmCtrl to set
	 */
	public void setRetencionFrmCtrl(RetencionFrmCtrl retencionFrmCtrl) {
		this.retencionFrmCtrl = retencionFrmCtrl;
	}

	/**
	 * @return the cajaChicaCtrl
	 */
	public CajaChicaCtrl getCajaChicaCtrl() {
		return cajaChicaCtrl;
	}

	/**
	 * @param cajaChicaCtrl the cajaChicaCtrl to set
	 */
	public void setCajaChicaCtrl(CajaChicaCtrl cajaChicaCtrl) {
		this.cajaChicaCtrl = cajaChicaCtrl;
	}

	/**
	 * @return the liqCompraFormCtrl
	 */
	public LiqCompraFormCtrl getLiqCompraFormCtrl() {
		return liqCompraFormCtrl;
	}

	/**
	 * @param liqCompraFormCtrl the liqCompraFormCtrl to set
	 */
	public void setLiqCompraFormCtrl(LiqCompraFormCtrl liqCompraFormCtrl) {
		this.liqCompraFormCtrl = liqCompraFormCtrl;
	}
}