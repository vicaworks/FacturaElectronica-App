/**
 * 
 */
package com.mako.util.firma;

import org.w3c.dom.Document;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.EnumFormatoFirma;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import java.security.cert.CertificateException;

/**
 * @author Jorge
 *
 */
public class XAdESBESSignature extends GenericXMLSignature
{
	private static String nameFile;
    private static String pathFile;
    private String fileToSign;

    public XAdESBESSignature()
    {
    }

    /**
     * @param xmlPath
     * @param pathSignature
     * @param passSignature
     * @param pathOut
     * @param nameFileOut
     * @throws CertificateException
     * @throws FirmaElectronicaException
     */
    public static void firmarFacade(String xmlPath, String pathSignature, String passSignature, 
            String pathOut, String nameFileOut) throws CertificateException, FirmaElectronicaException
    {
        XAdESBESSignature signature = new XAdESBESSignature();
        signature.setFileToSign(xmlPath);
        signature.setPassSignature(passSignature);
        signature.setPathSignature(pathSignature);
        pathFile = pathOut;
        nameFile = nameFileOut;
        signature.execute();
    }
    
    
    /**
     * @author cristianvillarreal
     * 
     * @param xmlToSign
     * @param penFile
     * @param passSignature
     * @return
     * @throws CertificateException
     * @throws FirmaElectronicaException 
     */
    public static byte[] firmarFacade(byte[] xmlToSign,byte[] penSignature,String passSignature)throws CertificateException, FirmaElectronicaException{
    	
    	XAdESBESSignature signature = new XAdESBESSignature();
    	signature.setPassSignature(passSignature);
    	
    	return signature.execute(xmlToSign, penSignature);
    }
    
    
    @Override
    protected DataToSign createDataToSign() throws FirmaElectronicaException
    {
        DataToSign datosAFirmar = new DataToSign();

        datosAFirmar.setXadesFormat(EnumFormatoFirma.XAdES_BES);

        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "contenido comprobante", null, "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");

        Document docToSign = getDocument(this.fileToSign);
        datosAFirmar.setDocument(docToSign);

        return datosAFirmar;
    }
    
    @Override
    protected DataToSign createDataToSign(byte[] xmlToSign) throws FirmaElectronicaException {
    	DataToSign datosAFirmar = new DataToSign();

        datosAFirmar.setXadesFormat(EnumFormatoFirma.XAdES_BES);

        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "contenido comprobante", null, "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");

        Document docToSign = getDocument(xmlToSign);
        datosAFirmar.setDocument(docToSign);

        return datosAFirmar;
    }
    

    protected String getSignatureFileName()
    {
        return nameFile;
    }

    protected String getPathOut()
    {
        return pathFile;
    }

	/**
	 * @return the fileToSign
	 */
	public String getFileToSign() {
		return fileToSign;
	}

	/**
	 * @param fileToSign the fileToSign to set
	 */
	public void setFileToSign(String fileToSign) {
		this.fileToSign = fileToSign;
	}

}