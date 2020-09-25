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

    public XAdESBESSignature(String fileToSign)
    {
        this.fileToSign = fileToSign;
    }

    public static void firmar(String xmlPath, String pathSignature, String passSignature, 
            String pathOut, String nameFileOut) throws CertificateException
    {
        XAdESBESSignature signature = new XAdESBESSignature(xmlPath);
        signature.setPassSignature(passSignature);
        signature.setPathSignature(pathSignature);
        pathFile = pathOut;
        nameFile = nameFileOut;

        signature.execute();
    }

    protected DataToSign createDataToSign()
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

    protected String getSignatureFileName()
    {
        return nameFile;
    }

    protected String getPathOut()
    {
        return pathFile;
    }
}