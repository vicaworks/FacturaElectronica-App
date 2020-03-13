/**
 * 
 */
package com.vcw.falecpv.web.common;


import java.awt.Color;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
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
 * @author cvillarreal
 * 
 *         Generador de reportes en excel
 * 
 * @version 1.0
 *
 */
public class ExportExcelUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6479600627615877890L;

	/**
	 * @author cvillarreal
	 *
	 */
	public enum TipoCeldaEnum {
		NUMERO, FECHA, HORA, CADENA, ESTADOENUM;
	}

	private static Integer FILA_ENCABEZADOS_COLUMNAS = 10;
	private static Integer FILA_DATOS_COLUMNAS = 11;
	private static Integer TOTAL_COLUMNAS = 10;

	private java.awt.Color COLOR_FONDO_CABECERA = new Color(229, 243, 255);
	private java.awt.Color COLOR_BORDE_CABECERA = new Color(0, 92, 184);
	private java.awt.Color COLOR_FUENTE_CABECERA = new Color(0, 0, 0);

	protected final String PATH_REPORTE = "/reportes/common/";
	protected final String PLANTILLA_EXCEL = "FormatoReporteGrilla.xlsx";
	protected final String FORMATO_FECHA = "dd-MM-yyyy";
	protected final String FORMATO_HORA = "HH:mm:ss";

	protected Map<Integer, Map<String, String>> cabecera;
	private List<String> encabezados;
	private List<String> camposObjeto;
	private List<String> footer;
	private List<ExcelEnum> alineacionCelda;
	private SimpleDateFormat sf = new SimpleDateFormat(FORMATO_FECHA);
	private SimpleDateFormat sh = new SimpleDateFormat(FORMATO_HORA);
	private String titulo = "TITULO REPORTE";

	/**
	 * 
	 */
	public ExportExcelUtil() {

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         M&eacute;todo facade para exportar los datos de una lista a una
	 *         plantilla de excel
	 * 
	 * @param lista
	 *            lista gen&eacute;rica de datos a ser exportados a excel
	 * @param nombreFileDescarga
	 *            nombre del archivo para la descarga
	 * @throws Exception
	 *             en caso de existir un error en la construcci&oacute;n del
	 *             excel.
	 */
	public <T> void generarExcelFacade(List<T> lista, String nombreFileDescarga) throws Exception {

		File xls = generarExcel(lista);

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
	 * @author cvillarreal Limpia el Map de encabezado
	 * 
	 */
	public void limpiarEncabezado() {
		cabecera = new HashMap<Integer, Map<String, String>>();
	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Agrega un item al enzabezado
	 * 
	 * @param posicion
	 *            posic&iacute;n del encabezado puede ser valores de 1 a 9
	 * @param label
	 *            La etiqueta del enzabezado si es vacia o null no pinta nada
	 * @param contenido
	 *            el valor de la celda del encabezado siempre debe ser una
	 *            cadena de texto, en caso de ser fecha o valor hay que dar
	 *            formato desde el inicio
	 */
	public void agregarEnzabezado(Integer posicion, String label, String contenido) {

		if (cabecera == null) {
			limpiarEncabezado();
		}

		Map<String, String> labelValor = new HashMap<>();
		labelValor.put(label, contenido);
		cabecera.put(posicion, labelValor);

	}

	/**
	 * @param nombreColumna
	 */
	public void agregarCabeceraColumna(String nombreColumna) {

		if (encabezados == null) {
			encabezados = new ArrayList<>();
		}

		encabezados.add(nombreColumna);

	}

	/**
	 * @param nombreColumna
	 */
	public void agregarFooter(String valorColumna) {

		if (footer == null) {
			footer = new ArrayList<>();
		}

		footer.add(valorColumna);

	}

	/**
	 * @param nombreCampoObjeto
	 */
	public void agregarColumnaObjeto(String nombreCampoObjeto) {
		if (camposObjeto == null) {
			camposObjeto = new ArrayList<>();
		}
		camposObjeto.add(nombreCampoObjeto);
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
	 */
	public void encerar() {
		cabecera = null;
		encabezados = null;
		camposObjeto = null;
		footer = null;
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
	 * 
	 *         M&eacute;todo para generar el excel
	 * 
	 * @param lista
	 *            lista gen&eacute;rica para exportar los datos
	 * @return el archivo de excel
	 * @throws HssException
	 *             en caso de error de la construcci&oacute;n
	 */
	public <T> File generarExcel(List<T> lista) throws ExportarFileException {

		try {

			validar(lista);

			File tempXls = File.createTempFile("plantillaExcel", ".xlsx");
			File template = new File(getPahArchivo(PATH_REPORTE, PLANTILLA_EXCEL));
			FileUtils.copyFile(template, tempXls);

			// @SuppressWarnings("static-access")
			// Workbook wb = new WorkbookFactory().create(new
			// FileInputStream(tempXls));

			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));

			XSSFSheet sheet = wb.getSheetAt(0);

			pintarEncabezado(sheet);
			sheet = wb.getSheetAt(0);
			pintarCabeceraColumnas(sheet);

			int fila = FILA_DATOS_COLUMNAS;
			for (T t : lista) {

				pintarFilaDatos(t, fila, sheet);
				fila++;
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

	public <T> File generarExcelFooter(List<T> lista, int iniColFooter, String name) throws ExportarFileException {

		try {

			validar(lista);

			File tempXls = File.createTempFile(name, ".xlsx");
			File template = new File(getPahArchivo(PATH_REPORTE, PLANTILLA_EXCEL));
			FileUtils.copyFile(template, tempXls);

			// @SuppressWarnings("static-access")
			// Workbook wb = new WorkbookFactory().create(new
			// FileInputStream(tempXls));

			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(tempXls));

			XSSFSheet sheet = wb.getSheetAt(0);

			pintarEncabezado(sheet);
			sheet = wb.getSheetAt(0);
			pintarCabeceraColumnas(sheet);
			Cell cellStyle = UtilExcel.getCellCreacion(10, 1, sheet);
			pintarFooter(FILA_DATOS_COLUMNAS + lista.size(), iniColFooter, footer, cellStyle, sheet);

			int fila = FILA_DATOS_COLUMNAS;
			for (T t : lista) {

				pintarFilaDatos(t, fila, sheet);
				fila++;
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

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Valida los datos antes de exportar a excel
	 * 
	 * @param lista
	 * @throws HssException
	 */
	private <T> void validar(List<T> lista) throws ExportarFileException {

		if (lista == null) {
			throw new ExportarFileException("La lista de datos no pueden estar vacios.");
		}

		if (encabezados == null) {
			throw new ExportarFileException("Los encabezados no pueden estar vacios.");
		}

		if (camposObjeto == null) {
			throw new ExportarFileException("Los campos del objeto no pueden estar vacios.");
		}

		if (alineacionCelda == null) {
			throw new ExportarFileException("La lista de alineacion de cada celda no puede ser vacia.");
		}

		if (encabezados.size() != camposObjeto.size() && encabezados.size() != alineacionCelda.size()) {
			throw new ExportarFileException("El numero de encabezados columnas no puede ser diferente a la cantidad de campos del objeto.");
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
	 * @author cvillarreal
	 * 
	 *         Pinta los datos de la cabecera de la plantilla de excel.
	 * 
	 * @param sheet
	 * @throws ExportarFileException
	 */
	public void pintarEncabezado(XSSFSheet sheet) throws ExportarFileException {

		// cuadra la cabecera del reporte

		int finCabecera = encabezados.size() < TOTAL_COLUMNAS ? TOTAL_COLUMNAS : encabezados.size();

		UtilExcel.borderRangoSinFondo(sheet, 1, FILA_ENCABEZADOS_COLUMNAS - 1, 1, finCabecera, COLOR_BORDE_CABECERA);

		// titulo
		Cell tituloCell = UtilExcel.getCellCreacion("E2", sheet);
		tituloCell.setCellValue(getTitulo());

		// formato del titulo
		UtilExcel.addAlineacionStyle(tituloCell, ExcelEnum.ALINEACION_HORIZONTAL_CENTRO);
		UtilExcel.addVerticalAlineacionStyle(tituloCell, ExcelEnum.ALINEACION_VERTICAL_CENTRO);
		UtilExcel.addFont(tituloCell, true, COLOR_BORDE_CABECERA, (short) 18);

		// encabezados de las columnas

		for (Map.Entry<Integer, Map<String, String>> row : cabecera.entrySet()) {

			Cell cellLabel = null;
			Cell cellValor = null;
			String columnaLabel = "";
			String columnaValor = "";
			int rowPintar = 0;

			for (Map.Entry<String, String> label : row.getValue().entrySet()) {

				if (row.getKey() >= 1 & row.getKey() <= 6) {

					// fila de la 4 - 9 columna B
					columnaLabel = "B";
					columnaValor = "D";
					rowPintar = row.getKey() + 3;

				} else if (row.getKey() >= 7 & row.getKey() <= 12) {
					// fila de la 4 - 9 columna C
					columnaLabel = "G";
					columnaValor = "I";
					rowPintar = row.getKey() - 3;

				} else {
					throw new ExportarFileException("Fila del encabezado erroneo solo [1 al 12]: " + row.getKey());
				}

				if (label.getKey() != null && label.getKey().trim().length() > 0) {
					cellLabel = UtilExcel.getCellCreacion(columnaLabel + rowPintar, sheet);
					UtilExcel.addFont(cellLabel, true, COLOR_FUENTE_CABECERA, (short) 11);

				}
				cellValor = UtilExcel.getCellCreacion(columnaValor + rowPintar, sheet);

				if (cellLabel != null) {
					cellLabel.setCellValue(label.getKey());
				}

				cellValor.setCellValue(label.getValue());

				break;
			}

		}

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Pinta los datos de la cabecera de la platilla de excel
	 * 
	 * @param sheet
	 */
	public void pintarCabeceraColumnas(XSSFSheet sheet) {

		for (int i = 0; i < encabezados.size(); i++) {

			Cell cell = UtilExcel.getCellCreacion(FILA_ENCABEZADOS_COLUMNAS, 1 + i, sheet);

			cell.setCellValue(encabezados.get(i));

			UtilExcel.styleCellFontBorderRellenoColorAlineacion(cell, COLOR_FUENTE_CABECERA, COLOR_BORDE_CABECERA, COLOR_FONDO_CABECERA, ExcelEnum.ALINEACION_HORIZONTAL_CENTRO);

			// setFontCell(cell, sheet);
			// setBorderCell(cell, sheet);

		}

		if (encabezados.size() < TOTAL_COLUMNAS) {
			for (int i = encabezados.size(); i <= (TOTAL_COLUMNAS + 1); i++) {
				Cell cell = UtilExcel.getCellCreacion(FILA_ENCABEZADOS_COLUMNAS, i - 1, sheet);
				// setFontCell(cell, sheet);
				// cell.setCellValue("A" + i);

				UtilExcel.styleCellFontBorderRellenoColorAlineacion(cell, COLOR_FUENTE_CABECERA, COLOR_BORDE_CABECERA, COLOR_FONDO_CABECERA, ExcelEnum.ALINEACION_HORIZONTAL_CENTRO);
			}
		}

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Pobla los datos de la lista gen&eacute;rica en cada una de las
	 *         filas del archivo excel
	 * 
	 * @param objeto
	 *            gen&eacute;rico de donde se extrae los datos
	 * @param fila
	 *            fila del excel en donde se escribe los datos
	 * @param sheet
	 *            hoja del libro de excel seleccionada
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public <T> void pintarFilaDatos(T objeto, int fila, XSSFSheet sheet) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ExportarFileException {

		int col = 1;

		siguiente: for (int i = 0; i < camposObjeto.size(); i++) {

			Object valorObjeto = getValorFielMain(objeto, camposObjeto.get(i));
			Cell cell = UtilExcel.getCellCreacion(fila, col + i, sheet);

			switch (alineacionCelda.get(i)) {
			case ALINEACION_HORIZONTAL_CENTRO:
				UtilExcel.addAlineacionStyle(cell, ExcelEnum.ALINEACION_HORIZONTAL_CENTRO);
				break;
			case ALINEACION_HORIZONTAL_IZQUIERDA:
				UtilExcel.addAlineacionStyle(cell, ExcelEnum.ALINEACION_HORIZONTAL_IZQUIERDA);
				break;
			case ALINEACION_HORIZONTAL_DERECHA:
				UtilExcel.addAlineacionStyle(cell, ExcelEnum.ALINEACION_HORIZONTAL_DERECHA);
				break;
			default:
				break;
			}

			/*
			 * UtilExcel.styleCellFontBorderAlineacion(cell,
			 * COLOR_FUENTE_CONTENIDO, COLOR_FUENTE_CONTENIDO,
			 * ExcelEnum.ALINEACION_HORIZONTAL_DERECHA);
			 */
			// setBorderCell(cell, sheet);

			if (valorObjeto == null)
				continue siguiente;

			TipoCeldaEnum tipoCeldaEnum = tipoCelda(valorObjeto);

			switch (tipoCeldaEnum) {
			case ESTADOENUM:
				cell.setCellValue(EstatusHelper.pintarEstado(((EstadoRegistroEnum) valorObjeto)));

				break;
			case CADENA:

				cell.setCellValue((String) valorObjeto);
				break;

			case FECHA:
				cell.setCellValue(valorObjeto != null ? sf.format((Date) valorObjeto) : "");
				break;

			case HORA:
				cell.setCellValue(valorObjeto != null ? sh.format((Time) valorObjeto) : "");
				break;

			case NUMERO:
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				if (valorObjeto instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) valorObjeto).doubleValue());
				}

				if (valorObjeto instanceof Double) {
					cell.setCellValue((Double) valorObjeto);
				}

				if (valorObjeto instanceof Float) {
					cell.setCellValue((Float) valorObjeto);
				}

				if (valorObjeto instanceof Long) {
					cell.setCellValue((Long) valorObjeto);
				}

				if (valorObjeto instanceof Integer) {
					cell.setCellValue((Integer) valorObjeto);
				}

				break;
			default:
				throw new ExportarFileException("No existe un cast al tipo : " + getValorField(objeto, camposObjeto.get(0)).getClass().getName());

			}

		}

		/*
		 * // completa las columnas de la fila if
		 * (encabezados.size()<TOTAL_COLUMNAS){ for (int i = encabezados.size();
		 * i <= (TOTAL_COLUMNAS + 1) ; i++) { Cell cell =
		 * UtilExcel.getCellCreacion(fila, i-1, sheet);
		 * 
		 * UtilExcel.styleCellFontBorderAlineacion(cell, COLOR_FUENTE_CONTENIDO,
		 * COLOR_FUENTE_CONTENIDO, ExcelEnum.ALINEACION_HORIZONTAL_DERECHA); } }
		 */

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

	public Map<Integer, Map<String, String>> getCabecera() {
		return cabecera;
	}

	public void setCabecera(Map<Integer, Map<String, String>> cabecera) {
		this.cabecera = cabecera;
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

	/**
	 * Metodo auxiliar para pintar los totales de las tablas
	 * 
	 * @param initRow
	 * @param initCol
	 * @param valores
	 * @param cellStyle
	 * @param sheet
	 */
	private void pintarFooter(int initRow, int initCol, List<String> valores, Cell cellStyle, XSSFSheet sheet) {
		for (String valor : valores) {
			Cell cell = UtilExcel.getCellCreacion(initRow, initCol++, sheet);
			cell.setCellValue(valor);
			cell.setCellStyle(cellStyle.getCellStyle());
			UtilExcel.addAlineacionStyle(cell, ExcelEnum.ALINEACION_HORIZONTAL_DERECHA);
		}
	}

}
