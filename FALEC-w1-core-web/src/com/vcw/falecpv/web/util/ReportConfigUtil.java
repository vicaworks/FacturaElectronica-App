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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;

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
     * @param jasperPrint
     * @param out
     * @param exporterParameters
     * @throws JRException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void exportReportAsExcel(JasperPrint jasperPrint, OutputStream out,
    		SimpleXlsReportConfiguration exporterConfiguration) throws JRException, FileNotFoundException, IOException {
         
        // coding For Excel:
        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        
        // configuraciones
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(true);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(true);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
        xlsReportConfiguration.setMaxRowsPerSheet(10000);
     // ===========================================================
        if(exporterConfiguration!=null) {
        	exporterXLS.setConfiguration(exporterConfiguration);
        }else {
        	exporterXLS.setConfiguration(xlsReportConfiguration);
        }
        exporterXLS.exportReport();
    }
    
    /**
     * @author cristianvillarreal
     * 
     * @param jasperPrint
     * @param out
     * @param exporterConfiguration
     * @throws JRException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void exportReportAsWord(JasperPrint jasperPrint, OutputStream out,
    		SimpleDocxReportConfiguration exporterConfiguration) throws JRException, FileNotFoundException, IOException {
         
    	JRDocxExporter exporter = new JRDocxExporter();
    	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
    	
    	// configuracion
    	SimpleDocxReportConfiguration docxConfiguration = new SimpleDocxReportConfiguration();
    	
    	// ===========================================================
    	if(exporterConfiguration!=null) {
    		exporter.setConfiguration(exporterConfiguration);
        }else {
        	exporter.setConfiguration(docxConfiguration);
        }
    	exporter.setConfiguration(docxConfiguration);
		exporter.exportReport();
        
    }
    
	/**
	 * @author cristianvillarreal
	 * 
	 * @param jasperPrint
	 * @param out
	 * @param exporterConfiguration
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportReportAsPdf(
			JasperPrint jasperPrint, OutputStream out,
			SimplePdfReportConfiguration exporterConfiguration) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
    	
    	// configuracion
    	SimplePdfReportConfiguration pdfConfiguration = new SimplePdfReportConfiguration();
    	
    	// ===========================================================
    	if(exporterConfiguration!=null) {
    		exporter.setConfiguration(exporterConfiguration);
        }else {
        	exporter.setConfiguration(pdfConfiguration);
        }
		exporter.exportReport();

	}
    
	/**
	 * @author cristianvillarreal
	 * 
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportReportAsCsv(
			JasperPrint jasperPrint, OutputStream out,
			SimpleCsvExporterConfiguration exporterConfiguration) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleWriterExporterOutput(out));
		
		// JRExporterParameter
    	SimpleCsvExporterConfiguration csvConfiguration = new SimpleCsvExporterConfiguration();
    	csvConfiguration.setFieldDelimiter(",");
    	
    	
    	// ===========================================================
    	if(exporterConfiguration!=null) {
    		exporter.setConfiguration(exporterConfiguration);
        }else {
        	exporter.setConfiguration(csvConfiguration);
        }
		
		exporter.exportReport();

	}
    
    
	/**
	 * @author cristianvillarreal
	 * 
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportReportAsXml(
			JasperPrint jasperPrint, OutputStream out) throws JRException,
			FileNotFoundException, IOException {
        
        // coding For Excel:
    	JRXmlExporter exporter = new JRXmlExporter();
    	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleXmlExporterOutput(out));
    	
    	//JRExporterParameter
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
	public static void exportReportAsRtf(
			JasperPrint jasperPrint, OutputStream out,
			SimpleRtfExporterConfiguration exporterConfiguration) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRRtfExporter exporter = new JRRtfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleWriterExporterOutput(out));
		
		// confiuguration
    	
    	SimpleRtfExporterConfiguration rtfConfiguration = new SimpleRtfExporterConfiguration();
    	
    	
    	// ===========================================================
    	
    	if(exporterConfiguration!=null) {
    		exporter.setConfiguration(exporterConfiguration);
        }else {
        	exporter.setConfiguration(rtfConfiguration);
        }
		exporter.exportReport();

	}
    
	/**
	 * @author cristianvillarreal
	 * 
	 * @param jasperPrint
	 * @param out
	 * @param exporterParameters
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportReportAsText(
			JasperPrint jasperPrint, OutputStream out,
			SimpleTextExporterConfiguration exporterConfiguration) throws JRException,
			FileNotFoundException, IOException {

		// coding For Excel:
		JRTextExporter exporter = new JRTextExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	exporter.setExporterOutput(new SimpleWriterExporterOutput(out));
		// Configuracion
		SimpleTextExporterConfiguration txtConfiguration = new SimpleTextExporterConfiguration();
		
		
		
		// ===========================================================
    	
    	if(exporterConfiguration!=null) {
    		exporter.setConfiguration(exporterConfiguration);
        }else {
        	exporter.setConfiguration(txtConfiguration);
        }

	}
}
