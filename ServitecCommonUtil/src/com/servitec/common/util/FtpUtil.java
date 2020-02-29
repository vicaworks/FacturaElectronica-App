/**
 * 
 */
package com.servitec.common.util;

import java.io.File;
import java.io.Serializable;

import com.enterprisedt.net.ftp.FileTransferClient;
import com.servitec.common.util.exceptions.ErrorFtpDownLoadFileException;
import com.servitec.common.util.exceptions.ErrorFtpUploadFileException;

/**
 * @author cvillarreal
 *
 */
public class FtpUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7116524639325558159L;

	/**
	 * 
	 */
	public FtpUtil() {
	}
	
	
	/**
	 * Permite verificar si existe un archivo en un repositorio FTP
	 * 
	 * @param fileName - nombre del archivo
	 * @param host - direccion IP del HOST del FTP
	 * @param user - nombre de usuario del ftp
	 * @param password - clave de acceso al FTP
	 * @return true si existe el archivo en el FTP, false si no existe
	 * 
	 * @author cvillarreal created 21/08/2011
	 * 
	 */
	public static boolean existeFile(String fileName, String host, String user,
			String password) {
		FileTransferClient ftp = new FileTransferClient();
		
		try {
			ftp.setRemoteHost(host);
			ftp.setUserName(user);
			ftp.setPassword(password);

			ftp.connect();

			if (ftp.exists(fileName)) {

				return true;

			}

		} catch (Exception e) {

		} finally {

			try {

				ftp.disconnect();

			} catch (Exception e) {

			}

		}

		return false;
	}

	/**
	 * Verifica si existe un archivo en un FTP que contenga subdirectorios
	 * 
	 * @param fileName - nombre del archivo de busqueda
	 * @param directorio - nombre del directorio en la cuenta FTP
	 * @param host - direcci�n IP del FTP
	 * @param user - nombre de usuario de la cuenta FTP
	 * @param password - clave de acceso a la cuenta FTP
	 * @return true si existe el archivo, false si no existe
	 * 
	 * @author cvillarreal created 21/08/2011
	 */
	public static boolean existeFileSubDirectorio(String fileName,
			String directorio, String host, String user, String password) {
		FileTransferClient ftp = new FileTransferClient();
		try {
			ftp.setRemoteHost(host);
			ftp.setUserName(user);
			ftp.setPassword(password);

			ftp.connect();
			if (directorio!=null){
				ftp.changeDirectory(directorio);
			}

			if (ftp.exists(fileName)) {

				return true;

			}

		} catch (Exception e) {

		} finally {

			try {

				ftp.disconnect();

			} catch (Exception e) {

			}

		}

		return false;
	}
	
	
	/**
	 * 
	 * Permite la carga de un archivo en la cuenta FTP
	 * 
	 * @param ftpFolder - si existe un subfolder en la cuenta ftp, si el valor es null se guarda en la ra�z.
	 * @param archivo - Archivo de tipo {@link File} que se guarda en el FTP
	 * @param ftpServer - Direcci�n IP del servidor FTP
	 * @param ftpUser - Usuario del servidor FTP
	 * @param ftpPassword - Password del usuario del servidor FTP
	 * @param fileName - Nombre del archivo con el que se guarda en la cuenta FTP
	 * 
	 * @throws ErrorFtpUploadFileException Si existe un error al guardar el archivo en la cuenta FTP
	 */
	public static void uploadFileFTP(String ftpFolder, File archivo,
			String ftpServer, String ftpUser, String ftpPassword,
			String fileName) throws ErrorFtpUploadFileException {

		FileTransferClient ftp = new FileTransferClient();
		try {
			ftp.setRemoteHost(ftpServer);
			ftp.setUserName(ftpUser);
			ftp.setPassword(ftpPassword);
			ftp.connect();

			if (ftpFolder != null) {
				ftp.changeDirectory(ftpFolder);
			}

			ftp.uploadFile(archivo.getAbsoluteFile().toString(), fileName);
			ftp.disconnect();
		} catch (Throwable e) {

		}

	}
	
	
	/**
	 * 
	 * Permite consultar el tama�o en bytes de un archivo en la cuenta FTP
	 * 
	 * @param ftpFolder - si existe un subdirectorio en la cuenta FTP, si es null se direcciona a la ra�z de la cuenta.
	 * @param ftpServer - Direcci�n IP del servidor FTP
	 * @param ftpUser - Nombre de usuario de la cuenta FTP
	 * @param ftpPassword - Clave de acceso a la cuenta FTP
	 * @param fileName - Nombre del archivo de la cuenta FTP
	 * @return el tama�o del archivo en bytes 
	 * @throws ErrorFtpDownLoadFileException si existe algun error en la conexi�n, o nombre de archivo
	 */
	public static Long getSizeFileFTP(String ftpFolder,
			String ftpServer, String ftpUser, String ftpPassword,
			String fileName) throws ErrorFtpDownLoadFileException {

		FileTransferClient ftp = new FileTransferClient();
		try {
			ftp.setRemoteHost(ftpServer);
			ftp.setUserName(ftpUser);
			ftp.setPassword(ftpPassword);
			ftp.connect();

			if (ftpFolder != null) {
				ftp.changeDirectory(ftpFolder);
			}
			
			/* verifica si existe el archivo */
			if (!existeFileSubDirectorio(fileName, ftpFolder, ftpServer, ftpUser, ftpPassword)){
				return null;
			}
			
			return ftp.getSize(fileName);
			
		} catch (Throwable e) {
			
			throw new ErrorFtpDownLoadFileException(e);
			
		}finally{
			
			try {
				ftp.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * 
	 * Permite eliminar un archivo en la cuenta FTP 
	 * 
	 * @param ftpFolder - Nombre del subdirectorio de la cuenta FTP, si es null se direcciona a la raiz
	 * @param ftpServer - Ditrecci�n IP de la cuenta FTP
	 * @param ftpUser - Nombre de Usuario de la cuenta FTP
	 * @param ftpPassword - Clave de acceso de la cuenta FTP
	 * @param fileName - Nombre del archivo a ser eliminado
	 * 
	 * @throws ErrorFtpDownLoadFileException si existe un error al concetarseo realizar la eliminaci�n del archivo
	 */
	public static void eliminarFileFTP(String ftpFolder,
			String ftpServer, String ftpUser, String ftpPassword,
			String fileName) throws ErrorFtpDownLoadFileException {

		FileTransferClient ftp = new FileTransferClient();
		try {
			ftp.setRemoteHost(ftpServer);
			ftp.setUserName(ftpUser);
			ftp.setPassword(ftpPassword);
			ftp.connect();

			if (ftpFolder != null) {
				
				ftp.changeDirectory(ftpFolder);
				
			}
			
			ftp.deleteFile(fileName);
			
		} catch (Throwable e) {
			
			throw new ErrorFtpDownLoadFileException(e);
			
		}finally{
			
			try {
				ftp.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 * Permite descargar un archivo de la cuenta FTP
	 * 
	 * @param ftpFolder - Nomre de un subdirectorio en la cuenta FTP, si es null se direcciona a la ra�z
	 * @param ftpServer - Direcci�n IP de la cuenta FTP
	 * @param ftpUser - Nombre de usuario de la cuenta FTP
	 * @param ftpPassword - Clave de acceso de la cuenta FTP
	 * @param fileName - nombre del archivo en la cuenta FTP
	 * @return el archivo en bytes {@link Byte}
	 * @throws ErrorFtpDownLoadFileException si existe un error en la descarga el archivo
	 */
	public static byte[] downLoadFileFTP(String ftpFolder,
			String ftpServer, String ftpUser, String ftpPassword,
			String fileName) throws ErrorFtpDownLoadFileException {

		FileTransferClient ftp = new FileTransferClient();
		try {
			ftp.setRemoteHost(ftpServer);
			ftp.setUserName(ftpUser);
			ftp.setPassword(ftpPassword);
			ftp.connect();

			if (ftpFolder != null) {
				ftp.changeDirectory(ftpFolder);
			}
			
			/* verifica si existe el archivo */
			if (!existeFileSubDirectorio(fileName, ftpFolder, ftpServer, ftpUser, ftpPassword)){
				return null;
			}
			
			return ftp.downloadByteArray(fileName);
			
		} catch (Throwable e) {
			
			throw new ErrorFtpDownLoadFileException(e);
			
		}finally{
			
			try {
				ftp.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}
	
}
