/**
 * 
 */
package com.vcw.falecpv.web.util;

import java.awt.Color;
import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.vcw.falecpv.core.constante.ExcelEnum;

/**
 * @author cvillarreal
 * 
 *         Utilitario de excel
 * 
 * @version 1.0
 *
 */
public class UtilExcel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2711974093184049776L;

	/**
	 * 
	 */
	public UtilExcel() {
	}

	/**
	 * 
	 * Consulta la referencia a una celda de excel
	 * 
	 * @param celda
	 *            nombre de la celda
	 * @param sheet
	 *            hoja activa de lectura
	 * @return objeto de referencia a la celda {@link Cell}
	 */
	public static Cell getCell(String celda, Sheet sheet) {
		CellReference cellReference = new CellReference(celda);
		Row row = sheet.getRow(cellReference.getRow());
		return row.getCell(cellReference.getCol());
	}

	/**
	 * Agrega una formula a la celda
	 * 
	 * @author ilobato
	 * @param formula
	 * @param row
	 * @param columna
	 */
	public static void getCellFormula(String formula, Row row, int columna) {
		Cell cell = row.createCell(columna);
		cell.setCellType(CellType.FORMULA);
		cell.setCellFormula(formula);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Refresca las f&oacute;s de un libro de excel
	 * 
	 * @param wb
	 *            {@link Workbook}
	 */
	public static void refrescarFormulasBook(Workbook wb) {
		HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Verifica si la celda existe
	 * 
	 * @param celda
	 *            referencia a {@link Cell}
	 * @param sheet
	 *            hoja de excel {@link Sheet}
	 * @return
	 */
	public static boolean existeCell(String celda, Sheet sheet) {
		CellReference cellReference = new CellReference(celda);
		Row row = sheet.getRow(cellReference.getRow());
		if (row != null) {
			return true;
		}
		return false;
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Verifica si existe la celda si no esta creada la crea
	 * 
	 * @param celda
	 *            referencia a la {@link Cell}
	 * @param sheet
	 *            hoja de c&aacute;lculo {@link Sheet}
	 * @return la referencia a la celda {@link Cell}
	 */
	public static Cell getCellCreacion(String celda, Sheet sheet) {

		CellReference cellReference = new CellReference(celda);
		Row row = sheet.getRow(cellReference.getRow());

		if (row == null) {
			row = sheet.createRow(cellReference.getRow());
		}

		if (row.getCell(cellReference.getCol()) == null) {
			row.createCell(cellReference.getCol(), CellType.BLANK);
		}

		return row.getCell(cellReference.getCol());

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Establece la referencia a una celda por las coordenadas fila,
	 *         columna
	 * 
	 * @param fila
	 *            refrencia a la fila
	 * @param columna
	 *            referencia a la columna
	 * @param sheet
	 *            hoja de c&aacute;lculo {@link Sheet}
	 * @return la celda {@link Cell}
	 */
	public static Cell getCellCreacion(int fila, int columna, Sheet sheet) {

		CellReference cellReference = new CellReference(fila, columna);
		Row row = sheet.getRow(cellReference.getRow());

		if (row == null) {
			row = sheet.createRow(cellReference.getRow());
		}

		if (row.getCell(cellReference.getCol()) == null) {
			row.createCell(cellReference.getCol(), CellType.BLANK);
		}

		return row.getCell(cellReference.getCol());

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Referencia a la celda y especificaci&oacute;n del tipo de dato de
	 *         la celda
	 * 
	 * @param celda
	 *            referencia a la celda
	 * @param sheet
	 *            referencia a la hoja
	 * @param cellType
	 *            tipo de celda
	 * @return {@link Cell}
	 */
	public static Cell getCellCreacion(String celda, Sheet sheet, CellType cellType) {
		Cell cell = getCellCreacion(celda, sheet);
		cell.setCellType(cellType);
		return cell;
	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Aplica un estilo a un rango de celdas
	 * 
	 * @param cellIni
	 *            Fila inicial
	 * @param colIni
	 *            Columna inicial
	 * @param cellFin
	 *            Fila final
	 * @param colFin
	 *            Columna Final
	 * @param sheet
	 *            {@link Sheet}
	 * @param cellStyle
	 *            Estilo {@link CellStyle}
	 */
	public static void applyStyle(int cellIni, char colIni, int cellFin, char colFin, Sheet sheet, CellStyle cellStyle,
			Workbook workbook) {
		String celda;
		for (int i = cellIni; i <= cellFin; i++) {
			for (Character j = colIni; j <= colFin; j++) {
				celda = j.toString() + i;
				Cell cell = getCellCreacion(celda, sheet);
				cell.setCellStyle(cellStyle);
			}
		}
	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Aplica estilo a la celda en:
	 * 
	 *         <ul>
	 *         <li>Color de fuente</li>
	 *         <li>Color de Borde</li<li>Color de fondo de la celda</li>
	 *         <li>Alineaci&oacute;n del texto en la celda</li>
	 *         </ul>
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorFuente
	 *            color de la fuente {@link java.awt.Color}
	 * @param colorBorde
	 *            color del borde {@link java.awt.Color}
	 * @param colorFondo
	 *            color del fondo {@link java.awt.Color}
	 * @param alineacion
	 *            {@link ExcelEnum}
	 */
	public static void styleCellFontBorderRellenoColorAlineacion(Cell cell, Color colorFuente, Color colorBorde,
			Color colorFondo, ExcelEnum alineacion) {

		switch (alineacion) {
		case ALINEACION_HORIZONTAL_IZQUIERDA:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, colorFondo, HorizontalAlignment.LEFT);
			break;
		case ALINEACION_HORIZONTAL_DERECHA:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, colorFondo, HorizontalAlignment.RIGHT);
			break;
		case ALINEACION_HORIZONTAL_CENTRO:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, colorFondo, HorizontalAlignment.CENTER);
			break;
		default:
			throw new IllegalArgumentException("Alineacion incorrecta");
		}

	}

	/**
	 * 
	 * @author cvillarreal
	 * 
	 *         Estilo a la celda para:
	 * 
	 *         <ul>
	 *         <li>Color de fuente</li>
	 *         <li>Color de borde</li>
	 *         <li>Color de fondo</li>
	 *         <li>Alineaci&oacute;n</li>
	 *         </ul>
	 * 
	 * @param cell
	 *            referencia ala celda
	 * @param colorFuente
	 *            Color de la fuente {@link java.awt.Color}
	 * @param colorBorde
	 *            Color del borde de la celda {@link java.awt.Color}
	 * @param colorFondo
	 *            Color del fondo {@link java.awt.Color}
	 * @param alineacion
	 *            alineaci&oacute;n
	 */
	public static void styleCellFontBorderColor(Cell cell, Color colorFuente, Color colorBorde, Color colorFondo,
			HorizontalAlignment alineacion) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();

		font.setBold(true);
		font.setColor(new XSSFColor(colorFuente));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		// style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		// style.setFillBackgroundColor(new XSSFColor(new
		// java.awt.Color(128,0,128)));
		style.setFillForegroundColor(new XSSFColor(colorFondo));

		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(alineacion);
		style.setFont(font);

		cell.setCellStyle(style);

	}
	
	
	/**
	 * @param cell
	 * @param colorFondo
	 * @param alineacion
	 */
	public static void styleCellFondoColor(Cell cell, Color colorFondo,HorizontalAlignment alineacion) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();


		// style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		// style.setFillBackgroundColor(new XSSFColor(new
		// java.awt.Color(128,0,128)));
		if(colorFondo!=null) {
			style.setFillForegroundColor(new XSSFColor(colorFondo));
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		style.setAlignment(alineacion);
		style.setFont(font);

		cell.setCellStyle(style);

	}
	
	

	/**
	 * @author cvillarreal
	 * 
	 *         Aplica el borde abajo, izquierda
	 * 
	 * @param cell
	 *            referencia a la celda {@link Cell}
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderBottomLeft(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde arriba, izquierda
	 * 
	 * @param cell
	 *            referencia a la celda {@link Cell}
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderTopLeft(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color arriba, derecha
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderTopRight(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color abajo, derecha
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderBottomRight(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color abajo
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderBottom(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color arriba
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderTop(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color celda izquierda
	 * 
	 * @param cell
	 *            refrencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderLeft(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		Color borde = new Color(255, 255, 255, 255);

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde y color derecha
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 */
	public static void borderRight(Cell cell, Color colorBorde) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		Color borde = new Color(255, 255, 255, 255);
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Borde de la celda en blanco
	 * 
	 * @param cell
	 *            referencia a la celda
	 */
	public static void borderWhite(Cell cell) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

		Color borde = new Color(255, 255, 255, 255);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(borde));

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Del estilo de una celda clona el estilo de la fuente
	 * 
	 * @param cell
	 *            referencia a celda
	 * @return El estilo de fuente clonado {@link XSSFFont}
	 */
	public static XSSFFont clonarFont(Cell cell) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		XSSFFont fontOld = style.getFont();

		font.setBold(true);;
		XSSFColor color = fontOld.getXSSFColor();
		font.setColor(color);
		font.setFontHeightInPoints(fontOld.getFontHeightInPoints());

		return font;
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Poner borde blanco a un rango de celdas
	 * 
	 * @param sheet
	 *            refrencia a la hoja de excel {@link XSSFSheet}
	 * @param fil_1
	 *            fila inicial del rango
	 * @param fil_2
	 *            fila final del rango
	 * @param col_1
	 *            columna inicial del rango
	 * @param col_2
	 *            columna final del rango
	 * @param color
	 *            {@link java.awt.Color}
	 */
	public static void borderRangoSinFondo(XSSFSheet sheet, int fil_1, int fil_2, int col_1, int col_2, Color color) {

		for (int fil = fil_1; fil <= fil_2; fil++) {

			for (int col = col_1; col <= col_2; col++) {
				// 1. poner en blanco el borde
				UtilExcel.borderWhite(UtilExcel.getCellCreacion(fil, col, sheet));
				if (fil == fil_1 && col == col_1) {
					// esquina top izquierda
					UtilExcel.borderTopLeft(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (fil == fil_1 && col == col_2) {
					// esquina top derecha
					UtilExcel.borderTopRight(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (fil == fil_2 && col == col_1) {
					// esquina bottom izquierda
					UtilExcel.borderBottomLeft(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (fil == fil_2 && col == col_2) {
					// esquina bottom derecha
					UtilExcel.borderBottomRight(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (fil == fil_1) {
					UtilExcel.borderTop(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (fil == fil_2) {
					UtilExcel.borderBottom(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (col == col_1) {
					UtilExcel.borderLeft(UtilExcel.getCellCreacion(fil, col, sheet), color);
				} else if (col == col_2) {
					UtilExcel.borderRight(UtilExcel.getCellCreacion(fil, col, sheet), color);
				}
			}

		}

	}

	/**
	 * @author cvillarreal
	 * 
	 *         A un estilo existente le aplica un estilo de fuente
	 *         <ul>
	 *         <li>BOLD, NORMAL</li>
	 *         <li>Color</li>
	 *         <li>Size</li>
	 *         </ul>
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param bold
	 *            true = BOLD, false= NORMAL
	 * @param color
	 *            {@link java.awt.Color}
	 * @param size
	 */
	public static void addFont(Cell cell, boolean bold, Color color, Short size) {

		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.cloneStyleFrom(cell.getCellStyle());
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontHeightInPoints(size);
		font.setBold(bold);
		font.setColor(new XSSFColor(color));
		style.setFont(font);
		cell.setCellStyle(style);

	}

	/**
	 * @author cvillarreal
	 * 
	 *         A un estilo de celda agrega la alineaci&oacute;n horizontal
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param alineacion
	 *            {@link ExcelEnum}
	 */
	public static void addAlineacionStyle(Cell cell, ExcelEnum alineacion) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.cloneStyleFrom(cell.getCellStyle());

		switch (alineacion) {
		case ALINEACION_HORIZONTAL_IZQUIERDA:
			style.setAlignment(HorizontalAlignment.LEFT);
			break;
		case ALINEACION_HORIZONTAL_DERECHA:
			style.setAlignment(HorizontalAlignment.RIGHT);
			break;
		case ALINEACION_HORIZONTAL_CENTRO:
			style.setAlignment(HorizontalAlignment.CENTER);
			break;
		default:
			throw new IllegalArgumentException("Alineacion incorrecta");
		}

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         A un estilo existente agrega una alineaci&oacute;n vertical
	 * 
	 * @param cell
	 *            refrencia a la celda
	 * @param alineacion
	 *            {@link ExcelEnum}
	 */
	public static void addVerticalAlineacionStyle(Cell cell, ExcelEnum alineacion) {
		Sheet sheet = cell.getSheet();
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.cloneStyleFrom(cell.getCellStyle());

		switch (alineacion) {
		case ALINEACION_VERTICAL_CENTRO:
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			break;
		case ALINEACION_VERTICAL_ARRIBA:
			style.setVerticalAlignment(VerticalAlignment.TOP);
			break;
		case ALINEACION_VERTICAL_ABAJO:
			style.setVerticalAlignment(VerticalAlignment.BOTTOM);
			break;
		default:
			throw new IllegalArgumentException("Alineacion vertical incorrecta");
		}

		cell.setCellStyle(style);
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Aplica un estilo a la celda de; color de fuente, color de borde,
	 *         alineaci&oacute;n
	 * 
	 * @param cell
	 *            referencia a la celda
	 * @param colorFuente
	 *            {@link java.awt.Color}
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 * @param alineacion
	 *            {@link java.awt.Color}
	 */
	public static void styleCellFontBorderAlineacion(Cell cell, Color colorFuente, Color colorBorde,
			ExcelEnum alineacion) {
		switch (alineacion) {
		case ALINEACION_HORIZONTAL_IZQUIERDA:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, HorizontalAlignment.LEFT, false);
			break;
		case ALINEACION_HORIZONTAL_DERECHA:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, HorizontalAlignment.RIGHT, false);
			break;
		case ALINEACION_HORIZONTAL_CENTRO:
			styleCellFontBorderColor(cell, colorFuente, colorBorde, HorizontalAlignment.CENTER, false);
			break;
		default:
			throw new IllegalArgumentException("Alineacion incorrecta");
		}
	}

	/**
	 * @author cvillarreal
	 * 
	 *         Aplica un estilo a la celda en; color de fuente, color de borde,
	 *         alineaci&oacute;n
	 * 
	 * @param cell
	 *            referencia de la celda
	 * @param colorFuente
	 *            {@link java.awt.Color}
	 * @param colorBorde
	 *            {@link java.awt.Color}
	 * @param alineacion
	 *            {@link java.awt.Color}
	 */
	public static void styleCellFontBorderColorAlig(Cell cell, Color colorFuente, Color colorBorde, HorizontalAlignment alineacion,VerticalAlignment alinver, 
			boolean bold) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName("Arial");
		font.setFontHeight(8);
		
		font.setBold(bold);
		font.setColor(new XSSFColor(colorFuente));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setAlignment(alineacion);
		style.setVerticalAlignment(alinver);
		style.setWrapText(true);
		style.setFont(font);

		cell.setCellStyle(style);

	}
	
	public static void styleCellFontBorderColor(Cell cell, Color colorFuente, Color colorBorde, HorizontalAlignment alineacion,
			boolean bold) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();

//		if (bold)
//			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setBold(bold);
		font.setColor(new XSSFColor(colorFuente));

		style.setBorderBottom(BorderStyle.THIN );
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(colorBorde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setAlignment(alineacion);
		style.setFont(font);

		cell.setCellStyle(style);

	}
	
	
	public static void styleCellWraptext(Cell cell, VerticalAlignment alineacion) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.setWrapText(true);
		style.setVerticalAlignment(alineacion);
		
		cell.setCellStyle(style);

	}
	

	/**
	 * Devuelve el XSSFCellStyle para bold
	 * 
	 * @param sheet
	 * @return
	 */
	public static XSSFCellStyle styleBold(Sheet sheet, HorizontalAlignment alineacion) {
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(alineacion);
		return style;
	}

	/**
	 * Aplica estilos en una fila, en todas las celdas que hayan sido creadas
	 * 
	 * @param row
	 * @param colorFuente
	 * @param colorBorde
	 * @param alineacion
	 */
	public static void styleCellFontBorderColorRango(Row row, Color colorFuente, Color colorBorde, HorizontalAlignment alineacion,
			boolean bold) {
		for (int i = 0; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell != null)
				styleCellFontBorderColor(row.getCell(i), colorFuente, colorBorde, alineacion, bold);
		}
	}

	/**
	 * Copia una fila que se encuentra en la posicion rowNumCopy de la
	 * worksheetCopiar, con sus valores y estilos, en otra que va a estar la
	 * posicion rowNumDest de la worksheet
	 * 
	 * @param worksheet
	 * @param rowNumCopy
	 * @param rowNumDest
	 */
	public static void copyRow(Sheet worksheet, Sheet worksheetCopiar, int rowNumCopy, int rowNumDest) {
		Row sourceRow = worksheetCopiar.getRow(rowNumCopy - 1);
		Row destinationRow = worksheet.createRow(rowNumDest - 1);

		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			Cell cellSource = sourceRow.getCell(i);
			if (cellSource != null) {
				// cellSource.getCellStyle().get
				Cell cellDest = destinationRow.createCell(i);

				// Copia el estilo de la celda de origen en la celda de destino
				CellStyle newCellStyle = worksheet.getWorkbook().createCellStyle();
				newCellStyle.cloneStyleFrom(cellSource.getCellStyle());
				cellDest.setCellStyle(newCellStyle);

				// Setea el tipo de celda y el valor correspondiente
				cellDest.setCellType(cellSource.getCellTypeEnum());
				switch (cellSource.getCellTypeEnum()) {
				case BLANK:
					break;
				case BOOLEAN:
					cellDest.setCellValue(cellSource.getBooleanCellValue());
					break;
				case ERROR:
					cellDest.setCellErrorValue(cellSource.getErrorCellValue());
					break;
				case NUMERIC:
					cellDest.setCellValue(cellSource.getNumericCellValue());
					break;
				case STRING:
					cellDest.setCellValue(cellSource.getRichStringCellValue());
					break;
				default:
					break;
				}

				CellRangeAddress cra = existInMergedRegions(worksheetCopiar, sourceRow.getRowNum(),
						cellSource.getColumnIndex());
				if (cra != null && cellDest.getColumnIndex() != cra.getLastColumn()) {
					CellRangeAddress newcra = new CellRangeAddress(destinationRow.getRowNum(),
							destinationRow.getRowNum(), cellDest.getColumnIndex(), cra.getLastColumn());
					worksheet.addMergedRegion(newcra);
				}
			}
		}
	}

	/**
	 * Indica si en una row en especifico de un sheet existe un merge de
	 * columnas y filas, y devuelve ese {@link CellRangeAddress}
	 * 
	 * @param sheet
	 * @param row
	 * @return
	 */
	public static CellRangeAddress existInMergedRegions(Sheet sheet, int row, int col) {
		for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if (range.getFirstRow() <= row && range.getLastRow() >= row && range.getFirstColumn() <= col
					&& range.getLastColumn() >= col)
				return range;
		}
		return null;
	}

	/**
	 * Copia la cantRows de filas que se encuentran a partir de en la posicion
	 * rowNumCopy de la worksheetCopiar, con sus valores y estilos, en otras que
	 * van a estar a partir de la posicion rowNumDest de la worksheet
	 * 
	 * @param worksheet
	 * @param worksheetCopiar
	 * @param rowNumCopy
	 * @param rowNumDest
	 * @param cantRows
	 */
	public static void copyRows(Sheet worksheet, Sheet worksheetCopiar, int rowNumCopy, int rowNumDest, int cantRows) {
		for (int i = 0; i < cantRows; i++) {
			copyRow(worksheet, worksheetCopiar, rowNumCopy + i, rowNumDest + i);
		}
	}

	/**
	 * @param worksheet
	 * @param iniRow
	 * @param nombreAgencia
	 * @param direccion
	 */
	public static int escribirEncabezado(Sheet worksheet, int iniRow, String nombreAgencia, String direccion) {
		XSSFCellStyle style = (XSSFCellStyle) worksheet.getWorkbook().createCellStyle();
		XSSFFont font1 = (XSSFFont) worksheet.getWorkbook().createFont();
		font1.setFontHeight(18);
		font1.setBold(true);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font1);

		XSSFCellStyle style2 = (XSSFCellStyle) worksheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) worksheet.getWorkbook().createFont();
		font.setFontHeight(11);
		style2.setAlignment(HorizontalAlignment.LEFT);
		style2.setFont(font);

		worksheet.createRow(iniRow - 1);
		Row agenciaRow = worksheet.getRow(iniRow - 1);
		Cell cellAgencia = agenciaRow.createCell(1);
		cellAgencia.setCellValue(nombreAgencia);
		cellAgencia.setCellStyle(style);

		String[] direccionTexto = direccion.split("<br>");
		for (String string : direccionTexto) {
			Row direccionRow = worksheet.createRow(iniRow);
			Cell cellDireccion = direccionRow.createCell(1);
			cellDireccion.setCellValue(string.trim());
			cellDireccion.setCellStyle(style2);
			iniRow++;
		}

		return iniRow;
	}
	
	
	/**
	 * @param worksheet
	 * @param iniRow
	 * @param cell
	 * @param nombreAgencia
	 * @param direccion
	 * @return
	 */
	public static int escribirEncabezadoCelda(Sheet worksheet, int iniRow, int cell, String nombreAgencia, String direccion) {
		XSSFCellStyle style = (XSSFCellStyle) worksheet.getWorkbook().createCellStyle();
		XSSFFont font1 = (XSSFFont) worksheet.getWorkbook().createFont();
		font1.setFontHeight(18);
		font1.setBold(true);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font1);

		XSSFCellStyle style2 = (XSSFCellStyle) worksheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) worksheet.getWorkbook().createFont();
		font.setFontHeight(11);
		style2.setAlignment(HorizontalAlignment.LEFT);
		style2.setFont(font);

		worksheet.createRow(iniRow - 1);
		Row agenciaRow = worksheet.getRow(iniRow - 1);
		Cell cellAgencia = agenciaRow.createCell(cell);
		cellAgencia.setCellValue(nombreAgencia);
		cellAgencia.setCellStyle(style);

		String[] direccionTexto = direccion.split("<br>");
		for (String string : direccionTexto) {
			Row direccionRow = worksheet.createRow(iniRow);
			Cell cellDireccion = direccionRow.createCell(cell);
			cellDireccion.setCellValue(string.trim());
			cellDireccion.setCellStyle(style2);
			iniRow++;
		}

		return iniRow;
	}

	/**
	 * @author cvillarreal
	 * 
	 * @param worksheet
	 * @param iniRow
	 * @param finRow
	 * @param iniCol
	 * @param finCol
	 */
	public static void mergeRegion(Sheet worksheet, int iniRow, int finRow, int iniCol, int finCol) {
		worksheet.addMergedRegion(new CellRangeAddress(iniRow, finRow, iniCol, finCol));
	}

	/**
	 * @author vnasimba
	 * @param worksheet
	 * @param cell
	 */
	public static void autoSizeRegion(Sheet worksheet, int cell) {
		worksheet.autoSizeColumn(cell);
	}
	
	

	
	public static void copyRowCell(Sheet worksheet, Sheet worksheetCopiar, int rowNumCopy, int rowNumDest) {
		Row sourceRow = worksheetCopiar.getRow(rowNumCopy - 1);
		Row destinationRow = worksheet.createRow(rowNumDest - 1);

		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			Cell cellSource = sourceRow.getCell(i);
			if (cellSource != null) {
				// cellSource.getCellStyle().get
				Cell cellDest = destinationRow.createCell(i+1);

				// Copia el estilo de la celda de origen en la celda de destino
				CellStyle newCellStyle = worksheet.getWorkbook().createCellStyle();
				newCellStyle.cloneStyleFrom(cellSource.getCellStyle());
				cellDest.setCellStyle(newCellStyle);

				// Setea el tipo de celda y el valor correspondiente
				cellDest.setCellType(cellSource.getCellTypeEnum());
				switch (cellSource.getCellTypeEnum()) {
				case  BLANK:
					break;
				case BOOLEAN:
					cellDest.setCellValue(cellSource.getBooleanCellValue());
					break;
				case ERROR:
					cellDest.setCellErrorValue(cellSource.getErrorCellValue());
					break;
				case NUMERIC:
					cellDest.setCellValue(cellSource.getNumericCellValue());
					break;
				case STRING:
					cellDest.setCellValue(cellSource.getRichStringCellValue());
					break;
				default:
					break;
				}

				CellRangeAddress cra = existInMergedRegions(worksheetCopiar, sourceRow.getRowNum(), cellSource.getColumnIndex());
				if (cra != null && cellDest.getColumnIndex() != cra.getLastColumn()) {
					CellRangeAddress newcra = new CellRangeAddress(destinationRow.getRowNum(), destinationRow.getRowNum(), cellDest.getColumnIndex(), cra.getLastColumn());
					worksheet.addMergedRegion(newcra);
				}
			}
		}
	}
	
	/**
	 * @author vnasimba
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 */
	public static void styleCellFontWrap(Cell cell, String tipoletra, Integer tamanoletra, VerticalAlignment alineacion) {

		Sheet sheet = cell.getSheet();
		
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		style.setFont(font);
		style.setWrapText(true);
		style.setVerticalAlignment(alineacion);
		cell.setCellStyle(style);
	}
	
	public static void styleCellFontWrapXls(Cell cell, String tipoletra, VerticalAlignment alineacion, HorizontalAlignment alineaciontexto) {

		Sheet sheet = cell.getSheet();
		
		HSSFCellStyle style =  (HSSFCellStyle) sheet.getWorkbook().createCellStyle();
		HSSFFont font = (HSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		style.setFont(font);
		style.setWrapText(true);
		style.setAlignment(alineaciontexto);
		style.setVerticalAlignment(alineacion);
		cell.setCellStyle(style);
	}
	
	public static void styleCellFontWrapTamXls(Cell cell, String tipoletra,short numero , VerticalAlignment alineacion, HorizontalAlignment alineaciontexto) {

		Sheet sheet = cell.getSheet();
		
		HSSFCellStyle style =  (HSSFCellStyle) sheet.getWorkbook().createCellStyle();
		HSSFFont font = (HSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeightInPoints((short)numero);
		style.setFont(font);
		style.setWrapText(true);
		style.setAlignment(alineaciontexto);
		style.setVerticalAlignment(alineacion);
		cell.setCellStyle(style);
	}
	
	/**
	 * @author vnasimba
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 * @param iniRow
	 * @param finRow
	 * @param iniCol
	 * @param finCol
	 * @param alineacion
	 */
	public static void styleCellFontWrapMerge(Cell cell, String tipoletra, Integer tamanoletra, int iniRow, int finRow, int iniCol, int finCol, VerticalAlignment alineacion) {

		Sheet sheet = cell.getSheet();
		sheet.addMergedRegion(new CellRangeAddress(iniRow, finRow, iniCol, finCol));
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		style.setFont(font);
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(alineacion);
		cell.setCellStyle(style);
	}
	
	/**
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 */
	public static void styleCellFontSize(Cell cell, String tipoletra, Integer tamanoletra) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		style.setFont(font);
		cell.setCellStyle(style);
	}
	
	/**
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 * @param colorBorde
	 */
	public static void styleCellFontSizeBorderLeft(Cell cell, String tipoletra, Integer tamanoletra, Color colorBorde) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		Color borde = new Color(255, 255, 255, 255);

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));
		
		style.setFont(font);
		
		cell.setCellStyle(style);
	}
	
	/**
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 * @param colorBorde
	 */
	public static void styleCellFontSizeBorderLeftBottom(Cell cell, String tipoletra, Integer tamanoletra, Color colorBorde) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		Color borde = new Color(255, 255, 255, 255);

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(colorBorde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(borde));
		
		style.setFont(font);
		
		cell.setCellStyle(style);
	}
	
	/**
	 * @param cell
	 * @param tipoletra
	 * @param tamanoletra
	 * @param colorBorde
	 * @param alineacion
	 */
	public static void styleCellFontSizeBorderRight(Cell cell, String tipoletra, Integer tamanoletra, Color colorBorde, VerticalAlignment alineacion) {

		Sheet sheet = cell.getSheet();

		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
		font.setFontName(tipoletra);
		font.setFontHeight(tamanoletra);
		Color borde = new Color(255, 255, 255, 255);

		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(new XSSFColor(colorBorde));

		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(new XSSFColor(borde));

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(new XSSFColor(borde));

		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(new XSSFColor(colorBorde));
		
		style.setWrapText(true);
		style.setVerticalAlignment(alineacion);
		style.setFont(font);
		
		cell.setCellStyle(style);
	}


}
