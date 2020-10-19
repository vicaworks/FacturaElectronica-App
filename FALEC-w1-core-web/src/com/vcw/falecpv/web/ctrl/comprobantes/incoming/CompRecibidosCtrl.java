/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ImportComprobanteEnum;
import com.vcw.falecpv.core.exception.FormatoArchivoException;
import com.vcw.falecpv.core.helper.SriAccesoHelper;
import com.vcw.falecpv.core.modelo.dto.FileSriDto;
import com.vcw.falecpv.core.modelo.dto.SriAccesoDto;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.servicio.sriimportarcomp.SriImportarComprobantesServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;
import com.vcw.sri.ws.exception.SriWebServiceExeption;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CompRecibidosCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 919159256550312725L;
	
	@EJB
	private SriImportarComprobantesServicio sriImportarComprobantesServicio;
	@EJB
	private SriAccesoHelper sriAccesoHelper;
	

	private String comprobanteRender = "IMPORTAR";
	private File fileComprobantes;
	private String nombreFile;
	private List<FileSriDto> fileSriDtoList;
	private Integer progress = 0;
	private boolean flagCargando = false;
	private Comprobanterecibido comprobanterecibidoSelected;
	private boolean formatoXml;
	
	/**
	 * 
	 */
	public CompRecibidosCtrl() {
	}
	
	public void switchComprobante(String comprobante) {
		comprobanteRender = comprobante;
		System.out.println(comprobante);
	}
	
	public void handleUpload(FileUploadEvent event) throws IOException {
		
		try {
			
			UploadedFile uploadedFile = event.getFile();
			nombreFile = uploadedFile.getFileName();
			File parent = new File("uploads");
			parent.mkdirs();
			fileComprobantes = new File(parent, nombreFile);
			FileUtils.writeByteArrayToFile(fileComprobantes, uploadedFile.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formCRFile", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	
	public void limpiar() {
		try {
			flagCargando = false;
			fileComprobantes = null;
			fileSriDtoList=null;
			nombreFile = null;
			AppJsfUtil.ajaxUpdate("formCRFile");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void showUploadFile() {
		try {
			flagCargando = false;
			fileComprobantes = null;
			nombreFile = null;
			AppJsfUtil.showModalRender("dlgCRImportFile", "formCRFile");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void aceptarFile() {
		try {
			
			fileSriDtoList = null;
			if(fileComprobantes==null) {
				AppJsfUtil.addErrorMessage("formCRFile", "ERROR", "No existe archivo seleccionado.");
				return;
			}
			populateFile();
			flagCargando = false;
			AppJsfUtil.hideModal("dlgCRImportFile");
			AppJsfUtil.updateComponente("formMain");
			
		} catch (FormatoArchivoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formCRFile", "ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formCRFile", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void populateFile() throws IOException,FormatoArchivoException {
		fileSriDtoList = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(fileComprobantes));
		String line = reader.readLine();
		int fila = 1;
		String importe = null;
		String anterior = null;
		while(line!=null) {
			if(fila>2 && (fila%2)!=0) {
				anterior = line;
			}else if(fila>2 && (fila%2)==0){
				importe = line;				
				fileSriDtoList.add(populateFileSriDto(anterior.concat("\t").concat(importe), fila+1));
			}
			fila++;
			line = reader.readLine();
		}
		reader.close();
		
		if(fila==1) {
			throw new FormatoArchivoException(msg.getString("label.archivovacio"));
		}
	}
	
	private FileSriDto populateFileSriDto(String line,Integer id)throws FormatoArchivoException {
		FileSriDto fileSriDto = new FileSriDto();
		fileSriDto.setEstado(ImportComprobanteEnum.PENDIENTE);
		String[] fileLine = line.split("\t");
		fileSriDto.setId(id);
		fileSriDto.setComprobante(fileLine[0]);
		fileSriDto.setSerieComprobante(fileLine[1]);
		fileSriDto.setRucEmisor(fileLine[2]);
		fileSriDto.setEmisor(fileLine[3]);
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			fileSriDto.setFechaEmision(sf.parse(fileLine[4]));
		} catch (ParseException e) {
			throw new FormatoArchivoException("Formato de archivo incorrecto: en la fecha de " + msg.getString("label.emision"));
		}
		try {
			sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fileSriDto.setFechaAutorizacion(sf.parse(fileLine[5]));
		} catch (ParseException e) {
			throw new FormatoArchivoException("Formato de archivo incorrecto: en la fecha de " + msg.getString("label.emision"));
		}
		fileSriDto.setTipoEmision(fileLine[6]);
		fileSriDto.setIdentificacionReceptor(fileLine[8]);
		fileSriDto.setClaveAcceso(fileLine[9]);
		fileSriDto.setNumeroAutorizacion(fileLine[10]);
		try {
			fileSriDto.setImporteTotal(BigDecimal.valueOf(Double.parseDouble(fileLine[11])));
		} catch (Exception e) {
			fileSriDto.setImporteTotal(BigDecimal.ZERO);
		}
		
		
		return fileSriDto;
	}
	
	public void cargarComprobantes() {
		try {
			
			flagCargando = true;
			// determina el acceso a los ws
			SriAccesoDto sriAccesoDto = sriAccesoHelper.consultarDatosAcceso("APROBACION", true);
			int cont = 1;
			for (FileSriDto f : fileSriDtoList) {
				// validaciones
				validar(f);
				f.setRegistrado(false);
				if(f.isValidacion()) {
					sriImportarComprobantesServicio.importarComprobanteFacade(f, sriAccesoDto,
							AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
							AppJsfUtil.getEstablecimiento().getIdestablecimiento(),
							AppJsfUtil.getUsuario().getIdusuario());
				}
				
				if(progress<100) {					
					progress = ((cont*100)/fileSriDtoList.size());
					
				}else {
					progress = 100;
				}
				cont++;
			}
			progress = 0;
			flagCargando = false;
			
			int errores = (int) fileSriDtoList.stream().filter(x->x.isValidacion()==false || x.isRegistrado()==false).count();
			
			// ordenan la lista primero los errores 
			List<FileSriDto> listaErrores = fileSriDtoList.stream().filter(x->x.isRegistrado()==false).collect(Collectors.toList());
			List<FileSriDto> lista = fileSriDtoList.stream().filter(x->x.isRegistrado()).collect(Collectors.toList());
			listaErrores.addAll(lista);
			fileSriDtoList = listaErrores;
			
			if(errores>0) {
				messageCtrl.cargarMenssage("WARNING", "EXISTEN ERRORES :" + errores  + " EN LA CARGA DE COMPROBANTES.", "WARNING");
			}else {
				messageCtrl.cargarMenssage("OK", "TODOS LOS COMPROBANTES FUERON GUARDADOS CORRECTAMENTE.", "OK");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccesoWsdlSriException || e instanceof SriWebServiceExeption) {
				messageCtrl.cargarMenssage("ERROR", e.getMessage(), "ERROR");
			}else {
				messageCtrl.cargarMenssage("ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")), "ERROR");
			}
		}finally {
			progress = 0;
			flagCargando = false;
		}
	}
	
	private void validar(FileSriDto fileSriDto) {
		fileSriDto.setValidacion(true);
		// valida si pertenece a la empresa
		if(!AppJsfUtil.getEstablecimiento().getEmpresa().getRuc().contains(fileSriDto.getIdentificacionReceptor())) {
			fileSriDto.setValidacion(false);
			fileSriDto.setEstado(ImportComprobanteEnum.ERROR);
			fileSriDto.setMensaje(msg.getString("mensaje.importcomprobante_1"));
			return;
		}
		
	}

	/**
	 * @return the comprobanteRender
	 */
	public String getComprobanteRender() {
		return comprobanteRender;
	}

	/**
	 * @param comprobanteRender the comprobanteRender to set
	 */
	public void setComprobanteRender(String comprobanteRender) {
		this.comprobanteRender = comprobanteRender;
	}

	/**
	 * @return the fileComprobantes
	 */
	public File getFileComprobantes() {
		return fileComprobantes;
	}

	/**
	 * @param fileComprobantes the fileComprobantes to set
	 */
	public void setFileComprobantes(File fileComprobantes) {
		this.fileComprobantes = fileComprobantes;
	}

	/**
	 * @return the nombreFile
	 */
	public String getNombreFile() {
		return nombreFile;
	}

	/**
	 * @param nombreFile the nombreFile to set
	 */
	public void setNombreFile(String nombreFile) {
		this.nombreFile = nombreFile;
	}

	/**
	 * @return the fileSriDtoList
	 */
	public List<FileSriDto> getFileSriDtoList() {
		return fileSriDtoList;
	}

	/**
	 * @param fileSriDtoList the fileSriDtoList to set
	 */
	public void setFileSriDtoList(List<FileSriDto> fileSriDtoList) {
		this.fileSriDtoList = fileSriDtoList;
	}

	/**
	 * @return the progress
	 */
	public Integer getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	/**
	 * @return the flagCargando
	 */
	public boolean isFlagCargando() {
		return flagCargando;
	}

	/**
	 * @param flagCargando the flagCargando to set
	 */
	public void setFlagCargando(boolean flagCargando) {
		this.flagCargando = flagCargando;
	}

	/**
	 * @return the comprobanterecibidoSelected
	 */
	public Comprobanterecibido getComprobanterecibidoSelected() {
		return comprobanterecibidoSelected;
	}

	/**
	 * @param comprobanterecibidoSelected the comprobanterecibidoSelected to set
	 */
	public void setComprobanterecibidoSelected(Comprobanterecibido comprobanterecibidoSelected) {
		this.comprobanterecibidoSelected = comprobanterecibidoSelected;
	}

	/**
	 * @return the formatoXml
	 */
	public boolean isFormatoXml() {
		return formatoXml;
	}

	/**
	 * @param formatoXml the formatoXml to set
	 */
	public void setFormatoXml(boolean formatoXml) {
		this.formatoXml = formatoXml;
	}

}
