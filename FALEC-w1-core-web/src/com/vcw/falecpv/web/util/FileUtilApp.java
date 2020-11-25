/**
 * 
 */
package com.vcw.falecpv.web.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.servitec.common.util.FileUtil;
import com.vcw.falecpv.web.common.ReportBaseController;
import com.vcw.falecpv.web.constante.ExportarFileEnum;

//import sun.security.action.GetPropertyAction;

/**
 * @author vcw
 */
public class FileUtilApp extends ReportBaseController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4747066077439293985L;

	private static Map<File, String> fileMap;
	private static DefaultStreamedContent download;
	protected Map<String, Object> parametros;
	static List<File> fileList;

//	public static final File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")).concat("tmpApp"));
//	{
//		if (tmpdir.exists()) {
//			tmpdir.delete();
//			File f = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")).concat("tmp"));
//			tmpdir.renameTo(f);
//		}
//		tmpdir.mkdir();
//	}
	public static final File tmpdir = new File(AccessController.doPrivileged( 
		       (PrivilegedAction<String>) () -> System.getProperty("java.io.tmpdir")).concat("tmpApp"));
	{
		if (tmpdir.exists()) {
			tmpdir.delete();
			File f = new File(AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("java.io.tmpdir")).concat("tmp"));
			tmpdir.renameTo(f);
		}
		tmpdir.mkdir();
	}

	/**
	 * 
	 */
	public FileUtilApp() {
		parametros = new HashMap<String, Object>();
		fileList = new ArrayList<>();
	}

	/**
	 * 
	 * Agrega a una carpeta zip los archivos del map con sus respectivo nombres
	 * 
	 * @param nombre
	 */
	public static String zipDirectory(String nombre) {
		try {
			SimpleDateFormat dateFormat  = new SimpleDateFormat("dd_MM_yyyy_hh_ss");
			//Long milisegundos = new Date().getTime();
			
			File fileZip = File.createTempFile(nombre + dateFormat.format(new Date()), ".zip");
			
			if (fileZip.exists())
				fileZip.delete();
		
			fileZip.createNewFile();

			FileOutputStream fos = new FileOutputStream(fileZip);
			ZipOutputStream zos = new ZipOutputStream(fos);

			if (fileMap != null) {
				for (Map.Entry<File, String> map : fileMap.entrySet()) {
					if (((File)map.getKey()).exists()) {
						ZipEntry ze = new ZipEntry(map.getValue());
						zos.putNextEntry(ze);
						FileInputStream fis = new FileInputStream(map.getKey());
						byte[] buffer = new byte[1024];
						int len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();
						fis.close();
					}
				}
			}

			zos.close();
			fos.close();
			
			
			return fileZip.getAbsolutePath();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Descarga el archivo zip generado previamene por el metodo zipDirectory
	 * 
	 * @param nombre
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	public static DefaultStreamedContent downloadZip(String nombreFisico, String nombre) throws FileNotFoundException, IOException {
		File file = new File(nombreFisico);  //new File(tmpdir.getAbsolutePath().concat("\\" + nombreFisico + ".zip"));
		InputStream input = new FileInputStream(file);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		setDownload(DefaultStreamedContent.builder().contentType(externalContext.getMimeType(file.getName())).name(nombre + ".zip").stream(() ->  input).build());
		
//		setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), nombre + ".zip"));
		clear();
		return download;
	}

	public <T> List<T> generateList(T t) {
		List<T> lista = new ArrayList<>();
		lista.add(t);
		return lista;
	}

	public <T> File getFileFile(String nombreReporte, T object, ExportarFileEnum exportOption) {
		return getFileFile(nombreReporte, generateList(object), exportOption);
	}

	public <T> File getFileFile(String nombreReporte, List<T> lista, ExportarFileEnum exportOption) {
		try {
			setReportName(nombreReporte.concat(".jasper"));
			setExportOption(exportOption);
			setTipoDataSorce(TipoDataSorce.COLLECTION);

			String nameReport = nombreReporte + (new SimpleDateFormat("hhmmss").format(new Date()));

			File reportFile = File.createTempFile(nameReport, consultarExtension(exportOption), tmpdir);
			OutputStream os = new FileOutputStream(reportFile);

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			parametros.put("BASE_DIR", servletContext.getRealPath(getReportDir()));
			generarReporte(parametros, null, os, lista);

			parametros = new HashMap<String, Object>();

			return reportFile;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public <T> File getFileFileExterno(String nombreReporte, List<T> lista, ExportarFileEnum exportOption, String direccionReporte) {
		try {
			setReportName(nombreReporte.concat(".jasper"));
			setExportOption(exportOption);
			setTipoDataSorce(TipoDataSorce.COLLECTION);

			String nameReport = nombreReporte + (new SimpleDateFormat("hhmmss").format(new Date()));

			File reportFile = File.createTempFile(nameReport, consultarExtension(exportOption), tmpdir);
			OutputStream os = new FileOutputStream(reportFile);

			parametros.put("BASE_DIR", direccionReporte+getReportDir());
			generarReporteExterno(parametros, null, os, lista,direccionReporte);
			parametros = new HashMap<String, Object>();

			return reportFile;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param <T>
	 * @param nombreReporte
	 * @param lista
	 * @param exportOption
	 * @param direccionReporte
	 * @return
	 */
	public <T> byte[] getReportByReportPath(String nombreReporte, List<T> lista, ExportarFileEnum exportOption, String direccionReporte) {
		try {
			setReportName(nombreReporte.concat(".jasper"));
			setExportOption(exportOption);
			setTipoDataSorce(TipoDataSorce.COLLECTION);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			parametros.put("BASE_DIR", direccionReporte + getReportDir());
			generarReporteExterno(parametros, null, os, lista,direccionReporte);
			parametros = new HashMap<String, Object>();
			return os.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public <T> String getFileResources(String nombreReporte, List<T> lista, ExportarFileEnum exportOption, String nombreMostrar, String pathResources) {
		try {
			setReportName(nombreReporte.concat(".jasper"));
			setExportOption(exportOption);
			setTipoDataSorce(TipoDataSorce.COLLECTION);

			String nameReport = nombreMostrar + (new SimpleDateFormat("YYMMDDhhmmssSSS").format(new Date()));

			File tmpdirResources = new File(pathResources);
			if (!tmpdirResources.exists()) {
				tmpdirResources.mkdir();
			}

			File reportFile = File.createTempFile(nameReport, consultarExtension(exportOption), tmpdirResources);
			OutputStream os = new FileOutputStream(reportFile);

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			parametros.put("BASE_DIR", servletContext.getRealPath(getReportDir()));
			generarReporte(parametros, null, os, lista);

			parametros = new HashMap<String, Object>();

			return reportFile.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * @author cvillarreal
	 * @param file
	 * @throws IOException
	 */
	public String saveFileWebContent(File file) throws IOException{
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String pathSaveFile = servletContext.getRealPath(getReportDir());
		
		//FileUtils.moveFile(file, new File(pathSaveFile.concat("/").concat(file.getName())));
		FileUtils.copyFile(file, new File(pathSaveFile.concat("/").concat(file.getName())));
		return "";
	}
	

	public <T> StreamedContent getFile(String nombreReporte, T object, ExportarFileEnum exportOption) {
		return getFile(nombreReporte, generateList(object), exportOption);
	}

	@SuppressWarnings("resource")
	public <T> StreamedContent getFile(String nombreReporte, List<T> lista, ExportarFileEnum exportOption) {
		try {
			setReportName(nombreReporte.concat(".jasper"));
			setExportOption(exportOption);
			setTipoDataSorce(TipoDataSorce.COLLECTION);

			File reportFile = File.createTempFile(nombreReporte, ".tmp", tmpdir);
			OutputStream os = new FileOutputStream(reportFile);

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			parametros.put("BASE_DIR", servletContext.getRealPath(getReportDir()));
			generarReporte(parametros, null, os, lista);

			InputStream is = new FileInputStream(reportFile);
			String nameReport = nombreReporte + (new SimpleDateFormat("hhmmss").format(new Date()) + consultarExtension(exportOption));

			parametros = new HashMap<String, Object>();

			return DefaultStreamedContent.builder().contentType(FileUtil.getMimeType(nameReport)).name(nameReport).stream(() -> is).build();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void setReportDir(String reportDir) {
		super.setReportDir(reportDir);
	}

	private String consultarExtension(ExportarFileEnum exportOption) {

		switch (exportOption) {
		case EXCEL:
			return ".xls";
		/*
		 * case CSV: return ".csv"; case RTF: return ".rtf"; case TEXT: return ".txt";
		 */
		case WORD:
			return ".doc";
		/*
		 * case XML: return ".xml";
		 */
		default:
			return ".pdf";
		}
	}

	/**
	 * Copia un directorio y su contenido en otro
	 * 
	 * @author wayala
	 * @param source
	 *            direccion de origen
	 * @param destination
	 *            direccion de destino
	 * @throws IOException
	 */
	public static File copyFolder(String source, String destination) throws IOException {
		File destinationF = new File(destination);
		if (!destinationF.exists()) {
			destinationF.mkdirs();
		}

		File sourceF = new File(source);
		String files[] = sourceF.list();

		if (files != null && files.length > 0) {
			for (String file : files) {
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);
				if (srcFile.isDirectory()) {
					copyFolder(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
				} else {
					Files.copy(srcFile.toPath(), destFile.toPath());
				}

			}
		} else {
			File destFile = new File(destination, sourceF.getName());
			if (!destFile.exists())
				Files.copy(sourceF.toPath(), destFile.toPath());

		}
		return destinationF;
	}

	/**
	 * elimina un arhivo o un directorio
	 * 
	 * @author wayala
	 * @param source
	 * @throws IOException
	 */
	public static void deleteFolder(String source) throws IOException {

		File sourceF = new File(source);
		String files[] = sourceF.list();
		if (files != null && files.length > 0) {
			for (String file : files) {
				File srcFile = new File(source, file);
				if (srcFile.isDirectory()) {
					deleteFolder(srcFile.getAbsolutePath());
				} else {
					Files.deleteIfExists(srcFile.toPath());
				}

			}
		} else {
			Files.deleteIfExists(sourceF.toPath());
		}

	}

	/**
	 * Transforma byte a file, el archivo es creado en temporales
	 * @author wayala
	 * @param bytes
	 * @param nombreArchivo
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static File guardarTemporal(byte[] bytes, String nombreArchivo, String extension) throws IOException {

		Long lo = new Date().getTime();
		File tempFile = File.createTempFile(nombreArchivo.concat(lo.toString()), extension);

		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(bytes);
		fos.close();

		return tempFile;

	}
	
	public static File guardarTemporalByNombre(byte[] bytes, String nombreArchivo, String extension) throws IOException {

		//Long lo = new Date().getTime();
		File tempFile = File.createTempFile(nombreArchivo,extension);

		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(bytes);
		fos.close();

		return tempFile;

	}
	
	/**
	 * Transforma un arhivo a bytes
	 * @author wayala
	 * @param archivo
	 * @return
	 */
	public static byte[] getFileToByte (File archivo){

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

		if (archivo.exists()) {

			try {

				bytesArray = new byte[(int) archivo.length()];

				fileInputStream = new FileInputStream(archivo);
				fileInputStream.read(bytesArray);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
		return bytesArray;
	}
	
	
	public static byte[] getFiletoByteUpload(File archivo){

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

			try {

				bytesArray = new byte[(int) archivo.length()];

				fileInputStream = new FileInputStream(archivo);
				fileInputStream.read(bytesArray);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		return bytesArray;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param srcFile
	 * @param dirDestFile
	 * @throws IOException
	 */
	public static void moveFile(File srcFile,String dirDestFile) throws IOException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		System.out.println(servletContext.getRealPath(dirDestFile));
		
		FileUtils.moveFile(srcFile, new File(servletContext.getRealPath(dirDestFile).concat("/").concat(srcFile.getName())));
		FileUtils.deleteDirectory(srcFile);
		
	}

	/**
	 * limpia el mapa
	 */
	public static void clear() {
		fileMap.clear();
	}

	/**
	 * @return the download
	 */
	public static DefaultStreamedContent getDownload() {
		return download;
	}

	/**
	 * @param download
	 *            the download to set
	 */
	public static void setDownload(DefaultStreamedContent download) {
		FileUtilApp.download = download;
	}

	/**
	 * @return the fileMap
	 */
	public static Map<File, String> getFileMap() {
		return fileMap;
	}

	/**
	 * @param fileMap
	 *            the fileMap to set
	 */
	public static void setFileMap(Map<File, String> fileMap) {
		FileUtilApp.fileMap = fileMap;
	}

	/**
	 * @return the parametros
	 */
	public Map<String, Object> getParametros() {
		return parametros;
	}

	/**
	 * @param parametros
	 *            the parametros to set
	 */
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public static List<File> getFileList() {
		return fileList;
	}

	public static void setFileList(List<File> fileList) {
		FileUtilApp.fileList = fileList;
	}

}
