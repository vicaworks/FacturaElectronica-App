/**
 * 
 */
package com.servitec.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.servitec.common.util.datamodel.CuentaFtp;
import com.servitec.common.util.exceptions.ErrorFtpUploadFileException;

/**
 * @author cvillarreal
 * 
 */
public class FileUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7016130497910735893L;

	/**
	 * 
	 */
	public FileUtil() {
	}

	/**
	 * 
	 * Creau un archivo temporal a travez de de un {@link InputStream}
	 * 
	 * @param in
	 *            - archivo a ser creado
	 * @return un archivo temporal {@link File}
	 * @throws IOException
	 */
	public static File crearArchivoTemporal(InputStream in) throws IOException {

		File tempFile = File.createTempFile("temporalFile", ".temp");
		FileOutputStream out = new FileOutputStream(tempFile);

		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = in.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}

		in.close();
		out.flush();
		out.close();

		return tempFile;

	}

	/**
	 * 
	 * Carga un archivo a partir de un {@link InputStream} a una cuenta FTP
	 * 
	 * @param in - {@link InputStream} de la carga del archivo
	 * @param ftpDirectorio - si existe un directorio dentro de la cuenta FTP
	 * @param nombreFtp - nombre del archivo en la cuenta FTP
	 * @param serverFtp - servifor ftp 
	 * @param userFtp - usuario de la cunta FTP
	 * @param passFtp - calve de acceso a la cuenta FTP
	 * @throws ErrorFtpUploadFileException 
	 * @throws IOException
	 */
	public static void uploadTempFileFtp(InputStream in, String ftpDirectorio,
			String nombreFtp, String serverFtp, String userFtp, String passFtp)
			throws ErrorFtpUploadFileException, IOException {

		File tempFile = crearArchivoTemporal(in);
		
		FtpUtil.uploadFileFTP(ftpDirectorio, tempFile, serverFtp, userFtp,
				passFtp, nombreFtp);

	}
	
	
	/**
	 * @param nombreArchivo
	 * @param subDirectorio
	 * @param cuentaFtp
	 * @return
	 */
	public static InputStream getInputStreamFtp(String nombreArchivo,String subDirectorio,CuentaFtp cuentaFtp){
		
		FileTransferClient ftp = new FileTransferClient();
		
		try {
			ftp.setRemoteHost(cuentaFtp.getFtpServer());
			ftp.setUserName(cuentaFtp.getFtpUser());
			ftp.setPassword(cuentaFtp.getFtpPassword());
			ftp.connect();
			if (subDirectorio!=null) {
				
				ftp.changeDirectory(subDirectorio);
				
			}
			
			if (!ftp.exists(nombreArchivo)) {
				ftp.disconnect();
				return null;
			}
			
			return new ByteArrayInputStream(ftp.downloadByteArray(nombreArchivo));
			
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				ftp.disconnect();
			} catch (FTPException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
	
	
	public static String getMimeType(String s) {
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

}
