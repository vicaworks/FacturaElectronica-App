package com.vcw.falecpv.web.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.servitec.common.util.ValidarParametro;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.web.constante.ExportarFileEnum;
import com.vcw.falecpv.web.util.ReportConfigUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

@SuppressWarnings("deprecation")
public abstract class ReportBaseController {

	public enum TipoDataSorce {
		 
		SQL,COLLECTION;
    }
	
	
    private ExportarFileEnum exportOption;
    private String reportName;
    private String reportDir;
    private TipoDataSorce tipoDataSorce;
    private Connection cnn;
    private String message;
	
	
	/**
	 * 
	 */
	public ReportBaseController() {
		
	}

	
	/**
	 * @param parametrosReporte
	 * @param exporterParameter
	 * @param out
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public <T extends JRExporterParameter,P> void generarReporte(Map<String, Object> parametrosReporte,
			Map<T, Object> exporterParameter,OutputStream out,List<P> dsCollection) throws JRException, FileNotFoundException, IOException,ParametroRequeridoException, URISyntaxException {
		
		ValidarParametro.validar(getReportDir(), "Ditrectorio Reporte");
		ValidarParametro.validar(getReportName(), "Nombre del reporte");
		ValidarParametro.validar(getExportOption(), "Tipo Reporte");
		ValidarParametro.validar(out, "PrintWriter");
		
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext context = (ServletContext) externalContext.getContext();
		File reportFile = new File(ReportConfigUtil.getJasperFilePath(context, getReportDir(), getReportName()));
		
		JasperPrint jasperPrint = null;
		
		switch (tipoDataSorce) {
		case COLLECTION:
			//ValidarParametro.validar(dsCollection, "Lista de Consulta");
			jasperPrint = ReportConfigUtil.fillReportCollection(reportFile, parametrosReporte, dsCollection);
			
			break;

		default:
			ValidarParametro.validar(getCnn(), "Coneccion Base Datos");
			jasperPrint = ReportConfigUtil.fillReportConection(reportFile, parametrosReporte, getCnn());
			
			break;
		}
		
		// export formato
		
		switch (getExportOption()) {
		/*case CSV:
			ReportConfigUtil.exportReportAsCsv(jasperPrint, out, exporterParameter);
			break;*/
		case EXCEL:
			ReportConfigUtil.exportReportAsExcel(jasperPrint, out, exporterParameter);
			break;
		case PDF:
			ReportConfigUtil.exportReportAsPdf(jasperPrint, out, exporterParameter);
			break;
		case WORD:
			ReportConfigUtil.exportReportAsWord(jasperPrint, out, exporterParameter);
			break;
		/*case RTF:
			ReportConfigUtil.exportReportAsRtf(jasperPrint, out, exporterParameter);
			break;
		case TEXT:
			ReportConfigUtil.exportReportAsText(jasperPrint, out, exporterParameter);
			break;
		case XML:
			ReportConfigUtil.exportReportAsXml(jasperPrint, out, exporterParameter);
			break;*/
		default:
			break;
		}
		
	}
	
	/**
	 * @param parametrosReporte
	 * @param exporterParameter
	 * @param out
	 * @throws JRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public <T extends JRExporterParameter,P> void generarReporteExterno(Map<String, Object> parametrosReporte,
			Map<T, Object> exporterParameter,OutputStream out,List<P> dsCollection,String direccionReporte) throws JRException, FileNotFoundException, IOException,ParametroRequeridoException, URISyntaxException {
		
		ValidarParametro.validar(getReportDir(), "Ditrectorio Reporte");
		ValidarParametro.validar(getReportName(), "Nombre del reporte");
		ValidarParametro.validar(getExportOption(), "Tipo Reporte");
		ValidarParametro.validar(out, "PrintWriter");
		
		File reportFile = new File(direccionReporte+getReportDir()+getReportName());
		
		JasperPrint jasperPrint = null;
		
		switch (tipoDataSorce) {
		case COLLECTION:
			//ValidarParametro.validar(dsCollection, "Lista de Consulta");
			jasperPrint = ReportConfigUtil.fillReportCollection(reportFile, parametrosReporte, dsCollection);
			
			break;

		default:
			ValidarParametro.validar(getCnn(), "Coneccion Base Datos");
			jasperPrint = ReportConfigUtil.fillReportConection(reportFile, parametrosReporte, getCnn());
			
			break;
		}
		
		// export formato
		
		switch (getExportOption()) {
		/*case CSV:
			ReportConfigUtil.exportReportAsCsv(jasperPrint, out, exporterParameter);
			break;*/
		case EXCEL:
			ReportConfigUtil.exportReportAsExcel(jasperPrint, out, exporterParameter);
			break;
		case PDF:
			ReportConfigUtil.exportReportAsPdf(jasperPrint, out, exporterParameter);
			break;
		case WORD:
			ReportConfigUtil.exportReportAsWord(jasperPrint, out, exporterParameter);
			break;
		/*case RTF:
			ReportConfigUtil.exportReportAsRtf(jasperPrint, out, exporterParameter);
			break;
		case TEXT:
			ReportConfigUtil.exportReportAsText(jasperPrint, out, exporterParameter);
			break;
		case XML:
			ReportConfigUtil.exportReportAsXml(jasperPrint, out, exporterParameter);
			break;*/
		default:
			break;
		}
		
	}
	

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}


	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}


	/**
	 * @return the reportDir
	 */
	public String getReportDir() {
		return reportDir;
	}


	/**
	 * @param reportDir the reportDir to set
	 */
	public void setReportDir(String reportDir) {
		this.reportDir = reportDir;
	}


	/**
	 * @return the tipoDataSorce
	 */
	public TipoDataSorce getTipoDataSorce() {
		return tipoDataSorce;
	}


	/**
	 * @param tipoDataSorce the tipoDataSorce to set
	 */
	public void setTipoDataSorce(TipoDataSorce tipoDataSorce) {
		this.tipoDataSorce = tipoDataSorce;
	}

	/**
	 * @return the cnn
	 */
	public Connection getCnn() {
		return cnn;
	}


	/**
	 * @param cnn the cnn to set
	 */
	public void setCnn(Connection cnn) {
		this.cnn = cnn;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	protected String getMimeType(String s) {
		int i = s.lastIndexOf(".");
		if (i > 0 && i < s.length() - 1) {
			String s1 = s.substring(i + 1);
			if (s1.equalsIgnoreCase("amr"))
				return "audio/amr";
			if (s1.equalsIgnoreCase("mid"))
				return "audio/midi";
			if (s1.equalsIgnoreCase("mmf"))
				return "application/vnd.smaf";
			if (s1.equalsIgnoreCase("qcp"))
				return "audio/vnd.qcelp";
			if (s1.equalsIgnoreCase("hqx"))
				return "application/mac-binhex40";
			if (s1.equalsIgnoreCase("cpt"))
				return "application/mac-compactpro";
			if (s1.equalsIgnoreCase("doc"))
				return "application/msword";
			if (s1.equalsIgnoreCase("docx"))
				return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			if (s1.equalsIgnoreCase("jsp"))
				return "application/jsp";
			if (s1.equalsIgnoreCase("oda"))
				return "application/oda";
			if (s1.equalsIgnoreCase("pdf"))
				return "application/pdf";
			if (s1.equalsIgnoreCase("ai"))
				return "application/postscript";
			if (s1.equalsIgnoreCase("eps"))
				return "application/postscript";
			if (s1.equalsIgnoreCase("ps"))
				return "application/postscript";
			if (s1.equalsIgnoreCase("ppt"))
				return "application/powerpoint";
			if (s1.equalsIgnoreCase("rtf"))
				return "application/rtf";
			if (s1.equalsIgnoreCase("bcpio"))
				return "application/x-bcpio";
			if (s1.equalsIgnoreCase("vcd"))
				return "application/x-cdlink";
			if (s1.equalsIgnoreCase("Z"))
				return "application/x-compress";
			if (s1.equalsIgnoreCase("cpio"))
				return "application/x-cpio";
			if (s1.equalsIgnoreCase("csh"))
				return "application/x-csh";
			if (s1.equalsIgnoreCase("dcr"))
				return "application/x-director";
			if (s1.equalsIgnoreCase("dir"))
				return "application/x-director";
			if (s1.equalsIgnoreCase("dxr"))
				return "application/x-director";
			if (s1.equalsIgnoreCase("dvi"))
				return "application/x-dvi";
			if (s1.equalsIgnoreCase("gtar"))
				return "application/x-gtar";
			if (s1.equalsIgnoreCase("gz"))
				return "application/x-gzip";
			if (s1.equalsIgnoreCase("hdf"))
				return "application/x-hdf";
			if (s1.equalsIgnoreCase("cgi"))
				return "application/x-httpd-cgi";
			if (s1.equalsIgnoreCase("jnlp"))
				return "application/x-java-jnlp-file";
			if (s1.equalsIgnoreCase("skp"))
				return "application/x-koan";
			if (s1.equalsIgnoreCase("skd"))
				return "application/x-koan";
			if (s1.equalsIgnoreCase("skt"))
				return "application/x-koan";
			if (s1.equalsIgnoreCase("skm"))
				return "application/x-koan";
			if (s1.equalsIgnoreCase("latex"))
				return "application/x-latex";
			if (s1.equalsIgnoreCase("mif"))
				return "application/x-mif";
			if (s1.equalsIgnoreCase("nc"))
				return "application/x-netcdf";
			if (s1.equalsIgnoreCase("cdf"))
				return "application/x-netcdf";
			if (s1.equalsIgnoreCase("sh"))
				return "application/x-sh";
			if (s1.equalsIgnoreCase("shar"))
				return "application/x-shar";
			if (s1.equalsIgnoreCase("sit"))
				return "application/x-stuffit";
			if (s1.equalsIgnoreCase("sv4cpio"))
				return "application/x-sv4cpio";
			if (s1.equalsIgnoreCase("sv4crc"))
				return "application/x-sv4crc";
			if (s1.equalsIgnoreCase("tar"))
				return "application/x-tar";
			if (s1.equalsIgnoreCase("tcl"))
				return "application/x-tcl";
			if (s1.equalsIgnoreCase("tex"))
				return "application/x-tex";
			if (s1.equalsIgnoreCase("textinfo"))
				return "application/x-texinfo";
			if (s1.equalsIgnoreCase("texi"))
				return "application/x-texinfo";
			if (s1.equalsIgnoreCase("t"))
				return "application/x-troff";
			if (s1.equalsIgnoreCase("tr"))
				return "application/x-troff";
			if (s1.equalsIgnoreCase("roff"))
				return "application/x-troff";
			if (s1.equalsIgnoreCase("man"))
				return "application/x-troff-man";
			if (s1.equalsIgnoreCase("me"))
				return "application/x-troff-me";
			if (s1.equalsIgnoreCase("ms"))
				return "application/x-troff-ms";
			if (s1.equalsIgnoreCase("ustar"))
				return "application/x-ustar";
			if (s1.equalsIgnoreCase("src"))
				return "application/x-wais-source";
			if (s1.equalsIgnoreCase("xml"))
				return "text/xml";
			if (s1.equalsIgnoreCase("ent"))
				return "text/xml";
			if (s1.equalsIgnoreCase("cat"))
				return "text/xml";
			if (s1.equalsIgnoreCase("sty"))
				return "text/xml";
			if (s1.equalsIgnoreCase("dtd"))
				return "text/dtd";
			if (s1.equalsIgnoreCase("xsl"))
				return "text/xsl";
			if (s1.equalsIgnoreCase("zip"))
				return "application/zip";
			if (s1.equalsIgnoreCase("au"))
				return "audio/basic";
			if (s1.equalsIgnoreCase("snd"))
				return "audio/basic";
			if (s1.equalsIgnoreCase("mpga"))
				return "audio/mpeg";
			if (s1.equalsIgnoreCase("mp2"))
				return "audio/mpeg";
			if (s1.equalsIgnoreCase("mp3"))
				return "audio/mpeg";
			if (s1.equalsIgnoreCase("aif"))
				return "audio/x-aiff";
			if (s1.equalsIgnoreCase("aiff"))
				return "audio/x-aiff";
			if (s1.equalsIgnoreCase("aifc"))
				return "audio/x-aiff";
			if (s1.equalsIgnoreCase("ram"))
				return "audio/x-pn-realaudio";
			if (s1.equalsIgnoreCase("rpm"))
				return "audio/x-pn-realaudio-plugin";
			if (s1.equalsIgnoreCase("ra"))
				return "audio/x-realaudio";
			if (s1.equalsIgnoreCase("wav"))
				return "audio/x-wav";
			if (s1.equalsIgnoreCase("pdb"))
				return "chemical/x-pdb";
			if (s1.equalsIgnoreCase("xyz"))
				return "chemical/x-pdb";
			if (s1.equalsIgnoreCase("gif"))
				return "image/gif";
			if (s1.equalsIgnoreCase("ief"))
				return "image/ief";
			if (s1.equalsIgnoreCase("jpeg"))
				return "image/jpeg";
			if (s1.equalsIgnoreCase("jpg"))
				return "image/jpeg";
			if (s1.equalsIgnoreCase("jpe"))
				return "image/jpeg";
			if (s1.equalsIgnoreCase("png"))
				return "image/png";
			if (s1.equalsIgnoreCase("tiff"))
				return "image/tiff";
			if (s1.equalsIgnoreCase("tif"))
				return "image/tiff";
			if (s1.equalsIgnoreCase("ras"))
				return "image/x-cmu-raster";
			if (s1.equalsIgnoreCase("pnm"))
				return "image/x-portable-anymap";
			if (s1.equalsIgnoreCase("pbm"))
				return "image/x-portable-bitmap";
			if (s1.equalsIgnoreCase("pgm"))
				return "image/x-portable-graymap";
			if (s1.equalsIgnoreCase("ppm"))
				return "image/x-portable-pixmap";
			if (s1.equalsIgnoreCase("rgb"))
				return "image/x-rgb";
			if (s1.equalsIgnoreCase("xbm"))
				return "image/x-xbitmap";
			if (s1.equalsIgnoreCase("xpm"))
				return "image/x-xpixmap";
			if (s1.equalsIgnoreCase("xwd"))
				return "image/x-xwindowdump";
			if (s1.equalsIgnoreCase("html"))
				return "text/html";
			if (s1.equalsIgnoreCase("htm"))
				return "text/html";
			if (s1.equalsIgnoreCase("txt"))
				return "text/plain";
			if (s1.equalsIgnoreCase("rtx"))
				return "text/richtext";
			if (s1.equalsIgnoreCase("tsv"))
				return "text/tab-separated-values";
			if (s1.equalsIgnoreCase("etx"))
				return "text/x-setext";
			if (s1.equalsIgnoreCase("sgml"))
				return "text/x-sgml";
			if (s1.equalsIgnoreCase("sgm"))
				return "text/x-sgml";
			if (s1.equalsIgnoreCase("mpeg"))
				return "video/mpeg";
			if (s1.equalsIgnoreCase("mpg"))
				return "video/mpeg";
			if (s1.equalsIgnoreCase("mpe"))
				return "video/mpeg";
			if (s1.equalsIgnoreCase("qt"))
				return "video/quicktime";
			if (s1.equalsIgnoreCase("mov"))
				return "video/quicktime";
			if (s1.equalsIgnoreCase("avi"))
				return "video/x-msvideo";
			if (s1.equalsIgnoreCase("movie"))
				return "video/x-sgi-movie";
			if (s1.equalsIgnoreCase("ice"))
				return "x-conference/x-cooltalk";
			if (s1.equalsIgnoreCase("wrl"))
				return "x-world/x-vrml";
			if (s1.equalsIgnoreCase("vrml"))
				return "x-world/x-vrml";
			if (s1.equalsIgnoreCase("wml"))
				return "text/vnd.wap.wml";
			if (s1.equalsIgnoreCase("wmlc"))
				return "application/vnd.wap.wmlc";
			if (s1.equalsIgnoreCase("wmls"))
				return "text/vnd.wap.wmlscript";
			if (s1.equalsIgnoreCase("wmlsc"))
				return "application/vnd.wap.wmlscriptc";
			if (s1.equalsIgnoreCase("wbmp"))
				return "image/vnd.wap.wbmp";
			if (s1.equalsIgnoreCase("css"))
				return "text/css";
			if (s1.equalsIgnoreCase("jad"))
				return "text/vnd.sun.j2me.app-descriptor";
			if (s1.equalsIgnoreCase("jar"))
				return "application/java-archive";
			if (s1.equalsIgnoreCase("3gp"))
				return "video/3gp";
			if (s1.equalsIgnoreCase("3g2"))
				return "video/3gpp2";
			if (s1.equalsIgnoreCase("mp4"))
				return "video/3gpp";
		}
		return "application/octet-stream";
	}


	/**
	 * @return the exportOption
	 */
	public ExportarFileEnum getExportOption() {
		return exportOption;
	}


	/**
	 * @param exportOption the exportOption to set
	 */
	public void setExportOption(ExportarFileEnum exportOption) {
		this.exportOption = exportOption;
	}
}
