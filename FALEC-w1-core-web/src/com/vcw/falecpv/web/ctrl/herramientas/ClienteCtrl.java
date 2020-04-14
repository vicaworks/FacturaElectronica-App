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
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.dto.ImportClienteDto;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ImportarClienteServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
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
			// validación 
			
			// 1. Identificación
			if(clienteServicio.getClienteDao().existeIdentificacion(clienteSelected.getIdentificacion(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmCliente", "ERROR", msg.getString("mensaje.identificacionexiste"));
				AppJsfUtil.addErrorMessage("frmCliente:intIdentificacion","YA EXISTE.");
				return;
			}
			clienteSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			clienteSelected.setUpdated(new Date());
			clienteServicio.guardar(clienteSelected);
			
			// lista principal
			consultarClientes();
			
			AppJsfUtil.addInfoMessage("frmCliente","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	@Override
	public void nuevo() {
		try {
			clienteSelected = new Cliente();
			clienteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
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
			
			return AppJsfUtil.downloadFile(new File(path), "FALECPV-importarClientes.xls");
			
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
			FileUtils.writeByteArrayToFile(fileClientes, uploadedFile.getContents());
			
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
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setIdTipoIdentificacion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO ID TIPO IDENTIFICACIÓN ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setIdentificacion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO IDENTIFICACIÓN ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setRazonSocial(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO RAZÓN SOCIAL ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setDireccion(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCIÓN ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setCorreoElectronico(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CORREO ELECTRÓNICO ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setTelefono(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELÉFONO ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setCedulaGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CÉDULA GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setDireccionGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCIÓN GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setTelefonoGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELÉFONO GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setOcupacionGarante1(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO OCUPACIÓN GARANTE 1 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setCedulaGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO CÉDULA GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setDireccionGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO DIRECCIÓN GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setTelefonoGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO TELÉFONO GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
				
				cell = row.getCell(col++);
				if(cell!=null) {
					try {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						c.setOcupacionGarante2(cell.getStringCellValue());
					} catch (Exception e) {
						c.setError(true);
						c.setNovedad("FORMATO OCUPACIÓN GARANTE 2 ERROR");
						importClienteDtoList.add(c);
						e.printStackTrace();
						fila++;
						continue conti1;
					}
				}
					
				importClienteDtoList.add(c);
				fila++;
				
			}
			
			// validación formato
			validarFormatoImportCliente(importClienteDtoList);
			
			// cargar los datos
			importClienteDtoList = importarClienteServicio.importarClienteFacade(importClienteDtoList,
					AppJsfUtil.getEstablecimiento().getEmpresa(), AppJsfUtil.getUsuario().getIdusuario());
			
			// colocar los errores en el archivo
			for (ImportClienteDto c : importClienteDtoList) {
				if(c.isError()) {
					
					// color
					HSSFCellStyle myStyle = wb.createCellStyle();
					myStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
					myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					
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
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmImportCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	private void validarFormatoImportCliente(List<ImportClienteDto> lista) throws DaoException {
		conti1:for (ImportClienteDto c : lista) {
			
			if(c.isError()) continue conti1;
			
			// datos obligatorios
			if(c.getIdTipoIdentificacion()==null) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO ID TIPO IDENTIFICACION OBLIGATORIO"):"CAMPO ID TIPO IDENTIFICACION OBLIGATORIO");
			}
			if(c.getIdentificacion()==null) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION OBLIGATORIO"):"CAMPO IDENTIFICACION OBLIGATORIO");
			} else {
				if (clienteServicio.getClienteDao().existeIdentificacion(c.getIdentificacion(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION YA EXISTE"):"CAMPO IDENTIFICACION YA EXISTE");
				}
				if (!isNumeric(c.getIdentificacion())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO IDENTIFICACION FORMATO INCORRECTO"):"CAMPO IDENTIFICACION FORMATO INCORRECTO");
				}
			}
			if(c.getRazonSocial()==null) {
				c.setError(true);
				c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO RAZON SOCIAL OBLIGATORIO"):"CAMPO RAZON SOCIAL OBLIGATORIO");
			}
			
			if(c.getTelefono()!=null) {
				if (!isNumeric(c.getTelefono())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO FORMATO INCORRECTO"):"CAMPO TELEFONO FORMATO INCORRECTO");
				}
			}
			
			if(c.getCedulaGarante1()!=null) {
				if (!isNumeric(c.getCedulaGarante1())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO CEDULA GARANTE 1 FORMATO INCORRECTO"):"CAMPO CEDULA GARANTE 1 FORMATO INCORRECTO");
				}
			}
			
			if(c.getTelefonoGarante1()!=null) {
				if (!isNumeric(c.getTelefonoGarante1())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO GARANTE 1 FORMATO INCORRECTO"):"CAMPO TELEFONO GARANTE 1 FORMATO INCORRECTO");
				}
			}
			
			if(c.getCedulaGarante2()!=null) {
				if (!isNumeric(c.getCedulaGarante2())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO CEDULA GARANTE 2 FORMATO INCORRECTO"):"CAMPO CEDULA GARANTE 2 FORMATO INCORRECTO");
				}
			}
			
			if(c.getTelefonoGarante2()!=null) {
				if (!isNumeric(c.getTelefonoGarante2())) {
					c.setError(true);
					c.setNovedad(c.getNovedad()!=null?c.getNovedad().concat(", CAMPO TELEFONO GARANTE 2 FORMATO INCORRECTO"):"CAMPO TELEFONO GARANTE 2 FORMATO INCORRECTO");
				}
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
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(1);
				cell.setCellValue(c.getIdentificacion());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(2);
				cell.setCellValue(c.getRazonsocial());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(3);
				cell.setCellValue(c.getDireccion());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(4);
				cell.setCellValue(c.getCorreoelectronico());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(5);
				cell.setCellValue(c.getTelefono());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(6);
				cell.setCellValue(usuarioServicio.getUsuarioDao().cargar(c.getIdusuario()).getNombre());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(7);
				cell.setCellValue(c.getUpdated());
				UtilExcel.setHSSBordeCell(cell,"dd/mm/yyyy HH:mm");
				
				cell = row.createCell(8);
				if(c.getEstado().compareTo("A") == 0) {
					cell.setCellValue("ACTIVO");
				}
				else {
					cell.setCellValue("INACTIVO");
				}
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(9);
				cell.setCellValue(c.getNombregarante1());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(10);
				cell.setCellValue(c.getCedulagarante1());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(11);
				cell.setCellValue(c.getDirecciongarante1());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(12);
				cell.setCellValue(c.getTelefonogarante1());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(13);
				cell.setCellValue(c.getOcupaciongarante1());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(14);
				cell.setCellValue(c.getNombregarante2());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(15);
				cell.setCellValue(c.getCedulagarante2());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(16);
				cell.setCellValue(c.getDirecciongarante2());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(17);
				cell.setCellValue(c.getTelefonogarante2());
				UtilExcel.setHSSBordeCell(cell);
				
				cell = row.createCell(18);
				cell.setCellValue(c.getOcupaciongarante2());
				UtilExcel.setHSSBordeCell(cell);
				
				fila++;
			}
			
			wb.setActiveSheet(0);
			sheet = wb.getSheetAt(0);
			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));
			
			// cerrando recursos
			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();
			
			return AppJsfUtil.downloadFile(tempXls,"FALECPV-clienteList.xls");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		
		}
		
		return null;
	}
	
	public StreamedContent getPlantillaImportarClienteNovedades() {
		try {
				
			return AppJsfUtil.downloadFile(fileClientes, "FALECPV-importarClientes.xls");
			
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
}