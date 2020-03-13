package com.vcw.falecpv.web.util;

import java.io.File;
import java.io.IOException;

import com.servitec.common.util.FtpUtil;
import com.servitec.common.util.exceptions.ErrorFtpDownLoadFileException;
import com.servitec.common.util.exceptions.ErrorFtpUploadFileException;

public class FileFtpAppUtil {

	public static enum Tipo {
		FTP, FILE_UTIL
	};

	/**
	 * 
	 * @author wayala
	 * @param tipo
	 *            - si se va a cargar el archivo utilizando ftp o FILEUTIL de
	 *            Apache
	 * @param folderDestiny
	 *            - si existe un subfolder en la cuenta ftp, si el valor es null
	 *            se guarda en la ra�z. o directorio del mismo sitio
	 * @param archivo
	 *            - Archivo de tipo {@link File} que se guarda en el FTP o
	 *            directorio
	 * @param ftpServer
	 *            - Direcci�n IP del servidor FTP
	 * @param ftpUser
	 *            - Usuario del servidor FTP
	 * @param ftpPassword
	 *            - Password del usuario del servidor FTP
	 * @param fileName
	 *            - Nombre del archivo con el que se guarda en la cuenta FTP
	 * @throws ErrorFtpUploadFileException
	 * @throws IOException
	 */
	public static void cargarArchivo(Tipo tipo, String folderDestiny, File archivo, String ftpServer, String ftpUser,
			String ftpPassword, String fileName) throws ErrorFtpUploadFileException, IOException {
		switch (tipo) {
		case FILE_UTIL:
			FileUtilApp.copyFolder(archivo.getAbsolutePath(), folderDestiny);
			break;
		case FTP:
			FtpUtil.uploadFileFTP(folderDestiny, archivo, ftpServer, ftpUser, ftpPassword, fileName);
			break;
		default:
			break;
		}
	}

	/**
	 * @author wayala
	 * @param tipo 
	 * @param sourceFolder - Nombre del subdirectorio de la cuenta FTP, si es null se direcciona a la raiz,
	 * si el tipo es FILE_UTIL, sera el path del archivo que se eliminara
	 * @param ftpServer    - Direcci�n IP del servidor FTP
	 * @param ftpUser - Usuario del servidor FTP
	 * @param ftpPassword - Password del usuario del servidor FTP
	 * @param fileName  - Nombre del archivo con el que se guarda en la cuenta FTP
	 * @throws ErrorFtpDownLoadFileException
	 * @throws IOException 
	 */
	public static void eliminarArchivo(Tipo tipo, String sourceFolder, String ftpServer, String ftpUser,
			String ftpPassword, String fileName) throws ErrorFtpDownLoadFileException, IOException {
		switch (tipo) {
		case FILE_UTIL:
			FileUtilApp.deleteFolder(sourceFolder);
			break;
		case FTP:
			FtpUtil.eliminarFileFTP(sourceFolder, ftpServer, ftpUser, ftpPassword, fileName);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @param tipo
	 * @param source
	 * @param destiny
	 * @param archivo
	 * @param ftpServer
	 * @param ftpUser
	 * @param ftpPassword
	 * @param fileName
	 * @throws IOException
	 * @throws ErrorFtpDownLoadFileException
	 * @throws ErrorFtpUploadFileException
	 */
	public static void reemplazar(Tipo tipo, String source, String destiny, File archivo,String ftpServer, String ftpUser,
			String ftpPassword, String fileName) throws IOException, ErrorFtpDownLoadFileException, ErrorFtpUploadFileException{
		switch (tipo) {
		case FILE_UTIL:
			
			FileUtilApp.copyFolder(source, destiny);
			break;
		case FTP:
			boolean existe = FtpUtil.existeFileSubDirectorio(fileName, destiny, ftpServer, ftpUser, ftpPassword);
			
			if(existe){
				FtpUtil.eliminarFileFTP(destiny, ftpServer, ftpUser, ftpPassword, fileName);
			}
			FtpUtil.uploadFileFTP(destiny, archivo, ftpServer, ftpUser, ftpPassword, fileName);
			
			break;
		default:
			break;
		}
	}
	
}
