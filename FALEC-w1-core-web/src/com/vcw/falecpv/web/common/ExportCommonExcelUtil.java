/**
 * 
 */
package com.vcw.falecpv.web.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.servitec.common.jsf.FacesUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.ExcelEnum;
import com.vcw.falecpv.core.exception.ExportarFileException;
import com.vcw.falecpv.core.helper.EstatusHelper;
import com.vcw.falecpv.web.util.UtilExcel;

/**
 * @author wayala
 * 
 *         Generador de reportes en excel para plantillas tipo carta
 * 
 * @version 1.0
 *
 */
public class ExportCommonExcelUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6479600627615877890L;

	
	public enum TipoCeldaEnum {
		NUMERO, FECHA, HORA, CADENA, ESTADOENUM;
	}

	
	private String PATH_REPORTE = "/reportes/carta/";
	
	protected final String FORMATO_FECHA = "dd-MMM-yyyy";
	protected final String FORMATO_HORA = "HH:mm:ss";


	private List<ExcelEnum> alineacionCelda;
	private SimpleDateFormat sf = new SimpleDateFormat(FORMATO_FECHA);
	private SimpleDateFormat sh = new SimpleDateFormat(FORMATO_HORA);
	private String titulo = "TITULO REPORTE";
	
	
	
	/**
	 * 
	 */
	public ExportCommonExcelUtil() {

	}


	/**
	 * @param parametros
	 * @param nombreFileDescarga
	 * @throws Exception
	 */
	public <T> void generarExcelFacade(Map<String, Object> parametros, String nombreFileDescarga) throws Exception {

		File xls = generarExcel(parametros,nombreFileDescarga);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		String nameReport = nombreFileDescarga.replace(".xlsx", "") + (new SimpleDateFormat("hhmmss").format(new Date()));

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Cache-Control", "cache, must-revalidate");
		response.setHeader("Pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename=\"" + nameReport + ".xlsx\"");

		fileToOutpuStream(xls, response.getOutputStream());

		response.getOutputStream().flush();
		response.getOutputStream().close();

		FacesContext.getCurrentInstance().responseComplete();

	}



	/**
	 * @param alineacion
	 */
	public void agregarAlineacionCelda(ExcelEnum alineacion) {
		if (alineacionCelda == null) {
			alineacionCelda = new ArrayList<ExcelEnum>();
		}
		alineacionCelda.add(alineacion);
	}

	

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Escribe los datos del archivo en el stream del response para la
	 *         descarga del excel
	 * 
	 * @param f
	 *            archivo en dende se escribe
	 * @param os
	 *            objetode escritura del archivo
	 * @throws Exception
	 *             en acso de error al escribir los datos en el archivo
	 */
	private void fileToOutpuStream(File f, ServletOutputStream os) throws Exception {

		InputStream ist = new FileInputStream(f);
		byte[] buf = new byte[8192];
		int c = 0;
		while ((c = ist.read(buf, 0, buf.length)) > 0) {
			os.write(buf, 0, c);
			os.flush();
		}
		os.close();
		ist.close();

	}

	/**
	 * 
	 * @author cvillarreal
	 * @author wayala refactorizado 09/08/2017
	 *         M&eacute;todo para generar el excel
	 * 
	 * @param lista
	 *            lista gen&eacute;rica para exportar los datos
	 * @return el archivo de excel
	 * @throws HssException
	 *             en caso de error de la construcci&oacute;n
	 */
	public <T> File generarExcel(Map<String, Object> parametros, String nombreFileDescarga) throws ExportarFileException {

		try {

			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(getPahArchivo(PATH_REPORTE, nombreFileDescarga));
			FileUtils.copyFile(template, tempXls);

			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));

			XSSFSheet sheet = wb.getSheetAt(0);

			sheet = wb.getSheetAt(0);

			for (Entry<String, Object> e: parametros.entrySet()) {
		        pintarFilaDatos(e.getValue(), e.getKey(), sheet);
		    }

			sheet.setActiveCell(new CellAddress(UtilExcel.getCellCreacion("A1", sheet)));

			FileOutputStream out = new FileOutputStream(tempXls);
			wb.write(out);
			out.close();

			return tempXls;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExportarFileException(e);
		}

	}

	public <T> void pintarFilaDatos(T objeto, String celda, XSSFSheet sheet) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ExportarFileException {
			
			Cell cell = UtilExcel.getCellCreacion(celda, sheet);

			TipoCeldaEnum tipoCeldaEnum = tipoCelda(objeto);

			switch (tipoCeldaEnum) {
			case ESTADOENUM:
				cell.setCellValue(EstatusHelper.pintarEstado(((EstadoRegistroEnum) objeto)));

				break;
			case CADENA:

				cell.setCellValue((String) objeto);
				break;

			case FECHA:
				cell.setCellValue(objeto != null ? sf.format((Date) objeto) : "");
				break;

			case HORA:
				cell.setCellValue(objeto != null ? sh.format((Time) objeto) : "");
				break;

			case NUMERO:
				cell.setCellType(CellType.NUMERIC);
				if (objeto instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) objeto).doubleValue());
				}

				if (objeto instanceof Double) {
					cell.setCellValue((Double) objeto);
				}

				if (objeto instanceof Float) {
					cell.setCellValue((Float) objeto);
				}

				if (objeto instanceof Long) {
					cell.setCellValue((Long) objeto);
				}

				if (objeto instanceof Integer) {
					cell.setCellValue((Integer) objeto);
				}

				break;
			default:
				throw new ExportarFileException("No existe un cast al tipo : "+objeto);
			}
			

		}



	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Selecciona el path del archivo de excel
	 * 
	 * @param reportDir
	 * @param jasperFile
	 * @return
	 */
	public String getPahArchivo(String reportDir, String plantilla) {

		return FacesUtil.getServletContext().getRealPath(reportDir + plantilla);

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Determina el tipo de la celda de excel, NUMBER, STRING, DATE
	 * 
	 * @param valor
	 * @return
	 */
	public TipoCeldaEnum tipoCelda(Object valor) {

		if (valor instanceof EstadoRegistroEnum) {
			return TipoCeldaEnum.ESTADOENUM;
		}

		if (valor instanceof java.lang.Number) {
			return TipoCeldaEnum.NUMERO;
		}

		if (valor instanceof java.sql.Time) {
			return TipoCeldaEnum.HORA;
		}

		if (valor instanceof java.util.Date || valor instanceof Timestamp) {
			return TipoCeldaEnum.FECHA;
		}

		return TipoCeldaEnum.CADENA;
	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         M&eacute;todo gen&eacute;rico que extrae el valor de la lista
	 * 
	 * @param clase
	 *            objeto gen&eacute;rico
	 * @param field
	 *            nombre del campo del objeto
	 * @return objeto gen&eacute;rico del objeto seleccionado
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public <T> Object getValorFielMain(T clase, String field) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {

		if (field.contains(".")) {
			String campo = field.substring(0, field.indexOf("."));
			field = field.substring(field.indexOf(".") + 1, field.length());
			Object obj = getValorField(clase, campo);
			if (obj == null) {
				return obj;
			}
			return getValorFielMain(obj, field);
		}

		return getValorField(clase, field);

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Extrae mediante reflection el valor del campo de una clase
	 *         gen&eacute;rica
	 * 
	 * @param clase
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> Object getValorField(T clase, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		Field privateField = clase.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);

		return privateField.get(clase);

	}

	public SimpleDateFormat getSf() {
		return sf;
	}

	public void setSf(SimpleDateFormat sf) {
		this.sf = sf;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getPATH_REPORTE() {
		return PATH_REPORTE;
	}


	public void setPATH_REPORTE(String pATH_REPORTE) {
		PATH_REPORTE = pATH_REPORTE;
	}


}
