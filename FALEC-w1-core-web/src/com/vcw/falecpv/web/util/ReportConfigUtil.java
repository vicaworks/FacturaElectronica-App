package com.vcw.falecpv.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.servitec.common.util.AppConfiguracion;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

@SuppressWarnings("deprecation")
public class ReportConfigUtil implements Serializable{

	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ReportConfigUtil() {
	}
	
	
	/**
	 * @param context
	 * @param uri
	 */
	private static void setCompileTempDir(ServletContext context, String uri) {
        System.setProperty(AppConfiguracion.getString("dir.compile.report"), context.getRealPath(uri));
    }
    
    /**
     * 
     * Permite compilar el reporte
     * 
     * @param context
     * @param compileDir
     * @param filename
     * @return
     * @throws JRException
     */
    public static boolean compileReport(ServletContext context, String compileDir, String filename) throws JRException {
        String jasperFileName = context.getRealPath(compileDir + filename + ".jasper");
        File jasperFile = new File(jasperFileName);
 
        if (jasperFile.exists()) {
            return true; // jasper file already exists, do not compile again
        }
        try {
            // jasper file has not been constructed yet, so compile the xml file
            setCompileTempDir(context, compileDir);
 
            String xmlFileName = jasperFileName.substring(0, jasperFileName.indexOf(".jasper")) + ".jrxml";
            JasperCompileManager.compileReportToFile(xmlFileName);
 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
	/**
	 * 
	 * Ejecuta el reoorte con una conecc�n de base de datos {@link Connection}
	 * 
	 * @param reportFile - Archivo del reporte {@link File}
	 * @param parameters - Lista de par�metros del reporte
	 * @param conn - Conecci�n de la base de datos {@link Connection}
	 * @return el reporte cejecutado {@link JasperPrint}
	 * @throws JRException
	 */
	public static JasperPrint fillReportConection(File reportFile,
			Map<String, Object> parameters, Connection conn) throws JRException {
		
		parameters.put("BaseDir", reportFile.getParentFile());
		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
				parameters, conn);

		return jasperPrint;
	}
	
	/**
	 * 
	 * Ejecuta el reporte con un DataSource de {@link Collection}
	 * 
	 * @param reportFile - reporte Jasper {@link File} 
	 * @param parameters - lista de parametros del reporte
	 * @param listaObjetos - Lista de Objetos del DataSource
	 * @return el reporte ejecutado {@link JasperPrint}
	 * @throws JRException
	 */
	public static <T> JasperPrint fillReportCollection(File reportFile, Map<String, Object> parameters,List<T> listaObjetos) throws JRException {
		
        //parameters.put("BaseDir", reportFile.getParentFile());
        JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
				parameters, new JRBeanCollectionDataSource(listaObjetos));
 
        return jasperPrint;
    }
    
    /**
     * 
     * Determina el path del sistema del reporte
     * 
     * @param context - contexto del servlet actual {@link ServletContext}
     * @param reportDir - path del directorio de reportes 
     * @param jasperFile - nombre del reporte compilado
     * @return
     */
    public static String getJasperFilePath(ServletContext context, String reportDir, String jasperFile) {
        return context.getRealPath(reportDir + jasperFile);
    }
    
	/**
	 * 
	 * Clase que inserta parametros en los formatos de exporter
	 * 
	 * @param exporter - {@link JRExporter}
	 * @param exporterParameters - Lista de parametros a ser a�adidos {@link JRExporterParameter}
	 */
	@SuppressWarnings("rawtypes")
	private static <T extends JRExporter, M extends JRExporterParameter> void setExporterParameters(
			T exporter, Map<M, Object> exporterParameters) {

		if (exporterParameters!=null){
			
			for (Map.Entry<M, Object> entry : exporterParameters.entrySet()) {
				exporter.setParameter(entry.getKey(), entry.getValue());
			}
			
		}

	}
    
    /**
     * @param jasperPrint
     * @param out
     * @param exporterParameters
     * @throws JRException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static <M extends JRExporterParameter> void exportReportAsExcel(JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException, FileNotFoundException, IOException {
         
        // coding For Excel:
        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterXLS.setParameter(
				JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 10000);
		exporterXLS.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporterXLS
				.setParameter(
						JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
						Boolean.TRUE);
		exporterXLS.setParameter(
				JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);
		
        setExporterParameters(exporterXLS, exporterParameters);
        exporterXLS.exportReport();
        
    }
    
    
    /**
     * @param jasperPrint
     * @param out
     * @param exporterParameters
     * @throws JRException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static <M extends JRExporterParameter> void exportReportAsWord(JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException, FileNotFoundException, IOException {
         
    	JRDocxExporter exporter = new JRDocxExporter();

		// JRExporterParameter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		setExporterParameters(exporter, exporterParameters);
		exporter.exportReport();
        
    }
    
	public static <M extends JRExporterParameter> void exportReportAsPdf(
			JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRPdfExporter exporter = new JRPdfExporter();
		// JRExporterParameter
		exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
		
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, out);
		setExporterParameters(exporter, exporterParameters);
		exporter.exportReport();

	}
    
	/**
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static <M extends JRExporterParameter> void exportReportAsCsv(
			JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRCsvExporter exporter = new JRCsvExporter();

		// JRExporterParameter
		exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRCsvExporterParameter.OUTPUT_STREAM, out);
		exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");

		setExporterParameters(exporter, exporterParameters);
		exporter.exportReport();

	}
    
    
	/**
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static <M extends JRExporterParameter> void exportReportAsXml(
			JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException,
			FileNotFoundException, IOException {
        
        // coding For Excel:
    	JRXmlExporter exporter = new JRXmlExporter();
    	
    	//JRExporterParameter
    	exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
        setExporterParameters(exporter, exporterParameters);
        exporter.exportReport();
        
    }
    
    
    
    /**
     * @param jasperPrint
     * @param out
     * @param exporterParameters
     * @throws JRException
     * @throws FileNotFoundException
     * @throws IOException
     */
	public static <M extends JRExporterParameter> void exportReportAsRtf(
			JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRRtfExporter exporter = new JRRtfExporter();

		// JRExporterParameter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		setExporterParameters(exporter, exporterParameters);
		exporter.exportReport();

	}
    
	/**
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static <M extends JRExporterParameter> void exportReportAsText(
			JasperPrint jasperPrint, OutputStream out,
			Map<M, Object> exporterParameters) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRTextExporter exporter = new JRTextExporter();

		// JRExporterParameter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		setExporterParameters(exporter, exporterParameters);
		exporter.exportReport();

	}
}
